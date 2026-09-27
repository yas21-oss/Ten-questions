package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.Question
import com.example.data.model.StageProgress
import com.example.data.model.StringListConverter
import com.example.data.model.UserAccount

@Database(
  entities = [
    UserAccount::class,
    StageProgress::class,
    Question::class,
    QuestionHistoryEntity::class,
    StageAssignmentEntity::class
  ],
  version = 5,
  exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun userDao(): UserDao
  abstract fun stageProgressDao(): StageProgressDao
  abstract fun questionDao(): QuestionDao
  abstract fun questionHistoryDao(): QuestionHistoryDao
  abstract fun stageAssignmentDao(): StageAssignmentDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "ten_questions.db"
        )
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
