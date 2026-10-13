package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class ScienceRepository(
    private val database: AppDatabase,
    private val syncAdapter: Base44SyncAdapter = Base44SyncAdapter()
) {
    val topicDao = database.topicDao()
    val lessonDao = database.lessonDao()
    val quizDao = database.quizDao()
    val simulationDao = database.simulationDao()
    val participationRoomDao = database.participationRoomDao()
    val learnerDao = database.learnerDao()
    val progressDao = database.lessonProgressDao()
    val quizSubmissionDao = database.quizSubmissionDao()
    val syncQueueDao = database.syncQueueDao()
    val customTeacherDao = database.customTeacherLessonDao()
    val badgeDao = database.badgeDao()

    /**
     * Seeds Room tables with initial curriculum lessons and topics if not already populated
     */
    suspend fun seedOfflineDatabaseIfEmpty() {
        if (lessonDao.getLessonCount() == 0) {
            val topicEntities = CurriculumDataSource.topics.map { topic ->
                TopicEntity(
                    id = topic.id,
                    grade = topic.grade,
                    titleEn = topic.titleEn,
                    titleYo = topic.titleYo,
                    iconEmoji = topic.iconEmoji,
                    colorHex = topic.colorHex,
                    descriptionEn = topic.descriptionEn,
                    descriptionYo = topic.descriptionYo,
                    lessonCount = topic.lessons.size
                )
            }
            topicDao.insertTopics(topicEntities)

            val lessonEntities = CurriculumDataSource.topics.flatMap { topic ->
                topic.lessons.map { lesson ->
                    // Serialize sections to JSON
                    val sectionsArray = JSONArray()
                    lesson.sections.forEach { sec ->
                        val secObj = JSONObject()
                        secObj.put("id", sec.id)
                        secObj.put("headingEn", sec.headingEn)
                        secObj.put("headingYo", sec.headingYo)
                        secObj.put("contentEn", sec.contentEn)
                        secObj.put("contentYo", sec.contentYo)
                        secObj.put("funFactEn", sec.funFactEn)
                        secObj.put("funFactYo", sec.funFactYo)
                        secObj.put("iconEmoji", sec.iconEmoji)
                        sectionsArray.put(secObj)
                    }

                    // Serialize vocabulary to JSON
                    val vocabArray = JSONArray()
                    lesson.vocabulary.forEach { v ->
                        val vObj = JSONObject()
                        vObj.put("english", v.english)
                        vObj.put("yoruba", v.yoruba)
                        vObj.put("toneMarks", v.toneMarks)
                        vObj.put("definitionEn", v.definitionEn)
                        vObj.put("definitionYo", v.definitionYo)
                        vObj.put("emoji", v.emoji)
                        vocabArray.put(vObj)
                    }

                    LessonEntity(
                        id = lesson.id,
                        topicId = lesson.topicId,
                        grade = lesson.grade,
                        titleEn = lesson.titleEn,
                        titleYo = lesson.titleYo,
                        summaryEn = lesson.summaryEn,
                        summaryYo = lesson.summaryYo,
                        iconEmoji = lesson.iconEmoji,
                        isCompleted = lesson.isCompleted,
                        isCachedOffline = true,
                        sectionsJson = sectionsArray.toString(),
                        vocabularyJson = vocabArray.toString(),
                        lastUpdated = System.currentTimeMillis()
                    )
                }
            }
            lessonDao.insertLessons(lessonEntities)

            // Seed Quiz entities linked to Lessons
            val quizEntities = CurriculumDataSource.topics.flatMap { topic ->
                topic.lessons.map { lesson ->
                    quizToEntity(lesson.quiz)
                }
            }
            quizDao.insertQuizzes(quizEntities)
        }

        if (simulationDao.getSimulationCount() == 0) {
            val defaultSims = listOf(
                SimulationEntity(
                    id = "sim_solar_system",
                    titleEn = "The Solar System & Planetary Orbits",
                    titleYo = "Ètò Oòrùn àti Àwọn Ayé Aláràbarà",
                    category = "solar_system",
                    grade = 2,
                    descriptionEn = "Explore planetary orbits, adjust revolution speed, and discover planets with bilingual facts and audio pronunciation.",
                    descriptionYo = "Ṣàwari bí àwọn ayé ṣe ń yí oòrùn ká, yí iyára padà, kí o sì gbọ́ orúkọ wọn lédè Yorùbá pẹ̀lú àmì ohùn.",
                    iconEmoji = "🪐",
                    conceptKeywords = "Oòrùn, Ayé, Mẹ́kúrí, Fẹ́núsì, Mássì, Júpítà, Sátọ̀nù",
                    defaultParamsJson = """{"speed": 1.0, "labels": true}""",
                    timesInteracted = 3,
                    starsAwarded = 6
                ),
                SimulationEntity(
                    id = "sim_plant_growth",
                    titleEn = "Basic Plant Growth & Photosynthesis",
                    titleYo = "Ìdàgbàsókè Ewéko & Ìmọ́lẹ̀ Oòrùn",
                    category = "plant_growth",
                    grade = 1,
                    descriptionEn = "Regulate sunlight, moisture, and soil nutrients to nourish your plant through photosynthesis, roots, and flowers.",
                    descriptionYo = "Ṣàkóso oòrùn, omi, àti ilẹ̀ ọlọ́ràá láti ran ewéko lọ́wọ́ láti hù gbòǹgbò, ewé, àti òdòdó dídùn.",
                    iconEmoji = "🌱",
                    conceptKeywords = "Oòrùn, Omi, Ilẹ̀ Ọlọ́ràá, Gbòǹgbò, Ewé, Òdòdó",
                    defaultParamsJson = """{"sunlight": 70, "water": 65, "soil": 80}""",
                    timesInteracted = 5,
                    starsAwarded = 10
                ),
                SimulationEntity(
                    id = "sim_water_cycle",
                    titleEn = "The Water Cycle (Evaporation & Rain)",
                    titleYo = "Ìyípo Omi: Ooru, Àwọsánmọ̀ & Òjò",
                    category = "water_cycle",
                    grade = 1,
                    descriptionEn = "Heat water to observe evaporation, cloud formation, condensation, and rainfall in real time.",
                    descriptionYo = "Lo ooru oòrùn láti fa evaporation, ṣẹ̀dá àwọsánmọ̀, kí òjò sì rọ̀ padà sínú odò.",
                    iconEmoji = "🌧️",
                    conceptKeywords = "Ooru, Àwọsánmọ̀, Òjò, Evaporation, Condensation",
                    defaultParamsJson = """{"heat": 75, "cooling": 50}""",
                    timesInteracted = 2,
                    starsAwarded = 4
                ),
                SimulationEntity(
                    id = "sim_human_senses",
                    titleEn = "Human Sense Organs Explorer",
                    titleYo = "Àwọn Ẹ̀yà Ìfòye Ènìyàn",
                    category = "human_senses",
                    grade = 1,
                    descriptionEn = "Connect eyes, ears, nose, tongue, and skin to nature's everyday sensory specimens.",
                    descriptionYo = "Ṣe ìbámu láàárín ojú, etí, imú, ahọ́n, àti awọ-ara pẹ̀lú àwọn ohun àyíká.",
                    iconEmoji = "👀",
                    conceptKeywords = "Ojú, Etí, Imú, Ahọ́n, Awọ-ara",
                    defaultParamsJson = """{"specimenIndex": 0}""",
                    timesInteracted = 1,
                    starsAwarded = 2
                )
            )
            simulationDao.insertSimulations(defaultSims)

            // Seed default collaborative Participation Room
            val initialRoom = ParticipationRoomEntity(
                id = "room_primary_science",
                roomCode = "IMO-101",
                titleEn = "Primary Science Live Classroom",
                titleYo = "Yàrá Sáyẹ́ǹsì Alààyè ti Kíláàsì",
                teacherName = "Olùkọ́ Adéyẹmí",
                currentTopic = "Ohun Alààyè & Ètò Oòrùn",
                currentSimulationId = "sim_solar_system",
                status = "ACTIVE",
                activeQuestionEn = "Which planet is third from the Sun where living things reside?",
                activeQuestionYo = "Èwo nínú àwọn ayé ló wà ní ipò kẹta sí oòrùn níbi tí ohun alààyè ń gbé?",
                activeQuestionOptionsJson = """["Mẹ́kúrí (Mercury)", "Fẹ́núsì (Venus)", "Ayé (Earth)", "Mássì (Mars)"]""",
                correctAnswerIndex = 2,
                learnersJoinedCount = 5
            )
            participationRoomDao.insertOrUpdateRoom(initialRoom)

            // Seed initial mock participants
            val initialResponses = listOf(
                RoomLearnerResponseEntity(
                    id = "resp_1",
                    roomId = "room_primary_science",
                    learnerId = "default_learner",
                    learnerName = "Abbas Talba",
                    avatarEmoji = "🚀",
                    selectedOptionIndex = 2,
                    isCorrect = true,
                    handRaised = false
                ),
                RoomLearnerResponseEntity(
                    id = "resp_2",
                    roomId = "room_primary_science",
                    learnerId = "learner_kehinde",
                    learnerName = "Kẹ́hìndé Adé",
                    avatarEmoji = "🌸",
                    selectedOptionIndex = 2,
                    isCorrect = true,
                    handRaised = true
                ),
                RoomLearnerResponseEntity(
                    id = "resp_3",
                    roomId = "room_primary_science",
                    learnerId = "learner_chidi",
                    learnerName = "Chidi Okafor",
                    avatarEmoji = "⚡",
                    selectedOptionIndex = 1,
                    isCorrect = false,
                    handRaised = false
                ),
                RoomLearnerResponseEntity(
                    id = "resp_4",
                    roomId = "room_primary_science",
                    learnerId = "learner_amina",
                    learnerName = "Amina Bello",
                    avatarEmoji = "🌟",
                    selectedOptionIndex = 2,
                    isCorrect = true,
                    handRaised = true
                )
            )
            initialResponses.forEach { participationRoomDao.insertResponse(it) }
        }

        // Seed initial Badges if empty
        if (badgeDao.getBadgeCount() == 0) {
            val initialBadges = listOf(
                BadgeEntity(
                    id = "badge_first_lesson",
                    titleEn = "Science Starter",
                    titleYo = "Olùbẹ̀rẹ̀ Sáyẹ́ǹsì",
                    descriptionEn = "Completed your first Basic Science lesson in Yorùbá!",
                    descriptionYo = "O parí ẹ̀kọ́ sáyẹ́ǹsì àkọ́kọ́ rẹ ní èdè Yorùbá!",
                    iconEmoji = "🌱",
                    category = "LESSON",
                    requirementDescriptionEn = "Complete 1 science lesson",
                    requirementDescriptionYo = "Parí ẹ̀kọ́ sáyẹ́ǹsì kan",
                    isUnlocked = true,
                    unlockedAt = System.currentTimeMillis() - 86400000L,
                    progress = 1,
                    targetCount = 1
                ),
                BadgeEntity(
                    id = "badge_three_lessons",
                    titleEn = "Dedicated Scholar",
                    titleYo = "Akẹ́kọ̀ọ́ Tòótọ́",
                    descriptionEn = "Completed 3 full science lessons and experiments!",
                    descriptionYo = "O parí ẹ̀kọ́ sáyẹ́ǹsì mẹ́ta pẹ̀lú àwọn àdánwò!",
                    iconEmoji = "📖",
                    category = "LESSON",
                    requirementDescriptionEn = "Complete 3 science lessons",
                    requirementDescriptionYo = "Parí àwọn ẹ̀kọ́ sáyẹ́ǹsì mẹ́ta",
                    isUnlocked = false,
                    progress = 1,
                    targetCount = 3
                ),
                BadgeEntity(
                    id = "badge_plant_detective",
                    titleEn = "Botanist Hero",
                    titleYo = "Akọni Ohun Ọ̀gbìn",
                    descriptionEn = "Mastered plant anatomy and seed experiment module.",
                    descriptionYo = "O mọ gbogbo ẹ̀yà ohun ọ̀gbìn àti bí èso ṣe ń dàgbà!",
                    iconEmoji = "🌻",
                    category = "LESSON",
                    requirementDescriptionEn = "Complete Plant Parts lesson",
                    requirementDescriptionYo = "Parí ẹ̀kọ́ Ẹ̀yà Ohun Ọ̀gbìn",
                    isUnlocked = false,
                    progress = 0,
                    targetCount = 1
                ),
                BadgeEntity(
                    id = "badge_quiz_sharpshooter",
                    titleEn = "Quiz Sharpshooter",
                    titleYo = "Agbábọ́ọ̀lù Àkọ́kọ́",
                    descriptionEn = "Scored 80% or higher on an interactive science quiz!",
                    descriptionYo = "O gba 80% tàbí jù bẹ́ẹ̀ lọ nínú ìdánwò sáyẹ́ǹsì!",
                    iconEmoji = "🎯",
                    category = "QUIZ",
                    requirementDescriptionEn = "Score 80%+ on any quiz",
                    requirementDescriptionYo = "Gba 80% tàbí jù bẹ́ẹ̀ lọ nínú ìdánwò",
                    isUnlocked = true,
                    unlockedAt = System.currentTimeMillis() - 43200000L,
                    progress = 1,
                    targetCount = 1
                ),
                BadgeEntity(
                    id = "badge_perfect_score",
                    titleEn = "100% Perfectionist",
                    titleYo = "Àpẹẹrẹ Pípé (100%)",
                    descriptionEn = "Achieved a flawless 100% score on a science quiz!",
                    descriptionYo = "O gba gbogbo àwọn ìbéèrè pátápátá láìṣe àṣìṣe kankan!",
                    iconEmoji = "💯",
                    category = "QUIZ",
                    requirementDescriptionEn = "Score 100% on a science quiz",
                    requirementDescriptionYo = "Gba 100% nínú ìdánwò sáyẹ́ǹsì",
                    isUnlocked = false,
                    progress = 0,
                    targetCount = 1
                ),
                BadgeEntity(
                    id = "badge_quiz_master",
                    titleEn = "Quiz Grand Master",
                    titleYo = "Òmìnira Ìdánwò",
                    descriptionEn = "Passed 3 science quizzes across different topics.",
                    descriptionYo = "O borí àwọn ìdánwò sáyẹ́ǹsì mẹ́ta nínú orí-ọ̀rọ̀ ọ̀tọ̀ọ̀tọ̀!",
                    iconEmoji = "🏆",
                    category = "QUIZ",
                    requirementDescriptionEn = "Pass 3 science quizzes",
                    requirementDescriptionYo = "Borí àwọn ìdánwò mẹ́ta",
                    isUnlocked = false,
                    progress = 1,
                    targetCount = 3
                ),
                BadgeEntity(
                    id = "badge_simulation_explorer",
                    titleEn = "Simulation Explorer",
                    titleYo = "Aṣàwárí Àdánwò",
                    descriptionEn = "Conducted an interactive science simulation in the digital lab!",
                    descriptionYo = "O ṣe àdánwò sáyẹ́ǹsì alálàyé nínú yàrá ìmọ̀-ẹ̀rọ!",
                    iconEmoji = "🔬",
                    category = "SIMULATION",
                    requirementDescriptionEn = "Interact with 1 simulation",
                    requirementDescriptionYo = "Ṣe àdánwò sáyẹ́ǹsì kan",
                    isUnlocked = true,
                    unlockedAt = System.currentTimeMillis() - 20000000L,
                    progress = 1,
                    targetCount = 1
                ),
                BadgeEntity(
                    id = "badge_solar_navigator",
                    titleEn = "Solar System Navigator",
                    titleYo = "Agbóye Ètò Oòrùn",
                    descriptionEn = "Explored planetary orbits and rotation in the Solar System simulation.",
                    descriptionYo = "O ṣàwárí àwọn pílánẹ́ẹ̀tì àti bí ayé ṣe ń yí oòrùn ká!",
                    iconEmoji = "🪐",
                    category = "SIMULATION",
                    requirementDescriptionEn = "Explore Solar System simulation",
                    requirementDescriptionYo = "Ṣàwárí àdánwò Ètò Oòrùn",
                    isUnlocked = false,
                    progress = 0,
                    targetCount = 1
                ),
                BadgeEntity(
                    id = "badge_plant_grower",
                    titleEn = "Virtual Botanist",
                    titleYo = "Agbin Ohun Ọ̀gbìn",
                    descriptionEn = "Simulated plant germination and photosynthesis in the lab.",
                    descriptionYo = "O ṣe àdánwò bí irúgbìn ṣe ń dàgbà àti ìmọ́lẹ̀ oòrùn!",
                    iconEmoji = "🌿",
                    category = "SIMULATION",
                    requirementDescriptionEn = "Simulate Plant Growth cycle",
                    requirementDescriptionYo = "Ṣe àdánwò ìdàgbàsókè ohun ọ̀gbìn",
                    isUnlocked = false,
                    progress = 0,
                    targetCount = 1
                ),
                BadgeEntity(
                    id = "badge_water_cycle_master",
                    titleEn = "Water Cycle Hydrologist",
                    titleYo = "Aṣàwárí Yípo Omi",
                    descriptionEn = "Simulated evaporation, clouds, and rainfall with Yoruba terms.",
                    descriptionYo = "O ṣe àdánwò bí ooru ṣe ń di àwọsánmọ̀ àti òjò!",
                    iconEmoji = "💧",
                    category = "SIMULATION",
                    requirementDescriptionEn = "Complete Water Cycle simulation",
                    requirementDescriptionYo = "Ṣe àdánwò bí omi ṣe ń yípo",
                    isUnlocked = false,
                    progress = 0,
                    targetCount = 1
                ),
                BadgeEntity(
                    id = "badge_classroom_star",
                    titleEn = "Classroom Contributor",
                    titleYo = "Akópa Yàrá Ìkẹ́kọ̀ọ́",
                    descriptionEn = "Participated and answered live questions in the Teacher-Learners room!",
                    descriptionYo = "O kópa dáadáa nínú yàrá ẹ̀kọ́ pẹ̀lú olùkọ́ rẹ!",
                    iconEmoji = "⭐",
                    category = "PARTICIPATION",
                    requirementDescriptionEn = "Answer questions in Participation Room",
                    requirementDescriptionYo = "Dáhùn ìbéèrè nínú Yàrá Ìkópa",
                    isUnlocked = true,
                    unlockedAt = System.currentTimeMillis() - 10000000L,
                    progress = 1,
                    targetCount = 1
                )
            )
            badgeDao.insertBadges(initialBadges)
        }
        evaluateBadges("default_learner")
    }

    fun serializeQuestions(questions: List<QuizQuestion>): String {
        val array = JSONArray()
        questions.forEach { q ->
            val obj = JSONObject().apply {
                put("id", q.id)
                put("promptEn", q.promptEn)
                put("promptYo", q.promptYo)
                put("optionsEn", JSONArray(q.optionsEn))
                put("optionsYo", JSONArray(q.optionsYo))
                put("correctIndex", q.correctIndex)
                put("explanationEn", q.explanationEn)
                put("explanationYo", q.explanationYo)
                put("iconEmoji", q.iconEmoji)
            }
            array.put(obj)
        }
        return array.toString()
    }

    fun deserializeQuestions(json: String): List<QuizQuestion> {
        if (json.isBlank()) return emptyList()
        val list = mutableListOf<QuizQuestion>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val optsEn = mutableListOf<String>()
                val enArr = obj.optJSONArray("optionsEn")
                if (enArr != null) {
                    for (j in 0 until enArr.length()) optsEn.add(enArr.getString(j))
                }
                val optsYo = mutableListOf<String>()
                val yoArr = obj.optJSONArray("optionsYo")
                if (yoArr != null) {
                    for (j in 0 until yoArr.length()) optsYo.add(yoArr.getString(j))
                }
                list.add(
                    QuizQuestion(
                        id = obj.optString("id", UUID.randomUUID().toString()),
                        promptEn = obj.optString("promptEn", ""),
                        promptYo = obj.optString("promptYo", ""),
                        optionsEn = optsEn,
                        optionsYo = optsYo,
                        correctIndex = obj.optInt("correctIndex", 0),
                        explanationEn = obj.optString("explanationEn", ""),
                        explanationYo = obj.optString("explanationYo", ""),
                        iconEmoji = obj.optString("iconEmoji", "❓")
                    )
                )
            }
        } catch (e: Exception) {
            // Fallback
        }
        return list
    }

    fun entityToQuiz(entity: QuizEntity): Quiz {
        return Quiz(
            id = entity.id,
            lessonId = entity.lessonId,
            titleEn = entity.titleEn,
            titleYo = entity.titleYo,
            passingScorePercentage = entity.passingScorePercentage,
            questions = deserializeQuestions(entity.questionsJson)
        )
    }

    fun quizToEntity(quiz: Quiz): QuizEntity {
        return QuizEntity(
            id = quiz.id,
            lessonId = quiz.lessonId,
            titleEn = quiz.titleEn,
            titleYo = quiz.titleYo,
            passingScorePercentage = quiz.passingScorePercentage,
            questionsJson = serializeQuestions(quiz.questions)
        )
    }

    fun getQuizForLesson(lessonId: String): Flow<Quiz?> {
        return quizDao.getQuizForLesson(lessonId).map { entity ->
            if (entity != null) entityToQuiz(entity) else findLessonById(lessonId)?.quiz
        }
    }

    suspend fun getQuizForLessonDirect(lessonId: String): Quiz? {
        val entity = quizDao.getQuizForLessonDirect(lessonId)
        return if (entity != null) entityToQuiz(entity) else findLessonById(lessonId)?.quiz
    }

    fun getLessonWithQuiz(lessonId: String): Flow<LessonWithQuiz?> {
        return lessonDao.getLessonWithQuiz(lessonId)
    }

    suspend fun getLessonWithQuizDirect(lessonId: String): LessonWithQuiz? {
        return lessonDao.getLessonWithQuizDirect(lessonId)
    }

    fun getLearnerProfile(): Flow<LearnerProfile> {
        return learnerDao.getProfile().map { entity ->
            if (entity != null) {
                LearnerProfile(
                    id = entity.id,
                    name = entity.name,
                    avatarEmoji = entity.avatarEmoji,
                    gradeLevel = entity.gradeLevel,
                    preferredLanguage = try {
                        LanguageMode.valueOf(entity.preferredLanguage)
                    } catch (e: Exception) {
                        LanguageMode.BILINGUAL
                    },
                    totalStars = entity.totalStars,
                    streakDays = entity.streakDays,
                    completedLessonsCount = 2,
                    audioSpeed = entity.audioSpeed
                )
            } else {
                LearnerProfile()
            }
        }
    }

    suspend fun saveLearnerProfile(profile: LearnerProfile) {
        learnerDao.insertOrUpdateProfile(
            LearnerProfileEntity(
                id = profile.id,
                name = profile.name,
                avatarEmoji = profile.avatarEmoji,
                gradeLevel = profile.gradeLevel,
                preferredLanguage = profile.preferredLanguage.name,
                totalStars = profile.totalStars,
                streakDays = profile.streakDays,
                audioSpeed = profile.audioSpeed
            )
        )
    }

    suspend fun addStars(count: Int) {
        val current = learnerDao.getProfileDirect()
        if (current != null) {
            learnerDao.addStars(current.id, count)
        } else {
            learnerDao.insertOrUpdateProfile(
                LearnerProfileEntity(
                    id = "default_learner",
                    name = "Ayọ̀",
                    avatarEmoji = "🚀",
                    gradeLevel = 2,
                    preferredLanguage = LanguageMode.BILINGUAL.name,
                    totalStars = 12 + count,
                    streakDays = 3,
                    audioSpeed = 0.9f
                )
            )
        }
    }

    private val dynamicUploadedLessons = mutableListOf<Lesson>()

    fun getAllTopics(): List<ScienceTopic> {
        val baseTopics = CurriculumDataSource.topics
        if (dynamicUploadedLessons.isEmpty()) {
            return baseTopics
        }
        val customTopic = ScienceTopic(
            id = "topic_custom_uploads",
            grade = dynamicUploadedLessons.firstOrNull()?.grade ?: 1,
            titleEn = "Teacher & Admin Uploads",
            titleYo = "Àwọn Ẹ̀kọ́ Olùkọ́ àti Olùṣàkóso",
            iconEmoji = "📝",
            colorHex = 0xFF6A1B9A,
            descriptionEn = "New science lessons authored and uploaded by teachers and administrators.",
            descriptionYo = "Àwọn ẹ̀kọ́ sáyẹ́ǹsì tuntun tí olùkọ́ àti olùṣàkóso tẹ̀jáde fún àwọn akẹ́kọ̀ọ́.",
            lessons = dynamicUploadedLessons
        )
        return listOf(customTopic) + baseTopics
    }

    fun getTopicsForGrade(grade: Int?): List<ScienceTopic> {
        val all = getAllTopics()
        return if (grade == null || grade == 0) {
            all
        } else {
            all.filter { it.grade == grade }
        }
    }

    fun findLessonById(lessonId: String): Lesson? {
        val customFound = dynamicUploadedLessons.firstOrNull { it.id == lessonId }
        if (customFound != null) return customFound

        for (topic in CurriculumDataSource.topics) {
            val found = topic.lessons.firstOrNull { it.id == lessonId }
            if (found != null) return found
        }
        return null
    }

    suspend fun uploadCompleteLesson(lesson: Lesson) {
        dynamicUploadedLessons.removeAll { it.id == lesson.id }
        dynamicUploadedLessons.add(0, lesson)

        // 1. Save to Room science_lessons
        val sectionsArray = JSONArray()
        lesson.sections.forEach { sec ->
            val secObj = JSONObject()
            secObj.put("id", sec.id)
            secObj.put("headingEn", sec.headingEn)
            secObj.put("headingYo", sec.headingYo)
            secObj.put("contentEn", sec.contentEn)
            secObj.put("contentYo", sec.contentYo)
            secObj.put("funFactEn", sec.funFactEn)
            secObj.put("funFactYo", sec.funFactYo)
            secObj.put("iconEmoji", sec.iconEmoji)
            sectionsArray.put(secObj)
        }

        val vocabArray = JSONArray()
        lesson.vocabulary.forEach { v ->
            val vObj = JSONObject()
            vObj.put("english", v.english)
            vObj.put("yoruba", v.yoruba)
            vObj.put("toneMarks", v.toneMarks)
            vObj.put("definitionEn", v.definitionEn)
            vObj.put("definitionYo", v.definitionYo)
            vObj.put("emoji", v.emoji)
            vocabArray.put(vObj)
        }

        lessonDao.insertLesson(
            LessonEntity(
                id = lesson.id,
                topicId = lesson.topicId,
                grade = lesson.grade,
                titleEn = lesson.titleEn,
                titleYo = lesson.titleYo,
                summaryEn = lesson.summaryEn,
                summaryYo = lesson.summaryYo,
                iconEmoji = lesson.iconEmoji,
                isCompleted = false,
                isCachedOffline = true,
                sectionsJson = sectionsArray.toString(),
                vocabularyJson = vocabArray.toString(),
                lastUpdated = System.currentTimeMillis()
            )
        )

        // Also insert Quiz entity into science_quizzes
        quizDao.insertQuiz(quizToEntity(lesson.quiz))

        // 2. Also insert into customTeacherDao
        customTeacherDao.insertLesson(
            CustomTeacherLessonEntity(
                id = lesson.id,
                grade = lesson.grade,
                titleEn = lesson.titleEn,
                titleYo = lesson.titleYo,
                summaryEn = lesson.summaryEn,
                summaryYo = lesson.summaryYo,
                contentEn = lesson.sections.joinToString("\n\n") { it.contentEn },
                contentYo = lesson.sections.joinToString("\n\n") { it.contentYo },
                authorTeacher = "Admin / Olùkọ́",
                isPublished = true,
                createdAt = System.currentTimeMillis()
            )
        )

        // 3. Enqueue into sync queue for Base44 upload
        val syncPayload = JSONObject().apply {
            put("action", "UPLOAD_LESSON")
            put("lesson_id", lesson.id)
            put("title_yo", lesson.titleYo)
            put("title_en", lesson.titleEn)
            put("grade", lesson.grade)
        }.toString()

        syncQueueDao.enqueueItem(
            SyncQueueEntity(
                entityType = "CUSTOM_LESSON",
                payloadJson = syncPayload,
                createdAt = System.currentTimeMillis(),
                status = "PENDING"
            )
        )
    }

    suspend fun recordQuizSubmission(
        quizId: String,
        score: Int,
        total: Int
    ): QuizSubmission {
        val percentage = if (total > 0) (score * 100) / total else 0
        val passed = percentage >= 60
        val submissionId = UUID.randomUUID().toString()
        val timestamp = System.currentTimeMillis()

        // 1. Save to Room quiz_submissions
        val entity = QuizSubmissionEntity(
            id = submissionId,
            quizId = quizId,
            learnerId = "default_learner",
            score = score,
            totalQuestions = total,
            percentage = percentage,
            passed = passed,
            answersJson = "[]",
            timestamp = timestamp,
            isSynced = false,
            idempotencyKey = "cai_sub_$submissionId"
        )
        quizSubmissionDao.insertSubmission(entity)

        // 2. Mark lesson progress in Room lesson_progress
        val lessonId = quizId.removePrefix("quiz_").let { "lesson_$it" }
        progressDao.saveProgress(
            LessonProgressEntity(
                lessonId = lessonId,
                learnerId = "default_learner",
                isCompleted = passed,
                highestScorePercentage = percentage,
                completedAt = timestamp,
                attemptsCount = 1,
                starsEarned = if (percentage == 100) 5 else if (passed) 3 else 1
            )
        )

        // 3. Mark offline lesson completed in Room science_lessons table
        if (passed) {
            lessonDao.markLessonCompleted(lessonId, true)
        }

        // 4. Award stars to profile
        val starsToAward = if (percentage == 100) 5 else if (passed) 3 else 1
        addStars(starsToAward)

        // 5. Enqueue into offline sync queue for Base44 upload
        val payload = syncAdapter.createSubmissionPayload(
            submissionId = submissionId,
            quizId = quizId,
            learnerId = "default_learner",
            score = score,
            total = total,
            percentage = percentage,
            passed = passed,
            timestamp = timestamp
        )
        syncQueueDao.enqueueItem(
            SyncQueueEntity(
                entityType = "QUIZ_SUBMISSION",
                payloadJson = payload,
                createdAt = timestamp,
                status = "PENDING"
            )
        )
        // 6. Evaluate badges for completing quizzes and scoring high
        evaluateBadges("default_learner")

        return QuizSubmission(
            id = submissionId,
            quizId = quizId,
            learnerId = "default_learner",
            score = score,
            totalQuestions = total,
            percentage = percentage,
            passed = passed,
            timestamp = timestamp,
            isSynced = false
        )
    }

    // Room-backed observations
    fun getAllSubmissions(): Flow<List<QuizSubmissionEntity>> = quizSubmissionDao.getAllSubmissions()

    fun getAllProgress(): Flow<List<LessonProgressEntity>> = progressDao.getAllProgress()

    fun getOfflineLessonsFlow(): Flow<List<LessonEntity>> = lessonDao.getAllLessons()

    fun getPendingSyncCount(): Flow<Int> = syncQueueDao.getPendingCount()

    fun getRecentSyncQueue(): Flow<List<SyncQueueEntity>> = syncQueueDao.getRecentQueueHistory()

    suspend fun performBase44Sync(): Base44SyncAdapter.SyncResult {
        val pending = syncQueueDao.getPendingQueue().firstOrNull() ?: emptyList()
        val result = syncAdapter.processSyncBatch(pending)
        if (result.success) {
            syncQueueDao.markAllSynced()
        }
        return result
    }

    fun getBadges(): List<AchievementBadge> {
        return CurriculumDataSource.defaultBadges
    }

    fun getCustomLessons(): Flow<List<CustomTeacherLessonEntity>> = customTeacherDao.getAllCustomLessons()

    suspend fun addCustomLesson(
        grade: Int,
        titleEn: String,
        titleYo: String,
        summaryEn: String,
        summaryYo: String,
        contentEn: String,
        contentYo: String,
        author: String
    ) {
        val id = "teacher_custom_" + System.currentTimeMillis()
        customTeacherDao.insertLesson(
            CustomTeacherLessonEntity(
                id = id,
                grade = grade,
                titleEn = titleEn,
                titleYo = titleYo,
                summaryEn = summaryEn,
                summaryYo = summaryYo,
                contentEn = contentEn,
                contentYo = contentYo,
                authorTeacher = author,
                isPublished = true,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteCustomLesson(id: String) {
        customTeacherDao.deleteLesson(id)
    }

    fun getApiSpecificationMarkdown(): String = syncAdapter.getApiSpecificationMarkdown()

    // --- Simulations Layer ---
    fun getAllSimulations(): Flow<List<SimulationEntity>> = simulationDao.getAllSimulations()
    suspend fun getAllSimulationsDirect(): List<SimulationEntity> = simulationDao.getAllSimulationsDirect()
    fun getSimulationById(id: String): Flow<SimulationEntity?> = simulationDao.getSimulationById(id)
    suspend fun getSimulationByIdDirect(id: String): SimulationEntity? = simulationDao.getSimulationByIdDirect(id)

    suspend fun recordSimulationActivity(id: String, stars: Int = 0) {
        simulationDao.recordInteraction(id)
        if (stars > 0) {
            simulationDao.awardSimulationStars(id, stars)
            learnerDao.addStars("default_learner", stars)
        }
        evaluateBadges("default_learner")
    }

    // --- Participation Room Layer ---
    fun getActiveParticipationRoom(): Flow<ParticipationRoomEntity?> = participationRoomDao.getActiveRoom()
    suspend fun getRoomByCodeDirect(code: String): ParticipationRoomEntity? = participationRoomDao.getRoomByCodeDirect(code)
    fun getRoomResponses(roomId: String): Flow<List<RoomLearnerResponseEntity>> = participationRoomDao.getAllResponsesForRoom(roomId)

    suspend fun broadcastRoomQuestion(
        roomId: String,
        qEn: String,
        qYo: String,
        options: List<String>,
        correctIdx: Int
    ) {
        val array = JSONArray()
        options.forEach { array.put(it) }
        participationRoomDao.updateActiveQuestion(roomId, qEn, qYo, array.toString(), correctIdx)
    }

    suspend fun submitRoomLearnerAnswer(
        roomId: String,
        learnerId: String,
        learnerName: String,
        avatarEmoji: String,
        selectedOptionIndex: Int,
        isCorrect: Boolean
    ) {
        val response = RoomLearnerResponseEntity(
            id = "resp_${learnerId}_${System.currentTimeMillis()}",
            roomId = roomId,
            learnerId = learnerId,
            learnerName = learnerName,
            avatarEmoji = avatarEmoji,
            selectedOptionIndex = selectedOptionIndex,
            isCorrect = isCorrect,
            handRaised = false,
            timestamp = System.currentTimeMillis()
        )
        participationRoomDao.insertResponse(response)
        if (isCorrect) {
            learnerDao.addStars(learnerId, 2)
        }
        badgeDao.unlockBadge("badge_classroom_star")
        evaluateBadges(learnerId)
    }

    suspend fun toggleLearnerHandRaise(roomId: String, learnerId: String, isRaised: Boolean) {
        participationRoomDao.updateHandRaised(roomId, learnerId, isRaised)
    }

    suspend fun awardTeacherBonusStar(learnerId: String, stars: Int = 1) {
        learnerDao.addStars(learnerId, stars)
    }

    // --- Badges & Achievements Layer (Room-backed) ---
    fun getAllBadgesFlow(): Flow<List<BadgeEntity>> = badgeDao.getAllBadges()

    fun getUnlockedBadgesFlow(): Flow<List<BadgeEntity>> = badgeDao.getUnlockedBadges()

    fun getUnlockedBadgeCountFlow(): Flow<Int> = badgeDao.getUnlockedBadgeCountFlow()

    suspend fun unlockBadgeDirect(badgeId: String) {
        badgeDao.unlockBadge(badgeId)
    }

    suspend fun evaluateBadges(learnerId: String = "default_learner"): List<BadgeEntity> {
        val completedProgress: List<LessonProgressEntity> = progressDao.getAllProgressForLearnerDirect(learnerId)
        val completedLessonCount: Int = completedProgress.count { it.isCompleted }
        val hasPlantLesson: Boolean = completedProgress.any { it.lessonId.contains("plant") && it.isCompleted }

        val submissions: List<QuizSubmissionEntity> = quizSubmissionDao.getAllSubmissionsDirect()
        val passedQuizCount: Int = submissions.count { it.passed }
        val hasHighScore: Boolean = submissions.any { it.percentage >= 80 }
        val hasPerfectScore: Boolean = submissions.any { it.percentage == 100 }

        val simulations = simulationDao.getAllSimulationsDirect()
        val totalSimInteractions = simulations.sumOf { it.timesInteracted }
        val solarInteractions = simulations.find { it.category == "solar_system" || it.id.contains("solar") }?.timesInteracted ?: 0
        val plantInteractions = simulations.find { it.category == "plant_growth" || it.id.contains("plant") }?.timesInteracted ?: 0
        val waterInteractions = simulations.find { it.category == "water_cycle" || it.id.contains("water") }?.timesInteracted ?: 0

        val currentBadges = badgeDao.getAllBadgesDirect()
        val newlyUnlocked = mutableListOf<BadgeEntity>()

        for (b in currentBadges) {
            var shouldUnlock = b.isUnlocked
            var currentProgress = b.progress

            when (b.id) {
                "badge_first_lesson" -> {
                    currentProgress = completedLessonCount.coerceAtMost(b.targetCount)
                    if (completedLessonCount >= 1) shouldUnlock = true
                }
                "badge_three_lessons" -> {
                    currentProgress = completedLessonCount.coerceAtMost(b.targetCount)
                    if (completedLessonCount >= 3) shouldUnlock = true
                }
                "badge_plant_detective" -> {
                    currentProgress = if (hasPlantLesson) 1 else 0
                    if (hasPlantLesson) shouldUnlock = true
                }
                "badge_quiz_sharpshooter" -> {
                    currentProgress = if (hasHighScore) 1 else 0
                    if (hasHighScore) shouldUnlock = true
                }
                "badge_perfect_score" -> {
                    currentProgress = if (hasPerfectScore) 1 else 0
                    if (hasPerfectScore) shouldUnlock = true
                }
                "badge_quiz_master" -> {
                    currentProgress = passedQuizCount.coerceAtMost(b.targetCount)
                    if (passedQuizCount >= 3) shouldUnlock = true
                }
                "badge_simulation_explorer" -> {
                    currentProgress = totalSimInteractions.coerceAtMost(b.targetCount)
                    if (totalSimInteractions >= 1) shouldUnlock = true
                }
                "badge_solar_navigator" -> {
                    currentProgress = solarInteractions.coerceAtMost(b.targetCount)
                    if (solarInteractions >= 1) shouldUnlock = true
                }
                "badge_plant_grower" -> {
                    currentProgress = plantInteractions.coerceAtMost(b.targetCount)
                    if (plantInteractions >= 1) shouldUnlock = true
                }
                "badge_water_cycle_master" -> {
                    currentProgress = waterInteractions.coerceAtMost(b.targetCount)
                    if (waterInteractions >= 1) shouldUnlock = true
                }
                "badge_classroom_star" -> {
                    currentProgress = b.progress
                    shouldUnlock = b.isUnlocked
                }
            }

            badgeDao.updateBadgeProgress(b.id, currentProgress)

            if (shouldUnlock && !b.isUnlocked) {
                badgeDao.unlockBadge(b.id, System.currentTimeMillis())
                newlyUnlocked.add(b.copy(isUnlocked = true, progress = b.targetCount))
            }
        }
        return newlyUnlocked
    }
}
