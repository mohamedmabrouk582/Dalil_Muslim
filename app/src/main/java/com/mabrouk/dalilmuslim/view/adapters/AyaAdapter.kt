package com.mabrouk.dalilmuslim.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.AyaItemViewBinding
import com.mabrouk.data.entities.VerseEntity

class AyaAdapter(val  listener:AyaListener) : RecyclerView.Adapter<AyaAdapter.Holder>(){
    var verses:ArrayList<VerseEntity> = ArrayList()
    set(value) {
        field=value
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
         Holder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.aya_item_view,parent,false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(verses[position])
    }

    fun updateVerse(position:Int,last:Int){
        if (last!=position){
            verses[last].selected=false
            notifyItemChanged(last)
        }
        if (position>-1) {
            verses[position].selected = true
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = verses.size

    inner class Holder(val viewBinding:AyaItemViewBinding) : RecyclerView.ViewHolder(viewBinding.root){

        fun bind(item:VerseEntity){
            viewBinding.verse=item
            viewBinding.root.setOnClickListener { listener.onAyaClick(it,item,absoluteAdapterPosition) }
            viewBinding.executePendingBindings()
        }

    }

    interface AyaListener{
        fun onAyaClick(view:View,verse:VerseEntity,position:Int)
    }


}