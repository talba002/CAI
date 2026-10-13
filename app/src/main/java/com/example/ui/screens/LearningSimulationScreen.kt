package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SimulationEntity
import com.example.data.model.LanguageMode
import com.example.ui.components.BilingualText
import com.example.ui.theme.*
import com.example.util.SoundPlayer
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningSimulationScreen(
    simulations: List<SimulationEntity> = emptyList(),
    selectedSimulationId: String? = "sim_solar_system",
    onSelectSimulation: (String) -> Unit = {},
    onRecordInteraction: (String, Int) -> Unit = { _, _ -> },
    currentLanguageMode: LanguageMode,
    onSpeak: (String, Boolean) -> Unit,
    onNavigateToParticipationRoom: () -> Unit = {},
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val activeSimId = selectedSimulationId ?: simulations.firstOrNull()?.id ?: "sim_solar_system"
    val scrollState = rememberScrollState()

    // Solar system state
    var orbitSpeed by remember { mutableFloatStateOf(1f) }
    var isOrbitPaused by remember { mutableStateOf(false) }
    var orbitAngle by remember { mutableFloatStateOf(0f) }
    var selectedPlanetIndex by remember { mutableIntStateOf(2) } // Earth default
    var solarStarsAwarded by remember { mutableStateOf(false) }

    // Plant growth state
    var sunlight by remember { mutableFloatStateOf(70f) }
    var water by remember { mutableFloatStateOf(65f) }
    var soil by remember { mutableFloatStateOf(80f) }
    var plantStarsAwarded by remember { mutableStateOf(false) }

    // Water cycle state
    var sunHeat by remember { mutableFloatStateOf(75f) }
    var coolingLevel by remember { mutableFloatStateOf(50f) }
    var isRaining by remember { mutableStateOf(false) }
    var waterStarsAwarded by remember { mutableStateOf(false) }

    // Continuous orbit animation ticker
    LaunchedEffect(isOrbitPaused, orbitSpeed) {
        while (!isOrbitPaused && orbitSpeed > 0f) {
            delay(32) // ~30 fps
            orbitAngle = (orbitAngle + (orbitSpeed * 1.5f)) % 360f
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Yàrá Àyẹ̀wò Sáyẹ́ǹsì (Virtual Lab)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Interactive Simulations from Room Database",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_simulation")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    FilledTonalButton(
                        onClick = onNavigateToParticipationRoom,
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = SciencePurpleLight, contentColor = SciencePurple),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.padding(end = 8.dp).testTag("btn_sim_goto_room")
                    ) {
                        Text("👥 Live Room", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WebBackground)
                .padding(paddingValues)
        ) {
            // Room Simulations Selector Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val simList = if (simulations.isNotEmpty()) simulations else listOf(
                        SimulationEntity("sim_solar_system", "Solar System", "Ètò Oòrùn", "solar_system", 2, "", "", "🪐", ""),
                        SimulationEntity("sim_plant_growth", "Plant Growth", "Ìdàgbàsókè Ewéko", "plant_growth", 1, "", "", "🌱", ""),
                        SimulationEntity("sim_water_cycle", "Water Cycle", "Ìyípo Omi", "water_cycle", 1, "", "", "🌧️", ""),
                        SimulationEntity("sim_human_senses", "Human Senses", "Ẹ̀yà Ìfòye", "human_senses", 1, "", "", "👀", "")
                    )

                    items(simList) { sim ->
                        val isSelected = sim.id == activeSimId
                        val chipColor = if (isSelected) WebForestGreenDark else Color.White
                        val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        val borderColor = if (isSelected) WebForestGreenDark else WebBorder

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = chipColor,
                            border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                            modifier = Modifier
                                .clickable {
                                    onSelectSimulation(sim.id)
                                    onRecordInteraction(sim.id, 0)
                                }
                                .testTag("sim_tab_${sim.category}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(sim.iconEmoji, fontSize = 16.sp)
                                Column {
                                    Text(
                                        text = if (currentLanguageMode == LanguageMode.YORUBA) sim.titleYo else sim.titleEn,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = textColor
                                    )
                                    Text(
                                        text = if (currentLanguageMode == LanguageMode.YORUBA) sim.titleEn else sim.titleYo,
                                        fontSize = 10.sp,
                                        color = if (isSelected) Color.White.copy(alpha = 0.8f) else TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Main Simulation Area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when {
                    activeSimId.contains("solar", ignoreCase = true) -> {
                        // 1. Solar System Interactive Simulation
                        SolarSystemSimulation(
                            orbitSpeed = orbitSpeed,
                            isPaused = isOrbitPaused,
                            orbitAngle = orbitAngle,
                            selectedPlanetIndex = selectedPlanetIndex,
                            currentLanguageMode = currentLanguageMode,
                            onSpeedChange = { orbitSpeed = it },
                            onTogglePause = { isOrbitPaused = !isOrbitPaused },
                            onSelectPlanet = { selectedPlanetIndex = it },
                            onSpeak = onSpeak,
                            starsAwarded = solarStarsAwarded,
                            onAwardStars = {
                                if (!solarStarsAwarded) {
                                    solarStarsAwarded = true
                                    SoundPlayer.playSuccess()
                                    onRecordInteraction(activeSimId, 3)
                                }
                            }
                        )
                    }

                    activeSimId.contains("plant", ignoreCase = true) -> {
                        // 2. Plant Growth Simulation
                        PlantGrowthSimulation(
                            sunlight = sunlight,
                            water = water,
                            soil = soil,
                            currentLanguageMode = currentLanguageMode,
                            onSunlightChange = { sunlight = it },
                            onWaterChange = { water = it },
                            onSoilChange = { soil = it },
                            onSpeak = onSpeak,
                            starsAwarded = plantStarsAwarded,
                            onAwardStars = {
                                if (!plantStarsAwarded) {
                                    plantStarsAwarded = true
                                    SoundPlayer.playSuccess()
                                    onRecordInteraction(activeSimId, 3)
                                }
                            }
                        )
                    }

                    activeSimId.contains("water", ignoreCase = true) -> {
                        // 3. Water Cycle Simulation
                        WaterCycleSimulation(
                            heat = sunHeat,
                            cooling = coolingLevel,
                            isRaining = isRaining,
                            currentLanguageMode = currentLanguageMode,
                            onHeatChange = { sunHeat = it },
                            onCoolingChange = { coolingLevel = it },
                            onToggleRain = { isRaining = !isRaining },
                            onSpeak = onSpeak,
                            starsAwarded = waterStarsAwarded,
                            onAwardStars = {
                                if (!waterStarsAwarded) {
                                    waterStarsAwarded = true
                                    SoundPlayer.playSuccess()
                                    onRecordInteraction(activeSimId, 3)
                                }
                            }
                        )
                    }

                    else -> {
                        // 4. Human Senses Explorer
                        HumanSensesSimulation(
                            currentLanguageMode = currentLanguageMode,
                            onSpeak = onSpeak
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// SOLAR SYSTEM SIMULATION COMPONENT
// -------------------------------------------------------------
data class PlanetInfo(
    val nameYo: String,
    val nameEn: String,
    val emoji: String,
    val distanceAu: Float,
    val color: Color,
    val sizeRadius: Float,
    val orbitRatio: Float,
    val factYo: String,
    val factEn: String
)

@Composable
fun SolarSystemSimulation(
    orbitSpeed: Float,
    isPaused: Boolean,
    orbitAngle: Float,
    selectedPlanetIndex: Int,
    currentLanguageMode: LanguageMode,
    onSpeedChange: (Float) -> Unit,
    onTogglePause: () -> Unit,
    onSelectPlanet: (Int) -> Unit,
    onSpeak: (String, Boolean) -> Unit,
    starsAwarded: Boolean,
    onAwardStars: () -> Unit
) {
    val planets = remember {
        listOf(
            PlanetInfo("Mẹ́kúrí", "Mercury", "🪨", 0.4f, Color(0xFFB0BEC5), 4.5f, 4.0f, "Ayé Mẹ́kúrí ló súnmọ́ oòrùn jùlọ, ó gbóná púpọ̀.", "Mercury is the closest planet to the Sun and extremely hot."),
            PlanetInfo("Fẹ́núsì", "Venus", "🌋", 0.7f, Color(0xFFFFB74D), 6.5f, 2.5f, "Fẹ́núsì tàn bí ìràwọ̀ àárọ̀, àwọsánmọ̀ rẹ̀ nípọn.", "Venus is the brightest and hottest planet in our solar system."),
            PlanetInfo("Ayé", "Earth", "🌍", 1.0f, Color(0xFF29B6F6), 7.5f, 1.0f, "Ayé ni ilé ohun alààyè tí ó ní omi àti afẹ́fẹ́ oxygen.", "Earth is our home planet, rich with water and oxygen for life."),
            PlanetInfo("Mássì", "Mars", "🔴", 1.5f, Color(0xFFE57373), 5.5f, 0.6f, "Ayé pupa tó ní erupe pupa àti àwọn òkè gíga.", "Mars is known as the Red Planet with rusty iron soil."),
            PlanetInfo("Júpítà", "Jupiter", "🪐", 2.2f, Color(0xFFFFCC80), 12.0f, 0.4f, "Júpítà ló tóbi jùlọ nínú gbogbo àwọn ayé tí ń bẹ nínú ètò oòrùn.", "Jupiter is a giant gas planet, the largest in the Solar System."),
            PlanetInfo("Sátọ̀nù", "Saturn", "🪐", 2.8f, Color(0xFFFFE082), 10.5f, 0.25f, "Sátọ̀nù ní òrùka yípo aláràbarà tó lẹ́wà gan-an.", "Saturn is famed for its majestic, dazzling rings made of ice.")
        )
    }

    val selectedPlanet = planets.getOrElse(selectedPlanetIndex) { planets[2] }

    // Simulation Canvas Card
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)), // Deep Space
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .testTag("solar_system_canvas")
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val centerX = canvasWidth * 0.5f
                val centerY = canvasHeight * 0.5f

                // Draw central Sun
                val sunRadius = 20f
                drawCircle(
                    color = Color(0xFFFFD54F).copy(alpha = 0.35f),
                    radius = sunRadius + 14f,
                    center = Offset(centerX, centerY)
                )
                drawCircle(
                    color = Color(0xFFFFB300),
                    radius = sunRadius,
                    center = Offset(centerX, centerY)
                )

                // Draw Orbit Rings and Revolving Planets
                val baseOrbitRadius = 32f
                val orbitStep = 18f

                planets.forEachIndexed { index, planet ->
                    val orbitRadius = baseOrbitRadius + (index * orbitStep)

                    // Draw Orbit path
                    drawCircle(
                        color = Color.White.copy(alpha = 0.15f),
                        radius = orbitRadius,
                        center = Offset(centerX, centerY),
                        style = Stroke(width = 1.2f)
                    )

                    // Calculate planet position
                    val currentPlanetAngle = (orbitAngle * planet.orbitRatio) % 360f
                    val radians = Math.toRadians(currentPlanetAngle.toDouble())
                    val planetX = (centerX + orbitRadius * cos(radians)).toFloat()
                    val planetY = (centerY + orbitRadius * sin(radians)).toFloat()

                    // Highlight selected planet orbit
                    if (index == selectedPlanetIndex) {
                        drawCircle(
                            color = ScienceYellow.copy(alpha = 0.4f),
                            radius = planet.sizeRadius + 6f,
                            center = Offset(planetX, planetY)
                        )
                    }

                    // Draw Planet
                    drawCircle(
                        color = planet.color,
                        radius = planet.sizeRadius,
                        center = Offset(planetX, planetY)
                    )

                    // Draw Saturn Ring if Saturn
                    if (planet.nameEn == "Saturn") {
                        drawOval(
                            color = Color(0xFFFFE082).copy(alpha = 0.6f),
                            topLeft = Offset(planetX - (planet.sizeRadius * 1.8f), planetY - 4f),
                            size = Size(planet.sizeRadius * 3.6f, 8f),
                            style = Stroke(width = 1.8f)
                        )
                    }
                }
            }

            // Live badge in canvas
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.Black.copy(alpha = 0.6f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("☀️ Oòrùn (Sun)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ScienceYellow)
                    Text("• Orbits in Motion", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }

            // Quick pause button
            IconButton(
                onClick = onTogglePause,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
            ) {
                Icon(
                    imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = "Pause Orbit",
                    tint = Color.White
                )
            }
        }
    }

    // Interactive Planet Selector Row
    Text(
        text = "Yan Ayé láti Ṣàwari (Tap a Planet to Explore):",
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = WebForestGreenDark
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(planets.indices.toList()) { index ->
            val p = planets[index]
            val isSelected = index == selectedPlanetIndex

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) ScienceGreenLight else MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (isSelected) ScienceGreen else WebBorder
                ),
                modifier = Modifier
                    .clickable { onSelectPlanet(index) }
                    .testTag("planet_item_${p.nameEn.lowercase()}")
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(p.emoji, fontSize = 20.sp)
                    Text(
                        text = if (currentLanguageMode == LanguageMode.YORUBA) p.nameYo else p.nameEn,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (currentLanguageMode == LanguageMode.YORUBA) p.nameEn else p.nameYo,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }

    // Selected Planet Fact Card with Pronunciation
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(selectedPlanet.emoji, fontSize = 28.sp)
                    Column {
                        Text(
                            text = "${selectedPlanet.nameYo} (${selectedPlanet.nameEn})",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Distance: ${selectedPlanet.distanceAu} AU from Sun",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                IconButton(
                    onClick = {
                        val text = "${selectedPlanet.nameYo}. ${selectedPlanet.factYo}"
                        onSpeak(text, true)
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .background(ScienceGreenLight, CircleShape)
                        .testTag("btn_speak_planet")
                ) {
                    Icon(
                        Icons.Default.VolumeUp,
                        contentDescription = "Pronounce",
                        tint = ScienceGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            BilingualText(
                yorubaText = selectedPlanet.factYo,
                englishText = selectedPlanet.factEn,
                languageMode = currentLanguageMode,
                yorubaStyle = MaterialTheme.typography.bodyMedium,
                englishStyle = MaterialTheme.typography.bodySmall,
                showBadges = false
            )
        }
    }

    // Orbit Speed Controller Slider
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Iyára Ìyípo (Revolution Speed): ${String.format("%.1f", orbitSpeed)}x",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = if (isPaused) "Dídúró (Paused)" else "Active Orbits",
                    fontSize = 11.sp,
                    color = if (isPaused) WrongRed else ScienceGreen
                )
            }

            Slider(
                value = orbitSpeed,
                onValueChange = onSpeedChange,
                valueRange = 0.2f..3.0f,
                colors = SliderDefaults.colors(
                    thumbColor = ScienceYellow,
                    activeTrackColor = ScienceYellow
                )
            )
        }
    }

    // Science Principle & Discovery Reward Button
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WebGoldenAmberLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🪐", fontSize = 20.sp)
                    Text(
                        text = "Àkíyèsí Sáyẹ́ǹsì: Agbára Òòfà (Gravity & Orbits)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = WebGoldenAmberDark
                    )
                }

                IconButton(
                    onClick = {
                        val txt = "Oòrùn lo agbára òòfà láti fa gbogbo àwọn ayé mọ́ra kí wọ́n má baà fò lọ sí òfurufú."
                        onSpeak(txt, true)
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = "Listen", tint = WebGoldenAmberDark, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            BilingualText(
                yorubaText = "Oòrùn wà ní àárín gbùngbùn, ó sì ń lo agbára òòfà (gravity) láti mú kí gbogbo àwọn ayé máa yípo rẹ̀ láìṣubú.",
                englishText = "The Sun sits at the center, its immense gravitational pull holding all planets in stable elliptical orbits.",
                languageMode = currentLanguageMode,
                yorubaStyle = MaterialTheme.typography.bodySmall,
                englishStyle = MaterialTheme.typography.bodySmall,
                showBadges = false
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onAwardStars,
                colors = ButtonDefaults.buttonColors(containerColor = if (starsAwarded) ScienceGreen else WebForestGreenDark),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_solar_earn_stars")
            ) {
                Icon(Icons.Default.Star, contentDescription = null, tint = ScienceYellow)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (starsAwarded) "O Gbà 3 Ìràwọ̀ Sáyẹ́ǹsì! (3 Stars Earned!) ⭐" else "Ṣàwari Ètò Oòrùn (Claim Discovery Stars ⭐)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// -------------------------------------------------------------
// PLANT GROWTH SIMULATION COMPONENT
// -------------------------------------------------------------
@Composable
fun PlantGrowthSimulation(
    sunlight: Float,
    water: Float,
    soil: Float,
    currentLanguageMode: LanguageMode,
    onSunlightChange: (Float) -> Unit,
    onWaterChange: (Float) -> Unit,
    onSoilChange: (Float) -> Unit,
    onSpeak: (String, Boolean) -> Unit,
    starsAwarded: Boolean,
    onAwardStars: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .testTag("plant_canvas")
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Draw Ground / Soil
                val soilTop = canvasHeight * 0.75f
                drawRect(
                    color = Color(0xFF5D4037),
                    topLeft = Offset(0f, soilTop),
                    size = Size(canvasWidth, canvasHeight - soilTop)
                )

                // Draw Sun based on sunlight slider
                val sunRadius = 15f + (sunlight / 100f) * 25f
                val sunAlpha = (sunlight / 100f).coerceIn(0.2f, 1f)
                drawCircle(
                    color = ScienceYellow.copy(alpha = sunAlpha),
                    radius = sunRadius,
                    center = Offset(canvasWidth * 0.85f, canvasHeight * 0.22f)
                )

                // Sun Rays
                if (sunlight > 30f) {
                    val sunCenter = Offset(canvasWidth * 0.85f, canvasHeight * 0.22f)
                    for (i in 0..7) {
                        val angle = (i * 45) * (Math.PI / 180f)
                        val startX = sunCenter.x + (sunRadius + 8f) * cos(angle).toFloat()
                        val startY = sunCenter.y + (sunRadius + 8f) * sin(angle).toFloat()
                        val endX = sunCenter.x + (sunRadius + 20f) * cos(angle).toFloat()
                        val endY = sunCenter.y + (sunRadius + 20f) * sin(angle).toFloat()
                        drawLine(
                            color = ScienceYellow.copy(alpha = sunAlpha * 0.8f),
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            strokeWidth = 3f,
                            cap = StrokeCap.Round
                        )
                    }
                }

                // Growth calculations
                val isWithering = water < 20f || sunlight < 15f
                val isThriving = water in 35f..90f && sunlight in 35f..95f && soil > 30f

                val stemColor = when {
                    water < 20f -> Color(0xFF8D6E63)
                    sunlight < 15f -> Color(0xFFFFF59D)
                    isThriving -> ScienceGreen
                    else -> Color(0xFF66BB6A)
                }

                val plantBaseX = canvasWidth * 0.45f
                val growthFactor = ((sunlight + water + soil) / 300f).coerceIn(0.2f, 1f)
                val stemHeight = 60f + (growthFactor * 85f)
                val stemTopY = soilTop - stemHeight

                // Roots
                val rootLength = 15f + (soil / 100f) * 25f
                drawLine(
                    color = Color(0xFFD7CCC8),
                    start = Offset(plantBaseX, soilTop),
                    end = Offset(plantBaseX - 25f, soilTop + rootLength),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color(0xFFD7CCC8),
                    start = Offset(plantBaseX, soilTop),
                    end = Offset(plantBaseX + 25f, soilTop + rootLength + 4f),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color(0xFFD7CCC8),
                    start = Offset(plantBaseX, soilTop),
                    end = Offset(plantBaseX, soilTop + rootLength + 10f),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )

                // Stem
                drawLine(
                    color = stemColor,
                    start = Offset(plantBaseX, soilTop),
                    end = Offset(plantBaseX, stemTopY),
                    strokeWidth = 7f,
                    cap = StrokeCap.Round
                )

                // Leaves
                val leafColor = if (isWithering) Color(0xFFA1887F) else ScienceGreen
                drawOval(
                    color = leafColor,
                    topLeft = Offset(plantBaseX - 35f, stemTopY + 25f),
                    size = Size(35f, 16f)
                )
                drawOval(
                    color = leafColor,
                    topLeft = Offset(plantBaseX, stemTopY + 38f),
                    size = Size(35f, 16f)
                )

                // Flower
                if (isThriving && growthFactor > 0.55f) {
                    drawCircle(
                        color = Color(0xFFE91E63),
                        radius = 16f,
                        center = Offset(plantBaseX, stemTopY)
                    )
                    drawCircle(
                        color = ScienceYellow,
                        radius = 7f,
                        center = Offset(plantBaseX, stemTopY)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            ) {
                val conditionText = when {
                    water < 20f -> "🥀 Ewéko ń gbẹ! (Needs water!)"
                    sunlight < 20f -> "☁️ Àìsí oòrùn! (Pale leaves - Needs light!)"
                    water > 95f -> "💧 Omi pọ̀ jù! (Overwatered roots)"
                    else -> "🌸 Ewéko ń yọ̀! (Healthy Photosynthesis)"
                }
                Text(
                    text = conditionText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }

    // Sliders Card
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Ṣàkóso Àyíká (Adjust Environmental Factors):",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            // Sunlight
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("☀️ Oòrùn (Sunlight): ${sunlight.toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(if (sunlight > 50) "Bright" else "Dim", fontSize = 11.sp, color = TextMuted)
                }
                Slider(
                    value = sunlight,
                    onValueChange = onSunlightChange,
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(thumbColor = ScienceYellow, activeTrackColor = ScienceYellow)
                )
            }

            // Water
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("💧 Omi (Water): ${water.toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(if (water in 30f..85f) "Optimal" else if (water < 30) "Dry" else "Overwatered", fontSize = 11.sp, color = TextMuted)
                }
                Slider(
                    value = water,
                    onValueChange = onWaterChange,
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(thumbColor = ScienceBlue, activeTrackColor = ScienceBlue)
                )
            }

            // Soil
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("🌱 Ilẹ̀ Ọlọ́ràá (Soil Nutrients): ${soil.toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Rich earth", fontSize = 11.sp, color = TextMuted)
                }
                Slider(
                    value = soil,
                    onValueChange = onSoilChange,
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(thumbColor = ScienceGreen, activeTrackColor = ScienceGreen)
                )
            }
        }
    }

    // Science Principle Card
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ScienceGreenLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🔬", fontSize = 20.sp)
                    Text(
                        text = "Àkíyèsí Sáyẹ́ǹsì (Photosynthesis)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = ScienceGreen
                    )
                }

                IconButton(
                    onClick = {
                        val text = "Ewéko nílò ìmọ́lẹ̀ oòrùn àti omi láti se oúnjẹ nínú ewé rẹ̀. Èyí ni a ń pè ní Photosynthesis!"
                        onSpeak(text, true)
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = "Listen", tint = ScienceGreen, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            BilingualText(
                yorubaText = "Ewéko ń lo gbòǹgbò láti fa omi nínú ilẹ̀, ó sì ń lo ewé tútù rẹ̀ láti gba ìmọ́lẹ̀ oòrùn láti se oúnjẹ fúnra rẹ̀.",
                englishText = "Plants absorb water through roots and catch sunlight with green chlorophyll to produce glucose food (Photosynthesis).",
                languageMode = currentLanguageMode,
                yorubaStyle = MaterialTheme.typography.bodySmall,
                englishStyle = MaterialTheme.typography.bodySmall,
                showBadges = false
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onAwardStars,
                colors = ButtonDefaults.buttonColors(containerColor = if (starsAwarded) ScienceGreen else WebForestGreenDark),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_plant_earn_stars")
            ) {
                Icon(Icons.Default.Star, contentDescription = null, tint = ScienceYellow)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (starsAwarded) "O Gbà 3 Ìràwọ̀ Ewéko! (3 Stars Earned!) ⭐" else "Ṣàwari Ìdàgbàsókè (Claim Plant Stars ⭐)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// -------------------------------------------------------------
// WATER CYCLE SIMULATION COMPONENT
// -------------------------------------------------------------
@Composable
fun WaterCycleSimulation(
    heat: Float,
    cooling: Float,
    isRaining: Boolean,
    currentLanguageMode: LanguageMode,
    onHeatChange: (Float) -> Unit,
    onCoolingChange: (Float) -> Unit,
    onToggleRain: () -> Unit,
    onSpeak: (String, Boolean) -> Unit,
    starsAwarded: Boolean,
    onAwardStars: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .testTag("water_cycle_canvas")
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Ocean water
                val waterTop = canvasHeight * 0.72f
                drawRect(
                    color = Color(0xFF0288D1),
                    topLeft = Offset(0f, waterTop),
                    size = Size(canvasWidth, canvasHeight - waterTop)
                )

                // Sun
                val sunRadius = 18f + (heat / 100f) * 15f
                drawCircle(
                    color = ScienceYellow,
                    radius = sunRadius,
                    center = Offset(canvasWidth * 0.85f, canvasHeight * 0.2f)
                )

                // Clouds
                val cloudAlpha = (cooling / 100f).coerceIn(0.3f, 1f)
                val cloudColor = if (isRaining) Color(0xFF546E7A) else Color(0xFFB0BEC5)
                val cloudCenter = Offset(canvasWidth * 0.35f, canvasHeight * 0.25f)
                drawCircle(color = cloudColor.copy(alpha = cloudAlpha), radius = 26f, center = cloudCenter)
                drawCircle(color = cloudColor.copy(alpha = cloudAlpha), radius = 22f, center = Offset(cloudCenter.x - 22f, cloudCenter.y + 6f))
                drawCircle(color = cloudColor.copy(alpha = cloudAlpha), radius = 22f, center = Offset(cloudCenter.x + 22f, cloudCenter.y + 6f))

                // Evaporation arrows rising
                if (heat > 40f) {
                    for (i in 0..3) {
                        val startX = canvasWidth * 0.65f + (i * 20f)
                        drawLine(
                            color = Color(0xFF4FC3F7).copy(alpha = 0.7f),
                            start = Offset(startX, waterTop - 5f),
                            end = Offset(startX - 15f, waterTop - 45f),
                            strokeWidth = 3f,
                            cap = StrokeCap.Round
                        )
                    }
                }

                // Rain drops falling
                if (isRaining) {
                    for (i in 0..6) {
                        val dropX = cloudCenter.x - 30f + (i * 12f)
                        val dropY = cloudCenter.y + 35f + (i % 3 * 15f)
                        drawLine(
                            color = Color(0xFF0277BD),
                            start = Offset(dropX, dropY),
                            end = Offset(dropX, dropY + 12f),
                            strokeWidth = 3f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            ) {
                Text(
                    text = if (isRaining) "🌧️ Òjò ń rọ̀! (Precipitation active!)" else "☁️ Evaporation & Condensation",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }

    // Controls
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Ṣàkóso Ooru àti Òjò (Adjust Heat & Cooling):",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            // Heat
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("☀️ Ooru Oòrùn (Solar Heat): ${heat.toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Drives Evaporation", fontSize = 11.sp, color = TextMuted)
                }
                Slider(
                    value = heat,
                    onValueChange = onHeatChange,
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(thumbColor = ScienceYellow, activeTrackColor = ScienceYellow)
                )
            }

            // Cooling
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("❄️ Ìtútù Àwọsánmọ̀ (Atmospheric Cooling): ${cooling.toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Condenses clouds", fontSize = 11.sp, color = TextMuted)
                }
                Slider(
                    value = cooling,
                    onValueChange = onCoolingChange,
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(thumbColor = ScienceBlue, activeTrackColor = ScienceBlue)
                )
            }

            Button(
                onClick = onToggleRain,
                colors = ButtonDefaults.buttonColors(containerColor = if (isRaining) ScienceCoral else ScienceBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().testTag("btn_toggle_rain")
            ) {
                Text(
                    text = if (isRaining) "Dá Òjò Dúró (Stop Rain)" else "Rọ̀ Òjò Sáyẹ́ǹsì (Trigger Rainfall 🌧️)",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // Science Principle Card
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ScienceBlueLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🌧️", fontSize = 20.sp)
                    Text(
                        text = "Àkíyèsí: Ìyípo Omi (The Water Cycle)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = ScienceBlue
                    )
                }

                IconButton(
                    onClick = {
                        val text = "Ooru oòrùn ń mu omi gbẹ di oru, oru ń di àwọsánmọ̀, àwọsánmọ̀ sì ń rọ̀ di òjò padà sí ilẹ̀."
                        onSpeak(text, true)
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = "Listen", tint = ScienceBlue, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            BilingualText(
                yorubaText = "Ooru ń mú omi gbẹ sókè (evaporation), ó ń di àwọsánmọ̀ nínú òtútù (condensation), ó sì ń rọ̀ bí òjò (precipitation).",
                englishText = "Water evaporates from lakes, condenses into clouds in cool air, and falls back to earth as precipitation.",
                languageMode = currentLanguageMode,
                yorubaStyle = MaterialTheme.typography.bodySmall,
                englishStyle = MaterialTheme.typography.bodySmall,
                showBadges = false
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onAwardStars,
                colors = ButtonDefaults.buttonColors(containerColor = if (starsAwarded) ScienceGreen else WebForestGreenDark),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_water_earn_stars")
            ) {
                Icon(Icons.Default.Star, contentDescription = null, tint = ScienceYellow)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (starsAwarded) "O Gbà 3 Ìràwọ̀ Omi! (3 Stars Earned!) ⭐" else "Ṣàwari Ìyípo Omi (Claim Water Stars ⭐)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// -------------------------------------------------------------
// HUMAN SENSES EXPLORER COMPONENT
// -------------------------------------------------------------
data class SenseSpecimen(
    val emoji: String,
    val nameYo: String,
    val nameEn: String,
    val correctSenseYo: String,
    val correctSenseEn: String,
    val senseEmoji: String,
    val descYo: String,
    val descEn: String
)

@Composable
fun HumanSensesSimulation(
    currentLanguageMode: LanguageMode,
    onSpeak: (String, Boolean) -> Unit
) {
    val specimens = remember {
        listOf(
            SenseSpecimen("🌺", "Òdòdó", "Sweet Flower", "Imú (Nose)", "Nose for smelling", "👃", "Imú ló ń gbọ́ òórùn dídùn òdòdó.", "Our nose smells the sweet fragrance of the flower."),
            SenseSpecimen("🔔", "Agogo", "School Bell", "Etí (Ears)", "Ears for hearing", "👂", "Etí ló ń gbọ́ ìró agogo tó ń dún.", "Our ears hear the loud chime of the school bell."),
            SenseSpecimen("🍯", "Oyin", "Pure Honey", "Ahọ́n (Tongue)", "Tongue for tasting", "👅", "Ahọ́n ló ń tọ́ adùn oyin wò.", "Our tongue tastes the rich sweetness of honey."),
            SenseSpecimen("🧊", "Yìnyín", "Cold Ice Cube", "Awọ (Skin)", "Skin for feeling", "✋", "Awọ-ara ló ń mọ̀ bí yìnyín ṣe tutù tó.", "Our skin feels the cold freezing temperature of ice."),
            SenseSpecimen("🌈", "Òṣùmàrè", "Rainbow", "Ojú (Eyes)", "Eyes for seeing", "👀", "Ojú ló ń rí àwọn àwọ̀ méje aláràbarà.", "Our eyes see the seven brilliant colors of the rainbow.")
        )
    }

    var selectedIndex by remember { mutableIntStateOf(0) }
    val item = specimens[selectedIndex]

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Yan Ohun Àyíká (Select Sensory Specimen):",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(specimens.indices.toList()) { idx ->
                    val spec = specimens[idx]
                    val isSel = idx == selectedIndex
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSel) ScienceGreenLight else BackgroundLight,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) ScienceGreen else WebBorder),
                        modifier = Modifier.clickable { selectedIndex = idx }
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(spec.emoji, fontSize = 24.sp)
                            Text(
                                text = if (currentLanguageMode == LanguageMode.YORUBA) spec.nameYo else spec.nameEn,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Active specimen card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = BackgroundLight,
                modifier = Modifier.fillMaxWidth()
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
                            Text(item.emoji, fontSize = 32.sp)
                            Column {
                                Text(
                                    text = "${item.nameYo} (${item.nameEn})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Ẹ̀yà Ìfòye: ${item.senseEmoji} ${item.correctSenseYo}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = ScienceGreen
                                )
                            }
                        }

                        IconButton(
                            onClick = { onSpeak("${item.nameYo}. ${item.descYo}", true) }
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = "Listen", tint = ScienceGreen)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    BilingualText(
                        yorubaText = item.descYo,
                        englishText = item.descEn,
                        languageMode = currentLanguageMode,
                        yorubaStyle = MaterialTheme.typography.bodySmall,
                        englishStyle = MaterialTheme.typography.bodySmall,
                        showBadges = false
                    )
                }
            }
        }
    }
}
