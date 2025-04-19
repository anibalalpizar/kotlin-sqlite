package utn.profe.tlcgo_anywhere.data

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.IOException

class VehiculoRepository(private val context: Context) {
    private val dbHelper = VehiculoDbHelper(context)

    fun saveVehiculo(serie: Int, annoLanzamiento: Int, caracteristicas: Int, imageBitmap: Bitmap?): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(VehiculoDbHelper.COLUMN_SERIE, serie)
            put(VehiculoDbHelper.COLUMN_ANNO, annoLanzamiento)
            put(VehiculoDbHelper.COLUMN_CARACTERISTICAS, caracteristicas)
            imageBitmap?.let {
                val stream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.PNG, 100, stream)
                put(VehiculoDbHelper.COLUMN_IMAGEN, stream.toByteArray())
            }
        }

        val id = db.insert(VehiculoDbHelper.TABLE_VEHICULOS, null, values)
        db.close()
        return id
    }

    fun getAllVehiculos(): List<VehiculoDB> {
        val vehiculos = mutableListOf<VehiculoDB>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            VehiculoDbHelper.TABLE_VEHICULOS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(VehiculoDbHelper.COLUMN_ID))
                val serie = getInt(getColumnIndexOrThrow(VehiculoDbHelper.COLUMN_SERIE))
                val anno = getInt(getColumnIndexOrThrow(VehiculoDbHelper.COLUMN_ANNO))
                val caracteristicas = getInt(getColumnIndexOrThrow(VehiculoDbHelper.COLUMN_CARACTERISTICAS))

                val imagenIndex = getColumnIndexOrThrow(VehiculoDbHelper.COLUMN_IMAGEN)
                val imagen = if (!isNull(imagenIndex)) {
                    val imageBytes = getBlob(imagenIndex)
                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                } else null

                vehiculos.add(VehiculoDB(id, serie, anno, caracteristicas, imagen))
            }
        }
        cursor.close()
        db.close()
        return vehiculos
    }

    fun getDrawableAsBitmap(drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId) as BitmapDrawable
        return drawable.bitmap
    }

    fun deleteVehiculo(id: Long): Int {
        val db = dbHelper.writableDatabase
        val result = db.delete(
            VehiculoDbHelper.TABLE_VEHICULOS,
            "${VehiculoDbHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
        return result
    }
}

data class VehiculoDB(
    val id: Long,
    val serie: Int,
    val annoLanzamiento: Int,
    val caracteristicas: Int,
    val imagen: Bitmap?
)