package id.tugasakhir.sistempendeteksiwadaikhasbanjar.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.R
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.adapter.SimilarItemsAdapter
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.databinding.ActivityDetectionResultBinding

class DetectionResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetectionResultBinding
    private var className = ""
    private var expired = ""
    private var about = ""
    private var temperature = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetectionResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpWindowInset()

        window.navigationBarColor = resources.getColor(R.color.md_theme_primaryContainer)
        window.statusBarColor = resources.getColor(R.color.md_theme_primaryContainer)
        binding.bvResultName.setBlur(this, binding.bvResultName, 100)

        val getImageBitmap: Bitmap? = intent.getParcelableExtra("imageBitmap")
        val getClassName = intent.getStringExtra("className")
        val similarItems = intent.getStringArrayListExtra("similarItems") ?: arrayListOf()
        val confidences = intent.getFloatArrayExtra("confidences") ?: floatArrayOf()

        binding.ivResultImage.setImageBitmap(getImageBitmap)
        binding.tvResultBodyName.text = getClassName
        binding.ibBack.setOnClickListener {
            val intent = Intent(this@DetectionResultActivity, MainActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
        binding.ivResultImage.setOnClickListener {
            val intent = Intent(this@DetectionResultActivity, DetailDetectionActivity::class.java).apply {
                putExtra("className", getClassName)
                putExtra("imageBitmap", getImageBitmap)
                when (getClassName) {
                    "Amparan Tatak" -> {
                        putExtra("expired", "1 Hari")
                        putExtra("about", getString(R.string.body_about_amparan_tatak))
                        putExtra("temperature", "20°C")
                    }
                    "Bingka" -> {
                        putExtra("className", "Bingka")
                        putExtra("expired", "2 sampai 4 Hari")
                        putExtra("about", getString(R.string.body_about_bingka))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Bingka Barandam" -> {
                        putExtra("className", "Bingka Barandam")
                        putExtra("expired", "2 sampai 4 Hari")
                        putExtra("about", getString(R.string.body_about_bingka_berandam))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Hula Hula" -> {
                        putExtra("className", "Hula Hula")
                        putExtra("expired", "3 sampai 5 Hari")
                        putExtra("about", "")
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Ipau" -> {
                        putExtra("className", "Ipau")
                        putExtra("expired", "1 sampai 3 Hari")
                        putExtra("about", getString(R.string.body_about_ipau))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Kararaban" -> {
                        putExtra("className","Kararaban")
                        putExtra("expired", "1 sampai 3 Hari")
                        putExtra("about", getString(R.string.body_about_ipau))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Lapis India" -> {
                        putExtra("className", "Lapis India")
                        putExtra("expired", "3 sampai 7 Hari")
                        putExtra("about", getString(R.string.body_about_lapis_india))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Sarimuka" -> {
                        putExtra("className", "Sarimuka")
                        putExtra("expired", "2 sampai 7 Hari")
                        putExtra("about", getString(R.string.body_about_sarimuka))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Talipuk" -> {
                        putExtra("className", "Talipuk")
                        putExtra("expired", "2 sampai 7 Hari")
                        putExtra("about", getString(R.string.body_about_talipuk))
                        putExtra("temperature", "20°C sampai 4°C")
                    }

                    "Untuk-Untuk" -> {
                        putExtra("className", "Untuk-Untuk")
                        putExtra("expired", "1 sampai 3 Hari")
                        putExtra("about", getString(R.string.body_about_untuk_untuk))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Lumpur Surga" -> {
                        putExtra("className", "Lumpur Surga")
                        putExtra("expired", "1 sampai 3 Hari")
                        putExtra("about", getString(R.string.body_about_lumpur_surga))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Kukulih" -> {
                        putExtra("className", "Kukulih")
                        putExtra("expired", "1 sampai 4 Hari")
                        putExtra("about", getString(R.string.body_about_kukulih))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Kikicak" -> {
                        putExtra("className", "Kikicak")
                        putExtra("expired", "1 sampai 4 Hari")
                        putExtra("about", getString(R.string.body_about_kikicak))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Gagodoh" -> {
                        putExtra("className", "Gagodoh")
                        putExtra("expired", "1 sampai 4 Hari")
                        putExtra("about", getString(R.string.body_about_gagodoh))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Lam" -> {
                        putExtra("className", "Lam")
                        putExtra("expired", "1 sampai 4 Hari")
                        putExtra("about", getString(R.string.body_about_lam))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Hintalu Karuang" -> {
                        putExtra("className", "Hintalu Karuang")
                        putExtra("expired", "1 sampai 4 Hari")
                        putExtra("about", getString(R.string.body_about_hintalu_karuang))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Ilat Sapi" -> {
                        putExtra("className", "Ilat Sapi")
                        putExtra("expired", "1 sampai 4 Hari")
                        putExtra("about", getString(R.string.body_about_ilat_sapi))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Dadar Gunting" -> {
                        putExtra("className", "Dadar Gunting")
                        putExtra("expired", "1 sampai 4 Hari")
                        putExtra("about", getString(R.string.body_about_bubur_gunting))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Pais" -> {
                        putExtra("className", "Pais")
                        putExtra("expired", "1 sampai 4 Hari")
                        putExtra("about", getString(R.string.body_about_pais))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Babongko" -> {
                        putExtra("className", "Babongko")
                        putExtra("expired", "1 sampai 4 Hari")
                        putExtra("about", getString(R.string.body_about_babongko))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Bubur Gunting" -> {
                        putExtra("className","Bubur Gunting")
                        putExtra("expired", "1 sampai 4 Hari")
                        putExtra("about", getString(R.string.body_about_bubur_gunting))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Lempeng" -> {
                        putExtra("className", "Lempeng")
                        putExtra("expired", "1 sampai 4 Hari")
                        putExtra("about", getString(R.string.body_about_lempeng))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Puteri Selat" -> {
                        putExtra("className", "Puteri Selat")
                        putExtra("expired", "1 sampai 4 Hari")
                        putExtra("about", getString(R.string.body_about_puteri_selat))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    "Pundut Nasi" -> {
                        putExtra("className", "Pundut Nasi")
                        putExtra("expired", "1 sampai 4 Hari")
                        putExtra("about", getString(R.string.body_about_pundut_nasi))
                        putExtra("temperature", "20°C sampai 4°C")
                    }
                    else -> {
                        className = ""
                        expired = ""
                        about = ""
                        temperature = ""
                    }
                }
            }
            startActivity(intent)
        }

        val adapter = SimilarItemsAdapter(similarItems, confidences) { item ->
            val intent =
                Intent(this@DetectionResultActivity, DetailDetectionActivity::class.java).apply {
                    putExtra("className", item)
                    putExtra("imageBitmap", getImageBitmap)
                    when (item) {
                        "Amparan Tatak" -> {
                            putExtra("expired", "1 Hari")
                            putExtra("about", getString(R.string.body_about_amparan_tatak))
                            putExtra("temperature", "20°C")
                        }

                        "Bingka" -> {
                            putExtra("className", "Bingka")
                            putExtra("expired", "2 sampai 4 Hari")
                            putExtra("about", getString(R.string.body_about_bingka))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Bingka Barandam" -> {
                            putExtra("className", "Bingka Barandam")
                            putExtra("expired", "2 sampai 4 Hari")
                            putExtra("about", getString(R.string.body_about_bingka_berandam))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Hula Hula" -> {
                            putExtra("className", "Hula Hula")
                            putExtra("expired", "3 sampai 5 Hari")
                            putExtra("about", "")
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Ipau" -> {
                            putExtra("className", "Ipau")
                            putExtra("expired", "1 sampai 3 Hari")
                            putExtra("about", getString(R.string.body_about_ipau))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Kararaban" -> {
                            putExtra("className","Kararaban")
                            putExtra("expired", "1 sampai 3 Hari")
                            putExtra("about", getString(R.string.body_about_ipau))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Lapis India" -> {
                            putExtra("className", "Lapis India")
                            putExtra("expired", "3 sampai 7 Hari")
                            putExtra("about", getString(R.string.body_about_lapis_india))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Sarimuka" -> {
                            putExtra("className", "Sarimuka")
                            putExtra("expired", "2 sampai 7 Hari")
                            putExtra("about", getString(R.string.body_about_sarimuka))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Talipuk" -> {
                            putExtra("className", "Talipuk")
                            putExtra("expired", "2 sampai 7 Hari")
                            putExtra("about", getString(R.string.body_about_talipuk))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Untuk-Untuk" -> {
                            putExtra("className", "Untuk-Untuk")
                            putExtra("expired", "1 sampai 3 Hari")
                            putExtra("about", getString(R.string.body_about_untuk_untuk))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Lumpur Surga" -> {
                            putExtra("className", "Lumpur Surga")
                            putExtra("expired", "1 sampai 3 Hari")
                            putExtra("about", getString(R.string.body_about_lumpur_surga))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Kukulih" -> {
                            putExtra("className", "Kukulih")
                            putExtra("expired", "1 sampai 4 Hari")
                            putExtra("about", getString(R.string.body_about_kukulih))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Kikicak" -> {
                            putExtra("className", "Kikicak")
                            putExtra("expired", "1 sampai 4 Hari")
                            putExtra("about", getString(R.string.body_about_kikicak))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Gagodoh" -> {
                            putExtra("className", "Gagodoh")
                            putExtra("expired", "1 sampai 4 Hari")
                            putExtra("about", getString(R.string.body_about_gagodoh))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Lam" -> {
                            putExtra("className", "Lam")
                            putExtra("expired", "1 sampai 4 Hari")
                            putExtra("about", getString(R.string.body_about_lam))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Hintalu Karuang" -> {
                            putExtra("className", "Hintalu Karuang")
                            putExtra("expired", "1 sampai 4 Hari")
                            putExtra("about", getString(R.string.body_about_hintalu_karuang))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Ilat Sapi" -> {
                            putExtra("className", "Ilat Sapi")
                            putExtra("expired", "1 sampai 4 Hari")
                            putExtra("about", getString(R.string.body_about_ilat_sapi))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Dadar Gunting" -> {
                            putExtra("className", "Dadar Gunting")
                            putExtra("expired", "1 sampai 4 Hari")
                            putExtra("about", getString(R.string.body_about_dadar_gunting))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Pais" -> {
                            putExtra("className", "Pais")
                            putExtra("expired", "1 sampai 4 Hari")
                            putExtra("about", getString(R.string.body_about_pais))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Babongko" -> {
                            putExtra("className", "Babongko")
                            putExtra("expired", "1 sampai 4 Hari")
                            putExtra("about", getString(R.string.body_about_babongko))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Bubur Gunting" -> {
                            putExtra("className","Bubur Gunting")
                            putExtra("expired", "1 sampai 4 Hari")
                            putExtra("about", getString(R.string.body_about_bubur_gunting))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Lempeng" -> {
                            putExtra("className", "Lempeng")
                            putExtra("expired", "1 sampai 4 Hari")
                            putExtra("about", getString(R.string.body_about_lempeng))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Puteri Selat" -> {
                            putExtra("className", "Puteri Selat")
                            putExtra("expired", "1 sampai 4 Hari")
                            putExtra("about", getString(R.string.body_about_puteri_selat))
                            putExtra("temperature", "20°C sampai 4°C")
                        }

                        "Pundut Nasi" -> {
                            putExtra("className", "Pundut Nasi")
                            putExtra("expired", "1 sampai 4 Hari")
                            putExtra("about", getString(R.string.body_about_pundut_nasi))
                            putExtra("temperature", "20°C sampai 4°C")
                        }
                    }
                }
            startActivity(intent)
        }
        binding.rvDetectionResult.setHasFixedSize(true)
        binding.rvDetectionResult.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvDetectionResult.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setUpWindowInset() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}