package com.mabrouk.dalilmuslim.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.AzkarCategoryItemViewBinding

class AzkarCategoryAdapter(val listener : AzkarCategoryListener) : RecyclerView.Adapter<AzkarCategoryAdapter.Holder>() {
    var data:ArrayList<String> = ArrayList()
    set(value) {
        field=value
        notifyDataSetChanged()
    }
    inner class Holder(private val viewBinding : AzkarCategoryItemViewBinding) : RecyclerView.ViewHolder(viewBinding.root){

        fun bind(item:String){
            viewBinding.category=item
            viewBinding.root.setOnClickListener {
                listener.onAzkarClick(item)
            }
        }

    }

    interface AzkarCategoryListener{
        fun onAzkarClick(item:String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.azkar_category_item_view,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}