package utn.profe.tlcgo_anywhere.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import utn.profe.tlcgo_anywhere.ui.viewmodel.VehiculoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroVehiculoScreen(
    viewModel: VehiculoViewModel,
    onNavigateToList: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.selectedImageUri.value = it

            // Load the selected image as a Bitmap for preview
            context.contentResolver.openInputStream(it)?.use { inputStream ->
                viewModel.selectedImageBitmap.value = android.graphics.BitmapFactory.decodeStream(inputStream)
            }
        }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is VehiculoViewModel.UiState.Success -> {
                Toast.makeText(context, "Operación completada con éxito", Toast.LENGTH_SHORT).show()
            }
            is VehiculoViewModel.UiState.Error -> {
                val errorMessage = (uiState as VehiculoViewModel.UiState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Vehículo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nombre Serie
            OutlinedTextField(
                value = viewModel.nombreSerie.value,
                onValueChange = { viewModel.nombreSerie.value = it },
                label = { Text("Nombre de la Serie") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Año de lanzamiento
            OutlinedTextField(
                value = viewModel.annoLanzamiento.value,
                onValueChange = { viewModel.annoLanzamiento.value = it },
                label = { Text("Año de lanzamiento") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Características
            OutlinedTextField(
                value = viewModel.caracteristicas.value,
                onValueChange = { viewModel.caracteristicas.value = it },
                label = { Text("Características") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Image selector
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                viewModel.selectedImageBitmap.value?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Selected vehicle image",
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: run {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Add photo",
                            modifier = Modifier.size(64.dp)
                        )
                        Text("Seleccionar Imagen")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save button
            Button(
                onClick = { viewModel.saveVehiculo() },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.nombreSerie.value.isNotEmpty() &&
                        viewModel.annoLanzamiento.value.isNotEmpty() &&
                        viewModel.caracteristicas.value.isNotEmpty() &&
                        viewModel.uiState.value !is VehiculoViewModel.UiState.Loading
            ) {
                Text("Guardar Vehículo")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // View saved vehicles button
            Button(
                onClick = onNavigateToList,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Vehículos Guardados!!!")
            }

            // Loading indicator
            if (uiState is VehiculoViewModel.UiState.Loading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}