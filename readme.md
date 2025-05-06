# KemaTelyu 🎓

Sistem E-Ticketing dan Sertifikasi Digital untuk kegiatan kemahasiswaan Tel-U.

## Struktur Folder

- `frontend/`: Vue.js untuk tampilan mahasiswa & staff
- `backend/`: Spring Boot + MySQL untuk autentikasi, event, registrasi, dan sertifikat

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
