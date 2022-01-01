package com.mabrouk.dalilmuslim.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.RadioItemViewBinding
import com.mabrouk.data.entities.RadioEntity


/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 07/09/2021 created by Just clean
 */
class RadioAdapter(val onItemClick: (item: RadioEntity) -> Unit) :
    RecyclerView.Adapter<RadioAdapter.Holder>() {
    var items: ArrayList<RadioEntity> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(private val viewBinding: RadioItemViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: RadioEntity) {
            viewBinding.radio = item
            viewBinding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.radio_item_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}