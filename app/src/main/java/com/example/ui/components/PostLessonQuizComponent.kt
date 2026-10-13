package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.data.model.QuizQuestion
import com.example.ui.theme.*
import com.example.util.SoundPlayer

/**
 * Multiple-choice Quiz UI Component displayed directly after a pupil finishes reading a science lesson.
 * Supports inline question answering, instant Yorùbá feedback, audio pronunciation, and Room database progress tracking.
 */
@Composable
fun PostLessonQuizComponent(
    quiz: Quiz,
    currentLanguageMode: LanguageMode,
    onQuizCompleted: (score: Int, total: Int) -> Unit,
    onSpeakText: (String, Boolean) -> Unit,
    onOpenFullscreenQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentQuestionIndex by remember(quiz.id) { mutableIntStateOf(0) }
    var selectedOptionIndex by remember(quiz.id, currentQuestionIndex) { mutableStateOf<Int?>(null) }
    var hasAnswered by remember(quiz.id, currentQuestionIndex) { mutableStateOf(false) }
    var correctCount by remember(quiz.id) { mutableIntStateOf(0) }
    var isQuizCompleted by remember(quiz.id) { mutableStateOf(false) }

    val currentQuestion: QuizQuestion? = quiz.questions.getOrNull(currentQuestionIndex)
    val totalQuestions = quiz.questions.size

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("post_lesson_quiz_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(ScienceGreenLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎯", fontSize = 22.sp)
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Ìdánwò Lẹ́yìn Ẹ̀kọ́",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = ScienceGreenLight
                            ) {
                                Text(
                                    text = "Multiple Choice",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ScienceGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Test your knowledge on this lesson",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }

                // Fullscreen shortcut button
                IconButton(
                    onClick = onOpenFullscreenQuiz,
                    modifier = Modifier
                        .size(36.dp)
                        .background(ScienceBlueLight, CircleShape)
                        .testTag("btn_expand_fullscreen_quiz")
                ) {
                    Icon(
                        imageVector = Icons.Default.Fullscreen,
                        contentDescription = "Open Fullscreen Quiz",
                        tint = ScienceBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isQuizCompleted && currentQuestion != null) {
                // Progress Bar & Question Counter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Ìbéèrè ${currentQuestionIndex + 1} nínú $totalQuestions (Q${currentQuestionIndex + 1}/$totalQuestions)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ScienceBlue
                    )
                    Text(
                        text = "Àmì: $correctCount (Score)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ScienceGreen
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { (currentQuestionIndex + 1).toFloat() / totalQuestions },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = ScienceGreen,
                    trackColor = ScienceGreenLight,
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Question Prompt Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BackgroundLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            if (currentLanguageMode != LanguageMode.ENGLISH) {
                                Text(
                                    text = currentQuestion.promptYo,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 22.sp
                                )
                            }
                            if (currentLanguageMode != LanguageMode.YORUBA) {
                                if (currentLanguageMode == LanguageMode.BILINGUAL) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                                Text(
                                    text = currentQuestion.promptEn,
                                    fontSize = 13.sp,
                                    color = TextMuted,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        // Audio speak prompt button
                        IconButton(
                            onClick = {
                                val text = when (currentLanguageMode) {
                                    LanguageMode.YORUBA -> currentQuestion.promptYo
                                    LanguageMode.ENGLISH -> currentQuestion.promptEn
                                    LanguageMode.BILINGUAL -> "${currentQuestion.promptYo}. ${currentQuestion.promptEn}"
                                }
                                onSpeakText(text, currentLanguageMode == LanguageMode.YORUBA)
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .background(ScienceYellowLight, CircleShape)
                                .testTag("btn_speak_inline_question")
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Read Question",
                                tint = WebGoldenAmberDark,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Multiple-Choice Options
                val optionsYo = currentQuestion.optionsYo
                val optionsEn = currentQuestion.optionsEn
                val optionCount = maxOf(optionsYo.size, optionsEn.size)

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    for (i in 0 until optionCount) {
                        val yoOption = optionsYo.getOrNull(i) ?: ""
                        val enOption = optionsEn.getOrNull(i) ?: ""
                        val isSelected = selectedOptionIndex == i
                        val isCorrectOption = i == currentQuestion.correctIndex

                        val containerColor = when {
                            !hasAnswered -> if (isSelected) ScienceBlueLight else MaterialTheme.colorScheme.surface
                            isCorrectOption -> Color(0xFFE8F5E9) // Light Green
                            isSelected -> Color(0xFFFFEBEE) // Light Red
                            else -> MaterialTheme.colorScheme.surface
                        }

                        val borderColor = when {
                            !hasAnswered -> if (isSelected) ScienceBlue else BorderSoft
                            isCorrectOption -> ScienceGreen
                            isSelected -> ScienceCoral
                            else -> BorderSoft
                        }

                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = containerColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
                                .clickable(enabled = !hasAnswered) {
                                    selectedOptionIndex = i
                                    hasAnswered = true
                                    if (i == currentQuestion.correctIndex) {
                                        correctCount += 1
                                        SoundPlayer.playCorrectChime()
                                    } else {
                                        SoundPlayer.playIncorrectTone()
                                    }
                                }
                                .testTag("mcq_option_$i")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    // Option Label letter (A, B, C, D)
                                    val letter = ('A' + i).toString()
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .background(
                                                color = when {
                                                    hasAnswered && isCorrectOption -> ScienceGreen
                                                    hasAnswered && isSelected -> ScienceCoral
                                                    isSelected -> ScienceBlue
                                                    else -> BackgroundLight
                                                },
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = letter,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = if (isSelected || (hasAnswered && isCorrectOption)) Color.White else TextPrimary
                                        )
                                    }

                                    Column {
                                        if (currentLanguageMode != LanguageMode.ENGLISH && yoOption.isNotBlank()) {
                                            Text(
                                                text = yoOption,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        if (currentLanguageMode != LanguageMode.YORUBA && enOption.isNotBlank()) {
                                            Text(
                                                text = enOption,
                                                fontSize = 12.sp,
                                                color = TextMuted
                                            )
                                        }
                                    }
                                }

                                // Status Icon
                                if (hasAnswered) {
                                    if (isCorrectOption) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Correct",
                                            tint = ScienceGreen,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    } else if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Cancel,
                                            contentDescription = "Incorrect",
                                            tint = ScienceCoral,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Feedback & Explanation Banner
                AnimatedVisibility(visible = hasAnswered) {
                    Column(modifier = Modifier.padding(top = 14.dp)) {
                        val isCorrect = selectedOptionIndex == currentQuestion.correctIndex
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCorrect) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = if (isCorrect) "🎉 Òótọ́! (Correct!)" else "❌ Àṣìṣe díẹ̀ (Not quite!)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isCorrect) Color(0xFF2E7D32) else Color(0xFFE65100)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (currentLanguageMode == LanguageMode.ENGLISH) {
                                        currentQuestion.explanationEn
                                    } else {
                                        "${currentQuestion.explanationYo} (${currentQuestion.explanationEn})"
                                    },
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    lineHeight = 17.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Next / Finish Button
                        Button(
                            onClick = {
                                if (currentQuestionIndex < totalQuestions - 1) {
                                    currentQuestionIndex += 1
                                    selectedOptionIndex = null
                                    hasAnswered = false
                                } else {
                                    isQuizCompleted = true
                                    SoundPlayer.playStarEarned()
                                    onQuizCompleted(correctCount, totalQuestions)
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ScienceBlue),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_next_inline_quiz")
                        ) {
                            Text(
                                text = if (currentQuestionIndex < totalQuestions - 1) {
                                    "Ìbéèrè Tókàn (Next Question) ➡️"
                                } else {
                                    "Parí Ìdánwò (Complete Quiz) 🏁"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            } else {
                // Quiz Completed Summary Card
                val percentage = if (totalQuestions > 0) (correctCount * 100) / totalQuestions else 0
                val passed = percentage >= 60

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (passed) "🌟 Ẹ Kú Iṣẹ́! 🌟" else "💪 Má Ṣe Rẹ̀wẹ̀sì!",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = if (passed) ScienceGreen else ScienceCoral
                    )

                    Text(
                        text = if (passed) "O ti parí ìdánwò lẹ́yìn ẹ̀kọ́ yìí!" else "Gbiyanju lẹ́ẹ̀kan síi láti kọ́ dáadáa!",
                        fontSize = 13.sp,
                        color = TextMuted
                    )

                    // Score Display
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (passed) ScienceGreenLight else ScienceYellowLight,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Àmì Rẹ (Your Score):",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "$correctCount / $totalQuestions ($percentage%)",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = if (passed) ScienceGreen else Color(0xFFE65100)
                            )
                        }
                    }

                    // Room offline persistence badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ScienceBlueLight
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.CloudDone, contentDescription = null, tint = ScienceBlue, modifier = Modifier.size(16.dp))
                            Text(
                                text = "Ti fipamọ́ sínú Room Database (Saved Offline)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = ScienceBlue
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                currentQuestionIndex = 0
                                selectedOptionIndex = null
                                hasAnswered = false
                                correctCount = 0
                                isQuizCompleted = false
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_retake_inline_quiz")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Tun Ṣe (Retake)", fontSize = 13.sp)
                        }

                        Button(
                            onClick = onOpenFullscreenQuiz,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ScienceGreen),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_open_full_quiz_from_summary")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ìdánwò Kikún", fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
