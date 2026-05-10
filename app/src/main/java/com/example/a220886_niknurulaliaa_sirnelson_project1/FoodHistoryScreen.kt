package com.example.a220886_niknurulaliaa_sirnelson_project1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodHistoryScreen(viewModel: SwiftBiteViewModel) {

    // ✅ 3. State untuk Search & Filter
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val filterOptions = listOf("All", "Breakfast", "Lunch", "Dinner")

    // ✅ Filter dan search logic
    val filteredList = viewModel.foodLog.filter { food ->
        val matchSearch = food.name.contains(searchQuery, ignoreCase = true)
        val matchFilter = selectedFilter == "All" || food.mealType == selectedFilter
        matchSearch && matchFilter
    }

    // Kategori ikut meal type dari filtered list
    val breakfast = filteredList.filter { it.mealType == "Breakfast" }
    val lunch = filteredList.filter { it.mealType == "Lunch" }
    val dinner = filteredList.filter { it.mealType == "Dinner" }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Food History", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // ✅ Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search food...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            // ✅ Filter Chips — All / Breakfast / Lunch / Dinner
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                filterOptions.forEach { option ->
                    FilterChip(
                        selected = selectedFilter == option,
                        onClick = { selectedFilter = option },
                        label = {
                            Text(
                                when (option) {
                                    "All"       -> "🍽️ All"
                                    "Breakfast" -> "🌞 Breakfast"
                                    "Lunch"     -> "🌤 Lunch"
                                    "Dinner"    -> "🌙 Dinner"
                                    else        -> option
                                },
                                fontSize = 12.sp
                            )
                        }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            if (viewModel.foodLog.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No food logged yet 🍽️")
                }
            } else if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No results found for \"$searchQuery\"")
                }
            } else {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    // Tunjuk ikut filter yang dipilih
                    if (selectedFilter == "All" || selectedFilter == "Breakfast") {
                        MealSection("🌞 Breakfast", breakfast)
                    }
                    if (selectedFilter == "All" || selectedFilter == "Lunch") {
                        MealSection("🌤 Lunch", lunch)
                    }
                    if (selectedFilter == "All" || selectedFilter == "Dinner") {
                        MealSection("🌙 Dinner", dinner)
                    }

                    // Summary bawah sekali
                    if (filteredList.isNotEmpty()) {
                        Spacer(Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Total: ${filteredList.size} meals",
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    "${filteredList.sumOf { it.calories }} kcal",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MealSection(title: String, foods: List<FoodItem>) {
    if (foods.isNotEmpty()) {
        Text(title, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        foods.forEach { food ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(food.name)
                    Text(
                        "${food.calories} kcal",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}
