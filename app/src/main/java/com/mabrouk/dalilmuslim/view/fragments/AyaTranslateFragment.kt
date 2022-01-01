package com.mabrouk.dalilmuslim.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.AyaTranslateLayoutBinding
import com.mabrouk.dalilmuslim.utils.AYA_CONTENT
import com.mabrouk.dalilmuslim.utils.AYA_TRANSLATE
import com.mabrouk.data.entities.TranslationEntity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AyaTranslateFragment : BottomSheetDialogFragment() {
    lateinit var viewBinding :AyaTranslateLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding=DataBindingUtil.inflate(inflater, R.layout.aya_translate_layout,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(AYA_CONTENT)?.let {
            viewBinding.txt.text= it
        }
        arguments?.getParcelableArrayList<TranslationEntity>(AYA_TRANSLATE)?.let{
            it.fold("",{ acc, translationEntity ->
                acc+" "+translationEntity.textTranslation
            }).apply {
                viewBinding.trans=this
            }
        }
    }

    companion object{
        fun start(item:ArrayList<TranslationEntity>,content:String) : AyaTranslateFragment{
            val bundle =Bundle()
             bundle.putParcelableArrayList(AYA_TRANSLATE,item)
            bundle.putString(AYA_CONTENT,content)
            val fragment = AyaTranslateFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}