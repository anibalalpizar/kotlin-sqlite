package utn.profe.tlcgo_anywhere.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
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
import utn.profe.tlcgo_anywhere.R
import utn.profe.tlcgo_anywhere.data.Vehiculo
import utn.profe.tlcgo_anywhere.data.VehiculoDB
import utn.profe.tlcgo_anywhere.data.VehiculoRepository
import utn.profe.tlcgo_anywhere.data.vehiculos

class VehiculoViewModel(private val repository: VehiculoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _vehiculosFromDb = MutableStateFlow<List<VehiculoDB>>(emptyList())
    val vehiculosFromDb: StateFlow<List<VehiculoDB>> = _vehiculosFromDb.asStateFlow()

    // Form state
    val selectedSerie = mutableStateOf(R.string.seriebj)
    val annoLanzamiento = mutableStateOf("")
    val selectedDescripcion = mutableStateOf(R.string.dsc_seriebj)
    val selectedImage = mutableStateOf<Bitmap?>(null)

    // Series options for dropdown
    val seriesOptions = mapOf(
        R.string.seriebj to R.string.dsc_seriebj,
        R.string.serie20 to R.string.dsc_serie20,
        R.string.serie40 to R.string.dsc_serie40,
        R.string.serie40safari to R.string.dsc_serie40safari,
        R.string.serie40pkp to R.string.dsc_serie40pkp,
        R.string.serie50 to R.string.dsc_serie50,
        R.string.serie60 to R.string.dsc_serie60,
        R.string.serie70 to R.string.dsc_serie70,
        R.string.serie70lx to R.string.dsc_serie70lx,
        R.string.serie70pkp to R.string.dsc_serie70pkp,
        R.string.serie80 to R.string.dsc_serie80
    )

    // Image resources mapping
    val imageResources = mapOf(
        R.string.seriebj to R.drawable.seriebj,
        R.string.serie20 to R.drawable.serie20,
        R.string.serie40 to R.drawable.serie40,
        R.string.serie40safari to R.drawable.serie40safari,
        R.string.serie40pkp to R.drawable.serie40pkp,
        R.string.serie50 to R.drawable.serie50,
        R.string.serie60 to R.drawable.serie60,
        R.string.serie70 to R.drawable.serie70,
        R.string.serie70lx to R.drawable.serie70lx,
        R.string.serie70pkp to R.drawable.serie70pkp,
        R.string.serie80 to R.drawable.serie80
    )

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

    fun onSerieSelected(serie: Int) {
        selectedSerie.value = serie
        selectedDescripcion.value = seriesOptions[serie] ?: R.string.dsc_seriebj

        // Update image
        viewModelScope.launch {
            try {
                val drawableId = imageResources[serie] ?: R.drawable.seriebj
                val bitmap = withContext(Dispatchers.IO) {
                    repository.getDrawableAsBitmap(drawableId)
                }
                selectedImage.value = bitmap
            } catch (e: Exception) {
                selectedImage.value = null
            }
        }
    }

    fun saveVehiculo() {
        val anno = annoLanzamiento.value.toIntOrNull() ?: return

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                withContext(Dispatchers.IO) {
                    repository.saveVehiculo(
                        selectedSerie.value,
                        anno,
                        selectedDescripcion.value,
                        selectedImage.value
                    )
                }
                // Reset form
                annoLanzamiento.value = ""
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