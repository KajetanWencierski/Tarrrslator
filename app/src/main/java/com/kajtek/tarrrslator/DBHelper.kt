package com.kajtek.tarrrslator

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class DBHelper(context: Context) : SQLiteOpenHelper(context, "Tarrrslator.db", null, 1) {
    //create table users
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USERS_TABLE = ("CREATE TABLE " +
                "users" + "("
                + "login" + " TEXT PRIMARY KEY,"
                + "password" + " TEXT)")

        val CREATE_TRANSLATIONS_TABLE = ("CREATE TABLE " +
            "translations" + "("
            + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "login" + " TEXT,"
            + "english" + " TEXT,"
            + "pirate" + " TEXT,"
            + "FOREIGN KEY (login) REFERENCES users(login) ON DELETE CASCADE)")

        db.execSQL(CREATE_USERS_TABLE)
        db.execSQL(CREATE_TRANSLATIONS_TABLE)
    }

    //drop table users
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + "users")
        db.execSQL("DROP TABLE IF EXISTS " + "translations")
        onCreate(db)
    }

    fun checkUser(login: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE login = '$login' AND " +
                "password = '$password'", null)
        return cursor.count != 0
    }

    // check if user exists in table users
    fun checkUserExists(login: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE login = '$login'", null)
        return cursor.count != 0
    }

    fun addUser(login: String, password: String): Boolean {
        val isUserAlreadyInDb = checkUserExists(login)
        if (isUserAlreadyInDb) {
            return false
        }
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("login", login)
        values.put("password", password)
        db.insert("users", null, values)
        db.close()
        return true
    }

    fun addTranslation(login: String, english: String, pirate: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("login", login)
        values.put("english", english)
        values.put("pirate", pirate)
        db.insert("translations", null, values)
        db.close()
        return true
    }

    fun getTranslations(login: String): ArrayList<Translation> {
        val db = this.readableDatabase
        val translations = ArrayList<Translation>()
        val cursor = db.rawQuery("SELECT * FROM translations WHERE login = '$login'", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val english = cursor.getString(cursor.getColumnIndexOrThrow("english"))
                val pirate = cursor.getString(cursor.getColumnIndexOrThrow("pirate"))
                translations.add(Translation(id, login, english, pirate))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return translations
    }

    fun deleteTranslation(id: Int?) {
        val db = this.writableDatabase
        db.delete("translations", "id = ?", arrayOf(id.toString()))
        db.close()
    }
}