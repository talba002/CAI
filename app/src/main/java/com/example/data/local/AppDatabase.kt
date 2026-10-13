package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        TopicEntity::class,
        LessonEntity::class,
        QuizEntity::class,
        SimulationEntity::class,
        ParticipationRoomEntity::class,
        RoomLearnerResponseEntity::class,
        BadgeEntity::class,
        LessonProgressEntity::class,
        LearnerProfileEntity::class,
        QuizSubmissionEntity::class,
        SyncQueueEntity::class,
        CustomTeacherLessonEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao
    abstract fun lessonDao(): LessonDao
    abstract fun quizDao(): QuizDao
    abstract fun simulationDao(): SimulationDao
    abstract fun participationRoomDao(): ParticipationRoomDao
    abstract fun badgeDao(): BadgeDao
    abstract fun learnerDao(): LearnerDao
    abstract fun lessonProgressDao(): LessonProgressDao
    abstract fun quizSubmissionDao(): QuizSubmissionDao
    abstract fun syncQueueDao(): SyncQueueDao
    abstract fun customTeacherLessonDao(): CustomTeacherLessonDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "yoruba_science_cai.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Initial seed can be triggered asynchronously
                            CoroutineScope(Dispatchers.IO).launch {
                                // Default learner seed
                                getInstance(context).learnerDao().insertOrUpdateProfile(
                                    LearnerProfileEntity(
                                        id = "default_learner",
                                        name = "Abbas Talba",
                                        avatarEmoji = "🚀",
                                        gradeLevel = 1,
                                        preferredLanguage = "YORUBA",
                                        totalStars = 12,
                                        streakDays = 3,
                                        audioSpeed = 0.9f
                                    )
                                )
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
