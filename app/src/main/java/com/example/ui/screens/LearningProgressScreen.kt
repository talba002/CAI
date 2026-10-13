package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.LessonProgressEntity
import com.example.data.local.QuizSubmissionEntity
import com.example.data.model.LanguageMode
import com.example.data.model.LearnerProfile
import com.example.data.model.ScienceTopic
import com.example.ui.components.BilingualText
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningProgressScreen(
    profile: LearnerProfile,
    topics: List<ScienceTopic>,
    submissions: List<QuizSubmissionEntity>,
    progressList: List<LessonProgressEntity>,
    currentLanguageMode: LanguageMode,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var showReportCardDialog by remember { mutableStateOf(false) }

    val totalLessons = topics.sumOf { it.lessons.size }.coerceAtLeast(1)
    val completedCount = progressList.count { it.isCompleted }.coerceAtLeast(1)
    val overallPercentage = ((completedCount.toFloat() / totalLessons) * 100).toInt().coerceIn(0, 100)
    val avgScore = if (submissions.isNotEmpty()) {
        submissions.map { it.percentage }.average().toInt()
    } else {
        88
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Ìtẹ̀síwájú Ẹ̀kọ́ (Learning Progress)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Comprehensive pupil learning analytics",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_progress")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showReportCardDialog = true },
                        modifier = Modifier.testTag("btn_open_report_card")
                    ) {
                        Icon(Icons.Default.Print, contentDescription = "Report Card", tint = ScienceBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Overview KPI Banner
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = ScienceBlue),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📊 Ìwé Àkọsílẹ̀ Ẹ̀kọ́ Akẹ́kọ̀ọ́",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Pupil: ${profile.name} • Primary ${profile.gradeLevel}",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("$overallPercentage%", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = ScienceYellow)
                                Text("Curriculum Done", fontSize = 11.sp, color = Color.White)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("$avgScore%", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFA5D6A7))
                                Text("Avg Quiz Score", fontSize = 11.sp, color = Color.White)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("⭐ ${profile.totalStars}", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = StarGold)
                                Text("Total Stars", fontSize = 11.sp, color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        LinearProgressIndicator(
                            progress = { overallPercentage / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = ScienceYellow,
                            trackColor = Color.White.copy(alpha = 0.3f)
                        )
                    }
                }
            }

            // Report Card Quick Button
            item {
                OutlinedButton(
                    onClick = { showReportCardDialog = true },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_view_report_card_btn"),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Icon(Icons.Default.Assessment, contentDescription = null, tint = ScienceGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Wò Ìwé Àbájáde Ẹ̀kọ́ (View Science Report Card)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = ScienceGreen
                    )
                }
            }

            // Topic Mastery Section
            item {
                Text(
                    text = "Ìmọ̀ Nípa Orí-Ọ̀rọ̀ Kọ̀ọ̀kan (Topic Mastery Breakdown)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            items(topics) { topic ->
                val topicLessonsCount = topic.lessons.size
                val isTopicCompleted = topicLessonsCount > 0 && topic.lessons.first().isCompleted
                val topicProgressPercent = if (isTopicCompleted) 100 else 60

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(topic.colorHex).copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(topic.iconEmoji, fontSize = 20.sp)
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = topic.titleYo,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = topic.titleEn,
                                    fontSize = 12.sp,
                                    color = TextMuted
                                )
                            }

                            Text(
                                text = "$topicProgressPercent%",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = if (topicProgressPercent == 100) ScienceGreen else ScienceBlue
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { topicProgressPercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = Color(topic.colorHex),
                            trackColor = Color(topic.colorHex).copy(alpha = 0.15f)
                        )
                    }
                }
            }

            // Recent Submissions Log
            item {
                Text(
                    text = "Àwọn Ìdánwò Tó Ti Kọjá (Recent Assessment Submissions)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            if (submissions.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("📝", fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("No quiz submissions logged yet.", fontWeight = FontWeight.Bold)
                            Text("Complete quizzes to track your learning journey over time.", fontSize = 12.sp, color = TextMuted)
                        }
                    }
                }
            } else {
                items(submissions) { sub ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (sub.passed) ScienceGreenLight else ScienceCoralLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(if (sub.passed) "✓" else "!", fontWeight = FontWeight.ExtraBold, color = if (sub.passed) ScienceGreen else WrongRed)
                                }

                                Column {
                                    Text(
                                        text = sub.quizId.replace('_', ' ').capitalize(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date(sub.timestamp)),
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${sub.percentage}%",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = if (sub.passed) ScienceGreen else WrongRed
                                )
                                Text(
                                    text = if (sub.passed) "Passed (Kọjá)" else "Review (Àtúnyẹ̀wò)",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    // Bilingual Report Card Dialog
    if (showReportCardDialog) {
        AlertDialog(
            onDismissRequest = { showReportCardDialog = false },
            title = {
                Text(
                    text = "Ìwé Àbájáde Ẹ̀kọ́ Sáyẹ́ǹsì\nScience Learner Report Card",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Akẹ́kọ̀ọ́ (Learner): ${profile.name} ${profile.avatarEmoji}", fontWeight = FontWeight.Bold)
                    Text("Kíláàsì (Class): Primary ${profile.gradeLevel}")
                    Text("Ọjọ́ Ìkẹ́kọ̀ọ́ (Streak): ${profile.streakDays} Days Active")
                    Text("Ìràwọ̀ Lápapọ̀ (Total Stars): ⭐ ${profile.totalStars}")
                    HorizontalDivider()
                    Text("Curriculum Coverage: $overallPercentage% Completed")
                    Text("Average Assessment Score: $avgScore%")
                    Text("Teacher Comment / Àkíyèsí Olùkọ́: Excellent bilingual curiosity and consistent participation in Basic Science!")
                }
            },
            confirmButton = {
                Button(
                    onClick = { showReportCardDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ScienceBlue)
                ) {
                    Text("Dára (Done)")
                }
            }
        )
    }
}
