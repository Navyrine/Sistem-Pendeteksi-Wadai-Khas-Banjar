package id.tugasakhir.sistempendeteksiwadaikhasbanjar.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.data.ClassificationResult
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.ml.ModelQuantized
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DetectionViewModel(application: Application) : AndroidViewModel(application) {
    private val _processingState = MutableLiveData<Boolean>()
    val processingState: LiveData<Boolean> get() = _processingState

    private val _classificationResult = MutableLiveData<ClassificationResult>()
    val classificationResult: LiveData<ClassificationResult> get() = _classificationResult
    private var imageSize = 256

    private val firebaseStorageRef: StorageReference = FirebaseStorage.getInstance().getReference("WadaiGallery")
    private val firebaseDatabaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("ClassificationResult")

    fun processCroppedImage(uri: Uri, contentResolver: ContentResolver) {
        _processingState.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val inputStream: InputStream? = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                if (bitmap != null) {
                    val resizeBitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, true)
                    val result = classificationImage(resizeBitmap)
                    withContext(Dispatchers.Main) {
                        _classificationResult.value = result
                        _processingState.value = false
                    }
                    uploadToFirebase(bitmap, result)
                }
            } catch (e: Exception) {
                _processingState.postValue(false)
            }
        }
    }

    private fun classificationImage(bitmap: Bitmap): ClassificationResult {
        val model = ModelQuantized.newInstance(getApplication())

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)
        val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
        val pixels = IntArray(imageSize * imageSize)
        var pixel = 0

        byteBuffer.order(ByteOrder.nativeOrder())
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (i in 0 until imageSize) {
            for (j in 0 until imageSize) {
                val rgb = pixels[pixel++]
                byteBuffer.putFloat(((rgb shr 16) and 0xFF) / 255f)
                byteBuffer.putFloat(((rgb shr 8) and 0xFF) / 255f)
                byteBuffer.putFloat((rgb and 0xFF) / 255f)
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val confidences = outputFeature0.floatArray

        val classes = arrayOf(
            "Amparan Tatak",
            "Babongko",
            "Bingka",
            "Bingka Barandam",
            "Bubur Gunting",
            "Bukan Wadai Banjar",
            "Bukan Wadai Banjar 1",
            "Bukan Wadai Banjar 2",
            "Dadar Gunting",
            "Gagodoh",
            "Hintalu Karuang",
            "Hula Hula",
            "Ilat Sapi",
            "Ipau",
            "Kararaban",
            "Kikicak",
            "Kokoleh",
            "Lam",
            "Lapis India",
            "Lempeng",
            "Lumpur Surga",
            "Pais",
            "Pundut Nasi",
            "Puteri Selat",
            "Sarimuka",
            "Talipuk",
            "Untuk-Untuk"
        )

        var maxPos = 0
        var maxConfidence = confidences[0]

        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }

        val detectedClassName = classes[maxPos]
        val threshold = 0.1

        val similarItemsWithConfidence = confidences
            .mapIndexed { index, confidence -> Pair(classes[index], confidence) }
            .filter { it.second >= threshold && it.first != detectedClassName }
            .sortedByDescending { it.second }

        val sortedSimilarItems = similarItemsWithConfidence.map { it.first }
        val sortedSimilarConfidences = similarItemsWithConfidence.map { it.second }.toFloatArray()

        Log.d(
            "DetectionViewModel",
            "Detected Class: $detectedClassName, Confidence: $maxConfidence"
        )

        for ((index, item) in sortedSimilarItems.withIndex()) {
            Log.d(
                "DetectionViewModel",
                "Similar Item: $item, Confidence: ${sortedSimilarConfidences[index]}"
            )
        }

        model.close()

        return ClassificationResult(
            classificationResultId = "",
            bitmap,
            detectedClassName,
            sortedSimilarItems,
            sortedSimilarConfidences
        )
    }

    private suspend fun uploadToFirebase(bitmap: Bitmap, result: ClassificationResult) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val imageRef = firebaseStorageRef.child("${System.currentTimeMillis()}.jpg")
        val uploadTask = imageRef.putBytes(data)

        try {
            uploadTask.await()
            val downloadUrl = imageRef.downloadUrl.await().toString()
            val classificationResultRef = firebaseDatabaseRef.push()
            val classificationResultId = classificationResultRef.key
            val classificationData = mapOf(
                "classificationResultId" to classificationResultId,
                "bitmap" to downloadUrl,
                "detectedClassName" to result.detectedClassName,
                "similarItems" to result.similarItems,
                "confidences" to result.confidences.toList()
            )

            firebaseDatabaseRef.child(classificationResultId ?: "")
                .setValue(classificationData).await()

            Log.d("UploadData", "Image uploaded")
        } catch (e: Exception) {
            Log.d("FailedData", "Failed to upload image and save data to Firebase")
        }
    }
}