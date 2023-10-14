package com.dicoding.githubuser.data.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubuser.data.response.GithubResponse
import com.dicoding.githubuser.data.response.items
import com.dicoding.githubuser.data.retrofit.ApiConfig
import com.dicoding.githubuser.db.UserEntity
import com.dicoding.githubuser.ui.DetailRepositoryUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _detailUser = MutableLiveData<GithubResponse>()
    val detailUser: LiveData<GithubResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _followers = MutableLiveData<List<items>>()
    val followers: LiveData<List<items>> = _followers

    private val _following = MutableLiveData<List<items>>()
    val following: LiveData<List<items>> = _following

    val errorMessage = MutableLiveData<String>()

    private val detailUserData: DetailRepositoryUser = DetailRepositoryUser(application)

    fun insert(user: UserEntity) {
        detailUserData.insert(user)
    }

    fun delete(user: UserEntity) {
        detailUserData.delete(user)
    }

    fun getDataByUsername(username: String) = detailUserData.getDataByUsername(username)

    fun getDetailUsers(username: String = "") {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.d(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                errorMessage.postValue(t.message.toString())
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowers(username: String = "") {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollower(username)
        client.enqueue(object : Callback<List<items>> {
            override fun onResponse(
                call: Call<List<items>>,
                response: Response<List<items>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followers.value = response.body()
                } else {
                    Log.d(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<items>>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowing(username: String = "") {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<items>> {
            override fun onResponse(
                call: Call<List<items>>,
                response: Response<List<items>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _following.value = response.body()
                } else {
                    Log.d(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<items>>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    class ViewModelFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
        companion object {
            @Volatile
            private var INSTANCE: ViewModelFactory? = null

            @JvmStatic
            fun getInstance(application: Application): ViewModelFactory {
                if (INSTANCE == null) {
                    synchronized(ViewModelFactory::class.java) {
                        INSTANCE = ViewModelFactory(application)
                    }
                }
                return INSTANCE as ViewModelFactory
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun <view : ViewModel> create(modelClass: Class<view>): view {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                return DetailViewModel(application) as view
            }
            throw IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
        }
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}
