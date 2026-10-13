package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUploadLessonScreen(
    onUploadLesson: (Lesson) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var grade by remember { mutableStateOf(2) }
    var topicCategory by remember { mutableStateOf("topic_custom_uploads") }
    var titleYo by remember { mutableStateOf("Ojú Ọ̀run àti Oòrùn") }
    var titleEn by remember { mutableStateOf("The Sun and the Sky") }
    var summaryYo by remember { mutableStateOf("Kọ́ bí oòrùn ṣe ń fún ayé ní ìmọ́lẹ̀ àti ooru, àti bí àwọn ìràwọ̀ ṣe ń tàn lálẹ́.") }
    var summaryEn by remember { mutableStateOf("Learn how the sun gives Earth light and warmth, and how stars glow in the night sky.") }
    var selectedEmoji by remember { mutableStateOf("☀️") }

    // Section 1
    var sec1HeadingYo by remember { mutableStateOf("Oòrùn: Ìmọ́lẹ̀ Ńlá Ayé") }
    var sec1HeadingEn by remember { mutableStateOf("The Sun: Earth's Great Light") }
    var sec1ContentYo by remember { mutableStateOf("Oòrùn jẹ́ ìràwọ̀ ńlá tí ó wà ní àárín ojú ọ̀run wa. Ó ń fún gbogbo ohun ọ̀gbìn àti ènìyàn ní ìmọ́lẹ̀ àti ooru.") }
    var sec1ContentEn by remember { mutableStateOf("The sun is a giant star at the center of our solar system. It provides essential light and heat for all plants and animals.") }
    var sec1FunFactYo by remember { mutableStateOf("Ṣé o mọ̀ pé ìmọ́lẹ̀ oòrùn máa ń gba ìṣẹ́jú mẹ́jọ péré láti dé orí ilẹ̀ ayé?") }
    var sec1FunFactEn by remember { mutableStateOf("Did you know it takes sunlight only about 8 minutes to travel all the way to Earth?") }

    // Vocab
    var vocabYo by remember { mutableStateOf("Oòrùn") }
    var vocabEn by remember { mutableStateOf("Sun") }
    var vocabTone by remember { mutableStateOf("Oò-rùn") }
    var vocabDefYo by remember { mutableStateOf("Ìràwọ̀ ńlá tó ń tan ìmọ́lẹ̀") }
    var vocabDefEn by remember { mutableStateOf("The star providing daylight and heat") }

    // Quiz Question
    var quizPromptYo by remember { mutableStateOf("Kí ni oòrùn ń fún àwọn ohun alààyè ní ayé?") }
    var quizPromptEn by remember { mutableStateOf("What does the sun give to living things on Earth?") }
    var opt0Yo by remember { mutableStateOf("Yìnyín tútù") }
    var opt0En by remember { mutableStateOf("Cold ice") }
    var opt1Yo by remember { mutableStateOf("Ìmọ́lẹ̀ àti ooru") }
    var opt1En by remember { mutableStateOf("Light and warmth") }
    var opt2Yo by remember { mutableStateOf("Oúnjẹ bí iṣu") }
    var opt2En by remember { mutableStateOf("Cooked yam") }
    var correctOptIndex by remember { mutableStateOf(1) }

    var uploadSuccess by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val yorubaDiacritics = listOf("á", "à", "é", "è", "ẹ́", "ẹ̀", "í", "ì", "ó", "ò", "ọ́", "ọ̀", "ú", "ù", "ṣ")
    val emojis = listOf("☀️", "🌱", "🔬", "🦁", "🌊", "🧠", "🐾", "⭐", "🌍", "💡")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Admin Lesson Upload Studio",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Publish new bilingual science curriculum",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_admin_upload")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            // Pre-fill with sample
                            titleYo = "Omi àti Àwọn Ìṣàn Rẹ̀"
                            titleEn = "Water and Its Properties"
                            summaryYo = "Kọ́ bí omi ṣe ń wúlò fún mímu, fífọ aṣọ, àti bí a ṣe ń tọ́jú rẹ̀ mọ́."
                            summaryEn = "Learn the importance of clean water for drinking, washing, and keeping healthy."
                            selectedEmoji = "💧"
                            sec1HeadingYo = "Omi Mímu Mímọ́"
                            sec1HeadingEn = "Clean Drinking Water"
                            sec1ContentYo = "Omi mímọ́ ṣe pàtàkì fún ẹ̀mí wa. A gbọ́dọ̀ se omi kí a tó mu láti pa kòkòrò."
                            sec1ContentEn = "Clean water is essential for life. Boiling water kills harmful germs before drinking."
                            sec1FunFactYo = "Ìpín mẹ́ta nínú mẹ́rin ara wa jẹ́ omi!"
                            sec1FunFactEn = "About 70% of the human body is made up of water!"
                            vocabYo = "Omi"
                            vocabEn = "Water"
                            vocabTone = "O-mi"
                            vocabDefYo = "Ohun mímu alààyè tí kò ní àwọ̀"
                            vocabDefEn = "Clear liquid necessary for all living things"
                            quizPromptYo = "Èéṣe tí a fi ń se omi kí a tó mu?"
                            quizPromptEn = "Why do we boil water before drinking?"
                            opt0Yo = "Láti pa kòkòrò àrùn"
                            opt0En = "To kill harmful germs"
                            opt1Yo = "Láti jẹ́ kí ó dùn bí oyin"
                            opt1En = "To make it sweet like honey"
                            opt2Yo = "Láti fi sí inú àpò"
                            opt2En = "To keep inside bag"
                            correctOptIndex = 0
                        },
                        modifier = Modifier.testTag("btn_sample_preset")
                    ) {
                        Text("Load Sample", fontSize = 12.sp, color = ScienceBlue, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Yoruba Diacritic Keyboard Helper Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ScienceGreenLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Àmì Ohùn Yorùbá (Quick Diacritic Marks):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = ScienceGreen
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(yorubaDiacritics) { char ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White,
                                modifier = Modifier
                                    .clickable {
                                        // Append to active title
                                        titleYo += char
                                    }
                                    .padding(2.dp)
                            ) {
                                Text(
                                    text = char,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ScienceGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // General Settings Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("1. Kíláàsì & Àkọlé (Class & Titles)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = ScienceBlue)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Target Primary Class:", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            (1..6).forEach { g ->
                                val isSelected = grade == g
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) ScienceBlue else ScienceBlueLight)
                                        .clickable { grade = g },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("P$g", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = if (isSelected) Color.White else ScienceBlue)
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = titleYo,
                        onValueChange = { titleYo = it },
                        label = { Text("Àkọlé ní Yorùbá (Yoruba Title)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_admin_title_yo"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = titleEn,
                        onValueChange = { titleEn = it },
                        label = { Text("English Title") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_admin_title_en"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = summaryYo,
                        onValueChange = { summaryYo = it },
                        label = { Text("Àkópọ̀ ní Yorùbá (Yoruba Summary)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = summaryEn,
                        onValueChange = { summaryEn = it },
                        label = { Text("English Summary") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Text("Pick Emoji Icon:", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(emojis) { emoji ->
                            val isSel = selectedEmoji == emoji
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isSel) ScienceYellowLight else Color(0xFFF5F5F5))
                                    .clickable { selectedEmoji = emoji },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(emoji, fontSize = 18.sp)
                            }
                        }
                    }
                }
            }

            // Section 1 Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("2. Àkóónú Ẹ̀kọ́ (Lesson Section 1)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = ScienceGreen)

                    OutlinedTextField(
                        value = sec1HeadingYo,
                        onValueChange = { sec1HeadingYo = it },
                        label = { Text("Section Heading (Yorùbá)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = sec1HeadingEn,
                        onValueChange = { sec1HeadingEn = it },
                        label = { Text("Section Heading (English)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = sec1ContentYo,
                        onValueChange = { sec1ContentYo = it },
                        label = { Text("Content (Yorùbá Explanation)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4,
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = sec1ContentEn,
                        onValueChange = { sec1ContentEn = it },
                        label = { Text("Content (English Explanation)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4,
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = sec1FunFactYo,
                        onValueChange = { sec1FunFactYo = it },
                        label = { Text("Ṣé o mọ̀ pé...? (Yoruba Fun Fact)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = sec1FunFactEn,
                        onValueChange = { sec1FunFactEn = it },
                        label = { Text("Did you know...? (English Fun Fact)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Vocabulary Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("3. Ọ̀rọ̀ Pàtàkì (Key Vocabulary)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = ScienceCoral)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = vocabYo,
                            onValueChange = { vocabYo = it },
                            label = { Text("Yorùbá") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = vocabEn,
                            onValueChange = { vocabEn = it },
                            label = { Text("English") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }

                    OutlinedTextField(
                        value = vocabTone,
                        onValueChange = { vocabTone = it },
                        label = { Text("Àmì Ohùn (e.g. Dọ̀-Rẹ-Mí)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = vocabDefYo,
                        onValueChange = { vocabDefYo = it },
                        label = { Text("Definition (Yorùbá)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = vocabDefEn,
                        onValueChange = { vocabDefEn = it },
                        label = { Text("Definition (English)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }

            // Quiz Question Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("4. Ìbéèrè Ìdánwò (Assessment Question)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = ScienceYellow)

                    OutlinedTextField(
                        value = quizPromptYo,
                        onValueChange = { quizPromptYo = it },
                        label = { Text("Prompt (Yorùbá)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = quizPromptEn,
                        onValueChange = { quizPromptEn = it },
                        label = { Text("Prompt (English)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Text("Options (A, B, C):", fontSize = 13.sp, fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value = opt0Yo,
                        onValueChange = { opt0Yo = it },
                        label = { Text("Option A (Yorùbá)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            RadioButton(selected = correctOptIndex == 0, onClick = { correctOptIndex = 0 })
                        }
                    )

                    OutlinedTextField(
                        value = opt1Yo,
                        onValueChange = { opt1Yo = it },
                        label = { Text("Option B (Yorùbá)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            RadioButton(selected = correctOptIndex == 1, onClick = { correctOptIndex = 1 })
                        }
                    )

                    OutlinedTextField(
                        value = opt2Yo,
                        onValueChange = { opt2Yo = it },
                        label = { Text("Option C (Yorùbá)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            RadioButton(selected = correctOptIndex == 2, onClick = { correctOptIndex = 2 })
                        }
                    )

                    Text(
                        text = "Correct Option: ${if (correctOptIndex == 0) "A" else if (correctOptIndex == 1) "B" else "C"}",
                        fontSize = 12.sp,
                        color = ScienceGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (uploadSuccess) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ScienceGreenLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ScienceGreen)
                        Column {
                            Text(
                                text = "A ti tẹ̀jáde ẹ̀kọ́ náà ní àṣeyọrí!",
                                fontWeight = FontWeight.Bold,
                                color = ScienceGreen,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Lesson uploaded to Room database and queued for Base44 sync.",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Publish Button
            Button(
                onClick = {
                    val lessonId = "lesson_admin_" + UUID.randomUUID().toString().take(8)
                    val newLesson = Lesson(
                        id = lessonId,
                        topicId = topicCategory,
                        grade = grade,
                        titleEn = titleEn,
                        titleYo = titleYo,
                        summaryEn = summaryEn,
                        summaryYo = summaryYo,
                        iconEmoji = selectedEmoji,
                        isCompleted = false,
                        isCachedOffline = true,
                        sections = listOf(
                            LessonSection(
                                id = "sec_${lessonId}_1",
                                headingEn = sec1HeadingEn,
                                headingYo = sec1HeadingYo,
                                contentEn = sec1ContentEn,
                                contentYo = sec1ContentYo,
                                funFactEn = sec1FunFactEn,
                                funFactYo = sec1FunFactYo,
                                iconEmoji = selectedEmoji
                            )
                        ),
                        vocabulary = listOf(
                            VocabularyWord(
                                english = vocabEn,
                                yoruba = vocabYo,
                                toneMarks = vocabTone,
                                definitionEn = vocabDefEn,
                                definitionYo = vocabDefYo,
                                emoji = selectedEmoji
                            )
                        ),
                        quiz = Quiz(
                            id = "quiz_$lessonId",
                            lessonId = lessonId,
                            titleEn = "$titleEn Quiz",
                            titleYo = "Ìdánwò $titleYo",
                            questions = listOf(
                                QuizQuestion(
                                    id = "q_${lessonId}_1",
                                    promptEn = quizPromptEn,
                                    promptYo = quizPromptYo,
                                    optionsEn = listOf(opt0En, opt1En, opt2En),
                                    optionsYo = listOf(opt0Yo, opt1Yo, opt2Yo),
                                    correctIndex = correctOptIndex,
                                    explanationEn = "Correct answer based on the lesson.",
                                    explanationYo = "Ìdáhùn tó tọ́ gẹ́gẹ́ bí ẹ̀kọ́ náà ṣe sọ.",
                                    iconEmoji = selectedEmoji
                                )
                            )
                        )
                    )
                    onUploadLesson(newLesson)
                    uploadSuccess = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_publish_lesson_action"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ScienceBlue)
            ) {
                Icon(Icons.Default.CloudUpload, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tẹ̀jáde sí Kíláàsì (Publish Lesson)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
