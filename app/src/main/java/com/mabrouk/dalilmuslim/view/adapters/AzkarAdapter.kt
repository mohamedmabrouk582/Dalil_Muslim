package com.mabrouk.dalilmuslim.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.AzkarItemViewBinding
import com.mabrouk.data.entities.AzkarEntity

class AzkarAdapter : RecyclerView.Adapter<AzkarAdapter.Holder>(){
    var data:ArrayList<AzkarEntity> = ArrayList()
    set(value) {
        field=value
        notifyDataSetChanged()
    }
    inner class Holder(private val viewBinding:AzkarItemViewBinding) : RecyclerView.ViewHolder(viewBinding.root){
        fun bind(item:AzkarEntity){
            viewBinding.azkar=item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
      return Holder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.azkar_item_view,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}