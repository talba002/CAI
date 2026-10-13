package com.example.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.*
import com.example.ui.viewmodel.ScienceViewModel

@Composable
fun AppNavigation(
    viewModel: ScienceViewModel,
    navController: NavHostController = rememberNavController()
) {
    val currentLanguageMode by viewModel.currentLanguageMode.collectAsState()
    val learnerProfile by viewModel.learnerProfile.collectAsState()
    val selectedGrade by viewModel.selectedGrade.collectAsState()
    val allTopics by viewModel.allTopics.collectAsState()
    val activeLesson by viewModel.activeLesson.collectAsState()
    val activeQuiz by viewModel.activeQuiz.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()
    val hasAnsweredCurrent by viewModel.hasAnsweredCurrent.collectAsState()
    val quizResult by viewModel.quizSubmissionResult.collectAsState()
    val pendingSyncCount by viewModel.pendingSyncCount.collectAsState()
    val isSpeaking by viewModel.audioNarrator.isSpeaking.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    val syncMessage by viewModel.syncMessage.collectAsState()
    val customLessons by viewModel.customLessons.collectAsState()
    val syncHistory by viewModel.recentSyncHistory.collectAsState()
    val isTeacherAuth by viewModel.teacherAuthenticated.collectAsState()
    val allSubmissions by viewModel.allSubmissions.collectAsState()
    val lessonProgressList by viewModel.lessonProgressList.collectAsState()
    val simulations by viewModel.simulations.collectAsState()
    val selectedSimulationId by viewModel.selectedSimulationId.collectAsState()
    val activeRoom by viewModel.activeParticipationRoom.collectAsState()
    val roomResponses by viewModel.roomResponses.collectAsState()
    val isHandRaised by viewModel.isHandRaised.collectAsState()
    val badges by viewModel.badges.collectAsState()
    val celebrationBadge by viewModel.celebrationBadge.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                currentLanguage = currentLanguageMode,
                onSelectLanguage = { viewModel.setLanguageMode(it) },
                onEnterAsPupil = { name, grade ->
                    viewModel.updateProfile(name, learnerProfile.avatarEmoji, grade, learnerProfile.audioSpeed)
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onEnterAsTeacher = {
                    navController.navigate(Screen.TeacherDashboard.route)
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                profile = learnerProfile,
                topics = if (selectedGrade == null) allTopics else allTopics.filter { it.grade == selectedGrade },
                currentLanguageMode = currentLanguageMode,
                selectedGrade = selectedGrade,
                onSelectGrade = { viewModel.selectGrade(it) },
                onTopicClick = { topic ->
                    val firstLesson = topic.lessons.firstOrNull()
                    if (firstLesson != null) {
                        viewModel.openLesson(firstLesson.id)
                        navController.navigate(Screen.LessonReader.route)
                    } else {
                        navController.navigate(Screen.TopicLibrary.route)
                    }
                },
                onContinueLesson = { lessonId ->
                    viewModel.openLesson(lessonId)
                    navController.navigate(Screen.LessonReader.route)
                },
                onLanguageChange = { viewModel.setLanguageMode(it) },
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onTeacherClick = { navController.navigate(Screen.TeacherDashboard.route) },
                onAchievementsClick = { navController.navigate(Screen.Achievements.route) },
                onSpeakWord = { word, isYo -> viewModel.speakText(word, isYo) },
                pendingSyncCount = pendingSyncCount,
                onTriggerSync = { viewModel.triggerBase44Sync() },
                onSimulationClick = { navController.navigate(Screen.LearningSimulation.route) },
                onGamesClick = { navController.navigate(Screen.ScienceGames.route) },
                onProgressClick = { navController.navigate(Screen.LearningProgress.route) },
                onParticipationRoomClick = { navController.navigate(Screen.ParticipationRoom.route) },
                badges = badges,
                celebrationBadge = celebrationBadge,
                onDismissCelebrationBadge = { viewModel.dismissCelebrationBadge() },
                progressList = lessonProgressList
            )
        }

        composable(Screen.TopicLibrary.route) {
            TopicLibraryScreen(
                topics = allTopics,
                selectedGrade = selectedGrade,
                currentLanguageMode = currentLanguageMode,
                onSelectGrade = { viewModel.selectGrade(it) },
                onSelectLesson = { lessonId ->
                    viewModel.openLesson(lessonId)
                    navController.navigate(Screen.LessonReader.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.LessonReader.route) {
            val lesson = activeLesson
            if (lesson != null) {
                LessonReaderScreen(
                    lesson = lesson,
                    currentLanguageMode = currentLanguageMode,
                    isSpeaking = isSpeaking,
                    onSpeakText = { text, isYo -> viewModel.speakText(text, isYo) },
                    onStopSpeech = { viewModel.stopSpeaking() },
                    audioSpeed = learnerProfile.audioSpeed,
                    onSpeedChange = { newSpeed ->
                        viewModel.updateProfile(learnerProfile.name, learnerProfile.avatarEmoji, learnerProfile.gradeLevel, newSpeed)
                    },
                    onLanguageChange = { viewModel.setLanguageMode(it) },
                    onStartQuiz = {
                        viewModel.startQuizForLesson(lesson)
                        navController.navigate(Screen.InteractiveQuiz.route)
                    },
                    onQuizCompleted = { score, total ->
                        viewModel.recordInlineQuizSubmission(lesson.quiz.id, score, total)
                    },
                    onBack = {
                        viewModel.stopSpeaking()
                        navController.popBackStack()
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }

        composable(Screen.InteractiveQuiz.route) {
            val quiz = activeQuiz
            if (quiz != null) {
                // If quiz completed, navigate to results
                LaunchedEffect(quizResult) {
                    if (quizResult != null) {
                        navController.navigate(Screen.QuizResults.route) {
                            popUpTo(Screen.InteractiveQuiz.route) { inclusive = true }
                        }
                    }
                }

                InteractiveQuizScreen(
                    quiz = quiz,
                    questionIndex = currentQuestionIndex,
                    selectedOption = selectedOption,
                    hasAnswered = hasAnsweredCurrent,
                    currentLanguageMode = currentLanguageMode,
                    isSpeaking = isSpeaking,
                    onSelectOption = { viewModel.selectQuizAnswer(it) },
                    onNextQuestion = { viewModel.nextQuizQuestion() },
                    onSpeakQuestion = {
                        val q = quiz.questions.getOrNull(currentQuestionIndex)
                        if (q != null) {
                            val text = "${q.promptYo}. ${q.promptEn}"
                            viewModel.speakText(text, currentLanguageMode == com.example.data.model.LanguageMode.YORUBA)
                        }
                    },
                    onBack = {
                        viewModel.stopSpeaking()
                        navController.popBackStack()
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }

        composable(Screen.QuizResults.route) {
            val result = quizResult
            if (result != null) {
                QuizResultsScreen(
                    submission = result,
                    onRetakeQuiz = {
                        viewModel.retakeCurrentQuiz()
                        navController.navigate(Screen.InteractiveQuiz.route) {
                            popUpTo(Screen.QuizResults.route) { inclusive = true }
                        }
                    },
                    onReturnHome = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Dashboard.route) { inclusive = true }
                        }
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Dashboard.route)
                }
            }
        }

        composable(Screen.Achievements.route) {
            AchievementsScreen(
                profile = learnerProfile,
                badges = badges,
                currentLanguageMode = currentLanguageMode,
                onSpeak = { text, isYo -> viewModel.speakText(text, isYo) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            LearnerProfileScreen(
                profile = learnerProfile,
                currentLanguageMode = currentLanguageMode,
                pendingSyncCount = pendingSyncCount,
                isSyncing = isSyncing,
                syncMessage = syncMessage,
                onSaveProfile = { name, avatar, grade, speed ->
                    viewModel.updateProfile(name, avatar, grade, speed)
                },
                onLanguageChange = { viewModel.setLanguageMode(it) },
                onTriggerSync = { viewModel.triggerBase44Sync() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.TeacherDashboard.route) {
            TeacherDashboardScreen(
                isAuthenticated = isTeacherAuth,
                onVerifyPin = { viewModel.verifyTeacherPin(it) },
                submissions = allSubmissions,
                customLessons = customLessons,
                syncHistory = syncHistory,
                pendingSyncCount = pendingSyncCount,
                isSyncing = isSyncing,
                syncMessage = syncMessage,
                apiSpecMarkdown = viewModel.getApiSpecificationMarkdown(),
                onCreateLesson = { grade, tEn, tYo, sEn, sYo, cEn, cYo, author ->
                    viewModel.createTeacherLesson(grade, tEn, tYo, sEn, sYo, cEn, cYo, author)
                },
                onDeleteLesson = { viewModel.deleteTeacherLesson(it) },
                onTriggerSync = { viewModel.triggerBase44Sync() },
                onLogout = { viewModel.logoutTeacher() },
                onNavigateToUpload = { navController.navigate(Screen.AdminUploadLesson.route) },
                onNavigateToProgress = { navController.navigate(Screen.LearningProgress.route) },
                onNavigateToSimulation = { navController.navigate(Screen.LearningSimulation.route) },
                onNavigateToGames = { navController.navigate(Screen.ScienceGames.route) },
                onNavigateToParticipationRoom = { navController.navigate(Screen.ParticipationRoom.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.AdminUploadLesson.route) {
            AdminUploadLessonScreen(
                onUploadLesson = { newLesson ->
                    viewModel.uploadLesson(newLesson)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.LearningProgress.route) {
            LearningProgressScreen(
                profile = learnerProfile,
                topics = allTopics,
                submissions = allSubmissions,
                progressList = lessonProgressList,
                currentLanguageMode = currentLanguageMode,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.LearningSimulation.route) {
            LearningSimulationScreen(
                simulations = simulations,
                selectedSimulationId = selectedSimulationId,
                onSelectSimulation = { viewModel.selectSimulation(it) },
                onRecordInteraction = { id, stars -> viewModel.recordSimulationInteraction(id, stars) },
                currentLanguageMode = currentLanguageMode,
                onSpeak = { text, isYo -> viewModel.speakText(text, isYo) },
                onNavigateToParticipationRoom = { navController.navigate(Screen.ParticipationRoom.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ParticipationRoom.route) {
            ParticipationRoomScreen(
                room = activeRoom,
                learnerProfile = learnerProfile,
                responses = roomResponses,
                isHandRaised = isHandRaised,
                currentLanguageMode = currentLanguageMode,
                isTeacherModeInitially = isTeacherAuth,
                onToggleHandRaise = { viewModel.toggleLearnerHandRaise() },
                onSubmitAnswer = { idx, isCorrect -> viewModel.submitRoomLearnerAnswer(idx, isCorrect) },
                onTeacherBroadcastQuestion = { qEn, qYo, opts, correctIdx ->
                    viewModel.teacherBroadcastQuestion(qEn, qYo, opts, correctIdx)
                },
                onTeacherAwardStar = { learnerId, stars ->
                    viewModel.teacherAwardParticipantStar(learnerId, stars)
                },
                onSpeak = { text, isYo -> viewModel.speakText(text, isYo) },
                onNavigateToSimulation = { navController.navigate(Screen.LearningSimulation.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ScienceGames.route) {
            ScienceGamesScreen(
                currentLanguageMode = currentLanguageMode,
                onAwardStars = { stars -> viewModel.awardGameStars(stars) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
