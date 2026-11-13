package com.example.lab07.locations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lab07.Location
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton

@Composable
fun LocationItem(
    location: Location,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = location.name,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = location.type,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (location.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorito",
                tint = if (location.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun LocationFilterRow(
    selected: LocationFilter,
    onFilterSelected: (LocationFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selected == LocationFilter.TODAS,
            onClick = { onFilterSelected(LocationFilter.TODAS) },
            label = { Text("Todas") }
        )
        FilterChip(
            selected = selected == LocationFilter.FAVORITAS,
            onClick = { onFilterSelected(LocationFilter.FAVORITAS) },
            label = { Text("Favoritas") }
        )
        FilterChip(
            selected = selected == LocationFilter.NO_FAVORITAS,
            onClick = { onFilterSelected(LocationFilter.NO_FAVORITAS) },
            label = { Text("No favoritas") }
        )
    }
}

@Composable
fun LocationsRoute(
    onNavigateToDetails: (Int) -> Unit,
    viewModel: LocationsViewModel = viewModel(factory = LocationsViewModel.Factory)
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(LocationsEvent.CargarDatos)
    }

    LocationsScreen (
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateToDetails = onNavigateToDetails
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    state: LocationsState,
    onEvent: (LocationsEvent) -> Unit,
    onNavigateToDetails: (Int) -> Unit
) {
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onEvent(LocationsEvent.ForzarError) },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Cargando")
            }
        }
    } else if (state.hasError) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Error al obtener listado de ubicaciones. \nIntenta de nuevo.",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onEvent(LocationsEvent.CargarDatos) }
                ) {
                    Text("Reintentar")
                }
            }
        }
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Locations",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LocationFilterRow(
                    selected = state.filter,
                    onFilterSelected = { onEvent(LocationsEvent.ChangeFilter(it)) }
                )

                if (state.data.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay elementos para mostrar",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.data) { location ->
                            LocationItem(
                                location = location,
                                onClick = { onNavigateToDetails(location.id) },
                                onFavoriteClick = { onEvent(LocationsEvent.ToggleFavorite(location.id, location.isFavorite)) }
                            )
                        }
                    }
                }
            }
        }
    }
}
