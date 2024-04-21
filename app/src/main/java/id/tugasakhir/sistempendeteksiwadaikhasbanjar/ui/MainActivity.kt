package id.tugasakhir.sistempendeteksiwadaikhasbanjar.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
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
    private var expired = ""
    private var about = ""
    private var temperature = ""
    private var linkPostIg = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()

        window.navigationBarColor = resources.getColor(R.color.blue)
        window.statusBarColor = resources.getColor(R.color.blue)

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
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA),  100)
        }
    }
    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun handleCameraResult(data: Intent?)
    {
        data?.extras?.get("data")?.let { bitmap ->
            processBitmap(bitmap as Bitmap)
        }
    }
    private fun handleGalleryResult(data: Intent?)
    {
        val uri = data?.data
        uri?.let {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                processBitmap(bitmap)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
        {
            when (requestCode){
                REQUEST_CODE_CAMERA -> handleCameraResult(data)
                REQUEST_CODE_PICK_IMAGE -> handleGalleryResult(data)
            }
        }
    }
    private fun processBitmap(bitmap: Bitmap)
    {
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

        var maxPos = 0
        var maxConfidence = confidences[0]
        val classes = arrayOf(
            "Amparan Tatak", "Bingka", "Bingka Barandam", "Hula Hula", "Ipau", "Kakaraban",
            "Lapis India", "Sarimuka", "Talipuk", "Untuk Untuk",
        )

        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }

        if (maxConfidence >= 0.6)
        {
            classifyImage(classes[maxPos])
        }
        startDetailActivity(bitmap)

        // Releases model resources if no longer used.
        model.close()
    }
    private fun startDetailActivity( bitmap: Bitmap)
    {
        val intent = Intent(this@MainActivity, DetailDetectionActivity::class.java).apply {
            putExtra("imageBitmap", bitmap)
            putExtra("className", className)
            putExtra("expired", expired)
            putExtra("about", about)
            putExtra("temperature", temperature)
            putExtra("linkPostIg", linkPostIg)
        }
        startActivity(intent)
    }
    private fun classifyImage(name: String)
    {
        return when (name) {
            "Amparan Tatak" -> {
                className = "Amparan Tatak"
                expired = ""
                about = ""
                temperature = "°C"
                linkPostIg = ""
            }
            "Bingka" -> {
                className = "Bingka"
                expired = ""
                about = ""
                temperature = "°C"
                linkPostIg = ""
            }
            "Bingka Barandam" -> {
                className = "Bingka Barandam"
                expired = ""
                about = ""
                temperature = "°C"
                linkPostIg = ""
            }
            "Hula Hula" -> {
                className = "Hula Hula"
                expired = ""
                about = ""
                temperature = "°C"
                linkPostIg = ""
            }
            "Ipau" -> {
                className = "Ipau"
                expired = "1 Hari"
                about = getString(R.string.body_about_ipau)
                temperature = "25 °C"
                linkPostIg = "https://www.instagram.com/p/CH7Kq-lL29G/?igsh=MXdrM29rdzY0ZHdvaw=="
            }
            "Lapis India" -> {
                className = "Lapis India"
                expired = ""
                about = ""
                temperature = "°C"
                linkPostIg = ""
            }
            "Sarimuka" -> {
                className = "Sarimuka"
                expired = ""
                about = ""
                temperature = "°C"
                linkPostIg = ""
            }
            "Talipuk" -> {
                className = "Talipuk"
                expired = ""
                about = ""
                temperature = "°C"
                linkPostIg = ""
            }
            else -> {
                className = ""
                expired = ""
                about = ""
                temperature = ""
                linkPostIg = ""
            }
        }
    }
    companion object {
        private const val REQUEST_CODE_CAMERA = 3
        private const val REQUEST_CODE_PICK_IMAGE = 1
    }
}