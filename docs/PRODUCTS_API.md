# Products API Documentation

## 🔗 Base URL
```
https://multitenant-apis-production.up.railway.app
```

## 📋 Products Endpoints Overview

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/products/:userId` | Get all products for a user |
| `GET` | `/products/:userId/:id` | Get specific product for a user |
| `POST` | `/products/:userId` | Create new product for a user |
| `PUT` | `/products/:userId/:id` | Update product for a user |
| `DELETE` | `/products/:userId/:id` | Delete product for a user |

---

## 📖 API Reference

### 1. Get All Products for a User

**`GET /products/:userId`**

Retrieves all products belonging to a specific user with pagination and search capabilities.

#### Parameters
- **Path Parameters:**
  - `userId` (string, required): The unique user identifier

- **Query Parameters:**
  - `page` (integer, optional): Page number (default: 1)
  - `limit` (integer, optional): Items per page (default: 10)

#### Example Request
```bash
curl https://multitenant-apis-production.up.railway.app/products/YOUR_USER_ID?page=1&limit=10
```

#### Example Response
```json
{
  "data": [
    {
      "id": 1,
      "title": "Gaming Laptop",
      "description": "High-performance gaming laptop with RTX graphics",
      "userId": "YOUR_USER_ID",
      "createdAt": "2024-01-15T10:30:00.000Z",
      "updatedAt": "2024-01-15T10:30:00.000Z",
      "user": {
        "userId": "YOUR_USER_ID",
        "createdAt": "2024-01-15T10:00:00.000Z"
      }
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 5,
    "total": 1,
    "pages": 1
  }
}
```

#### Response Codes
- `200` - Success
- `404` - User not found
- `429` - Rate limit exceeded
- `500` - Server error

---

### 2. Get Specific Product

**`GET /products/:userId/:id`**

Retrieves a specific product by ID for a user.

#### Parameters
- **Path Parameters:**
  - `userId` (string, required): The unique user identifier
  - `id` (integer, required): The product ID

#### Example Request
```bash
curl https://multitenant-apis-production.up.railway.app/products/YOUR_USER_ID/1
```

#### Example Response
```json
{
  "id": 1,
  "title": "Gaming Laptop",
  "description": "High-performance gaming laptop with RTX graphics",
  "userId": "YOUR_USER_ID",
  "createdAt": "2024-01-15T10:30:00.000Z",
  "updatedAt": "2024-01-15T10:30:00.000Z",
  "user": {
    "userId": "YOUR_USER_ID",
    "createdAt": "2024-01-15T10:00:00.000Z"
  }
}
```

#### Response Codes
- `200` - Success
- `404` - Product not found or doesn't belong to user
- `429` - Rate limit exceeded
- `500` - Server error

---

### 3. Create New Product

**`POST /products/:userId`**

Creates a new product for a specific user.

#### Parameters
- **Path Parameters:**
  - `userId` (string, required): The unique user identifier

- **Request Body:**
  ```json
  {
    "title": "Product Title",
    "description": "Product description (optional)"
  }
  ```

#### Validation Rules
- `title`: Required, 1-255 characters
- `description`: Optional, max 1000 characters
- `userId`: Must exist in the database

#### Example Request
```bash
curl -X POST https://multitenant-apis-production.up.railway.app/products/YOUR_USER_ID \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Wireless Headphones",
    "description": "Noise-cancelling Bluetooth headphones with 30-hour battery"
  }'
```

#### Example Response
```json
{
  "id": 2,
  "title": "Wireless Headphones",
  "description": "Noise-cancelling Bluetooth headphones with 30-hour battery",
  "userId": "YOUR_USER_ID",
  "createdAt": "2024-01-15T11:00:00.000Z",
  "updatedAt": "2024-01-15T11:00:00.000Z",
  "user": {
    "userId": "YOUR_USER_ID",
    "createdAt": "2024-01-15T10:00:00.000Z"
  }
}
```

#### Response Codes
- `201` - Created successfully
- `400` - Validation error or user not found
- `429` - Rate limit exceeded
- `500` - Server error

---

### 4. Update Product

**`PUT /products/:userId/:id`**

Updates an existing product for a specific user.

#### Parameters
- **Path Parameters:**
  - `userId` (string, required): The unique user identifier
  - `id` (integer, required): The product ID

- **Request Body:**
  ```json
  {
    "title": "Updated Product Title",
    "description": "Updated product description"
  }
  ```

#### Validation Rules
- `title`: Required, 1-255 characters
- `description`: Optional, max 1000 characters

#### Example Request
```bash
curl -X PUT https://multitenant-apis-production.up.railway.app/products/YOUR_USER_ID/2 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Premium Wireless Headphones",
    "description": "Premium noise-cancelling Bluetooth headphones with 35-hour battery and fast charging"
  }'
```

#### Example Response
```json
{
  "id": 2,
  "title": "Premium Wireless Headphones",
  "description": "Premium noise-cancelling Bluetooth headphones with 35-hour battery and fast charging",
  "userId": "YOUR_USER_ID",
  "createdAt": "2024-01-15T11:00:00.000Z",
  "updatedAt": "2024-01-15T11:30:00.000Z",
  "user": {
    "userId": "YOUR_USER_ID",
    "createdAt": "2024-01-15T10:00:00.000Z"
  }
}
```

#### Response Codes
- `200` - Updated successfully
- `400` - Validation error
- `404` - Product not found or doesn't belong to user
- `429` - Rate limit exceeded
- `500` - Server error

---

### 5. Delete Product

**`DELETE /products/:userId/:id`**

Deletes a specific product for a user.

#### Parameters
- **Path Parameters:**
  - `userId` (string, required): The unique user identifier
  - `id` (integer, required): The product ID

#### Example Request
```bash
curl -X DELETE https://multitenant-apis-production.up.railway.app/products/YOUR_USER_ID/2
```

#### Example Response
- No content (empty response body)

#### Response Codes
- `204` - Deleted successfully (no content)
- `404` - Product not found or doesn't belong to user
- `429` - Rate limit exceeded
- `500` - Server error

---

## 📊 Error Handling

### Error Response Format
All errors follow this consistent format:
```json
{
  "error": "Error description message"
}
```

### Validation Errors
For request validation failures:
```json
{
  "errors": [
    {
      "msg": "Title is required and must be less than 255 characters",
      "param": "title",
      "location": "body"
    }
  ]
}
```

### Common Error Codes
- `400` - Bad Request (validation errors, malformed JSON)
- `404` - Not Found (user or product doesn't exist)
- `429` - Too Many Requests (rate limit exceeded)
- `500` - Internal Server Error (database errors, server issues)

---

### cURL Examples
```bash
# Get all products with search
curl "https://multitenant-apis-production.up.railway.app/products/your-user-id?search=laptop&page=1&limit=5"

# Create a product
curl -X POST https://multitenant-apis-production.up.railway.app/products/your-user-id \
  -H "Content-Type: application/json" \
  -d '{"title": "New Product", "description": "Product description"}'

# Update a product
curl -X PUT https://multitenant-apis-production.up.railway.app/products/your-user-id/1 \
  -H "Content-Type: application/json" \
  -d '{"title": "Updated Product", "description": "Updated description"}'

# Delete a product
curl -X DELETE https://multitenant-apis-production.up.railway.app/products/your-user-id/1
```