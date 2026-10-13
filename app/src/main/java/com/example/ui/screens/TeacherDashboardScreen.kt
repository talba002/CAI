package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CustomTeacherLessonEntity
import com.example.data.local.QuizSubmissionEntity
import com.example.data.local.SyncQueueEntity
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardScreen(
    isAuthenticated: Boolean,
    onVerifyPin: (String) -> Boolean,
    submissions: List<QuizSubmissionEntity>,
    customLessons: List<CustomTeacherLessonEntity>,
    syncHistory: List<SyncQueueEntity>,
    pendingSyncCount: Int,
    isSyncing: Boolean,
    syncMessage: String?,
    apiSpecMarkdown: String,
    onCreateLesson: (Int, String, String, String, String, String, String, String) -> Unit,
    onDeleteLesson: (String) -> Unit,
    onTriggerSync: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToUpload: () -> Unit,
    onNavigateToProgress: () -> Unit = {},
    onNavigateToSimulation: () -> Unit = {},
    onNavigateToGames: () -> Unit = {},
    onNavigateToParticipationRoom: () -> Unit = {},
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var enteredPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }

    if (!isAuthenticated) {
        // PIN Verification Dialog / Screen
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Teacher Portal Access") },
                    navigationIcon = {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("btn_teacher_auth_back")) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(ScienceGreenLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = ScienceGreen,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Ààyè Àwọn Olùkọ́ (Teacher Authorization)",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Please enter the Teacher PIN to manage curriculum, examine learner assessments, and inspect Base44 sync.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = enteredPin,
                    onValueChange = {
                        enteredPin = it
                        pinError = false
                    },
                    label = { Text("Teacher PIN (Default: 1234)") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    isError = pinError,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .testTag("input_teacher_pin")
                )

                if (pinError) {
                    Text(
                        text = "PIN tí o tẹ̀ kò tọ́. Gbìyànjú 1234. (Incorrect PIN. Try 1234)",
                        color = WrongRed,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val ok = onVerifyPin(enteredPin)
                        if (!ok) pinError = true
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(48.dp)
                        .testTag("btn_verify_pin"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ScienceGreen)
                ) {
                    Text("Wọle (Authenticate)", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    // Authenticated Teacher Dashboard
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Ibùdó Olùkọ́ (Teacher Dashboard)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Basic Science Assessment & Content Manager",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_teacher_back")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(
                        onClick = onNavigateToUpload,
                        colors = ButtonDefaults.buttonColors(containerColor = ScienceBlue),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("btn_teacher_goto_upload")
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Upload Studio", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = onLogout, modifier = Modifier.testTag("btn_teacher_logout")) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Lock", tint = WrongRed)
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
        ) {
            // Admin Hub Shortcut Row
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Àwọn Ibùdó Olùkọ́ & Alákòóso (Admin Portals):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = WebForestGreenDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // 1. Live Room
                        FilledTonalButton(
                            onClick = onNavigateToParticipationRoom,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(containerColor = SciencePurpleLight, contentColor = SciencePurple),
                            modifier = Modifier.weight(1f).testTag("admin_quick_live_room")
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("👥", fontSize = 16.sp)
                                Text("Live Room", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // 2. Upload Lesson
                        FilledTonalButton(
                            onClick = onNavigateToUpload,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(containerColor = ScienceBlueLight, contentColor = ScienceBlue),
                            modifier = Modifier.weight(1f).testTag("admin_quick_upload")
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📤", fontSize = 16.sp)
                                Text("Upload", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // 3. Learning Progress
                        FilledTonalButton(
                            onClick = onNavigateToProgress,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(containerColor = ScienceGreenLight, contentColor = ScienceGreen),
                            modifier = Modifier.weight(1f).testTag("admin_quick_progress")
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📊", fontSize = 16.sp)
                                Text("Progress", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // 4. Learning Simulation
                        FilledTonalButton(
                            onClick = onNavigateToSimulation,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(containerColor = WebForestGreenLight, contentColor = WebForestGreen),
                            modifier = Modifier.weight(1f).testTag("admin_quick_simulation")
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🔬", fontSize = 16.sp)
                                Text("Simulate", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // 5. Games
                        FilledTonalButton(
                            onClick = onNavigateToGames,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(containerColor = WebGoldenAmberLight, contentColor = WebGoldenAmberDark),
                            modifier = Modifier.weight(1f).testTag("admin_quick_games")
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🎮", fontSize = 16.sp)
                                Text("Games", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Live Room", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Reports", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Curriculum", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("Base44 Sync", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
            }

            when (selectedTab) {
                0 -> TeacherLiveRoomTab(onNavigateToParticipationRoom, onNavigateToSimulation)
                1 -> ClassroomReportsTab(submissions, pendingSyncCount)
                2 -> CurriculumManagerTab(customLessons, onCreateLesson, onDeleteLesson)
                3 -> Base44SyncTab(syncHistory, pendingSyncCount, isSyncing, syncMessage, apiSpecMarkdown, onTriggerSync)
            }
        }
    }
}

@Composable
fun ClassroomReportsTab(
    submissions: List<QuizSubmissionEntity>,
    pendingSyncCount: Int
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Analytics KPI Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ScienceBlueLight)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Active Pupils", fontSize = 11.sp, color = ScienceBlue, fontWeight = FontWeight.Bold)
                        Text("18 Learners", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ScienceGreenLight)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Avg Assessment", fontSize = 11.sp, color = ScienceGreen, fontWeight = FontWeight.Bold)
                        Text("92% Passed", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }

        item {
            Text(
                text = "Àwọn Àbájáde Ìdánwò Tuntun (Recent Learner Submissions):",
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
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📝", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No assessment submissions yet.", fontWeight = FontWeight.Bold)
                        Text("Submissions by pupils will appear here in real-time.", fontSize = 12.sp, color = TextMuted)
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
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Learner: Ayọ̀ • ${sub.quizId.replace('_', ' ').capitalize()}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Score: ${sub.score}/${sub.totalQuestions} (${sub.percentage}%) • ${if (sub.passed) "Passed" else "Needs Review"}",
                                fontSize = 12.sp,
                                color = if (sub.passed) ScienceGreen else WrongRed,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (sub.isSynced) ScienceGreenLight else ScienceYellowLight
                        ) {
                            Text(
                                text = if (sub.isSynced) "Synced" else "Local Queue",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (sub.isSynced) ScienceGreen else Color(0xFFE65100),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CurriculumManagerTab(
    customLessons: List<CustomTeacherLessonEntity>,
    onCreateLesson: (Int, String, String, String, String, String, String, String) -> Unit,
    onDeleteLesson: (String) -> Unit
) {
    var titleEn by remember { mutableStateOf("") }
    var titleYo by remember { mutableStateOf("") }
    var summaryEn by remember { mutableStateOf("") }
    var summaryYo by remember { mutableStateOf("") }
    var contentEn by remember { mutableStateOf("") }
    var contentYo by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf(2) }
    var author by remember { mutableStateOf("Mrs. Adebayo") }
    var showForm by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Teacher-Authored Science Modules",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Button(
                    onClick = { showForm = !showForm },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ScienceGreen)
                ) {
                    Icon(imageVector = if (showForm) Icons.Default.Close else Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (showForm) "Cancel" else "Create Lesson", fontSize = 12.sp)
                }
            }
        }

        if (showForm) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Add New Bilingual Science Lesson", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = ScienceGreen)

                        OutlinedTextField(
                            value = titleYo,
                            onValueChange = { titleYo = it },
                            label = { Text("Yoruba Title (e.g. Àyíká Ilé)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = titleEn,
                            onValueChange = { titleEn = it },
                            label = { Text("English Title (e.g. Home Environment)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = contentYo,
                            onValueChange = { contentYo = it },
                            label = { Text("Yoruba Content & Explanations") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 4
                        )

                        OutlinedTextField(
                            value = contentEn,
                            onValueChange = { contentEn = it },
                            label = { Text("English Content & Explanations") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 4
                        )

                        Button(
                            onClick = {
                                if (titleYo.isNotBlank() && titleEn.isNotBlank()) {
                                    onCreateLesson(
                                        grade,
                                        titleEn,
                                        titleYo,
                                        summaryEn.ifBlank { titleEn },
                                        summaryYo.ifBlank { titleYo },
                                        contentEn,
                                        contentYo,
                                        author
                                    )
                                    showForm = false
                                    titleYo = ""
                                    titleEn = ""
                                    contentYo = ""
                                    contentEn = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ScienceBlue)
                        ) {
                            Text("Publish Lesson to Pupil Library", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (customLessons.isEmpty()) {
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
                        Text("📚", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("No custom teacher lessons added yet.", fontWeight = FontWeight.Bold)
                        Text("Click 'Create Lesson' to add curriculum topics tailored for your pupils.", fontSize = 12.sp, color = TextMuted)
                    }
                }
            }
        } else {
            items(customLessons) { item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.titleYo, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(item.titleEn, fontSize = 12.sp, color = TextMuted)
                            }

                            IconButton(onClick = { onDeleteLesson(item.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = WrongRed)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Base44SyncTab(
    syncHistory: List<SyncQueueEntity>,
    pendingSyncCount: Int,
    isSyncing: Boolean,
    syncMessage: String?,
    apiSpecMarkdown: String,
    onTriggerSync: () -> Unit
) {
    var showSpecDoc by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ScienceBlueLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Base44 Cloud Synchronization Engine", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = ScienceBlue)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Base44 Web App: https://yoruba-basic-science-cai.base44.app/dashboard",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pending Queue: $pendingSyncCount items",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (pendingSyncCount > 0) Color(0xFFE65100) else ScienceGreen
                        )

                        Button(
                            onClick = onTriggerSync,
                            enabled = !isSyncing,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ScienceBlue)
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
                                Text("Execute Batch Sync")
                            }
                        }
                    }

                    if (syncMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(syncMessage, fontSize = 12.sp, color = ScienceGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showSpecDoc = !showSpecDoc }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Base44 API Adapter & Migration Spec", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("View REST endpoints, schema & idempotency models", fontSize = 11.sp, color = TextMuted)
                    }
                    Icon(
                        imageVector = if (showSpecDoc) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            }
        }

        if (showSpecDoc) {
            item {
                Surface(
                    color = Color(0xFF1E1E1E),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = apiSpecMarkdown,
                        color = Color(0xFFE0E0E0),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        }

        item {
            Text("Recent Sync Queue Events", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        if (syncHistory.isEmpty()) {
            item {
                Text("No sync events recorded yet.", fontSize = 12.sp, color = TextMuted)
            }
        } else {
            items(syncHistory) { event ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(event.entityType, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Queue ID: #${event.queueId}", fontSize = 10.sp, color = TextMuted)
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (event.status == "SYNCED") ScienceGreenLight else ScienceYellowLight
                        ) {
                            Text(
                                text = event.status,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (event.status == "SYNCED") ScienceGreen else Color(0xFFE65100),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeacherLiveRoomTab(
    onNavigateToParticipationRoom: () -> Unit,
    onNavigateToSimulation: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SciencePurpleLight),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, SciencePurple),
                modifier = Modifier.fillMaxWidth().testTag("card_teacher_live_room_hub")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SciencePurple
                        ) {
                            Text(
                                text = "ACTIVE CLASSROOM: IMO-101",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ScienceGreenLight
                        ) {
                            Text(
                                text = "🟢 5 Learners Joined",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ScienceGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Yàrá Ìkópa Olùkọ́ & Àwọn Akẹ́kọ̀ọ́",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = SciencePurple
                    )
                    Text(
                        text = "Broadcast live science challenges, view hands raised, track student answers, and launch simulations with pupils in real-time.",
                        fontSize = 12.sp,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onNavigateToParticipationRoom,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SciencePurple),
                            modifier = Modifier.weight(1f).testTag("btn_teacher_enter_live_room")
                        ) {
                            Text("Wọ Yàrá Ìkópa (Open Live Room 👥)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = onNavigateToSimulation,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).testTag("btn_teacher_open_sim")
                        ) {
                            Text("Simulations (🔬)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Àwọn Àfarawé tí wà fún Kíláàsì (Classroom Simulations in Room):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = WebForestGreenDark
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF0F172A),
                            modifier = Modifier.weight(1f).clickable { onNavigateToSimulation() }
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("🪐", fontSize = 20.sp)
                                Text("Ètò Oòrùn", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = ScienceYellow)
                                Text("Solar System orbits & planets", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = WebForestGreenLight,
                            modifier = Modifier.weight(1f).clickable { onNavigateToSimulation() }
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("🌱", fontSize = 20.sp)
                                Text("Ìdàgbàsókè Ewéko", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = WebForestGreen)
                                Text("Photosynthesis, light & soil", fontSize = 10.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }
        }
    }
}

