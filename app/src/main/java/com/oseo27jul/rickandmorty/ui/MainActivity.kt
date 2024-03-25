package com.oseo27jul.rickandmorty.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.oseo27jul.rickandmorty.R

import com.oseo27jul.rickandmorty.databinding.ActivityMainBinding
import com.oseo27jul.rickandmorty.ui.adapter.TabsPagerAdapter
import com.oseo27jul.rickandmorty.ui.fragments.Characters.CharacterFragment

import com.oseo27jul.rickandmorty.ui.fragments.EpisodesFragment
import com.oseo27jul.rickandmorty.ui.fragments.LocationFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //if(savedInstanceState != null){
            // Inicializar el ViewPager y TabLayout
            val viewPager = binding.viewPager
            val tabLayout = binding.tabLayout

        viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT

            // Adaptador para el ViewPager
            val adapter = TabsPagerAdapter(supportFragmentManager, lifecycle)
            viewPager.adapter = adapter

            // Asociar el ViewPager con el TabLayout
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.character)
                    1 -> getString(R.string.location)
                    2 -> getString(R.string.episodes)
                    else -> throw IllegalArgumentException("Position $position is not supported")
                }
            }.attach()

            // Configurar la acción al seleccionar una pestaña del TabLayout
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        // Cambiar el Fragment en el FragmentContainerView según la pestaña seleccionada
                        when (tab.position) {
                            0 -> showFragment(CharacterFragment())
                            1 -> showFragment(LocationFragment())
                            2 -> showFragment(EpisodesFragment())
                        }

                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // No es necesario implementar nada aquí
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // No es necesario implementar nada aquí
                }
            })
       // }


    }

    //hacemos que solo se haga una instancia de un fragment
    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            // Ocultar todos los fragmentos
            hide(CharacterFragment())
            hide(LocationFragment())
            hide(EpisodesFragment())
            // Mostrar el fragmento deseado
            show(fragment)
            // Confirmar la transacción
            commit()
        }
    }
}
