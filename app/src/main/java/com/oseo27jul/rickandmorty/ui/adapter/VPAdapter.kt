package com.oseo27jul.rickandmorty.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class VPAdapter(fm: FragmentManager,behavior:Int) : FragmentPagerAdapter(fm,behavior) {


    private val fragmentTitle: ArrayList<String> = arrayListOf()
    private val fragments: ArrayList<Fragment> = arrayListOf()

    private val behavior: Int = 3

    override fun getCount(): Int {


        return behavior
    }

    override fun getItem(position: Int): Fragment {
       return fragments.get(position)
    }

    fun addFragment(fragment: Fragment, title:String){
        fragments.add(fragment)
        fragmentTitle.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle.get(position)
    }
}