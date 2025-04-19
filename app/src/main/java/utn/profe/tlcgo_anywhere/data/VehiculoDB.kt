package utn.profe.tlcgo_anywhere.data

data class VehiculoDB(
        val id: Long = 0,
        val nombreSerie: String,
        val annoLanzamiento: Int,
        val caracteristicas: String,
        val imagenPath: String? = null
)