package id.tugasakhir.sistempendeteksiwadaikhasbanjar.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.yalantis.ucrop.UCrop
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.DetectionViewModel
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.R
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.databinding.ActivityMainBinding
import java.io.File

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var detectionViewModel: DetectionViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()

        window.navigationBarColor = resources.getColor(R.color.md_theme_primaryContainer)
        window.statusBarColor = resources.getColor(R.color.md_theme_primaryContainer)

        val progressBar: LottieAnimationView = binding.progressBar
        detectionViewModel = ViewModelProvider(this).get(DetectionViewModel::class.java)

        binding.btnCamera.setOnClickListener {
            if (checkCameraPermission())
            {
               startCamera()
            }
            else
            {
               ActivityCompat.requestPermissions(
                   this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
               )
            }
        }

        binding.btnUploadImage.setOnClickListener {
            pickImageFromGallery()
        }

        detectionViewModel.processingState.observe(this, Observer { isProcessing ->
            if (isProcessing)
            {
                progressBar.visibility = View.VISIBLE
                progressBar.playAnimation()
            }
            else
            {
                progressBar.visibility = View.GONE
                progressBar.cancelAnimation()
            }
        })

        detectionViewModel.classificationResult.observe(this, Observer { result ->
            result?.let {
                startResultActivity(it.bitmap, it.detectedClassName, it.similarItems, it.confidences)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS)
        {
            if (checkCameraPermission())
            {
                startCamera()
            }
            else
            {
                Toast.makeText(this, "Permission not granted by the user", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickImageFromGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null)
        {
            cropImage(uri)
        }
        else
        {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun checkCameraPermission() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun startCamera()
    {
        val intent =Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

    private fun cropImage(sourceUri: Uri)
    {
        val destinationUri = Uri.fromFile(File(cacheDir, "croppedImage.jpg"))
        val options = UCrop.Options().apply {
            setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.md_theme_primaryContainer))
            setStatusBarColor(ContextCompat.getColor(this@MainActivity, R.color.md_theme_primaryContainer))
            setActiveControlsWidgetColor(ContextCompat.getColor(this@MainActivity, R.color.md_theme_primaryContainer))
            setFreeStyleCropEnabled(true)
            setShowCropGrid(true)
            setShowCropFrame(true)
        }
        val uCrop = UCrop.of(sourceUri, destinationUri)
            .withOptions(options)

        cropLauncher.launch(uCrop.getIntent(this))
    }
    private val cropLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            if (resultUri != null) {
                detectionViewModel.processCroppedImage(resultUri, contentResolver)
            } else {
                Log.d("UCrop", "Crop failed: Uri is null")
            }
        } else {
            Log.d("UCrop", "Crop failed")
        }
    }

    private fun startResultActivity(
        imageBitmap: Bitmap,
        detectedClassName: String,
        similarItems: List<String>,
        similarConfidences: FloatArray
    ) {
        val intent = Intent(this@MainActivity, DetectionResultActivity::class.java).apply {
            putExtra("imageBitmap", imageBitmap)
            putExtra("detectedClassName", detectedClassName)
            putStringArrayListExtra("similarItems", ArrayList(similarItems))
            putExtra("confidences", similarConfidences)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}