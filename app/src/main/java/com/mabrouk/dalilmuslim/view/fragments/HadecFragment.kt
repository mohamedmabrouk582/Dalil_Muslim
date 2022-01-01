package com.mabrouk.dalilmuslim.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.HadecFragmentLayoutBinding
import com.mabrouk.dalilmuslim.states.HadithStates
import com.mabrouk.dalilmuslim.view.adapters.HadithCategoryAdapter
import com.mabrouk.dalilmuslim.viewModels.HadithCategoryViewModel
import com.mabrouk.data.entities.HadithCategoryEntity
import com.mabrouk.domain.models.HadithCategory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HadecFragment: Fragment(R.layout.hadec_fragment_layout),
    HadithCategoryAdapter.HadithListener {
    lateinit var layoutBinding: HadecFragmentLayoutBinding
    val viewModel : HadithCategoryViewModel by viewModels()
    val adapter by lazy { HadithCategoryAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataBindingUtil.bind<HadecFragmentLayoutBinding>(view)?.let { layoutBinding=it }
        layoutBinding.vm=viewModel
        layoutBinding.rcv.layoutManager=GridLayoutManager(requireContext(),3,GridLayoutManager.VERTICAL,false)
        layoutBinding.rcv.adapter=adapter
        handleStates()
        viewModel.getHadithCategories()
    }

    private fun handleStates() {
        lifecycleScope.launch {
            viewModel.states.collect {
                if (it is HadithStates.LoadCategories){
                   adapter.data=it.data
                }
            }
        }
    }

    override fun onCategoryClick(item: HadithCategoryEntity) {
       HadithBookFragment.start(item.name).show(activity?.supportFragmentManager!!,"HadithBookFragment")
    }

}