package com.example.experiment11

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Experiment 11: Develop an application to display Google Maps.
 *  - SupportMapFragment hosts the Google Map
 *  - OnMapReadyCallback gives us the GoogleMap object once the map is ready
 *  - Markers, camera movement, map types, tap-to-drop-pin and "My Location"
 * The UI is built in code, so no XML layout files are needed.
 */
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val MAP_CONTAINER_ID = 2001
    }

    private data class Place(val name: String, val latLng: LatLng)

    private val places = listOf(
        Place("Bengaluru", LatLng(12.9716, 77.5946)),
        Place("Lalbagh", LatLng(12.9507, 77.5848)),
        Place("Mysuru Palace", LatLng(12.3052, 76.6552)),
        Place("New Delhi", LatLng(28.6139, 77.2090))
    )

    private var googleMap: GoogleMap? = null
    private lateinit var tvInfo: TextView

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) enableMyLocation() else toast("Location permission denied")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUi()

        // Add the map fragment (or reuse it after a screen rotation)
        var mapFragment =
            supportFragmentManager.findFragmentByTag("map") as? SupportMapFragment
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(MAP_CONTAINER_ID, mapFragment, "map")
                .commitNow()
        }
        mapFragment.getMapAsync(this)
    }

    private fun buildUi() {
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }

        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(20 + bars.left, 10 + bars.top, 20 + bars.right, 10 + bars.bottom)
            insets
        }

        root.addView(text("Experiment 11: Google Maps", 20f, bold = true))
        root.addView(text("Name: Nimitha BC  |  USN: 25MCAR0132", 14f))
        tvInfo = text("Loading map...", 13f)
        root.addView(tvInfo)

        // Map type buttons
        root.addView(buttonRow(
            button("Normal") { googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL; info("Map type: Normal") },
            button("Satellite") { googleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE; info("Map type: Satellite") },
            button("Terrain") { googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN; info("Map type: Terrain") },
            button("Hybrid") { googleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID; info("Map type: Hybrid") }
        ))

        // Place buttons + utilities
        val placeButtons = places.map { place ->
            button(place.name) { goToPlace(place) }
        }.toTypedArray()
        root.addView(buttonRow(*placeButtons))

        root.addView(buttonRow(
            button("My Location") { requestMyLocation() },
            button("Clear Pins") { googleMap?.clear(); info("All pins cleared") }
        ))

        // Container for the map fragment
        val mapContainer = FrameLayout(this).apply { id = MAP_CONTAINER_ID }
        root.addView(mapContainer, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
        ))

        setContentView(root)
    }

    // Called when the map is ready to use
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isCompassEnabled = true

        val start = places[0]
        map.addMarker(MarkerOptions().position(start.latLng).title(start.name))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(start.latLng, 12f))
        info("Showing: ${start.name}")

        // Tap anywhere on the map to drop a pin
        map.setOnMapClickListener { point ->
            val coords = "%.5f, %.5f".format(point.latitude, point.longitude)
            map.addMarker(MarkerOptions().position(point).title("Dropped pin").snippet(coords))
            info("Pin dropped at $coords")
        }
    }

    private fun goToPlace(place: Place) {
        val map = googleMap ?: return
        map.addMarker(MarkerOptions().position(place.latLng).title(place.name))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 13f))
        info("Showing: ${place.name}")
    }

    // ---------- My Location ----------
    private fun requestMyLocation() {
        val granted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) enableMyLocation()
        else locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        googleMap?.isMyLocationEnabled = true
        googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        info("My Location enabled (blue dot shows your position)")
    }

    // ---------- Small helpers ----------
    private fun button(label: String, onClick: () -> Unit) = Button(this).apply {
        text = label
        isAllCaps = false
        setOnClickListener { onClick() }
    }

    private fun buttonRow(vararg buttons: Button): HorizontalScrollView {
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        buttons.forEach { row.addView(it) }
        return HorizontalScrollView(this).apply { addView(row) }
    }

    private fun text(t: String, size: Float, bold: Boolean = false) = TextView(this).apply {
        text = t
        textSize = size
        if (bold) typeface = Typeface.DEFAULT_BOLD
        setPadding(0, 6, 0, 6)
    }

    private fun info(msg: String) { tvInfo.text = msg }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
