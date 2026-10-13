package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.data.model.LanguageMode
import com.example.data.model.Quiz
import com.example.ui.components.BilingualText
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveQuizScreen(
    quiz: Quiz,
    questionIndex: Int,
    selectedOption: Int?,
    hasAnswered: Boolean,
    currentLanguageMode: LanguageMode,
    isSpeaking: Boolean,
    onSelectOption: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    onSpeakQuestion: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val currentQuestion = quiz.questions.getOrNull(questionIndex) ?: return
    val totalQuestions = quiz.questions.size
    val progress = (questionIndex + 1).toFloat() / totalQuestions
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = quiz.titleYo,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Ìbéèrè ${questionIndex + 1} nínú $totalQuestions (Question ${questionIndex + 1}/$totalQuestions)",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_quiz")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onSpeakQuestion,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(if (isSpeaking) ScienceYellowLight else ScienceBlueLight, CircleShape)
                            .testTag("btn_speak_quiz_question")
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Filled.Stop else Icons.Filled.VolumeUp,
                            contentDescription = "Speak Question",
                            tint = if (isSpeaking) ScienceYellow else ScienceBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            if (hasAnswered) {
                Surface(
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .navigationBarsPadding()
                    ) {
                        Button(
                            onClick = onNextQuestion,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ScienceBlue),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_next_question")
                        ) {
                            Text(
                                text = if (questionIndex == totalQuestions - 1)
                                    "Parí Ìdánwò (Complete Quiz 🏆)"
                                else
                                    "Ìbéèrè Tó Kàn (Next Question ➔)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Linear Progress Indicator
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = ScienceGreen,
                trackColor = ScienceGreenLight
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Question Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(currentQuestion.iconEmoji, fontSize = 28.sp)
                        Surface(
                            color = ScienceBlueLight,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Ìbéèrè ${questionIndex + 1}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ScienceBlue,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    BilingualText(
                        yorubaText = currentQuestion.promptYo,
                        englishText = currentQuestion.promptEn,
                        languageMode = currentLanguageMode,
                        yorubaStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        englishStyle = MaterialTheme.typography.bodyMedium,
                        showBadges = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Options
            val optionLetters = listOf("A", "B", "C", "D")
            currentQuestion.optionsYo.forEachIndexed { index, optionYo ->
                val optionEn = currentQuestion.optionsEn.getOrNull(index) ?: ""
                val isSelected = selectedOption == index
                val isCorrect = index == currentQuestion.correctIndex

                val containerColor = when {
                    !hasAnswered -> if (isSelected) ScienceBlueLight else MaterialTheme.colorScheme.surface
                    isCorrect -> ScienceGreenLight
                    isSelected && !isCorrect -> ScienceCoralLight
                    else -> MaterialTheme.colorScheme.surface
                }

                val borderColor = when {
                    !hasAnswered -> if (isSelected) ScienceBlue else BorderSoft
                    isCorrect -> ScienceGreen
                    isSelected && !isCorrect -> WrongRed
                    else -> BorderSoft
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable(enabled = !hasAnswered) {
                            onSelectOption(index)
                        }
                        .testTag("option_${optionLetters.getOrElse(index) { "X" }}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = containerColor),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(borderColor)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        !hasAnswered -> if (isSelected) ScienceBlue else ScienceBlueLight
                                        isCorrect -> ScienceGreen
                                        isSelected && !isCorrect -> WrongRed
                                        else -> ScienceBlueLight
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = optionLetters.getOrElse(index) { "$index" },
                                fontWeight = FontWeight.Bold,
                                color = if ((isSelected && !hasAnswered) || (hasAnswered && (isCorrect || isSelected))) Color.White else ScienceBlue
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            when (currentLanguageMode) {
                                LanguageMode.YORUBA -> {
                                    Text(
                                        text = optionYo,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                                LanguageMode.ENGLISH -> {
                                    Text(
                                        text = optionEn,
                                        fontSize = 14.sp
                                    )
                                }
                                LanguageMode.BILINGUAL -> {
                                    Text(
                                        text = optionYo,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = optionEn,
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }

                        if (hasAnswered) {
                            if (isCorrect) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Correct",
                                    tint = ScienceGreen
                                )
                            } else if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Cancel,
                                    contentDescription = "Wrong",
                                    tint = WrongRed
                                )
                            }
                        }
                    }
                }
            }

            // Explanation & Praise box after answer
            AnimatedVisibility(visible = hasAnswered) {
                val answeredCorrectly = selectedOption == currentQuestion.correctIndex
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (answeredCorrectly) ScienceGreenLight else ScienceYellowLight
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(if (answeredCorrectly) "🎉" else "💡", fontSize = 20.sp)
                            Text(
                                text = if (answeredCorrectly) "O kú iṣẹ́! (Great job!)" else "Àkíyèsí (Explanation):",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = if (answeredCorrectly) ScienceGreen else Color(0xFFE65100)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        BilingualText(
                            yorubaText = currentQuestion.explanationYo,
                            englishText = currentQuestion.explanationEn,
                            languageMode = currentLanguageMode,
                            yorubaStyle = MaterialTheme.typography.bodySmall,
                            englishStyle = MaterialTheme.typography.bodySmall,
                            showBadges = false
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
