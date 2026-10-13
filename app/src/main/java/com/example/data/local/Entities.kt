package com.example.data.local
 
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

/**
 * Entity for storing Science Topics offline.
 */
@Entity(
    tableName = "science_topics",
    indices = [Index(value = ["grade"])]
)
data class TopicEntity(
    @PrimaryKey val id: String,
    val grade: Int,
    val titleEn: String,
    val titleYo: String,
    val iconEmoji: String,
    val colorHex: Long,
    val descriptionEn: String,
    val descriptionYo: String,
    val lessonCount: Int = 1
)

/**
 * Entity for storing Science Lessons offline.
 * Stores bilingual content, sections, vocabulary, and hands-on experiments as structured JSON.
 */
@Entity(
    tableName = "science_lessons",
    indices = [Index(value = ["topicId"]), Index(value = ["grade"])]
)
data class LessonEntity(
    @PrimaryKey val id: String,
    val topicId: String,
    val grade: Int,
    val titleEn: String,
    val titleYo: String,
    val summaryEn: String,
    val summaryYo: String,
    val iconEmoji: String,
    val isCompleted: Boolean = false,
    val isCachedOffline: Boolean = true,
    val sectionsJson: String,      // Serialized list of LessonSection
    val vocabularyJson: String,    // Serialized list of VocabularyWord
    val experimentJson: String? = null, // Serialized ExperimentGuide
    val quizJson: String? = null,  // Serialized Quiz
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * Entity for storing multiple-choice Quizzes linked to Lessons in the Room database.
 */
@Entity(
    tableName = "science_quizzes",
    foreignKeys = [
        ForeignKey(
            entity = LessonEntity::class,
            parentColumns = ["id"],
            childColumns = ["lessonId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["lessonId"])]
)
data class QuizEntity(
    @PrimaryKey val id: String,
    val lessonId: String,
    val titleEn: String,
    val titleYo: String,
    val passingScorePercentage: Int = 60,
    val questionsJson: String, // Serialized list of QuizQuestion JSON
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Relation class that links a LessonEntity with its associated QuizEntity.
 */
data class LessonWithQuiz(
    @Embedded val lesson: LessonEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "lessonId"
    )
    val quiz: QuizEntity?
)

/**
 * Entity for tracking user learning progress per lesson.
 */
@Entity(
    tableName = "lesson_progress",
    indices = [Index(value = ["lessonId"]), Index(value = ["learnerId"])]
)
data class LessonProgressEntity(
    @PrimaryKey val lessonId: String,
    val learnerId: String = "default_learner",
    val isCompleted: Boolean,
    val highestScorePercentage: Int,
    val completedAt: Long,
    val attemptsCount: Int = 1,
    val starsEarned: Int = 0
)

/**
 * Entity for storing learner profile and learning settings.
 */
@Entity(tableName = "learner_profile")
data class LearnerProfileEntity(
    @PrimaryKey val id: String = "default_learner",
    val name: String,
    val avatarEmoji: String,
    val gradeLevel: Int,
    val preferredLanguage: String,
    val totalStars: Int,
    val streakDays: Int,
    val audioSpeed: Float,
    val soundEffectsEnabled: Boolean = true,
    val lastActiveTimestamp: Long = System.currentTimeMillis()
)

/**
 * Entity for storing quiz submissions and assessment results.
 */
@Entity(
    tableName = "quiz_submissions",
    indices = [Index(value = ["quizId"]), Index(value = ["learnerId"]), Index(value = ["timestamp"])]
)
data class QuizSubmissionEntity(
    @PrimaryKey val id: String,
    val quizId: String,
    val learnerId: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val passed: Boolean,
    val answersJson: String = "[]",
    val timestamp: Long,
    val isSynced: Boolean = false,
    val idempotencyKey: String = ""
)

/**
 * Entity for managing offline synchronization queue to Base44 / remote API.
 */
@Entity(
    tableName = "sync_queue",
    indices = [Index(value = ["status"])]
)
data class SyncQueueEntity(
    @PrimaryKey(autoGenerate = true) val queueId: Long = 0,
    val entityType: String, // "QUIZ_SUBMISSION", "PROGRESS", "CUSTOM_LESSON"
    val payloadJson: String,
    val createdAt: Long,
    val status: String // "PENDING", "SYNCED", "FAILED"
)

/**
 * Entity for teacher-created lessons authored on device.
 */
@Entity(tableName = "custom_teacher_lessons")
data class CustomTeacherLessonEntity(
    @PrimaryKey val id: String,
    val grade: Int,
    val titleEn: String,
    val titleYo: String,
    val summaryEn: String,
    val summaryYo: String,
    val contentEn: String,
    val contentYo: String,
    val authorTeacher: String,
    val isPublished: Boolean,
    val createdAt: Long
)

/**
 * Entity for storing interactive Science Simulations (Solar System, Plant Growth, Water Cycle, etc.) in Room.
 */
@Entity(
    tableName = "science_simulations",
    indices = [Index(value = ["category"]), Index(value = ["grade"])]
)
data class SimulationEntity(
    @PrimaryKey val id: String,
    val titleEn: String,
    val titleYo: String,
    val category: String, // "solar_system", "plant_growth", "water_cycle", "human_senses"
    val grade: Int,
    val descriptionEn: String,
    val descriptionYo: String,
    val iconEmoji: String,
    val conceptKeywords: String,
    val defaultParamsJson: String = "{}",
    val timesInteracted: Int = 0,
    val starsAwarded: Int = 0,
    val lastInteractedAt: Long = 0L
)

/**
 * Entity for Teacher and Learners Participation Room sessions.
 */
@Entity(
    tableName = "participation_rooms",
    indices = [Index(value = ["roomCode"]), Index(value = ["status"])]
)
data class ParticipationRoomEntity(
    @PrimaryKey val id: String,
    val roomCode: String, // e.g. "IMO-101"
    val titleEn: String,
    val titleYo: String,
    val teacherName: String,
    val currentTopic: String,
    val currentSimulationId: String? = null,
    val status: String = "ACTIVE", // "ACTIVE", "PAUSED", "COMPLETED"
    val activeQuestionEn: String = "",
    val activeQuestionYo: String = "",
    val activeQuestionOptionsJson: String = "[]",
    val correctAnswerIndex: Int = 0,
    val learnersJoinedCount: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Entity for tracking learner answers and hand-raising in Participation Rooms.
 */
@Entity(
    tableName = "room_learner_responses",
    indices = [Index(value = ["roomId"]), Index(value = ["learnerId"])]
)
data class RoomLearnerResponseEntity(
    @PrimaryKey val id: String,
    val roomId: String,
    val learnerId: String,
    val learnerName: String,
    val avatarEmoji: String,
    val selectedOptionIndex: Int,
    val isCorrect: Boolean,
    val handRaised: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Entity for storing Badges in Room to reward students for:
 * - Completing lessons
 * - Scoring high on quizzes
 * - Interacting with simulations
 * - Participating in live rooms
 */
@Entity(
    tableName = "student_badges",
    indices = [Index(value = ["category"]), Index(value = ["isUnlocked"])]
)
data class BadgeEntity(
    @PrimaryKey val id: String,
    val titleEn: String,
    val titleYo: String,
    val descriptionEn: String,
    val descriptionYo: String,
    val iconEmoji: String,
    val category: String, // "LESSON", "QUIZ", "SIMULATION", "PARTICIPATION"
    val requirementDescriptionEn: String,
    val requirementDescriptionYo: String,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null,
    val progress: Int = 0,
    val targetCount: Int = 1
)

