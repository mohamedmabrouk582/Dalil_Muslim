package com.mabrouk.dalilmuslim.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.HadithBookItemViewBinding
import com.mabrouk.dalilmuslim.databinding.HadithCategoryItemViewBinding
import com.mabrouk.dalilmuslim.databinding.HadithItemViewBinding
import com.mabrouk.data.entities.HadithBookNumberEntity
import com.mabrouk.data.entities.HadithCategoryEntity
import com.mabrouk.data.entities.HadithEntity
import com.mabrouk.domain.models.HadithCategory

class HadithAdapter() : RecyclerView.Adapter<HadithAdapter.Holder>() {
    val data:ArrayList<HadithEntity> = ArrayList()

    fun setValues(data:ArrayList<HadithEntity>){
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class Holder(private val viewBinding : HadithItemViewBinding) : RecyclerView.ViewHolder(viewBinding.root){
        fun bind(item:HadithEntity){
            viewBinding.hadith=item
        }
    }

    interface HadithListener{
        fun onBookClick(item:HadithEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.hadith_item_view,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}