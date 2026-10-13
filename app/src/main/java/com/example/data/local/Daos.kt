package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {
    @Query("SELECT * FROM science_topics ORDER BY grade ASC")
    fun getAllTopics(): Flow<List<TopicEntity>>

    @Query("SELECT * FROM science_topics WHERE grade = :grade")
    fun getTopicsByGrade(grade: Int): Flow<List<TopicEntity>>

    @Query("SELECT * FROM science_topics WHERE id = :id LIMIT 1")
    suspend fun getTopicById(id: String): TopicEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopics(topics: List<TopicEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: TopicEntity)

    @Query("SELECT COUNT(*) FROM science_topics")
    suspend fun getTopicCount(): Int
}

@Dao
interface LessonDao {
    @Query("SELECT * FROM science_lessons ORDER BY grade ASC")
    fun getAllLessons(): Flow<List<LessonEntity>>

    @Query("SELECT * FROM science_lessons WHERE grade = :grade")
    fun getLessonsByGrade(grade: Int): Flow<List<LessonEntity>>

    @Query("SELECT * FROM science_lessons WHERE topicId = :topicId")
    fun getLessonsByTopic(topicId: String): Flow<List<LessonEntity>>

    @Query("SELECT * FROM science_lessons WHERE id = :id LIMIT 1")
    fun getLessonById(id: String): Flow<LessonEntity?>

    @Query("SELECT * FROM science_lessons WHERE id = :id LIMIT 1")
    suspend fun getLessonByIdDirect(id: String): LessonEntity?

    @Query("SELECT * FROM science_lessons WHERE titleEn LIKE '%' || :query || '%' OR titleYo LIKE '%' || :query || '%'")
    fun searchLessons(query: String): Flow<List<LessonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: LessonEntity)

    @Update
    suspend fun updateLesson(lesson: LessonEntity)

    @Query("UPDATE science_lessons SET isCompleted = :completed WHERE id = :lessonId")
    suspend fun markLessonCompleted(lessonId: String, completed: Boolean)

    @Query("DELETE FROM science_lessons WHERE id = :id")
    suspend fun deleteLesson(id: String)

    @Query("SELECT COUNT(*) FROM science_lessons")
    suspend fun getLessonCount(): Int

    @Transaction
    @Query("SELECT * FROM science_lessons WHERE id = :lessonId LIMIT 1")
    fun getLessonWithQuiz(lessonId: String): Flow<LessonWithQuiz?>

    @Transaction
    @Query("SELECT * FROM science_lessons WHERE id = :lessonId LIMIT 1")
    suspend fun getLessonWithQuizDirect(lessonId: String): LessonWithQuiz?

    @Transaction
    @Query("SELECT * FROM science_lessons ORDER BY grade ASC")
    fun getAllLessonsWithQuiz(): Flow<List<LessonWithQuiz>>
}

/**
 * Data Access Object for Quizzes linked to Lessons.
 */
@Dao
interface QuizDao {
    @Query("SELECT * FROM science_quizzes WHERE lessonId = :lessonId LIMIT 1")
    fun getQuizForLesson(lessonId: String): Flow<QuizEntity?>

    @Query("SELECT * FROM science_quizzes WHERE lessonId = :lessonId LIMIT 1")
    suspend fun getQuizForLessonDirect(lessonId: String): QuizEntity?

    @Query("SELECT * FROM science_quizzes WHERE id = :id LIMIT 1")
    fun getQuizById(id: String): Flow<QuizEntity?>

    @Query("SELECT * FROM science_quizzes WHERE id = :id LIMIT 1")
    suspend fun getQuizByIdDirect(id: String): QuizEntity?

    @Query("SELECT * FROM science_quizzes ORDER BY createdAt DESC")
    fun getAllQuizzes(): Flow<List<QuizEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: QuizEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizzes(quizzes: List<QuizEntity>)

    @Query("DELETE FROM science_quizzes WHERE id = :id")
    suspend fun deleteQuiz(id: String)

    @Query("DELETE FROM science_quizzes WHERE lessonId = :lessonId")
    suspend fun deleteQuizForLesson(lessonId: String)

    @Query("SELECT COUNT(*) FROM science_quizzes")
    suspend fun getQuizCount(): Int
}

@Dao
interface LessonProgressDao {
    @Query("SELECT * FROM lesson_progress WHERE learnerId = :learnerId")
    fun getAllProgressForLearner(learnerId: String = "default_learner"): Flow<List<LessonProgressEntity>>

    @Query("SELECT * FROM lesson_progress")
    fun getAllProgress(): Flow<List<LessonProgressEntity>>

    @Query("SELECT * FROM lesson_progress WHERE lessonId = :lessonId LIMIT 1")
    suspend fun getProgressForLesson(lessonId: String): LessonProgressEntity?

    @Query("SELECT * FROM lesson_progress WHERE lessonId = :lessonId AND learnerId = :learnerId LIMIT 1")
    fun observeProgressForLesson(lessonId: String, learnerId: String = "default_learner"): Flow<LessonProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: LessonProgressEntity)

    @Query("SELECT COUNT(*) FROM lesson_progress WHERE isCompleted = 1 AND learnerId = :learnerId")
    fun getCompletedLessonsCount(learnerId: String = "default_learner"): Flow<Int>

    @Query("SELECT SUM(starsEarned) FROM lesson_progress WHERE learnerId = :learnerId")
    fun getTotalStarsFromProgress(learnerId: String = "default_learner"): Flow<Int?>

    @Query("SELECT * FROM lesson_progress WHERE learnerId = :learnerId")
    suspend fun getAllProgressForLearnerDirect(learnerId: String = "default_learner"): List<LessonProgressEntity>
}

@Dao
interface QuizSubmissionDao {
    @Query("SELECT * FROM quiz_submissions ORDER BY timestamp DESC")
    fun getAllSubmissions(): Flow<List<QuizSubmissionEntity>>

    @Query("SELECT * FROM quiz_submissions ORDER BY timestamp DESC")
    suspend fun getAllSubmissionsDirect(): List<QuizSubmissionEntity>

    @Query("SELECT * FROM quiz_submissions WHERE learnerId = :learnerId ORDER BY timestamp DESC")
    fun getSubmissionsByLearner(learnerId: String): Flow<List<QuizSubmissionEntity>>

    @Query("SELECT * FROM quiz_submissions WHERE quizId = :quizId ORDER BY timestamp DESC")
    fun getSubmissionsForQuiz(quizId: String): Flow<List<QuizSubmissionEntity>>

    @Query("SELECT * FROM quiz_submissions WHERE quizId = :quizId AND learnerId = :learnerId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestSubmissionForQuiz(quizId: String, learnerId: String = "default_learner"): Flow<QuizSubmissionEntity?>

    @Query("SELECT * FROM quiz_submissions WHERE isSynced = 0 ORDER BY timestamp ASC")
    fun getUnsyncedSubmissions(): Flow<List<QuizSubmissionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubmission(submission: QuizSubmissionEntity)

    @Query("UPDATE quiz_submissions SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: String)

    @Query("SELECT AVG(percentage) FROM quiz_submissions WHERE learnerId = :learnerId")
    fun getAverageQuizScore(learnerId: String = "default_learner"): Flow<Double?>
}

@Dao
interface LearnerDao {
    @Query("SELECT * FROM learner_profile WHERE id = :id LIMIT 1")
    fun getProfile(id: String = "default_learner"): Flow<LearnerProfileEntity?>

    @Query("SELECT * FROM learner_profile WHERE id = :id LIMIT 1")
    suspend fun getProfileDirect(id: String = "default_learner"): LearnerProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: LearnerProfileEntity)

    @Query("UPDATE learner_profile SET totalStars = totalStars + :addedStars WHERE id = :id")
    suspend fun addStars(id: String, addedStars: Int)

    @Query("UPDATE learner_profile SET streakDays = :streakDays WHERE id = :id")
    suspend fun updateStreak(id: String, streakDays: Int)
}

@Dao
interface SyncQueueDao {
    @Query("SELECT * FROM sync_queue WHERE status = 'PENDING' ORDER BY createdAt ASC")
    fun getPendingQueue(): Flow<List<SyncQueueEntity>>

    @Query("SELECT * FROM sync_queue ORDER BY createdAt DESC LIMIT 20")
    fun getRecentQueueHistory(): Flow<List<SyncQueueEntity>>

    @Query("SELECT COUNT(*) FROM sync_queue WHERE status = 'PENDING'")
    fun getPendingCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun enqueueItem(item: SyncQueueEntity): Long

    @Query("UPDATE sync_queue SET status = 'SYNCED' WHERE queueId = :queueId")
    suspend fun markSynced(queueId: Long)

    @Query("UPDATE sync_queue SET status = 'SYNCED' WHERE status = 'PENDING'")
    suspend fun markAllSynced()

    @Query("DELETE FROM sync_queue WHERE status = 'SYNCED'")
    suspend fun clearSynced()
}

@Dao
interface CustomTeacherLessonDao {
    @Query("SELECT * FROM custom_teacher_lessons ORDER BY createdAt DESC")
    fun getAllCustomLessons(): Flow<List<CustomTeacherLessonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: CustomTeacherLessonEntity)

    @Query("DELETE FROM custom_teacher_lessons WHERE id = :id")
    suspend fun deleteLesson(id: String)
}

@Dao
interface SimulationDao {
    @Query("SELECT * FROM science_simulations ORDER BY grade ASC, titleEn ASC")
    fun getAllSimulations(): Flow<List<SimulationEntity>>

    @Query("SELECT * FROM science_simulations ORDER BY grade ASC, titleEn ASC")
    suspend fun getAllSimulationsDirect(): List<SimulationEntity>

    @Query("SELECT * FROM science_simulations WHERE category = :category LIMIT 1")
    fun getSimulationByCategory(category: String): Flow<SimulationEntity?>

    @Query("SELECT * FROM science_simulations WHERE id = :id LIMIT 1")
    fun getSimulationById(id: String): Flow<SimulationEntity?>

    @Query("SELECT * FROM science_simulations WHERE id = :id LIMIT 1")
    suspend fun getSimulationByIdDirect(id: String): SimulationEntity?

    @Query("SELECT COUNT(*) FROM science_simulations")
    suspend fun getSimulationCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSimulations(simulations: List<SimulationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSimulation(simulation: SimulationEntity)

    @Query("UPDATE science_simulations SET timesInteracted = timesInteracted + 1, lastInteractedAt = :timestamp WHERE id = :id")
    suspend fun recordInteraction(id: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE science_simulations SET starsAwarded = starsAwarded + :stars WHERE id = :id")
    suspend fun awardSimulationStars(id: String, stars: Int)
}

@Dao
interface ParticipationRoomDao {
    @Query("SELECT * FROM participation_rooms WHERE status = 'ACTIVE' ORDER BY createdAt DESC LIMIT 1")
    fun getActiveRoom(): Flow<ParticipationRoomEntity?>

    @Query("SELECT * FROM participation_rooms WHERE roomCode = :roomCode LIMIT 1")
    fun getRoomByCode(roomCode: String): Flow<ParticipationRoomEntity?>

    @Query("SELECT * FROM participation_rooms WHERE roomCode = :roomCode LIMIT 1")
    suspend fun getRoomByCodeDirect(roomCode: String): ParticipationRoomEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateRoom(room: ParticipationRoomEntity)

    @Query("UPDATE participation_rooms SET activeQuestionEn = :qEn, activeQuestionYo = :qYo, activeQuestionOptionsJson = :optsJson, correctAnswerIndex = :correctIdx WHERE id = :roomId")
    suspend fun updateActiveQuestion(roomId: String, qEn: String, qYo: String, optsJson: String, correctIdx: Int)

    @Query("SELECT * FROM room_learner_responses WHERE roomId = :roomId ORDER BY timestamp DESC")
    fun getAllResponsesForRoom(roomId: String): Flow<List<RoomLearnerResponseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(response: RoomLearnerResponseEntity)

    @Query("UPDATE room_learner_responses SET handRaised = :raised WHERE roomId = :roomId AND learnerId = :learnerId")
    suspend fun updateHandRaised(roomId: String, learnerId: String, raised: Boolean)
}

@Dao
interface BadgeDao {
    @Query("SELECT * FROM student_badges ORDER BY isUnlocked DESC, id ASC")
    fun getAllBadges(): Flow<List<BadgeEntity>>

    @Query("SELECT * FROM student_badges WHERE isUnlocked = 1 ORDER BY unlockedAt DESC")
    fun getUnlockedBadges(): Flow<List<BadgeEntity>>

    @Query("SELECT * FROM student_badges WHERE id = :id LIMIT 1")
    suspend fun getBadgeById(id: String): BadgeEntity?

    @Query("SELECT * FROM student_badges")
    suspend fun getAllBadgesDirect(): List<BadgeEntity>

    @Query("SELECT COUNT(*) FROM student_badges")
    suspend fun getBadgeCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<BadgeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadge(badge: BadgeEntity)

    @Query("UPDATE student_badges SET isUnlocked = 1, unlockedAt = :unlockedAt, progress = targetCount WHERE id = :id")
    suspend fun unlockBadge(id: String, unlockedAt: Long = System.currentTimeMillis())

    @Query("UPDATE student_badges SET progress = :progress WHERE id = :id")
    suspend fun updateBadgeProgress(id: String, progress: Int)

    @Query("SELECT COUNT(*) FROM student_badges WHERE isUnlocked = 1")
    fun getUnlockedBadgeCountFlow(): Flow<Int>
}

