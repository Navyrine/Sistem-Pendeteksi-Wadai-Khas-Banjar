package id.tugasakhir.sistempendeteksiwadaikhasbanjar.ui

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.R
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.databinding.ActivityCameraBinding
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.utils.FrameOverlayView
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.viewmodel.DetectionViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private lateinit var frameOverlayView: FrameOverlayView
    private lateinit var detectionViewModel: DetectionViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        windowInset()

        val progressBar: LottieAnimationView = binding.progressBar

        frameOverlayView = binding.fvFrameCapture
        detectionViewModel = ViewModelProvider(this).get(DetectionViewModel::class.java)

        binding.ivCloseCaptureImage.setOnClickListener { onBackPressed() }
        binding.ivCaptureImage.setOnClickListener { takePhoto() }

        detectionViewModel.processingState.observe(this, Observer { isProcessing ->
            if (isProcessing) {
                progressBar.visibility = View.VISIBLE
                progressBar.playAnimation()
            } else {
                progressBar.visibility = View.GONE
                progressBar.cancelAnimation()
            }
        })

        detectionViewModel.classificationResult.observe(this, Observer { result ->
            if (result.detectedClassName == "Bukan Wadai Banjar" || result.detectedClassName == "Bukan Wadai Banjar 1" || result.detectedClassName == "Bukan Wadai Banjar 2")
            {
                showAlertDialog()
                return@Observer
            }
            else
            {
                startResultActivity(
                    result.bitmap,
                    result.detectedClassName,
                    result.similarItems,
                    result.confidences
                )
            }
        })
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun windowInset() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(this@CameraActivity, "Gagal memunculkan kamera", Toast.LENGTH_SHORT)
                    .show()
                Log.e(TAG, "startCamera: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        val outputOption = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${outputFileResults.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)

                    outputFileResults.savedUri?.let { uri ->
                        applyBlurEffect(uri, this@CameraActivity, frameOverlayView)
                        detectionViewModel.processCroppedImage(uri, contentResolver)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }
            }
        )
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
            )
        }
        supportActionBar?.hide()
    }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }

                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                imageCapture?.targetRotation = rotation
            }
        }
    }

    private fun applyBlurEffect(
        imageUri: Uri,
        context: Context,
        frameOverlayView: FrameOverlayView
    ) {
        try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(imageUri)
            val exif = ExifInterface(inputStream!!)

            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            val rotatedBitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flipBitmap(
                    bitmap,
                    horizontal = true,
                    vertical = false
                )

                ExifInterface.ORIENTATION_FLIP_VERTICAL -> flipBitmap(
                    bitmap,
                    horizontal = false,
                    vertical = true
                )

                else -> bitmap
            }

            val rotatedWidth = rotatedBitmap.width
            val rotatedHeight = rotatedBitmap.height

            val frameLeftRatio = FrameOverlayView.FRAME_LEFT_RATIO
            val frameRightRatio = FrameOverlayView.FRAME_RIGHT_RATIO
            val frameTopRatio = FrameOverlayView.FRAME_TOP_RATIO
            val frameBottomRatio = FrameOverlayView.FRAME_BOTTOM_RATIO

            val frameLeft = (rotatedWidth * frameLeftRatio).toInt()
            val frameTop = (rotatedHeight * frameTopRatio).toInt()
            val frameRight = (rotatedWidth * frameRightRatio).toInt()
            val frameBottom = (rotatedHeight * frameBottomRatio).toInt()

            val cropLeft = max(frameLeft, 0)
            val cropTop = max(frameTop, 0)
            val cropRight = min(frameRight, rotatedWidth)
            val cropBottom = min(frameBottom, rotatedHeight)

            val croppedBitmap = Bitmap.createBitmap(
                rotatedBitmap, cropLeft, cropTop, cropRight - cropLeft, cropBottom - cropTop
            )

            val outputStream = contentResolver.openOutputStream(imageUri)
            if (outputStream != null) {
                croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
            }

            bitmap.recycle()
            rotatedBitmap.recycle()
            croppedBitmap.recycle()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to crop image: ${e.message}", e)
        }
    }

    private fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun flipBitmap(source: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap {
        val matrix = Matrix()
        matrix.postScale(if (horizontal) -1f else 1f, if (vertical) -1f else 1f)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun startResultActivity(
        imageBitmap: Bitmap,
        detectedClassName: String,
        similarItems: List<String>,
        similarConfidences: FloatArray
    ) {
        val intent = Intent(this@CameraActivity, DetectionResultActivity::class.java).apply {
            putExtra("imageBitmap", imageBitmap)
            putExtra("detectedClassName", detectedClassName)
            putStringArrayListExtra("similarItems", ArrayList(similarItems))
            putExtra("confidences", similarConfidences)
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun showAlertDialog()
    {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)

        val btnOk = dialog.findViewById<Button>(R.id.btn_ok_alert)
        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    companion object {
        private const val TAG = "CameraActivity"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        const val CAMERAX_RESULT = 200
    }
}