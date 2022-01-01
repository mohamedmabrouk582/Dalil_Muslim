package com.mabrouk.dalilmuslim.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.QuranRadioLayoutBinding
import com.mabrouk.dalilmuslim.states.RadioStates
import com.mabrouk.dalilmuslim.view.adapters.RadioAdapter
import com.mabrouk.dalilmuslim.viewModels.RadioViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 03/09/2021 created by Just clean
 */
@AndroidEntryPoint
class QuranRadioFragment : Fragment(R.layout.quran_radio_layout) {
    lateinit var viewBinding: QuranRadioLayoutBinding
    val viewModel: RadioViewModel by viewModels()
    private val player by lazy { SimpleExoPlayer.Builder(requireContext()).build() }
    private val adapter by lazy { RadioAdapter{
        player.clearMediaItems()
        player.addMediaItem(MediaItem.fromUri(it.radioUrl))
        player.seekTo(playbackPosition)
        player.prepare()
        player.play()
        viewBinding.name = it.name
    } }
    var playbackPosition: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = DataBindingUtil.bind(view)!!
        player.seekTo(playbackPosition)
        viewModel.requestRadios()
        viewBinding.rcv.adapter = adapter
        val layoutManager = FlexboxLayoutManager(requireContext())
        layoutManager.flexDirection = FlexDirection.ROW
        viewBinding.rcv.layoutManager = layoutManager
        player.addMediaItem(MediaItem.fromUri("http://live.mp3quran.net:9702/;"))
        player.prepare()
        player.play()
        handleStates()
    }



    private fun handleStates(){
        lifecycleScope.launch {
            viewModel.states.collect {
                when(it){
                    RadioStates.Idle -> {}
                    is RadioStates.LoadData -> {
                      adapter.items = it.data
                        viewBinding.progress.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playbackPosition = player.currentPosition
        player.release()
    }
}