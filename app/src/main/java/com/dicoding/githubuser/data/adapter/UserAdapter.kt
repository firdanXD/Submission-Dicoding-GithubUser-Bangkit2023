package com.dicoding.githubuser.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.data.response.items
import com.dicoding.githubuser.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private val listUser = ArrayList<items>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun List(newList: List<items>) {
        val diffResult = DiffUtil.calculateDiff(DIFFCALLBACK(listUser, newList))
        listUser.clear()
        listUser.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(v: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(v.context), v, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    class DIFFCALLBACK(
        private val oldList: List<items>,
        private val newList: List<items>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldList[oldItem].id == newList[newItem].id
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldList[oldItem] == newList[newItem]
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: items)
    }

    override fun getItemCount(): Int = listUser.size

    inner class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: items) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .centerCrop()
                    .into(imgAvatar)
                tvAccount.text = user.login

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }
            }
        }
    }

}