# Modrinth Launcher Android - Full Edition with Java & Renderers

A professional-grade Minecraft launcher for Android with complete Java runtime support, advanced graphics renderer selection, and full game launching capabilities.

## 🎮 Core Features

### Java Runtime Management
- **Multiple Java Versions**: Java 8, 17, 21, and **Java 25** (for versions 26.1+)
- **Auto-Detection**: Automatically detects device capabilities
- **On-Demand Download**: Download only what you need
- **Version Switching**: Seamless switching between Java versions

### Graphics Renderer Support
- 🎨 **OpenGL ES 2.0** - Compatibility mode (older devices)
- 🎨 **OpenGL ES 3.0+** - Modern Android devices
- 🎨 **OpenGL 4.x** - Desktop emulators
- 🎨 **Vulkan** - High-performance renderer (Android 7.0+)
- 🎨 **Zink** - OpenGL on Vulkan (Android 8.0+)
- 🎨 **Software Rendering** - Fallback for testing

### Game Features
- 🚀 **Launch Any Minecraft Version** - 1.0 through 26.1+
- 🎯 **Auto Version Detection** - Fetches all available versions
- 📦 **Mod Management** - Browse, search, install mods
- 🎒 **Modpack Support** - Full modpack installation
- 🔧 **Performance Tuning** - JVM optimization per device
- 👤 **Account Management** - Offline & Microsoft accounts
- 📊 **Graphics Configuration** - Per-version renderer selection

## 🛠️ Technical Stack

- **Language**: Kotlin 1.9+
- **UI**: Jetpack Compose + Material 3
- **Database**: Room ORM with multi-entity support
- **Networking**: Retrofit 2 + OkHttp
- **Graphics Detection**: OpenGL/EGL/Vulkan detection
- **Dependency Injection**: Hilt
- **Async**: Coroutines + Flow
- **Java**: Azul Zulu JDK (portable)

## 📋 System Requirements

**Device:**
- Android 7.0 (API 24) or higher
- 2GB RAM minimum (4GB recommended)
- 8GB+ storage for games
- 512MB app space

**Build:**
- Android SDK 34+
- JDK 17+
- Gradle 8.0+

## 🔨 Building

### Android Studio
```
1. Open project
2. Build → Build Bundle(s) / APK(s) → Build APK(s)
3. APK: app/build/outputs/apk/debug/app-debug.apk
```

### Command Line
```bash
./gradlew assembleDebug    # Debug build
./gradlew assembleRelease  # Release build
```

### GitHub Actions (Auto)
- Push to repo → APK auto-builds → Download from Actions tab

## 📱 Installation

1. Download APK from releases/artifacts
2. Enable "Unknown Sources": Settings → Security
3. Install APK file
4. Open "Modrinth Launcher"

## 🎮 Quick Start

### Step 1: Create Profile
- Tab: "Profile"
- Enter username
- Choose account type (Offline/Microsoft)
- Create profile

### Step 2: Select Java Version
- Tab: "Java Settings"
- View recommended version for your device
- Download required Java runtime
- Select preferred version

### Step 3: Choose Renderer
- Tab: "Graphics Settings"
- View device-recommended renderer
- Select from available renderers
- Adjust quality settings (FPS, texture quality, render distance)

### Step 4: Launch Minecraft
- Tab: "Versions"
- Select Minecraft version
- Tap "Launch"
- Game starts with optimized settings

## 📊 Java Version to Minecraft Version Mapping

| Java Version | Minecraft Versions |
|---|---|
| Java 8 | 1.0 - 1.16.5 |
| Java 17 | 1.17 - 1.20.x |
| Java 21 | 1.21 - 24.x |
| **Java 25** | **26.1+** (Latest) |

## 🎨 Renderer Performance

| Renderer | Performance | Compatibility | Memory |
|---|---|---|---|
| Vulkan | ⭐⭐⭐⭐⭐ | Android 7.0+ | Low |
| Zink | ⭐⭐⭐⭐ | Android 8.0+ | Medium |
| OpenGL ES 3 | ⭐⭐⭐ | Android 4.3+ | Medium |
| OpenGL ES 2 | ⭐⭐ | All devices | High |
| Software | ⭐ | All devices | Very High |

## 📝 Architecture

```
app/
├── data/
│   ├── api/               # Modrinth + Minecraft APIs
│   ├── database/          # Room entities & DAOs
│   ├── model/             # Data models (Java, Renderer, etc.)
│   └── repository/        # Java + Minecraft repositories
├── ui/
│   ├── screen/            # Compose screens
│   ├── theme/             # Material 3 theme
│   └── viewmodel/         # ViewModels
├── util/
│   ├── MinecraftLauncher.kt    # Launch configuration
│   ├── JavaDownloadManager.kt  # Java management
│   └── RendererOptimizer.kt    # Graphics optimization
├── di/
│   └── AppModule.kt       # Hilt injection
└── MainActivity.kt
```

## 🔍 Graphics Detection

App automatically detects:
- ✅ Available renderers (Vulkan, OpenGL, etc.)
- ✅ Max texture size
- ✅ GPU capabilities
- ✅ Recommended Java version
- ✅ Optimal renderer for device

## 🚀 Java Download URLs

Using **Azul Zulu OpenJDK** (optimized for performance):

- **Java 8**: `cdn.azul.com/zulu/zulu8.80.0.21-ca-jdk8.0.402-linux_x64.tar.gz`
- **Java 17**: `cdn.azul.com/zulu/zulu17.54.19-ca-jdk17.0.11-linux_x64.tar.gz`
- **Java 21**: `cdn.azul.com/zulu/zulu21.36.17-ca-jdk21.0.3-linux_x64.tar.gz`
- **Java 25**: `cdn.azul.com/zulu/zulu25.2.9-ca-jdk25.0.2-linux_x64.tar.gz`

## 🛡️ Troubleshooting

**"App won't launch"**
- Check device storage (need 5GB+)
- Verify Android 7.0+
- Restart device

**"Java download fails"**
- Check internet connection
- Verify storage space
- Try different Java version

**"Graphics errors"**
- Try different renderer
- Update Android OS
- Check GPU drivers

**"Game crashes on launch"**
- Reduce Java memory (-Xmx)
- Use different renderer
- Close other apps

## 📚 API Documentation

**Modrinth API**: https://docs.modrinth.com/
**Minecraft Launcher**: https://launcher.mojang.com/
**Azul Zulu**: https://www.azul.com/downloads/zulu/

## 📄 License

MIT License - Free for personal use

## ⚠️ Disclaimer

- Unofficial launcher (not by Mojang/Microsoft)
- Minecraft™ - Mojang Studios trademark
- Modrinth™ - Modrinth Inc trademark
- Use responsibly, respect mod licenses

## 🤝 Contributing

1. Fork repo
2. Create feature branch
3. Commit changes
4. Push & create PR

## 📞 Support

- 🐛 Bug reports: Open issue
- 💡 Features: Open issue
- ❓ Questions: Check existing issues

## 🗺️ Roadmap

- [ ] Fabric/Forge auto-install
- [ ] Quilt loader support
- [ ] Resource pack manager
- [ ] Shader pack support
- [ ] Screenshot gallery
- [ ] Play statistics
- [ ] Cloud sync
- [ ] Multiplayer tracking
- [ ] Custom JVM args UI
- [ ] Performance benchmarks

---

**Version**: 2.0 (Full Java & Renderer Support)  
**Last Updated**: 2026-07-02  
**Status**: Production Ready ✅
