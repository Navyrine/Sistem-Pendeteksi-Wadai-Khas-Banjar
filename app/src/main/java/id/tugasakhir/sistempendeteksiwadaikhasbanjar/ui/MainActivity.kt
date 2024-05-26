package id.tugasakhir.sistempendeteksiwadaikhasbanjar.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.R
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.databinding.ActivityMainBinding
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.ml.ModelQuantized
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var imageSize = 256
    private var className = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()

        window.navigationBarColor = resources.getColor(R.color.md_theme_primaryContainer)
        window.statusBarColor = resources.getColor(R.color.md_theme_primaryContainer)

        binding.btnCamera.setOnClickListener {
            checkCameraPermission()
        }

        binding.btnUploadImage.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    private fun checkCameraPermission() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 3)
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 100)
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleCameraResult(data: Intent?) {
        data?.extras?.get("data")?.let { bitmap ->
            processBitmap(bitmap as Bitmap)
        }
    }

    private fun handleGalleryResult(data: Intent?) {
        val uri = data?.data
        uri?.let {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                processBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_CAMERA -> handleCameraResult(data)
                REQUEST_CODE_PICK_IMAGE -> handleGalleryResult(data)
            }
        }
    }

    private fun processBitmap(bitmap: Bitmap) {
        val resizedBitmap = ThumbnailUtils.extractThumbnail(bitmap, imageSize, imageSize)
        classificationImage(resizedBitmap)
    }

    private fun classificationImage(bitmap: Bitmap) {
        val model = ModelQuantized.newInstance(applicationContext)

        // Creates inputs for reference.
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

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val confidences = outputFeature0.floatArray

        val classes = arrayOf(
            "Amparan Tatak", "Babongko", "Bingka", "Bingka Barandam", "Bubur Gunting", "Dadar Gunting", "Gagodoh",
            "Hintalu Karuang", "Hula Hula", "Ilat Sapi", "Ipau", "Kararaban", "Kikicak", "Kukulih", "Lam", "Lapis India",
            "Lempeng", "Lumpur Surga", "Pais", "Pundut Nasi", "Puteri Selat", "Sarimuka", "Talipuk", "Untuk-Untuk",
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
        val similarItems: MutableList<String> = ArrayList()
        val similarConfidences: MutableList<Float> = ArrayList()
        val threshold = 0.1
        val thresholdPercent = (threshold * 100).toInt()

        for (i in confidences.indices) {
            val confidenceValue = confidences[i]
            val confidencePercent = (confidenceValue * 100).toInt()
            Log.d("Filtering", "Confidence value (percent): $confidencePercent, Threshold (percent): $thresholdPercent")
            if (confidences[i] >= threshold) {
                similarItems.add(classes[i])
                similarConfidences.add(confidences[i])
            }
        }

        classifyImage(detectedClassName)
        val similarConfidencesArray: FloatArray = similarConfidences.toFloatArray()
        Log.d("FilteredData", "Filtered similarItems: $similarItems")
        Log.d("FilteredData", "Filtered confidences: ${similarConfidencesArray.contentToString()}")

        val similarItemsWithConfidence = similarItems.zip(similarConfidences)
            .sortedByDescending { it.second }
            .toMutableList()

        val sortedSimilarItems = similarItemsWithConfidence.map { it.first }
        val sortedSimilarConfidences = similarItemsWithConfidence.map { it.second }.toFloatArray()

        startResultActivity(bitmap, sortedSimilarItems, sortedSimilarConfidences)

        // Releases model resources if no longer used.
        model.close()
    }

    private fun startResultActivity(
        imageBitmap: Bitmap,
        similarItems: List<String>,
        similarConfidences: FloatArray
    ) {
        val intent = Intent(this@MainActivity, DetectionResultActivity::class.java).apply {
            putExtra("imageBitmap", imageBitmap)
            putExtra("className", className)
            putStringArrayListExtra("similarItems", ArrayList(similarItems))
            putExtra("confidences", similarConfidences)
        }
        startActivity(intent)
    }
    private fun classifyImage(name: String) {
        return when (name) {
            "Amparan Tatak" -> className = "Amparan Tatak"
            "Bingka" -> className = "Bingka"
            "Bingka Barandam" -> className = "Bingka Barandam"
            "Hula Hula" -> className = "Hula Hula"
            "Ipau" -> className = "Ipau"
            "Kararaban" -> className = "Kararaban"
            "Lapis India" -> className = "Lapis India"
            "Sarimuka" -> className = "Sarimuka"
            "Talipuk" -> className = "Talipuk"
            "Untuk-Untuk" -> className = "Untuk-Untuk"
            "Lumpur Surga" -> className = "Lumpur Surga"
            "Kukulih" -> className = "Kukulih"
            "Kikicak" -> className = "Kikicak"
            "Gagodoh" -> className = "Gagodoh"
            "Lam" -> className = "Lam"
            "Hintalu Karuang" -> className = "Hintalu Karuang"
            "Ilat Sapi" -> className = "Ilat Sapi"
            "Dadar Gunting" -> className = "Dadar Gunting"
            "Pais" -> className = "Pais"
            "Babongko" -> className = "Babongko"
            "Bubur Gunting" -> className = "Bubur Gunting"
            "Lempeng" -> className = "Lempeng"
            "Puteri Selat" -> className = "Puteri Selat"
            "Pundut Nasi" -> className = "Pundut Nasi"
            else -> className = ""
        }
    }

    companion object {
        private const val REQUEST_CODE_CAMERA = 3
        private const val REQUEST_CODE_PICK_IMAGE = 1
    }
}