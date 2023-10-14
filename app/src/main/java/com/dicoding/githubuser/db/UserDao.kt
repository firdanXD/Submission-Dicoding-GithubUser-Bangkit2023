package com.dicoding.githubuser.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserEntity)

    @Delete
    fun delete(user: UserEntity)

    @Query("SELECT * FROM FavoriteUser ORDER BY username ASC")
    fun getAllFavoriteData(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun getDataByUsername(username: String): LiveData<List<UserEntity>>
}