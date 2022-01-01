package com.mabrouk.dalilmuslim.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.AyaTafsirLayoutBinding
import com.mabrouk.dalilmuslim.utils.AYA_TAFSIRS
import com.mabrouk.dalilmuslim.view.adapters.TafsirAdapter
import com.mabrouk.data.entities.AyaTafsirs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TafsirFragment : BottomSheetDialogFragment() {
    lateinit var layoutBinding :AyaTafsirLayoutBinding
    val adapter : TafsirAdapter by lazy { TafsirAdapter() }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutBinding=DataBindingUtil.inflate(inflater,R.layout.aya_tafsir_layout,container,false)
        return layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutBinding.rcv.adapter=adapter
        arguments?.getParcelable<AyaTafsirs>(AYA_TAFSIRS)?.let{
            layoutBinding.ayaTxt.text=it.name
            adapter.tafsirs=it.tafsirs

        }
    }

    override fun onStart() {
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

}