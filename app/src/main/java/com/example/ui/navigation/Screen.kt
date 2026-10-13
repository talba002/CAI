package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Dashboard : Screen("dashboard")
    object TopicLibrary : Screen("topic_library")
    object LessonReader : Screen("lesson_reader")
    object InteractiveQuiz : Screen("interactive_quiz")
    object QuizResults : Screen("quiz_results")
    object Achievements : Screen("achievements")
    object Profile : Screen("profile")
    object TeacherDashboard : Screen("teacher_dashboard")
    object AdminUploadLesson : Screen("admin_upload_lesson")
    object LearningProgress : Screen("learning_progress")
    object LearningSimulation : Screen("learning_simulation")
    object ScienceGames : Screen("science_games")
    object ParticipationRoom : Screen("participation_room")
}
