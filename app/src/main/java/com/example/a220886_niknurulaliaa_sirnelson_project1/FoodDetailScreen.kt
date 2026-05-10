package com.example.a220886_niknurulaliaa_sirnelson_project1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(food: FoodItem) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Food Detail", fontWeight = FontWeight.Bold) },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // Ikon makanan
            Text("🍽️", fontSize = 80.sp)
            Spacer(Modifier.height(16.dp))

            // Nama makanan
            Text(
                food.name,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(8.dp))

            // Kalori
            Text(
                "${food.calories} kcal",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(8.dp))

            // Masa makan badge
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    when (food.mealType) {
                        "Breakfast" -> "🌞 Breakfast"
                        "Lunch"     -> "🌤 Lunch"
                        "Dinner"    -> "🌙 Dinner"
                        else        -> food.mealType
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(32.dp))

            // Detail Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    // Row: Food Name
                    DetailRow(label = "Food Name", value = food.name)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // Row: Calories
                    DetailRow(label = "Calories", value = "${food.calories} kcal")

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // Row: Meal Time
                    DetailRow(label = "Meal Time", value = food.mealType)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // Row: Status
                    DetailRow(label = "Status", value = "Logged Successfully ✅")
                }
            }
        }
    }
}

// ✅ Reusable row untuk detail
@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            value,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
