package utn.profe.tlcgo_anywhere.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import utn.profe.tlcgo_anywhere.data.AppDatabase
import utn.profe.tlcgo_anywhere.data.VehiculoEntity

class VehiculoViewModel(application: Application) : AndroidViewModel(application) {

    private val vehiculoDao = AppDatabase.getDatabase(application).vehiculoDao()

    private val _vehiculos = MutableStateFlow<List<VehiculoEntity>>(emptyList())

    val vehiculos = _vehiculos.asStateFlow()

    init {
        viewModelScope.launch {
            vehiculoDao.obtenerVehiculos().collect {
                _vehiculos.value = it
            }
        }
    }

    fun agregarVehiculo(vehiculo: VehiculoEntity) {
        viewModelScope.launch {
            vehiculoDao.insertarVehiculo(vehiculo)
        }
    }
}
