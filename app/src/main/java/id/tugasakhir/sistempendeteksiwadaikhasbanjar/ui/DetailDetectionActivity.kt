package id.tugasakhir.sistempendeteksiwadaikhasbanjar.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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
        val getLinkPostIg = intent.getStringExtra("linkPostIg")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@DetailDetectionActivity)

        binding.ivDetailImage.setImageBitmap(getImageBitmap)
        binding.tvBodyName.text = getNameImage
        binding.tvBodyExpired.text = getExpiredImage
        binding.tvBodyTemperature?.text = getTemperature
        binding.tvBodyAbout.text = getAboutImage

        binding.ibBack?.setOnClickListener {
            val intent = Intent(this@DetailDetectionActivity, MainActivity::class.java)
            intent.flags =  Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(intent)
        }

        binding.ibInstagram?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getLinkPostIg))
            startActivity(intent)
        }

        if (getNameImage == "Ipau") {
            builder
                .setTitle("Alert")
                .setMessage("Maaf, $getNameImage bukan termasuk wadai khas Banjar")
                .setPositiveButton("Ok") { dialog, which ->
                    dialog.dismiss()
                }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }
}