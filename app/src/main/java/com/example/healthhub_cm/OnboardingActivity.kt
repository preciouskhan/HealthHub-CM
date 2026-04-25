package com.healthhub.cm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.WindowCompat

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make status bar transparent so gradient fills full screen
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_onboarding)

        val cardBuyer: CardView = findViewById(R.id.card_buyer)
        val cardSeller: CardView = findViewById(R.id.card_seller)

        // Buyer card → go to BuyerHomeActivity
        cardBuyer.setOnClickListener {
            animateCardClick(it) {
                val intent = Intent(this, BuyerHomeActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }

        // Seller card → go to SellerRegisterActivity
        cardSeller.setOnClickListener {
            animateCardClick(it) {
                val intent = Intent(this, SellerRegisterActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    /**
     * Quick scale-down/up animation on tap before navigating.
     */
    private fun animateCardClick(view: View, onEnd: () -> Unit) {
        view.animate()
            .scaleX(0.96f)
            .scaleY(0.96f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .withEndAction { onEnd() }
                    .start()
            }
            .start()
    }

    // Prevent going back to a splash screen
    override fun onBackPressed() {
        finishAffinity()
    }
}
