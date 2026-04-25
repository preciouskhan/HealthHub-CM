package com.healthhub.cm

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class BuyerHomeActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_buyer_home)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupChips()
        setupNavigation()
        setupProductCards()
        requestLocationPermission()
    }

    // ── LOCATION ──────────────────────────────────────
    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        } else {
            getLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) return

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val area = addresses[0].subLocality
                        ?: addresses[0].locality
                        ?: "Your location"
                    findViewById<TextView>(R.id.tv_location).text = "📍 $area"
                }
            }
        }
    }

    // ── CHIPS ──────────────────────────────────────────
    private val chips: List<Int> by lazy {
        listOf(R.id.chip_all, R.id.chip_medicines, R.id.chip_skincare,
            R.id.chip_vitamins, R.id.chip_firstaid)
    }

    private fun setupChips() {
        chips.forEach { chipId ->
            findViewById<TextView>(chipId).setOnClickListener {
                setActiveChip(chipId)
            }
        }
    }

    private fun setActiveChip(activeId: Int) {
        chips.forEach { chipId ->
            val chip = findViewById<TextView>(chipId)
            if (chipId == activeId) {
                chip.setBackgroundResource(R.drawable.bg_chip_active)
                chip.setTextColor(getColor(R.color.green_primary))
            } else {
                chip.setBackgroundResource(R.drawable.bg_chip_inactive)
                chip.setTextColor(getColor(R.color.gray_mid))
            }
        }
    }

    // ── NAVIGATION ────────────────────────────────────
    private fun setupNavigation() {
        // Map nav
        findViewById<LinearLayout>(R.id.nav_map).setOnClickListener {
            // TODO: startActivity(Intent(this, MapActivity::class.java))
        }

        // Scan nav
        findViewById<LinearLayout>(R.id.nav_scan).setOnClickListener {
            // TODO: startActivity(Intent(this, ScannerActivity::class.java))
        }

        // Map preview card
        findViewById<CardView>(R.id.card_map).setOnClickListener {
            // TODO: startActivity(Intent(this, MapActivity::class.java))
        }

        // Scan button in search bar
        findViewById<TextView>(R.id.btn_scan).setOnClickListener {
            // TODO: startActivity(Intent(this, ScannerActivity::class.java))
        }
    }

    // ── PRODUCT CARDS ─────────────────────────────────
    private fun setupProductCards() {
        listOf(
            R.id.card_product_1,
            R.id.card_product_2,
            R.id.card_product_3,
            R.id.card_product_4
        ).forEach { cardId ->
            findViewById<CardView>(cardId).setOnClickListener {
                animateCard(it as CardView) {
                    // TODO: navigate to SearchResultsActivity with product name
                }
            }
        }
    }

    private fun animateCard(view: CardView, onEnd: () -> Unit) {
        view.animate()
            .scaleX(0.97f).scaleY(0.97f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f).scaleY(1f)
                    .setDuration(100)
                    .withEndAction { onEnd() }
                    .start()
            }.start()
    }

    // Prevent going back to onboarding
    override fun onBackPressed() {
        finishAffinity()
    }
}
