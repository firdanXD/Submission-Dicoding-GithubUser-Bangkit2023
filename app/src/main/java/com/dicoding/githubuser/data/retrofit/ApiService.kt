package com.dicoding.githubuser.data.retrofit

import com.dicoding.githubuser.data.response.GithubResponse
import com.dicoding.githubuser.data.response.items
import com.dicoding.githubuser.data.response.SearchUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun getUser(): Call<List<items>>

    @GET("search/users")
    fun searchUser(
        @Query("q") query: String
    ): Call<SearchUserResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<GithubResponse>

    @GET("users/{username}/followers")
    fun getFollower(
        @Path("username") username: String
    ): Call<List<items>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<items>>
}
