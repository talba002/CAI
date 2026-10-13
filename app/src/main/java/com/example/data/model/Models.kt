package com.example.data.model

enum class LanguageMode(val displayName: String, val yorubaName: String) {
    BILINGUAL("Bilingual (Mejeeji)", "Àpapọ̀"),
    YORUBA("Yorùbá Nìkan", "Yorùbá"),
    ENGLISH("English Only", "Gẹ̀ẹ́sì")
}

enum class UserRole {
    PUPIL,
    TEACHER,
    ADMIN
}

data class LearnerProfile(
    val id: String = "learner_default",
    val name: String = "Abbas Talba",
    val avatarEmoji: String = "🚀",
    val gradeLevel: Int = 1, // Primary 1
    val preferredLanguage: LanguageMode = LanguageMode.YORUBA,
    val totalStars: Int = 12,
    val streakDays: Int = 3,
    val completedLessonsCount: Int = 2,
    val audioSpeed: Float = 0.9f, // slightly slower for young learners
    val soundEffectsEnabled: Boolean = true
)

data class ScienceTopic(
    val id: String,
    val grade: Int,
    val titleEn: String,
    val titleYo: String,
    val iconEmoji: String,
    val colorHex: Long,
    val descriptionEn: String,
    val descriptionYo: String,
    val lessons: List<Lesson>
)

data class Lesson(
    val id: String,
    val topicId: String,
    val grade: Int,
    val titleEn: String,
    val titleYo: String,
    val summaryEn: String,
    val summaryYo: String,
    val iconEmoji: String,
    val isCompleted: Boolean = false,
    val isCachedOffline: Boolean = true,
    val sections: List<LessonSection>,
    val vocabulary: List<VocabularyWord>,
    val experimentGuide: ExperimentGuide? = null,
    val quiz: Quiz
)

data class LessonSection(
    val id: String,
    val headingEn: String,
    val headingYo: String,
    val contentEn: String,
    val contentYo: String,
    val funFactEn: String? = null,
    val funFactYo: String? = null,
    val iconEmoji: String = "🔬"
)

data class VocabularyWord(
    val english: String,
    val yoruba: String,
    val toneMarks: String, // e.g. "Dọ̀-Rẹ-Mí"
    val definitionEn: String,
    val definitionYo: String,
    val emoji: String
)

data class ExperimentGuide(
    val titleEn: String,
    val titleYo: String,
    val materialsEn: List<String>,
    val materialsYo: List<String>,
    val stepsEn: List<String>,
    val stepsYo: List<String>,
    val outcomeEn: String,
    val outcomeYo: String
)

data class Quiz(
    val id: String,
    val lessonId: String,
    val titleEn: String,
    val titleYo: String,
    val passingScorePercentage: Int = 60,
    val questions: List<QuizQuestion>
)

data class QuizQuestion(
    val id: String,
    val promptEn: String,
    val promptYo: String,
    val optionsEn: List<String>,
    val optionsYo: List<String>,
    val correctIndex: Int,
    val explanationEn: String,
    val explanationYo: String,
    val iconEmoji: String = "❓"
)

data class QuizSubmission(
    val id: String,
    val quizId: String,
    val learnerId: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val passed: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)

data class AchievementBadge(
    val id: String,
    val titleEn: String,
    val titleYo: String,
    val descriptionEn: String,
    val descriptionYo: String,
    val emoji: String,
    val isUnlocked: Boolean,
    val unlockedDate: String? = null
)

data class SyncStatus(
    val isOnline: Boolean,
    val pendingUploadsCount: Int,
    val lastSyncTime: String,
    val isSyncing: Boolean = false,
    val serverEndpoint: String = "https://yoruba-basic-science-cai.base44.app/api"
)
