package com.dicoding.githubuser.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.dicoding.githubuser.data.response.GithubResponse
import com.dicoding.githubuser.data.response.items
import com.dicoding.githubuser.data.response.SearchUserResponse
import com.dicoding.githubuser.data.retrofit.ApiConfig
import com.dicoding.githubuser.mode.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val prefs: SettingPreferences) : ViewModel() {
    private val _listUsers = MutableLiveData<List<items>>()
    val listUsers: LiveData<List<items>> = _listUsers

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getUsers()
    }

    private fun getUsers() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser()
        client.enqueue(object : Callback<List<items>> {
            override fun onResponse(
                call: Call<List<items>>,
                response: Response<List<items>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _listUsers.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<items>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun searchUsers(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUser(query)
        client.enqueue(object: Callback<SearchUserResponse> {
            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _listUsers.value = response.body()!!.item
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getThemeSetting(): LiveData<Boolean> {
        return prefs.getThemeSetting().asLiveData()
    }

    class ViewModelFactory(private val prefs: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(prefs) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
