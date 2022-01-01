package com.mabrouk.dalilmuslim.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.AzkarItemFargmentBinding
import com.mabrouk.dalilmuslim.utils.AZKAR_CATEGORY
import com.mabrouk.dalilmuslim.view.adapters.AzkarAdapter
import com.mabrouk.data.db.azkar.AzkarDao
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class AzkarItemFragment : BottomSheetDialogFragment() {
    lateinit var viewBinding : AzkarItemFargmentBinding
    @Inject
    lateinit var dao: AzkarDao
    val adapter by lazy { AzkarAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(inflater,R.layout.azkar_item_fargment,container,false)
        viewBinding.itemsRcv.adapter=adapter
        arguments?.getString(AZKAR_CATEGORY)?.let {
            lifecycleScope.launchWhenStarted {
                dao.getAzkarBYCategory(it).collect {
                    adapter.data=ArrayList(it)
                }
            }
        }
        return viewBinding.root
    }

//    override fun onStart() {
//        super.onStart()
//        val sheetContainer = requireView().parent as? ViewGroup ?: return
//        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
//    }

    companion object{
        fun start( item:String) : AzkarItemFragment{
            val bundle =Bundle()
            bundle.putString(AZKAR_CATEGORY,item)
            val fragment = AzkarItemFragment()
            fragment.arguments = bundle
            return fragment
        }
    }



}