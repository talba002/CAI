package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import com.example.data.model.ScienceTopic
import com.example.ui.components.BilingualText
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicLibraryScreen(
    topics: List<ScienceTopic>,
    selectedGrade: Int?,
    currentLanguageMode: LanguageMode,
    onSelectGrade: (Int?) -> Unit,
    onSelectLesson: (String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var expandedTopicId by remember { mutableStateOf<String?>(topics.firstOrNull()?.id) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Àkójọpọ̀ Ẹ̀kọ́ Sáyẹ́ǹsì",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Topic & Lesson Curriculum Library",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_topics")) {
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Class selection row
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Yan Kíláàsì (Filter by Class):",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedGrade == null,
                            onClick = { onSelectGrade(null) },
                            label = { Text("Gbogbo (All)") },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    items((1..6).toList()) { grade ->
                        FilterChip(
                            selected = selectedGrade == grade,
                            onClick = { onSelectGrade(grade) },
                            label = { Text("Primary $grade") },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            val filteredTopics = if (selectedGrade == null) topics else topics.filter { it.grade == selectedGrade }

            items(filteredTopics) { topic ->
                val isExpanded = expandedTopicId == topic.id

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("topic_item_${topic.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedTopicId = if (isExpanded) null else topic.id
                                }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(topic.colorHex).copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(topic.iconEmoji, fontSize = 24.sp)
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    color = ScienceBlueLight,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "Primary ${topic.grade}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ScienceBlue,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                BilingualText(
                                    yorubaText = topic.titleYo,
                                    englishText = topic.titleEn,
                                    languageMode = currentLanguageMode,
                                    yorubaStyle = MaterialTheme.typography.titleMedium,
                                    englishStyle = MaterialTheme.typography.bodySmall,
                                    showBadges = false
                                )
                            }

                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Expand",
                                tint = TextMuted
                            )
                        }

                        AnimatedVisibility(visible = isExpanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                HorizontalDivider()
                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = if (currentLanguageMode == LanguageMode.YORUBA) topic.descriptionYo else topic.descriptionEn,
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Àwọn Ẹ̀kọ́ Tó Wà Níbẹ̀ (Lessons):",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                topic.lessons.forEach { lesson ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = ScienceBlueLight,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 8.dp)
                                            .clickable { onSelectLesson(lesson.id) }
                                            .testTag("btn_lesson_${lesson.id}")
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Text(lesson.iconEmoji, fontSize = 20.sp)
                                                Column {
                                                    Text(
                                                        text = lesson.titleYo,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 13.sp,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Text(
                                                        text = lesson.titleEn,
                                                        fontSize = 11.sp,
                                                        color = TextMuted
                                                    )
                                                }
                                            }

                                            FilledTonalButton(
                                                onClick = { onSelectLesson(lesson.id) },
                                                shape = RoundedCornerShape(10.dp),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                colors = ButtonDefaults.filledTonalButtonColors(containerColor = ScienceBlue)
                                            ) {
                                                Text("Kà (Read)", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
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
