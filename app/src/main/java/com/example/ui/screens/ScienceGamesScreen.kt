package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LanguageMode
import com.example.ui.theme.*
import com.example.util.SoundPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScienceGamesScreen(
    currentLanguageMode: LanguageMode,
    onAwardStars: (Int) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var selectedGameTab by remember { mutableStateOf(0) } // 0: Sorter, 1: Memory Match

    // Game 1: Living vs Non-Living state
    data class SorterItem(val emoji: String, val nameYo: String, val nameEn: String, val isLiving: Boolean)
    val sorterItems = remember {
        listOf(
            SorterItem("🦁", "Kìnìún", "Lion", true),
            SorterItem("🪨", "Òkúta", "Stone", false),
            SorterItem("🌱", "Ohun Ọ̀gbìn", "Bean Sprout", true),
            SorterItem("🚗", "Ọkọ̀", "Car", false),
            SorterItem("🦅", "Ẹyẹ", "Eagle", true),
            SorterItem("✏️", "Pẹ́ńsù", "Pencil", false),
            SorterItem("🐟", "Ẹja", "Fish", true),
            SorterItem("🪑", "Àga", "Wooden Chair", false),
            SorterItem("👶", "Ọmọdé", "Baby Child", true),
            SorterItem("🥛", "Ife Omi", "Glass Cup", false)
        )
    }

    var sorterRound by remember { mutableStateOf(0) }
    var sorterScore by remember { mutableStateOf(0) }
    var sorterFeedback by remember { mutableStateOf<String?>(null) }
    var sorterGameOver by remember { mutableStateOf(false) }

    // Game 2: Memory Match state
    data class MemoryCard(val id: Int, val text: String, val emoji: String, val pairId: Int, var isFlipped: Boolean = false, var isMatched: Boolean = false)
    val initialCards = remember {
        listOf(
            MemoryCard(0, "Gbòǹgbò", "🌱", 1),
            MemoryCard(1, "Root", "🌱", 1),
            MemoryCard(2, "Ewé", "🍃", 2),
            MemoryCard(3, "Leaf", "🍃", 2),
            MemoryCard(4, "Ojú", "👀", 3),
            MemoryCard(5, "Eye", "👀", 3),
            MemoryCard(6, "Òjò", "🌧️", 4),
            MemoryCard(7, "Rain", "🌧️", 4)
        ).shuffled()
    }

    var cards by remember { mutableStateOf(initialCards) }
    var flippedIndices by remember { mutableStateOf<List<Int>>(emptyList()) }
    var matchedPairsCount by remember { mutableStateOf(0) }
    var movesCount by remember { mutableStateOf(0) }
    var matchGameOver by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Eré Sáyẹ́ǹsì (Science Games)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Playful educational learning activities",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_games")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            TabRow(
                selectedTabIndex = selectedGameTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedGameTab == 0,
                    onClick = { selectedGameTab = 0 },
                    text = { Text("🐾 Living vs Non-Living Sorter", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedGameTab == 1,
                    onClick = { selectedGameTab = 1 },
                    text = { Text("🃏 Science Word Match", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
            }

            if (selectedGameTab == 0) {
                // Game 1: Sorter
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = ScienceBlueLight
                        ) {
                            Text(
                                text = "Ìyípo: ${sorterRound + 1} / ${sorterItems.size}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = ScienceBlue,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = ScienceYellowLight
                        ) {
                            Text(
                                text = "Àmì (Score): $sorterScore",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFFE65100),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    if (!sorterGameOver) {
                        val currentItem = sorterItems[sorterRound]

                        // Item Card
                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(currentItem.emoji, fontSize = 64.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = currentItem.nameYo,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = currentItem.nameEn,
                                    fontSize = 15.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        if (sorterFeedback != null) {
                            Text(
                                text = sorterFeedback!!,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (sorterFeedback!!.startsWith("🎉")) ScienceGreen else WrongRed
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Two Target Buttons: Living vs Non-Living
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (currentItem.isLiving) {
                                        sorterScore += 10
                                        sorterFeedback = "🎉 O tọ́! Ohun alààyè ni! (Correct: Living!)"
                                        SoundPlayer.playCorrectChime()
                                    } else {
                                        sorterFeedback = "❌ Rárá! Kò ní ẹ̀mí (Non-living item)"
                                        SoundPlayer.playIncorrectTone()
                                    }
                                    if (sorterRound < sorterItems.size - 1) {
                                        sorterRound += 1
                                    } else {
                                        sorterGameOver = true
                                        onAwardStars(5)
                                    }
                                },
                                shape = RoundedCornerShape(18.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ScienceGreen),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp)
                                    .testTag("btn_sorter_living")
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("🌿 Alààyè", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                                    Text("(Living Thing)", fontSize = 11.sp)
                                }
                            }

                            Button(
                                onClick = {
                                    if (!currentItem.isLiving) {
                                        sorterScore += 10
                                        sorterFeedback = "🎉 O tọ́! Ohun aláìláàyè ni! (Correct: Non-Living!)"
                                        SoundPlayer.playCorrectChime()
                                    } else {
                                        sorterFeedback = "❌ Rárá! Ẹ̀dá alààyè ni (Living item)"
                                        SoundPlayer.playIncorrectTone()
                                    }
                                    if (sorterRound < sorterItems.size - 1) {
                                        sorterRound += 1
                                    } else {
                                        sorterGameOver = true
                                        onAwardStars(5)
                                    }
                                },
                                shape = RoundedCornerShape(18.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF6C00)),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp)
                                    .testTag("btn_sorter_non_living")
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("🪨 Aláìláàyè", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                                    Text("(Non-Living)", fontSize = 11.sp)
                                }
                            }
                        }
                    } else {
                        // Game Over Celebration
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = ScienceYellowLight),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("🏆", fontSize = 52.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Ẹ Kú Oríire! (Game Complete!)",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp,
                                    color = Color(0xFFE65100)
                                )
                                Text(
                                    text = "You scored $sorterScore points and earned +5 Stars!",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Button(
                                    onClick = {
                                        sorterRound = 0
                                        sorterScore = 0
                                        sorterFeedback = null
                                        sorterGameOver = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ScienceBlue),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Tún Ṣeré (Play Again)", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            } else {
                // Game 2: Memory Match Grid
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pairs: $matchedPairsCount / 4",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = ScienceGreen
                        )
                        Text(
                            text = "Moves: $movesCount",
                            fontSize = 14.sp,
                            color = TextMuted
                        )
                        Button(
                            onClick = {
                                cards = initialCards.map { it.copy(isFlipped = false, isMatched = false) }.shuffled()
                                flippedIndices = emptyList()
                                matchedPairsCount = 0
                                movesCount = 0
                                matchGameOver = false
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ScienceBlueLight, contentColor = ScienceBlue)
                        ) {
                            Text("Reset", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (!matchGameOver) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            itemsIndexed(cards) { index, card ->
                                val isRevealed = card.isFlipped || card.isMatched
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = when {
                                            card.isMatched -> ScienceGreenLight
                                            isRevealed -> ScienceBlueLight
                                            else -> ScienceBlue
                                        }
                                    ),
                                    modifier = Modifier
                                        .height(105.dp)
                                        .clickable(enabled = !isRevealed && flippedIndices.size < 2) {
                                            movesCount += 1
                                            val newFlipped = flippedIndices + index
                                            cards = cards.mapIndexed { i, c -> if (i == index) c.copy(isFlipped = true) else c }
                                            flippedIndices = newFlipped

                                            if (newFlipped.size == 2) {
                                                val firstCard = cards[newFlipped[0]]
                                                val secondCard = cards[newFlipped[1]]
                                                if (firstCard.pairId == secondCard.pairId) {
                                                    // Matched!
                                                    SoundPlayer.playCorrectChime()
                                                    cards = cards.mapIndexed { i, c ->
                                                        if (i in newFlipped) c.copy(isMatched = true) else c
                                                    }
                                                    matchedPairsCount += 1
                                                    flippedIndices = emptyList()
                                                    if (matchedPairsCount == 4) {
                                                        matchGameOver = true
                                                        onAwardStars(3)
                                                    }
                                                } else {
                                                    // Mismatch, reset after a moment
                                                    SoundPlayer.playIncorrectTone()
                                                }
                                            } else if (newFlipped.size > 2) {
                                                // Reset un-matched flips
                                                cards = cards.map { if (!it.isMatched) it.copy(isFlipped = false) else it }
                                                cards = cards.mapIndexed { i, c -> if (i == index) c.copy(isFlipped = true) else c }
                                                flippedIndices = listOf(index)
                                            }
                                        }
                                        .testTag("memory_card_$index")
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isRevealed) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(card.emoji, fontSize = 28.sp)
                                                Text(
                                                    text = card.text,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp,
                                                    color = if (card.isMatched) ScienceGreen else ScienceBlue
                                                )
                                            }
                                        } else {
                                            Text("❓", fontSize = 28.sp)
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = ScienceGreenLight),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("🎉", fontSize = 52.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "O Kú Iṣẹ́! (All Pairs Matched!)",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = ScienceGreen
                                )
                                Text("Completed in $movesCount moves. +3 Stars awarded!", fontSize = 13.sp, color = TextSecondary)
                            }
                        }
                    }
                }
            }
        }
    }
}
