package com.example.olacak.room


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.olacak.data.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User) : Long

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?


    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: Long): User

}