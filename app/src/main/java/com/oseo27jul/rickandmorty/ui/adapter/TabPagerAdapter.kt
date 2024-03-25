package com.oseo27jul.rickandmorty.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.oseo27jul.rickandmorty.ui.fragments.Characters.CharacterFragment

import com.oseo27jul.rickandmorty.ui.fragments.EpisodesFragment
import com.oseo27jul.rickandmorty.ui.fragments.LocationFragment


class TabsPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CharacterFragment()
            1 -> LocationFragment()
            else -> EpisodesFragment()
        }
    }
}