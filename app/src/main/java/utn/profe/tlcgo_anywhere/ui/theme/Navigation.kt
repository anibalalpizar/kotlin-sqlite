package utn.profe.tlcgo_anywhere.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import utn.profe.tlcgo_anywhere.viewmodel.VehiculoViewModel

@Composable
fun AppNavigation(navController: NavHostController, viewModel: VehiculoViewModel) {
    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            PantallaLista(navController = navController, viewModel = viewModel)
        }

        composable("registro") {
            AgregarVehiculoScreen(viewModel = viewModel,  navController = navController)
        }
    }
}
