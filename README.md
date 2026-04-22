# BarberConnect

Android application for barbershop booking and shop management.

## Features

### For Customers
- Browse nearby barbershops
- View services and pricing
- Book appointments
- Leave reviews
- Manage appointment history

### For Shop Owners
- Manage shop profile and services
- View and manage appointments
- Track customer reviews
- Payment history

## Tech Stack
- **Language**: Java 8
- **UI**: Android Views with ViewBinding
- **Backend**: Firebase (Authentication, Realtime Database, Storage)
- **Maps**: Google Maps SDK
- **Local Storage**: SQLite
- **Image Loading**: Picasso

## Project Structure
- `app/` - Main Android application module
- `mylibrary/` - Shared library module

## Setup Instructions

1. **Open in Android Studio** - Import the project and let Gradle sync

2. **Add Firebase Configuration**
   - Download `google-services.json` from your Firebase Console
   - Place it in `app/google-services.json`

3. **Configure Google Maps API Key**
   - Create a `local.properties` file in the project root
   - Add your API key:
     ```
     MAPS_API_KEY=YOUR_API_KEY_HERE
     ```

4. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

## Requirements
- Android Studio Arctic Fox or newer
- Android SDK 21+ (Android 5.0)
- Target SDK 32 (Android 12)

## Notes
- Do not commit workspace-specific files (`.idea/`, `local.properties`, `*.iml`)
- Replace app icons and screenshots with your own before publishing


