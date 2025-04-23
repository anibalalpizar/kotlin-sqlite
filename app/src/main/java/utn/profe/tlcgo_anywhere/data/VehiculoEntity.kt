package utn.profe.tlcgo_anywhere.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehiculos")

data class VehiculoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serie: String,
    val anno: Int,
    val descripcion: String,
    val imagenUri: String
)
