package id.tugasakhir.sistempendeteksiwadaikhasbanjar.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.R
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.adapter.WadaiImageAdapter
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.data.WadaiImage
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.databinding.ActivityDetailDetectionBinding

@Suppress("DEPRECATION")
class DetailDetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDetectionBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var wadaiImageList: ArrayList<WadaiImage>
    private lateinit var adapter: WadaiImageAdapter
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

        setupUI(
            getImageBitmap, getClassName, getExpired, getAbout,
            getTemperature
        )
    }
    private fun init()
    {
        recyclerView = findViewById(R.id.rv_wadai_image)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        wadaiImageList= ArrayList()

        addWadaiImage()

        adapter = WadaiImageAdapter(wadaiImageList)
        recyclerView.adapter = adapter
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
    ) {
        binding.ivDetailImage.setImageBitmap(imageBitmap)
        binding.tvBodyName.text = className
        binding.tvBodyExpired.text = expired
        binding.tvBodyTemperature?.text = temperature
        binding.tvBodyAbout.text = about

        init()

        binding.ibBack?.setOnClickListener {
            val intent = Intent(this@DetailDetectionActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }

        if (className == "Ipau") {
            showAlertDialog()
        }
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

    private fun addWadaiImage()
    {
        wadaiImageList.add(WadaiImage(R.drawable.ipau1))
        wadaiImageList.add(WadaiImage(R.drawable.ipau2))
        wadaiImageList.add(WadaiImage(R.drawable.ipau3))
        wadaiImageList.add(WadaiImage(R.drawable.ipau4))
        wadaiImageList.add(WadaiImage(R.drawable.ipau5))
    }
}