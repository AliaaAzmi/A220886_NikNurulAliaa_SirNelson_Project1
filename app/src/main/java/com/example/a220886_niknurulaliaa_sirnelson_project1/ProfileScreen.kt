package com.example.a220886_niknurulaliaa_sirnelson_project1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: SwiftBiteViewModel) {
    val scrollState = rememberScrollState()

    var showEditNameDialog by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf(viewModel.userName) }

    var showEditCalorieDialog by remember { mutableStateOf(false) }
    var tempCalorie by remember { mutableStateOf(viewModel.calorieGoal.toString()) }

    var showEditBMIDialog by remember { mutableStateOf(false) }
    var tempWeight by remember { mutableStateOf(viewModel.weight.toString()) }
    var tempHeight by remember { mutableStateOf(viewModel.height.toString()) }

    // ✅ Dialog: Edit Nama
    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            title = { Text("Edit Name") },
            text = {
                OutlinedTextField(
                    value = tempName,
                    onValueChange = { tempName = it },
                    label = { Text("Your Name") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (tempName.isNotBlank()) viewModel.userName = tempName
                    showEditNameDialog = false
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showEditNameDialog = false }) { Text("Cancel") }
            }
        )
    }

    // ✅ Dialog: Edit Calorie Goal
    if (showEditCalorieDialog) {
        AlertDialog(
            onDismissRequest = { showEditCalorieDialog = false },
            title = { Text("Edit Calorie Goal") },
            text = {
                OutlinedTextField(
                    value = tempCalorie,
                    onValueChange = { tempCalorie = it },
                    label = { Text("Calorie Goal (kcal)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val value = tempCalorie.toIntOrNull()
                    if (value != null && value > 0) viewModel.calorieGoal = value
                    showEditCalorieDialog = false
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showEditCalorieDialog = false }) { Text("Cancel") }
            }
        )
    }

    // ✅ Dialog: Edit Weight & Height
    if (showEditBMIDialog) {
        AlertDialog(
            onDismissRequest = { showEditBMIDialog = false },
            title = { Text("Edit Weight & Height") },
            text = {
                Column {
                    OutlinedTextField(
                        value = tempWeight,
                        onValueChange = { tempWeight = it },
                        label = { Text("Weight (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = tempHeight,
                        onValueChange = { tempHeight = it },
                        label = { Text("Height (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val w = tempWeight.toFloatOrNull()
                    val h = tempHeight.toFloatOrNull()
                    if (w != null && w > 0) viewModel.weight = w
                    if (h != null && h > 0) viewModel.height = h
                    showEditBMIDialog = false
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showEditBMIDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Profile", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Section 1: Gambar Profil & Nama ──────────────────────────
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("👤", fontSize = 50.sp)
            }

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    viewModel.userName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        tempName = viewModel.userName
                        showEditNameDialog = true
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Name",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Text("A220886", color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(Modifier.height(24.dp))

            // ── Section 2: Health Stats ───────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Health Stats", fontWeight = FontWeight.Bold)
                        IconButton(
                            onClick = {
                                tempWeight = viewModel.weight.toString()
                                tempHeight = viewModel.height.toString()
                                showEditBMIDialog = true
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit Stats",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileStatItem("Weight", "${viewModel.weight.toInt()} kg")
                        VerticalDivider(
                            modifier = Modifier.height(30.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        ProfileStatItem("Height", "${viewModel.height.toInt()} cm")
                        VerticalDivider(
                            modifier = Modifier.height(30.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        ProfileStatItem("Age", "${viewModel.age}")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Section 3: BMI Calculator ─────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("⚖️ BMI Calculator", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "%.1f".format(viewModel.getBMI()),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        viewModel.getBMIStatus(),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Based on ${viewModel.weight.toInt()} kg / ${viewModel.height.toInt()} cm",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Section 4: Today's Food Summary ──────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "🍽️ Today's Summary",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ProfileStatItem("Total", "${viewModel.getTotalFoodCount()}")
                        VerticalDivider(
                            modifier = Modifier.height(30.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        ProfileStatItem("Breakfast", "${viewModel.getFoodCountByMeal("Breakfast")}")
                        VerticalDivider(
                            modifier = Modifier.height(30.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        ProfileStatItem("Lunch", "${viewModel.getFoodCountByMeal("Lunch")}")
                        VerticalDivider(
                            modifier = Modifier.height(30.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        ProfileStatItem("Dinner", "${viewModel.getFoodCountByMeal("Dinner")}")
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Total Calories: ${viewModel.getTotalCalories()} / ${viewModel.calorieGoal} kcal",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Section 5: My Goals ───────────────────────────────────────
            Text(
                "My Goals",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(12.dp))

            // Calorie Goal — boleh diedit
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Daily Calorie Goal",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "${viewModel.calorieGoal} kcal",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = {
                            tempCalorie = viewModel.calorieGoal.toString()
                            showEditCalorieDialog = true
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Calorie Goal",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            GoalItem("Step Goal", viewModel.stepGoal)
            GoalItem("Water Intake Goal", viewModel.waterGoal)

            Spacer(Modifier.height(16.dp))

            // ── Section 6: Dark Mode Toggle ───────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            if (viewModel.isDarkMode) "🌙 Dark Mode" else "☀️ Light Mode",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            "Toggle app appearance",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    // ✅ 2. Dark Mode Switch
                    Switch(
                        checked = viewModel.isDarkMode,
                        onCheckedChange = { viewModel.isDarkMode = it }
                    )
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
fun ProfileStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun GoalItem(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
