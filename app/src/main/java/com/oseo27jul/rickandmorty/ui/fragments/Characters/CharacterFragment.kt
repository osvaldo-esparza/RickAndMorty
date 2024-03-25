package com.oseo27jul.rickandmorty.ui.fragments.Characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oseo27jul.rickandmorty.R
import com.oseo27jul.rickandmorty.data.model.Character
import com.oseo27jul.rickandmorty.databinding.FragmentCharacterBinding
import com.oseo27jul.rickandmorty.ui.adapter.CharacterAdapter
import com.oseo27jul.rickandmorty.ui.adapter.OnItemClickListener
import com.oseo27jul.rickandmorty.ui.viewmodel.CharactersViewModel

class CharacterFragment : Fragment() {
    private lateinit var binding: FragmentCharacterBinding
    private val characterViewModel: CharactersViewModel by viewModels()

    private val characterAdapter = CharacterAdapter(object : OnItemClickListener {
        override fun onItemClick(character: Character) {
            navigateToCharacterDetail(character)
            }

    })

    private fun navigateToCharacterDetail(character: Character) {
        val bundle = Bundle().apply {
            putParcelable("character",character)
        }
        val fragment = CharacterDetail().apply {
            arguments = bundle
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.containerPrincipal, fragment) // Reemplazar con el ID correcto
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewCharacters.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewCharacters.adapter = characterAdapter

        characterViewModel.onCreate(1)
        characterViewModel.characters.observe(viewLifecycleOwner) {
            characterAdapter.submitList(it)
        }

        binding.recyclerViewCharacters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!characterViewModel.isLoading && !characterViewModel.isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition > 0) {
                        characterViewModel.loadMoreCharacter()
                    }
                }
            }
        })

        characterViewModel.isLoading2.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                characterAdapter.filter.filter(newText)
                return true
            }
        })

        characterViewModel.selectedCharacter.observe(viewLifecycleOwner, Observer { character ->
            character?.let {
                navigateToCharacterDetail(it)
            }
        })
    }


}
