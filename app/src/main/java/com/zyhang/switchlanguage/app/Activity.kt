package com.zyhang.switchlanguage.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
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

class FragmentAdapter(fm: androidx.fragment.app.FragmentManager, private val list: List<androidx.fragment.app.Fragment>) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): androidx.fragment.app.Fragment = list[position]

    override fun getCount(): Int = list.size
}

fun androidx.viewpager.widget.ViewPager.addOnPageChangeListener(onPageSelected: (position: Int) -> Unit) {
    addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            onPageSelected(position)
        }
    })
}
