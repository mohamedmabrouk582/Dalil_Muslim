package com.mabrouk.dalilmuslim.view.fragments

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkInfo
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.StoryFragmentBinding
import com.mabrouk.dalilmuslim.states.StoryStates
import com.mabrouk.dalilmuslim.view.adapters.StoryAdapter
import com.mabrouk.dalilmuslim.viewModels.StoriesViewModel
import com.mabrouk.data.utils.CheckNetwork
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.mabrouk.data.entities.StoryEntity
import com.mabrouk.data.utils.FileUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 24/07/2021 created by Just clean
 */
@AndroidEntryPoint
class StoryFragment : Fragment(R.layout.story_fragment) {
    lateinit var viewBinding: StoryFragmentBinding
    val viewModel: StoriesViewModel by viewModels()
    var selectedItem: StoryEntity? = null
    var youTubePlayer: YouTubePlayer? = null


    val adapter by lazy {
        StoryAdapter { item, pos ->
            selectedItem = item
            viewBinding.playing = true
            viewBinding.story = item
            youTubePlayer?.apply {
                this.loadVideo(item.video_key, 0f)
            }
            if (!CheckNetwork.isOnline(requireContext())) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setVideoKey() {
        viewBinding.playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@StoryFragment.youTubePlayer = youTubePlayer
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = DataBindingUtil.bind(view)!!
        viewBinding.videosRcv.adapter = adapter
        viewModel.loadStories()
        lifecycle.addObserver(viewBinding.playerView)
        setVideoKey()
        handleStates()
        viewBinding.downloadVideo.setOnClickListener {
            viewModel.downloadVideo(selectedItem!!)
        }
    }

    private fun handleStates() {
        lifecycleScope.launchWhenStarted {
            viewModel.states.collect {
                when (it) {
                    is StoryStates.AddStory -> {
                        adapter.addItem(it.storyEntity)
                        viewBinding.loader.visibility = View.GONE
                    }
                    is StoryStates.Error -> Toast.makeText(
                        requireContext(),
                        it.error,
                        Toast.LENGTH_SHORT
                    ).show()
                    StoryStates.Idle -> {}
                    StoryStates.ShowNotification -> {}//Notification.showNotification(requireContext(),adapter.data,player,1)
                    is StoryStates.DownloadVideo -> {
                        it.workInfo.observe(viewLifecycleOwner) {
                            Toast.makeText(requireContext(), it.state.name, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        viewBinding.playerView.release()
    }

}