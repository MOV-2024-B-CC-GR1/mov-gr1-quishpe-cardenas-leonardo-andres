package com.example.deber_02db.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Nombre y versión de la base de datos
        private const val DATABASE_NAME = "AutoParteDB.db"
        private const val DATABASE_VERSION = 2

        // Tablas y scripts de creación
        private const val TABLE_AUTO = "Auto"
        private const val TABLE_PARTE = "Parte"
        private const val CREATE_INDEX_PARTE = "CREATE INDEX idx_parte_id_auto ON Parte(id_auto);"

        private const val CREATE_TABLE_AUTO = """
            CREATE TABLE $TABLE_AUTO (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                marca TEXT NOT NULL,
                precio REAL NOT NULL,
                fecha_fabricacion TEXT NOT NULL,
                latitud REAL NOT NULL,
                longitud REAL NOT NULL
            );
        """

        private const val CREATE_TABLE_PARTE = """
            CREATE TABLE $TABLE_PARTE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_auto INTEGER NOT NULL,
                nombre_parte TEXT NOT NULL,
                precio REAL NOT NULL,
                fecha_reemplazo TEXT NOT NULL,
                FOREIGN KEY(id_auto) REFERENCES $TABLE_AUTO(id) ON DELETE CASCADE
            );
        """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_AUTO)
        db?.execSQL(CREATE_TABLE_PARTE)
        db?.execSQL(CREATE_INDEX_PARTE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE $TABLE_AUTO ADD COLUMN latitud REAL DEFAULT 0.0;")
            db?.execSQL("ALTER TABLE $TABLE_AUTO ADD COLUMN longitud REAL DEFAULT 0.0;")
        }
    }
}