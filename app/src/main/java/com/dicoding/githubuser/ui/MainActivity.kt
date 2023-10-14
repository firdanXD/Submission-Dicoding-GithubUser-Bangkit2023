package com.dicoding.githubuser.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.adapter.UserAdapter
import com.dicoding.githubuser.data.response.items
import com.dicoding.githubuser.data.viewmodel.MainViewModel
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.favorite.FavoriteActivity
import com.dicoding.githubuser.mode.ModeActivity
import com.dicoding.githubuser.mode.SettingPreferences
import com.dicoding.githubuser.mode.dataStore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    val query = binding.searchView.text.toString()
                    mainViewModel.searchUsers(query)
                    searchBar.text = searchView.text
                    binding.searchView.hide()
                    true
                }
            searchBar.inflateMenu(R.menu.search)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId){
                    R.id.favoritUser -> {
                        val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.theme_settings -> {
                        val intent = Intent(this@MainActivity, ModeActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }


        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        adapter = UserAdapter()
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: items) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USER, data.login)
                    it.putExtra(DetailActivity.EXTRA_AVATAR, data.avatarUrl)
                    it.putExtra(Intent.EXTRA_TITLE, data.login)
                    startActivity(it)
                }
            }
        })

        val modePreferences = SettingPreferences.getInstance(dataStore)
        val modeViewModel = ViewModelProvider(
            this,
            MainViewModel.ViewModelFactory(modePreferences)
        )[MainViewModel::class.java]

        modeViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        mainViewModel.listUsers.observe(this) { list ->
            adapter.List(list)
        }

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onResume() {
        super.onResume()
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.searchBar.menu.findItem(R.id.favoritUser)?.setIcon(R.drawable.baseline_favorite_24)
            binding.searchBar.menu.findItem(R.id.theme_settings)?.setIcon(R.drawable.baseline_bedtime_24)
        } else {
            binding.searchBar.menu.findItem(R.id.favoritUser)?.setIcon(R.drawable.baseline_favorite_24_dark)
            binding.searchBar.menu.findItem(R.id.theme_settings)?.setIcon(R.drawable.baseline_wb_sunny_24)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

}
