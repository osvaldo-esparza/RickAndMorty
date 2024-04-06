package com.oseo27jul.rickandmorty.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oseo27jul.rickandmorty.data.model.Episodes


import com.oseo27jul.rickandmorty.databinding.EpisodeItemBinding



class EpisodesAdapter(private val listener: onItemClickListen):
    ListAdapter<Episodes, EpisodesAdapter.EpisodesAdapterViewHolder>(EpisodesListDiffCallback()),
    Filterable {


    private var listEpisodes = listOf<Episodes>()
    inner class EpisodesAdapterViewHolder(private val binding: EpisodeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(episodes: Episodes) {
            binding.episodes = episodes
            itemView.setOnClickListener { listener.onItemClick(episodes) }

        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val character = getItem(position)
                    listener.onItemClick(character)
                }
            }
            listEpisodes = currentList.toList()
        }

    }
    class EpisodesListDiffCallback : DiffUtil.ItemCallback<Episodes>() {
        override fun areItemsTheSame(oldItem: Episodes, newItem: Episodes): Boolean {
            // Compara si los identificadores únicos de los personajes son iguales
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(oldItem: Episodes, newItem: Episodes): Boolean {
            return  oldItem == newItem
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpisodesAdapterViewHolder {
        val binding =
            EpisodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodesAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: EpisodesAdapterViewHolder,
        position: Int
    ) {
        val episodes: Episodes = getItem(position)
        holder.bind(episodes)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Episodes>()
                val filterPattern = constraint.toString().trim().lowercase()

                if (filterPattern.isEmpty()) {
                    filteredList.addAll(listEpisodes)
                } else {
                    for (episodes in listEpisodes) {
                        if (episodes.name.lowercase().contains(filterPattern)) {
                            filteredList.add(episodes)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val filteredList = results?.values as? List<Episodes>
                filteredList?.let {
                    submitList(it)
                }
            }


        }
    }


}

interface onItemClickListen {

    fun onItemClick(episodes: Episodes)
}
