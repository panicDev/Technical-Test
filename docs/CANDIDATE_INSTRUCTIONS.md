# Desktop Application Development Instructions

## 🎯 Project Overview

You are tasked with creating a desktop application using **Kotlin Multiplatform** that connects to our Products API with full offline/online synchronization capabilities. The application should work seamlessly whether the user has internet connection or not.

---

## 📋 Requirements

### Core Functionality

1. **Desktop Application**: Cross-platform desktop app using Kotlin Multiplatform (JVM target)
2. **API Integration**: Connect to our Products API
3. **Offline Support**: Full functionality when internet is unavailable
4. **Online Sync**: Automatic synchronization when internet is available
5. **Local Database**: Read-only local storage for products
6. **Queue System**: Queue CUD operations when offline

### Technical Requirements

- **Technology Stack**: **Kotlin Multiplatform** (REQUIRED) - JVM target for desktop
- **UI Framework**: Compose Multiplatform for desktop UI
- **Network Detection**: Detect online/offline status
- **Queue Management**: Persist queued operations during app restarts
- **Error Handling**: Robust error handling for network failures
- **User Interface**: Clean, intuitive UI with offline indicators using Compose

---

## 🔗 API Information

### Base URL
```
https://multitenant-apis-production.up.railway.app
```

### Your User ID
```
[CANDIDATE_USER_ID_WILL_BE_PROVIDED]
```

### API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/products/:userId` | Get all products for a user |
| `GET` | `/products/:userId/:id` | Get specific product for a user |
| `POST` | `/products/:userId` | Create new product for a user |
| `PUT` | `/products/:userId/:id` | Update product for a user |
| `DELETE` | `/products/:userId/:id` | Delete product for a user |

---

## 🎨 User Interface Requirements

### Essential UI Elements

1. **Connection Status Indicator**: Show online/offline status
2. **Sync Status**: Show last sync time and sync progress
3. **Product List**: Display products
4. **Product Form**: Add/Edit product form
5. **Queue Indicator**: Show pending operations count
6. **Error Messages**: Display sync errors and network issues

### UI States

- **Online + Synced**: Green indicator, "Connected and synced"
- **Online + Syncing**: Blue indicator, "Synchronizing..."
- **Offline**: Red indicator, "Offline - changes will sync when online"
- **Error**: Orange indicator, "Sync error - will retry"

---

## 🧪 Testing Requirements

### Test Scenarios

1. **Offline Create**: Create product while offline, verify it's queued
2. **Offline Update**: Update product while offline, verify it's queued
3. **Offline Delete**: Delete product while offline, verify it's queued
4. **Network Recovery**: Go online and verify queue is processed
5. **Sync Verification**: Verify local data matches remote after sync
6. **Error Handling**: Test API errors and network failures
7. **App Restart**: Verify queue persists through app restarts

---

## 📝 Deliverables

1. **Source Code**: Complete Kotlin Multiplatform application source code
2. **Documentation**: Setup and user guide
3. **Build Instructions**: How to build and run the application

---

## 🔧 Required Technology Stack

- **Kotlin Multiplatform**: REQUIRED - Use JVM target for desktop
- **Compose Multiplatform**: For declarative UI development

---

## 📞 Support & Resources

### API Documentation
- **Products API**: `PRODUCTS_API.md`
- **Live API**: https://multitenant-apis-production.up.railway.app

### Contact Information
- **Questions**: [Contact information will be provided]

### Submission Guidelines

1. **Code Repository**: Push to GitHub/GitLab with clear README
2. **Repository Access**: Invite michael.holtrack@gmail.com as collaborator for review
3. **Documentation**: Include setup guide and user manual
4. **Deadline**: [Deadline will be specified]

---

# Instruksi Pengembangan Aplikasi Desktop

## 🎯 Gambaran Proyek

Anda ditugaskan untuk membuat aplikasi desktop menggunakan **Kotlin Multiplatform** yang terhubung dengan Products API kami dengan kemampuan sinkronisasi offline/online penuh. Aplikasi harus bekerja dengan lancar baik saat pengguna memiliki koneksi internet maupun tidak.

---

## 📋 Persyaratan

### Fungsi Utama

1. **Aplikasi Desktop**: Aplikasi desktop lintas platform menggunakan Kotlin Multiplatform (target JVM)
2. **Integrasi API**: Terhubung dengan Products API kami
3. **Dukungan Offline**: Fungsionalitas penuh saat internet tidak tersedia
4. **Sinkronisasi Online**: Sinkronisasi otomatis saat internet tersedia
5. **Database Lokal**: Penyimpanan lokal read-only untuk produk
6. **Sistem Antrian**: Mengantri operasi CUD saat offline

### Persyaratan Teknis

- **Stack Teknologi**: **Kotlin Multiplatform** (WAJIB) - target JVM untuk desktop
- **Framework UI**: Compose Multiplatform untuk UI desktop
- **Deteksi Jaringan**: Mendeteksi status online/offline
- **Manajemen Antrian**: Menyimpan operasi yang diantri selama restart aplikasi
- **Penanganan Error**: Penanganan error yang robust untuk kegagalan jaringan
- **User Interface**: UI yang bersih dan intuitif dengan indikator offline menggunakan Compose

---

## 🔗 Informasi API

### URL Dasar
```
https://multitenant-apis-production.up.railway.app
```

### ID Pengguna Anda
```
[CANDIDATE_USER_ID_WILL_BE_PROVIDED]
```

### Endpoint API

| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| `GET` | `/products/:userId` | Ambil semua produk untuk pengguna |
| `GET` | `/products/:userId/:id` | Ambil produk spesifik untuk pengguna |
| `POST` | `/products/:userId` | Buat produk baru untuk pengguna |
| `PUT` | `/products/:userId/:id` | Update produk untuk pengguna |
| `DELETE` | `/products/:userId/:id` | Hapus produk untuk pengguna |

---

## 🎨 Persyaratan User Interface

### Elemen UI Penting

1. **Indikator Status Koneksi**: Tampilkan status online/offline
2. **Status Sinkron**: Tampilkan waktu sinkron terakhir dan progress sinkron
3. **Daftar Produk**: Tampilkan produk dengan pencarian/filter
4. **Form Produk**: Form tambah/edit produk
5. **Indikator Antrian**: Tampilkan jumlah operasi yang tertunda
6. **Pesan Error**: Tampilkan error sinkron dan masalah jaringan

### Status UI

- **Online + Tersinkron**: Indikator hijau, "Terhubung dan tersinkron"
- **Online + Menyinkron**: Indikator biru, "Sedang menyinkron..."
- **Offline**: Indikator merah, "Offline - perubahan akan disinkron saat online"
- **Error**: Indikator oranye, "Error sinkron - akan dicoba lagi"

---

## 🧪 Persyaratan Testing

### Skenario Testing

1. **Buat Offline**: Buat produk saat offline, verifikasi masuk antrian
2. **Update Offline**: Update produk saat offline, verifikasi masuk antrian
3. **Hapus Offline**: Hapus produk saat offline, verifikasi masuk antrian
4. **Pemulihan Jaringan**: Go online dan verifikasi antrian diproses
5. **Verifikasi Sinkron**: Verifikasi data lokal cocok dengan remote setelah sinkron
6. **Penanganan Error**: Test error API dan kegagalan jaringan
7. **Restart Aplikasi**: Verifikasi antrian bertahan setelah restart aplikasi

---

## 📝 Yang Harus Diserahkan

1. **Source Code**: Source code aplikasi Kotlin Multiplatform lengkap
2. **Dokumentasi**: Panduan setup dan pengguna
3. **Instruksi Build**: Cara build dan menjalankan aplikasi

---

## 🔧 Stack Teknologi yang Diwajibkan

- **Kotlin Multiplatform**: WAJIB - Gunakan target JVM untuk desktop
- **Compose Multiplatform**: Untuk pengembangan UI deklaratif

---

## 📞 Dukungan & Sumber Daya

### Dokumentasi API
- **Products API**: `PRODUCTS_API.md`
- **Live API**: https://multitenant-apis-production.up.railway.app

### Informasi Kontak
- **Pertanyaan**: [Informasi kontak akan disediakan]

### Panduan Pengumpulan

1. **Repository Kode**: Push ke GitHub/GitLab dengan README yang jelas
2. **Akses Repository**: Undang michael.holtrack@gmail.com sebagai kolaborator untuk review
3. **Dokumentasi**: Sertakan panduan setup dan manual pengguna
4. **Deadline**: [Deadline akan ditentukan]

---

*Semoga berhasil dengan pengembangan Anda!*