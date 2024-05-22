package id.tugasakhir.sistempendeteksiwadaikhasbanjar.ui

import android.app.Dialog
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
        binding.bvResultName.setBlur(this, binding.bvResultName, 100)

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
    private fun init(className: String?)
    {
        recyclerView = findViewById(R.id.rv_wadai_image)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        wadaiImageList= ArrayList()

        when (className) {
            "Ipau" -> addWadaiIpau()
            "Amparan Tatak" -> addWadaiAmparanTatak()
            "Bingka" -> addWadaiBingka()
            "Bingka Barandam" -> addWadaiBingkaBarandam()
            "Hula Hula" -> addWadaiHulaHula()
            "Lapis India" -> addWadaiLapisIndia()
            "Sarimuka" -> addWadaiSarimuka()
            "Talipuk" -> addWadaiTalipuk()
        }

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
        binding.ivResultImage.setImageBitmap(imageBitmap)
        binding.tvResultBodyName.text = className
        binding.tvBodyExpired.text = expired
        binding.tvBodyTemperature.text = temperature
        binding.tvBodyAbout.text = about

        binding.ibBack.setOnClickListener {
           onBackPressed()
        }


        init(className)

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

    private fun addWadaiIpau()
    {
        wadaiImageList.add(WadaiImage(R.drawable.ipau1))
        wadaiImageList.add(WadaiImage(R.drawable.ipau2))
        wadaiImageList.add(WadaiImage(R.drawable.ipau3))
        wadaiImageList.add(WadaiImage(R.drawable.ipau4))
        wadaiImageList.add(WadaiImage(R.drawable.ipau5))
    }
    private fun addWadaiAmparanTatak()
    {
        wadaiImageList.add(WadaiImage(R.drawable.amparan_tatak1))
        wadaiImageList.add(WadaiImage(R.drawable.amparan_tatak2))
        wadaiImageList.add(WadaiImage(R.drawable.amparan_tatak3))
        wadaiImageList.add(WadaiImage(R.drawable.amparan_tatak4))
        wadaiImageList.add(WadaiImage(R.drawable.amparan_tatak5))
    }
    private fun addWadaiBingka()
    {
        wadaiImageList.add(WadaiImage(R.drawable.bingka_kentang))
        wadaiImageList.add(WadaiImage(R.drawable.bingka_kentang2))
        wadaiImageList.add(WadaiImage(R.drawable.bingka_kentang3))
        wadaiImageList.add(WadaiImage(R.drawable.bingka_kentang4))
        wadaiImageList.add(WadaiImage(R.drawable.bingka_kentang5))
    }
    private fun addWadaiBingkaBarandam()
    {
        wadaiImageList.add(WadaiImage(R.drawable.bingka_barandam1))
        wadaiImageList.add(WadaiImage(R.drawable.bingka_barandam2))
        wadaiImageList.add(WadaiImage(R.drawable.bingka_barandam3))
        wadaiImageList.add(WadaiImage(R.drawable.bingka_barandam4))
        wadaiImageList.add(WadaiImage(R.drawable.bingka_barandam5))
    }
    private fun addWadaiHulaHula()
    {
        wadaiImageList.add(WadaiImage(R.drawable.hula_hula1))
        wadaiImageList.add(WadaiImage(R.drawable.hula_hula2))
        wadaiImageList.add(WadaiImage(R.drawable.hula_hula3))
        wadaiImageList.add(WadaiImage(R.drawable.hula_hula4))
        wadaiImageList.add(WadaiImage(R.drawable.hula_hula5))
    }
    private fun addWadaiLapisIndia()
    {
        wadaiImageList.add(WadaiImage(R.drawable.lapis_india1))
        wadaiImageList.add(WadaiImage(R.drawable.lapis_india2))
        wadaiImageList.add(WadaiImage(R.drawable.lapis_india3))
        wadaiImageList.add(WadaiImage(R.drawable.lapis_india4))
        wadaiImageList.add(WadaiImage(R.drawable.lapis_india5))
    }
    private fun addWadaiSarimuka()
    {
        wadaiImageList.add(WadaiImage(R.drawable.sarimuka1))
        wadaiImageList.add(WadaiImage(R.drawable.sarimuka2))
        wadaiImageList.add(WadaiImage(R.drawable.sarimuka3))
        wadaiImageList.add(WadaiImage(R.drawable.sarimuka4))
        wadaiImageList.add(WadaiImage(R.drawable.sarimuka5))
    }
    private fun addWadaiTalipuk()
    {
        wadaiImageList.add(WadaiImage(R.drawable.talipuk1))
        wadaiImageList.add(WadaiImage(R.drawable.talipuk2))
        wadaiImageList.add(WadaiImage(R.drawable.talipuk3))
        wadaiImageList.add(WadaiImage(R.drawable.talipuk4))
        wadaiImageList.add(WadaiImage(R.drawable.talipuk5))
    }
}