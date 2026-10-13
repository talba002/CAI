package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ParticipationRoomEntity
import com.example.data.local.RoomLearnerResponseEntity
import com.example.data.model.LanguageMode
import com.example.data.model.LearnerProfile
import com.example.ui.components.BilingualText
import com.example.ui.theme.*
import com.example.util.SoundPlayer
import org.json.JSONArray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticipationRoomScreen(
    room: ParticipationRoomEntity?,
    learnerProfile: LearnerProfile,
    responses: List<RoomLearnerResponseEntity>,
    isHandRaised: Boolean,
    currentLanguageMode: LanguageMode,
    isTeacherModeInitially: Boolean = false,
    onToggleHandRaise: () -> Unit,
    onSubmitAnswer: (Int, Boolean) -> Unit,
    onTeacherBroadcastQuestion: (String, String, List<String>, Int) -> Unit,
    onTeacherAwardStar: (String, Int) -> Unit,
    onSpeak: (String, Boolean) -> Unit,
    onNavigateToSimulation: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var isTeacherView by remember { mutableStateOf(isTeacherModeInitially) }
    var selectedAnswerOption by remember { mutableStateOf<Int?>(null) }
    var hasSubmittedAnswer by remember { mutableStateOf(false) }

    // Parse options from JSON
    val optionsList = remember(room?.activeQuestionOptionsJson) {
        val list = mutableListOf<String>()
        try {
            val arr = JSONArray(room?.activeQuestionOptionsJson ?: "[]")
            for (i in 0 until arr.length()) {
                list.add(arr.getString(i))
            }
        } catch (_: Exception) {}
        if (list.isEmpty()) {
            listOf("Mẹ́kúrí (Mercury)", "Fẹ́núsì (Venus)", "Ayé (Earth)", "Mássì (Mars)")
        } else list
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Yàrá Ìkópa Sáyẹ́ǹsì (Live Room)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Room Code: ${room?.roomCode ?: "IMO-101"} • ${room?.teacherName ?: "Olùkọ́ Adéyẹmí"}",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_room_back")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Switch between Student View & Teacher View
                    FilterChip(
                        selected = isTeacherView,
                        onClick = { isTeacherView = !isTeacherView },
                        label = {
                            Text(
                                if (isTeacherView) "Mode: Olùkọ́ (Teacher)" else "Mode: Akẹ́kọ̀ọ́ (Pupil)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SciencePurpleLight,
                            selectedLabelColor = SciencePurple
                        ),
                        modifier = Modifier.padding(end = 8.dp).testTag("btn_toggle_room_role")
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(WebBackground)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Room Status & Live Session Banner
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = WebForestGreenDark),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth().testTag("card_room_banner")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ScienceGreenLight
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(ScienceGreen))
                                    Text("🟢 LIVE CLASSROOM", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ScienceGreen)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "Code: ${room?.roomCode ?: "IMO-101"}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (currentLanguageMode == LanguageMode.YORUBA) (room?.titleYo ?: "Yàrá Sáyẹ́ǹsì Alààyè") else (room?.titleEn ?: "Live Science Participation Room"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Olùkọ́: ${room?.teacherName ?: "Olùkọ́ Adéyẹmí"} • Kókó Ẹ̀kọ́: ${room?.currentTopic ?: "Ohun Alààyè & Ètò Oòrùn"}",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick link to open simulation in class
                        Button(
                            onClick = onNavigateToSimulation,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WebGoldenAmberDark),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("btn_room_open_sim")
                        ) {
                            Icon(Icons.Default.Science, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Wo Àfarawé Kíláàsì (Class Simulation: Solar / Plant)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Classmates Connected Strip
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Àwọn Akẹ́kọ̀ọ́ Tí Wọ́n Wà Ní Yàrá (${responses.size} Joined):",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = WebForestGreenDark
                            )
                            Text(
                                text = "Hands Raised: ${responses.count { it.handRaised }} ✋",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (responses.any { it.handRaised }) WrongRed else TextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(responses) { resp ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (resp.handRaised) ScienceCoralLight else BackgroundLight,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (resp.handRaised) ScienceCoral else WebBorder
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(resp.avatarEmoji, fontSize = 18.sp)
                                        Column {
                                            Text(
                                                text = resp.learnerName,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            if (resp.handRaised) {
                                                Text("Hand Up ✋", fontSize = 9.sp, color = ScienceCoral, fontWeight = FontWeight.Bold)
                                            } else {
                                                Text(if (resp.isCorrect) "Answered ✅" else "Listening 👂", fontSize = 9.sp, color = TextMuted)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // IF TEACHER VIEW: Show Teacher Controls
            if (isTeacherView) {
                item {
                    TeacherRoomControlSection(
                        room = room,
                        responses = responses,
                        onBroadcastQuestion = onTeacherBroadcastQuestion,
                        onAwardBonusStar = onTeacherAwardStar,
                        onSpeak = onSpeak
                    )
                }
            } else {
                // IF LEARNER VIEW: Show Interactive Student Room Component
                item {
                    LearnerRoomInteractionSection(
                        room = room,
                        options = optionsList,
                        selectedOption = selectedAnswerOption,
                        hasSubmitted = hasSubmittedAnswer,
                        isHandRaised = isHandRaised,
                        currentLanguageMode = currentLanguageMode,
                        onToggleHandRaise = {
                            onToggleHandRaise()
                            SoundPlayer.playClick()
                        },
                        onSelectOption = { idx ->
                            selectedAnswerOption = idx
                            val correct = idx == (room?.correctAnswerIndex ?: 2)
                            hasSubmittedAnswer = true
                            onSubmitAnswer(idx, correct)
                            if (correct) SoundPlayer.playSuccess() else SoundPlayer.playIncorrect()
                        },
                        onSpeak = onSpeak
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// LEARNER INTERACTION SECTION
// -------------------------------------------------------------
@Composable
fun LearnerRoomInteractionSection(
    room: ParticipationRoomEntity?,
    options: List<String>,
    selectedOption: Int?,
    hasSubmitted: Boolean,
    isHandRaised: Boolean,
    currentLanguageMode: LanguageMode,
    onToggleHandRaise: () -> Unit,
    onSelectOption: (Int) -> Unit,
    onSpeak: (String, Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Raise Hand Big Button for Pupils
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = if (isHandRaised) ScienceCoralLight else MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(
                1.5.dp,
                if (isHandRaised) ScienceCoral else ScienceBlue
            ),
            modifier = Modifier.fillMaxWidth().testTag("card_raise_hand")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(if (isHandRaised) "✋" else "🙋", fontSize = 32.sp)
                    Column {
                        Text(
                            text = if (isHandRaised) "Ọwọ́ Rẹ Wà Lókè! (Hand Raised)" else "Gbọ́wọ́ Sókè (Raise Hand in Class)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isHandRaised) ScienceCoral else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isHandRaised) "Olùkọ́ ti rí ọwọ́ rẹ! (Teacher notified)" else "Tap to ask a question or volunteer",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                Button(
                    onClick = onToggleHandRaise,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isHandRaised) ScienceCoral else ScienceBlue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("btn_raise_hand")
                ) {
                    Text(
                        text = if (isHandRaised) "Rẹ̀wọ́ (Lower ✋)" else "Gbọ́wọ́ (Raise ✋)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Teacher's Live Challenge Question
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder(),
            modifier = Modifier.fillMaxWidth().testTag("card_room_question")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = WebGoldenAmberLight
                    ) {
                        Text(
                            text = "❓ ÌBÈÈRÈ OLÙKỌ́ (Teacher's Question)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = WebGoldenAmberDark,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            val prompt = "${room?.activeQuestionYo ?: ""}. ${room?.activeQuestionEn ?: ""}"
                            onSpeak(prompt, currentLanguageMode == LanguageMode.YORUBA)
                        },
                        modifier = Modifier.size(36.dp).background(ScienceGreenLight, CircleShape)
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Listen", tint = ScienceGreen, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                BilingualText(
                    yorubaText = room?.activeQuestionYo ?: "Èwo nínú àwọn ayé ló wà ní ipò kẹta sí oòrùn níbi tí ohun alààyè ń gbé?",
                    englishText = room?.activeQuestionEn ?: "Which planet is third from the Sun where living things reside?",
                    languageMode = currentLanguageMode,
                    yorubaStyle = MaterialTheme.typography.titleMedium,
                    englishStyle = MaterialTheme.typography.bodyMedium,
                    showBadges = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Yan Ìdáhùn Rẹ (Select your answer):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Options List
                options.forEachIndexed { index, optionText ->
                    val isSelected = selectedOption == index
                    val isCorrectChoice = index == (room?.correctAnswerIndex ?: 2)

                    val containerColor = when {
                        !hasSubmitted -> if (isSelected) ScienceBlueLight else Color.White
                        isCorrectChoice -> ScienceGreenLight
                        isSelected -> ScienceCoralLight
                        else -> Color.White
                    }

                    val borderColor = when {
                        !hasSubmitted -> if (isSelected) ScienceBlue else WebBorder
                        isCorrectChoice -> ScienceGreen
                        isSelected -> WrongRed
                        else -> WebBorder
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = containerColor,
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable(enabled = !hasSubmitted) { onSelectOption(index) }
                            .testTag("room_opt_$index")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected || (hasSubmitted && isCorrectChoice)) ScienceGreen else BackgroundLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val letter = ('A' + index).toString()
                                    Text(
                                        text = letter,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (isSelected || (hasSubmitted && isCorrectChoice)) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Text(
                                    text = optionText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            if (hasSubmitted) {
                                if (isCorrectChoice) {
                                    Text("✅ O tọ́! (+2 ⭐)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = ScienceGreen)
                                } else if (isSelected) {
                                    Text("❌ Gbìyànjú lẹ́ẹ̀kan síi", fontSize = 11.sp, color = WrongRed)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TEACHER CONTROL SECTION
// -------------------------------------------------------------
@Composable
fun TeacherRoomControlSection(
    room: ParticipationRoomEntity?,
    responses: List<RoomLearnerResponseEntity>,
    onBroadcastQuestion: (String, String, List<String>, Int) -> Unit,
    onAwardBonusStar: (String, Int) -> Unit,
    onSpeak: (String, Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Teacher Studio Command Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder(),
            modifier = Modifier.fillMaxWidth().testTag("card_teacher_studio")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Ìṣàkóso Olùkọ́ (Teacher Command Center)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = SciencePurple
                )
                Text(
                    text = "Broadcast instant questions to all joined pupils and reward participation in Room.",
                    fontSize = 11.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text("Tẹ láti fi Ìbéèrè Ránṣẹ́ (Quick Broadcast Questions):", fontWeight = FontWeight.Bold, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(6.dp))

                // Quick Broadcast 1: Solar System
                Button(
                    onClick = {
                        onBroadcastQuestion(
                            "Which planet is third from the Sun where living things reside?",
                            "Èwo nínú àwọn ayé ló wà ní ipò kẹta sí oòrùn níbi tí ohun alààyè ń gbé?",
                            listOf("Mẹ́kúrí (Mercury)", "Fẹ́núsì (Venus)", "Ayé (Earth)", "Mássì (Mars)"),
                            2
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ScienceBlue),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("btn_broadcast_solar")
                ) {
                    Text("🪐 Broadcast: Ètò Oòrùn (Solar System Question)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Quick Broadcast 2: Plant Growth
                Button(
                    onClick = {
                        onBroadcastQuestion(
                            "What does a green plant need to produce food through photosynthesis?",
                            "Kí ni ewéko tútù nílò láti se oúnjẹ nípa Photosynthesis?",
                            listOf("Oòrùn àti Omi (Sun & Water)", "Òkúta (Rocks)", "Yìnyín (Ice)", "Òkùnkùn (Darkness)"),
                            0
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ScienceGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("btn_broadcast_plant")
                ) {
                    Text("🌱 Broadcast: Ìdàgbàsókè Ewéko (Plant Photosynthesis)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Live Student Responses Grid / List
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Àwọn Akẹ́kọ̀ọ́ & Ìdáhùn Wọn (Pupil Participation Tracking):",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                responses.forEach { resp ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (resp.handRaised) ScienceCoralLight else BackgroundLight,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (resp.handRaised) ScienceCoral else WebBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(resp.avatarEmoji, fontSize = 24.sp)
                                Column {
                                    Text(
                                        text = resp.learnerName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        if (resp.handRaised) {
                                            Text("✋ Hand Raised", fontSize = 10.sp, color = ScienceCoral, fontWeight = FontWeight.Bold)
                                        }
                                        Text(
                                            text = if (resp.isCorrect) "Answer: Correct ✅" else "Answer: Pending / Attempted",
                                            fontSize = 10.sp,
                                            color = if (resp.isCorrect) ScienceGreen else TextMuted
                                        )
                                    }
                                }
                            }

                            // Award star button
                            IconButton(
                                onClick = {
                                    onAwardBonusStar(resp.learnerId, 1)
                                    SoundPlayer.playSuccess()
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(WebGoldenAmberLight, CircleShape)
                                    .testTag("btn_reward_${resp.learnerId}")
                            ) {
                                Icon(Icons.Default.Star, contentDescription = "Award Star", tint = WebGoldenAmberDark, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
