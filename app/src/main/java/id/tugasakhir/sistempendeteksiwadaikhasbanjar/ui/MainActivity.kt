package id.tugasakhir.sistempendeteksiwadaikhasbanjar.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.R
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.databinding.ActivityMainBinding
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var imageSize = 32
    private var predictName = ""
    private var expired = ""
    private var suhu = ""
    private var about = ""
    private var linkPostIg = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.navigationBarColor = resources.getColor(R.color.blue)
        window.statusBarColor = resources.getColor(R.color.blue)

        binding.btnCamera.setOnClickListener {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) ==  PackageManager.PERMISSION_GRANTED)
            {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 3)
            }
            else
            {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 100)
            }
        }

        binding.btnUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(intent, 1)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 3)
        {
            var bitmap = data?.extras?.get("data") as Bitmap

            val dimension = Math.min(bitmap.width, bitmap.height)
            val intent = Intent(this@MainActivity, DetailDetectionActivity::class.java)

            bitmap = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension)
            bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false)
            classificationImage(bitmap)

            intent.putExtra("imageBitmap", bitmap)
            intent.putExtra("className", predictName)
            intent.putExtra("expired", expired)
            intent.putExtra("about", about)
            intent.putExtra("suhu", suhu)
            intent.putExtra("linkPostIg", linkPostIg)
            startActivity(intent)

        }
        else
        {
            val uri = data?.data
            var bitmap: Bitmap? = null
            val intent = Intent(this@MainActivity, DetailDetectionActivity::class.java)

            if (uri != null)
            {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false)
                    classificationImage(bitmap)

                    intent.putExtra("imageBitmap", bitmap)
                    intent.putExtra("className", predictName)
                    intent.putExtra("expired", expired)
                    intent.putExtra("about", about)
                    intent.putExtra("suhu", suhu)
                    intent.putExtra("linkPostIg", linkPostIg)
                    startActivity(intent)

                }catch (e: IOException){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun classificationImage(bitmap: Bitmap) {
        val model = Model.newInstance(applicationContext)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 32, 32, 3), DataType.FLOAT32)
        val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
        val value = IntArray(imageSize * imageSize)
        var pixel = 0

        byteBuffer.order(ByteOrder.nativeOrder())
        bitmap.getPixels(value, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (i in 0..< imageSize)
        {
            for (j in 0..< imageSize)
            {
                val rgb = value[pixel++]
                byteBuffer.putFloat(((rgb shr 16) and 0xFF) * (1f/ 1f))
                byteBuffer.putFloat(((rgb shr 8) and 0xFF) * (1f / 1f))
                byteBuffer.putFloat((rgb * 0xFF) * (1f / 1f))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val confidences = outputFeature0.floatArray
        var maxPos = 0
        var maxConfidence = 0f
        val classes = arrayOf(
            "Amparan Tatak",
            "Bingka",
            "Bingka Barandam",
            "Hula Hula",
            "Ipau",
            "Kakaraban",
            "Lapis India",
            "Sarimuka",
            "Talipuk",
            "Untuk Untuk",
        )
        val intent = Intent(this@MainActivity, DetailDetectionActivity::class.java)

        for (i in confidences.indices)
        {
            if (confidences[i] > maxConfidence)
            {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        predictName = classes[maxPos]

        when (predictName) {
            "Amparan Tatak" -> {
                expired = "1 Hari"
                about = ""
            }
            "Bingka" -> {
                expired = "1 Hari"
                about = "Buah Pisang"
            }
            "Bingka Barandam" -> {
                expired = "1 Hari"
                about = ""
            }
            "Hula Hula" -> {
                expired = "1 Hari"
                about = ""
            }
            "Ipau" -> {
                expired = "1 Hari"
                about = "Ipau adalah makanan khas dari Banjarmasin, Kalimantan Selatan dan sudah ditetapkan dalam SK Walikota nomor 811/2017. Kue Ipau berwarna putih, berbentuk bulat dengan taburan daging di lapisan atasnya dan memiliki tekstur lembut. Ipau dikenal sebagai kue tradisional yang diolah oleh warga dari keturunan Arab yang berasal dari Kelurahan Antasan Kecil Barat, Banjarmasin Tengah atau di Kampung Arab-nya Banjarmasin. Kue Ipau ada yang disajikan kering dan ada yang basah. Kue Ipau yang basah dikarenakan ditambah siraman kuah santan. Kue ini memiliki cita rasa manis dan gurih dari kuah santan dan taburan daging sapi.\n" +
                        "Sekilas kue ini memiliki tampilan yang mirip dengan Lasagna. Hal ini dikarenakan bentuk kue Ipau yang memiliki banyak lapisan dengan daging di setiap lapisannya. Ipau biasanya dijual di bulan Ramadan dan merupakan salah satu takjil untuk berbuka puasa. Kue Ipau akan dijual dalam bentuk loyang atau potongan kecil.\n"
                suhu = "25 °C"
                linkPostIg = "https://www.instagram.com/p/CH7Kq-lL29G/?igsh=MXdrM29rdzY0ZHdvaw=="
            }
            "Kakaraban" -> {
                expired = "1 Hari"
                about = ""
            }
            "Lapis India" -> {
                expired = "1 Hari"
                about = ""
            }
            "Sarimuka" -> {
                expired = "1 Hari"
                about = ""
            }
            "Talipuk" -> {
                expired = "1 Hari"
                about = ""
            }
            "Untuk Untuk" -> {
                expired = "1 Hari"
                about = ""
            }
            else -> {
                expired = ""
                about = ""
                Toast.makeText(this, "Bukan wadai khas Banjar", Toast.LENGTH_SHORT).show()
            }
        }

        // Releases model resources if no longer used.
        model.close()
    }
}