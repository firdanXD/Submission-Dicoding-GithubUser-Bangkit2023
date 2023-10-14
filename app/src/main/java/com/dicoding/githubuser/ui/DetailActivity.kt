package com.dicoding.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.githubuser.data.adapter.PagerAdapter
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.viewmodel.DetailViewModel
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.db.UserEntity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        DetailViewModel.ViewModelFactory.getInstance(application)
    }

    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USER) ?: ""
        val avatar = intent.getStringExtra(EXTRA_AVATAR) ?: ""
        val bundle = Bundle()
        bundle.putString(EXTRA_USER, username)

        supportActionBar?.title = getString(R.string.detail_user)

        detailViewModel.getDetailUsers(username)
        showLoading(true)

        detailViewModel.detailUser.observe(this) {
            showLoading(false)
            if (it != null) {
                binding.apply {
                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .centerCrop()
                        .into(image)
                    nama.text = it.name
                    tvUsername.text = it.login
                    repoView.text = resources.getString(R.string.data_repos, it.repos)
                    follower.text = resources.getString(R.string.data_follower, it.followers)
                    following.text = resources.getString(R.string.data_following, it.following)
                }
            }
        }

        detailViewModel.getDataByUsername(username).observe(this) {
            isFavorite = it.isNotEmpty()
            val favoriteUser = UserEntity(username, avatar)
            if (it.isEmpty()) {
                binding.btnFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.btnFavorite.context,
                        R.drawable.baseline_favorite_border_24
                    )
                )
                binding.btnFavorite.contentDescription = getString(R.string.favorite_added)
            } else {
                binding.btnFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.btnFavorite.context,
                        R.drawable.baseline_favorite_24_dark
                    )
                )
                binding.btnFavorite.contentDescription = getString(R.string.favorite_removed)
            }

            binding.btnFavorite.setOnClickListener {
                if (isFavorite) {
                    detailViewModel.delete(favoriteUser)
                    Toast.makeText(this, R.string.favorite_removed, Toast.LENGTH_SHORT).show()
                } else {
                    detailViewModel.insert(favoriteUser)
                    Toast.makeText(this, R.string.favorite_added, Toast.LENGTH_SHORT).show()
                }
            }
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.errorMessage.observe(this) {
            binding.apply {
                progressBar.visibility = View.GONE
            }
        }

        supportActionBar?.elevation = 0f

        val sectionsPagerAdapter = PagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = binding.viewpager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tab
        TabLayoutMediator(tabs, viewPager) {
                tabLayout, position ->
            tabLayout.text = resources.getString(TAB_TITLE[position])
        }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLE = arrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }
}
