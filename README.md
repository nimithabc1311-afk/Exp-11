# Experiment 11: Develop an Application to Display Google Maps

| **Name** | Nimitha BC |
| **USN** | 25MCAR0132 |
| **Course** | MCA |
| **Platform** | Android Studio, Kotlin |
| **Experiment** | |7|

## 1. About the Experiment

Location and maps are part of many everyday apps, such as ride booking, food delivery and navigation. This experiment builds an Android app in Kotlin that embeds an interactive **Google Map** and lets the user:

- View the map centred on a city with a marker
- Switch between **Normal, Satellite, Terrain and Hybrid** map types
- Jump to different places (Bengaluru, Lalbagh, Mysuru Palace, New Delhi)
- **Tap anywhere on the map** to drop a pin that shows its latitude and longitude
- Show their own position with **My Location**

---

## 2. Concept / Technology Behind It

### Maps SDK for Android
Google provides the **Maps SDK for Android** (`play-services-maps`) so that apps can show Google Maps and control it from code. Using it needs a **Google Maps API key**, created in the Google Cloud Console with the *Maps SDK for Android* API enabled.

### Key classes used

| Class | Role in this app |
|-------|------------------|
| `SupportMapFragment` | A Fragment that hosts and displays the map inside the activity |
| `OnMapReadyCallback` / `onMapReady()` | Called when the map has loaded; gives the `GoogleMap` object |
| `GoogleMap` | The map itself: used to set the map type, add markers and move the camera |
| `LatLng` | A latitude and longitude pair that identifies a point on Earth |
| `MarkerOptions` | Describes a marker (position, title, snippet) added with `addMarker()` |
| `CameraUpdateFactory` | Moves or animates the camera to a location and zoom level |

### Permissions
- `INTERNET` to download map tiles
- `ACCESS_FINE_LOCATION` and `ACCESS_COARSE_LOCATION` for the **My Location** feature, requested from the user at runtime

### API key handling
The API key is stored in `local.properties` as `MAPS_API_KEY`. That file is excluded from Git by `.gitignore`, so the key is **not uploaded to GitHub**. Gradle reads it and injects it into `AndroidManifest.xml` through a manifest placeholder.

---

## 3. Scenario Used to Demonstrate It

A **City Explorer** app:

1. The app opens with the map centred on **Bengaluru** with a marker on it.
2. The user switches the map type to **Satellite** or **Terrain** with the buttons at the top.
3. The user taps a place button such as **Lalbagh** or **Mysuru Palace** and the camera animates there and drops a marker.
4. The user taps any point on the map and a pin appears with its coordinates.
5. The user taps **My Location** and allows the permission, and a blue dot shows their position.

The student name and USN are shown at the top of the screen.

---

## 4. Project Folder and File Structure

```
Experiment11/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           └── java/com/example/experiment11/
│               └── MainActivity.kt
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── screenshots/
│   ├── output.png
│   ├── test1_map_marker.png
│   ├── test2_map_types.png
│   └── test3_drop_pin.png
├── .gitignore
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
└── README.md
```

| File | Purpose |
|------|---------|
| `MainActivity.kt` | Builds the screen in code (no XML layout needed), hosts the `SupportMapFragment`, and handles map types, markers, tap-to-pin and My Location |
| `AndroidManifest.xml` | Declares the permissions, the Google Maps API key meta-data and `MainActivity` as the launcher |
| `app/build.gradle.kts` | Dependencies (including `play-services-maps`) and the code that reads `MAPS_API_KEY` from `local.properties` |
| `screenshots/` | Output and test case screenshots shown in this README |
| `local.properties` | *Not in the repository.* Created by Android Studio on your computer; holds `MAPS_API_KEY` |

---

## 5. Output Test Cases

### Test Case 1: Map loads with a marker

| | |
|---|---|
| **Input** | Launch the app (internet on) |
| **Expected result** | The Google Map opens centred on Bengaluru with a marker. The header shows Name: Nimitha BC and USN: 25MCAR0132 |
| **Status** | Pass |

**Screenshot:**

<img width="589" height="1280" alt="WhatsApp Image 2026-10-30 at 8 33 46 PM" src="https://github.com/user-attachments/assets/9807d13f-59b7-4c7a-a4d8-9fe062c0aacd" />


### Test Case 2: Change the map type

| | |
|---|---|
| **Input** | Tap **Satellite** (then **Terrain**) |
| **Expected result** | The map changes to satellite imagery (then terrain view) and the status line shows the selected map type |
| **Status** | Pass |

**Screenshot:**

<img width="589" height="1280" alt="WhatsApp Image 2026-10-30 at 8 33 46 PM" src="https://github.com/user-attachments/assets/bea4c1a0-efd2-452f-9e71-eac8f9bb310f" />


### Test Case 3: Navigate to a place and drop a pin

| | |
|---|---|
| **Input** | Tap **Mysuru Palace**, then tap any point on the map |
| **Expected result** | The camera animates to Mysuru Palace with a marker, and tapping the map drops a pin; the status line shows its latitude and longitude |
| **Status** | Pass |

**Screenshot:**

<img width="589" height="1280" alt="WhatsApp Image 2026-10-30 at 8 33 54 PM" src="https://github.com/user-attachments/assets/5c35ad38-1cab-467e-bfb1-469ad2721f88" />


## 7. How to Run

1. **Get a Google Maps API key**
   - Go to the [Google Cloud Console](https://console.cloud.google.com/) and create a project.
   - Enable **Maps SDK for Android** under *APIs & Services → Library*.
   - Under *APIs & Services → Credentials*, create an **API key**.
2. Open the project in Android Studio and wait for Gradle sync to finish.
3. Open the `local.properties` file in the project root and add this line (replace with your own key):
   ```
   MAPS_API_KEY=your_api_key_here
   ```
4. Run the app on an emulator that includes **Google Play services** (a "Google Play" system image) or on a real phone with internet.
5. Tap **Allow** when asked for location permission after tapping **My Location**.

---

## 8. Conclusion

The experiment shows how the Maps SDK for Android embeds a fully interactive Google Map in an app. Using `SupportMapFragment` and `GoogleMap`, the app changed map types, added markers, moved the camera and responded to taps, and it used a runtime permission for the user's location.
