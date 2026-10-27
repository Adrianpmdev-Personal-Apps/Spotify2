<div align="center">

# 🎵 Spotify² — Android Project

### An educational Android application inspired by Spotify

**Developed as part of the 2nd year of the DAM program in 2025**

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![Supabase](https://img.shields.io/badge/Supabase-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white)
![Deezer API](https://img.shields.io/badge/Deezer_API-FEAA2D?style=for-the-badge&logo=deezer&logoColor=white)

[![Author](https://img.shields.io/badge/Author-adrianpmdev-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/adrianpmdev)

</div>

---

<br>

## 👋 About

**Spotify²** is an educational Android application inspired by Spotify, developed as a project during the **2nd year of the DAM program in 2025**.

The application provides a personalized music experience where users can register, log in, explore songs and artists, browse playlists, manage their profile and update their profile picture using the device camera.

The project integrates several technologies and external services into a single Android application:

> 🔥 Firebase Firestore & Analytics
> ☁️ Supabase Storage
> 🎵 Deezer API

> 📦 This project was created for educational purposes as part of the DAM program.

---

<br>

## ✨ Features

* 👤 User registration and login
* 🔐 Password hashing and local session persistence
* 🏠 Personalized home screen
* 🎵 Song and artist browsing
* 📚 Personal music library
* 📋 Playlist management
* ❤️ Personalized **"For You"** content
* ▶️ Dedicated music player
* 📸 Profile picture capture using the device camera
* ☁️ Multimedia storage using Supabase
* 🔗 Real-time music catalog data through the Deezer API

---

<br>

## 🛠️ Tech Stack

| Area                      | Technologies                                                                                                                                                                                                                                       |
| ------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 📱 **Platform**           | ![Android](https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=android&logoColor=white)                                                                                                                                           |
| 💻 **Language**           | ![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white)                                                                                                                                                 |
| 🔥 **Backend / Services** | ![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=flat-square&logo=firebase&logoColor=black) ![Supabase](https://img.shields.io/badge/Supabase-3ECF8E?style=flat-square&logo=supabase&logoColor=white)                            |
| 🎵 **Music API**          | ![Deezer API](https://img.shields.io/badge/Deezer_API-FEAA2D?style=flat-square&logo=deezer&logoColor=white)                                                                                                                                      |
| 🌐 **Networking**         | ![Retrofit](https://img.shields.io/badge/Retrofit-48B983?style=flat-square&logo=android&logoColor=white) ![Gson](https://img.shields.io/badge/Gson-4285F4?style=flat-square&logo=google&logoColor=white)                                       |
| 🖼️ **Image Loading**     | ![Glide](https://img.shields.io/badge/Glide-8BC34A?style=flat-square&logo=google&logoColor=white)                                                                                                                                                |
| 🎨 **UI**                 | ![Material Components](https://img.shields.io/badge/Material_Components-757575?style=flat-square&logo=materialdesign&logoColor=white) ![AndroidX](https://img.shields.io/badge/AndroidX-3DDC84?style=flat-square&logo=android&logoColor=white) |
| 🔗 **View Binding**       | ViewBinding                                                                                                                                                                                                                                        |
| 💾 **Local Storage**      | SharedPreferences                                                                                                                                                                                                                                  |

---

<br>

## 🏗️ Architecture & Project Structure

The application follows a package-based structure that separates activities, fragments, adapters, models, data access and utility classes.

### 📂 Package Structure

| Package            | Description                                                                                                                                                   |
| ------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `activities`       | Full-screen activities such as `SplashActivity`, `BienvenidaActivity`, `LoginActivity`, `RegistroActivity`, `SpotifyActivity` and `ReproductorActivity`.      |
| `fragments`        | Modular views hosted inside `SpotifyActivity`: `HomeFragment`, `BibliotecaFragment` and `PerfilFragment`.                                                     |
| `adapters`         | List adapters such as `PlaylistAdapter`, `ParaTiAdapter`, `ArtistaAdapter` and `CancionBibliotecaAdapter`, plus `CancionReproductorAdapter` for `ViewPager2`. |
| `models/api`       | Models used to map Deezer API responses such as `Cancion`, `Artista` and `Album`.                                                                             |
| `models/ui`        | Application-specific UI models such as `Usuario`, `Playlist` and `ParaTi`.                                                                                    |
| `data/api`         | Retrofit clients for Deezer and Supabase: `DeezerApiClient` and `SupabaseApiClient`.                                                                          |
| `data/db`          | Firestore access through `FirebaseService`.                                                                                                                   |
| `data/preferences` | Session persistence through `SharedPreferencesService`.                                                                                                       |
| `callbacks`        | Interfaces for asynchronous results such as `LoginCallback` and `UrlCallback`.                                                                                |
| `utils`            | General-purpose utilities such as image loading, password hashing and timestamp generation through `SpotifyUtils`.                                            |

---

<br>

## 🧭 Navigation Flow

### 🚪 Application Entry

**1. `SplashActivity`**

Displays the native splash screen and redirects the user after approximately 2 seconds:

* Active session → `SpotifyActivity`
* No active session → `BienvenidaActivity`

**2. `BienvenidaActivity`**

Public welcome screen with a background video hosted on Supabase.

From this screen, users can access:

* `LoginActivity`
* `RegistroActivity`

### 🏠 Main Application

**3. `SpotifyActivity`**

Acts as the main container of the application.

It includes:

* `TopToolbar`
* `BottomNavigationView`
* `FragmentContainerView`

Navigation between the main sections is handled through fragments:

* 🏠 Home → `HomeFragment`
* 📚 Library → `BibliotecaFragment`
* 👤 Profile → `PerfilFragment`

The special **Camera** action opens the device camera and uploads the captured image to Supabase Storage.

**4. `ReproductorActivity`**

Dedicated music player activity launched from the Library when a song is selected.

---

<br>

## 🖼️ UI Components

### 📋 RecyclerView

RecyclerView is used to display content in lists and horizontal carousels.

#### 🏠 Home

* `rvPlaylists` → Grid → `PlaylistAdapter`
* `rvParaTi` → Horizontal → `ParaTiAdapter`
* `rvArtistasPopulares` → Horizontal → `ArtistaAdapter`

#### 📚 Library

* Vertical song list → `CancionBibliotecaAdapter`

### 🎵 ViewPager2 Player

`ReproductorActivity` uses **ViewPager2** to allow users to **swipe between songs**.

Each page displays:

* Album artwork
* Song title
* Artist name

The pages are rendered by `CancionReproductorAdapter` using:

```text
item_cancion_reproductor.xml
```

When the user changes the current page:

1. `cancionActualIndex` is updated.
2. The current `MediaPlayer` instance is released.
3. The newly selected song is loaded automatically.

---

<br>

## 🗄️ Data & Services

### 🔥 Firebase Firestore

Firestore is used for the application's own persistent data.

| Collection      | Purpose                            |
| --------------- | ---------------------------------- |
| `usuarios`      | User registration and login data   |
| `tus_playlists` | Playlist information               |
| `para_ti`       | Personalized **"For You"** content |

**Firebase Analytics** is also integrated into the project.

### 🎵 Deezer API

The Deezer API provides real-time catalog information, including:

* Songs
* Artists
* Albums

Requests are handled through Retrofit using `DeezerApiClient`.

### ☁️ Supabase Storage

Supabase Storage is used for multimedia content.

| Bucket           | Content          |
| ---------------- | ---------------- |
| `Videos`         | Welcome video    |
| `Canciones`      | MP3 songs        |
| `ImagenesPerfil` | Profile pictures |

Access to stored files is handled through **signed URLs**, which expire after **10 minutes**.

---

<br>

## 🔐 Authentication & Session Management

### 📝 Registration

When a user registers:

1. User information is collected.
2. The password is hashed using **SHA-256**.
3. The user is stored in Firestore.

### 🔑 Login

The provided credentials are validated against the stored Firestore data.

### 💾 Session

The authenticated user's email is stored locally using `SharedPreferencesService`.

This allows the application to restore the user's session when it starts.

---

<br>

## ⚙️ Configuration & Setup

### ☁️ Supabase

Supabase credentials are configured through `local.properties`:

```properties
SUPABASE_API_KEY=your_api_key
SUPABASE_URL=your_supabase_url
```

### 🔥 Firebase

Firebase is configured using:

```text
google-services.json
```

### 🔐 Android Permissions

The application requires permissions for:

* 🌐 Internet access
* 📸 Camera access

These permissions are declared in `AndroidManifest.xml`.

---

<br>

## 📁 Project Structure

```text
Spotify2/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/aperezm/spotify2/
│   │   │   │   ├── activities/
│   │   │   ├── adapters/
│   │   │   ├── callbacks/
│   │   │   ├── data/
│   │   │   │   ├── api/
│   │   │   │   ├── db/
│   │   │   │   └── preferences/
│   │   │   ├── fragments/
│   │   │   ├── models/
│   │   │   │   ├── api/
│   │   │   │   └── ui/
│   │   │   └── utils/
│   │   │
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   ├── drawable/
│   │   │   ├── values/
│   │   │   └── menu/
│   │   │
│   │   └── AndroidManifest.xml
│   │
│   └── ...
├── build.gradle.kts
├── settings.gradle.kts
└── ...
```

---

<br>

## 🎓 Educational Purpose

This project was developed as part of the **2nd year of the DAM program in 2025**.

Its main goal was to put Android development concepts into practice and learn how to integrate multiple technologies and external services into a complete application.

The project covers topics such as:

* Android application development
* REST API consumption
* Firebase Firestore
* Firebase Analytics
* Supabase Storage
* Authentication and session management
* RecyclerView and ViewPager2
* Media playback with `MediaPlayer`
* Camera integration
* Asynchronous operations
* Local data persistence
* Third-party service integration

---

<br>

## 📄 License

Source code licensed under the **MIT License**. Spotify branding, Deezer catalog data and any music files are excluded. See [LICENSE](LICENSE) for details.

---

<br>

## 👤 Author

[@adrianpmdev](https://github.com/adrianpmdev) · Junior Full Stack Developer

---

<br>

<div align="center">

✨ Made in **2025** by [@adrianpmdev](https://github.com/adrianpmdev) ✨

</div>
