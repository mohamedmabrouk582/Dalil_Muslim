package com.mabrouk.dalilmuslim.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.HadithLayoutBinding
import com.mabrouk.dalilmuslim.states.HadithStates
import com.mabrouk.dalilmuslim.utils.AYA_CONTENT
import com.mabrouk.dalilmuslim.utils.AYA_TRANSLATE
import com.mabrouk.dalilmuslim.utils.HADITH_KEYS
import com.mabrouk.dalilmuslim.view.adapters.HadithAdapter
import com.mabrouk.dalilmuslim.viewModels.HadithCategoryViewModel
import com.mabrouk.data.entities.PassHadithKeys
import com.mabrouk.data.entities.TranslationEntity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HadithFragment : BottomSheetDialogFragment(){
    lateinit var viewBinding: HadithLayoutBinding
    private val viewModel : HadithCategoryViewModel by viewModels()
    private val adapter by lazy { HadithAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(inflater,R.layout.hadith_layout,container,false)
        viewBinding.vm=viewModel
        viewBinding.rcv.adapter=adapter
        handleStates()
        arguments?.getParcelable<PassHadithKeys>(HADITH_KEYS)?.let {
            viewModel.loadHadiths(it.name,it.bookNumber)
        }
        return viewBinding.root
    }

    private fun handleStates() {
        lifecycleScope.launch {
            viewModel.states.collect {
                if (it is HadithStates.LoadHadith){
                    adapter.setValues(it.data)
                }
            }
        }
    }

    companion object{
        fun start(item:PassHadithKeys) : HadithFragment{
            val bundle =Bundle()
            bundle.putParcelable(HADITH_KEYS,item)
            val fragment = HadithFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}