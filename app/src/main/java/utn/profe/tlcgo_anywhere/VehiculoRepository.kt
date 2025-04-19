package utn.profe.tlcgo_anywhere.data

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class VehiculoRepository(private val context: Context) {
    private val dbHelper = VehiculoDbHelper(context)
    private val imageDir = File(context.filesDir, "vehiculo_images")

    init {
        if (!imageDir.exists()) {
            imageDir.mkdirs()
        }
    }

    fun saveVehiculo(nombreSerie: String, annoLanzamiento: Int, caracteristicas: String, imageUri: Uri?): Long {
        val db = dbHelper.writableDatabase

        // Handle image saving
        var imagePath: String? = null
        imageUri?.let {
            imagePath = saveImageToInternalStorage(it)
        }

        val values = ContentValues().apply {
            put(VehiculoDbHelper.COLUMN_NOMBRE_SERIE, nombreSerie)
            put(VehiculoDbHelper.COLUMN_ANNO, annoLanzamiento)
            put(VehiculoDbHelper.COLUMN_CARACTERISTICAS, caracteristicas)
            put(VehiculoDbHelper.COLUMN_IMAGEN_PATH, imagePath)
        }

        val id = db.insert(VehiculoDbHelper.TABLE_VEHICULOS, null, values)
        db.close()
        return id
    }

    private fun saveImageToInternalStorage(imageUri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val fileName = "VEHICULO_${UUID.randomUUID()}.jpg"
        val file = File(imageDir, fileName)

        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        return file.absolutePath
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
                val nombreSerie = getString(getColumnIndexOrThrow(VehiculoDbHelper.COLUMN_NOMBRE_SERIE))
                val anno = getInt(getColumnIndexOrThrow(VehiculoDbHelper.COLUMN_ANNO))
                val caracteristicas = getString(getColumnIndexOrThrow(VehiculoDbHelper.COLUMN_CARACTERISTICAS))

                val imagenPathIndex = getColumnIndexOrThrow(VehiculoDbHelper.COLUMN_IMAGEN_PATH)
                val imagenPath = if (!isNull(imagenPathIndex)) getString(imagenPathIndex) else null

                vehiculos.add(VehiculoDB(id, nombreSerie, anno, caracteristicas, imagenPath))
            }
        }
        cursor.close()
        db.close()
        return vehiculos
    }

    fun getImageFromPath(path: String?): Bitmap? {
        if (path == null) return null

        return try {
            BitmapFactory.decodeFile(path)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun deleteVehiculo(id: Long): Int {
        // First get the vehicle to delete its image
        val vehiculo = getVehiculoById(id)

        // Delete image file if exists
        vehiculo?.imagenPath?.let {
            val file = File(it)
            if (file.exists()) {
                file.delete()
            }
        }

        // Delete database record
        val db = dbHelper.writableDatabase
        val result = db.delete(
            VehiculoDbHelper.TABLE_VEHICULOS,
            "${VehiculoDbHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
        return result
    }

    private fun getVehiculoById(id: Long): VehiculoDB? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            VehiculoDbHelper.TABLE_VEHICULOS,
            null,
            "${VehiculoDbHelper.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var vehiculo: VehiculoDB? = null

        with(cursor) {
            if (moveToFirst()) {
                val nombreSerie = getString(getColumnIndexOrThrow(VehiculoDbHelper.COLUMN_NOMBRE_SERIE))
                val anno = getInt(getColumnIndexOrThrow(VehiculoDbHelper.COLUMN_ANNO))
                val caracteristicas = getString(getColumnIndexOrThrow(VehiculoDbHelper.COLUMN_CARACTERISTICAS))

                val imagenPathIndex = getColumnIndexOrThrow(VehiculoDbHelper.COLUMN_IMAGEN_PATH)
                val imagenPath = if (!isNull(imagenPathIndex)) getString(imagenPathIndex) else null

                vehiculo = VehiculoDB(id, nombreSerie, anno, caracteristicas, imagenPath)
            }
        }
        cursor.close()
        db.close()
        return vehiculo
    }
}