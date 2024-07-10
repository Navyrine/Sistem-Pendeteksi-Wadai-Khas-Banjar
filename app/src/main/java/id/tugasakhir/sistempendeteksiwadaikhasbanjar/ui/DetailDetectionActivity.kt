package id.tugasakhir.sistempendeteksiwadaikhasbanjar.ui

import android.graphics.Bitmap
import android.os.Bundle
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

        window.navigationBarColor = resources.getColor(R.color.md_theme_primaryContainer)
        window.statusBarColor = resources.getColor(R.color.md_theme_primaryContainer)
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
            "Untuk-Untuk" -> addWadaiUntukUntuk()
            "Lumpur Surga" -> addWadaiLumpurSurga()
            "Kokoleh" -> addWadaiKokoleh()
            "Kikicak" -> addWadaiKikicak()
            "Gagodoh" -> addWadaiGagodoh()
            "Kararaban" -> addWadaiKararaban()
            "Lam" -> addWadaiLam()
            "Hintalu Karuang" -> addWadaiHintaluKaruang()
            "Ilat Sapi" -> addWadaiIlatSapi()
            "Dadar Gunting" -> addWadaiDadarGunting()
            "Pais" -> addWadaiPais()
            "Babongko" -> addWadaiBabongko()
            "Bubur Gunting" -> addWadaiBuburGunting()
            "Lempeng" -> addWadaiLempeng()
            "Puteri Selat" -> addWadaiPuteriSelat()
            "Pundut Nasi" -> addWadaiPundutNasi()
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

    private fun addWadaiUntukUntuk()
    {
        wadaiImageList.add(WadaiImage(R.drawable.untuk_untuk1))
        wadaiImageList.add(WadaiImage(R.drawable.untuk_untuk2))
        wadaiImageList.add(WadaiImage(R.drawable.untuk_untuk3))
        wadaiImageList.add(WadaiImage(R.drawable.untuk_untuk4))
        wadaiImageList.add(WadaiImage(R.drawable.untuk_untuk5))
    }
    private fun addWadaiLumpurSurga()
    {
        wadaiImageList.add(WadaiImage(R.drawable.lumpur_surga1))
        wadaiImageList.add(WadaiImage(R.drawable.lumpur_surga2))
        wadaiImageList.add(WadaiImage(R.drawable.lumpur_surga3))
        wadaiImageList.add(WadaiImage(R.drawable.lumpur_surga4))
        wadaiImageList.add(WadaiImage(R.drawable.lumpur_surga5))
    }
    private fun addWadaiKokoleh()
    {
        wadaiImageList.add(WadaiImage(R.drawable.kukulih1))
        wadaiImageList.add(WadaiImage(R.drawable.kukulih2))
        wadaiImageList.add(WadaiImage(R.drawable.kukulih3))
        wadaiImageList.add(WadaiImage(R.drawable.kukulih4))
        wadaiImageList.add(WadaiImage(R.drawable.kukulih5))
    }
    private fun addWadaiKikicak()
    {
        wadaiImageList.add(WadaiImage(R.drawable.kikicak1))
        wadaiImageList.add(WadaiImage(R.drawable.kikicak2))
        wadaiImageList.add(WadaiImage(R.drawable.kikicak3))
        wadaiImageList.add(WadaiImage(R.drawable.kikicak4))
        wadaiImageList.add(WadaiImage(R.drawable.kikicak5))
    }
    private fun addWadaiGagodoh()
    {
        wadaiImageList.add(WadaiImage(R.drawable.gagodoh1))
        wadaiImageList.add(WadaiImage(R.drawable.gagodoh2))
        wadaiImageList.add(WadaiImage(R.drawable.gagodoh3))
        wadaiImageList.add(WadaiImage(R.drawable.gagodoh4))
        wadaiImageList.add(WadaiImage(R.drawable.gagodoh5))
    }
    private fun addWadaiLam()
    {
        wadaiImageList.add(WadaiImage(R.drawable.lam1))
        wadaiImageList.add(WadaiImage(R.drawable.lam2))
        wadaiImageList.add(WadaiImage(R.drawable.lam3))
        wadaiImageList.add(WadaiImage(R.drawable.lam4))
        wadaiImageList.add(WadaiImage(R.drawable.lam5))
    }
    private fun addWadaiHintaluKaruang()
    {
        wadaiImageList.add(WadaiImage(R.drawable.hintalu_karuang1))
        wadaiImageList.add(WadaiImage(R.drawable.hintalu_karuang2))
        wadaiImageList.add(WadaiImage(R.drawable.hintalu_karuang3))
        wadaiImageList.add(WadaiImage(R.drawable.hintalu_karuang4))
        wadaiImageList.add(WadaiImage(R.drawable.hintalu_karuang5))
    }
    private fun addWadaiIlatSapi()
    {
        wadaiImageList.add(WadaiImage(R.drawable.ilat_sapi1))
        wadaiImageList.add(WadaiImage(R.drawable.ilat_sapi2))
        wadaiImageList.add(WadaiImage(R.drawable.ilat_sapi3))
        wadaiImageList.add(WadaiImage(R.drawable.ilat_sapi4))
        wadaiImageList.add(WadaiImage(R.drawable.ilat_sapi5))
    }
    private fun addWadaiDadarGunting()
    {
        wadaiImageList.add(WadaiImage(R.drawable.dadar_gunting1))
        wadaiImageList.add(WadaiImage(R.drawable.dadar_gunting2))
        wadaiImageList.add(WadaiImage(R.drawable.dadar_gunting3))
        wadaiImageList.add(WadaiImage(R.drawable.dadar_gunting4))
        wadaiImageList.add(WadaiImage(R.drawable.dadar_gunting5))
    }
    private fun addWadaiPais()
    {
        wadaiImageList.add(WadaiImage(R.drawable.pais1))
        wadaiImageList.add(WadaiImage(R.drawable.pais2))
        wadaiImageList.add(WadaiImage(R.drawable.pais3))
        wadaiImageList.add(WadaiImage(R.drawable.pais4))
        wadaiImageList.add(WadaiImage(R.drawable.pais5))
    }
    private fun addWadaiBabongko()
    {
        wadaiImageList.add(WadaiImage(R.drawable.babongko1))
        wadaiImageList.add(WadaiImage(R.drawable.babongko2))
        wadaiImageList.add(WadaiImage(R.drawable.babongko3))
        wadaiImageList.add(WadaiImage(R.drawable.babongko4))
        wadaiImageList.add(WadaiImage(R.drawable.babongko5))
    }
    private fun addWadaiBuburGunting()
    {
        wadaiImageList.add(WadaiImage(R.drawable.bubur_gunting1))
        wadaiImageList.add(WadaiImage(R.drawable.bubur_gunting2))
        wadaiImageList.add(WadaiImage(R.drawable.bubur_gunting3))
        wadaiImageList.add(WadaiImage(R.drawable.bubur_gunting4))
        wadaiImageList.add(WadaiImage(R.drawable.bubur_gunting5))
    }
    private fun addWadaiLempeng()
    {
        wadaiImageList.add(WadaiImage(R.drawable.lempeng1))
        wadaiImageList.add(WadaiImage(R.drawable.lempeng2))
        wadaiImageList.add(WadaiImage(R.drawable.lempeng3))
        wadaiImageList.add(WadaiImage(R.drawable.lempeng4))
        wadaiImageList.add(WadaiImage(R.drawable.lempeng5))
    }
    private fun addWadaiPuteriSelat()
    {
        wadaiImageList.add(WadaiImage(R.drawable.putri_selat1))
        wadaiImageList.add(WadaiImage(R.drawable.putri_selat2))
        wadaiImageList.add(WadaiImage(R.drawable.putri_selat3))
        wadaiImageList.add(WadaiImage(R.drawable.putri_selat4))
        wadaiImageList.add(WadaiImage(R.drawable.putri_selat5))
    }
    private fun addWadaiPundutNasi()
    {
        wadaiImageList.add(WadaiImage(R.drawable.pundut_nasi1))
        wadaiImageList.add(WadaiImage(R.drawable.pundut_nasi2))
        wadaiImageList.add(WadaiImage(R.drawable.pundut_nasi3))
        wadaiImageList.add(WadaiImage(R.drawable.pundut_nasi4))
        wadaiImageList.add(WadaiImage(R.drawable.pundut_nasi5))
    }

    private fun addWadaiKararaban()
    {
        wadaiImageList.add(WadaiImage(R.drawable.kakaraban1))
        wadaiImageList.add(WadaiImage(R.drawable.kakaraban2))
        wadaiImageList.add(WadaiImage(R.drawable.kakaraban3))
        wadaiImageList.add(WadaiImage(R.drawable.kakaraban4))
        wadaiImageList.add(WadaiImage(R.drawable.kakaraban5))
    }
}