package com.umestudio.moovi.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.umestudio.moovi.R
import com.umestudio.moovi.adapter.TabAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupFragment()
    }

    private fun setupFragment(){
        val tabAdapter = TabAdapter(supportFragmentManager, lifecycle)
        view_pager.adapter = tabAdapter

        val tabTitles = arrayOf("Now Playing","Upcoming")
        TabLayoutMediator(tab_home, view_pager){tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}