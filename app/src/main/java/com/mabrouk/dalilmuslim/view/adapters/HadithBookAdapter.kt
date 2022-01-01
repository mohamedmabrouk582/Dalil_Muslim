package com.mabrouk.dalilmuslim.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.HadithBookItemViewBinding
import com.mabrouk.dalilmuslim.databinding.HadithCategoryItemViewBinding
import com.mabrouk.data.entities.HadithBookNumberEntity
import com.mabrouk.data.entities.HadithCategoryEntity
import com.mabrouk.domain.models.HadithCategory

class HadithBookAdapter(val listener : HadithListener) : RecyclerView.Adapter<HadithBookAdapter.Holder>() {
    var data:ArrayList<HadithBookNumberEntity> = ArrayList()
    set(value) {
        field=value
        notifyDataSetChanged()
    }

    inner class Holder(private val viewBinding : HadithBookItemViewBinding) : RecyclerView.ViewHolder(viewBinding.root){
        fun bind(item:HadithBookNumberEntity){
            viewBinding.category=item
            viewBinding.root.setOnClickListener {
                listener.onBookClick(item)
            }
        }
    }

    interface HadithListener{
        fun onBookClick(item:HadithBookNumberEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.hadith_book_item_view,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}