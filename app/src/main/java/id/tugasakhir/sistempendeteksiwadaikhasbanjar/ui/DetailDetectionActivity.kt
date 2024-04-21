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
        setWindowsInsets()

        window.navigationBarColor = resources.getColor(R.color.blue)
        window.statusBarColor = resources.getColor(R.color.blue)
        binding.bvBodyName?.setBlur(this, binding.bvBodyName, 100)

        val getImageBitmap: Bitmap? = intent.getParcelableExtra("imageBitmap")
        val getClassName = intent.getStringExtra("className")
        val getExpired = intent.getStringExtra("expired")
        val getAbout = intent.getStringExtra("about")
        val getTemperature = intent.getStringExtra("temperature")
        val getLinkPostIg = intent.getStringExtra("linkPostIg")

        setupUI(
            getImageBitmap, getClassName, getExpired, getAbout,
            getTemperature, getLinkPostIg
        )
    }
    private fun setWindowsInsets()
    {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_detection_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun setupUI(
        imageBitmap: Bitmap?,
        className: String?,
        expired: String?,
        about: String?,
        temperature: String?,
        linkPostIg: String?,
    ) {
        binding.ivDetailImage.setImageBitmap(imageBitmap)
        binding.tvBodyName.text = className
        binding.tvBodyExpired.text = expired
        binding.tvBodyTemperature?.text = temperature
        binding.tvBodyAbout.text = about

        binding.ibBack?.setOnClickListener {
            val intent = Intent(this@DetailDetectionActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }

        binding.ibInstagram?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkPostIg))
            startActivity(intent)
        }

        if (className == "Ipau") {
            showAlertDialog()
        }
    }
    private fun showAlertDialog()
    {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@DetailDetectionActivity)
        builder
            .setTitle("Alert")
            .setMessage("Maaf, Ipau bukan termasuk wadai khas Banjar")
            .setPositiveButton("Ok") { dialog, which ->
                dialog.dismiss()
            }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}