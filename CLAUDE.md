# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Barbershop is a dual-role Android application (Java) for barbershop booking and shop management. It supports two user types:
- **Customers**: Browse shops, book appointments, view reviews
- **Shop Owners**: Manage services, view appointments, handle reviews

## Build & Run

```bash
# Open in Android Studio and sync Gradle, or:
./gradlew assembleDebug  # Build debug APK
./gradlew clean          # Clean build
```

**Prerequisites:**
- Android Studio (Gradle 7.1.2, compileSdk 32)
- `app/google-services.json` - Firebase config (not committed)
- `local.properties` with `MAPS_API_KEY=YOUR_KEY` for Google Maps

## Architecture

### Module Structure
- `app/` - Main application module
- `mylibrary/` - Shared Android library module

### Tech Stack
- **Language**: Java 8
- **UI**: Android Views with ViewBinding (not Jetpack Compose)
- **Navigation**: FragmentManager with FragmentTransaction
- **Backend**: Firebase (Auth, Realtime Database, Storage)
- **Local Storage**: SQLite (dbhelper for user data)
- **Maps**: Google Maps SDK
- **Image Loading**: Picasso

### Firebase Data Model
```
Users/{uid}           - Customer profile (user object)
Shops/{uid}           - Shop data (shop_details, services, appointments, reviews)
```

### Key Components

**Authentication Flow:**
- `firstscreen` → Entry point (customer/owner selection)
- `Login`/`Registration` → Customer auth (email+password, Google Sign-In, phone OTP)
- `Ownerlogin`/`OwenerRegistration` → Owner auth

**Customer Side:**
- `custHomeActivity` - Main container with bottom navigation (Home/Appointments/Profile)
- `Apphomescreen` - Shop list with location-based search
- `Appointmentscreen` - User's appointments
- `Profilescreen` - User profile management

**Owner Side:**
- `OwnerHomeActivity` - Main container with bottom navigation (Profile/Appointments/Services/Reviews)
- `Ownerefragmentprofile` - Owner profile
- `Ownerfragmentappointments` - Manage appointments
- `Ownerfragmentservices` - CRUD services
- `Ownerfragmentreviews` - View reviews

**Data Models:**
- `user` - Customer profile
- `Shop` - Shop/profile data
- `services` - Service offerings
- `appointment_in_userside` - Booking data
- `reviewdetail_class` - Review data

**Custom Adapters** (`custom_adapters/`):
- `shop_list_adapter` - Shop listing
- `serviceslist_*` - Service lists
- `appointmentlist_*_adapter` - Appointment lists for owner/user
- `reviews_list_selectedshop_adapter` - Reviews display

### Database Helpers
- `dbhelper` - SQLite for local user login data
- `dbhelperforowner` - SQLite for owner data

## Development Notes

- Fragments use `addToBackStack()` for navigation history
- Location permissions requested at runtime in `custHomeActivity`
- ProgressDialog used extensively for loading states
- Firebase Realtime Database listeners use `addValueEventListener` for real-time updates