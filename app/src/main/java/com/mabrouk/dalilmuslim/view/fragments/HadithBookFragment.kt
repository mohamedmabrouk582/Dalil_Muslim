package com.mabrouk.dalilmuslim.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.HadithBookFragmentLayoutBinding
import com.mabrouk.dalilmuslim.states.HadithStates
import com.mabrouk.dalilmuslim.utils.AYA_CONTENT
import com.mabrouk.dalilmuslim.utils.AYA_TRANSLATE
import com.mabrouk.dalilmuslim.utils.COLLECTION_NAME
import com.mabrouk.dalilmuslim.utils.HADITH_KEYS
import com.mabrouk.dalilmuslim.view.MainActivity
import com.mabrouk.dalilmuslim.view.adapters.HadithBookAdapter
import com.mabrouk.dalilmuslim.viewModels.HadithCategoryViewModel
import com.mabrouk.data.entities.HadithBookNumberEntity
import com.mabrouk.data.entities.PassHadithKeys
import com.mabrouk.data.entities.TranslationEntity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HadithBookFragment : BottomSheetDialogFragment(), HadithBookAdapter.HadithListener {
    lateinit var viewBinding : HadithBookFragmentLayoutBinding
    val viewModel : HadithCategoryViewModel by viewModels()
    val adapter by lazy { HadithBookAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.hadith_book_fragment_layout,container,false)
        viewBinding.rcv.layoutManager=
            GridLayoutManager(requireContext(),3, GridLayoutManager.VERTICAL,false)
        viewBinding.rcv.adapter=adapter
        viewBinding.vm=viewModel
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(COLLECTION_NAME)?.let {
            viewModel.loadHadithBook(it)
        }
         handleStates()
    }

    private fun handleStates() {
        lifecycleScope.launch {
            viewModel.states.collect {
                if (it is HadithStates.LoadHadithBooks){
                    adapter.data=it.data
                }
            }
        }
    }

    override fun onBookClick(item: HadithBookNumberEntity) {
        HadithFragment.start(PassHadithKeys(item.collectionName,item.bookNumber)).show(activity?.supportFragmentManager!!,"HadithFragment")
    }

//        override fun onStart() {
//        super.onStart()
//        val sheetContainer = requireView().parent as? ViewGroup ?: return
//        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
//    }


    companion object{
        fun start(collectionName:String) : HadithBookFragment{
            val bundle =Bundle()
            bundle.putString(COLLECTION_NAME,collectionName)
            val fragment = HadithBookFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}