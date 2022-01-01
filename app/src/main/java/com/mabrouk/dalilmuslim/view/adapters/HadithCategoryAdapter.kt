package com.mabrouk.dalilmuslim.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.HadithCategoryItemViewBinding
import com.mabrouk.data.entities.HadithCategoryEntity
import com.mabrouk.domain.models.HadithCategory

class HadithCategoryAdapter(val listener : HadithListener) : RecyclerView.Adapter<HadithCategoryAdapter.Holder>() {
    var data:ArrayList<HadithCategoryEntity> = ArrayList()
    set(value) {
        field=value
        notifyDataSetChanged()
    }

    inner class Holder(private val viewBinding : HadithCategoryItemViewBinding) : RecyclerView.ViewHolder(viewBinding.root){
        fun bind(item:HadithCategoryEntity){
            viewBinding.category=item
            viewBinding.root.setOnClickListener {
                listener.onCategoryClick(item)
            }
        }
    }

    interface HadithListener{
        fun onCategoryClick(item:HadithCategoryEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.hadith_category_item_view,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}