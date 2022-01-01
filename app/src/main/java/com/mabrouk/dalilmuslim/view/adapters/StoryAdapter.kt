package com.mabrouk.dalilmuslim.view.adapters

import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.StoryItemViewBinding
import com.mabrouk.data.entities.StoryEntity

class StoryAdapter(val onItemClick: (item: StoryEntity, pos: Int) -> Unit) :
    RecyclerView.Adapter<StoryAdapter.Holder>() {
    var data: ArrayList<StoryEntity> = ArrayList()
    var lastPosition: Int = -1
    fun addItem(item: StoryEntity) {
        data.add(item)
        notifyItemInserted(data.size - 1)
    }


    inner class Holder(val viewBinding: StoryItemViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: StoryEntity) {
            viewBinding.story = item
            viewBinding.root.setOnClickListener {
                if (lastPosition != absoluteAdapterPosition) {
                    if (lastPosition != -1) {
                        data[lastPosition].isPlaying = false
                        notifyItemChanged(lastPosition)
                    }
                    item.isPlaying = true
                    lastPosition = absoluteAdapterPosition
                    notifyItemChanged(absoluteAdapterPosition)
                    onItemClick(item, absoluteAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.story_item_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}