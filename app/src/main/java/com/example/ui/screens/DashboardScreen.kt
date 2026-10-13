package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.BadgeEntity
import com.example.data.local.LessonProgressEntity
import com.example.data.model.LanguageMode
import com.example.data.model.LearnerProfile
import com.example.data.model.ScienceTopic
import com.example.ui.components.BilingualText
import com.example.ui.components.ScienceTopBar
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    profile: LearnerProfile,
    topics: List<ScienceTopic>,
    currentLanguageMode: LanguageMode,
    selectedGrade: Int?,
    onSelectGrade: (Int?) -> Unit,
    onTopicClick: (ScienceTopic) -> Unit,
    onContinueLesson: (String) -> Unit,
    onLanguageChange: (LanguageMode) -> Unit,
    onProfileClick: () -> Unit,
    onTeacherClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onSpeakWord: (String, Boolean) -> Unit,
    pendingSyncCount: Int = 0,
    onTriggerSync: () -> Unit,
    onSimulationClick: () -> Unit,
    onGamesClick: () -> Unit,
    onProgressClick: () -> Unit,
    onParticipationRoomClick: () -> Unit = {},
    badges: List<BadgeEntity> = emptyList(),
    celebrationBadge: BadgeEntity? = null,
    onDismissCelebrationBadge: () -> Unit = {},
    progressList: List<LessonProgressEntity> = emptyList()
) {
    val scrollState = rememberScrollState()
    var selectedDetailBadge by remember { mutableStateOf<BadgeEntity?>(null) }

    Scaffold(
        topBar = {
            ScienceTopBar(
                learnerName = profile.name,
                avatarEmoji = profile.avatarEmoji,
                totalStars = profile.totalStars,
                currentLanguageMode = currentLanguageMode,
                onLanguageChange = onLanguageChange,
                onProfileClick = onProfileClick,
                onTeacherClick = onTeacherClick,
                pendingSyncCount = pendingSyncCount
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WebBackground)
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Offline sync banner if pending
            if (pendingSyncCount > 0) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { onTriggerSync() }
                        .testTag("banner_offline_sync"),
                    colors = CardDefaults.cardColors(containerColor = WebGoldenAmberLight),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.CloudSync, contentDescription = null, tint = WebGoldenAmberDark)
                            Column {
                                Text(
                                    text = "Pending Offline Records: $pendingSyncCount",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = WebGoldenAmberDark
                                )
                                Text(
                                    text = "Fọwọ́bà láti sopọ̀ mọ́ Base44 (Tap to sync now)",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                        Button(
                            onClick = onTriggerSync,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WebGoldenAmber),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("Sync", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Green Hero Card matching Home page.png exactly
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = WebForestGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_home_hero")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // Badge: Logo + Yorùbá CAI • Interactive Learning
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.18f),
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.web_logo),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )
                            Text(
                                text = "Ìmọ̀ Science • Interactive Learning",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Welcome Headline
                    Text(
                        text = "Ẹ kú àbọ̀, ${profile.name}! 👋",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Ẹ káàbọ̀ sí ètò ẹ̀kọ́ Sáyẹ́ǹsì ní èdè Yorùbá. Ẹ bẹ̀rẹ̀ kíkọ́ lónìí!",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Three Action Buttons matching Home page.png
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 1. Start Learning (Golden Amber)
                        Button(
                            onClick = { onContinueLesson("lesson_living_char") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WebGoldenAmber,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier
                                .weight(1.2f)
                                .testTag("btn_start_learning_action")
                        ) {
                            Text("📖 Bẹ̀rẹ̀ Ẹ̀kọ́", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        // 2. Take Quiz (Tonal Green)
                        Button(
                            onClick = {
                                val firstTopic = topics.firstOrNull()
                                val firstLesson = firstTopic?.lessons?.firstOrNull()
                                if (firstLesson != null) {
                                    onContinueLesson(firstLesson.id)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.22f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_take_quiz_hero")
                        ) {
                            Text("🏆 Ṣe Ìdánwò", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        // 3. Download Report (Tonal Green)
                        Button(
                            onClick = onProgressClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.22f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_download_report_hero")
                        ) {
                            Text("📥 Ìpín", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Four Summary Metric Cards from Home page.png
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Card 1: 28 Lessons
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📖", fontSize = 20.sp)
                        Text(
                            text = "28",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = TextPrimary
                        )
                        Text("Ẹ̀kọ́", fontSize = 10.sp, color = TextMuted)
                    }
                }

                // Card 2: 0 Completed
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("⭐", fontSize = 20.sp)
                        Text(
                            text = "${profile.completedLessonsCount}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = TextPrimary
                        )
                        Text("Ti Parí", fontSize = 10.sp, color = TextMuted)
                    }
                }

                // Card 3: 0 Quizzes
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🏆", fontSize = 20.sp)
                        Text(
                            text = "1",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = TextPrimary
                        )
                        Text("Ìdánwò", fontSize = 10.sp, color = TextMuted)
                    }
                }

                // Card 4: Avg Score
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎮", fontSize = 20.sp)
                        Text(
                            text = "88%",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = TextPrimary
                        )
                        Text("Àpapọ̀", fontSize = 10.sp, color = TextMuted)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Science Hub: Virtual Lab Simulation, Games, Progress & Admin Upload
            Text(
                text = "Àwọn Ìgbòkègbodò Sáyẹ́ǹsì (Science Hub)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = WebForestGreenDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Simulation Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSimulationClick() }
                        .testTag("hub_btn_simulation"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = WebForestGreenLight),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🌱", fontSize = 22.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Yàrá Àyẹ̀wò", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = WebForestGreen)
                        Text("Virtual Lab", fontSize = 9.sp, color = TextMuted)
                    }
                }

                // Games Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onGamesClick() }
                        .testTag("hub_btn_games"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = WebGoldenAmberLight),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎮", fontSize = 22.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Eré Sáyẹ́ǹsì", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = WebGoldenAmberDark)
                        Text("Games", fontSize = 9.sp, color = TextMuted)
                    }
                }

                // Learning Progress Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onProgressClick() }
                        .testTag("hub_btn_progress"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📊", fontSize = 22.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Àtẹ̀jáde", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = TextPrimary)
                        Text("Progress", fontSize = 9.sp, color = TextMuted)
                    }
                }

                // Admin Upload Studio Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTeacherClick() }
                        .testTag("hub_btn_admin"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("👑", fontSize = 22.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Olùkọ́", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = WebForestGreen)
                        Text("Admin", fontSize = 9.sp, color = TextMuted)
                    }
                }
            }

            // Live Classroom Participation Room Banner for Students
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SciencePurpleLight),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, SciencePurple),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onParticipationRoomClick() }
                    .testTag("banner_pupil_live_room")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(SciencePurple),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👥", fontSize = 20.sp)
                        }
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Yàrá Ìkópa Pẹ̀lú Olùkọ́",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = SciencePurple
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = WrongRed
                                ) {
                                    Text("LIVE", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                }
                            }
                            Text(
                                text = "Code: IMO-101 • Olùkọ́ Adéyẹmí ń kọ́ni lọ́wọ́!",
                                fontSize = 11.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "Raise hands ✋ & answer live questions together",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Button(
                        onClick = onParticipationRoomClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SciencePurple),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("btn_join_live_room")
                    ) {
                        Text("Wọlé (Join)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Quick Interactive Simulation Preview Chips (Solar System 🪐 & Plant Growth 🌱)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🔬 Àwọn Àfarawé Aláṣẹkẹ́kọ̀ọ́ (Room Simulations):",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = WebForestGreenDark
                        )
                        Text(
                            text = "Tap to open",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Solar System Pill
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF0F172A),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSimulationClick() }
                                .testTag("quick_sim_solar")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("🪐", fontSize = 18.sp)
                                Column {
                                    Text("Ètò Oòrùn", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = ScienceYellow)
                                    Text("Solar System Lab", fontSize = 9.sp, color = Color.White.copy(alpha = 0.8f))
                                }
                            }
                        }

                        // Plant Growth Pill
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = WebForestGreenLight,
                            border = androidx.compose.foundation.BorderStroke(1.dp, WebBorder),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSimulationClick() }
                                .testTag("quick_sim_plant")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("🌱", fontSize = 18.sp)
                                Column {
                                    Text("Ìdàgbàsókè Ewéko", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = WebForestGreen)
                                    Text("Plant Photosynthesis", fontSize = 9.sp, color = TextMuted)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Badges & Achievements Section (Room-backed) ---
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("section_badges_showcase")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("🏅", fontSize = 22.sp)
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Àwọn Àmì Ẹ̀yẹ Rẹ",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = TextPrimary
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = ScienceYellowLight
                                    ) {
                                        Text(
                                            text = "${badges.count { it.isUnlocked }}/${badges.size} Gba",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFE65100),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Your Science Badges & Milestones",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        TextButton(
                            onClick = onAchievementsClick,
                            modifier = Modifier.testTag("btn_see_all_badges")
                        ) {
                            Text(
                                text = "Wo Gbogbo ➔",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = WebForestGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (badges.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Kò sí àmì ẹ̀yẹ kankan láti Room database",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(horizontal = 2.dp)
                        ) {
                            items(badges) { badge ->
                                val isUnlocked = badge.isUnlocked
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isUnlocked) Color(0xFFFFFBEB) else Color(0xFFF8FAFC)
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(
                                        width = if (isUnlocked) 1.5.dp else 1.dp,
                                        color = if (isUnlocked) Color(0xFFF59E0B) else Color(0xFFCBD5E1)
                                    ),
                                    modifier = Modifier
                                        .width(135.dp)
                                        .clickable { selectedDetailBadge = badge }
                                        .testTag("badge_card_${badge.id}")
                                ) {
                                    Column(
                                        modifier = Modifier.padding(10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(46.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (isUnlocked) Color(0xFFFEF3C7) else Color(0xFFE2E8F0)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = badge.iconEmoji,
                                                fontSize = 24.sp
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = badge.titleYo,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = if (isUnlocked) Color(0xFF92400E) else TextPrimary,
                                            maxLines = 1
                                        )

                                        Text(
                                            text = badge.titleEn,
                                            fontSize = 9.sp,
                                            color = TextMuted,
                                            maxLines = 1
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isUnlocked) Color(0xFFDCFCE7) else Color(0xFFF1F5F9)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                if (isUnlocked) {
                                                    Text("✨", fontSize = 9.sp)
                                                    Text(
                                                        text = "Gba!",
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF166534)
                                                    )
                                                } else {
                                                    Text("🔒", fontSize = 9.sp)
                                                    Text(
                                                        text = "${badge.progress}/${badge.targetCount}",
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = TextMuted
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lessons by Module Section matching Home page.png
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ẹ̀kọ́ (Lessons by Module)",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val allTotalUnits = topics.sumOf { it.lessons.size }
            val allCompletedUnits = topics.sumOf { t ->
                t.lessons.count { l ->
                    progressList.any { it.lessonId == l.id && it.isCompleted } || l.isCompleted
                }
            }
            val totalOverallPercent = if (allTotalUnits > 0) ((allCompletedUnits * 100) / allTotalUnits) else 0

            // Theme 1 Header
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WebForestGreenLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Theme 1: LEARNING ABOUT YOUR ENVIRONMENT",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = WebForestGreen
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.White
                    ) {
                        Text(
                            text = "$allCompletedUnits/$allTotalUnits ✓ ($totalOverallPercent%)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = WebForestGreen,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Modules / Topics list with visual progress tracking & progress bars
            topics.forEachIndexed { index, topic ->
                val totalUnits = topic.lessons.size
                val completedUnits = topic.lessons.count { lesson ->
                    progressList.any { it.lessonId == lesson.id && it.isCompleted } || lesson.isCompleted
                }
                val progressFraction = if (totalUnits > 0) completedUnits.toFloat() / totalUnits.toFloat() else 0f
                val progressPercent = (progressFraction * 100).toInt().coerceIn(0, 100)
                val isCompleted = progressPercent == 100
                val isInProgress = progressPercent > 0 && !isCompleted

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { onTopicClick(topic) }
                        .testTag("card_topic_${topic.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isCompleted) 1.5.dp else 1.dp,
                        color = if (isCompleted) ScienceGreen else if (isInProgress) Color(topic.colorHex).copy(alpha = 0.5f) else Color(0xFFE2E8F0)
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(topic.colorHex).copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(topic.iconEmoji, fontSize = 24.sp)
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Module ${index + 1}: ${topic.titleEn.uppercase()}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = TextPrimary
                                )

                                Text(
                                    text = "${topic.titleYo} • $completedUnits/$totalUnits units done",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }

                            // Completion Status Badge
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = when {
                                    isCompleted -> Color(0xFFDCFCE7)
                                    isInProgress -> Color(0xFFFEF3C7)
                                    else -> Color(0xFFF1F5F9)
                                }
                            ) {
                                Text(
                                    text = when {
                                        isCompleted -> "100% Parí ✓"
                                        isInProgress -> "$progressPercent%"
                                        else -> "0% Bẹ̀rẹ̀"
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when {
                                        isCompleted -> Color(0xFF166534)
                                        isInProgress -> Color(0xFFB45309)
                                        else -> TextMuted
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = 6.dp, vertical = 3.dp)
                                        .testTag("topic_progress_percent_${topic.id}")
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Visual Progress Bar for Individual Science Topic
                        LinearProgressIndicator(
                            progress = { progressFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .testTag("topic_progress_bar_${topic.id}"),
                            color = when {
                                isCompleted -> ScienceGreen
                                isInProgress -> Color(topic.colorHex)
                                else -> Color(0xFF94A3B8)
                            },
                            trackColor = Color(topic.colorHex).copy(alpha = 0.12f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when {
                                    isCompleted -> "Gbogbo ẹ̀kọ́ parí! (All units completed) 🌟"
                                    isInProgress -> "${totalUnits - completedUnits} ẹ̀kọ́ ló kù (${totalUnits - completedUnits} remaining)"
                                    else -> "Kò tíì bẹ̀rẹ̀ — tẹ́ ẹ láti bẹ̀rẹ̀ (Tap to start)"
                                },
                                fontSize = 11.sp,
                                fontWeight = if (isCompleted) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isCompleted) ScienceGreen else TextMuted
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "$progressPercent%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCompleted) ScienceGreen else TextPrimary
                                )
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Open Topic",
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // Interactive Badge Details Dialog
    selectedDetailBadge?.let { badge ->
        AlertDialog(
            onDismissRequest = { selectedDetailBadge = null },
            confirmButton = {
                TextButton(
                    onClick = { selectedDetailBadge = null },
                    modifier = Modifier.testTag("btn_close_badge_dialog")
                ) {
                    Text("Ó dára (OK)", fontWeight = FontWeight.Bold, color = WebForestGreen)
                }
            },
            dismissButton = {
                IconButton(
                    onClick = { onSpeakWord(badge.titleYo + ". " + badge.descriptionYo, true) },
                    modifier = Modifier.testTag("btn_speak_badge")
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Speak Badge",
                        tint = WebForestGreen
                    )
                }
            },
            icon = {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(if (badge.isUnlocked) Color(0xFFFEF3C7) else Color(0xFFE2E8F0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(badge.iconEmoji, fontSize = 34.sp)
                }
            },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = badge.titleYo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = badge.titleEn,
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Category chip
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = when (badge.category) {
                            "LESSON" -> WebForestGreenLight
                            "QUIZ" -> ScienceYellowLight
                            "SIMULATION" -> WebGoldenAmberLight
                            else -> SciencePurpleLight
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = when (badge.category) {
                                "LESSON" -> "Ẹ̀kọ́ Sáyẹ́ǹsì (Lesson)"
                                "QUIZ" -> "Ìdánwò (Quiz)"
                                "SIMULATION" -> "Àfarawé Yàrá (Simulation)"
                                else -> "Ìkópa Yàrá (Participation)"
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (badge.category) {
                                "LESSON" -> WebForestGreen
                                "QUIZ" -> Color(0xFFE65100)
                                "SIMULATION" -> WebGoldenAmberDark
                                else -> SciencePurple
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Text(
                        text = badge.descriptionYo,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )

                    Text(
                        text = badge.descriptionEn,
                        fontSize = 11.sp,
                        color = TextMuted
                    )

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Bí o ṣe lè gba á (Criteria):",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = badge.requirementDescriptionYo,
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (badge.isUnlocked) Color(0xFFDCFCE7) else Color(0xFFF1F5F9)
                        ) {
                            Text(
                                text = if (badge.isUnlocked) "Gba! ✨" else "${badge.progress}/${badge.targetCount}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (badge.isUnlocked) Color(0xFF166534) else TextMuted,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        )
    }

    // Celebratory Newly Unlocked Badge Dialog
    celebrationBadge?.let { badge ->
        AlertDialog(
            onDismissRequest = onDismissCelebrationBadge,
            confirmButton = {
                Button(
                    onClick = onDismissCelebrationBadge,
                    colors = ButtonDefaults.buttonColors(containerColor = WebForestGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("btn_dismiss_celebration")
                ) {
                    Text("Ó dára púpọ̀! 🎉 (Awesome!)", fontWeight = FontWeight.Bold)
                }
            },
            icon = {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF3C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(badge.iconEmoji, fontSize = 40.sp)
                }
            },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Ẹ kú oríire! 🎉",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = Color(0xFFD97706)
                    )
                    Text(
                        text = "New Science Badge Unlocked!",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${badge.titleYo} (${badge.titleEn})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = badge.descriptionYo,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        )
    }
}
