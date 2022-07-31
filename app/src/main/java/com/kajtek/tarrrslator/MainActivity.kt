package com.kajtek.tarrrslator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

lateinit var db: DBHelper
lateinit var login: String

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        login = intent.getStringExtra("login").toString()

        setContentView(R.layout.activity_main)
        db = DBHelper(this)
    }

    override fun onStart() {
        super.onStart()

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val pagerAdapter = SectionsPagerAdapter(supportFragmentManager, this)

        val pager = findViewById<View>(R.id.pager) as ViewPager
        pager.adapter = pagerAdapter

        if (pager.currentItem == 2) {
            pager.currentItem = 0
        }

        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(pager)

    }
}

private class SectionsPagerAdapter(fm: FragmentManager, private val context: Context) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TopFragment()
            1 -> AddFragment()
            2 -> ReviseFragment()
            else -> TopFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return context.resources.getString(R.string.home_tab)
            1 -> return context.resources.getString(R.string.kat1_tab)
            2 -> return context.resources.getString(R.string.kat2_tab)
            else -> return null
        }
    }
}