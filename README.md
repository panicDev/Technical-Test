# Product Sync Desktop Application

A cross-platform desktop application built with Kotlin Multiplatform and Compose Multiplatform that provides full offline/online synchronization with a REST API.

## 📚 Documentation

This project includes comprehensive documentation to help you get started and understand the architecture:

| Document | Description | Link |
|----------|-------------|------|
| 📖 **README.md** | Main documentation (you are here) | [README.md](README.md) |
| ⚙️ **SETUP_GUIDE.md** | Step-by-step installation and setup guide | [SETUP_GUIDE.md](docs/SETUP_GUIDE.md) |
| 🏗️ **ARCHITECTURE.md** | Technical architecture and design patterns | [ARCHITECTURE.md](docs/ARCHITECTURE.md) |
| 🧪 **TESTING_GUIDE.md** | Comprehensive testing scenarios | [TESTING_GUIDE.md](docs/TESTING_GUIDE.md) |
| 📋 **PROJECT_SUMMARY.md** | Complete project overview and summary | [PROJECT_SUMMARY.md](docs/PROJECT_SUMMARY.md) |
| 🔌 **PRODUCTS_API.md** | API endpoint documentation | [PRODUCTS_API.md](docs/PRODUCTS_API.md) |
| 📝 **CANDIDATE_INSTRUCTIONS.md** | Original project requirements | [CANDIDATE_INSTRUCTIONS.md](docs/CANDIDATE_INSTRUCTIONS.md) |

### 📖 Quick Navigation

- **New to the project?** Start with [SETUP_GUIDE.md](docs/SETUP_GUIDE.md)
- **Want to understand the code?** Read [ARCHITECTURE.md](docs/ARCHITECTURE.md)
- **Ready to test?** Check [TESTING_GUIDE.md](docs/TESTING_GUIDE.md)
- **Need API details?** See [PRODUCTS_API.md](docs/PRODUCTS_API.md)
- **Quick overview?** Read [PROJECT_SUMMARY.md](docs/PROJECT_SUMMARY.md)

---

## 📋 Features

- ✅ **Offline-First Architecture**: Full functionality without internet connection
- ✅ **Automatic Sync**: Queue operations offline, sync when online
- ✅ **Real-time Network Detection**: Visual indicators for connection status
- ✅ **CRUD Operations**: Create, Read, Update, Delete products
- ✅ **Search & Filter**: Fast local search capabilities
- ✅ **Queue Management**: Persistent queue for offline operations
- ✅ **Modern UI**: Material Design 3 with Compose Multiplatform
- ✅ **Error Handling**: Robust error handling with retry mechanism
- ✅ **Cross-Platform**: Runs on Windows, macOS, and Linux

## 🏗️ Architecture

### Clean Architecture with MVVM Pattern

```
┌─────────────────────────────────────────────┐
│           Presentation Layer                │
│  (Compose UI, ViewModels, UI Components)    │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│            Domain Layer                      │
│    (Use Cases, Domain Models, Business       │
│            Logic)                            │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│             Data Layer                       │
│  (Repositories, API Client, Local Database)  │
└─────────────────────────────────────────────┘
```

### Key Components

- **Local Database**: SQLDelight for type-safe SQL
- **Network Layer**: Ktor Client for HTTP communication
- **Dependency Injection**: Koin for Dependency Injection
- **State Management**: StateFlow and SharedFlow
- **Offline Queue**: Persistent operation queue

## 🚀 Getting Started

### Prerequisites

- **JDK 17 or higher**
- **Gradle 8.x** (included via wrapper)
- **Internet connection** (for initial setup and dependency download)

> 💡 **Need detailed setup instructions?** Check [SETUP_GUIDE.md](SETUP_GUIDE.md) for step-by-step guidance.

### Quick Start (3 Steps)

1. **Clone the repository**
```bash
git clone <your-repo-url>
cd ProductSyncApp
```

2. **Configure your User ID**

Open `composeApp/src/desktopMain/kotlin/com/android/technicaltest/data/repository/ProductRepository.kt` and replace the placeholder:

```kotlin
val userId = "YOUR_USER_ID_HERE" // Replace with your actual userId
```

3. **Build and run**
```bash
./gradlew run
```

> 📚 **For detailed installation steps**, see [SETUP_GUIDE.md](SETUP_GUIDE.md)  
> 🧪 **To verify everything works**, follow [TESTING_GUIDE.md](TESTING_GUIDE.md)

## 🔧 Configuration

### API Endpoint

The base URL is configured in `ApiClient.kt`:
```kotlin
private const val BASE_URL = "https://multitenant-apis-production.up.railway.app"
```

### Database Location

The local database is stored at:
- **Windows**: `C:\Users\<username>\.productsync\productsync.db`
- **macOS**: `/Users/<username>/.productsync/productsync.db`
- **Linux**: `/home/<username>/.productsync/productsync.db`

## 📦 Project Structure

```
ProductSyncApp/
├── composeApp/
│   ├── src/
│   │   ├── commonMain/
│   │   │   ├── kotlin/com/android/technicaltest/
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/           # Database & local storage
│   │   │   │   │   ├── remote/          # API client & DTOs
│   │   │   │   │   └── repository/      # Data repositories
│   │   │   │   ├── domain/
│   │   │   │   │   ├── model/           # Domain models
│   │   │   │   │   └── usecase/         # Business logic use cases
│   │   │   │   ├── ui/
│   │   │   │   │   ├── component/       # Reusable UI components
│   │   │   │   │   ├── screen/          # Screen composables
│   │   │   │   │   └── viewmodel/       # ViewModels
│   │   │   │   ├── util/                # Utilities
│   │   │   │   ├── di/                  # Dependency injection
│   │   │   │   └── App.kt               # Main app composable
│   │   │   └── sqldelight/              # SQL schema definitions
│   │   └── desktopMain/
│   │       └── kotlin/com/android/technicaltest/
│   │           ├── Main.kt              # Desktop entry point
│   │           └── data/local/
│   │               └── DatabaseFactory.jvm.kt
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## 🎯 Key Features Explained

### 1. Offline-First Approach

The app works seamlessly offline:
- All data is stored locally in SQLDelight database
- Operations (Create, Update, Delete) are queued when offline
- Queue persists through app restarts
- Automatic sync when connection is restored

### 2. Network Monitoring

Real-time connection status:
- Green: Online and synced
- Blue: Syncing in progress
- Red: Offline mode
- Orange: Sync error (will retry)

### 3. Queue System

Intelligent operation queuing:
- Operations are executed in order
- Failed operations are retried
- Retry count tracking
- Error logging for debugging

### 4. Data Synchronization

Two-way sync mechanism:
1. **Push**: Local changes → Server
2. **Pull**: Server data → Local database
3. **Conflict Resolution**: Server data takes precedence

## 🧪 Testing Guide

### Test Scenarios

#### 1. **Offline Create**
```
1. Disconnect internet
2. Create a new product
3. Verify product appears in list with cloud-off icon
4. Check pending operations counter
5. Reconnect internet
6. Wait for auto-sync or click sync button
7. Verify product synced (cloud-off icon disappears)
```

#### 2. **Offline Update**
```
1. Disconnect internet
2. Edit an existing product
3. Verify changes saved locally
4. Verify unsynced indicator appears
5. Reconnect internet
6. Verify changes sync to server
```

#### 3. **Offline Delete**
```
1. Disconnect internet
2. Delete a product
3. Verify product marked for deletion
4. Reconnect internet
5. Verify product deleted from server
```

#### 4. **App Restart with Pending Queue**
```
1. Create operations while offline
2. Close the application
3. Reopen the application
4. Verify queued operations persist
5. Connect to internet
6. Verify operations sync automatically
```

#### 5. **Network Recovery**
```
1. Work offline with multiple operations
2. Reconnect to internet
3. Observe auto-sync process
4. Verify all operations completed
5. Check for any errors
```

## 🛠️ Development

### Building from Source

```bash
# Clean build
./gradlew clean

# Build project
./gradlew build

# Run application
./gradlew run

# Run tests
./gradlew test

# Create distribution package
./gradlew packageDistributionForCurrentOS
```

### Distribution Packages

The app can be packaged for distribution:

```bash
# Create native installer for current OS
./gradlew packageDistributionForCurrentOS
```

Output locations:
- **Windows**: `composeApp/build/compose/binaries/main/msi/`
- **macOS**: `composeApp/build/compose/binaries/main/dmg/`
- **Linux**: `composeApp/build/compose/binaries/main/deb/`

## 📚 Technology Stack

| Technology | Purpose | Version |
|------------|---------|---------|
| **Kotlin** | Programming Language | 2.2.20  |
| **Compose Multiplatform** | UI Framework | 1.9.0   |
| **Ktor Client** | HTTP Client | 3.3.0   |
| **SQLDelight** | Local Database | 2.1.0   |
| **Kotlinx Coroutines** | Async Operations | 1.10.2  |
| **Kotlinx DateTime** | Date/Time Handling | 0.7.1   |
| **Material 3** | Design System | Latest  |

## 🏛️ Design Patterns

### Repository Pattern
Abstracts data sources (local DB, remote API) behind a clean interface.

### Use Case Pattern
Encapsulates business logic in single-responsibility classes.

### MVVM Pattern
Separates UI logic (ViewModel) from UI rendering (Composables).

### Observer Pattern
StateFlow for reactive state management.

### Singleton Pattern
ApiClient and NetworkMonitor as application-wide singletons.

> 📖 **For detailed architecture information**, see [ARCHITECTURE.md](ARCHITECTURE.md)

---

## 🧪 Testing

This project includes comprehensive test scenarios covering:

- ✅ Functional testing (CRUD operations)
- ✅ Offline/online synchronization
- ✅ Edge cases and error handling
- ✅ Performance benchmarks
- ✅ UI/UX validation

### Quick Test
```bash
# Test 1: Launch application
./gradlew run

# Test 2: Create product while online
# Click + button → Enter title → Create

# Test 3: Test offline mode
# Disconnect internet → Create product → Reconnect → Verify sync
```

> 🧪 **For complete testing guide with scenarios**, see [TESTING_GUIDE.md](TESTING_GUIDE.md)

---

## 📊 Database Schema

### Products Table
```sql
CREATE TABLE ProductEntity (
    id INTEGER PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,
    userId TEXT NOT NULL,
    createdAt TEXT NOT NULL,
    updatedAt TEXT NOT NULL,
    isSynced INTEGER NOT NULL,
    lastSyncedAt TEXT
);
```

### Queue Table
```sql
CREATE TABLE QueuedOperationEntity (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    operationType TEXT NOT NULL,
    productId INTEGER,
    title TEXT,
    description TEXT,
    userId TEXT NOT NULL,
    createdAt TEXT NOT NULL,
    retryCount INTEGER NOT NULL,
    lastError TEXT,
    status TEXT NOT NULL
);
```

## 🐛 Troubleshooting

### Common Issues

**1. Database file locked**
```
Solution: Close all instances of the application
```

**2. Network timeout**
```
Solution: Check internet connection or increase timeout in ApiClient.kt
```

**3. Sync fails repeatedly**
```
Solution: Check server status and API credentials
Clear queue: Delete ~/.productsync/productsync.db and restart
```

**4. Build fails**
```
Solution: 
- Run ./gradlew clean
- Check JDK version (must be 17+)
- Delete .gradle folder and rebuild
```

> 🔧 **For detailed troubleshooting**, see [SETUP_GUIDE.md#troubleshooting](SETUP_GUIDE.md#troubleshooting)

---

## 📝 API Documentation

Refer to `PRODUCTS_API.md` for complete API documentation.

### Quick Reference

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/products/:userId` | GET | Get all products |
| `/products/:userId/:id` | GET | Get product by ID |
| `/products/:userId` | POST | Create product |
| `/products/:userId/:id` | PUT | Update product |
| `/products/:userId/:id` | DELETE | Delete product |

> 🔌 **For complete API documentation**, see [PRODUCTS_API.md](PRODUCTS_API.md)

---

## 📄 License

This project is created for technical assessment purposes.

---

## 📞 Support

For questions or issues:
- **Email**: musthofaaliu@gmail.com
- **Repository**: https://github.com/panicDev/Technical-Test
- **Documentation**: See all docs in the [Documentation](#-documentation) section above

### Need Help?

| Question Type | Resource |
|---------------|----------|
| How to install? | [SETUP_GUIDE.md](SETUP_GUIDE.md) |
| How does it work? | [ARCHITECTURE.md](ARCHITECTURE.md) |
| How to test? | [TESTING_GUIDE.md](TESTING_GUIDE.md) |
| API endpoints? | [PRODUCTS_API.md](PRODUCTS_API.md) |
| Project overview? | [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) |
| Build issues? | [SETUP_GUIDE.md#troubleshooting](SETUP_GUIDE.md#troubleshooting) |

---

## 🙏 Acknowledgments

- Kotlin Multiplatform Team
- Compose Multiplatform Community
- SQLDelight Contributors
- Ktor Framework Team

---

**Made with ❤️ using Kotlin Multiplatform**

---

## 📚 Additional Resources

### External Documentation
- [Kotlin Multiplatform Docs](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform Docs](https://www.jetbrains.com/lp/compose-multiplatform/)
- [SQLDelight Documentation](https://cashapp.github.io/sqldelight/)
- [Ktor Client Documentation](https://ktor.io/docs/client.html)

### Project Documentation (Internal)
- [README.md](README.md) - You are here
- [SETUP_GUIDE.md](docs/SETUP_GUIDE.md) - Installation guide
- [ARCHITECTURE.md](docs/ARCHITECTURE.md) - Technical documentation
- [TESTING_GUIDE.md](docs/TESTING_GUIDE.md) - Testing scenarios
- [PROJECT_SUMMARY.md](docs/PROJECT_SUMMARY.md) - Complete overview
- [PRODUCTS_API.md](docs/PRODUCTS_API.md) - API reference
- [CANDIDATE_INSTRUCTIONS.md](docs/CANDIDATE_INSTRUCTIONS.md) - Requirements

---

**🚀 Ready to start? Go to [SETUP_GUIDE.md](SETUP_GUIDE.md)**
