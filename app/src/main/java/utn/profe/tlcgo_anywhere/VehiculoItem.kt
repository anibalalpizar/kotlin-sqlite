package utn.profe.tlcgo_anywhere

import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import utn.profe.tlcgo_anywhere.data.VehiculoDB

@Composable
fun VehiculoItem(
    vehiculo: VehiculoDB,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        // El campo correcto es vehiculo.serie (no nombreSerie)
        Text(text = "Vehículo: - Año: ${vehiculo.annoLanzamiento}")
    }
}
