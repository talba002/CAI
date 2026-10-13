package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VolumeUp
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
import com.example.data.local.BadgeEntity
import com.example.data.model.AchievementBadge
import com.example.data.model.LanguageMode
import com.example.data.model.LearnerProfile
import com.example.ui.components.BilingualText
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    profile: LearnerProfile,
    badges: List<BadgeEntity>,
    currentLanguageMode: LanguageMode,
    onSpeak: ((String, Boolean) -> Unit)? = null,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var selectedCategory by remember { mutableStateOf<String?>("ALL") }

    val categories = listOf(
        "ALL" to "Gbogbo Rẹ̀ (All)",
        "LESSON" to "Ẹ̀kọ́ (Lessons)",
        "QUIZ" to "Ìdánwò (Quizzes)",
        "SIMULATION" to "Àdánwò (Simulations)",
        "PARTICIPATION" to "Yàrá Ìkópa (Room)"
    )

    val filteredBadges = remember(badges, selectedCategory) {
        if (selectedCategory == null || selectedCategory == "ALL") {
            badges
        } else {
            badges.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }
    }

    val unlockedCount = badges.count { it.isUnlocked }
    val totalCount = badges.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Àwọn Àmì Ẹ̀yẹ àti Ìtẹ̀síwájú",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Room-backed Badges & Milestones",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_achievements")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            // Milestone Summary Hero Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ScienceYellowLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🌟", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${profile.totalStars} Ìràwọ̀ Lápapọ̀ (Total Stars)",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = Color(0xFFE65100)
                        )
                        Text(
                            text = "Gba àwọn àmì ẹ̀yẹ pẹ̀lú ẹ̀kọ́, ìdánwò àti àfarawé sáyẹ́ǹsì!",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🔥 ${profile.streakDays}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("Day Streak", fontSize = 11.sp, color = TextMuted)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📚 ${profile.completedLessonsCount}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("Lessons", fontSize = 11.sp, color = TextMuted)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🏆 $unlockedCount / $totalCount", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = WebForestGreen)
                                Text("Badges", fontSize = 11.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }

            // Category Filter Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(categories) { (code, label) ->
                        val isSelected = selectedCategory == code
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = code },
                            label = { Text(label, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = WebForestGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Àwọn Ẹ̀bùn Ẹ̀kọ́ (Badges Collection)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Badges List
            items(filteredBadges) { badge ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (badge.isUnlocked) Color(0xFFFFFBEB) else Color(0xFFF8FAFC)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (badge.isUnlocked) 1.5.dp else 1.dp,
                        color = if (badge.isUnlocked) Color(0xFFF59E0B) else Color(0xFFCBD5E1)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("badge_${badge.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(if (badge.isUnlocked) Color(0xFFFEF3C7) else Color(0xFFE2E8F0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(badge.iconEmoji, fontSize = 28.sp)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = badge.titleYo,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (badge.isUnlocked) Color(0xFF92400E) else TextPrimary
                                )

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = when (badge.category) {
                                        "LESSON" -> WebForestGreenLight
                                        "QUIZ" -> ScienceYellowLight
                                        "SIMULATION" -> WebGoldenAmberLight
                                        else -> SciencePurpleLight
                                    }
                                ) {
                                    Text(
                                        text = when (badge.category) {
                                            "LESSON" -> "Ẹ̀kọ́"
                                            "QUIZ" -> "Ìdánwò"
                                            "SIMULATION" -> "Àfarawé"
                                            else -> "Yàrá"
                                        },
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when (badge.category) {
                                            "LESSON" -> WebForestGreen
                                            "QUIZ" -> Color(0xFFE65100)
                                            "SIMULATION" -> WebGoldenAmberDark
                                            else -> SciencePurple
                                        },
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Text(
                                text = badge.titleEn,
                                fontSize = 11.sp,
                                color = TextMuted
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = if (currentLanguageMode == LanguageMode.YORUBA) badge.descriptionYo else badge.descriptionEn,
                                fontSize = 12.sp,
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Bí a ṣe ń gba: ${badge.requirementDescriptionYo}",
                                fontSize = 10.sp,
                                color = TextMuted
                            )

                            if (badge.isUnlocked) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "✨ Gba nínú Room database!",
                                    fontSize = 10.sp,
                                    color = ScienceGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (badge.isUnlocked) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Unlocked",
                                    tint = ScienceGreen
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Locked",
                                    tint = TextMuted
                                )
                                Text(
                                    text = "${badge.progress}/${badge.targetCount}",
                                    fontSize = 10.sp,
                                    color = TextMuted,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (onSpeak != null) {
                                IconButton(
                                    onClick = { onSpeak(badge.titleYo + ". " + badge.descriptionYo, true) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Pronounce",
                                        tint = WebForestGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
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
}
