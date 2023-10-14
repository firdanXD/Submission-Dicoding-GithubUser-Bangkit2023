package com.dicoding.githubuser.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.databinding.ItemUserBinding
import com.dicoding.githubuser.db.UserEntity
import de.hdodenhof.circleimageview.CircleImageView

class FavoriteAdapter(private val listFavorite: List<UserEntity>) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: UserEntity)
    }

    inner class ViewHolder(binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvUser: TextView = binding.tvAccount
        val ivPhoto: CircleImageView = binding.imgAvatar
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listFavorite.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvUser.text = listFavorite[position].username
        Glide.with(holder.itemView.context)
            .load(listFavorite[position].avatar)
            .into(holder.ivPhoto)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listFavorite[holder.absoluteAdapterPosition])
        }
    }
}