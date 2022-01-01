package com.mabrouk.dalilmuslim.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.JuzItemViewBinding
import com.mabrouk.dalilmuslim.databinding.SuraItemViewBinding
import com.mabrouk.data.entities.Juz_Sura
import com.mabrouk.domain.models.Sura

class SuraAdapter(val listener:SuraListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var data:ArrayList<Juz_Sura> = ArrayList()
    set(value) {
        field=value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when(viewType){
            0 -> JuzHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.juz_item_view,parent,false))
            1 -> SuraHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.sura_item_view,parent,false))
            else -> throw Exception("This type not found here")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is JuzHolder -> holder.bind(data[position])
            is SuraHolder -> holder.bind(data[position])
        }
    }

    fun update(postion:Int){
        notifyItemChanged(postion)
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return if(data[position].sura?.name_arabic!=null) 1 else 0
    }


    inner class JuzHolder(val viewBinding: JuzItemViewBinding) : RecyclerView.ViewHolder(viewBinding.root){

        fun bind(item:Juz_Sura){
            viewBinding.juz=item
            viewBinding.executePendingBindings()
            viewBinding.downloadImg.setOnClickListener { listener.onJuzDownload(item,adapterPosition) }
        }

    }

    inner class SuraHolder(val viewBinding:SuraItemViewBinding) : RecyclerView.ViewHolder(viewBinding.root){

        fun bind(item:Juz_Sura){
            viewBinding.juz=item
            viewBinding.executePendingBindings()
            viewBinding.root.setOnClickListener { listener.onSuraClick(item) }
        }

    }

    interface SuraListener{
        fun onJuzDownload(item:Juz_Sura,postion: Int)
        fun onSuraClick(item: Juz_Sura)
    }


}