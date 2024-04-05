package com.oseo27jul.rickandmorty.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oseo27jul.rickandmorty.R
import com.oseo27jul.rickandmorty.data.model.Character
import com.oseo27jul.rickandmorty.data.model.Locations
import com.oseo27jul.rickandmorty.databinding.FragmentLocationDetailBinding
import com.oseo27jul.rickandmorty.ui.adapter.CharacterAdapter
import com.oseo27jul.rickandmorty.ui.adapter.OnItemClickListener
import com.oseo27jul.rickandmorty.ui.fragments.Characters.CharacterDetail
import com.oseo27jul.rickandmorty.ui.viewmodel.CharactersViewModel
import kotlinx.coroutines.launch


class LocationDetail : Fragment() {

    lateinit var binding: FragmentLocationDetailBinding
    private lateinit var locations: Locations
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
        fun newInstance(locations: Locations): LocationDetail {
            val fragment = LocationDetail()
            val args = Bundle()
            args.putParcelable("location", locations) // Pasar el objeto Character como argumento
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            locations = it.getParcelable("location")
                ?: throw IllegalArgumentException("Locations must not be null")
        }

        viewModel = ViewModelProvider(this).get(CharactersViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationDetailBinding.inflate(inflater, container, false)
        locations?.let {
            binding.location = locations
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerViewResidents.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewResidents.adapter = characterAdapter
        //divimos el arreglo
        val dividedResidents = splitResidents(locations.residents, chunkSize)
        if (page < dividedResidents.size) {


            residentsSize = dividedResidents.size


            //viewModel.getCharacterByURL(locations.residents)
            viewModel.getCharacterByURL(dividedResidents[page])
            page++
            viewModel.setResident(false)

            viewModel.characters.observe(viewLifecycleOwner) {
                characterAdapter.submitList(it)
            }

            binding.recyclerViewResidents.addOnScrollListener(object :
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
                binding.progressBarLocationDetail.visibility =
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