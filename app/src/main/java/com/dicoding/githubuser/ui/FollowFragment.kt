package com.dicoding.githubuser.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.data.adapter.UserAdapter
import com.dicoding.githubuser.data.response.items
import com.dicoding.githubuser.data.viewmodel.DetailViewModel
import com.dicoding.githubuser.data.viewmodel.MainViewModel
import com.dicoding.githubuser.databinding.FragmentFollowersBinding

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        setAdapter()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var position = 0
        var username = arguments?.getString(USERNAME)

        setAdapter()

        arguments?.let {
            position = it.getInt(POSITION)
            username = it.getString(USERNAME)
        }

        if (position == 1) {
            showLoading(true)
            username?.let {
                detailViewModel.getFollowers(it)
            }
        } else {
            showLoading(true)
            username?.let {
                detailViewModel.getFollowing(it)
            }
        }

        detailViewModel.followers.observe(viewLifecycleOwner) {
            if (position == 1) {
                adapter.List(it)
            }
            showLoading(false)
        }

        detailViewModel.following.observe(viewLifecycleOwner) {
            if (position == 2) {
                adapter.List(it)
            }
            showLoading(false)
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setAdapter() {
        adapter = UserAdapter()
        binding.rvFollowers.adapter = adapter
        binding.rvFollowers.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val POSITION = "position"
        const val USERNAME = "username"
    }
}