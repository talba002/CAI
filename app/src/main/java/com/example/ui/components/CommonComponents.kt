package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LanguageMode
import com.example.data.model.VocabularyWord
import com.example.ui.theme.*

@Composable
fun BilingualText(
    yorubaText: String,
    englishText: String,
    languageMode: LanguageMode,
    modifier: Modifier = Modifier,
    yorubaStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.titleMedium,
    englishStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
    showBadges: Boolean = true
) {
    Column(modifier = modifier) {
        when (languageMode) {
            LanguageMode.YORUBA -> {
                Text(
                    text = yorubaText,
                    style = yorubaStyle.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.testTag("text_yoruba")
                )
            }
            LanguageMode.ENGLISH -> {
                Text(
                    text = englishText,
                    style = englishStyle.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.testTag("text_english")
                )
            }
            LanguageMode.BILINGUAL -> {
                // Primary Yoruba with vibrant indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (showBadges) {
                        Surface(
                            color = ScienceGreenLight,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "YO",
                                color = ScienceGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = yorubaText,
                        style = yorubaStyle.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                // English counterpart
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (showBadges) {
                        Surface(
                            color = ScienceBlueLight,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "EN",
                                color = ScienceBlue,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = englishText,
                        style = englishStyle.copy(color = TextSecondary),
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
            }
        }
    }
}

@Composable
fun AudioNarrationCard(
    isSpeaking: Boolean,
    onSpeakYoruba: () -> Unit,
    onSpeakEnglish: () -> Unit,
    onStop: () -> Unit,
    currentSpeed: Float,
    onSpeedChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("audio_narration_bar"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSpeaking) ScienceYellowLight else ScienceBlueLight
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (isSpeaking) ScienceYellow else ScienceBlue,
                    modifier = Modifier.size(42.dp)
                ) {
                    IconButton(
                        onClick = {
                            if (isSpeaking) onStop() else onSpeakYoruba()
                        },
                        modifier = Modifier.testTag("btn_audio_toggle")
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Filled.Stop else Icons.Filled.VolumeUp,
                            contentDescription = if (isSpeaking) "Stop Narration" else "Listen Audio",
                            tint = Color.White
                        )
                    }
                }

                Column {
                    Text(
                        text = if (isSpeaking) "Ń sọ̀rọ̀ báyìí... (Narrating...)" else "Gbọ́ Ọ̀rọ̀ Náà (Audio Narration)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Listen in Yorùbá or English",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                    onClick = onSpeakYoruba,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ScienceGreen,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("btn_listen_yoruba")
                ) {
                    Text("Yorùbá", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                FilledTonalButton(
                    onClick = onSpeakEnglish,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = ScienceBlue,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("btn_listen_english")
                ) {
                    Text("English", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun VocabularyChip(
    word: VocabularyWord,
    onSpeak: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .testTag("chip_vocab_${word.english}")
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(word.emoji, fontSize = 22.sp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = word.yoruba,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = ScienceGreen
                    )
                    Text(
                        text = word.english,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
                IconButton(
                    onClick = { onSpeak(word.yoruba, true) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.VolumeUp,
                        contentDescription = "Speak ${word.yoruba}",
                        tint = ScienceBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .background(ScienceGreenLight, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Àmì Ohùn (Tones): ${word.toneMarks}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = ScienceGreen
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = word.definitionYo,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Def: ${word.definitionEn}",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }
}

@Composable
fun ScienceTopBar(
    learnerName: String,
    avatarEmoji: String,
    totalStars: Int,
    currentLanguageMode: LanguageMode,
    onLanguageChange: (LanguageMode) -> Unit,
    onProfileClick: () -> Unit,
    onTeacherClick: () -> Unit,
    pendingSyncCount: Int = 0
) {
    var languageMenuOpen by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Logo and App Brand (Yorùbá CAI)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onProfileClick() }
                    .padding(2.dp)
                    .testTag("topbar_learner_profile")
            ) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.web_logo),
                    contentDescription = "Ìmọ̀ Science Logo",
                    contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Column {
                    Text(
                        text = "Ìmọ̀ Science",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Yorùbá Basic Science",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            // Language Switcher & Teacher Portal
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Language Dropdown
                Box {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .clickable { languageMenuOpen = true }
                            .testTag("btn_language_selector")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = "Language",
                                modifier = Modifier.size(16.dp),
                                tint = ScienceBlue
                            )
                            Text(
                                text = when (currentLanguageMode) {
                                    LanguageMode.BILINGUAL -> "Àpapọ̀"
                                    LanguageMode.YORUBA -> "Yorùbá"
                                    LanguageMode.ENGLISH -> "English"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = languageMenuOpen,
                        onDismissRequest = { languageMenuOpen = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Àpapọ̀ (Bilingual)") },
                            onClick = {
                                onLanguageChange(LanguageMode.BILINGUAL)
                                languageMenuOpen = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Yorùbá Nìkan (Yoruba Only)") },
                            onClick = {
                                onLanguageChange(LanguageMode.YORUBA)
                                languageMenuOpen = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("English Only (Gẹ̀ẹ́sì)") },
                            onClick = {
                                onLanguageChange(LanguageMode.ENGLISH)
                                languageMenuOpen = false
                            }
                        )
                    }
                }

                // Teacher Mode Access Button
                IconButton(
                    onClick = onTeacherClick,
                    modifier = Modifier
                        .size(36.dp)
                        .background(ScienceGreenLight, CircleShape)
                        .testTag("btn_teacher_portal")
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Teacher Portal",
                        tint = ScienceGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
