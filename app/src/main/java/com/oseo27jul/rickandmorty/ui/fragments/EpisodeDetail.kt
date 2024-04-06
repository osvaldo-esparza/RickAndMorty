package com.oseo27jul.rickandmorty.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oseo27jul.rickandmorty.R
import com.oseo27jul.rickandmorty.data.model.Character
import com.oseo27jul.rickandmorty.data.model.Episodes


import com.oseo27jul.rickandmorty.databinding.FragmentEpisodeDetailBinding
import com.oseo27jul.rickandmorty.ui.adapter.CharacterAdapter
import com.oseo27jul.rickandmorty.ui.adapter.OnItemClickListener
import com.oseo27jul.rickandmorty.ui.fragments.Characters.CharacterDetail
import com.oseo27jul.rickandmorty.ui.viewmodel.CharactersViewModel


class EpisodeDetail : Fragment() {

    lateinit var binding : FragmentEpisodeDetailBinding

    private lateinit var episodes: Episodes
    private lateinit var viewModel: CharactersViewModel

    // Variable para rastrear la lista de sublistas de residents
    private val chunkSize = 10 // Tamaño deseado del grupo
    private var page = 0
    private var residentsSize = 0
    private val characterAdapter = CharacterAdapter(object : OnItemClickListener {
        override fun onItemClick(character: Character) {
            navigateToCharacterDetail(character)
        }

    })
    private fun navigateToCharacterDetail(character: Character) {
        val bundle = Bundle().apply {
            putParcelable("character", character)
        }
        val fragment = CharacterDetail().apply {
            arguments = bundle
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.containerPrincipal, fragment) // Reemplazar con el ID correcto
            .addToBackStack(null)
            .commit()
    }

    companion object {
        // Método estático para crear una nueva instancia de CharacterDetail
        fun newInstance(episodes: Episodes): EpisodeDetail {
            val fragment = EpisodeDetail()
            val args = Bundle()
            args.putParcelable("episodes", episodes) // Pasar el objeto Character como argumento
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            episodes = it.getParcelable("episodes")
                ?: throw IllegalArgumentException("Episodes must not be null")
        }

        viewModel = ViewModelProvider(this).get(CharactersViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpisodeDetailBinding.inflate(inflater,container,false)

        episodes?.let {
            binding.episode = episodes
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerViewCharacters.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewCharacters.adapter = characterAdapter
        //divimos el arreglo
        val dividedResidents = splitResidents(episodes.characters, chunkSize)

        if (page < dividedResidents.size) {


            residentsSize = dividedResidents.size


            //viewModel.getCharacterByURL(locations.residents)
            viewModel.getCharacterByURL(dividedResidents[page])
            page++
            viewModel.setResident(false)

            viewModel.characters.observe(viewLifecycleOwner) {
                characterAdapter.submitList(it)
            }

            binding.recyclerViewCharacters.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()


                    if (!viewModel.isLoading && !viewModel.isLastPage) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition > 0) {
                            //Toast.makeText(context,"Scroll",Toast.LENGTH_LONG).show()
                            if (page < dividedResidents.size)
                                viewModel.getCharacterByURL(dividedResidents[page])
                            page++
                        }
                    }
                }
            })

            viewModel.isLoading2.observe(viewLifecycleOwner, Observer { isLoading ->
                binding.progressBarEpisodeDetail.visibility =
                    if (isLoading) View.VISIBLE else View.GONE
            })

        }

        return binding.root
    }


    fun splitResidents(residents: List<String>, chunkSize: Int): List<List<String>> {
        val chunks = mutableListOf<List<String>>()

        var index = 0
        while (index < residents.size) {
            val chunk = residents.subList(index, kotlin.math.min(index + chunkSize, residents.size))
            chunks.add(chunk)
            index += chunkSize
        }

        return chunks
    }




}