package com.example.lab07

import androidx.compose.foundation.lazy.items
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.lab07.ui.theme.Lab07Theme
import kotlinx.serialization.Serializable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.navigation.toRoute
import com.example.lab07.characters.ScreenDetailsDestination
import com.example.lab07.characters.ScreenHomeDestination
import com.example.lab07.login.ScreenLoginDestination
import com.example.lab07.navigation.DetailsDestination
import com.example.lab07.navigation.HomeDestination
import com.example.lab07.navigation.LoginDestination


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

    NavHost(
        navController = navController,
        startDestination = LoginDestination
    ) {
        composable<LoginDestination> {
            ScreenLoginDestination(
                onNavigateToHome = {
                    navController.navigate(route = HomeDestination) {
                        popUpTo<LoginDestination> { inclusive = true }
                    }
                }
            )
        }
        composable<HomeDestination> {
            ScreenHomeDestination(
                onNavigateToDetails = { id ->
                    navController.navigate(route = DetailsDestination(characterId = id))
                }
            )
        }
        composable<DetailsDestination> {backStackEntry ->
            val destination = backStackEntry.toRoute<DetailsDestination>()

            ScreenDetailsDestination(
                characterId = destination.characterId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
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