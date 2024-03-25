package com.oseo27jul.rickandmorty.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oseo27jul.rickandmorty.data.model.Character
import com.oseo27jul.rickandmorty.data.model.CharactersModel
import com.oseo27jul.rickandmorty.databinding.CharacterItemBinding

interface OnItemClickListener {
    fun onItemClick(pokemonName: Character)
}

class CharacterAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Character, CharacterAdapter.CharacterViewHolder>(CharacterListDiffCallback()),Filterable {


    private var characterListFull = listOf<Character>()

    inner class CharacterViewHolder(private val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.character = character
            Glide.with(itemView.context)
                .load(character.image)
                .into(binding.imageViewCharacter)
            itemView.setOnClickListener { listener.onItemClick(character) }

        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val character = getItem(position)
                    listener.onItemClick(character)
                }
            }
            characterListFull = currentList.toList()
        }

    }



    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character: Character = getItem(position)
        holder.bind(character)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding =
            CharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    class CharacterListDiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            // Compara si los identificadores únicos de los personajes son iguales
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            // Compara si los contenidos de los personajes son iguales
            return oldItem == newItem
        }
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Character>()
                val filterPattern = constraint.toString().trim().toLowerCase()

                if (filterPattern.isEmpty()) {
                    filteredList.addAll(characterListFull)
                } else {
                    for (character in characterListFull) {
                        if (character.name.toLowerCase().contains(filterPattern)) {
                            filteredList.add(character)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as List<Character>)
            }
        }
    }

}


