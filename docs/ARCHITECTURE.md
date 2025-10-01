# Product Sync Desktop App - Architecture Documentation

## 🏗️ Architecture Overview

This application follows **Clean Architecture** principles with clear separation of concerns across three main layers.

```
┌────────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │   UI/Compose │→ │  ViewModels  │→ │  UI States   │    │
│  │  Components  │← │   (MVVM)     │← │   & Events   │    │
│  └──────────────┘  └──────────────┘  └──────────────┘    │
└────────────────────────────────────────────────────────────┘
                            ↕
┌────────────────────────────────────────────────────────────┐
│                     DOMAIN LAYER                           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │  Use Cases   │→ │    Models    │← │  Interfaces  │    │
│  │ (Business    │  │  (Entities)  │  │ (Repository) │    │
│  │   Logic)     │  └──────────────┘  └──────────────┘    │
│  └──────────────┘                                          │
└────────────────────────────────────────────────────────────┘
                            ↕
┌────────────────────────────────────────────────────────────┐
│                      DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │ Repositories │→ │ Local DB     │  │  Remote API  │    │
│  │(Impl)        │→ │ (SQLDelight) │  │  (Ktor)      │    │
│  └──────────────┘  └──────────────┘  └──────────────┘    │
└────────────────────────────────────────────────────────────┘
```

## 📦 Layer Breakdown

### 1. Presentation Layer

**Responsibility**: Handle user interaction and display data

**Components**:
- **UI Components**: Reusable Compose composables
- **Screens**: Full-screen composables
- **ViewModels**: State management and business logic orchestration
- **UI State**: Immutable state representations
- **Events**: User interaction events

**Key Files**:
```
ui/
├── component/
│   ├── ConnectionStatusBar.kt
│   ├── ProductCard.kt
│   └── EmptyState.kt
├── screen/
│   ├── ProductListScreen.kt
│   ├── ProductFormScreen.kt
│   └── MainScreen.kt
└── viewmodel/
    ├── ProductViewModel.kt
    └── SyncViewModel.kt
```

**Design Patterns**:
- **MVVM**: Separation of UI and business logic
- **Unidirectional Data Flow**: Events up, state down
- **Single Source of Truth**: StateFlow for state management

### 2. Domain Layer

**Responsibility**: Business logic and rules (framework-independent)

**Components**:
- **Use Cases**: Single-responsibility business operations
- **Domain Models**: Pure data classes representing business entities
- **Repository Interfaces**: Abstract data access

**Key Files**:
```
domain/
├── model/
│   ├── Product.kt
│   ├── QueuedOperation.kt
│   └── SyncStatus.kt
└── usecase/
    ├── GetProductsUseCase.kt
    ├── CreateProductUseCase.kt
    ├── UpdateProductUseCase.kt
    ├── DeleteProductUseCase.kt
    └── SyncUseCase.kt
```

**Design Patterns**:
- **Use Case Pattern**: Encapsulate business operations
- **Domain Models**: Framework-agnostic entities
- **Repository Pattern**: Abstract data sources

### 3. Data Layer

**Responsibility**: Data access and storage

**Components**:
- **Repositories**: Implement domain interfaces
- **Local Database**: SQLDelight for local storage
- **Remote API**: Ktor for network communication
- **DTOs**: Data transfer objects for API
- **Mappers**: Convert between DTOs and domain models

**Key Files**:
```
data/
├── local/
│   ├── ProductDatabase.kt
│   ├── QueueDatabase.kt
│   └── DatabaseFactory.kt
├── remote/
│   ├── ApiClient.kt
│   ├── ProductApi.kt
│   └── dto/
│       ├── ProductDto.kt
│       └── ProductResponse.kt
└── repository/
    ├── ProductRepository.kt
    └── SyncRepository.kt
```

**Design Patterns**:
- **Repository Pattern**: Centralize data access
- **DTO Pattern**: Separate API models from domain
- **Factory Pattern**: Platform-specific database creation

## 🔄 Data Flow

### Read Flow (Query)
```
User Action → UI Event → ViewModel → Use Case → Repository 
→ Local DB → Flow → ViewModel → State → UI Update
```

### Write Flow (Command) - Online
```
User Action → UI Event → ViewModel → Use Case → Repository 
→ API Call → Success → Local DB Update → Flow → UI Update
```

### Write Flow (Command) - Offline
```
User Action → UI Event → ViewModel → Use Case → Repository 
→ Local DB Update → Queue Operation → Flow → UI Update
```

### Sync Flow
```
Network Available → Auto Sync Trigger → SyncUseCase 
→ Fetch Queue → Process Each Operation → API Call 
→ Success → Update Local DB → Clear Queue → UI Update
```

## 🗄️ Database Design

### Schema Design

**ProductEntity Table**:
- Primary storage for products
- Includes sync status flags
- Indexed for fast queries

**QueuedOperationEntity Table**:
- Stores pending operations
- Ordered by creation time
- Tracks retry attempts

### Database Operations

**CRUD Operations**:
```kotlin
// Create
suspend fun insertProduct(product: Product)

// Read
fun getAllProducts(): Flow<List<Product>>
fun getProductById(id: Long): Flow<Product?>

// Update
suspend fun updateProduct(id: Long, title: String, description: String?)

// Delete
suspend fun deleteProduct(id: Long)
```

**Sync Operations**:
```kotlin
// Mark as synced
suspend fun markAsSynced(id: Long)

// Get unsynced
fun getUnsyncedProducts(): Flow<List<Product>>
```

## 🌐 Network Layer

### API Client Configuration

**Base Setup**:
```kotlin
HttpClient(OkHttp) {
    install(ContentNegotiation) { json() }
    install(Logging) { level = LogLevel.INFO }
    install(HttpTimeout) { 
        requestTimeoutMillis = 30_000L 
    }
}
```

**Error Handling**:
- Automatic retry on network failure
- Queue operations on error
- User-friendly error messages

### Endpoints

| Operation | Endpoint | Method | Offline Support |
|-----------|----------|--------|-----------------|
| List | `/products/:userId` | GET | ✅ Cached |
| Get | `/products/:userId/:id` | GET | ✅ Cached |
| Create | `/products/:userId` | POST | ✅ Queued |
| Update | `/products/:userId/:id` | PUT | ✅ Queued |
| Delete | `/products/:userId/:id` | DELETE | ✅ Queued |

## 🔄 Synchronization Strategy

### Offline-First Approach

1. **Write Operations**: Always write to local DB first
2. **Queue Management**: Queue operations when offline
3. **Automatic Sync**: Sync when network available
4. **Conflict Resolution**: Server data takes precedence

### Sync Process

```
┌─────────────────────────────────────────────┐
│         Sync Process Flow                   │
└─────────────────────────────────────────────┘

1. Network Detection
   ↓
2. Fetch Pending Operations from Queue
   ↓
3. Process Each Operation (FIFO)
   ↓
4. Execute API Call
   ↓
5a. Success → Update Local DB → Clear from Queue
   ↓
5b. Failure → Increment Retry Count → Keep in Queue
   ↓
6. Fetch Latest from Server
   ↓
7. Update Local Cache
   ↓
8. Notify UI
```

### Queue Processing

**Strategy**:
- FIFO (First In, First Out)
- Max 3 retry attempts
- Exponential backoff
- Error logging

**Queue States**:
- `PENDING`: Ready to process
- `PROCESSING`: Currently being processed
- `FAILED`: Exceeded retry limit

## 🎨 UI Architecture

### State Management

**Unidirectional Data Flow**:
```
User Event → ViewModel → State Update → UI Recomposition
```

**State Container**:
```kotlin
data class ProductUiState(
    val products: List<Product>,
    val isLoading: Boolean,
    val error: String?,
    val searchQuery: String,
    val selectedProduct: Product?
)
```

**Event Handling**:
```kotlin
sealed class ProductEvent {
    data class SearchQueryChanged(val query: String)
    data class ProductSelected(val product: Product?)
    data class CreateProduct(val title: String, val description: String?)
    // ... more events
}
```

### Composable Structure

```
App (Material Theme)
└── MainScreen
    ├── ProductListScreen
    │   ├── TopAppBar
    │   ├── ConnectionStatusBar
    │   ├── SearchBar
    │   ├── ProductList (LazyColumn)
    │   │   └── ProductCard (repeated)
    │   └── FloatingActionButton
    └── ProductFormScreen (conditional)
        ├── TopAppBar
        ├── Form Fields
        └── Action Buttons
```

## 🔐 Security Considerations

### Data Security

- Local database not encrypted (add SQLCipher if needed)
- HTTPS enforced for all API calls
- No sensitive data stored
- User ID validation

### Network Security

- TLS/SSL certificate validation
- Timeout configurations
- Retry limits
- Rate limiting awareness

## ⚡ Performance Optimization

### Database Optimization

- **Indexing**: Fast queries on frequently accessed columns
- **Batch Operations**: Reduce DB transactions
- **Flow-based Updates**: Only emit when data changes

### Network Optimization

- **Connection Pooling**: Reuse HTTP connections
- **Compression**: Enable gzip compression
- **Caching**: Cache responses locally
- **Batch Sync**: Sync multiple operations together

### UI Optimization

- **Lazy Loading**: Use LazyColumn for lists
- **Stable Keys**: Prevent unnecessary recompositions
- **Remember**: Cache computed values
- **Immutable State**: Prevent object comparison overhead

## 🔍 Monitoring & Debugging

### Logging

**Ktor Logging**:
```kotlin
install(Logging) {
    logger = Logger.DEFAULT
    level = LogLevel.INFO
}
```

**Custom Logging**:
```kotlin
println("DEBUG: Processing operation ${operation.id}")
```

### Error Tracking

- Console logs for debugging
- Error state in UI
- Retry count tracking
- Last error message storage

## 📊 Code Metrics

### Estimated Lines of Code

| Layer | LOC | Files |
|-------|-----|-------|
| Presentation | ~800 | 10 |
| Domain | ~400 | 10 |
| Data | ~1000 | 12 |
| Total | ~2200 | 32 |

### Complexity

- Average cyclomatic complexity: 5-10
- Max nesting level: 3
- Max function length: 50 lines
- Max file length: 300 lines

## 🎯 Design Principles Applied

### SOLID Principles

- **S**: Single Responsibility - Each class has one reason to change
- **O**: Open/Closed - Open for extension, closed for modification
- **L**: Liskov Substitution - Interfaces used throughout
- **I**: Interface Segregation - Small, focused interfaces
- **D**: Dependency Inversion - Depend on abstractions

### Clean Code

- Meaningful names
- Small functions
- No side effects
- DRY (Don't Repeat Yourself)
- YAGNI (You Aren't Gonna Need It)

### Kotlin Best Practices

- Immutable data classes
- Null safety
- Extension functions
- Coroutines for async
- Flow for reactive streams

## 📚 References

- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [SQLDelight](https://cashapp.github.io/sqldelight/)
- [Ktor](https://ktor.io/)

---

**Last Updated**: 2024-01-15  
**Version**: 1.0.0  
**Author**: Musthofa Ali Ubaed  
**Email**: musthofaaliu@gmail.com