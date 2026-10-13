package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuizSubmission
import com.example.ui.theme.*

@Composable
fun QuizResultsScreen(
    submission: QuizSubmission,
    onRetakeQuiz: () -> Unit,
    onReturnHome: () -> Unit
) {
    BackHandler { onReturnHome() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Star Trophy Icon
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(if (submission.passed) ScienceYellowLight else ScienceCoralLight),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (submission.percentage == 100) "🏆" else if (submission.passed) "⭐" else "💪",
                fontSize = 54.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Yoruba Praise
        Text(
            text = if (submission.percentage == 100) {
                "O Kú Oríire! Ẹ Kú Iṣẹ́!"
            } else if (submission.passed) {
                "O Ṣe Dáadáa Gan-an!"
            } else {
                "Gbìyànjú Lẹ́ẹ̀kan Si!"
            },
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = if (submission.passed) ScienceGreen else ScienceCoral
            ),
            textAlign = TextAlign.Center
        )

        Text(
            text = if (submission.passed) "Superstar Scientist! Assessment Passed" else "Keep practicing to master this topic!",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Score Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${submission.score} / ${submission.totalQuestions}",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${submission.percentage}% Score",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (submission.passed) ScienceGreen else ScienceCoral
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⭐ +${if (submission.percentage == 100) 5 else if (submission.passed) 3 else 1}", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = StarGold)
                        Text("Stars Earned", fontSize = 11.sp, color = TextMuted)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(if (submission.passed) "Kọjá (Pass)" else "Ṣe Àtúnkọ", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if (submission.passed) ScienceGreen else ScienceCoral)
                        Text("Result", fontSize = 11.sp, color = TextMuted)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Sync & Offline message
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = ScienceBlueLight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CloudQueue,
                    contentDescription = null,
                    tint = ScienceBlue,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Saved locally. Queued for Base44 cloud sync.",
                    fontSize = 12.sp,
                    color = ScienceBlue,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Action Buttons
        Button(
            onClick = onReturnHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("btn_return_dashboard"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ScienceBlue)
        ) {
            Icon(Icons.Default.Home, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Padà sí Ibùdó (Dashboard)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onRetakeQuiz,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_retake_quiz"),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Ṣe Àtúnyẹ̀wò (Retake Quiz)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}
