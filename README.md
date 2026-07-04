# Android Modrinth App - Minecraft Java Edition Launcher

A feature-rich Android launcher for Minecraft Java Edition that combines the functionality of Modrinth with native Java Edition support.

## Features

### Core Launcher
- **Multi-Version Support**: Launch any Minecraft version from 1.0 to 26.2
- **Version Types**: Support for releases, snapshots, pre-releases, beta, and alpha versions
- **Instance Management**: Create and manage multiple game instances with custom configurations
- **Account Management**: Offline and online account support

### Java Runtime
- **Bundled Java Runtime**: Includes Java 8, 17, 21, and 25
- **Automatic Java Management**: Automatic download and version selection
- **Version-Specific Compatibility**: Automatic JVM argument optimization

### Rendering
- **OpenGL ES**: Standard mobile rendering
- **Vulkan**: High-performance rendering for compatible devices
- **Mobile GLUES**: Optimized graphics layer for mobile
- **Graphics Settings**: Customizable resolution, MSAA, shadow distance, render distance

### Mods & Content
- **Modrinth Integration**: Full mod browser integration with search
- **Mod Management**: Install, update, and remove mods from the launcher
- **Modpack Support**: Install and manage modpacks
- **Resource Pack & Shaders**: Manage resource packs and shader packs

### Touch Controls
- **On-Screen Controls**: Full touch control interface
- **Joystick Layout**: Analog stick support
- **Button Layout**: Customizable button layout
- **Hybrid Mode**: Combine joystick and buttons

## Architecture

### Project Structure
```
app/src/main/
├── java/com/evramantony/modrinthapp/
│   ├── data/
│   │   ├── api/           # API services (Modrinth, Minecraft)
│   │   ├── models/        # Data models
│   │   └── repository/    # Repository implementations
│   ├── domain/
│   │   ├── repository/    # Repository interfaces
│   │   ├── launcher/      # Game launching logic
│   │   └── authentication/ # Authentication logic
│   └── presentation/
│       ├── ui/
│       │   ├── screens/   # UI screens
│       │   └── theme/     # Theme configuration
│       └── MainActivity.kt
├── AndroidManifest.xml
└── res/
```

### Dependencies
- **Jetpack Compose**: Modern Android UI framework
- **Kotlin Coroutines**: Asynchronous programming
- **Retrofit**: HTTP client for API calls
- **Room**: Local database for caching
- **Hilt**: Dependency injection
- **Material Design 3**: UI components

## Getting Started

### Prerequisites
- Android Studio 2022.1+
- Android SDK 24+
- Kotlin 1.9+

### Build & Run
```bash
# Clone the repository
git clone https://github.com/evramantony-source/Android-Modrinth-app.git

# Build
./gradlew build

# Run on device/emulator
./gradlew installDebug
```

## Current Status

This is a **work in progress**. Core project structure, models, and API integrations are set up. Next phases include:

- [ ] Database implementation
- [ ] Modrinth API integration
- [ ] Game launching system
- [ ] Java runtime bundling
- [ ] Touch controls UI
- [ ] Renderer abstraction layer

## License

MIT License - See LICENSE file for details

## Credits

Inspired by [Modrinth](https://modrinth.com) and built for Minecraft Java Edition enthusiasts.
