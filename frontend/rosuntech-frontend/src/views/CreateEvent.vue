<template>
  <div>
    <SidebarStaff />
    <div class="content-container">
      <div class="container py-4" style="min-height: 100vh;">
        <div class="text-center mb-4 mt-5">
          <h2 class="fw-bold">Create Event</h2>
          <p class="text-muted">Buat acara baru untuk mahasiswa Telkom University</p>
        </div>

        <div class="card shadow-sm border-0 mb-4">
          <div class="card-body p-4">
            <form @submit.prevent="submitEvent">
              <!-- Title -->
              <div class="mb-4">
                <label class="form-label fw-semibold">Judul Event</label>
                <input v-model="form.title" class="form-control bg-light border-0" placeholder="Judul" required />
              </div>

              <!-- Description -->
              <div class="mb-4">
                <label class="form-label fw-semibold">Deskripsi Event</label>
                <textarea v-model="form.description" class="form-control bg-light border-0" rows="4" placeholder="Deskripsi" required></textarea>
              </div>

              <!-- Date & Time -->
              <div class="row mb-4">
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Tanggal</label>
                  <input type="date" v-model="form.date" class="form-control bg-light border-0" required />
                </div>
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Waktu</label>
                  <input type="time" v-model="form.time" class="form-control bg-light border-0" required />
                </div>
              </div>

              <!-- Max Participants -->
              <div class="mb-4">
                <label class="form-label fw-semibold">Maksimal Peserta</label>
                <input type="number" v-model="form.maxParticipant" class="form-control bg-light border-0" required />
              </div>

              <!-- Upload Photo -->
              <div class="mb-4">
                <label class="form-label fw-semibold">Upload Poster</label>
                <input type="file" @change="handleFile" class="form-control bg-light border-0" accept="image/*" required />
                <small class="text-muted">Max 2 MB • JPG/PNG</small>

                <div v-if="preview" class="mt-3 text-center">
                  <img :src="preview" alt="Preview" class="img-preview rounded" />
                </div>
              </div>

              <!-- Submit Button -->
              <div class="text-center mt-4">
                <button class="btn btn-danger px-5 py-2 rounded-pill">
                  <i class="fas fa-plus-circle me-2"></i>Buat Event
                </button>
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
        fotoFile: null
      },
      preview: null
    };
  },
  methods: {
    handleFile(e) {
      const file = e.target.files[0];
      this.form.fotoFile = file;
      if (file) {
        const reader = new FileReader();
        reader.onload = ev => (this.preview = ev.target.result);
        reader.readAsDataURL(file);
      }
    },
    async submitEvent() {
      try {
        if (!this.form.fotoFile) {
          alert('Foto wajib di-upload');
          return;
        }

        // 1️⃣ Upload file ke backend
        const formData = new FormData();
        formData.append('file', this.form.fotoFile);
        await api.post('/api/upload', formData); // pastikan backend handle ini

        // 2️⃣ Build JSON payload
        const payload = {
          title: this.form.title,
          description: this.form.description,
          date: this.form.date,
          time: this.form.time,
          maxParticipant: parseInt(this.form.maxParticipant),
          fotoPath: 'uploads/events/' + this.form.fotoFile.name
        };

        // 3️⃣ Kirim data ke backend
        await createEvent(payload);

        // 4️⃣ Beres
        this.$swal({ title: 'Mantap!', text: 'Event berhasil dibuat', icon: 'success' });
        this.reset();
      } catch (err) {
        console.error(err);
        alert('Gagal membuat event. Cek console.');
      }
    },
    reset() {
      this.form = {
        title: '',
        description: '',
        date: '',
        time: '',
        maxParticipant: '',
        fotoFile: null
      };
      this.preview = null;
    }
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
