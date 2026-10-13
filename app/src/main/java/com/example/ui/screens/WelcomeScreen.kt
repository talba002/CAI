package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.LanguageMode
import com.example.ui.theme.*

@Composable
fun WelcomeScreen(
    currentLanguage: LanguageMode,
    onSelectLanguage: (LanguageMode) -> Unit,
    onEnterAsPupil: (String, Int) -> Unit,
    onEnterAsTeacher: () -> Unit
) {
    var learnerName by remember { mutableStateOf("Abbas Talba") }
    var selectedGrade by remember { mutableStateOf(1) }
    var showPupilSetupDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WebForestGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Top Bar matching Index page.png
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.web_logo),
                        contentDescription = "Ìmọ̀ Science Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Text(
                        text = "Ìmọ̀ Science",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }

                // Wọlé / Login Button
                FilledTonalButton(
                    onClick = onEnterAsTeacher,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = WebForestGreenDark,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_top_login")
                ) {
                    Text("Wọlé / Login ➔", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Pill tag: • Interactive • Bilingual • Fun
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.2f),
                modifier = Modifier.testTag("badge_interactive_tag")
            ) {
                Text(
                    text = "• Interactive • Bilingual • Fun",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Centered Large Logo Badge Card
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .size(136.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.web_logo),
                    contentDescription = "Ìmọ̀ Science Logo Emblem",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Headline Title
            Text(
                text = "Ìmọ̀ Science",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = 38.sp
                ),
                textAlign = TextAlign.Center
            )

            // Subtitle
            Text(
                text = "Yorùbá Basic Science CAI",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WebGoldenAmber
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descriptive paragraph from Index page.png
            Text(
                text = "An educational platform helping Nigerian primary school pupils learn Basic Science through the Yorùbá language — with lessons, quizzes, and games for every class level.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.85f),
                    lineHeight = 22.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Golden CTA Button: Bẹ̀rẹ̀ Kíkọ́ (Start Learning) ➔
            Button(
                onClick = {
                    onEnterAsPupil(learnerName, selectedGrade)
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(54.dp)
                    .testTag("btn_start_learning_hero"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WebGoldenAmber,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Bẹ̀rẹ̀ Kíkọ́ (Start Learning)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick profile customization trigger
            TextButton(
                onClick = { showPupilSetupDialog = true },
                modifier = Modifier.testTag("btn_custom_pupil_setup")
            ) {
                Icon(Icons.Default.Tune, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Ṣe Àtúnṣe Akẹ́kọ̀ọ́ ($learnerName • P$selectedGrade)",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // Learner setup dialog
        if (showPupilSetupDialog) {
            AlertDialog(
                onDismissRequest = { showPupilSetupDialog = false },
                title = { Text("Wọle Bí Akẹ́kọ̀ọ́ (Learner Setup)", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = learnerName,
                            onValueChange = { learnerName = it },
                            label = { Text("Orúkọ (Name)") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text("Kíláàsì (Class):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            (1..6).forEach { g ->
                                val isSel = selectedGrade == g
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) WebForestGreen else WebForestGreenLight)
                                        .clickable { selectedGrade = g },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("P$g", fontWeight = FontWeight.Bold, color = if (isSel) Color.White else WebForestGreen)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showPupilSetupDialog = false
                            onEnterAsPupil(learnerName, selectedGrade)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WebForestGreen)
                    ) {
                        Text("Bẹ̀rẹ̀ (Start)")
                    }
                }
            )
        }
    }
}
