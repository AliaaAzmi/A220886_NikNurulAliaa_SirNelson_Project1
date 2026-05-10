package com.example.a220886_niknurulaliaa_sirnelson_project1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.a220886_niknurulaliaa_sirnelson_project1.ui.theme.A220886_NikNurulAliaa_SirNelson_Project1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: SwiftBiteViewModel = viewModel()

            // ✅ 2. Dark Mode — ikut state dalam ViewModel
            A220886_NikNurulAliaa_SirNelson_Project1Theme(
                darkTheme = viewModel.isDarkMode
            ) {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        if (currentRoute != "setup") {
                            BottomMenu(navController)
                        }
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (viewModel.isProfileSetup) "dashboard" else "setup",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("setup") {
                            SetupScreen(
                                viewModel = viewModel,
                                onSetupComplete = {
                                    navController.navigate("dashboard") {
                                        popUpTo("setup") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("dashboard") {
                            DashboardScreen(
                                viewModel = viewModel,
                                onNavigateToAddFood = { navController.navigate("add_food") },
                                onNavigateToDetail = { index ->
                                    navController.navigate("food_detail/$index")
                                }
                            )
                        }
                        composable("add_food") {
                            AddFoodScreen(
                                viewModel = viewModel,
                                snackbarHostState = snackbarHostState,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable("profile") {
                            ProfileScreen(viewModel = viewModel)
                        }
                        composable("history") {
                            FoodHistoryScreen(viewModel)
                        }
                        composable("food_detail/{index}") { backStackEntry ->
                            val index = backStackEntry.arguments?.getString("index")!!.toInt()
                            FoodDetailScreen(food = viewModel.foodLog[index])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(
    viewModel: SwiftBiteViewModel,
    onNavigateToAddFood: () -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    val totalCalories = viewModel.getTotalCalories()
    val calorieGoal = viewModel.calorieGoal
    val scrollState = rememberScrollState()

    // ✅ 7. Animated progress untuk circular dan linear
    val progress by animateFloatAsState(
        targetValue = (totalCalories.toFloat() / calorieGoal).coerceIn(0f, 1f),
        label = "CalorieProgress"
    )

    // ✅ 4. Warna progress bar — hijau ok, merah lebih
    val progressColor = when {
        progress >= 1f   -> Color(0xFFE53935) // merah
        progress >= 0.8f -> Color(0xFFFFA726) // oren
        else             -> Color(0xFF43A047) // hijau
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // 1. HEADER CARD
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .size(50.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👤", fontSize = 24.sp)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "Hello, ${viewModel.userName}!",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text("Matrik: A220886", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        "🔥 Active",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ✅ 1. QUOTE CARD — Quote bertukar-tukar dari ViewModel
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("💡", fontSize = 24.sp)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Quote of the Day",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    // ✅ Guna dailyQuote dari ViewModel — bertukar setiap app dibuka
                    Text(
                        viewModel.dailyQuote,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ✅ 7. MAIN CALORIE CARD — Circular Progress Bar
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Daily Calorie Goal",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Text(
                        "Goal: ${viewModel.weightGoal}",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )

                    Spacer(Modifier.height(20.dp))

                    // ✅ Circular Progress Bar
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(160.dp)
                    ) {
                        // Track — latar belakang bulatan
                        CircularProgressIndicator(
                            progress = { 1f },
                            modifier = Modifier.fillMaxSize(),
                            color = Color.White.copy(alpha = 0.2f),
                            strokeWidth = 14.dp
                        )
                        // Progress sebenar — bertukar merah kalau lebih goal
                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxSize(),
                            color = if (progress >= 1f) Color(0xFFFF6B6B) else Color.White,
                            strokeWidth = 14.dp
                        )
                        // Text dalam bulatan
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "$totalCalories",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                "/ $calorieGoal",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Text(
                                "kcal",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ✅ Dynamic Macros ikut weightGoal
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MacroItem("🥩 Protein", "${viewModel.getProteinGrams()}g")
                        MacroItem("🍞 Carbs", "${viewModel.getCarbsGrams()}g")
                        MacroItem("🥑 Fat", "${viewModel.getFatGrams()}g")
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        when (viewModel.weightGoal) {
                            "Lose Weight"     -> "Protein 35% • Carbs 35% • Fat 30%"
                            "Maintain Weight" -> "Protein 25% • Carbs 50% • Fat 25%"
                            "Gain Weight"     -> "Protein 30% • Carbs 50% • Fat 20%"
                            else              -> ""
                        },
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ✅ 4 & 7. NUTRITION PROGRESS — Linear Progress Bar warna bertukar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Nutrition Progress",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "${(progress * 100).toInt()}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = progressColor
                )
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp),
                // ✅ Warna bertukar ikut progress — hijau, oren, merah
                color = progressColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            // ✅ Status text bertukar ikut progress
            Text(
                when {
                    progress >= 1f   -> "⚠️ Daily goal reached!"
                    progress >= 0.8f -> "Almost there, keep going!"
                    progress >= 0.5f -> "Halfway through your goal"
                    else             -> "Keep tracking your meals"
                },
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(Modifier.height(20.dp))

            // ✅ 5. WATER INTAKE TRACKER
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "💧 Water Intake",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            "${viewModel.waterIntake} / ${viewModel.waterGoalCount} glasses",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Ikon gelas air
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        (1..viewModel.waterGoalCount).forEach { i ->
                            Text(
                                if (i <= viewModel.waterIntake) "🥤" else "⬜",
                                fontSize = 20.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // Button + dan -
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.removeWater() },
                            modifier = Modifier.size(48.dp),
                            contentPadding = PaddingValues(0.dp),
                            shape = CircleShape
                        ) {
                            Text("−", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(Modifier.width(24.dp))

                        Text(
                            "${viewModel.waterIntake}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )

                        Spacer(Modifier.width(24.dp))

                        Button(
                            onClick = { viewModel.addWater() },
                            modifier = Modifier.size(48.dp),
                            contentPadding = PaddingValues(0.dp),
                            shape = CircleShape
                        ) {
                            Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // 5. TODAY'S LOG (EXPANDABLE)
            Text("Today's Food Log", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            if (viewModel.foodLog.isEmpty()) {
                Text(
                    "No food added yet.",
                    color = Color.Gray,
                    modifier = Modifier.padding(8.dp)
                )
            } else {
                viewModel.foodLog.forEachIndexed { index, food ->
                    var isExpanded by remember { mutableStateOf(false) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .animateContentSize(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        onClick = { isExpanded = !isExpanded }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = food.name, fontWeight = FontWeight.Bold)
                                TextButton(
                                    onClick = { onNavigateToDetail(index) },
                                    contentPadding = PaddingValues(
                                        horizontal = 8.dp,
                                        vertical = 0.dp
                                    )
                                ) {
                                    Text("Details →", fontSize = 12.sp)
                                }
                            }

                            if (isExpanded) {
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "Calories: ${food.calories} kcal",
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "Meal Time: ${food.mealType}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    "Status: Recorded",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            } else {
                                Text(
                                    "${food.calories} kcal • ${food.mealType}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(100.dp))
        }

        FloatingActionButton(
            onClick = onNavigateToAddFood,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}

@Composable
fun MacroItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
        Text(value, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BottomMenu(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, "Home") },
            label = { Text("Home") },
            selected = currentRoute == "dashboard",
            onClick = { if (currentRoute != "dashboard") navController.navigate("dashboard") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, "History") },
            label = { Text("History") },
            selected = currentRoute == "history",
            onClick = { if (currentRoute != "history") navController.navigate("history") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, "Profile") },
            label = { Text("Profile") },
            selected = currentRoute == "profile",
            onClick = { if (currentRoute != "profile") navController.navigate("profile") }
        )
    }
}
