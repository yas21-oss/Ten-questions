package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.UserAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
  @Query("SELECT * FROM users LIMIT 1")
  fun getCurrentUser(): Flow<UserAccount?>

  @Query("SELECT * FROM users WHERE uid = :uid LIMIT 1")
  suspend fun getUserById(uid: String): UserAccount?

  @Query("SELECT * FROM users ORDER BY totalStars DESC, xp DESC")
  fun getAllUsersRankedByStars(): Flow<List<UserAccount>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUser(user: UserAccount)

  @Update
  suspend fun updateUser(user: UserAccount)

  @Query("DELETE FROM users")
  suspend fun deleteAllUsers()
}
