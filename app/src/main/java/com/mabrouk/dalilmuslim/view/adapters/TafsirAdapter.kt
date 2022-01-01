package com.mabrouk.dalilmuslim.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.TafsirItemViewBinding
import com.mabrouk.data.entities.TafsirAyaEntity

class TafsirAdapter : RecyclerView.Adapter<TafsirAdapter.Holder>(){

    var tafsirs:ArrayList<TafsirAyaEntity> = ArrayList()
    set(value) {
        field=value
        notifyDataSetChanged()
    }

    inner class Holder(val viewBinding:TafsirItemViewBinding) : RecyclerView.ViewHolder(viewBinding.root){
        fun bind(item:TafsirAyaEntity){
            viewBinding.tafsir=item
            viewBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.tafsir_item_view,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(tafsirs[position])
    }

    override fun getItemCount(): Int = tafsirs.size
}