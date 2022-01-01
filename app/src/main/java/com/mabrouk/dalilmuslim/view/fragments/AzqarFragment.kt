package com.mabrouk.dalilmuslim.view.fragments

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.AzqarFragmentLayoutBinding
import com.mabrouk.dalilmuslim.view.adapters.AzkarCategoryAdapter
import com.mabrouk.data.db.azkar.AzkarDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class AzqarFragment : Fragment(R.layout.azqar_fragment_layout), AzkarCategoryAdapter.AzkarCategoryListener {
  lateinit var layoutBinding: AzqarFragmentLayoutBinding
  @Inject
  lateinit var dao: AzkarDao
  val adapter by lazy { AzkarCategoryAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataBindingUtil.bind<AzqarFragmentLayoutBinding>(view)?.let {
            layoutBinding=it
        }
        layoutBinding.azkarRcv.adapter=adapter
        lifecycleScope.launchWhenStarted {
            dao.getAzkarCategories().collect {
                val data = ArrayList(HashSet<String>(it).toList())
                data.sort()
                adapter.data=data
            }
        }
    }



    override fun onAzkarClick(item: String) {
        AzkarItemFragment.start(item).show(activity?.supportFragmentManager!!,"AzkarItemFragment")
      }
}