package com.baiganov.devlife.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baiganov.devlife.databinding.ItemPostBinding
import com.baiganov.devlife.models.ResultItem

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var data = emptyList<ResultItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(newData: List<ResultItem>) {
        data = newData
        notifyDataSetChanged()
    }

    class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ResultItem) {
            binding.model = model
            binding.executePendingBindings()
        }

        companion object {

            fun from(parent: ViewGroup): PostViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemPostBinding.inflate(inflater, parent, false)
                return PostViewHolder(binding)
            }
        }
    }
}