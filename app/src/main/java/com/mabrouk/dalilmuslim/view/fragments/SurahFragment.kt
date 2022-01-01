package com.mabrouk.dalilmuslim.view.fragments

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ListPopupWindow
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data.*
import androidx.work.WorkInfo
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.SuraAyatFragmentBinding
import com.mabrouk.dalilmuslim.states.VerseStates
import com.mabrouk.dalilmuslim.utils.*
import com.mabrouk.dalilmuslim.view.MainActivity
import com.mabrouk.dalilmuslim.view.adapters.AyaAdapter
import com.mabrouk.dalilmuslim.view.adapters.AyaPoupAdapter
import com.mabrouk.dalilmuslim.viewModels.SuraViewModel
import com.mabrouk.dalilmuslim.viewModels.VerseViewModel
import com.mabrouk.data.entities.*
import com.mabrouk.data.utils.DataStorePreferences
import com.mabrouk.data.utils.FileUtils
import com.google.android.exoplayer2.*
import com.google.android.material.snackbar.Snackbar
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SurahFragment : Fragment(R.layout.sura_ayat_fragment), AyaAdapter.AyaListener,
    AyaPoupAdapter.AyaPoupListener, Player.EventListener, MultiplePermissionsListener {
    lateinit var layoutBinding: SuraAyatFragmentBinding
    lateinit var player: SimpleExoPlayer
    var currentPosition: Int = 0
    var lastPosition: Int = 0
    var sura: SuraEntity? = null
    val viewModel: VerseViewModel by viewModels()
    val surahViewModel: SuraViewModel by viewModels()
    var playbackPosition: Long = 0
    val adapter: AyaAdapter by lazy { AyaAdapter(this) }
    val popAdapter: AyaPoupAdapter by lazy { AyaPoupAdapter(this) }
    lateinit var popupWindow: ListPopupWindow
    var hideAudioButtons: Int = -1
    var index = 0
    val data by lazy { adapter.verses.subtract(arrayListOf(adapter.verses.first())).chunked(7) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataBindingUtil.bind<SuraAyatFragmentBinding>(view)?.let { layoutBinding = it }
        hideAudio()
        layoutBinding.ayatRcv.adapter = adapter
        initPlayer()
        layoutBinding.isPlaying = player.isPlaying
        arguments?.getParcelable<SuraEntity>(VERSES_LIST)?.apply {
            layoutBinding.surah = this
            this@SurahFragment.sura = this
            loadSurah()
        }


        layoutBinding.scroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                layoutBinding.player.hide()
                layoutBinding.showHide.hide()
                hideAudio()
            } else {
                layoutBinding.showHide.show()
                layoutBinding.player.show()
                if (hideAudioButtons == 1) showAudio()
            }
        })

        layoutBinding.player.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
            layoutBinding.isPlaying = player.isPlaying
        }

        layoutBinding.nextAudio.setOnClickListener {
            player.seekToNext()
        }

        layoutBinding.prevAudio.setOnClickListener {
            player.seekToPrevious()
        }

        layoutBinding.showHide.setOnClickListener {
            if (layoutBinding.show == true) {
                hideAudioButtons = 0
                hideAudio()
            } else {
                hideAudioButtons = 1
                showAudio()
            }
        }


        layoutBinding.readerImg.setOnClickListener {
            ReadersFragment.start(activity?.supportFragmentManager!!,viewModel)
        }

    }

    private fun handleStates() {
        lifecycleScope.launchWhenStarted {
            viewModel.states.collect {
                when(it){
                    is VerseStates.DownloadVerse -> downloadVerse(it.workInfo)
                    VerseStates.Idle -> {}
                    is VerseStates.UpdateReader -> {
                        player.release()
                        playbackPosition = 0
                        index = 0
                        initPlayer()
                        adapter.updateVerse(-1, lastPosition)
                        lastPosition = 0
                        if (layoutBinding.isPlaying == true)player.play()
                        checkPermission()
                    }
                }
                viewModel.resetStates()
            }
        }
    }

    private fun loadSurah() {
        lifecycleScope.launch {
            val data = surahViewModel.fetchVerse(arguments?.getInt(VERSES_ID) ?: 0).first()
            layoutBinding.loaderView.visibility = View.GONE
            if (data.isNullOrEmpty()) return@launch
            adapter.verses = ArrayList(data)
            checkPermission()
            handleStates()
        }
    }


    private fun checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val permissionIntent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(permissionIntent)
            }
        }

        viewModel.getReader {
            if (it.versesIds != null && it.versesIds?.contains(sura?.sura_id?.toLong()) == true){
                prepareAudios()
            }else{
                Dexter.withContext(requireContext())
                    .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    .withListener(this)
                    .check()
            }
        }

    }

    override fun onAyaClick(view: View, verse: VerseEntity, position: Int) {
        popupWindow = ListPopupWindow(requireContext())
        popAdapter.verse = verse
        popupWindow.setAdapter(popAdapter)
        popupWindow.width = requireContext().resources.getDimensionPixelSize(R.dimen.popup_width)
        popupWindow.height = requireContext().resources.getDimensionPixelSize(R.dimen.popup_height)
        popupWindow.setDropDownGravity(Gravity.CENTER)
        popupWindow.anchorView = view
        popupWindow.isModal = true
        popupWindow.show()
    }

    override fun OnPlayClick(verse: VerseEntity?) {
        verse?.apply {
            player.seekTo(verse.verse_number - if (sura?.bismillah_pre!!) -1 else 0, C.TIME_UNSET)
            player.prepare()
            player.play()
            popupWindow.dismiss()
            layoutBinding.isPlaying = true
            showAudio()
        }
    }

    override fun OnTranlationClick(verse: VerseEntity?) {
        verse?.translations?.let {
            AyaTranslateFragment.start(
                it,
                if (verse.text_madani.isNullOrEmpty()) verse.text_indopak
                    ?: "" else verse.text_madani ?: ""
            ).show(
                activity?.supportFragmentManager!!,
                ""
            )
            popupWindow.dismiss()
        }
    }

    override fun OnTafsirClick(verse: VerseEntity?) {
        popupWindow.dismiss()
        lifecycleScope.launch {
            viewModel.getTafsier(verse?.chapter_id!!, verse.verse_number).first().let {
                val bundle = Bundle()
                bundle.putParcelable(AYA_TAFSIRS, AyaTafsirs(verse.text_madani!!, ArrayList(it)))
                try {
                    (activity as MainActivity).navController.navigate(
                        R.id.action_surahFragment_to_tafsirFragment,
                        bundle
                    )
                } catch (e: Exception) {
                }
            }
        }
    }

    override fun OnYoutClick(verse: VerseEntity?) {
        if (verse?.media_contents.isNullOrEmpty()) {
            Snackbar.make(layoutBinding.root, "This Aya Not Have Video", Snackbar.LENGTH_SHORT)
                .show()
        } else viewModel.youtube(verse?.media_contents?.get(0)!!.url)
        popupWindow.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        playbackPosition = player.currentPosition
        player.release()
    }


    private fun initPlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        //layoutBinding.playerView.player = player
        player.addListener(this)
        player.seekTo(playbackPosition)
    }


    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
        report?.let {
            if (report.areAllPermissionsGranted()) {
                showProgress()
                viewModel.downloadVerseAudio(
                    sura?.name_arabic!!,
                    arrayListOf(adapter.verses.first())
                )
            }
        }
    }

    private fun showProgress() {
        DownloadProgressDialog.start().show(
            activity?.supportFragmentManager!!,
            "DownloadProgressDialog"
        )
    }

    private fun prepareAudios() {
        player.addMediaItem(addMediaItem(FileUtils.getAudioPath(viewModel.currentReader.sufix,1, 0), "-1"))
        if (sura?.bismillah_pre!!)
            player.addMediaItem(addMediaItem(FileUtils.getAudioPath(viewModel.currentReader.sufix,1, 1), "-1"))
        player.addMediaItems(adapter.verses.map {
            addMediaItem(
                FileUtils.getAudioPath(
                    viewModel.currentReader.sufix,
                    it.chapter_id,
                    it.verse_number
                ), "${it.verse_number - 1}"
            )
        })
        // player.addMediaItem(MediaItem.fromUri("http://www.liveradiu.com/2018/06/holy-quran-radio-station-cairo-live.html"))
        player.prepare()
    }

    override fun onPermissionRationaleShouldBeShown(
        p0: MutableList<PermissionRequest>?,
        token: PermissionToken?
    ) {
        token?.continuePermissionRequest()
    }


    private fun downloadVerse(workInfo: LiveData<WorkInfo>) {
        workInfo.observe(viewLifecycleOwner, { workInfo ->
            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                DownloadProgressDialog.stop()
                prepareAudios()
                Log.d("downloadVerse","$index : ${data.size}")
                if (index < data.size) {
                    viewModel.downloadVerseAudio(sura?.name_arabic!!, ArrayList(data[index]))
                    index += 1
                } else {
                    sura?.audiosDownloaded = true
                    viewModel.updateSura(sura!!)
                    viewModel.updateReader(sura?.sura_id?.toLong() ?: 0L)
                }
            } else if (workInfo.state == WorkInfo.State.FAILED) {
                val error = workInfo.outputData.getString(AUDIO_DOWNLOAD)
                if (!error.isNullOrBlank()) {
                    Snackbar.make(layoutBinding.root, error, Snackbar.LENGTH_SHORT).show()
                }
                DownloadProgressDialog.stop()
            }
        })
    }


    private fun addMediaItem(path: String, id: String): MediaItem {
        return MediaItem.Builder().setUri(Uri.parse(path)).setMediaId(id).build()
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        if (mediaItem?.mediaId?.toInt()!! >= 0) {
            currentPosition = mediaItem.mediaId.toInt()
            adapter.updateVerse(currentPosition, lastPosition)

            layoutBinding.scroll.post {
                try {
                    val pos =
                        (layoutBinding.ayatRcv.layoutManager!! as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (currentPosition >= pos) {
                        layoutBinding.scroll.smoothScrollTo(
                            0,
                            layoutBinding.ayatRcv.getChildAt(currentPosition).y.toInt()
                        )
                    }
                } catch (e: Exception) {
                }
            }
            lastPosition = currentPosition

            if (mediaItem.mediaId.toInt() == adapter.verses.size)
                player.pause()
        }
    }

    private fun hideAudio() {
        layoutBinding.show = false
        layoutBinding.nextAudio.hide()
        layoutBinding.prevAudio.hide()
    }

    private fun showAudio() {
        layoutBinding.show = true
        layoutBinding.nextAudio.show()
        layoutBinding.prevAudio.show()
    }

    fun fromToObject(json: String?): ArrayList<AudioDataPass>? {
        val type = object : TypeToken<ArrayList<AudioDataPass>>() {}.type
        return Gson().fromJson(json, type)
    }


}