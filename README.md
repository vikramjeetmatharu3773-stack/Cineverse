# Cineverse - Movie Streaming App

## Android App
- **Package:** com.cineverse.app
- **MainActivity:** MainActivity.java
- **Layout:** activity_main.xml
- **Manifest:** AndroidManifest.xml

## Backend API
- **Server:** Express.js
- **Port:** 3000
- **API Endpoints:** /api/movies (GET, POST)
- **Files:** server.js, package.json, .env

## Build Instructions
1. Install Android SDK
2. Run `./gradlew assembleDebug` to build APK
3. Backend: `npm install && node server.js`

## Files Structure
```
cine/
├── app/                    # Android app source
├── build.gradle           # Root build config
├── settings.gradle        # Project settings
├── gradlew               # Gradle wrapper
├── gradle.properties     # Gradle properties
├── gradle/               # Gradle wrapper files
└── cineverse-backend/    # Backend API files
    ├── package.json
    ├── server.js
    ├── .env
    └── cineverse-backend.tar.gz  # Full backend (with node_modules)
```