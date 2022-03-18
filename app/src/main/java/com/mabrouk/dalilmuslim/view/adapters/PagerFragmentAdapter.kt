package com.mabrouk.dalilmuslim.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerFragmentAdapter(fg : FragmentActivity) : FragmentStateAdapter(fg)  {
     var fragments : ArrayList<Fragment> = arrayListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}