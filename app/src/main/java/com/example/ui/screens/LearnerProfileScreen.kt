package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import com.example.data.model.LearnerProfile
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnerProfileScreen(
    profile: LearnerProfile,
    currentLanguageMode: LanguageMode,
    pendingSyncCount: Int,
    isSyncing: Boolean,
    syncMessage: String?,
    onSaveProfile: (String, String, Int, Float) -> Unit,
    onLanguageChange: (LanguageMode) -> Unit,
    onTriggerSync: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var name by remember { mutableStateOf(profile.name) }
    var selectedAvatar by remember { mutableStateOf(profile.avatarEmoji) }
    var selectedGrade by remember { mutableStateOf(profile.gradeLevel) }
    var speechSpeed by remember { mutableStateOf(profile.audioSpeed) }
    val avatars = listOf("🚀", "🦁", "🌻", "🔬", "⭐", "🦋", "🌍")
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Àkọọ́lẹ̀ Akẹ́kọ̀ọ́ (Profile & Settings)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Manage your preferences and speech audio",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_profile")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            onSaveProfile(name, selectedAvatar, selectedGrade, speechSpeed)
                            onBack()
                        },
                        modifier = Modifier.testTag("btn_save_profile")
                    ) {
                        Text("Fipamọ́ (Save)", fontWeight = FontWeight.Bold, color = ScienceBlue)
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
            // Avatar Selector Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(ScienceBlueLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(selectedAvatar, fontSize = 38.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Yan Àwòrán Titun (Select Avatar):", fontSize = 13.sp, fontWeight = FontWeight.Medium)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        avatars.forEach { avatar ->
                            val isSelected = selectedAvatar == avatar
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) ScienceYellowLight else Color.Transparent)
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) ScienceYellow else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedAvatar = avatar },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(avatar, fontSize = 20.sp)
                            }
                        }
                    }
                }
            }

            // Learner Details
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Orúkọ Akẹ́kọ̀ọ́ (Pupil Name):", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_edit_name"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Kíláàsì (Primary Level):", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        (1..6).forEach { grade ->
                            val isSelected = selectedGrade == grade
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) ScienceGreen else ScienceGreenLight)
                                    .clickable { selectedGrade = grade },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "P$grade",
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else ScienceGreen
                                )
                            }
                        }
                    }
                }
            }

            // Audio & Language Settings
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Èdè Ìkọ́nilẹ́kọ̀ọ́ (Instruction Language):", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    LanguageMode.values().forEach { mode ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onLanguageChange(mode) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = currentLanguageMode == mode,
                                onClick = { onLanguageChange(mode) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(mode.displayName, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Ìyára Ọ̀rọ̀ Sísọ (Audio Speech Speed: ${String.format("%.1f", speechSpeed)}x):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Slider(
                        value = speechSpeed,
                        onValueChange = { speechSpeed = it },
                        valueRange = 0.7f..1.2f,
                        steps = 4,
                        modifier = Modifier.testTag("slider_speech_speed")
                    )
                }
            }

            // Offline Cache & Base44 Cloud Sync
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CloudSync, contentDescription = null, tint = ScienceBlue)
                        Text(
                            text = "Offline Cache & Base44 Sync",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "All lessons and assessments are cached offline in Room SQLite database. Pending records synchronize with Base44 when online.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pending uploads: $pendingSyncCount",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (pendingSyncCount > 0) Color(0xFFE65100) else ScienceGreen
                        )

                        Button(
                            onClick = onTriggerSync,
                            enabled = !isSyncing,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ScienceBlue),
                            modifier = Modifier.testTag("btn_profile_sync_now")
                        ) {
                            if (isSyncing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Syncing...")
                            } else {
                                Text("Sync Now (Ṣe Ìṣọ̀kan)")
                            }
                        }
                    }

                    if (syncMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = syncMessage,
                            fontSize = 11.sp,
                            color = ScienceGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
