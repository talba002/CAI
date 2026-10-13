package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.*
import com.example.data.model.*
import com.example.data.repository.ScienceRepository
import com.example.util.AudioNarrator
import com.example.util.SoundPlayer
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ScienceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ScienceRepository(AppDatabase.getInstance(application))
    val audioNarrator = AudioNarrator(application)

    private val _currentLanguageMode = MutableStateFlow(LanguageMode.BILINGUAL)
    val currentLanguageMode: StateFlow<LanguageMode> = _currentLanguageMode.asStateFlow()

    private val _currentUserRole = MutableStateFlow(UserRole.PUPIL)
    val currentUserRole: StateFlow<UserRole> = _currentUserRole.asStateFlow()

    val learnerProfile: StateFlow<LearnerProfile> = repository.getLearnerProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LearnerProfile())

    private val _selectedGrade = MutableStateFlow<Int?>(1) // Default to Primary 1
    val selectedGrade: StateFlow<Int?> = _selectedGrade.asStateFlow()

    private val _allTopics = MutableStateFlow(repository.getAllTopics())
    val allTopics: StateFlow<List<ScienceTopic>> = _allTopics.asStateFlow()

    fun refreshTopics() {
        _allTopics.value = repository.getAllTopics()
    }

    fun uploadLesson(lesson: Lesson) {
        viewModelScope.launch {
            repository.uploadCompleteLesson(lesson)
            refreshTopics()
        }
    }

    fun awardGameStars(stars: Int) {
        viewModelScope.launch {
            repository.addStars(stars)
            SoundPlayer.playStarEarned()
        }
    }

    private val _activeLesson = MutableStateFlow<Lesson?>(null)
    val activeLesson: StateFlow<Lesson?> = _activeLesson.asStateFlow()

    // Quiz state
    private val _activeQuiz = MutableStateFlow<Quiz?>(null)
    val activeQuiz: StateFlow<Quiz?> = _activeQuiz.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _selectedOption = MutableStateFlow<Int?>(null)
    val selectedOption: StateFlow<Int?> = _selectedOption.asStateFlow()

    private val _hasAnsweredCurrent = MutableStateFlow(false)
    val hasAnsweredCurrent: StateFlow<Boolean> = _hasAnsweredCurrent.asStateFlow()

    private val _correctCount = MutableStateFlow(0)
    val correctCount: StateFlow<Int> = _correctCount.asStateFlow()

    private val _quizSubmissionResult = MutableStateFlow<QuizSubmission?>(null)
    val quizSubmissionResult: StateFlow<QuizSubmission?> = _quizSubmissionResult.asStateFlow()

    // Sync State
    val pendingSyncCount: StateFlow<Int> = repository.getPendingSyncCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val recentSyncHistory: StateFlow<List<SyncQueueEntity>> = repository.getRecentSyncQueue()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _syncMessage = MutableStateFlow<String?>(null)
    val syncMessage: StateFlow<String?> = _syncMessage.asStateFlow()

    // Teacher & Custom lessons
    val customLessons: StateFlow<List<CustomTeacherLessonEntity>> = repository.getCustomLessons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _teacherAuthenticated = MutableStateFlow(false)
    val teacherAuthenticated: StateFlow<Boolean> = _teacherAuthenticated.asStateFlow()

    val allSubmissions: StateFlow<List<QuizSubmissionEntity>> = repository.getAllSubmissions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lessonProgressList: StateFlow<List<LessonProgressEntity>> = repository.getAllProgress()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val offlineLessons: StateFlow<List<LessonEntity>> = repository.getOfflineLessonsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Simulations from Room
    val simulations: StateFlow<List<SimulationEntity>> = repository.getAllSimulations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedSimulationId = MutableStateFlow<String?>("sim_solar_system")
    val selectedSimulationId: StateFlow<String?> = _selectedSimulationId.asStateFlow()

    // Participation Room from Room
    val activeParticipationRoom: StateFlow<ParticipationRoomEntity?> = repository.getActiveParticipationRoom()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val roomResponses: StateFlow<List<RoomLearnerResponseEntity>> = activeParticipationRoom
        .flatMapLatest { room ->
            if (room != null) repository.getRoomResponses(room.id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isHandRaised = MutableStateFlow(false)
    val isHandRaised: StateFlow<Boolean> = _isHandRaised.asStateFlow()

    // Badges from Room
    val badges: StateFlow<List<BadgeEntity>> = repository.getAllBadgesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unlockedBadges: StateFlow<List<BadgeEntity>> = repository.getUnlockedBadgesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unlockedBadgeCount: StateFlow<Int> = repository.getUnlockedBadgeCountFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _celebrationBadge = MutableStateFlow<BadgeEntity?>(null)
    val celebrationBadge: StateFlow<BadgeEntity?> = _celebrationBadge.asStateFlow()

    fun dismissCelebrationBadge() {
        _celebrationBadge.value = null
    }

    fun triggerBadgeEvaluation() {
        viewModelScope.launch {
            val newlyUnlocked = repository.evaluateBadges("default_learner")
            if (newlyUnlocked.isNotEmpty()) {
                _celebrationBadge.value = newlyUnlocked.first()
            }
        }
    }

    init {
        // Seed Room database tables with offline curriculum
        viewModelScope.launch {
            repository.seedOfflineDatabaseIfEmpty()
        }

        // Observe profile to sync language preference and speech rate
        viewModelScope.launch {
            learnerProfile.collect { profile ->
                _currentLanguageMode.value = profile.preferredLanguage
                audioNarrator.setSpeechRate(profile.audioSpeed)
            }
        }
    }

    fun setLanguageMode(mode: LanguageMode) {
        _currentLanguageMode.value = mode
        viewModelScope.launch {
            val current = learnerProfile.value
            repository.saveLearnerProfile(current.copy(preferredLanguage = mode))
        }
    }

    fun setUserRole(role: UserRole) {
        _currentUserRole.value = role
    }

    fun selectGrade(grade: Int?) {
        _selectedGrade.value = grade
    }

    fun openLesson(lessonId: String) {
        val lesson = repository.findLessonById(lessonId)
        _activeLesson.value = lesson
        audioNarrator.stop()
    }

    fun startQuizForLesson(lesson: Lesson) {
        _activeQuiz.value = lesson.quiz
        _currentQuestionIndex.value = 0
        _selectedOption.value = null
        _hasAnsweredCurrent.value = false
        _correctCount.value = 0
        _quizSubmissionResult.value = null
        audioNarrator.stop()

        // Read question out loud if sound enabled
        val firstQ = lesson.quiz.questions.firstOrNull()
        if (firstQ != null && learnerProfile.value.soundEffectsEnabled) {
            val textToRead = when (currentLanguageMode.value) {
                LanguageMode.YORUBA -> firstQ.promptYo
                LanguageMode.ENGLISH -> firstQ.promptEn
                LanguageMode.BILINGUAL -> "${firstQ.promptYo}. ${firstQ.promptEn}"
            }
            speakText(textToRead, currentLanguageMode.value == LanguageMode.YORUBA)
        }
    }

    fun selectQuizAnswer(optionIndex: Int) {
        if (_hasAnsweredCurrent.value) return
        _selectedOption.value = optionIndex
        _hasAnsweredCurrent.value = true

        val quiz = _activeQuiz.value ?: return
        val currentQ = quiz.questions.getOrNull(_currentQuestionIndex.value) ?: return

        if (optionIndex == currentQ.correctIndex) {
            _correctCount.value += 1
            SoundPlayer.playCorrectChime()
        } else {
            SoundPlayer.playIncorrectTone()
        }
    }

    fun nextQuizQuestion() {
        val quiz = _activeQuiz.value ?: return
        if (_currentQuestionIndex.value < quiz.questions.size - 1) {
            _currentQuestionIndex.value += 1
            _selectedOption.value = null
            _hasAnsweredCurrent.value = false
            audioNarrator.stop()

            val nextQ = quiz.questions[_currentQuestionIndex.value]
            val textToRead = when (currentLanguageMode.value) {
                LanguageMode.YORUBA -> nextQ.promptYo
                LanguageMode.ENGLISH -> nextQ.promptEn
                LanguageMode.BILINGUAL -> "${nextQ.promptYo}. ${nextQ.promptEn}"
            }
            speakText(textToRead, currentLanguageMode.value == LanguageMode.YORUBA)
        } else {
            // Finish quiz
            finishQuiz(quiz)
        }
    }

    private fun finishQuiz(quiz: Quiz) {
        val total = quiz.questions.size
        val score = _correctCount.value
        SoundPlayer.playStarEarned()

        viewModelScope.launch {
            val submission = repository.recordQuizSubmission(
                quizId = quiz.id,
                score = score,
                total = total
            )
            _quizSubmissionResult.value = submission
        }
    }

    fun recordInlineQuizSubmission(quizId: String, score: Int, total: Int) {
        viewModelScope.launch {
            val submission = repository.recordQuizSubmission(
                quizId = quizId,
                score = score,
                total = total
            )
            _quizSubmissionResult.value = submission
        }
    }

    fun retakeCurrentQuiz() {
        val lesson = _activeLesson.value ?: return
        startQuizForLesson(lesson)
    }

    fun speakText(text: String, isYoruba: Boolean = false) {
        audioNarrator.speak(text, isYoruba)
    }

    fun stopSpeaking() {
        audioNarrator.stop()
    }

    fun updateProfile(name: String, avatar: String, grade: Int, speed: Float) {
        viewModelScope.launch {
            val updated = learnerProfile.value.copy(
                name = name,
                avatarEmoji = avatar,
                gradeLevel = grade,
                audioSpeed = speed
            )
            repository.saveLearnerProfile(updated)
            audioNarrator.setSpeechRate(speed)
        }
    }

    fun triggerBase44Sync() {
        viewModelScope.launch {
            _isSyncing.value = true
            _syncMessage.value = "À ń sopọ̀ pẹ̀lú Base44... (Connecting to Base44 Cloud...)"
            val result = repository.performBase44Sync()
            _isSyncing.value = false
            _syncMessage.value = result.message
        }
    }

    fun verifyTeacherPin(pin: String): Boolean {
        // Teacher default passcode: 1234
        val valid = pin == "1234"
        _teacherAuthenticated.value = valid
        return valid
    }

    fun logoutTeacher() {
        _teacherAuthenticated.value = false
        _currentUserRole.value = UserRole.PUPIL
    }

    fun createTeacherLesson(
        grade: Int,
        titleEn: String,
        titleYo: String,
        summaryEn: String,
        summaryYo: String,
        contentEn: String,
        contentYo: String,
        author: String
    ) {
        viewModelScope.launch {
            repository.addCustomLesson(
                grade = grade,
                titleEn = titleEn,
                titleYo = titleYo,
                summaryEn = summaryEn,
                summaryYo = summaryYo,
                contentEn = contentEn,
                contentYo = contentYo,
                author = author
            )
        }
    }

    fun deleteTeacherLesson(id: String) {
        viewModelScope.launch {
            repository.deleteCustomLesson(id)
        }
    }

    fun getApiSpecificationMarkdown(): String = repository.getApiSpecificationMarkdown()

    // --- Simulation Actions ---
    fun selectSimulation(simulationId: String) {
        _selectedSimulationId.value = simulationId
    }

    fun recordSimulationInteraction(id: String, stars: Int = 0) {
        viewModelScope.launch {
            repository.recordSimulationActivity(id, stars)
        }
    }

    // --- Participation Room Actions ---
    fun toggleLearnerHandRaise() {
        val next = !_isHandRaised.value
        _isHandRaised.value = next
        viewModelScope.launch {
            val room = activeParticipationRoom.value
            val profile = learnerProfile.value
            if (room != null) {
                repository.toggleLearnerHandRaise(room.id, profile.id, next)
            }
        }
    }

    fun submitRoomLearnerAnswer(optionIndex: Int, isCorrect: Boolean) {
        viewModelScope.launch {
            val room = activeParticipationRoom.value
            val profile = learnerProfile.value
            if (room != null) {
                repository.submitRoomLearnerAnswer(
                    roomId = room.id,
                    learnerId = profile.id,
                    learnerName = profile.name,
                    avatarEmoji = profile.avatarEmoji,
                    selectedOptionIndex = optionIndex,
                    isCorrect = isCorrect
                )
            }
        }
    }

    fun teacherBroadcastQuestion(qEn: String, qYo: String, options: List<String>, correctIdx: Int) {
        viewModelScope.launch {
            val room = activeParticipationRoom.value
            if (room != null) {
                repository.broadcastRoomQuestion(room.id, qEn, qYo, options, correctIdx)
            }
        }
    }

    fun teacherAwardParticipantStar(learnerId: String, stars: Int = 1) {
        viewModelScope.launch {
            repository.awardTeacherBonusStar(learnerId, stars)
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioNarrator.shutdown()
    }
}
