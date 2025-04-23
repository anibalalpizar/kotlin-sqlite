package utn.profe.tlcgo_anywhere.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import utn.profe.tlcgo_anywhere.TLCgoanywhereTopAppBar
import utn.profe.tlcgo_anywhere.VehiculoItemNuevo
import utn.profe.tlcgo_anywhere.viewmodel.VehiculoViewModel

@Composable
fun PantallaLista(navController: NavHostController, viewModel: VehiculoViewModel) {
    val listaVehiculos by viewModel.vehiculos.collectAsState()

    Scaffold(
        topBar = {
            TLCgoanywhereTopAppBar()
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("registro") }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar vehículo")
            }
        }
    ) { padding ->
        if (listaVehiculos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay vehículos registrados",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }

        } else {
            LazyColumn(contentPadding = padding) {
                items(listaVehiculos) { vehiculo ->
                    VehiculoItemNuevo(vehiculo = vehiculo)
                }
            }
        }
    }
}
