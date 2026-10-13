package com.example.data.repository

import com.example.data.model.*

object CurriculumDataSource {

    val defaultBadges: List<AchievementBadge> = listOf(
        AchievementBadge(
            id = "badge_first_lesson",
            titleEn = "Science Starter",
            titleYo = "Olùbẹ̀rẹ̀ Sáyẹ́ǹsì",
            descriptionEn = "Completed your very first Basic Science lesson!",
            descriptionYo = "O parí ẹ̀kọ́ sáyẹ́ǹsì àkọ́kọ́ rẹ!",
            emoji = "🌱",
            isUnlocked = true,
            unlockedDate = "Today"
        ),
        AchievementBadge(
            id = "badge_living_expert",
            titleEn = "Biology Scout",
            titleYo = "Aṣáájú Ohun Alààyè",
            descriptionEn = "Mastered Living & Non-living things with 100% quiz score!",
            descriptionYo = "O mọ ohun alààyè àti aláìláàyè dáadáa!",
            emoji = "🐾",
            isUnlocked = true,
            unlockedDate = "Yesterday"
        ),
        AchievementBadge(
            id = "badge_plant_detective",
            titleEn = "Botanist Hero",
            titleYo = "Akọni Ohun Ọ̀gbìn",
            descriptionEn = "Completed the plant parts and seed experiment module.",
            descriptionYo = "O mọ gbogbo ẹ̀yà ohun ọ̀gbìn!",
            emoji = "🌻",
            isUnlocked = false
        ),
        AchievementBadge(
            id = "badge_quiz_master",
            titleEn = "Quiz Star",
            titleYo = "Òmìnira Ìdánwò",
            descriptionEn = "Passed 5 quizzes in a row without a single error.",
            descriptionYo = "O gba gbogbo àwọn ìbéèrè márùn-ún láìṣe àṣìṣe kankan!",
            emoji = "🏆",
            isUnlocked = false
        ),
        AchievementBadge(
            id = "badge_bilingual_champion",
            titleEn = "Yoruba Scholar",
            titleYo = "Ọ̀mọ̀wé Yorùbá",
            descriptionEn = "Learned 20 new Yoruba science vocabulary words.",
            descriptionYo = "O kọ́ àwọn ọ̀rọ̀ sáyẹ́ǹsì ogún ní èdè Yorùbá!",
            emoji = "📚",
            isUnlocked = false
        )
    )

    val topics: List<ScienceTopic> = listOf(
        // Topic 1: Living & Non-living Things
        ScienceTopic(
            id = "topic_living_things",
            grade = 1,
            titleEn = "Living & Non-Living Things",
            titleYo = "Ohun Alààyè àti Ohun Aláìláàyè",
            iconEmoji = "🐾",
            colorHex = 0xFF2E7D32, // Green
            descriptionEn = "Learn how living things breathe, grow, and feed unlike non-living things.",
            descriptionYo = "Kọ́ bí ohun alààyè ṣe ń mí, ń dàgbà, tí wọ́n sì ń jẹun yàtọ̀ sí ohun aláìláàyè.",
            lessons = listOf(
                Lesson(
                    id = "lesson_living_char",
                    topicId = "topic_living_things",
                    grade = 1,
                    titleEn = "Characteristics of Living Things",
                    titleYo = "Àwọn Àbùdá Ohun Alààyè",
                    summaryEn = "Living things move, breathe, feed, grow, and produce young ones.",
                    summaryYo = "Ohun alààyè le rìn, mí, jẹun, dàgbà, kí wọ́n sì bímọ.",
                    iconEmoji = "🦁",
                    isCompleted = true,
                    isCachedOffline = true,
                    sections = listOf(
                        LessonSection(
                            id = "sec_1_1",
                            headingEn = "What Are Living Things?",
                            headingYo = "Kí Ni Ohun Alààyè?",
                            contentEn = "Living things are creations that have life in them. Examples include humans (ènìyàn), animals (ẹranko), birds (ẹyẹ), and plants (ohun ọ̀gbìn).",
                            contentYo = "Ohun alààyè ni àwọn ohun tí ẹ̀mí wà nínú wọn. Àpẹẹrẹ ni ènìyàn, ẹranko, ẹyẹ, àti àwọn ewéko tàbí ohun ọ̀gbìn.",
                            funFactEn = "Did you know? Even giant trees are living things and drink water through their roots!",
                            funFactYo = "Ṣé o mọ̀? Pẹ̀lúpẹ̀lù àwọn igi ńlá jẹ́ ohun alààyè, wọ́n sì ń mu omi láti gbòǹgbò wọn!",
                            iconEmoji = "🌳"
                        ),
                        LessonSection(
                            id = "sec_1_2",
                            headingEn = "Key Characteristics (Àwọn Àbùdá)",
                            headingYo = "Àwọn Àbùdá Pàtàkì",
                            contentEn = "1. Movement: They can move.\n2. Respiration: They breathe in air.\n3. Feeding: They eat food to get energy.\n4. Growth: Small babies grow into adults.\n5. Reproduction: They give birth to young ones.",
                            contentYo = "1. Rírin / Ìyípadà: Wọ́n lè rìn tàbí yípo.\n2. Mími: Wọ́n ń mí afẹ́fẹ́ tútù.\n3. Jíjẹun: Wọ́n ń jẹ oúnjẹ láti ní agbára.\n4. Dídàgbà: Àwọn ọmọdé ń dàgbà di àgbàlagbà.\n5. Bíbí ọmọ: Wọ́n ń bí ọmọ sí ayé.",
                            funFactEn = "Plants move too! They slowly turn their flowers and leaves towards sunlight.",
                            funFactYo = "Àwọn ohun ọ̀gbìn náà ń yíra! Wọ́n ń kọjú sí ìmọ́lẹ̀ oòrùn díẹ̀díẹ̀.",
                            iconEmoji = "☀️"
                        ),
                        LessonSection(
                            id = "sec_1_3",
                            headingEn = "What Are Non-Living Things?",
                            headingYo = "Kí Ni Ohun Aláìláàyè?",
                            contentEn = "Non-living things do not have life. They cannot breathe, eat, or reproduce. Examples: stone (òkúta), pencil (pẹ́ńsù), cup (ife), and book (ìwé).",
                            contentYo = "Ohun aláìláàyè kò ní ẹ̀mí. Wọn kò lè mí, jẹ oúnjẹ, tàbí bímọ. Àpẹẹrẹ: òkúta, pẹ́ńsù, ife omi, àti ìwé kíkà.",
                            funFactEn = "A plastic toy car does not need food to sleep, because it is non-living!",
                            funFactYo = "Ọkọ̀ ayọ́kẹ́lẹ́ eré kò nílò oúnjẹ láti sùn, nítorí ohun aláìláàyè ni!",
                            iconEmoji = "🚗"
                        )
                    ),
                    vocabulary = listOf(
                        VocabularyWord("Living Thing", "Ohun Alààyè", "O-hun A-làà-yè", "Anything that has life and breathes", "Ohun tí ẹ̀mí wà nínú rẹ̀", "🐾"),
                        VocabularyWord("Non-living", "Aláìláàyè", "A-láì-láà-yè", "Things without life like stones", "Ohun tí kò ní ẹ̀mí bí òkúta", "🪨"),
                        VocabularyWord("Breathe", "Mími", "Mí-mi", "Taking air into the lungs", "Gbigba afẹ́fẹ́ sínú ẹ̀dọ̀fóró", "🌬️"),
                        VocabularyWord("Food", "Oúnjẹ", "Oún-jẹ", "Nourishment eaten for energy", "Ohun tí a ń jẹ fún okun àti agbára", "🍲"),
                        VocabularyWord("Growth", "Dídàgbà", "Dí-dà-gbà", "Becoming bigger and stronger", "Kí nǹkan pọ̀ sí i ní títóbi", "📏")
                    ),
                    experimentGuide = ExperimentGuide(
                        titleEn = "Living vs Non-Living Hunt",
                        titleYo = "Ṣíṣe Àwárí Ohun Alààyè àti Aláìláàyè",
                        materialsEn = listOf("A small notebook", "A pencil", "Magnifying glass"),
                        materialsYo = listOf("Ìwé pẹlẹbẹ kékeré", "Pẹ́ńsù kan", "Gíláàsì amúnǹkandàgbà"),
                        stepsEn = listOf(
                            "Walk into the garden or school yard.",
                            "Look at an ant (èèrà) and a stone (òkúta).",
                            "Touch the stone: does it move by itself? No!",
                            "Watch the ant: is it crawling searching for food? Yes!"
                        ),
                        stepsYo = listOf(
                            "Rìn lọ sí inú ọgbà ilé-ìwé tàbí ilé rẹ.",
                            "Wo èèrà kékeré kan àti òkúta kan.",
                            "Fọwọ́kan òkúta náà: ǹjẹ́ ó lè rìn fúnra rẹ̀? Rárá!",
                            "Wo èèrà náà: ǹjẹ́ ó ń rìn láti wá oúnjẹ? Bẹ́ẹ̀ni!"
                        ),
                        outcomeEn = "You proved that ants are living things because they move and seek food!",
                        outcomeYo = "O fi hàn pé èèrà jẹ́ ohun alààyè nítorí ó ń rìn ó sì ń wá oúnjẹ!"
                    ),
                    quiz = Quiz(
                        id = "quiz_living_char",
                        lessonId = "lesson_living_char",
                        titleEn = "Living Things Challenge",
                        titleYo = "Ìdánwò Ohun Alààyè",
                        questions = listOf(
                            QuizQuestion(
                                id = "q1_1",
                                promptEn = "Which of the following is a LIVING thing?",
                                promptYo = "Èwo nínú àwọn wọ̀nyí ni OHUN ALÀÀYÈ?",
                                optionsEn = listOf("A stone", "A goat", "A wooden chair", "A plastic ruler"),
                                optionsYo = listOf("Òkúta", "Ewúrẹ́", "Àga igi", "Rúlà"),
                                correctIndex = 1,
                                explanationEn = "A goat (ewúrẹ́) is a living thing because it breathes, eats grass, and has baby goats!",
                                explanationYo = "Ewúrẹ́ jẹ́ ohun alààyè nítorí ó ń mí, ń jẹ koríko, ó sì ń bí ọmọ!",
                                iconEmoji = "🐐"
                            ),
                            QuizQuestion(
                                id = "q1_2",
                                promptEn = "What do all living things need to breathe?",
                                promptYo = "Kí ni gbogbo ohun alààyè nílò láti mí?",
                                optionsEn = listOf("Sand", "Air", "Plastic", "Petrol"),
                                optionsYo = listOf("Iyanrìn", "Afẹ́fẹ́", "Púpọ̀", "Epo bẹnsíìni"),
                                correctIndex = 1,
                                explanationEn = "All living things require fresh air (afẹ́fẹ́) to breathe and survive.",
                                explanationYo = "Gbogbo ohun alààyè nílò afẹ́fẹ́ mímọ́ láti mí àti láti wà láàyè.",
                                iconEmoji = "🌬️"
                            ),
                            QuizQuestion(
                                id = "q1_3",
                                promptEn = "Why is a pencil NOT a living thing?",
                                promptYo = "Èéṣe tí pẹ́ńsù kò fi jẹ́ ohun alààyè?",
                                optionsEn = listOf("It is too long", "It cannot grow, eat, or breathe", "It is yellow", "It can write"),
                                optionsYo = listOf("Ó gùn jù", "Kò lè dàgbà, jẹun tàbí mí", "Àwọ̀ pupa ló ní", "Ó lè kọ̀wé"),
                                correctIndex = 1,
                                explanationEn = "A pencil cannot breathe, eat food, or grow, so it is a non-living thing (ohun aláìláàyè).",
                                explanationYo = "Pẹ́ńsù kò lè mí, kò lè jẹun, kò sì lè dàgbà, nítorí náà ohun aláìláàyè ni.",
                                iconEmoji = "✏️"
                            )
                        )
                    )
                )
            )
        ),

        // Topic 2: Plants and Their Parts
        ScienceTopic(
            id = "topic_plants",
            grade = 2,
            titleEn = "Plants and Their Parts",
            titleYo = "Àwọn Ohun Ọ̀gbìn àti Àwọn Ẹ̀yà Wọn",
            iconEmoji = "🌻",
            colorHex = 0xFF388E3C, // Emerald
            descriptionEn = "Discover how roots, stems, leaves, and flowers help plants grow.",
            descriptionYo = "Ṣe àwárí bí gbòǹgbò, igi, ewé, àti òdòdó ṣe ń ran ohun ọ̀gbìn lọ́wọ́ láti dàgbà.",
            lessons = listOf(
                Lesson(
                    id = "lesson_plant_parts",
                    topicId = "topic_plants",
                    grade = 2,
                    titleEn = "The Five Main Parts of a Plant",
                    titleYo = "Àwọn Ẹ̀yà Márùn-ún Pàtàkì Ní Ara Ohun Ọ̀gbìn",
                    summaryEn = "Every flowering plant has roots, a stem, leaves, flowers, and fruits.",
                    summaryYo = "Gbogbo ohun ọ̀gbìn tí ń tanná ní gbòǹgbò, igi/ìtẹ̀rẹ́, ewé, òdòdó, àti èso.",
                    iconEmoji = "🌿",
                    isCompleted = false,
                    isCachedOffline = true,
                    sections = listOf(
                        LessonSection(
                            id = "sec_2_1",
                            headingEn = "1. Roots (Gbòǹgbò)",
                            headingYo = "1. Gbòǹgbò",
                            contentEn = "Roots grow deep inside the soil. They hold the plant firm against wind and absorb water and nutrients from the earth.",
                            contentYo = "Gbòǹgbò ń hù sínú ilẹ̀. Wọ́n ń di ohun ọ̀gbìn mú láti dúró ṣinṣin, wọ́n sì ń fa omi àti oúnjẹ láti inú ilẹ̀.",
                            funFactEn = "Carrots and cassava are actually big nutritious roots that we eat!",
                            funFactYo = "Kárọ́ọ̀tì àti ẹ̀gẹ́ (gàrí) jẹ́ gbòǹgbò títóbi tí a ń jẹ!",
                            iconEmoji = "🥕"
                        ),
                        LessonSection(
                            id = "sec_2_2",
                            headingEn = "2. Stem (Igi / Ìtẹ̀rẹ́)",
                            headingYo = "2. Igi tàbí Ìtẹ̀rẹ́",
                            contentEn = "The stem supports the plant upright like our spine. It carries water from roots upwards to the green leaves.",
                            contentYo = "Igi tàbí ìtẹ̀rẹ́ ń gbé ohun ọ̀gbìn dúró gbandan bí ọ̀pá ẹ̀hin wa. Ó ń gbé omi láti gbòǹgbò lọ sí orí àwọn ewé tútù.",
                            funFactEn = "The tree trunk of an Iroko or Baobab tree is its stem!",
                            funFactYo = "Igi ńlá bí igi Ìrókò tàbí Ọ̀ṣẹ jẹ́ ìtẹ̀rẹ́ ohun ọ̀gbìn náà!",
                            iconEmoji = "🪵"
                        ),
                        LessonSection(
                            id = "sec_2_3",
                            headingEn = "3. Leaves, Flowers & Fruits (Ewé, Òdòdó àti Èso)",
                            headingYo = "3. Ewé, Òdòdó àti Èso",
                            contentEn = "Leaves make food using sunlight (photosynthesis). Flowers make pleasant smells and seeds. Fruits protect the seeds and are delicious!",
                            contentYo = "Ewé ń se oúnjẹ fún ohun ọ̀gbìn nípa lílo ìmọ́lẹ̀ oòrùn. Òdòdó ń tanná ó sì ń pèsè kóró. Èso ń dáàbò bo kóró ó sì dùn láti jẹ!",
                            funFactEn = "Green leaves release pure oxygen for us to breathe every morning!",
                            funFactYo = "Àwọn ewé tútù ń tú afẹ́fẹ́ mímọ́ sílẹ̀ fún wa láti mí láràárọ̀!",
                            iconEmoji = "🍃"
                        )
                    ),
                    vocabulary = listOf(
                        VocabularyWord("Root", "Gbòǹgbò", "Gbòǹ-gbò", "Part under the ground absorbing water", "Ẹ̀yà abẹ́ ilẹ̀ tó ń fa omi", "🌱"),
                        VocabularyWord("Leaf", "Ewé", "E-wé", "Green part that prepares food from sunlight", "Ẹ̀yà tútù tó ń se oúnjẹ pẹ̀lú oòrùn", "🍃"),
                        VocabularyWord("Flower", "Òdòdó", "Ò-dò-dó", "Colorful part producing seeds", "Ẹ̀yà aláràbarà tó ń bí kóró", "🌸"),
                        VocabularyWord("Fruit", "Èso", "È-so", "Sweet fleshy part holding seeds like mango", "Ẹ̀yà dídùn tó gbé kóró bí máńgòrò", "🥭")
                    ),
                    experimentGuide = ExperimentGuide(
                        titleEn = "Grow Your Own Bean Seed",
                        titleYo = "Gbingbin Kóró Ẹ̀wà",
                        materialsEn = listOf("Clear transparent cup", "Cotton wool or tissue", "3 dried beans", "Water"),
                        materialsYo = listOf("Ife gíláàsì tí a lè ríran nínú rẹ̀", "Òwú tútù", "Kóró ẹ̀wà mẹ́ta", "Omi"),
                        stepsEn = listOf(
                            "Dampen the cotton wool with a little water.",
                            "Place the beans between the wool and the side of the cup.",
                            "Keep near a window with sunlight.",
                            "Watch after 3 days: tiny white roots appear first, then a green shoot!"
                        ),
                        stepsYo = listOf(
                            "Fi omi kékeré rẹ òwú náà.",
                            "Fi kóró ẹ̀wà náà sí àárín òwú àti ẹ̀gbẹ́ ife náà.",
                            "Gbé e sí ẹ̀bá fèrèsé níbi tí oòrùn wà.",
                            "Wò ó lẹ́yìn ọjọ́ mẹ́ta: gbòǹgbò funfun kékeré yóò kọ́kọ́ yọ, lẹ́yìn náà ewé tútù yóò yọ!"
                        ),
                        outcomeEn = "You will see a live seed germinate into a baby plant!",
                        outcomeYo = "Wàá rí bí kóró ẹ̀wà ṣe ń yípadà di ọmọ ohun ọ̀gbìn tútù!"
                    ),
                    quiz = Quiz(
                        id = "quiz_plant_parts",
                        lessonId = "lesson_plant_parts",
                        titleEn = "Plant Parts Quiz",
                        titleYo = "Ìdánwò Ẹ̀yà Ohun Ọ̀gbìn",
                        questions = listOf(
                            QuizQuestion(
                                id = "q2_1",
                                promptEn = "Which part of the plant absorbs water from the soil?",
                                promptYo = "Ẹ̀yà wo nínú ohun ọ̀gbìn ló ń fa omi láti inú ilẹ̀?",
                                optionsEn = listOf("The flower", "The root", "The leaf", "The branch"),
                                optionsYo = listOf("Òdòdó", "Gbòǹgbò", "Ewé", "Ẹ̀ka igi"),
                                correctIndex = 1,
                                explanationEn = "The root (gbòǹgbò) goes deep into soil to drink water and anchor the plant.",
                                explanationYo = "Gbòǹgbò ń wọ inú ilẹ̀ láti fa omi àti láti di igi mú ṣinṣin.",
                                iconEmoji = "🌱"
                            ),
                            QuizQuestion(
                                id = "q2_2",
                                promptEn = "Where does the plant make food using sunlight?",
                                promptYo = "Níbo ni ohun ọ̀gbìn ti ń se oúnjẹ pẹ̀lú ìmọ́lẹ̀ oòrùn?",
                                optionsEn = listOf("Inside the roots", "In the green leaves", "In the dry bark", "Under the rock"),
                                optionsYo = listOf("Inú gbòǹgbò", "Ní ara àwọn ewé tútù", "Ní ara èèpo igi", "Lábẹ́ òkúta"),
                                correctIndex = 1,
                                explanationEn = "Green leaves (ewé) act like the kitchen of the plant, cooking food using sunlight!",
                                explanationYo = "Ewé tútù dàbí ilé-ìdáná ohun ọ̀gbìn, wọ́n ń se oúnjẹ pẹ̀lú ìmọ́lẹ̀ oòrùn!",
                                iconEmoji = "🌿"
                            ),
                            QuizQuestion(
                                id = "q2_3",
                                promptEn = "Mango and orange are examples of which plant part?",
                                promptYo = "Máńgòrò àti ọsàn jẹ́ àpẹẹrẹ ẹ̀yà wo nínú ohun ọ̀gbìn?",
                                optionsEn = listOf("Roots", "Fruits", "Leaves", "Bark"),
                                optionsYo = listOf("Gbòǹgbò", "Èso", "Ewé", "Èèpo igi"),
                                correctIndex = 1,
                                explanationEn = "Mango and orange are sweet fruits (èso) that contain seeds.",
                                explanationYo = "Máńgòrò àti ọsàn jẹ́ èso dídùn tí ó ní kóró nínú.",
                                iconEmoji = "🍊"
                            )
                        )
                    )
                )
            )
        ),

        // Topic 3: The Human Body and Senses
        ScienceTopic(
            id = "topic_human_body",
            grade = 2,
            titleEn = "Human Body & Senses",
            titleYo = "Ara Ènìyàn àti Àwọn Ìmọ̀-Ìtọ́jú",
            iconEmoji = "👀",
            colorHex = 0xFF1565C0, // Blue
            descriptionEn = "Explore your eyes, ears, nose, tongue, and skin that tell you about the world.",
            descriptionYo = "Ṣe àwárí ojú, etí, imú, ahọ́n, àti awọ-ara tí ń sọ nípa ayé fún ọ.",
            lessons = listOf(
                Lesson(
                    id = "lesson_five_senses",
                    topicId = "topic_human_body",
                    grade = 2,
                    titleEn = "The Five Wonder Senses",
                    titleYo = "Àwọn Ìmọ̀-Ìtọ́jú Márùn-ún Àgbàyanu",
                    summaryEn = "Our body uses 5 senses to see, hear, smell, taste, and feel everything around us.",
                    summaryYo = "Ara wa ń lo ìmọ̀-ìtọ́jú márùn-ún láti ríran, gbọ́ràn, gbọ́ òórùn, tọ́wò, àti fọwọ́kàn nǹkan.",
                    iconEmoji = "🧠",
                    isCompleted = false,
                    isCachedOffline = true,
                    sections = listOf(
                        LessonSection(
                            id = "sec_3_1",
                            headingEn = "Eyes (Ojú) & Ears (Etí)",
                            headingYo = "Ojú fún Ríran àti Etí fún Gbígbọ́ran",
                            contentEn = "• Ojú (Eyes): We use our two eyes to see colors, shapes, and read books.\n• Etí (Ears): We use our two ears to hear teacher's voice, drums, and birds singing.",
                            contentYo = "• Ojú: A ń lo ojú méjèèjì láti rí àwọ̀, àwòrán, àti láti ka ìwé.\n• Etí: A ń lo etí méjèèjì láti gbọ́ ohùn olùkọ́, ìlù dídùn, àti orin ẹyẹ.",
                            funFactEn = "Your ears also help you balance when running or riding a bicycle!",
                            funFactYo = "Etí rẹ tún ń ràn ọ́ lọ́wọ́ láti wà ní ìdúró ṣinṣin nígbà tí o bá ń sáré!",
                            iconEmoji = "👂"
                        ),
                        LessonSection(
                            id = "sec_3_2",
                            headingEn = "Nose (Imú) & Tongue (Ahọ́n)",
                            headingYo = "Imú fún Òórùn àti Ahọ́n fún Títọ́wò",
                            contentEn = "• Imú (Nose): Smells sweet flowers or warns us of burning smoke.\n• Ahọ́n (Tongue): Tastes sweet honey (oyin), salty soup (ọbẹ̀), sour lime (ọsàn wẹ́wẹ́), and bitter leaf (ewúro).",
                            contentYo = "• Imú: Ó ń gbọ́ òórùn dídùn bí òdòdó, tàbí kìlọ̀ fún wa nípa èéfín iná.\n• Ahọ́n: Ó ń tọ́ oyin dídùn, ọbẹ̀ iyọ̀, ọsàn kíkan, àti ewúro korò wò.",
                            funFactEn = "Your tongue has thousands of tiny taste buds to celebrate tasty food!",
                            funFactYo = "Ahọ́n rẹ ní ẹgbẹẹgbẹ̀rún àwọn kóró kékèké tí ń jẹ́ kí o mọ adùn oúnjẹ!",
                            iconEmoji = "👅"
                        ),
                        LessonSection(
                            id = "sec_3_3",
                            headingEn = "Skin (Awọ-Ara) for Touch",
                            headingYo = "Awọ-Ara fún Fífọwọ́bà",
                            contentEn = "Our skin covers the entire body. It feels hot sunshine, cold ice water, soft cotton, and rough stone bark.",
                            contentYo = "Awọ-ara bo gbogbo ara wa mọ́lẹ̀. Ó ń mọ igbóná oòrùn, tútù omi yìnyín, rírọ̀ òwú, àti líle òkúta.",
                            funFactEn = "Always wash your hands with soap and water to protect your skin from germs!",
                            funFactYo = "Máa fi ọṣẹ àti omi fọ ọwọ́ rẹ nígbà gbogbo láti dáàbò bo awọ rẹ kúrò lọ́wọ́ kòkòrò!",
                            iconEmoji = "🧼"
                        )
                    ),
                    vocabulary = listOf(
                        VocabularyWord("Eye", "Ojú", "O-jú", "Organ for seeing", "Ẹ̀yà ara fún ríran", "👀"),
                        VocabularyWord("Ear", "Etí", "E-tí", "Organ for hearing sounds", "Ẹ̀yà ara fún gbígbọ́ran", "👂"),
                        VocabularyWord("Nose", "Imú", "I-mú", "Organ for smelling and breathing", "Ẹ̀yà ara fún gbígbọ́ òórùn àti mími", "👃"),
                        VocabularyWord("Tongue", "Ahọ́n", "A-họ́n", "Organ in the mouth for tasting food", "Ẹ̀yà inú ẹnu fún títọ́wò", "👅"),
                        VocabularyWord("Skin", "Awọ-Ara", "A-wọ-A-ra", "Outer cover that feels touch and temperature", "Ẹ̀yà tí ó bo gbogbo ara fún fífọwọ́bà", "✋")
                    ),
                    experimentGuide = ExperimentGuide(
                        titleEn = "Blindfold Mystery Game",
                        titleYo = "Eré Ìfojúdé Àràmàdà",
                        materialsEn = listOf("A clean scarf or handkerchief", "An orange", "A bell or rattle", "An ice cube"),
                        materialsYo = listOf("Gèlè mímọ́ tàbí aṣọ ìnuwọ́", "Ọsàn dídùn kan", "Agogo tàbí ṣẹ̀kẹ̀rẹ̀", "Kóró yìnyín tútù"),
                        stepsEn = listOf(
                            "Cover pupil's eyes with the clean scarf.",
                            "Let them smell the orange: which sense was used? Nose!",
                            "Shake the bell: which sense was used? Ears!",
                            "Touch the ice cube: which sense was used? Skin!"
                        ),
                        stepsYo = listOf(
                            "Fi aṣọ bo ojú akẹ́kọ̀ọ́ náà.",
                            "Jẹ́ kí ó gbọ́ òórùn ọsàn náà: ẹ̀yà wo ló lò? Imú!",
                            "Mi agogo náà: ẹ̀yà wo ló lò? Etí!",
                            "Fọwọ́kan kóró yìnyín náà: ẹ̀yà wo ló lò? Awọ-ara!"
                        ),
                        outcomeEn = "Pupils learn that we can understand the world using our other senses even without seeing!",
                        outcomeYo = "Àwọn akẹ́kọ̀ọ́ yóò mọ̀ pé a lè lo àwọn ìmọ̀-ìtọ́jú yòókù láti mọ nǹkan bí a kò tilẹ̀ ríran!"
                    ),
                    quiz = Quiz(
                        id = "quiz_five_senses",
                        lessonId = "lesson_five_senses",
                        titleEn = "Sense Organs Quiz",
                        titleYo = "Ìdánwò Àwọn Ìmọ̀-Ìtọ́jú",
                        questions = listOf(
                            QuizQuestion(
                                id = "q3_1",
                                promptEn = "Which sense organ do you use to hear your teacher call your name?",
                                promptYo = "Ẹ̀yà ara wo ni o ń lò láti gbọ́ bí olùkọ́ ṣe ń pe orúkọ rẹ?",
                                optionsEn = listOf("Eyes", "Ears", "Nose", "Teeth"),
                                optionsYo = listOf("Ojú", "Etí", "Imú", "Eyín"),
                                correctIndex = 1,
                                explanationEn = "Ears (etí) capture sound waves and send them to your brain so you can hear.",
                                explanationYo = "Etí ló ń gba ohùn tí ó sì ń jẹ́ kí a gbọ́ràn kedere.",
                                iconEmoji = "👂"
                            ),
                            QuizQuestion(
                                id = "q3_2",
                                promptEn = "Which organ tastes sweetness in honey or salt in jollof rice?",
                                promptYo = "Ẹ̀yà wo ló ń tọ́ adùn oyin tàbí iyọ̀ inú ìrẹsì jọlọ́ọ̀fù wò?",
                                optionsEn = listOf("Tongue", "Skin", "Hair", "Elbow"),
                                optionsYo = listOf("Ahọ́n", "Awọ", "Irun", "Ìkọkọ"),
                                correctIndex = 0,
                                explanationEn = "The tongue (ahọ́n) has taste buds to recognize sweet, salty, sour, and bitter.",
                                explanationYo = "Ahọ́n ló ní àwọn kókó ìtọ́wò tí ń fi adùn, iyọ̀, kíkan, àti korò hàn.",
                                iconEmoji = "👅"
                            ),
                            QuizQuestion(
                                id = "q3_3",
                                promptEn = "How many main senses does the human body have?",
                                promptYo = "Mélòó ni àwọn ìmọ̀-ìtọ́jú pàtàkì tí ara ènìyàn ní?",
                                optionsEn = listOf("Two (2)", "Five (5)", "Ten (10)", "One (1)"),
                                optionsYo = listOf("Méjì (2)", "Márùn-ún (5)", "Mẹ́wàá (10)", "Ọ̀kan (1)"),
                                correctIndex = 1,
                                explanationEn = "We have 5 wonder senses: Sight, Hearing, Smell, Taste, and Touch!",
                                explanationYo = "A ní ìmọ̀-ìtọ́jú márùn-ún pàtàkì: Ríran, Gbígbọ́ran, Òórùn, Ìtọ́wò, àti Fífọwọ́bà!",
                                iconEmoji = "🖐️"
                            )
                        )
                    )
                )
            )
        ),

        // Topic 4: Animals & Habitats
        ScienceTopic(
            id = "topic_animals",
            grade = 3,
            titleEn = "Animals & Their Homes",
            titleYo = "Àwọn Ẹranko àti Ibùgbé Wọn",
            iconEmoji = "🦁",
            colorHex = 0xFFE65100, // Amber
            descriptionEn = "Learn about domestic and wild animals, and how they thrive in water, forest, or air.",
            descriptionYo = "Kọ́ nípa ẹranko ilé àti ẹranko igbó, àti bí wọ́n ṣe ń gbé inú omi, igbó, tàbí ojú ọ̀run.",
            lessons = listOf(
                Lesson(
                    id = "lesson_animal_habitats",
                    topicId = "topic_animals",
                    grade = 3,
                    titleEn = "Where Do Animals Live?",
                    titleYo = "Níbo Ni Àwọn Ẹranko Ń Gbé?",
                    summaryEn = "Some animals live on land (orí ilẹ̀), others in water (inú omi), or fly in air (ojú ọ̀run).",
                    summaryYo = "Àwọn ẹranko kan ń gbé orí ilẹ̀, àwọn mìíràn inú omi, àti ojú ọ̀run.",
                    iconEmoji = "🐠",
                    isCompleted = false,
                    isCachedOffline = true,
                    sections = listOf(
                        LessonSection(
                            id = "sec_4_1",
                            headingEn = "Land Animals (Ẹranko Orí Ilẹ̀)",
                            headingYo = "Àwọn Ẹranko Orí Ilẹ̀",
                            contentEn = "Land animals walk or run with legs. Domestic animals live with us at home: Dog (Ajá), Cat (Ológbò), Goat (Ewúrẹ́). Wild animals live in deep forests: Lion (Kìnìún), Elephant (Erin).",
                            contentYo = "Àwọn ẹranko orí ilẹ̀ ń lo ẹsẹ̀ láti rìn. Ẹranko ilé ń bá wa gbé nílé: Ajá, Ológbò, Ewúrẹ́. Ẹranko igbó ń gbé inú aginjù: Kìnìún, Erin títóbi.",
                            funFactEn = "Dogs have a sense of smell forty times stronger than humans!",
                            funFactYo = "Imú ajá lágbára láti gbọ́ òórùn ní ìlọ́po ogójì ju ti ènìyàn lọ!",
                            iconEmoji = "🐕"
                        ),
                        LessonSection(
                            id = "sec_4_2",
                            headingEn = "Water Animals (Ẹranko Inú Omi)",
                            headingYo = "Àwọn Ẹranko Inú Omi",
                            contentEn = "Water animals have fins and gills to breathe underwater. Examples: Fish (Ẹja), Crab (Alákàn), Turtle (Ìbín/Àwun). If you take fish out of water, they cannot breathe air!",
                            contentYo = "Àwọn ẹranko inú omi ní lẹ́bẹ́ láti lúwẹ̀ẹ́ àti ẹ̀rẹ̀kẹ́ omi láti mí lábẹ́ omi. Àpẹẹrẹ: Ẹja, Alákàn, Àwun. Bí o bá mú ẹja jáde kúrò nínú omi, wọn kò lè mí afẹ́fẹ́!",
                            funFactEn = "Some electric fish in West African rivers can produce gentle sparks to find their way!",
                            funFactYo = "Àwọn ẹja kan ní odò ilẹ̀ Áfíríkà lè kọ iná mọ̀nàmọ́ná jẹ́jẹ́ láti mọ ọ̀nà wọn!",
                            iconEmoji = "🐟"
                        )
                    ),
                    vocabulary = listOf(
                        VocabularyWord("Animal", "Ẹranko", "Ẹ-ran-ko", "Creatures that move and feed", "Àwọn ẹ̀dá alààyè tí ń rìn", "🦓"),
                        VocabularyWord("Habitat", "Ibùgbé", "I-bù-gbé", "Natural home of an animal", "Ilé àti ibi tí ẹranko ń gbé", "🏡"),
                        VocabularyWord("Fish", "Ẹja", "Ẹ-ja", "Animal living and swimming in water", "Ẹranko tó ń gbé inú omi", "🐠"),
                        VocabularyWord("Bird", "Ẹyẹ", "Ẹ-yẹ", "Feathered creature flying in the air", "Ẹ̀dá abiyẹ́ tí ń fò ní ojú ọ̀run", "🦅")
                    ),
                    quiz = Quiz(
                        id = "quiz_animal_homes",
                        lessonId = "lesson_animal_habitats",
                        titleEn = "Animal Homes Quiz",
                        titleYo = "Ìdánwò Ibùgbé Ẹranko",
                        questions = listOf(
                            QuizQuestion(
                                id = "q4_1",
                                promptEn = "Where is the natural home of a fish?",
                                promptYo = "Níbo ni ibùgbé àdánidá ti ẹja?",
                                optionsEn = listOf("Inside a tree trunk", "Inside water", "On dry desert sand", "In the sky"),
                                optionsYo = listOf("Inú igi", "Inú omi", "Orí iyanrìn", "Ojú ọ̀run"),
                                correctIndex = 1,
                                explanationEn = "Fish live and breathe in water using their gills.",
                                explanationYo = "Inú omi ni ẹja ń gbé, ibẹ̀ sì ni ó ti ń mí.",
                                iconEmoji = "🐟"
                            ),
                            QuizQuestion(
                                id = "q4_2",
                                promptEn = "Which of the following is a DOMESTIC animal kept at home?",
                                promptYo = "Èwo nínú àwọn wọ̀nyí ni ẸRANKO ILÉ?",
                                optionsEn = listOf("Lion (Kìnìún)", "Dog (Ajá)", "Hyena (Ìkòòkò)", "Elephant (Erin)"),
                                optionsYo = listOf("Kìnìún", "Ajá", "Ìkòòkò", "Erin"),
                                correctIndex = 1,
                                explanationEn = "A dog (ajá) is a domestic pet and friend of humans.",
                                explanationYo = "Ajá jẹ́ ẹranko ilé tí ó jẹ́ ọ̀rẹ́ ènìyàn.",
                                iconEmoji = "🐶"
                            )
                        )
                    )
                )
            )
        ),

        // Topic 5: Weather and Seasons
        ScienceTopic(
            id = "topic_weather",
            grade = 3,
            titleEn = "Weather & Nigerian Seasons",
            titleYo = "Ojú Ọjọ́ àti Àwọn Àkókò Ọdún",
            iconEmoji = "⛅",
            colorHex = 0xFF00838F, // Teal
            descriptionEn = "Explore rain, sunshine, harmattan wind, and the rhythm of nature.",
            descriptionYo = "Ṣe àwárí òjò, oòrùn, afẹ́fẹ́ ọ̀yẹ́, àti bí àkókò ṣe ń yípo nínú ọdún.",
            lessons = listOf(
                Lesson(
                    id = "lesson_seasons",
                    topicId = "topic_weather",
                    grade = 3,
                    titleEn = "Seasons in Nigeria (Àwọn Àkókò)",
                    titleYo = "Àwọn Àkókò Pàtàkì ní Orílẹ̀-èdè Nàìjíríà",
                    summaryEn = "We experience Rainy season (Àkókò Òjò), Dry season (Àkókò Ọ̀gbẹlẹ̀), and Harmattan (Ọ̀yẹ́).",
                    summaryYo = "A ní Àkókò Òjò, Àkókò Ọ̀gbẹlẹ̀, àti Àkókò Ọ̀yẹ́.",
                    iconEmoji = "🌧️",
                    isCompleted = false,
                    isCachedOffline = true,
                    sections = listOf(
                        LessonSection(
                            id = "sec_5_1",
                            headingEn = "Rainy Season (Àkókò Òjò)",
                            headingYo = "Àkókò Òjò",
                            contentEn = "Dark clouds gather in the sky, thunder rumbles, and rain pours! Farmers plant maize, yams, and vegetables. Rain fills our rivers and wells with drinking water.",
                            contentYo = "Àwọsánmọ̀ dúdú ń kóra jọ sí ojú ọ̀run, ààrá ń sán, òjò sì ń rọ̀! Àwọn àgbẹ̀ ń gbin àgbàdo, iṣu, àti ẹ̀fọ́. Òjò ń kún odò àti kànga wa pẹ̀lú omi mímu.",
                            funFactEn = "Rain comes from water on the ground that evaporates up to form clouds!",
                            funFactYo = "Omi òjò ń wá láti inú omi ilẹ̀ tí ó gbóná tí ó sì fò lọ sí ojú ọ̀run di àwọsánmọ̀!",
                            iconEmoji = "⛈️"
                        ),
                        LessonSection(
                            id = "sec_5_2",
                            headingEn = "Dry Season & Harmattan (Ọ̀gbẹlẹ̀ àti Ọ̀yẹ́)",
                            headingYo = "Àkókò Ọ̀gbẹlẹ̀ àti Ọ̀yẹ́",
                            contentEn = "During dry season, the sun shines brightly. Harmattan brings cool, dusty, dry winds from the northern Sahara desert. We wear warm sweaters and apply shea butter (orí) to our lips!",
                            contentYo = "Ní àsìkò ọ̀gbẹlẹ̀, oòrùn ń ràn gan-an. Àsìkò ọ̀yẹ́ ń mú afẹ́fẹ́ tútù, eruku, àti gbigbẹ wá. A ń wọ aṣọ tútù (ṣúwẹ́tà) a sì ń kùn orí sí ètè wa!",
                            funFactEn = "Harmattan mornings are very foggy, making the sun look like an orange ball!",
                            funFactYo = "Òwúrọ̀ àsìkò ọ̀yẹ́ máa ń kún fún ìkùukùu tó ń jẹ́ kí oòrùn rí bí bọ́ọ̀lù pupa!",
                            iconEmoji = "🧣"
                        )
                    ),
                    vocabulary = listOf(
                        VocabularyWord("Rain", "Òjò", "Ò-jò", "Water falling in drops from clouds", "Omi tó ń kán bọ̀ láti àwọsánmọ̀", "🌧️"),
                        VocabularyWord("Sun", "Oòrùn", "Oò-rùn", "Star that gives daylight and warmth", "Ìmọ́lẹ̀ ńlá tó ń fún wa ní ooru àti ìmọ́lẹ̀", "☀️"),
                        VocabularyWord("Harmattan", "Ọ̀yẹ́", "Ọ̀-yẹ́", "Dry dusty wind season in West Africa", "Àkókò afẹ́fẹ́ tútù àti eruku", "🌬️"),
                        VocabularyWord("Cloud", "Àwọsánmọ̀", "À-wọ-sán-mọ̀", "White or gray vapor floating in the sky", "Ìkùukùu funfun tàbí dúdú ní ojú ọ̀run", "☁️")
                    ),
                    quiz = Quiz(
                        id = "quiz_seasons",
                        lessonId = "lesson_seasons",
                        titleEn = "Weather & Seasons Quiz",
                        titleYo = "Ìdánwò Ojú Ọjọ́",
                        questions = listOf(
                            QuizQuestion(
                                id = "q5_1",
                                promptEn = "Which season brings heavy rain helping farmers plant their crops?",
                                promptYo = "Àkókò wo ló ń mú òjò ńlá wá tí ó ń ran àwọn àgbẹ̀ lọ́wọ́ láti gbin oúnjẹ?",
                                optionsEn = listOf("Dry season", "Rainy season", "Harmattan season", "Snow season"),
                                optionsYo = listOf("Àkókò Ọ̀gbẹlẹ̀", "Àkókò Òjò", "Àkókò Ọ̀yẹ́", "Àkókò Yìnyín"),
                                correctIndex = 1,
                                explanationEn = "Rainy season (àkókò òjò) provides plentiful water for plants to sprout.",
                                explanationYo = "Àkókò òjò ń pèsè omi tó pọ̀ fún àwọn ohun ọ̀gbìn láti hù.",
                                iconEmoji = "🌧️"
                            ),
                            QuizQuestion(
                                id = "q5_2",
                                promptEn = "What traditional item helps protect dry skin and lips during Harmattan?",
                                promptYo = "Kí ni a máa ń kùn sí awọ àti ètè wa ní àsìkò ọ̀yẹ́?",
                                optionsEn = listOf("Red pepper", "Shea butter (Orí)", "Sand", "Cold water"),
                                optionsYo = listOf("Ata pupa", "Orí mímọ́", "Iyanrìn", "Omi tútù"),
                                correctIndex = 1,
                                explanationEn = "Shea butter (orí) keeps our lips and skin smooth and moisturized during cold harmattan winds.",
                                explanationYo = "Orí ń mú kí awọ àti ètè wa rọ̀, kò sì ní ya nígbà afẹ́fẹ́ ọ̀yẹ́.",
                                iconEmoji = "🧈"
                            )
                        )
                    )
                )
            )
        )
    )
}
