package com.healthhub.cm

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class SellerLoginActivity : AppCompatActivity() {

    // Simple PIN — in real app this would be proper auth
    private val SELLER_PIN = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_seller_login)

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        findViewById<Button>(R.id.btn_seller_login).setOnClickListener {
            val pin = findViewById<EditText>(R.id.et_pin).text.toString()
            if (pin == SELLER_PIN) {
                startActivity(Intent(this, SellerDashboardActivity::class.java))
            } else {
                Toast.makeText(this, "Incorrect PIN. Try: 1234", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
