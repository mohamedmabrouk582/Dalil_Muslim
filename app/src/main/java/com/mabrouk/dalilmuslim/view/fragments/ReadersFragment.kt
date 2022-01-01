package com.mabrouk.dalilmuslim.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.ReadersLayoutBinding
import com.mabrouk.dalilmuslim.view.adapters.ReaderAdapter
import com.mabrouk.dalilmuslim.viewModels.VerseViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 03/09/2021 created by Just clean
 */
@AndroidEntryPoint
class ReadersFragment(val viewModel: VerseViewModel) : BottomSheetDialogFragment() {
    lateinit var viewBinding: ReadersLayoutBinding
    val adapter by lazy {
        ReaderAdapter {
             viewModel.updateReader(it)
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<ReadersLayoutBinding>(
            inflater,
            R.layout.readers_layout,
            container,
            false
        ).apply {
            viewBinding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.readersRcv.adapter = adapter
        viewModel.getAllReaders {

            adapter.items = it.apply {
                find { it.readerId == viewModel.currentReader.readerId }?.isSelected=true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
    }


    companion object{
        fun start(fm:FragmentManager,viewModel: VerseViewModel){
            ReadersFragment(viewModel).show(fm,"ReadersFragment")
        }
    }

}