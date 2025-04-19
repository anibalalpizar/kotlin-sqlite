package utn.profe.tlcgo_anywhere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import utn.profe.tlcgo_anywhere.ui.components.TLCgoanywhereTopAppBar
import utn.profe.tlcgo_anywhere.ui.screens.RegistroVehiculoScreen
import utn.profe.tlcgo_anywhere.ui.screens.VehiculosGuardadosScreen
import utn.profe.tlcgo_anywhere.ui.theme.TLCGoanywhereTheme
import utn.profe.tlcgo_anywhere.ui.viewmodel.VehiculoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TLCGoanywhereTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TLCGoAnywhereNavigation()
                }
            }
        }
    }
}

@Composable
fun TLCGoAnywhereNavigation() {
    val navController = rememberNavController()
    val viewModel: VehiculoViewModel = viewModel(
        factory = VehiculoViewModel.Factory(LocalContext.current)
    )

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            TLCgoanywhereApp(
                onNavigateToRegister = { navController.navigate("register") },
                viewModel = viewModel
            )
        }
        composable("register") {
            RegistroVehiculoScreen(
                viewModel = viewModel,
                onNavigateToList = { navController.navigate("saved_vehicles") }
            )
        }
        composable("saved_vehicles") {
            VehiculosGuardadosScreen(
                viewModel = viewModel,
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
    }
}

/**
 * Composable that displays the home screen with a button to navigate to the register screen
 */
@Composable
fun TLCgoanywhereApp(
    onNavigateToRegister: () -> Unit,
    viewModel: VehiculoViewModel
) {
    val vehiculos by viewModel.vehiculosFromDb.collectAsState()
    Scaffold(
        topBar = {
            TLCgoanywhereTopAppBar()
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToRegister) {
                Text("+")
            }
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(vehiculos) {
                VehiculoItem(
                    vehiculo = it,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                )
            }
        }
    }
}
