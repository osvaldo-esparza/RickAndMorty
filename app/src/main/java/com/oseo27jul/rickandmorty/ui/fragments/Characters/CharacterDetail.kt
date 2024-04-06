package com.oseo27jul.rickandmorty.ui.fragments.Characters

// CharacterDetail.kt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.oseo27jul.rickandmorty.data.model.Character
import com.oseo27jul.rickandmorty.databinding.FragmentCharacterDetailBinding

class CharacterDetail : Fragment() {

    private lateinit var binding: FragmentCharacterDetailBinding

    private lateinit var character: Character



    companion object {
        // Método estático para crear una nueva instancia de CharacterDetail
        fun newInstance(character: Character): CharacterDetail {
            val fragment = CharacterDetail()
            val args = Bundle()
            args.putParcelable("character", character) // Pasar el objeto Character como argumento
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            character = it.getParcelable("character") ?: throw IllegalArgumentException("Character must not be null")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla y vincula el layout utilizando enlace de datos
        binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        character?.let { noNullCharacter->

            binding.character = character

            Glide.with(this)
                .load(character.image)
                .into(binding.image)

        }

        return binding.root
    }





}
