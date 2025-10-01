# Product Sync Desktop App - Setup Guide

Complete step-by-step guide to set up and run the Product Sync Desktop Application.

## 📋 Table of Contents

1. [System Requirements](#system-requirements)
2. [Installation Steps](#installation-steps)
3. [Configuration](#configuration)
4. [First Run](#first-run)
5. [Verification](#verification)
6. [Troubleshooting](#troubleshooting)

## 💻 System Requirements

### Minimum Requirements

- **Operating System**: Windows 10+, macOS 11+, or Linux (Ubuntu 20.04+)
- **RAM**: 4 GB minimum, 8 GB recommended
- **Storage**: 500 MB free space
- **Java**: JDK 17 or higher

### Recommended Requirements

- **RAM**: 8 GB or more
- **Storage**: 1 GB free space
- **Internet**: Stable connection for initial setup and sync

## 🔧 Installation Steps

### Step 1: Install Java Development Kit (JDK)

#### Windows

1. Download JDK 17 from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [AdoptOpenJDK](https://adoptium.net/)
2. Run the installer
3. Verify installation:
```cmd
java -version
```

#### macOS

Using Homebrew:
```bash
brew install openjdk@17
```

Verify:
```bash
java -version
```

#### Linux (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install openjdk-17-jdk
java -version
```

### Step 2: Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/ProductSyncApp.git
cd ProductSyncApp
```

### Step 3: Configure User ID

Open `composeApp/src/desktopMain/kotlin/com/android/technicaltest/data/repository/ProductRepository.kt`:

```kotlin
// Replace with your actual user ID
private val userId = "YOUR_USER_ID" // ⚠️ CHANGE THIS
```

**⚠️ IMPORTANT**: Replace `"YOUR_USER_ID"` with your actual User ID provided in the documentation.

### Step 4: Build the Project

#### First-time build (downloads dependencies)

```bash
# Linux/macOS
./gradlew build

# Windows
gradlew.bat build
```

This may take 5-10 minutes on first run as it downloads all dependencies.

### Step 5: Run the Application

```bash
# Linux/macOS
./gradlew run

# Windows
gradlew.bat run
```

## ⚙️ Configuration

### API Configuration

If you need to change the API endpoint, edit `composeApp/src/commonMain/kotlin/com/android/technicaltest/data/remote/ApiClient.kt`:

```kotlin
object ApiClient {
    private const val BASE_URL = "https://multitenant-apis-production.up.railway.app"
    // Change BASE_URL if needed
}
```

### Network Timeout

To adjust network timeout, edit the same file:

```kotlin
install(HttpTimeout) {
    requestTimeoutMillis = 30_000L  // 30 seconds
    connectTimeoutMillis = 30_000L  // 30 seconds
    socketTimeoutMillis = 30_000L   // 30 seconds
}
```

### Database Location

The database is automatically created at:
- **Windows**: `C:\Users\<YourUsername>\.productsync\productsync.db`
- **macOS**: `/Users/<YourUsername>/.productsync/productsync.db`
- **Linux**: `/home/<YourUsername>/.productsync/productsync.db`

To change the location, edit `composeApp/src/desktopMain/kotlin/com/android/technicaltest/data/local/DatabaseFactory.jvm.kt`:

```kotlin
actual class DatabaseFactory {
    actual fun createDriver(): SqlDriver {
        val databasePath = File(System.getProperty("user.home"), ".productsync")
        // Change ".productsync" to your preferred folder name
    }
}
```

## 🚀 First Run

### 1. Launch Application

When you first run the application:

```bash
./gradlew run
```

You should see:
- Window opens with title "Product Sync - Offline/Online Desktop App"
- Connection status bar (green if online, red if offline)
- Empty product list with message "No products yet. Click + to add one!"

### 2. Test Connection

The status bar should show:
- **Online**: Green background with "Connected and synced"
- **Offline**: Red background with "Offline - changes will sync when online"

### 3. Create First Product

1. Click the **+** (Add) button (blue floating button at bottom-right)
2. Fill in:
   - **Title**: "Test Product"
   - **Description**: "This is my first test product"
3. Click **Create**
4. Product should appear in the list

### 4. Test Offline Mode

1. Disconnect your internet
2. Create another product
3. Notice the cloud-off icon next to the product
4. Check the status bar shows "Offline" with pending operations count
5. Reconnect internet
6. Click the sync button or wait for auto-sync
7. Verify the product syncs (cloud-off icon disappears)

## 🐛 Troubleshooting

### Issue: Application won't start

**Error**: `JAVA_HOME not set` or `java: command not found`

**Solution**:
```bash
# Check if Java is installed
java -version

# If not installed, install JDK 17+
# Then set JAVA_HOME environment variable

# Linux/macOS
export JAVA_HOME=/path/to/jdk-17

# Windows (Command Prompt)
set JAVA_HOME=C:\Path\To\jdk-17

# Windows (PowerShell)
$env:JAVA_HOME="C:\Path\To\jdk-17"
```

### Issue: Build fails with "Could not resolve dependencies"

**Solution**:
```bash
# Clear Gradle cache
./gradlew clean
rm -rf ~/.gradle/caches/

# Rebuild
./gradlew build --refresh-dependencies
```

### Issue: "User not found" error

**Solution**:
- Verify your User ID is correct in `ProductRepository.kt`
- Test the API manually:
```bash
curl https://multitenant-apis-production.up.railway.app/products/YOUR_USER_ID
```
- If the API returns 404, contact support for the correct User ID

### Issue: Database locked error

**Solution**:
```bash
# Close all instances of the application
# Then delete the database (data will be re-synced)
rm ~/.productsync/productsync.db

# Restart the application
./gradlew run
```

### Issue: Sync not working

**Solutions**:

1. **Check internet connection**:
check your connection

2. **Check application logs**:
Look for errors in the console output

3. **Manual sync**:
Click the sync button in the status bar

4. **Clear queue**:
```sql
-- Use SQLite browser to open ~/.productsync/productsync.db
DELETE FROM QueuedOperationEntity;
```

### Issue: UI not responding

**Solution**:
```bash
# Kill the process
# Linux/macOS
pkill -f ProductSyncApp

# Windows
taskkill /F /IM ProductSyncApp.exe

# Restart
./gradlew run
```

### Issue: Gradle daemon issues

**Solution**:
```bash
# Stop all Gradle daemons
./gradlew --stop

# Clean and rebuild
./gradlew clean build
```

## 🏗️ Building Distribution Packages

### For Current OS

```bash
./gradlew packageDistributionForCurrentOS
```

### For Specific OS

```bash
# Windows MSI
./gradlew packageMsi

# macOS DMG
./gradlew packageDmg

# Linux DEB
./gradlew packageDeb
```

Output location: `composeApp/build/compose/binaries/main/`

## 🔍 Debugging

### Enable Verbose Logging

Edit `ApiClient.kt`:

```kotlin
install(Logging) {
    logger = Logger.DEFAULT
    level = LogLevel.ALL  // Change from INFO to ALL
}
```

### Check Database Contents

Use SQLite browser:

```bash
# Install SQLite browser
# macOS
brew install --cask db-browser-for-sqlite

# Windows
# Download from https://sqlitebrowser.org/

# Open database
# ~/.productsync/productsync.db
```

### Monitor Network Requests

Check console output for Ktor logs showing:
- Request URLs
- Response codes
- Error messages

## 📚 Additional Resources

- [Kotlin Multiplatform Docs](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform Docs](https://www.jetbrains.com/lp/compose-multiplatform/)
- [SQLDelight Documentation](https://cashapp.github.io/sqldelight/)
- [Ktor Client Documentation](https://ktor.io/docs/client.html)

## 💡 Tips for Development

1. **Hot Reload**: Compose supports limited hot reload. Restart the app for major changes.

2. **Fast Iteration**: Keep the app running and test changes quickly with `./gradlew installDist`

3. **Database Reset**: Delete `~/.productsync/productsync.db` to start fresh

4. **Test Offline**: Disable network in OS settings, not just WiFi

5. **Multiple Instances**: You can run multiple instances with different User IDs for testing

## 📞 Getting Help

If you encounter issues not covered here:

1. Check the main README.md
2. Review API documentation (PRODUCTS_API.md)
3. Contact support with:
   - Error messages from console
   - Steps to reproduce
   - OS and Java version
   - Application logs

---

**Happy Coding! 🚀**