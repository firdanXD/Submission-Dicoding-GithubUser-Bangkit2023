package com.dicoding.githubuser.ui

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubuser.db.UserDao
import com.dicoding.githubuser.db.UserEntity
import com.dicoding.githubuser.db.UserRoom
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailRepositoryUser(application: Application) {
    private val userDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val database = UserRoom.getDatabase(application)
        userDao = database.userDao()
    }

    fun getAllFavoriteData(): LiveData<List<UserEntity>> = userDao.getAllFavoriteData()

    fun insert(user: UserEntity) {
        executorService.execute {
            userDao.insert((user))
        }
    }

    fun delete(user: UserEntity) {
        executorService.execute {
            userDao.delete(user)
        }
    }

    fun getDataByUsername(username: String): LiveData<List<UserEntity>> = userDao.getDataByUsername(username)
}
