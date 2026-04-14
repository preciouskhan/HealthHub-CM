package com.example.healthhub_cm

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val email = findViewById<EditText>(R.id.emailSignup)
        val password = findViewById<EditText>(R.id.passwordSignup)
        val confirm = findViewById<EditText>(R.id.confirmPassword)
        val signupBtn = findViewById<Button>(R.id.signupBtn)

        signupBtn.setOnClickListener {

            val emailText = email.text.toString()
            val passText = password.text.toString()
            val confirmText = confirm.text.toString()

            if (emailText.isEmpty() || passText.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            }
            else if (passText != confirmText) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Signup successful 🎉", Toast.LENGTH_SHORT).show()
            }
        }
    }
}