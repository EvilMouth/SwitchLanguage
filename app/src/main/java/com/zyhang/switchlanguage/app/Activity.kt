package com.zyhang.switchlanguage.app

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.zyhang.switchlanguage.AutoConfigLanguageActivity
import com.zyhang.switchlanguage.SwitchLanguageUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting.*
import java.util.*

class MainActivity : AutoConfigLanguageActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vp.adapter = FragmentAdapter(supportFragmentManager, listOf(
                HomeFragment(),
                CategoryFragment(),
                FollowFragment(),
                CartFragment(),
                AccountFragment()
        ))

        vp.addOnPageChangeListener { position ->
            bnv.menu.getItem(position).isChecked = true
        }

        bnv.setOnNavigationItemSelectedListener { item ->
            vp.currentItem = item.order
            true
        }
    }
}

class SettingActivity : AutoConfigLanguageActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setTitle(R.string.setting)

        chinese.setOnClickListener {
            switch(Locale.CHINA)
        }

        english.setOnClickListener {
            switch(Locale.ENGLISH)
        }

        arabic.setOnClickListener {
            switch(Locale("ar"))
        }
    }

    private fun switch(locale: Locale) {
        SwitchLanguageUtils.startSwitchLanguage(locale)
        finish()
    }
}

class FragmentAdapter(fm: FragmentManager, private val list: List<Fragment>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = list[position]

    override fun getCount(): Int = list.size
}

fun ViewPager.addOnPageChangeListener(onPageSelected: (position: Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            onPageSelected(position)
        }
    })
}
