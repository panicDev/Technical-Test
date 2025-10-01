# Product Sync Desktop App - Testing Guide

Comprehensive testing guide with detailed scenarios and expected results.

## 📋 Table of Contents

1. [Pre-Testing Checklist](#pre-testing-checklist)
2. [Functional Testing](#functional-testing)
3. [Offline/Online Testing](#offlineonline-testing)
4. [Edge Cases](#edge-cases)
5. [Performance Testing](#performance-testing)
6. [UI/UX Testing](#uiux-testing)

## ✅ Pre-Testing Checklist

Before starting tests, ensure:

- [ ] Application is built successfully
- [ ] User ID is configured correctly in `Main.kt`
- [ ] API endpoint is accessible (test with curl)
- [ ] Java 17+ is installed
- [ ] Internet connection is available

**Quick API Test**:
```bash
curl https://multitenant-apis-production.up.railway.app/products/YOUR_USER_ID
```

## 🧪 Functional Testing

### Test 1: Application Launch

**Objective**: Verify application starts correctly

**Steps**:
1. Run `./gradlew run`
2. Wait for application window to appear

**Expected Results**:
- ✅ Window opens with title "Product Sync - Offline/Online Desktop App"
- ✅ Window size is 1200x800 pixels
- ✅ Status bar shows connection status
- ✅ No error messages in console
- ✅ Empty state message appears if no products

**Pass Criteria**: All expected results met

---

### Test 2: Create Product (Online)

**Objective**: Create a new product while online

**Steps**:
1. Ensure internet is connected (green status bar)
2. Click the **+** (floating action button)
3. Enter title: "Test Laptop"
4. Enter description: "High-performance gaming laptop"
5. Click **Create**

**Expected Results**:
- ✅ Form screen appears with empty fields
- ✅ Title field has red error if left empty
- ✅ Create button is disabled when title is empty
- ✅ After clicking Create, returns to list screen
- ✅ New product appears in the list
- ✅ Success message: "Product created successfully"
- ✅ No cloud-off icon (product is synced)
- ✅ Product is visible in API (verify with curl)

**Pass Criteria**: Product created and synced immediately

**Verification Command**:
```bash
curl https://multitenant-apis-production.up.railway.app/products/YOUR_USER_ID
```

---

### Test 3: Edit Product

**Objective**: Update existing product

**Steps**:
1. Click on a product card or Edit icon
2. Modify title to "Updated Laptop"
3. Modify description to "Updated description"
4. Click **Update**

**Expected Results**:
- ✅ Form pre-fills with existing data
- ✅ Changes are saved
- ✅ Product updates in the list
- ✅ Success message: "Product updated successfully"
- ✅ Updated time refreshes
- ✅ Changes reflected in API

**Pass Criteria**: Product updates successfully

---

### Test 4: Delete Product

**Objective**: Delete a product

**Steps**:
1. Click the **Delete** icon on a product card
2. Confirm deletion in the dialog
3. Check the list

**Expected Results**:
- ✅ Confirmation dialog appears
- ✅ Dialog shows product title
- ✅ After confirming, product disappears from list
- ✅ Success message: "Product deleted successfully"
- ✅ Product removed from API

**Pass Criteria**: Product deleted successfully

---

### Test 5: Search Functionality

**Objective**: Search products by title or description

**Steps**:
1. Create multiple products with different titles
2. Enter search query in search bar
3. Observe filtered results

**Test Data**:
- Product 1: "Gaming Laptop" / "High-end gaming"
- Product 2: "Office Laptop" / "For productivity"
- Product 3: "Gaming Mouse" / "RGB lighting"

**Search Tests**:
| Query | Expected Results |
|-------|------------------|
| "Gaming" | Shows Product 1 & 3 |
| "Laptop" | Shows Product 1 & 2 |
| "productivity" | Shows Product 2 |
| "xyz" | Shows empty state |

**Pass Criteria**: Search works for title and description

---

## 🔌 Offline/Online Testing

### Test 6: Create Product (Offline)

**Objective**: Create product without internet

**Steps**:
1. Disconnect internet (disable WiFi or network)
2. Wait for status bar to show "Offline"
3. Click **+** button
4. Enter title: "Offline Product"
5. Enter description: "Created offline"
6. Click **Create**

**Expected Results**:
- ✅ Status bar turns red: "Offline - changes will sync when online"
- ✅ Product appears in list immediately
- ✅ Cloud-off icon appears next to product
- ✅ Pending operations counter shows "1"
- ✅ Success message: "Product created successfully"
- ✅ Product has negative ID (temporary local ID)

**Pass Criteria**: Product created locally, queued for sync

---

### Test 7: Update Product (Offline)

**Objective**: Update product while offline

**Steps**:
1. Ensure offline mode (red status bar)
2. Edit an existing product
3. Change title to "Offline Updated"
4. Click **Update**

**Expected Results**:
- ✅ Changes saved locally
- ✅ Cloud-off icon appears
- ✅ Pending operations counter increments
- ✅ Warning message: "This product has unsaved changes..."

**Pass Criteria**: Update queued successfully

---

### Test 8: Delete Product (Offline)

**Objective**: Delete product while offline

**Steps**:
1. Ensure offline mode
2. Delete a product
3. Confirm deletion

**Expected Results**:
- ✅ Product marked as unsynced
- ✅ Pending operations counter increments
- ✅ Product still visible but with indicator
- ✅ Delete operation queued

**Pass Criteria**: Delete operation queued

---

### Test 9: Sync After Online

**Objective**: Verify automatic sync when connection restored

**Steps**:
1. Perform several offline operations (create, update, delete)
2. Note the pending operations count (e.g., "3")
3. Reconnect internet
4. Observe status bar and operations count

**Expected Results**:
- ✅ Status bar changes to blue: "Synchronizing..."
- ✅ Pending operations counter decreases
- ✅ Status bar turns green: "Connected and synced"
- ✅ All cloud-off icons disappear
- ✅ Operations count reaches 0
- ✅ Temporary IDs replaced with server IDs
- ✅ Deleted products removed from list

**Pass Criteria**: All operations sync successfully

**Timing**: Sync should complete within 30 seconds

---

### Test 10: Manual Sync

**Objective**: Test manual sync button

**Steps**:
1. Create some offline operations
2. Reconnect internet (wait for green status)
3. Click the sync button in status bar
4. Observe sync progress

**Expected Results**:
- ✅ Sync starts immediately
- ✅ Button shows loading indicator
- ✅ Status bar shows "Synchronizing..."
- ✅ All operations process
- ✅ Success state appears

**Pass Criteria**: Manual sync works correctly

---

### Test 11: App Restart with Queue

**Objective**: Verify queue persists after app restart

**Steps**:
1. Disconnect internet
2. Create 2-3 products offline
3. Note the pending operations count
4. Close the application completely
5. Reopen the application
6. Check pending operations count

**Expected Results**:
- ✅ Products still visible
- ✅ Cloud-off icons present
- ✅ Pending operations count unchanged
- ✅ Status bar shows offline
- ✅ Queue data persists

**Pass Criteria**: Queue survives restart

---

### Test 12: Sync After Restart

**Objective**: Verify sync works after restart with queued operations

**Steps**:
1. Open app with pending operations from previous test
2. Connect to internet
3. Wait for auto-sync

**Expected Results**:
- ✅ Auto-sync triggers
- ✅ All operations process
- ✅ Queue clears
- ✅ Products sync to server

**Pass Criteria**: Sync completes successfully after restart

---

## 🎯 Edge Cases

### Test 13: Validation Tests

**Objective**: Test input validation

**Test Cases**:

| Field | Input | Expected Result |
|-------|-------|-----------------|
| Title | "" (empty) | Error: "Title cannot be empty" |
| Title | 256+ chars | Error: "Title must be less than 255 characters" |
| Description | 1001+ chars | Truncated to 1000, counter shows "1000/1000" |
| Title | "  spaces  " | Trimmed before save |

**Pass Criteria**: All validations work correctly

---

### Test 14: Network Interruption During Sync

**Objective**: Test behavior when network drops during sync

**Steps**:
1. Create multiple offline operations (5+)
2. Reconnect internet
3. Wait for sync to start
4. Immediately disconnect internet mid-sync

**Expected Results**:
- ✅ Partial sync completes
- ✅ Some operations succeed, some fail
- ✅ Failed operations remain in queue
- ✅ Retry counter increments
- ✅ Status shows sync error
- ✅ Next sync attempt processes remaining operations

**Pass Criteria**: Handles interruption gracefully

---

### Test 15: API Error Handling

**Objective**: Test handling of API errors

**Scenarios**:
1. **Invalid User ID**: Edit Main.kt with invalid ID
   - Expected: Error message, operations queue
   
2. **Rate Limiting**: Rapidly create many products
   - Expected: Queue excess operations
   
3. **Server Error**: (Simulate if possible)
   - Expected: Retry mechanism activates

**Pass Criteria**: Errors handled without crashes

---

### Test 16: Large Dataset

**Objective**: Test performance with many products

**Steps**:
1. Create 100+ products (use API directly or script)
2. Open application
3. Scroll through list
4. Search products
5. Perform CRUD operations

**Expected Results**:
- ✅ List loads within 2 seconds
- ✅ Smooth scrolling (no lag)
- ✅ Search is instant
- ✅ Operations complete normally
- ✅ No memory issues

**Pass Criteria**: Handles large datasets efficiently

---

### Test 17: Concurrent Operations

**Objective**: Test multiple rapid operations

**Steps**:
1. Rapidly click Create button 5 times
2. Fill forms quickly and submit
3. Observe queue processing

**Expected Results**:
- ✅ All operations queued
- ✅ Operations process in order (FIFO)
- ✅ No duplicates
- ✅ No crashes

**Pass Criteria**: Handles rapid operations

---

### Test 18: Special Characters

**Objective**: Test special characters in input

**Test Data**:
- Title: "Product™ with © symbols"
- Description: "UTF-8: 你好, مرحبا, 🎉"

**Expected Results**:
- ✅ Characters save correctly
- ✅ Display correctly in UI
- ✅ Sync to server correctly
- ✅ Search works with special chars

**Pass Criteria**: UTF-8 support works

---

## 📊 Performance Testing

### Test 19: Startup Time

**Objective**: Measure application startup time

**Steps**:
1. Close application
2. Start timer
3. Run `./gradlew run`
4. Stop timer when UI appears

**Benchmarks**:
- Cold start: < 5 seconds
- Warm start: < 2 seconds

**Pass Criteria**: Meets benchmark

---

### Test 20: Sync Performance

**Objective**: Measure sync speed

**Test Data**: 50 queued operations

**Steps**:
1. Create 50 operations offline
2. Connect internet
3. Measure sync time

**Benchmarks**:
- 50 operations: < 60 seconds
- Average: < 1.2 seconds per operation

**Pass Criteria**: Meets benchmark

---

### Test 21: Memory Usage

**Objective**: Monitor memory consumption

**Steps**:
1. Start application
2. Load 500+ products
3. Monitor RAM usage

**Benchmarks**:
- Idle: < 200 MB
- With 500 products: < 400 MB
- No memory leaks after 1 hour

**Pass Criteria**: Stays within limits

---

## 🎨 UI/UX Testing

### Test 22: Responsiveness

**Objective**: Test UI responsiveness

**Tests**:
- Resize window (minimum: 800x600)
- Maximize window
- Check all elements visible
- Test on different screen resolutions

**Pass Criteria**: UI adapts correctly

---

**Happy Testing! 🧪**