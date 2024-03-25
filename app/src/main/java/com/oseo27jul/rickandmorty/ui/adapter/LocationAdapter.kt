package com.oseo27jul.rickandmorty.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


import com.oseo27jul.rickandmorty.data.model.Locations

import com.oseo27jul.rickandmorty.databinding.LocationItemBinding



class LocationAdapter(private val listener: onItemClickListeners):
        ListAdapter<Locations,LocationAdapter.LocationAdapterViewHolder>(LocationListDiffCallback()),Filterable {

        private var listLocations = listOf<Locations>()
    inner class LocationAdapterViewHolder(private val binding: LocationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(location: Locations) {
            binding.location = location
            itemView.setOnClickListener { listener.onItemClick(location) }

        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val character = getItem(position)
                    listener.onItemClick(character)
                }
            }
            listLocations = currentList.toList()
        }

    }
    class LocationListDiffCallback : DiffUtil.ItemCallback<Locations>() {
        override fun areItemsTheSame(oldItem: Locations, newItem: Locations): Boolean {
            // Compara si los identificadores únicos de los personajes son iguales
            return oldItem.id == newItem.id
        }
        

        override fun areContentsTheSame(oldItem: Locations, newItem: Locations): Boolean {
            return  oldItem == newItem
        }
    }

    

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationAdapterViewHolder {
        val binding =
            LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationAdapterViewHolder, position: Int) {
        val locations: Locations = getItem(position)
        holder.bind(locations)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Locations>()
                val filterPattern = constraint.toString().trim().lowercase()

                if (filterPattern.isEmpty()) {
                    filteredList.addAll(listLocations)
                } else {
                    for (locations in listLocations) {
                        if (locations.name.lowercase().contains(filterPattern)) {
                            filteredList.add(locations)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                results?.let { it ->
                    val filteredList = it.values as? List<Locations>
                    filteredList?.let { submitList(it) }
                }
            }

        }
    }
}

interface onItemClickListeners {

    fun onItemClick(location: Locations)
}

