package com.baiganov.devlife.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.baiganov.devlife.R
import com.baiganov.devlife.adapters.PagerAdapter
import com.baiganov.devlife.ui.fragments.HotFragment
import com.baiganov.devlife.ui.fragments.LatestFragment
import com.baiganov.devlife.ui.fragments.TopFragment
import com.baiganov.devlife.util.Constants.Companion.TITLE_HOT
import com.baiganov.devlife.util.Constants.Companion.TITLE_LATEST
import com.baiganov.devlife.util.Constants.Companion.TITLE_TOP
import com.baiganov.devlife.viemodels.MainViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var appBar: AppBarLayout

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.clearDb()
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        appBar = findViewById(R.id.appBar)

        val alphaAnim = AnimationUtils.loadAnimation(this, R.anim.up_to_center_anim)
        appBar.startAnimation(alphaAnim)

        val fragments = ArrayList<Fragment>()
        fragments.add(LatestFragment())
        fragments.add(TopFragment())
        fragments.add(HotFragment())

        val titles = ArrayList<String>()
        titles.add(TITLE_LATEST)
        titles.add(TITLE_TOP)
        titles.add(TITLE_HOT)

        val pagerAdapter = PagerAdapter(
            fragments,
            this
        )

        viewPager.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}