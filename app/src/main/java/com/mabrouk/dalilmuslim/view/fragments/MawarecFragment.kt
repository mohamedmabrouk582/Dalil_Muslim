package com.mabrouk.dalilmuslim.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.MawarecFragmentLayoutBinding

class MawarecFragment : Fragment() {
    lateinit var layoutBinding: MawarecFragmentLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutBinding=
            DataBindingUtil.inflate(inflater, R.layout.mawarec_fragment_layout,container,false)

        return layoutBinding.root
    }
}