package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.LanguageMode
import com.example.data.model.Lesson
import com.example.ui.components.AudioNarrationCard
import com.example.ui.components.BilingualText
import com.example.ui.components.PostLessonQuizComponent
import com.example.ui.components.VocabularyChip
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonReaderScreen(
    lesson: Lesson,
    currentLanguageMode: LanguageMode,
    isSpeaking: Boolean,
    onSpeakText: (String, Boolean) -> Unit,
    onStopSpeech: () -> Unit,
    audioSpeed: Float,
    onSpeedChange: (Float) -> Unit,
    onLanguageChange: (LanguageMode) -> Unit,
    onStartQuiz: () -> Unit,
    onQuizCompleted: (score: Int, total: Int) -> Unit = { _, _ -> },
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = lesson.titleYo,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 1
                        )
                        Text(
                            text = lesson.titleEn,
                            fontSize = 12.sp,
                            color = TextMuted,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("btn_back_lesson")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Quick language toggle
                    IconButton(
                        onClick = {
                            val nextMode = when (currentLanguageMode) {
                                LanguageMode.BILINGUAL -> LanguageMode.YORUBA
                                LanguageMode.YORUBA -> LanguageMode.ENGLISH
                                LanguageMode.ENGLISH -> LanguageMode.BILINGUAL
                            }
                            onLanguageChange(nextMode)
                        },
                        modifier = Modifier.testTag("btn_toggle_lang_lesson")
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ScienceBlueLight
                        ) {
                            Text(
                                text = when (currentLanguageMode) {
                                    LanguageMode.BILINGUAL -> "Àpapọ̀"
                                    LanguageMode.YORUBA -> "YO"
                                    LanguageMode.ENGLISH -> "EN"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = ScienceBlue,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Ṣetan fún Ìdánwò?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Ready to test knowledge?",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                    Button(
                        onClick = onStartQuiz,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ScienceGreen),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                        modifier = Modifier.testTag("btn_start_quiz_bottom")
                    ) {
                        Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ṣe Ìdánwò (Take Quiz)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Audio narration master card
                AudioNarrationCard(
                    isSpeaking = isSpeaking,
                    onSpeakYoruba = {
                        val textToSpeak = buildString {
                            append(lesson.titleYo).append(". ")
                            lesson.sections.forEach {
                                append(it.headingYo).append(". ")
                                append(it.contentYo).append(" ")
                            }
                        }
                        onSpeakText(textToSpeak, true)
                    },
                    onSpeakEnglish = {
                        val textToSpeak = buildString {
                            append(lesson.titleEn).append(". ")
                            lesson.sections.forEach {
                                append(it.headingEn).append(". ")
                                append(it.contentEn).append(" ")
                            }
                        }
                        onSpeakText(textToSpeak, false)
                    },
                    onStop = onStopSpeech,
                    currentSpeed = audioSpeed,
                    onSpeedChange = onSpeedChange
                )
            }

            // Summary Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(lesson.iconEmoji, fontSize = 28.sp)
                            Column {
                                Text(
                                    text = "Àkópọ̀ Ẹ̀kọ́ (Lesson Overview)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ScienceBlue
                                )
                                BilingualText(
                                    yorubaText = lesson.summaryYo,
                                    englishText = lesson.summaryEn,
                                    languageMode = currentLanguageMode,
                                    yorubaStyle = MaterialTheme.typography.bodyLarge,
                                    englishStyle = MaterialTheme.typography.bodyMedium,
                                    showBadges = false
                                )
                            }
                        }
                    }
                }
            }

            // Lesson Sections
            items(lesson.sections) { section ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(section.iconEmoji, fontSize = 22.sp)
                                BilingualText(
                                    yorubaText = section.headingYo,
                                    englishText = section.headingEn,
                                    languageMode = currentLanguageMode,
                                    yorubaStyle = MaterialTheme.typography.titleMedium,
                                    englishStyle = MaterialTheme.typography.titleSmall,
                                    showBadges = false
                                )
                            }

                            IconButton(
                                onClick = {
                                    val isYo = currentLanguageMode == LanguageMode.YORUBA || currentLanguageMode == LanguageMode.BILINGUAL
                                    val text = if (isYo) "${section.headingYo}. ${section.contentYo}" else "${section.headingEn}. ${section.contentEn}"
                                    onSpeakText(text, isYo)
                                },
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(ScienceBlueLight, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Speak Section",
                                    tint = ScienceBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        BilingualText(
                            yorubaText = section.contentYo,
                            englishText = section.contentEn,
                            languageMode = currentLanguageMode,
                            yorubaStyle = MaterialTheme.typography.bodyMedium,
                            englishStyle = MaterialTheme.typography.bodyMedium,
                            showBadges = true
                        )

                        // Fun Fact Callout if available
                        if (section.funFactYo != null && section.funFactEn != null) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = ScienceYellowLight)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text("💡", fontSize = 18.sp)
                                    Column {
                                        Text(
                                            text = "Ṣé O Mọ̀ Pé...? (Did You Know?)",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp,
                                            color = Color(0xFFE65100)
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        BilingualText(
                                            yorubaText = section.funFactYo,
                                            englishText = section.funFactEn,
                                            languageMode = currentLanguageMode,
                                            yorubaStyle = MaterialTheme.typography.bodySmall,
                                            englishStyle = MaterialTheme.typography.bodySmall,
                                            showBadges = false
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Key Vocabulary Section
            item {
                Text(
                    text = "Àwọn Ọ̀rọ̀ Pàtàkì (Key Vocabulary)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            items(lesson.vocabulary) { word ->
                VocabularyChip(
                    word = word,
                    onSpeak = { text, isYo -> onSpeakText(text, isYo) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Hands-on Experiment Guide if present
            if (lesson.experimentGuide != null) {
                val exp = lesson.experimentGuide
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = ScienceCoralLight),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("🔬", fontSize = 26.sp)
                                Column {
                                    Text(
                                        text = "Àdánwò Sáyẹ́ǹsì (Hands-On Activity)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ScienceCoral
                                    )
                                    BilingualText(
                                        yorubaText = exp.titleYo,
                                        englishText = exp.titleEn,
                                        languageMode = currentLanguageMode,
                                        yorubaStyle = MaterialTheme.typography.titleMedium,
                                        englishStyle = MaterialTheme.typography.titleSmall,
                                        showBadges = false
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Àwọn Ohun Èlò (Materials Needed):",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            exp.materialsYo.forEachIndexed { i, matYo ->
                                val matEn = exp.materialsEn.getOrNull(i) ?: ""
                                Text("• $matYo ($matEn)", fontSize = 12.sp, color = TextSecondary)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Àwọn Ìgbésẹ̀ (Step-by-Step):",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            exp.stepsYo.forEachIndexed { i, stepYo ->
                                val stepEn = exp.stepsEn.getOrNull(i) ?: ""
                                Text("${i + 1}. $stepYo\n   ($stepEn)", fontSize = 12.sp, color = TextSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }
            }

            // Post-Lesson Multiple Choice Quiz Component
            item {
                Spacer(modifier = Modifier.height(16.dp))
                PostLessonQuizComponent(
                    quiz = lesson.quiz,
                    currentLanguageMode = currentLanguageMode,
                    onQuizCompleted = onQuizCompleted,
                    onSpeakText = onSpeakText,
                    onOpenFullscreenQuiz = onStartQuiz,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
