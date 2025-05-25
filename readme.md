# KemaTelyu 🎓

Sistem E-Ticketing dan Sertifikasi Digital untuk kegiatan kemahasiswaan Tel-U.

## Struktur Folder

- `frontend/`: Vue.js untuk tampilan mahasiswa & staff
- `backend/`: Spring Boot + MySQL untuk autentikasi, event, registrasi, dan sertifikat

# KemaTelyu API – Specification (v1)

RESTful API for **Sistem E‑Ticketing dan Sertifikasi Digital KemaTelyu**.

Semua endpoint yang bertanda 🔐 membutuhkan header:

```
Authorization: Bearer <token>
```

Base URL default (local): `http://localhost:8888`

---

## 1. Auth Management

### a. User Login

Endpoint : **POST** `/api/auth/login`

**Request Header**

| Key          | Value              |
| ------------ | ------------------ |
| Content‑Type | `application/json` |
| Accept       | `application/json` |

**Request Body**

```json
{
  "email": "fadhil@telyu.ac.id",
  "password": "password123"
}
```

**Response Body**

```json
{
  "token": "<string>",
  "user": {
    "id": "<long>",
    "name": "<string>",
    "email": "<string>",
    "role": "<string>"
  }
}
```

---

### b. Get Current User Profile 🔐

Endpoint : **GET** `/api/auth/profile`

**Request Header**

| Key           | Value              |
| ------------- | ------------------ |
| Accept        | `application/json` |
| Authorization | `Bearer <token>`   |

**Response Body**

```json
{
  "id": "<long>",
  "name": "<string>",
  "email": "<string>",
  "role": "<string>"
}
```

---

## 2. Event Management

### a. List All Events

Endpoint : **GET** `/api/events`

**Request Header**

| Key    | Value              |
| ------ | ------------------ |
| Accept | `application/json` |

**Response Body**

```json
[
  {
    "id": "<long>",
    "title": "<string>",
    "description": "<string>",
    "date": "<date>",
    "createdBy": {
      "divisi": "<string>",
      "email": "<string>",
      "id": "<long>",
      "name": "<string>",
      "role": "<string>"
    }
  }
]
```

---

### b. Get Event by ID

Endpoint : **GET** `/api/events/{id}`

**Path Param**: `id` – event ID (Long, required)

**Request Header**

| Key    | Value              |
| ------ | ------------------ |
| Accept | `application/json` |

**Response Body**

```json
{
  "id": "<long>",
  "title": "<string>",
  "description": "<string>",
  "date": "<date>",
  "createdBy": {
    "divisi": "<string>",
    "email": "<string>",
    "id": "<long>",
    "name": "<string>",
    "role": "<string>"
  }
}
```

---

### c. Create Event 🔐 (staff only)

Endpoint : **POST** `/api/events`

**Request Header**

| Key           | Value              |
| ------------- | ------------------ |
| Content‑Type  | `application/json` |
| Accept        | `application/json` |
| Authorization | `Bearer <token>`   |

**Request Body**

```json
{
  "title": "event2",
  "description": "asdsda.",
  "date": "2025-06-01",
  "time": "09:00",
  "maxParticipant": 150,
  "fotoPath": "ai-ethics.jpg"
}
```

**Response Body**

```json
{
  "id": "<long>",
  "title": "<string>",
  "description": "<string>",
  "date": "<date>",
  "createdBy": {
    "divisi": "<string>",
    "email": "<string>",
    "id": "<long>",
    "name": "<string>",
    "role": "<string>"
  }
}
```

---

### d. Update Event 🔐

Endpoint : **PUT** `/api/events/{id}`

**Path Param**: `id` (Long, required)

**Request Header** same as *Create Event*

**Request Body** (example identical to response above, include fields yang ingin di‑update).

**Response Body** – event object updated.

---

### e. Delete Event 🔐

Endpoint : **DELETE** `/api/events/{id}`

**Request Header**

| Key           | Value            |
| ------------- | ---------------- |
| Authorization | `Bearer <token>` |

**Response**: HTTP **204 No Content**

---

## 3. Registration Management

### a. Register Mahasiswa to Event 🔐

Endpoint : **POST** `/api/events/{eventId}/register`

**Path Param**: `eventId` – ID event yang dituju

**Request Header**

| Key           | Value              |
| ------------- | ------------------ |
| Accept        | `application/json` |
| Authorization | `Bearer <token>`   |

**Request Body**: *(none)*

**Response Body**

```json
{
  "id": "<long>",
  "event": { "id": "<long>", "title": "<string>" },
  "mahasiswa": { "id": "<long>", "name": "<string>" },
  "date": "<date>",
  "status": "APPROVED",
}
```

---

### b. List Registrations for an Event 🔐 (staff)

Endpoint : **GET** `/api/events/{eventId}/registrations`

**Request Header** seperti di atas.

**Response Body**: *Array of registration objects* (lihat contoh JSON).

---

## 4. Participant Approval (Staff Only)

| Action                  | Method & Endpoint                               |
| ----------------------- | ----------------------------------------------- |
| Approve registration    | **PUT** `/api/events/participants/{id}/approve` |
| Reject registration     | **PUT** `/api/events/participants/{id}/reject`  |
| Cancel own registration | **DELETE** `/api/registrations/{id}`            |

All require `Authorization: Bearer <token>`.

**Responses**
*Approve / Reject*: HTTP 200 dengan registration object.
*Cancel*: HTTP 204 No Content.

---

## 5. Certificate Management

### a. List Certificates 🔐

Endpoint : **GET** `/api/certificates`

**Response Body**: Array of certificate objects.

---

### b. Download Certificate PDF 🔐

Endpoint : **GET** `/api/certificates/{id}/download`

Response headers include `Content‑Type: application/pdf` dan body binary.

---

## 6. Upload Management (Image)

### a. Upload Event Cover Image 🔐

Endpoint : **POST** `/api/upload/image`

**Request Header**

| Key           | Value                 |
| ------------- | --------------------- |
| Content‑Type  | `multipart/form-data` |
| Accept        | `application/json`    |
| Authorization | `Bearer <token>`      |

**Request Body (form‑data)**

| Key  | Type | Desc              |
| ---- | ---- | ----------------- |
| file | file | gambar (JPEG/PNG) |

**Response Body**

```json
{
  "path": "<string>" // relative path gambar
}
```

---

## 7. Environment Variables (Postman)

```
baseUrl = http://localhost:8888
bearerToken = <your_jwt_here>
```

---

> **Note**
>
> * Semua tanggal menggunakan format `YYYY‑MM‑DD`.
> * Field `<long>`, `<string>`, dll hanyalah placeholder.
> * Jika membutuhkan pagination di masa depan, tambahkan query param `page`, `size`, dll.

---

### Changelog

| Version | Date       | Notes                                          |
| ------- | ---------- | ---------------------------------------------- |
| 1.0     | 22‑05‑2025 | Initial spec generated from Postman collection |


## 😤 Git Workflow Tim Rosun (Proyek KemaTelyu)

Agar pengembangan terorganisir dan tidak bentrok antar developer, ikuti alur kerja Git berikut:

---

### 1. 🛠 Clone Repository Pertama Kali

```bash
git clone https://github.com/aprilianza/KemaTelyu-RosunTech.git
cd KemaTelyu-RosunTech
```

> Catatan: Saat pertama kali clone, hanya branch `main` yang otomatis muncul secara lokal. Branch lain (seperti `fadhi`, `athila`, dll) masih di remote dan perlu di-*fetch* dulu.

---

### 2. 🌱 Ambil Semua Branch dari Remote

```bash
git fetch origin                # ambil semua cabang dari remote
```

### 3. 🔀 Cek Daftar Branch yang Ada

```bash
git branch -r                  # lihat semua branch yang ada di remote
```

Contoh output:

```
  origin/main
  origin/fadhi
  origin/athila
  origin/april
```

### 4. 🚀 Checkout ke Branch Masing-masing dan clone dari main ke branch masing masing

```bash
git checkout -b [namamu] origin/[namamu]
git fetch origin
git merge origin/main
```

Contoh:

```bash
git checkout -b fadhi origin/fadhi
git fetch origin
git merge origin/main
```

> Ini akan membuat branch lokal `fadhi` yang melacak remote `origin/fadhi`

---

### 5. ✍️ Workflow Harian: Commit & Push di Branch Sendiri

```bash
git checkout [namamu]              # pastikan kerja di branch sendiri
git pull origin [namamu]           # update branch dari remote
# lakukan perubahan koding...
git add .
git commit -m "feat: tambah fitur registrasi"
git push origin [namamu]           # kirim perubahan ke GitHub
```

---

### 6. 🔄 Update dengan Perubahan dari Branch `main`

Jika ada perubahan terbaru di `main`, kamu harus sinkron dulu:

```bash
git checkout main
git pull origin main              # ambil update terbaru dari main

# lalu merge ke branch kamu
git checkout [namamu]
git merge main
```

> Atau jika kamu ingin rebase (lebih bersih history):

```bash
git checkout [namamu]
git fetch origin
git rebase origin/main
```

---

### 7. 📬 Jika Fitur Sudah Selesai, Buat Pull Request (PR)

* Buka GitHub
* Buat PR dari branch pribadi (`namamu`) ke `main`
* Tunggu review & approval dari anggota lain

---

### 8. 🧠 Troubleshooting

**Q: Kenapa branch orang lain gak muncul setelah clone?**

> Karena default clone hanya ambil `main`. Jalankan `git fetch origin` lalu `git checkout -b [nama] origin/[nama]`

**Q: Kenapa branch saya beda isinya dengan di GitHub?**

> Kemungkinan belum `pull` dari remote atau belum `push` setelah commit lokal.

**Q: Saya sudah buat branch, tapi orang lain gak lihat?**

> Pastikan kamu `git push origin [namamu]` setelah buat branch!

---

### ✅ Best Practices

* Jangan kerja di `main`
* Commit sering dengan deskripsi jelas (`feat:`, `fix:`, dll)
* Selalu `pull` dulu sebelum `push`
* Buat PR setelah fitur siap
* Bahas konflik bareng kalau ada bentrok

---

### ✨ Cabang Branch:

| Nama Anggota | Nama Branch |
| ------------ | ----------- |
| April        | `april`     |
| Bill         | `bill`      |
| Davino       | `davino`    |
| Fadhi        | `fadhi`     |
| Irgi         | `irgi`      |
| Athila       | `athila`    |

### Backend
```bash
cd backend
./mvnw spring-boot:run
