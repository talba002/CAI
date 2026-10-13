package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.*
import com.example.data.repository.CurriculumDataSource
import com.example.data.repository.ScienceRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Yorùbá CAI", appName)
    }

    @Test
    fun `verify bilingual curriculum topics and quizzes load correctly`() {
        val topics = CurriculumDataSource.topics
        assertTrue("Curriculum should have topics", topics.isNotEmpty())
        val livingThingsTopic = topics.find { it.id == "topic_living_things" }
        assertNotNull("Living things topic should exist", livingThingsTopic)
        assertEquals("Ohun Alààyè àti Ohun Aláìláàyè", livingThingsTopic?.titleYo)

        val firstLesson = livingThingsTopic?.lessons?.firstOrNull()
        assertNotNull("First lesson should exist", firstLesson)
        assertTrue("Lesson should have sections with Yoruba content", firstLesson!!.sections.isNotEmpty())
        assertTrue("Lesson should have quiz questions", firstLesson.quiz.questions.isNotEmpty())
    }

    @Test
    fun `test offline lesson dao and seeding in Room`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getInstance(context)
        val repo = ScienceRepository(db)

        repo.seedOfflineDatabaseIfEmpty()

        val lessonsCount = db.lessonDao().getLessonCount()
        assertTrue("Lessons should be seeded in Room database", lessonsCount > 0)

        val topicCount = db.topicDao().getTopicCount()
        assertTrue("Topics should be seeded in Room database", topicCount > 0)

        val fetchedLesson = db.lessonDao().getLessonByIdDirect("lesson_living_char")
        assertNotNull("Fetched lesson from Room should not be null", fetchedLesson)
        assertEquals("Characteristics of Living Things", fetchedLesson?.titleEn)
        assertEquals("Àwọn Àbùdá Ohun Alààyè", fetchedLesson?.titleYo)
        assertTrue("Lesson should be marked cached offline", fetchedLesson?.isCachedOffline == true)
    }

    @Test
    fun `test user progress dao offline storage and completion tracking`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getInstance(context)
        val progressDao = db.lessonProgressDao()

        val progress = LessonProgressEntity(
            lessonId = "lesson_plant_parts",
            learnerId = "test_pupil_1",
            isCompleted = true,
            highestScorePercentage = 100,
            completedAt = System.currentTimeMillis(),
            attemptsCount = 2,
            starsEarned = 5
        )

        progressDao.saveProgress(progress)

        val saved = progressDao.getProgressForLesson("lesson_plant_parts")
        assertNotNull("Progress should be saved in Room", saved)
        assertEquals(true, saved?.isCompleted)
        assertEquals(100, saved?.highestScorePercentage)
        assertEquals(5, saved?.starsEarned)
    }

    @Test
    fun `test quiz submission dao stores quiz results and calculates analytics`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getInstance(context)
        val repo = ScienceRepository(db)

        val submission = repo.recordQuizSubmission(
            quizId = "quiz_living_char",
            score = 3,
            total = 3
        )

        assertEquals(100, submission.percentage)
        assertTrue(submission.passed)

        val submissions = db.quizSubmissionDao().getAllSubmissions().first()
        assertTrue("Submission should exist in Room table", submissions.isNotEmpty())
        assertEquals(100, submissions.first().percentage)
        assertEquals(3, submissions.first().score)
    }

    @Test
    fun `test admin can upload complete bilingual lesson and find it in repository`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getInstance(context)
        val repo = ScienceRepository(db)

        val newLesson = com.example.data.model.Lesson(
            id = "lesson_admin_solar_system",
            topicId = "topic_custom_uploads",
            grade = 2,
            titleEn = "The Solar System",
            titleYo = "Ojú Ọ̀run àti Oòrùn",
            summaryEn = "Learn about the planets and sun.",
            summaryYo = "Kọ́ nípa àwọn ìràwọ̀ àti oòrùn.",
            iconEmoji = "☀️",
            isCompleted = false,
            isCachedOffline = true,
            sections = listOf(
                com.example.data.model.LessonSection(
                    id = "sec_solar_1",
                    headingEn = "The Sun",
                    headingYo = "Oòrùn",
                    contentEn = "The sun is a star.",
                    contentYo = "Oòrùn jẹ́ ìràwọ̀ ńlá.",
                    iconEmoji = "☀️"
                )
            ),
            vocabulary = listOf(
                com.example.data.model.VocabularyWord(
                    english = "Sun",
                    yoruba = "Oòrùn",
                    toneMarks = "Oò-rùn",
                    definitionEn = "The central star",
                    definitionYo = "Ìràwọ̀ àárín",
                    emoji = "☀️"
                )
            ),
            quiz = com.example.data.model.Quiz(
                id = "quiz_solar",
                lessonId = "lesson_admin_solar_system",
                titleEn = "Sun Quiz",
                titleYo = "Ìdánwò Oòrùn",
                questions = listOf(
                    com.example.data.model.QuizQuestion(
                        id = "q_solar_1",
                        promptEn = "What is the sun?",
                        promptYo = "Kí ni oòrùn?",
                        optionsEn = listOf("A rock", "A star", "A river"),
                        optionsYo = listOf("Òkúta", "Ìràwọ̀", "Odò"),
                        correctIndex = 1,
                        explanationEn = "The sun is a star.",
                        explanationYo = "Oòrùn jẹ́ ìràwọ̀."
                    )
                )
            )
        )

        repo.uploadCompleteLesson(newLesson)

        val found = repo.findLessonById("lesson_admin_solar_system")
        assertNotNull("Uploaded lesson must be retrievable", found)
        assertEquals("The Solar System", found?.titleEn)
        assertEquals("Ojú Ọ̀run àti Oòrùn", found?.titleYo)

        val allTopics = repo.getAllTopics()
        assertTrue("All topics should include custom uploads", allTopics.any { it.id == "topic_custom_uploads" })
    }

    @Test
    fun `test Quiz entity and LessonWithQuiz relation in Room database`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getInstance(context)
        val repo = ScienceRepository(db)

        repo.seedOfflineDatabaseIfEmpty()

        val quizDao = db.quizDao()
        val lessonDao = db.lessonDao()

        // 1. Verify Quiz entity table was seeded and linked to lessons
        val quizCount = quizDao.getQuizCount()
        assertTrue("Quiz entities should be seeded in Room database", quizCount > 0)

        // 2. Query quiz by lessonId directly
        val livingQuiz = quizDao.getQuizForLessonDirect("lesson_living_char")
        assertNotNull("Quiz for living things lesson must exist in Room", livingQuiz)
        assertEquals("lesson_living_char", livingQuiz?.lessonId)
        assertTrue("Quiz questions JSON should not be empty", livingQuiz?.questionsJson?.isNotEmpty() == true)

        // Deserialization test
        val questions = repo.deserializeQuestions(livingQuiz!!.questionsJson)
        assertTrue("Questions should be parsed from JSON", questions.isNotEmpty())
        assertEquals(1, questions.first().correctIndex)

        // 3. Test Room 1-to-1 relation: LessonWithQuiz
        val lessonWithQuiz = lessonDao.getLessonWithQuizDirect("lesson_living_char")
        assertNotNull("LessonWithQuiz relation should return data", lessonWithQuiz)
        assertEquals("Characteristics of Living Things", lessonWithQuiz?.lesson?.titleEn)
        assertEquals("lesson_living_char", lessonWithQuiz?.quiz?.lessonId)

        // 4. Test repository helper method returns Quiz model linked to lesson
        val fetchedQuiz = repo.getQuizForLessonDirect("lesson_living_char")
        assertNotNull("Repository must return Quiz model", fetchedQuiz)
        assertEquals("quiz_living_char", fetchedQuiz?.id)
        assertEquals("lesson_living_char", fetchedQuiz?.lessonId)
    }

    @Test
    fun `test Simulation entity and SimulationDao offline storage in Room`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getInstance(context)
        val repo = ScienceRepository(db)

        repo.seedOfflineDatabaseIfEmpty()

        val simDao = db.simulationDao()
        val count = simDao.getSimulationCount()
        assertTrue("Simulation entities should be seeded in Room database", count >= 2)

        // 1. Fetch Solar System simulation
        val solarSim = simDao.getSimulationByIdDirect("sim_solar_system")
        assertNotNull("Solar system simulation must exist in Room", solarSim)
        assertEquals("The Solar System & Planetary Orbits", solarSim?.titleEn)
        assertEquals("Ètò Oòrùn àti Àwọn Ayé Aláràbarà", solarSim?.titleYo)
        assertEquals("solar_system", solarSim?.category)

        // 2. Fetch Plant Growth simulation
        val plantSim = simDao.getSimulationByIdDirect("sim_plant_growth")
        assertNotNull("Plant growth simulation must exist in Room", plantSim)
        assertEquals("plant_growth", plantSim?.category)

        // 3. Record interaction and star award
        val prevStars = db.learnerDao().getProfileDirect("default_learner")?.totalStars ?: 0
        repo.recordSimulationActivity("sim_solar_system", 3)

        val updatedSolar = simDao.getSimulationByIdDirect("sim_solar_system")
        assertTrue("Times interacted should increase", (updatedSolar?.timesInteracted ?: 0) > 0)

        val updatedStars = db.learnerDao().getProfileDirect("default_learner")?.totalStars ?: 0
        assertEquals(prevStars + 3, updatedStars)
    }

    @Test
    fun `test ParticipationRoom entity and live interaction in Room database`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getInstance(context)
        val repo = ScienceRepository(db)

        repo.seedOfflineDatabaseIfEmpty()

        val roomDao = db.participationRoomDao()
        val activeRoom = roomDao.getRoomByCodeDirect("IMO-101")
        assertNotNull("Default participation room should be seeded", activeRoom)
        assertEquals("IMO-101", activeRoom?.roomCode)
        assertEquals("Olùkọ́ Adéyẹmí", activeRoom?.teacherName)

        // 1. Submit learner answer
        repo.submitRoomLearnerAnswer(
            roomId = "room_primary_science",
            learnerId = "test_pupil_99",
            learnerName = "Bisi Alabi",
            avatarEmoji = "⭐",
            selectedOptionIndex = 2,
            isCorrect = true
        )

        // 2. Raise hand
        repo.toggleLearnerHandRaise("room_primary_science", "test_pupil_99", true)

        val responses = roomDao.getAllResponsesForRoom("room_primary_science").first()
        val bisiResp = responses.find { it.learnerId == "test_pupil_99" }
        assertNotNull("Learner response should be recorded in Room", bisiResp)
        assertEquals(true, bisiResp?.isCorrect)
        assertEquals(true, bisiResp?.handRaised)

        // 3. Teacher broadcast new question
        repo.broadcastRoomQuestion(
            roomId = "room_primary_science",
            qEn = "What is the center star of our solar system?",
            qYo = "Kí ni ìràwọ̀ àárín gbùngbùn ètò oòrùn?",
            options = listOf("Sun / Oòrùn", "Moon / Oṣùpá"),
            correctIdx = 0
        )

        val reloadedRoom = roomDao.getRoomByCodeDirect("IMO-101")
        assertEquals("What is the center star of our solar system?", reloadedRoom?.activeQuestionEn)
        assertEquals(0, reloadedRoom?.correctAnswerIndex)
    }

    @Test
    fun `test Badges entity seeding and retrieval in Room database`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getInstance(context)
        val repo = ScienceRepository(db)

        repo.seedOfflineDatabaseIfEmpty()

        val badgeDao = db.badgeDao()
        val count = badgeDao.getBadgeCount()
        assertTrue("Badges should be seeded in Room database", count >= 8)

        val allBadges = badgeDao.getAllBadgesDirect()
        val categories = allBadges.map { it.category }.toSet()
        assertTrue("Should contain LESSON category", categories.contains("LESSON"))
        assertTrue("Should contain QUIZ category", categories.contains("QUIZ"))
        assertTrue("Should contain SIMULATION category", categories.contains("SIMULATION"))

        val firstLessonBadge = badgeDao.getBadgeById("badge_first_lesson")
        assertNotNull("Science starter badge should exist", firstLessonBadge)
        assertEquals("Olùbẹ̀rẹ̀ Sáyẹ́ǹsì", firstLessonBadge?.titleYo)
        assertEquals("Science Starter", firstLessonBadge?.titleEn)
    }

    @Test
    fun `test Badge unlocking for completing lessons high quiz scores and simulations`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getInstance(context)
        val repo = ScienceRepository(db)

        repo.seedOfflineDatabaseIfEmpty()
        val badgeDao = db.badgeDao()

        // 1. High quiz score (100%) unlocks both sharpshooter and perfect score badges
        repo.recordQuizSubmission("quiz_living_char", 5, 5)

        val sharpshooter = badgeDao.getBadgeById("badge_quiz_sharpshooter")
        assertTrue("80%+ quiz score should unlock sharpshooter badge", sharpshooter?.isUnlocked == true)

        val perfectScore = badgeDao.getBadgeById("badge_perfect_score")
        assertTrue("100% quiz score should unlock perfect score badge", perfectScore?.isUnlocked == true)

        // 2. Interacting with simulations unlocks simulation badges
        repo.recordSimulationActivity("sim_solar_system", 3)
        val simBadge = badgeDao.getBadgeById("badge_simulation_explorer")
        assertTrue("Interacting with simulation unlocks simulation explorer badge", simBadge?.isUnlocked == true)

        val solarBadge = badgeDao.getBadgeById("badge_solar_navigator")
        assertTrue("Interacting with solar simulation unlocks solar navigator badge", solarBadge?.isUnlocked == true)

        // 3. Participating in live room unlocks classroom contributor badge
        repo.submitRoomLearnerAnswer(
            roomId = "room_primary_science",
            learnerId = "default_learner",
            learnerName = "Abbas Talba",
            avatarEmoji = "🚀",
            selectedOptionIndex = 2,
            isCorrect = true
        )
        val classroomBadge = badgeDao.getBadgeById("badge_classroom_star")
        assertTrue("Room participation unlocks classroom contributor badge", classroomBadge?.isUnlocked == true)

        // 4. Check unlocked badges flow
        val unlockedList = repo.getUnlockedBadgesFlow().first()
        assertTrue("Unlocked badges list should contain earned badges", unlockedList.size >= 4)
    }

    @Test
    fun `test topic completion tracking and progress calculation for dashboard`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getInstance(context)
        val repo = ScienceRepository(db)

        repo.seedOfflineDatabaseIfEmpty()

        val progressDao = db.lessonProgressDao()

        val livingThingsTopic = CurriculumDataSource.topics.find { it.id == "topic_living_things" }
        assertNotNull("Living things topic should exist", livingThingsTopic)

        val totalUnits = livingThingsTopic!!.lessons.size
        assertTrue("Living things topic should have lessons", totalUnits > 0)

        val firstLessonId = livingThingsTopic.lessons.first().id
        progressDao.saveProgress(
            LessonProgressEntity(
                lessonId = firstLessonId,
                learnerId = "default_learner",
                isCompleted = true,
                highestScorePercentage = 100,
                completedAt = System.currentTimeMillis(),
                attemptsCount = 1,
                starsEarned = 5
            )
        )

        val progressList = progressDao.getAllProgressForLearnerDirect("default_learner")
        val completedUnits = livingThingsTopic.lessons.count { lesson ->
            progressList.any { it.lessonId == lesson.id && it.isCompleted } || lesson.isCompleted
        }
        val progressPercent = ((completedUnits.toFloat() / totalUnits.toFloat()) * 100).toInt()

        assertTrue("Completed units should be at least 1", completedUnits >= 1)
        assertTrue("Topic progress percent should be greater than 0", progressPercent > 0)
    }
}
