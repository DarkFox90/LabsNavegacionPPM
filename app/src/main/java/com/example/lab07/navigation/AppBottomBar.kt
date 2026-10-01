package com.example.lab07.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight

data class BottomNavItem (
    val title: String,
    val icon: ImageVector,
    val route: Any
)

@Composable
fun AppBottomBar(
    currentRoute: String?,
    onNavigate: (Any) -> Unit
) {
    val items = listOf(
        BottomNavItem("Characters", Icons.Filled.People, CharactersGraph),
        BottomNavItem("Locations", Icons.Filled.LocationOn, LocationsGraph),
        BottomNavItem("Profile", Icons.Filled.Person, ProfileDestination)
    )

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentRoute?.contains(item.route::class.simpleName ?: "") == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onNavigate(item.route)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }
    }
}