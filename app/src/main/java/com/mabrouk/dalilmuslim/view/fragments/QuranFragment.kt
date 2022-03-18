package com.mabrouk.dalilmuslim.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkInfo
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.QuranFragmentLayoutBinding
import com.mabrouk.dalilmuslim.states.SurahStates
import com.mabrouk.dalilmuslim.utils.*
import com.mabrouk.dalilmuslim.view.MainActivity
import com.mabrouk.dalilmuslim.view.adapters.SuraAdapter
import com.mabrouk.dalilmuslim.viewModels.SuraViewModel
import com.mabrouk.data.entities.JuzEntity
import com.mabrouk.data.entities.Juz_Sura
import com.mabrouk.data.entities.ParaceVerses
import com.mabrouk.data.entities.SuraEntity
import com.mabrouk.data.utils.DataStorePreferences
import com.mabrouk.data.utils.mapJuz
import com.mabrouk.data.utils.toDomain
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class QuranFragment : Fragment(R.layout.quran_fragment_layout) , SuraAdapter.SuraListener {
    lateinit var layoutBinding: QuranFragmentLayoutBinding
    @Inject
    lateinit var dataStore : DataStorePreferences
    val viewModel : SuraViewModel by viewModels()
    val adapter : SuraAdapter by lazy { SuraAdapter(this) }
    var juzs : ArrayList<JuzEntity> = ArrayList()
    var suras:ArrayList<SuraEntity> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutBinding=
            DataBindingUtil.inflate(inflater,R.layout.quran_fragment_layout ,container,false)
        layoutBinding.vm=viewModel
        return layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutBinding.surasRcv.adapter=adapter
        lifecycleScope.launch {
            if (!dataStore.getBoolean(READERS_DOWNLOADS)) viewModel.getReaders()
                if (dataStore.getBoolean(SURA_LIST_DOWNLOADS)){
                    savedJuzs(viewModel.getSavedJuzs())
                    savedsura(viewModel.getSavedSuras())
                }else{
                    viewModel.requestJuz()
                    viewModel.requestSuras()
                }
        }


      handleStates()
    }

   private fun handleStates(){
        lifecycleScope.launchWhenStarted {
            viewModel.states.collect {
                when(it){
                    is SurahStates.Error -> error(it.error)
                    is SurahStates.RequestJuzs -> requestJuzs(it.juzs)
                    is SurahStates.RequestSurahs -> requestSuras(it.suras)
                    is SurahStates.SearchResult -> Toast.makeText(
                        requireContext(),
                        it.query,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                viewModel.resetStates()
            }
        }
    }

    private fun requestJuzs(juzs: ArrayList<JuzEntity>) {
        this.juzs=juzs
        if (suras.isNotEmpty()){
            viewModel.loader.set(false)
            adapter.data= mapJuz(juzs, suras)
        }
    }

    private fun requestSuras(suras: ArrayList<SuraEntity>) {
        this.suras=suras
        if (juzs.isNotEmpty()){
            lifecycleScope.launch { dataStore.setBoolean(SURA_LIST_DOWNLOADS,true) }
            viewModel.loader.set(false)
            adapter.data= mapJuz(juzs, suras)
        }
    }

     fun savedJuzs(juzs: LiveData<List<JuzEntity>>) {
        juzs.observe(viewLifecycleOwner) {
            this.juzs = ArrayList(it)
            viewModel.juzsResult.postValue(ArrayList(it).toDomain())
            if (suras.isNotEmpty()) {
                lifecycleScope.launch { dataStore.setBoolean(SURA_LIST_DOWNLOADS, true) }
                adapter.data = mapJuz(this.juzs, suras)
            }
        }
     }

     fun savedsura(suras: LiveData<List<SuraEntity>>) {
        suras.observe(viewLifecycleOwner) {
            this.suras = ArrayList(it)
            if (juzs.isNotEmpty()) {
                adapter.data = mapJuz(juzs, this.suras)
            }
        }
     }

    fun error(error: String) {
        viewModel.loader.set(false)
        viewModel.error.set(error)
    }

    override fun onJuzDownload(item: Juz_Sura,postion: Int) {
        showProgress()
        val ids=item.verse_ids.filter { verse_id -> !suras[verse_id-1].isDownload }
        viewModel.downloadJuz(ids,requireContext()).observe(viewLifecycleOwner) {
            if (it.state == WorkInfo.State.SUCCEEDED) {
                lifecycleScope.launch {
                    item.isDownload = true
                    adapter.update(postion)
                    ids.forEach { sura_id ->
                        viewModel.updateSura(suras[sura_id - 1].apply {
                            isDownload = true
                        })
                    }
                    viewModel.updateJuz(JuzEntity(item.juz_num, item.juz_num, item.verse_map, true))
                    DownloadProgressDialog.stop()
                }
            } else if (it.state == WorkInfo.State.FAILED) {
                DownloadProgressDialog.stop()
                //Toast.makeText(requireContext(), it.outputData.getString(DOWNLOAD_VERSES_IDS), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showProgress(){
         DownloadProgressDialog.start().show(activity?.supportFragmentManager!!,"DownloadProgressDialog")
    }

    override fun onSuraClick(item: Juz_Sura) {
        lifecycleScope.launch {
            val value=viewModel.fetchVerse(item.sura?.sura_id!!).first()
            if (value.isNullOrEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.sourah_not_download), Toast.LENGTH_SHORT).show()
            } else {
                val bundle = Bundle()
                bundle.putInt(VERSES_ID,item.sura?.sura_id!!)
                bundle.putParcelable(VERSES_LIST,item.sura)
                (activity as MainActivity).navController.navigate(
                        R.id.action_quranFragment_to_surahFragment,
                        bundle
                )
            }
        }
    }
}