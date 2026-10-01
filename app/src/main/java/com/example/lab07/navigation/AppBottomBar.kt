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
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

data class BottomNavItem (
    val title: String,
    val icon: ImageVector,
    val route: Any,
    val matchWord: String
)

@Composable
fun AppBottomBar(
    currentRoute: NavDestination?,
    onNavigate: (Any) -> Unit
) {
    val items = listOf(
        BottomNavItem("Characters", Icons.Filled.People, CharactersGraph, "Characters"),
        BottomNavItem("Locations", Icons.Filled.LocationOn, LocationsGraph, "Locations"),
        BottomNavItem("Profile", Icons.Filled.Person, ProfileDestination, "Profile")
    )

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentRoute?.hierarchy?.any{
                it.route?.contains(item.matchWord, ignoreCase = true) == true
            } == true

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