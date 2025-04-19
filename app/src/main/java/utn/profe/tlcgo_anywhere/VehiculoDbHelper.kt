package utn.profe.tlcgo_anywhere.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class VehiculoDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Vehiculos.db"

        // Table and column names
        const val TABLE_VEHICULOS = "vehiculos"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE_SERIE = "nombre_serie"
        const val COLUMN_ANNO = "anno"
        const val COLUMN_CARACTERISTICAS = "caracteristicas"
        const val COLUMN_IMAGEN_PATH = "imagen_path"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_VEHICULOS_TABLE = ("CREATE TABLE " + TABLE_VEHICULOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOMBRE_SERIE + " TEXT NOT NULL,"
                + COLUMN_ANNO + " INTEGER NOT NULL,"
                + COLUMN_CARACTERISTICAS + " TEXT NOT NULL,"
                + COLUMN_IMAGEN_PATH + " TEXT"
                + ")")
        db.execSQL(CREATE_VEHICULOS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_VEHICULOS")
        onCreate(db)
    }
}