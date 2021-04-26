package com.umestudio.moovi.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umestudio.moovi.fragment.NowPlayingFragment
import com.umestudio.moovi.fragment.UpcomingFragment

class TabAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    :FragmentStateAdapter(fragmentManager, lifecycle){

    val fragments: ArrayList<Fragment> = arrayListOf(
        NowPlayingFragment(),
        UpcomingFragment()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}