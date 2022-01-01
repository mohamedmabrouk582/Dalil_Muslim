package com.mabrouk.dalilmuslim.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.ActivityMainBinding
import com.mabrouk.dalilmuslim.databinding.MawarecFragmentLayoutBinding
import com.mabrouk.dalilmuslim.utils.FROM_QURAN
import com.mabrouk.dalilmuslim.utils.VERSES_ID
import com.mabrouk.dalilmuslim.utils.VERSES_LIST
import com.mabrouk.data.entities.SuraEntity
import com.mabrouk.data.repository.QuranRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : LocalizationActivity(), TextToSpeech.OnInitListener {
    lateinit var layoutBinding: ActivityMainBinding
    lateinit var navController: NavController

    @Inject
    lateinit var dao: QuranRepository

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage("ar")
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.main_nav)
        val navHostFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.main_nav)

        val tts = TextToSpeech(this, this)
        //  tts.language = Locale("ar")
        tts.setPitch(.8f)
        tts.setSpeechRate(.1f)
        tts.speak("hello every body ", TextToSpeech.QUEUE_ADD, null, null)
        val menuItems = arrayOf(
            CbnMenuItem(R.drawable.ic_hadec, R.drawable.avd_hadec, R.id.hadecFragment),
            CbnMenuItem(R.drawable.ic_azqar, R.drawable.avd_azqar, R.id.azqarFragment),
            CbnMenuItem(R.drawable.ic_quran, R.drawable.avd_quran, R.id.quranFragment),
            CbnMenuItem(R.drawable.ic_story, R.drawable.avd_story, R.id.storyFragment),
            CbnMenuItem(R.drawable.ic_inheritance, R.drawable.avd_inhertince, R.id.mawarecFragment)
        )

        layoutBinding.navView.setMenuItems(menuItems, 2)
        layoutBinding.navView.setupWithNavController(navController)

        intent.extras?.apply {
            if (getBoolean(FROM_QURAN)) {
                getInt(VERSES_ID).also {
                    lifecycleScope.launch {
                        dao.getSurah(it).collect { sura ->
                            val bundle = Bundle()
                            bundle.putInt(VERSES_ID, it)
                            bundle.putParcelable(VERSES_LIST, sura)
                            navController.navigate(
                                R.id.action_quranFragment_to_surahFragment,
                                bundle
                            )
                        }

                    }

                }
            } else {
                lifecycleScope.launch {
                    delay(1000)
                    layoutBinding.navView.onMenuItemClick(3)
                }

            }
        }

    }

    override fun onInit(status: Int) {

    }


}