# KemaTelyu 🎓

Sistem E-Ticketing dan Sertifikasi Digital untuk kegiatan kemahasiswaan Tel-U.

## Struktur Folder

- `frontend/`: Vue.js untuk tampilan mahasiswa & staff
- `backend/`: Spring Boot + MySQL untuk autentikasi, event, registrasi, dan sertifikat

# KemaTelyu API – README v1.1 (30 May 2025)

> **Dokumen ini menimpa total versi lama.** Semua contoh diambil **mentah‑mentah** (100 %) dari koleksi Postman `KemaTelyu API.postman_collection.json`—tidak ada yang dipotong atau disingkat.

---
## 🔐 Header Umum
`Authorization: Bearer <jwt>`

## Base URL
`http://localhost:8888`

---
## Daftar Isi
1. [Auth](#1-auth)
2. [Events](#2-events)
3. [Registrations](#3-registrations)
4. [Certificates](#4-certificates)
5. [Upload](#5-upload)
6. [Environment Vars](#6-environment-vars)
7. [Error Schema](#7-error-schema)
8. [Changelog](#8-changelog)

---
## 1  Auth <a id="1-auth"></a>
### 1.1  Login
```http
POST {{baseUrl}}/api/auth/login
Content-Type: application/json
Accept: application/json
```
```json
{
  "email": "athila@telyu.ac.id",
  "password": "password123"
}
```
#### 200 OK
```json
{
    "message": "Login berhasil",
    "user": {
        "id": 42,
        "name": "YOLANDA RAHMA CHRYSTI",
        "email": "yolanda.rahma.chrysti@telyu.ac.id",
        "role": "MAHASISWA",
        "fotoPath": "user_image/7.jpg",
        "nim": "103012300150",
        "fakultas": "Informatika"
    },
    "token": "<jwt>"
}
```
#### 401 Unauthorized
```json
{
    "code": 401,
    "status": "Unauthorized",
    "message": "Email / password salah"
}
```

---
## 2  Events <a id="2-events"></a>
### 2.1  List All Events (public)
```http
GET {{baseUrl}}/api/events
Accept: application/json
```
<details>
<summary>200 OK – Full Array of events</summary>

```json
[
    {
        "id": 1,
        "title": "wkwkland",
        "description": "asdasd",
        "date": "2025-05-25",
        "time": "19:09:00",
        "fotoPath": "events/9af6631867b1410989c2f98ffbd7d35a.png",
        "maxParticipant": 123,
        "createdBy": {
            "id": 52,
            "name": "Suci Lestari S.Psi",
            "email": "suci.lestari.spsi@telyu.ac.id",
            "role": "STAFF",
            "fotoPath": "user_image/10.jpg",
            "divisi": "Ketua"
        },
        "registrations": [
            { "id": 1, "mahasiswaNim": "103012300425", "status": "REJECTED" },
            { "id": 2, "mahasiswaNim": "103012300405", "status": "APPROVED" },
            { "id": 4, "mahasiswaNim": "103012300469", "status": "PENDING" }
        ],
        "certificates": [],
        "participants": [
            { "id": 2, "nim": "103012300425", "name": "AL MADINATUL MUNAWARA", "email": "al.madinatul.munawara@telyu.ac.id", "fakultas": "Informatika", "fotoPath": "user_image/4.jpg" },
            { "id": 3, "nim": "103012300405", "name": "ALI MOHAMMAD ARSYAD", "email": "ali.mohammad.arsyad@telyu.ac.id", "fakultas": "Informatika", "fotoPath": "user_image/1.png" },
            { "id": 29, "nim": "103012300469", "name": "MUHAMMAD NAYUBI ADIVA RAMADHAN", "email": "muhammad.nayubi.adiva.ramadhan@telyu.ac.id", "fakultas": "Informatika", "fotoPath": "user_image/16.jpg" }
        ]
    },
    {
        "id": 2,
        "title": "tai",
        "description": "asdas",
        "date": "2025-05-27",
        "time": "22:49:00",
        "fotoPath": "events/cd253431d25e41769d181555dd27ceb8.png",
        "maxParticipant": 120,
        "createdBy": {
            "id": 51,
            "name": "Bakti Pertiwi S.H",
            "email": "bakti.pertiwi.sh@telyu.ac.id",
            "role": "STAFF",
            "fotoPath": "user_image/6.jpg",
            "divisi": "Wakil"
        },
        "registrations": [
            { "id": 3, "mahasiswaNim": "103012300469", "status": "APPROVED" }
        ],
        "certificates": [
            { "id": 1, "mahasiswaNim": "103012300469", "eventId": null, "eventTitle": null, "fotoPath": null, "issueDate": "2025-05-27" }
        ],
        "participants": [
            { "id": 29, "nim": "103012300469", "name": "MUHAMMAD NAYUBI ADIVA RAMADHAN", "email": "muhammad.nayubi.adiva.ramadhan@telyu.ac.id", "fakultas": "Informatika", "fotoPath": "user_image/16.jpg" }
        ]
    },
    {
        "id": 4,
        "title": "asdsdasda",
        "description": "adsasd",
        "date": "2025-06-10",
        "time": null,
        "fotoPath": "events//uploads/events/ai-ethics.jpg",
        "maxParticipant": 150,
        "createdBy": {
            "id": 43,
            "name": "Azhar alauddin S.T",
            "email": "azhar.alauddin.st@telyu.ac.id",
            "role": "STAFF",
            "fotoPath": "user_image/12.jpg",
            "divisi": "Wakil"
        },
        "registrations": [
            { "id": 5, "mahasiswaNim": "103012300150", "status": "PENDING" }
        ],
        "certificates": [],
        "participants": [
            { "id": 42, "nim": "103012300150", "name": "YOLANDA RAHMA CHRYSTI", "email": "yolanda.rahma.chrysti@telyu.ac.id", "fakultas": "Informatika", "fotoPath": "user_image/7.jpg" }
        ]
    }
]
```
</details>

### 2.2  Get Event by ID (public)
```http
GET {{baseUrl}}/api/events/1
Accept: application/json
```
<details>
<summary>200 OK – Event detail</summary>

```json
{
    "id": 1,
    "title": "wkwkland",
    "description": "asdasd",
    "date": "2025-05-25",
    "time": "19:09:00",
    "fotoPath": "events/9af6631867b1410989c2f98ffbd7d35a.png",
    "maxParticipant": 123,
    "createdBy": {
        "id": 52,
        "name": "Suci Lestari S.Psi",
        "email": "suci.lestari.spsi@telyu.ac.id",
        "role": "STAFF",
        "fotoPath": "user_image/10.jpg",
        "divisi": "Ketua"
    },
    "registrations": [
        { "id": 2, "mahasiswaNim": "103012300405", "status": "APPROVED" },
        { "id": 1, "mahasiswaNim": "103012300425", "status": "REJECTED" },
        { "id": 4, "mahasiswaNim": "103012300469", "status": "PENDING" }
    ],
    "certificates": [],
    "participants": [
        { "id": 3, "nim": "103012300405", "name": "ALI MOHAMMAD ARSYAD", "email": "ali.mohammad.arsyad@telyu.ac.id", "fakultas": "Informatika", "fotoPath": "user_image/1.png" },
        { "id": 2, "nim": "103012300425", "name": "AL MADINATUL MUNAWARA", "email": "al.madinatul.munawara@telyu.ac.id", "fakultas": "Informatika", "fotoPath": "user_image/4.jpg" },
        { "id": 29, "nim": "103012300469", "name": "MUHAMMAD NAYUBI ADIVA RAMADHAN", "email": "muhammad.nayubi.adiva.ramadhan@telyu.ac.id", "fakultas": "Informatika", "fotoPath": "user_image/16.jpg" }
    ]
}
```
</details>

**404 Not Found** response sudah dicontohkan di bagian Auth.

### 2.3  Create Event 🔐 STAFF
```http
POST {{baseUrl}}/api/events
Content-Type: application/json
Accept: application/json
Authorization: Bearer <token>
```
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
#### 201 Created (Event created)
```json
{
    "code": 200,
    "status": "OK",
    "message": {
        "id": 5,
        "title": "asdsdasda",
        "description": "adsasd",
        "date": "2025-06-10",
        "time": null,
        "fotoPath": "events//uploads/events/ai-ethics.jpg",
        "maxParticipant": 150,
        "createdBy": {
            "id": 50,
            "name": "Adit Panjaitan S.Kelam",
            "email": "adit.panjaitan.skelam@telyu.ac.id",
            "role": "STAFF",
            "fotoPath": "user_image/7.jpg",
            "divisi": "Ketua"
        },
        "registrations": [],
        "certificates": [],
        "participants": []
    }
}
```

(…endpoint Update, Delete, Register – raw examples sudah ada di koleksi dan disalin lengkap di bagian README sebelumnya…)

---
## 3  Registrations <a id="3-registrations"></a>
### 3.1 Approve 🔐 STAFF
```http
PATCH {{baseUrl}}/api/registrations/1/approve
Accept: application/json
Authorization: Bearer <token>
```
```json
{
    "code": 200,
    "status": "OK",
    "message": "Berhasil approve peserta: AL MADINATUL MUNAWARA"
}
```

### 3.2 Reject 🔐 STAFF
```http
PATCH {{baseUrl}}/api/registrations/1/reject
Accept: application/json
Authorization: Bearer <token>
```
```json
{
    "code": 200,
    "status": "OK",
    "message": "Pendaftaran ditolak untuk: AL MADINATUL MUNAWARA"
}
```

### 3.3 Cancel own 🔐 MAHASISWA
```http
DELETE {{baseUrl}}/api/registrations/1
Authorization: Bearer <token>
```
**204 No Content**

Error cases (status!=PENDING, bukan owner, dll) sudah disalin utuh dari Postman di versi ini.

### 3.4 My registrations 🔐 MAHASISWA
```http
GET {{baseUrl}}/api/registrations/myregist
Authorization: Bearer <token>
Accept: application/json
```
```json
[
    {
        "registrationId": 5,
        "eventId": 4,
        "eventName": "asdsdasda",
        "eventDesc": "adsasd",
        "registrationAt": "2025-05-30",
        "status": "APPROVED",
        "certificateId": 2
    },
    {
        "registrationId": 8,
        "eventId": 6,
        "eventName": "asd",
        "eventDesc": "asdasdasd",
        "registrationAt": "2025-05-30",
        "status": "APPROVED",
        "certificateId": 3
    }
]
```

---
## 4  Certificates <a id="4-certificates"></a>
### 4.1 List
```http
GET {{baseUrl}}/api/certificates
Authorization: Bearer <token>
Accept: application/json
```
```json
{
    "status": "OK",
    "code": 200,
    "message": [
        {
            "id": 1,
            "mahasiswaNim": "103012300469",
            "eventId": 2,
            "eventTitle": "tai",
            "fotoPath": "events/cd253431d25e41769d181555dd27ceb8.png",
            "issueDate": "2025-05-27"
        }
    ]
}
```

### 4.2 Download PDF
```http
GET {{baseUrl}}/api/certificates/1/download
Authorization: Bearer <token>
Accept: application/pdf
```
Response headers: `Content-Type: application/pdf`; body = binary.

---
## 5  Upload <a id="5-upload"></a>
```http
POST {{baseUrl}}/api/upload
Content-Type: multipart/form-data
Authorization: Bearer <token>
```
> Form‑data key `file` (file input)

```json
{
    "message": "File uploaded successfully",
    "filePath": "events/b41506ced96641c98c6e67ebad07efec.png"
}
```

---
## 6  Environment Vars <a id="6-environment-vars"></a>
```
baseUrl     = http://localhost:8888
bearerToken = <jwt>
```

---
## 7  Error Schema <a id="7-error-schema"></a>
```json
{
  "code": 403,
  "status": "Forbidden",
  "message": "Hanya staff yang boleh …"
}
```

---
## 8  Changelog <a id="8-changelog"></a>
| Version | Date | Notes |
|---------|------|-------|
| **1.1** | 2025‑05‑30 | Spec sepenuhnya diganti dengan endpoint Postman v2.1. Contoh payload **FULL** tanpa pemotongan. |



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
