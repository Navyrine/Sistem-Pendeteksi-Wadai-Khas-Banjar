package id.tugasakhir.sistempendeteksiwadaikhasbanjar.ui

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.R
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.databinding.ActivityDetailDetectionBinding

@Suppress("DEPRECATION")
class DetailDetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDetectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_detection_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.navigationBarColor = resources.getColor(R.color.blue)
        window.statusBarColor = resources.getColor(R.color.blue)
        binding.bvBodyName?.setBlur(this, binding.bvBodyName, 100)

        val getImageBitmap: Bitmap? = intent.getParcelableExtra("imageBitmap")
        val getNameImage = intent.getStringExtra("className")
        val getExpiredImage = intent.getStringExtra("expired")
        val getAboutImage = intent.getStringExtra("about")
        val getTemperature = intent.getStringExtra("suhu")

        if (getImageBitmap != null)
        {
            binding.ivDetailImage.setImageBitmap(getImageBitmap)
            binding.tvBodyName.text = getNameImage
            binding.tvBodyExpired.text = getExpiredImage
            binding.tvBodyTemperature?.text = getTemperature
//            binding.tvBodyAbout.text = getAboutImage
        }
    }
}