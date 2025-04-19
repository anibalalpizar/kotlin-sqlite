package utn.profe.tlcgo_anywhere.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utn.profe.tlcgo_anywhere.data.VehiculoDB
import utn.profe.tlcgo_anywhere.data.VehiculoRepository

class VehiculoViewModel(val repository: VehiculoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _vehiculosFromDb = MutableStateFlow<List<VehiculoDB>>(emptyList())
    val vehiculosFromDb: StateFlow<List<VehiculoDB>> = _vehiculosFromDb.asStateFlow()

    // Form state
    val nombreSerie = mutableStateOf("")
    val annoLanzamiento = mutableStateOf("")
    val caracteristicas = mutableStateOf("")
    val selectedImageUri = mutableStateOf<Uri?>(null)
    val selectedImageBitmap = mutableStateOf<Bitmap?>(null)

    init {
        loadVehiculos()
    }

    fun loadVehiculos() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                withContext(Dispatchers.IO) {
                    val vehiculos = repository.getAllVehiculos()
                    _vehiculosFromDb.value = vehiculos
                }
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun loadImageFromPath(path: String?) {
        viewModelScope.launch {
            try {
                val bitmap = withContext(Dispatchers.IO) {
                    repository.getImageFromPath(path)
                }
                selectedImageBitmap.value = bitmap
            } catch (e: Exception) {
                // Handle error
                selectedImageBitmap.value = null
            }
        }
    }

    fun saveVehiculo() {
        if (nombreSerie.value.isBlank() || annoLanzamiento.value.isBlank() || caracteristicas.value.isBlank()) {
            _uiState.value = UiState.Error("Todos los campos son obligatorios")
            return
        }

        val anno = annoLanzamiento.value.toIntOrNull() ?: run {
            _uiState.value = UiState.Error("El año debe ser un número válido")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                withContext(Dispatchers.IO) {
                    repository.saveVehiculo(
                        nombreSerie.value,
                        anno,
                        caracteristicas.value,
                        selectedImageUri.value
                    )
                }
                // Reset form
                nombreSerie.value = ""
                annoLanzamiento.value = ""
                caracteristicas.value = ""
                selectedImageUri.value = null
                selectedImageBitmap.value = null

                // Reload list
                loadVehiculos()
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun deleteVehiculo(id: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                withContext(Dispatchers.IO) {
                    repository.deleteVehiculo(id)
                }
                loadVehiculos()
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VehiculoViewModel::class.java)) {
                return VehiculoViewModel(VehiculoRepository(context)) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}