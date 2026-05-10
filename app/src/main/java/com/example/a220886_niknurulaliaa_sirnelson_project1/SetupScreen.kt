package com.example.a220886_niknurulaliaa_sirnelson_project1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    viewModel: SwiftBiteViewModel,
    onSetupComplete: () -> Unit
) {
    // ✅ State semua input
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    // ✅ State untuk step wizard (4 langkah)
    var currentStep by remember { mutableStateOf(1) }

    // ✅ State dropdown
    var genderExpanded by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("") }

    var activityExpanded by remember { mutableStateOf(false) }
    var selectedActivity by remember { mutableStateOf("") }

    var goalExpanded by remember { mutableStateOf(false) }
    var selectedGoal by remember { mutableStateOf("") }

    // ✅ State untuk error message
    var errorMessage by remember { mutableStateOf("") }

    val genderOptions = listOf("Female", "Male")
    val activityOptions = listOf("Sedentary", "Lightly Active", "Moderately Active", "Very Active")
    val goalOptions = listOf("Lose Weight", "Maintain Weight", "Gain Weight")
    val scrollState = rememberScrollState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // ── Header ───────────────────────────────────────────────────
            Text("🏃 SwiftBite Setup", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(
                "Let's personalise your calorie goal!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            // ── Step Indicator ───────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                (1..4).forEach { step ->
                    val isActive = step == currentStep
                    val isDone = step < currentStep
                    Surface(
                        modifier = Modifier
                            .size(if (isActive) 36.dp else 28.dp)
                            .padding(2.dp),
                        shape = RoundedCornerShape(50),
                        color = when {
                            isDone   -> MaterialTheme.colorScheme.primary
                            isActive -> MaterialTheme.colorScheme.primaryContainer
                            else     -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                if (isDone) "✓" else "$step",
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp,
                                color = if (isDone || isActive)
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    if (step < 4) {
                        HorizontalDivider(
                            modifier = Modifier
                                .width(32.dp)
                                .padding(top = 14.dp),
                            color = if (isDone) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // ── Step Content ─────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (currentStep) {

                        // ── STEP 1: Nama & Gender ─────────────────────────
                        1 -> {
                            Text("👤 Personal Info", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(Modifier.height(20.dp))

                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Your Name") },
                                placeholder = { Text("e.g. Aliaa") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            Spacer(Modifier.height(16.dp))

                            // Dropdown Gender
                            ExposedDropdownMenuBox(
                                expanded = genderExpanded,
                                onExpandedChange = { genderExpanded = !genderExpanded }
                            ) {
                                OutlinedTextField(
                                    value = selectedGender,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Gender") },
                                    placeholder = { Text("Select gender") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(genderExpanded)
                                    },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = genderExpanded,
                                    onDismissRequest = { genderExpanded = false }
                                ) {
                                    genderOptions.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(option) },
                                            onClick = {
                                                selectedGender = option
                                                genderExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // ── STEP 2: Umur, Berat, Tinggi ──────────────────
                        2 -> {
                            Text("📏 Body Measurements", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(Modifier.height(20.dp))

                            OutlinedTextField(
                                value = age,
                                onValueChange = { age = it },
                                label = { Text("Age") },
                                placeholder = { Text("e.g. 21") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            Spacer(Modifier.height(16.dp))

                            OutlinedTextField(
                                value = weight,
                                onValueChange = { weight = it },
                                label = { Text("Weight (kg)") },
                                placeholder = { Text("e.g. 55") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            Spacer(Modifier.height(16.dp))

                            OutlinedTextField(
                                value = height,
                                onValueChange = { height = it },
                                label = { Text("Height (cm)") },
                                placeholder = { Text("e.g. 160") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }

                        // ── STEP 3: Activity Level ────────────────────────
                        3 -> {
                            Text("🏃 Activity Level", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "How active are you on a weekly basis?",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(20.dp))

                            activityOptions.forEach { option ->
                                val isSelected = selectedActivity == option
                                val description = when (option) {
                                    "Sedentary"         -> "Little or no exercise"
                                    "Lightly Active"    -> "Exercise 1–3x per week"
                                    "Moderately Active" -> "Exercise 3–5x per week"
                                    "Very Active"       -> "Exercise 6–7x per week"
                                    else -> ""
                                }
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else
                                            MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    onClick = { selectedActivity = option }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { selectedActivity = option }
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Column {
                                            Text(option, fontWeight = FontWeight.SemiBold)
                                            Text(
                                                description,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // ── STEP 4: Weight Goal ───────────────────────────
                        4 -> {
                            Text("🎯 Your Goal", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "What do you want to achieve?",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(20.dp))

                            val goalEmojis = mapOf(
                                "Lose Weight"     to "⬇️",
                                "Maintain Weight" to "⚖️",
                                "Gain Weight"     to "⬆️"
                            )
                            val goalDesc = mapOf(
                                "Lose Weight"     to "Calorie deficit –500 kcal",
                                "Maintain Weight" to "Stay at maintenance calories",
                                "Gain Weight"     to "Calorie surplus +500 kcal"
                            )

                            goalOptions.forEach { option ->
                                val isSelected = selectedGoal == option
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else
                                            MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    onClick = { selectedGoal = option }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(goalEmojis[option] ?: "", fontSize = 24.sp)
                                        Spacer(Modifier.width(12.dp))
                                        Column {
                                            Text(option, fontWeight = FontWeight.SemiBold)
                                            Text(
                                                goalDesc[option] ?: "",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        Spacer(Modifier.weight(1f))
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { selectedGoal = option }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ── Error Message ─────────────────────────────────────────────
            if (errorMessage.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Navigation Buttons ────────────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth()) {
                // Back button
                if (currentStep > 1) {
                    OutlinedButton(
                        onClick = {
                            errorMessage = ""
                            currentStep--
                        },
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("← Back")
                    }
                    Spacer(Modifier.width(12.dp))
                }

                // Next / Finish button
                Button(
                    onClick = {
                        errorMessage = ""
                        when (currentStep) {
                            1 -> {
                                if (name.isBlank() || selectedGender.isEmpty()) {
                                    errorMessage = "Please fill in your name and gender."
                                } else {
                                    currentStep++
                                }
                            }
                            2 -> {
                                val a = age.toIntOrNull()
                                val w = weight.toFloatOrNull()
                                val h = height.toFloatOrNull()
                                if (a == null || w == null || h == null || a <= 0 || w <= 0 || h <= 0) {
                                    errorMessage = "Please enter valid age, weight and height."
                                } else {
                                    currentStep++
                                }
                            }
                            3 -> {
                                if (selectedActivity.isEmpty()) {
                                    errorMessage = "Please select your activity level."
                                } else {
                                    currentStep++
                                }
                            }
                            4 -> {
                                if (selectedGoal.isEmpty()) {
                                    errorMessage = "Please select your goal."
                                } else {
                                    // ✅ Simpan semua data ke ViewModel
                                    viewModel.userName = name
                                    viewModel.gender = selectedGender
                                    viewModel.age = age.toInt()
                                    viewModel.weight = weight.toFloat()
                                    viewModel.height = height.toFloat()
                                    viewModel.activityLevel = selectedActivity
                                    viewModel.weightGoal = selectedGoal

                                    // ✅ Kira calorie goal berdasarkan formula
                                    viewModel.calculateCalorieGoal()

                                    // ✅ Mark setup selesai → pergi Dashboard
                                    viewModel.isProfileSetup = true
                                    onSetupComplete()
                                }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        if (currentStep == 4) "✅ Let's Go!" else "Next →",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}