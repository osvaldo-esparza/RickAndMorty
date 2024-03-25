package com.oseo27jul.rickandmorty

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.oseo27jul.rickandmorty.R
import com.oseo27jul.rickandmorty.databinding.ActivityMainBinding
import com.oseo27jul.rickandmorty.ui.adapter.TabsPagerAdapter
import com.oseo27jul.rickandmorty.ui.fragments.Characters.CharacterFragment

import com.oseo27jul.rickandmorty.ui.fragments.EpisodesFragment
import com.oseo27jul.rickandmorty.ui.fragments.LocationFragment
import com.oseo27jul.rickandmorty.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //if(savedInstanceState != null){
            // Inicializar el ViewPager y TabLayout
            val viewPager = binding.viewPager
            val tabLayout = binding.tabLayout

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
                        val fragment = when (tab.position) {
                            0 -> CharacterFragment()
                            1 -> LocationFragment()
                            2 -> EpisodesFragment()
                            else -> throw IllegalArgumentException("Position ${tab.position} is not supported")
                        }
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.containerPrincipal, fragment)
                            setReorderingAllowed(true)
                            commit()
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
}
