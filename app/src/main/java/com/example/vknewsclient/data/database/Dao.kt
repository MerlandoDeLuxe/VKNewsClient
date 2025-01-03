package com.example.vknewsclient.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserData(userIdDbModel: UserIdDbModel)

    @Query("select * from user_id where id = :userId")
    fun getAccessToken(userId: Int): UserIdDbModel

}