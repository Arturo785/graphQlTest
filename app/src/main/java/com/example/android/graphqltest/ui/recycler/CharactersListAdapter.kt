package com.example.android.graphqltest.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.graphqltest.api.models.Character
import com.example.android.graphqltest.databinding.ItemCharacterBinding


class CharactersListAdapter(
    private val listener: ((item: Character) -> Unit)? = null,
    private val listener2: ((item2: String) -> Unit)? = null,
) :
    ListAdapter<Character, CharactersListAdapter.TaskViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class DiffCallBack : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }


    inner class TaskViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            listener?.let {
                binding.apply {
                    root.setOnClickListener {
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            val current = getItem(position)
                            listener.invoke(current)
                        }
                    }
                }
            }
        }

        fun bind(item: Character) {
            binding.apply {
                characterId.text = item.id
                characterName.text = item.name
                characterSpecie.text = item.specie
            }
        }
    }
}

