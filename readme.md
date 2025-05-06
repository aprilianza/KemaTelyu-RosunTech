# KemaTelyu 🎓

Sistem E-Ticketing dan Sertifikasi Digital untuk kegiatan kemahasiswaan Tel-U.

## Struktur Folder

- `frontend/`: Vue.js untuk tampilan mahasiswa & staff
- `backend/`: Spring Boot + MySQL untuk autentikasi, event, registrasi, dan sertifikat

## 😠 Git Workflow Tim Rosun (Proyek KemaTelyu)

Agar pengembangan terorganisir dan tidak bentrok antar developer, ikuti alur kerja Git berikut:

---

### 1. 📅 Clone Repository Pertama Kali

```bash
git clone https://github.com/aprilianza/KemaTelyu-RosunTech.git
cd KemaTelyu-RosunTech
```

---

### 2. 🌱 Buat Branch Pribadi (Sekali Saja di Awal)

```bash
git checkout -b [namamu]
git push origin [namamu]
```

Contoh:

```bash
git checkout -b athila
git push origin athila
```

---

### 3. ✍️ Workflow Harian: Commit di Branch Sendiri

```bash
git checkout [namamu]             # pastikan kerja di branch sendiri
git pull origin [namamu]          # update branch pribadi dari remote
# lakukan perubahan koding...
git add .
git commit -m "feat: tambah fitur pendaftaran event"
git push origin [namamu]
```

> Jika ada update dari tim lain, jalankan:

```bash
git fetch origin                  # ambil semua update dari remote
```

---

### 4. 🔁 Update dari Branch `main` (jika ada update dari tim lain)

```bash
git checkout main
git pull origin main              # ambil perubahan terbaru dari main

# lalu merge ke branch kamu
git checkout [namamu]
git merge main
```

> Atau pakai rebase biar history lebih bersih:

```bash
git checkout [namamu]
git fetch origin
git rebase origin/main
```

---

### 5. 📬 Jika Fitur Sudah Selesai, Buat Pull Request

* Buka GitHub
* Buat Pull Request dari `branch kamu` ke `main`
* Tunggu review & persetujuan dari tim

---

### 6. 🪠 Best Practices

* Jangan commit langsung ke `main`
* Pastikan `pull` dari `main` dulu sebelum merge atau PR
* Gunakan `git fetch` dan `git pull` secara berkala untuk sync
* Commit kecil & teratur (gunakan format: `feat:`, `fix:`, `refactor:`)
* Tambahkan penjelasan singkat di setiap PR
* Kalau terjadi konflik saat merge, diskusi bareng tim!

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