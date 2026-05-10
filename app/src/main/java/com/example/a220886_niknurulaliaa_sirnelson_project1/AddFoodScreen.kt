package com.example.a220886_niknurulaliaa_sirnelson_project1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(
    viewModel: SwiftBiteViewModel,
    snackbarHostState: SnackbarHostState, // Ditambah untuk notification
    onNavigateBack: () -> Unit
) {
    var foodName by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope() // Untuk launch Snackbar

    // State untuk Dropdown Meal Time
    var mealType by remember { mutableStateOf("Breakfast") }
    var expanded by remember { mutableStateOf(false) }
    val mealOptions = listOf("Breakfast", "Lunch", "Dinner")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Log Meal", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ikon Visual
            Text("🍱", fontSize = 60.sp)
            Spacer(Modifier.height(16.dp))

            Text(
                "What did you eat?",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(24.dp))

            // Input Nama Makanan
            OutlinedTextField(
                value = foodName,
                onValueChange = { foodName = it },
                label = { Text("Food Name") },
                placeholder = { Text("e.g. Chicken Rice") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            // Input Kalori (Keyboard nombor)
            OutlinedTextField(
                value = calories,
                onValueChange = { calories = it },
                label = { Text("Calories (kcal)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            // Dropdown Meal Time
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = mealType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Meal Time") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    mealOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                mealType = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // Butang Save dengan Logik Gabungan
            Button(
                onClick = {
                    val calValue = calories.toIntOrNull()

                    // Logik Notification: Jika salah satu kosong atau kalori bukan nombor
                    if (foodName.isBlank() || calValue == null) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please enter a valid food name and calories!")
                        }
                    } else {
                        // Jika semua OK, simpan dengan mealType dan balik ke Dashboard
                        viewModel.addFood(foodName, calValue, mealType)
                        onNavigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("SAVE MEAL", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
