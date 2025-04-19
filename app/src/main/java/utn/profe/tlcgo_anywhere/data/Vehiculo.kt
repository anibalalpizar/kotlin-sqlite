package utn.profe.tlcgo_anywhere.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import utn.profe.tlcgo_anywhere.R

data class Vehiculo(
        @DrawableRes val imageResourceId: Int,
        @StringRes val serie: Int,
        val annoLanzamiento: Int,
        @StringRes val caracteristicas: Int
)
