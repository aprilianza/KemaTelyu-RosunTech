<template>
  <div>
    <SidebarStaff />
    <div class="content-container">
      <div class="container py-4" style="min-height: 100vh">
        <div class="text-center mb-4 mt-5">
          <h2 class="fw-bold">Buat Event</h2>
          <p class="text-muted">Buat acara baru untuk mahasiswa Telkom University</p>
        </div>

        <div class="card shadow-sm border-0 mb-4">
          <div class="card-body p-4">
            <form @submit.prevent="submitEvent">
              <!-- Title -->
              <div class="mb-4">
                <label class="form-label fw-semibold">Judul Event</label>
                <input v-model="form.title" class="form-control bg-light border-0" placeholder="Judul" />
              </div>

              <!-- Description -->
              <div class="mb-4">
                <label class="form-label fw-semibold">Deskripsi Event</label>
                <textarea v-model="form.description" class="form-control bg-light border-0" rows="4" placeholder="Deskripsi" ></textarea>
              </div>

              <!-- Date & Time -->
              <div class="row mb-4">
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Tanggal</label>
                  <input type="date" v-model="form.date" class="form-control bg-light border-0"  />
                </div>
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Waktu</label>
                  <input type="time" v-model="form.time" class="form-control bg-light border-0"  />
                </div>
              </div>

              <!-- Max Participants -->
              <div class="mb-4">
                <label class="form-label fw-semibold">Maksimal Peserta</label>
                <input type="number" v-model="form.maxParticipant" class="form-control bg-light border-0"  />
              </div>

              <!-- Upload Photo -->
              <div class="mb-4">
                <label class="form-label fw-semibold">Upload Poster</label>
                <input type="file" @change="handleFile" class="form-control bg-light border-0" accept="image/*"  />
                <small class="text-muted">Max 2 MB • JPG/PNG</small>

                <div v-if="preview" class="mt-3 text-center">
                  <img :src="preview" alt="Preview" class="img-preview rounded" />
                </div>
              </div>

              <!-- Submit Button -->
              <div class="text-center mt-4">
                <button class="btn btn-danger px-5 py-2 rounded-pill">Buat Event</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SidebarStaff from '@/components/SidebarStaff.vue';
import { createEvent } from '@/api/event';
import api from '@/api/axios';
import Swal from 'sweetalert2';

export default {
  components: { SidebarStaff },
  data() {
    return {
      form: {
        title: '',
        description: '',
        date: '',
        time: '',
        maxParticipant: '',
        fotoFile: null,
      },
      preview: null,
      isLoading: false,
    };
  },
  methods: {
    handleFile(e) {
      const file = e.target.files[0];
      if (file) {
        // Validasi ukuran file (max 2MB)
        if (file.size > 2 * 1024 * 1024) {
          this.showErrorAlert('Ukuran file terlalu besar. Maksimal 2MB.');
          return;
        }

        // Validasi tipe file
        if (!['image/jpeg', 'image/png'].includes(file.type)) {
          this.showErrorAlert('Format file tidak didukung. Gunakan JPG/PNG.');
          return;
        }

        this.form.fotoFile = file;
        const reader = new FileReader();
        reader.onload = (ev) => (this.preview = ev.target.result);
        reader.readAsDataURL(file);
      }
    },
    async submitEvent() {
      this.isLoading = true;

      try {
        // Validasi form
        if (!this.validateForm()) return;

        // 1️⃣ Upload file ke backend
        const formData = new FormData();
        formData.append('file', this.form.fotoFile);
        const uploadResponse = await api.post('/api/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });

        // 2️⃣ Build JSON payload
        const payload = {
          title: this.form.title,
          description: this.form.description,
          date: this.form.date,
          time: this.form.time,
          maxParticipant: parseInt(this.form.maxParticipant),
          fotoPath: uploadResponse.data.filePath || 'uploads/events/' + this.form.fotoFile.name,
        };

        // 3️⃣ Kirim data ke backend (tidak perlu simpan response jika tidak digunakan)
        await createEvent(payload);

        // 4️⃣ Tampilkan notifikasi sukses
        await this.showSuccessAlert('Event Berhasil Dibuat!', 'Event baru telah berhasil ditambahkan ke sistem.');

        // Reset form setelah berhasil
        this.reset();
      } catch (err) {
        this.handleError(err);
      } finally {
        this.isLoading = false;
      }
    },
    validateForm() {
      if (!this.form.fotoFile) {
        this.showErrorAlert('Foto wajib di-upload');
        return false;
      }

      if (!this.form.title || !this.form.description || !this.form.date || !this.form.time || !this.form.maxParticipant) {
        this.showErrorAlert('Semua field wajib diisi');
        return false;
      }

      return true;
    },
    reset() {
      this.form = {
        title: '',
        description: '',
        date: '',
        time: '',
        maxParticipant: '',
        fotoFile: null,
      };
      this.preview = null;
    },
    async showSuccessAlert(title, text) {
      await Swal.fire({
        title: title,
        text: text,
        icon: 'success',
        confirmButtonText: 'OK',
        confirmButtonColor: '#3085d6',
      });
    },
    showErrorAlert(message) {
      Swal.fire({
        title: 'Gagal Membuat Event',
        text: message,
        icon: 'error',
        confirmButtonText: 'OK',
        confirmButtonColor: '#d33',
      });
    },
    handleError(error) {
      console.error('Error creating event:', error);

      let errorMessage = 'Terjadi kesalahan saat membuat event';

      if (error.response) {
        // Error dari server
        if (error.response.data && error.response.data.message) {
          errorMessage = error.response.data.message;
        } else if (error.response.status === 413) {
          errorMessage = 'Ukuran file terlalu besar';
        } else if (error.response.status === 400) {
          errorMessage = 'Data yang dikirim tidak valid';
        } else if (error.response.status === 401) {
          errorMessage = 'Anda tidak memiliki izin';
        } else if (error.response.status === 500) {
          errorMessage = 'Server mengalami masalah';
        }
      } else if (error.request) {
        // Tidak ada response dari server
        errorMessage = 'Tidak dapat terhubung ke server';
      }

      this.showErrorAlert(errorMessage);
    },
  },

  // Route guard untuk proteksi tambahan
  beforeRouteEnter(to, from, next) {
    const token = localStorage.getItem('token');
    const userData = localStorage.getItem('user');
    
    if (!token) {
      next('/');
      return;
    }

    if (userData) {
      try {
        const user = JSON.parse(userData);
        if (!user.role || user.role.toUpperCase() !== 'STAFF') {
          next('/');
          return;
        }
      } catch (error) {
        next('/');
        return;
      }
    }

    next();
  }
};
</script>


<style scoped>
.content-container {
  padding-top: 60px;
  background-color: #fdeeee;
}
.img-preview {
  max-width: 100%;
  max-height: 200px;
  border: 1px solid #dee2e6;
}
input.form-control:focus,
textarea.form-control:focus {
  box-shadow: none;
  border-color: #dc3545;
}
.card {
  border-radius: 10px;
}
.btn-danger {
  background-color: #dc3545;
  border-color: #dc3545;
  box-shadow: 0 2px 4px rgba(220, 53, 69, 0.2);
  transition: all 0.3s;
}
.btn-danger:hover {
  background-color: #c82333;
  border-color: #bd2130;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(220, 53, 69, 0.3);
}
.form-label {
  color: #495057;
}
.form-control {
  padding: 10px 15px;
  border-radius: 8px;
}
.form-control.bg-light {
  background-color: #f8f9fa !important;
}
</style>
