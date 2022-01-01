package com.mabrouk.dalilmuslim.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.AyaOpationsLayoutBinding
import com.mabrouk.data.entities.VerseEntity

class AyaPoupAdapter(val listener:AyaPoupListener): BaseAdapter() {
    var verse:VerseEntity?=null
    override fun getCount(): Int =1

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       val view= DataBindingUtil.inflate<AyaOpationsLayoutBinding>(LayoutInflater.from(parent?.context),R.layout.aya_opations_layout,parent,false)
       view.playImg.setOnClickListener { listener.OnPlayClick(verse) }
        view.translationImg.setOnClickListener { listener.OnTranlationClick(verse) }
        view.tafsirImg.setOnClickListener { listener.OnTafsirClick(verse) }
        view.youtube.setOnClickListener { listener.OnYoutClick(verse) }
        return view.root
    }

    interface AyaPoupListener{
        fun OnPlayClick(verse:VerseEntity?)
        fun OnTranlationClick(verse:VerseEntity?)
        fun OnTafsirClick(verse:VerseEntity?)
        fun OnYoutClick(verse:VerseEntity?)
    }
}