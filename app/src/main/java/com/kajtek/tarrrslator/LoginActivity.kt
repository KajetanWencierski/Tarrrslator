package com.kajtek.tarrrslator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        val signInButton = findViewById<Button>(R.id.buttonSignIn)
        val loginEditText = findViewById<EditText>(R.id.editTextLogin)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val registerButton = findViewById<Button>(R.id.buttonRegister)
        val db = DBHelper(this)

        signInButton.setOnClickListener() {
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()
            if ((login.isEmpty()) || (password.isEmpty())) {
                Toast.makeText(applicationContext, resources.getString(R.string.empty_login_or_password_toast_message), Toast.LENGTH_LONG).show()
            } else {
                val isDataCorrect = db.checkUser(login, password)
                if (!isDataCorrect) {
                    Toast.makeText(applicationContext, resources.getString(R.string.incorrect_password_or_password_toast_message), Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("login", login)
                    startActivity(intent)
                }
            }
        }

        registerButton.setOnClickListener() {
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()
            if ((login.isEmpty()) || (password.isEmpty())) {
                Toast.makeText(applicationContext, resources.getString(R.string.empty_login_or_password_toast_message), Toast.LENGTH_LONG).show()
            } else {
                val isDataCorrect = db.addUser(login, password)
                if (!isDataCorrect) {
                    Toast.makeText(applicationContext, resources.getString(R.string.user_already_exists), Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("login", login)
                    startActivity(intent)
                }
            }
        }
    }
}