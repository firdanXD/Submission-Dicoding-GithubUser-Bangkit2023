package com.dicoding.githubuser.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityFavoriteBinding
import com.dicoding.githubuser.db.UserEntity
import com.dicoding.githubuser.ui.DetailActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.favorite)

        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavoriteData().observe(this) {
            setFavoriteData(it)
        }

        favoriteViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setFavoriteData(userEntities: List<UserEntity>) {
        val items = arrayListOf<UserEntity>()
        userEntities.map {
            val item = UserEntity(
                username = it.username,
                avatar = it.avatar
            )
            items.add(item)
        }
        val adapter = FavoriteAdapter(items)
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity) {
                startActivity(
                    Intent(this@FavoriteActivity,DetailActivity::class.java)
                        .putExtra(DetailActivity.EXTRA_USER, data.username)
                        .putExtra(DetailActivity.EXTRA_AVATAR, data.avatar)
                )
            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModel.ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }
}