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

import com.oseo27jul.rickandmorty.data.model.Locations
import com.oseo27jul.rickandmorty.databinding.FragmentLocationBinding

import com.oseo27jul.rickandmorty.ui.adapter.LocationAdapter
import com.oseo27jul.rickandmorty.ui.adapter.onItemClickListeners

import com.oseo27jul.rickandmorty.ui.viewmodel.LocationViewModel


class LocationFragment : Fragment() {

    lateinit var binding: FragmentLocationBinding
    private val locationViewModel: LocationViewModel by viewModels()


    private val locationAdapter= LocationAdapter(object : onItemClickListeners {
        override fun onItemClick(location:Locations){
            navigateToLocationDetail(location)
        }


    })

    private fun navigateToLocationDetail(location: Locations) {
        val bundle = Bundle().apply {
            putParcelable("location",location)
        }
        val fragment = LocationDetail().apply {
            arguments = bundle
        }
        parentFragmentManager.beginTransaction()

            .replace(R.id.containerPrincipal, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLocationBinding.inflate(inflater,container,false)




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if(savedInstanceState == null) {

            binding.recyclerViewLocations.layoutManager = GridLayoutManager(requireContext(),1)
            binding.recyclerViewLocations.adapter= locationAdapter
            if(locationViewModel.locations.value == null){
                locationViewModel.onCreate(1)
            }
          //
            locationViewModel.locations.observe(viewLifecycleOwner) {
                locationAdapter.submitList(it)

            }



            binding.recyclerViewLocations.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!locationViewModel.isLoading && !locationViewModel.isLastPage) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition > 0) {
                            locationViewModel.loadMoreLocation()
                        }
                    }
                }
            })

            locationViewModel.isLoading2.observe(viewLifecycleOwner) { isLoading ->
                binding.progressBarLocation.visibility = if (isLoading) View.VISIBLE else View.GONE
            }


            binding.searchViewLocation.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    locationAdapter.filter.filter(newText)
                    return true
                }
            })

            locationViewModel.selectedCharacter.observe(viewLifecycleOwner) { location ->
                location?.let {
                    navigateToLocationDetail(it)
                }
            }
        }

    }




}