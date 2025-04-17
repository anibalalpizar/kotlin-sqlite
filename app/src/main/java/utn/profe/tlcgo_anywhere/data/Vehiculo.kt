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

val vehiculos = listOf(
    Vehiculo(R.drawable.seriebj, R.string.seriebj, 1951, R.string.dsc_seriebj),
    Vehiculo(R.drawable.serie20, R.string.serie20, 1955, R.string.dsc_serie20),
    Vehiculo(R.drawable.serie40, R.string.serie40, 1960, R.string.dsc_serie20),
    Vehiculo(R.drawable.serie40safari, R.string.serie40safari, 1960, R.string.dsc_serie40safari),
    Vehiculo(R.drawable.serie40pkp, R.string.serie40pkp, 1960, R.string.dsc_serie40pkp),
    Vehiculo(R.drawable.serie50, R.string.serie50, 1967, R.string.dsc_serie50),
    Vehiculo(R.drawable.serie60, R.string.serie60, 1980, R.string.dsc_serie60),
    Vehiculo(R.drawable.serie70, R.string.serie70, 1984, R.string.dsc_serie70),
    Vehiculo(R.drawable.serie70lx, R.string.serie70lx, 1984, R.string.dsc_serie70lx),
    Vehiculo(R.drawable.serie70pkp, R.string.serie70pkp, 1984, R.string.dsc_serie70pkp),
    Vehiculo(R.drawable.serie80, R.string.serie80, 1950, R.string.dsc_serie80)
)