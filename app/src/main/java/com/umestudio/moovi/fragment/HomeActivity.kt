package com.umestudio.moovi.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.umestudio.moovi.adapter.TabAdapter
import com.umestudio.moovi.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFragment()
    }

    private fun setupFragment(){
        val tabAdapter = TabAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = tabAdapter

        val tabTitles = arrayOf("Now Playing","Upcoming")
        TabLayoutMediator(binding.tabHome, binding.viewPager){tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}