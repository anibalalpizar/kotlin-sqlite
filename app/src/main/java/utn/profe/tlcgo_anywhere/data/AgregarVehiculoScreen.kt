package utn.profe.tlcgo_anywhere.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import utn.profe.tlcgo_anywhere.R
import utn.profe.tlcgo_anywhere.data.VehiculoEntity
import utn.profe.tlcgo_anywhere.viewmodel.VehiculoViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarVehiculoScreen(viewModel: VehiculoViewModel, navController: NavController) {
    val context = LocalContext.current

    var serie by remember { mutableStateOf("") }
    var anno by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    var errorSerie by remember { mutableStateOf(false) }
    var errorAnno by remember { mutableStateOf(false) }
    var errorDescripcion by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            imagenUri = uri
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text("Registrar vehículo Nuevo") },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = "Regresar"
                    )
                }
            }
        )

        OutlinedTextField(
            value = serie,
            onValueChange = {
                serie = it
                if (it.isNotBlank()) errorSerie = false
            },
            isError = errorSerie,
            label = { Text("Serie del vehículo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = anno,
            onValueChange = {
                anno = it
                if (it.trim().toIntOrNull() != null) errorAnno = false
            },
            isError = errorAnno,
            label = { Text("Año de fabricación") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = {
                descripcion = it
                if (it.isNotBlank()) errorDescripcion = false
            },
            isError = errorDescripcion,
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .size(200.dp)
                .clickable { launcher.launch(arrayOf("image/*")) },

            contentAlignment = Alignment.Center
        ) {
            if (imagenUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imagenUri),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Agregar imagen del vehículo",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val serieLimpio = serie.trim()
            val annoInt = anno.trim().toIntOrNull()
            val descripcionLimpia = descripcion.trim()

            errorSerie = serieLimpio.isEmpty()
            errorAnno = annoInt == null
            errorDescripcion = descripcionLimpia.isEmpty()

            val camposValidos = !errorSerie && !errorAnno && !errorDescripcion

            if (camposValidos) {
                viewModel.agregarVehiculo(
                    VehiculoEntity(
                        serie = serieLimpio,
                        anno = annoInt!!,
                        descripcion = descripcionLimpia,
                        imagenUri = imagenUri?.toString() ?: ""
                    )
                )
                Toast.makeText(context, "Vehículo registrado", Toast.LENGTH_SHORT).show()
                navController.popBackStack()

                serie = ""
                anno = ""
                descripcion = ""
                imagenUri = null
            } else {
                Toast.makeText(context, "Complete correctamente los campos marcados", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Guardar vehículo")
        }
    }
}
