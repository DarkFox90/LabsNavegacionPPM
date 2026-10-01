package com.example.lab07

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab07.ui.theme.Lab07Theme
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.lab07.characters.ScreenDetailsDestination
import com.example.lab07.characters.ScreenHomeDestination
import com.example.lab07.locations.LocationDetailsScreen
import com.example.lab07.locations.LocationsScreen
import com.example.lab07.login.ScreenLoginDestination
import com.example.lab07.navigation.AppBottomBar
import com.example.lab07.navigation.CharacterDetailsDestination
import com.example.lab07.navigation.CharactersGraph
import com.example.lab07.navigation.CharactersListDestination
import com.example.lab07.navigation.LocationDetailsDestination
import com.example.lab07.navigation.LocationsGraph
import com.example.lab07.navigation.LocationsListDestination
import com.example.lab07.navigation.LoginDestination
import com.example.lab07.profile.ProfileScreen
import com.example.lab07.navigation.ProfileDestination
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab07Theme {
                AppNavigation()
            }
        }
    }
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val showBottomBar = currentDestination?.contains(LoginDestination::class.simpleName?: "") == false

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(
                    currentRoute = currentDestination,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(CharactersGraph) { saveState = true}
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LoginDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable<LoginDestination> {
                ScreenLoginDestination(
                    onNavigateToHome = {
                        navController.navigate(CharactersGraph) {
                            popUpTo<LoginDestination> { inclusive = true }
                        }
                    }
                )
            }

            navigation<CharactersGraph>(startDestination = CharactersListDestination) {
                composable<CharactersListDestination> {
                    ScreenHomeDestination(
                        onNavigateToDetails = { id ->
                            navController.navigate(CharacterDetailsDestination(id))
                        }
                    )
                }
                composable<CharacterDetailsDestination> { backStackEntry ->
                    val destination = backStackEntry.toRoute<CharacterDetailsDestination>()
                    ScreenDetailsDestination(
                        characterId = destination.characterId,
                        onNavigateBack = {navController.popBackStack()}
                    )
                }
            }

            navigation<LocationsGraph>(startDestination = LocationsListDestination) {
                composable<LocationsListDestination> {
                    LocationsScreen(
                        onNavigateToDetails = { id ->
                            navController.navigate(LocationDetailsDestination(id))
                        }
                    )
                }
                composable<LocationDetailsDestination> { backStackEntry ->
                    val destination = backStackEntry.toRoute<LocationDetailsDestination>()
                    LocationDetailsScreen(
                        locationId = destination.locationId,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }

            composable< ProfileDestination > {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(LoginDestination) {
                            popUpTo(0)
                        }
                    }
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lab07Theme {
        AppNavigation()
    }
}