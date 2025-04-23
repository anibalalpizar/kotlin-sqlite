package utn.profe.tlcgo_anywhere.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VehiculoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)

    suspend fun insertarVehiculo(vehiculo: VehiculoEntity)

    @Query("SELECT * FROM vehiculos")
    fun obtenerVehiculos(): Flow<List<VehiculoEntity>>
}
