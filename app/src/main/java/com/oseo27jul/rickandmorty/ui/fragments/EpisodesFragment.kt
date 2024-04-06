package com.oseo27jul.rickandmorty.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oseo27jul.rickandmorty.R
import com.oseo27jul.rickandmorty.data.model.Episodes

import com.oseo27jul.rickandmorty.databinding.FragmentEpisodesBinding
import com.oseo27jul.rickandmorty.ui.MainActivity
import com.oseo27jul.rickandmorty.ui.adapter.EpisodesAdapter
import com.oseo27jul.rickandmorty.ui.adapter.onItemClickListen



import com.oseo27jul.rickandmorty.ui.viewmodel.EpisodesViewModel

class EpisodesFragment : Fragment() {

    lateinit var binding: FragmentEpisodesBinding
    private val episodeViewModel : EpisodesViewModel by viewModels()

    private val episodesAdapter= EpisodesAdapter(object : onItemClickListen {
        override fun onItemClick(episodes: Episodes){
            navigateToEpisodeDetail(episodes )
        }


    })

    private fun navigateToEpisodeDetail(episodes: Episodes) {
        val bundle = Bundle().apply {
            putParcelable("episodes",episodes)
        }
        val fragment = EpisodeDetail().apply {
            arguments = bundle
        }

        // Obtener la instancia de FragmentManager del MainActivity
        val fragmentManager = requireActivity().supportFragmentManager

        // Llamar al método showSecondaryFragment() en el MainActivity para mostrar el fragmento secundario
        (requireActivity() as MainActivity).showSecondaryFragment(fragment)
    }

    /*private fun navigateToEpisodeDetail(episodes: Episodes) {
        val bundle = Bundle().apply {
            putParcelable("episodes",episodes)
        }
        val fragment = EpisodeDetail().apply {
            arguments = bundle
        }
        parentFragmentManager.beginTransaction()

            .replace(R.id.containerPrincipal, fragment)
            .addToBackStack(null)
            .commit()
    }*/




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpisodesBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if(savedInstanceState == null) {

            binding.recyclerViewEpisodes.layoutManager = GridLayoutManager(requireContext(),1)
            binding.recyclerViewEpisodes.adapter= episodesAdapter
            if(episodeViewModel.episodes.value == null){
                episodeViewModel.onCreate(1)
            }
            //
            episodeViewModel.episodes.observe(viewLifecycleOwner) {
                episodesAdapter.submitList(it)

            }



            binding.recyclerViewEpisodes.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!episodeViewModel.isLoading && !episodeViewModel.isLastPage) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition > 0) {
                            episodeViewModel.loadMoreEpisodes()
                        }
                    }
                }
            })

            episodeViewModel.isLoading2.observe(viewLifecycleOwner) { isLoading ->
                binding.progressBarEpisodes.visibility = if (isLoading) View.VISIBLE else View.GONE
            }


            binding.searchViewEpisodes.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    episodesAdapter.filter.filter(newText)
                    return true
                }
            })

            episodeViewModel.selectedCharacter.observe(viewLifecycleOwner) { episodes ->
                episodes?.let {
                    navigateToEpisodeDetail(it)
                }
            }
        }

    }



}