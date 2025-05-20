<template>
  <div>
    <SidebarStaff />
    <div class="content-container">
      <div class="container py-4" style="min-height: 100vh;">
        <!-- Create Event Section -->
        <div class="text-center mb-4 mt-5">
          <h2 class="fw-bold">Create Event</h2>
          <p class="text-muted">Buat acara baru untuk mahasiswa Telkom University</p>
        </div>

        <!-- Form Card -->
        <div class="card shadow-sm border-0 mb-4">
          <div class="card-body p-4">
            <form @submit.prevent="submitEvent">
              <!-- Title -->
              <div class="mb-4">
                <label for="title" class="form-label fw-semibold">Judul Event</label>
                <input 
                  type="text" 
                  id="title" 
                  v-model="form.title" 
                  class="form-control bg-light border-0" 
                  placeholder="Masukkan judul event" 
                  required 
                />
              </div>

              <!-- Description -->
              <div class="mb-4">
                <label for="description" class="form-label fw-semibold">Deskripsi Event</label>
                <textarea 
                  id="description" 
                  v-model="form.description" 
                  class="form-control bg-light border-0" 
                  rows="4" 
                  placeholder="Masukkan deskripsi lengkap tentang event"
                  required
                ></textarea>
              </div>

              <!-- Date & Time -->
              <div class="row mb-4">
                <div class="col-md-6">
                  <label for="eventDate" class="form-label fw-semibold">Tanggal Event</label>
                  <input 
                    type="date" 
                    id="eventDate" 
                    v-model="form.eventDate" 
                    class="form-control bg-light border-0" 
                    required 
                  />
                </div>
                <div class="col-md-6">
                  <label for="eventTime" class="form-label fw-semibold">Waktu Event</label>
                  <input 
                    type="time" 
                    id="eventTime" 
                    v-model="form.eventTime" 
                    class="form-control bg-light border-0" 
                    required 
                  />
                </div>
              </div>

              <!-- Max Participants -->
              <div class="mb-4">
                <label for="maxParticipants" class="form-label fw-semibold">Maksimal Peserta</label>
                <input 
                  type="number" 
                  id="maxParticipants" 
                  v-model="form.maxParticipants" 
                  class="form-control bg-light border-0" 
                  placeholder="Masukkan jumlah maksimal peserta"
                  required 
                />
              </div>

              <!-- Upload Photo -->
              <div class="mb-4">
                <label for="photo" class="form-label fw-semibold">Upload Foto Event</label>
                <div class="input-group">
                  <input 
                    type="file" 
                    id="photo" 
                    @change="handleFileUpload" 
                    class="form-control bg-light border-0" 
                    accept="image/*"
                    required 
                  />
                </div>
                <small class="text-muted">Format yang didukung: JPG, PNG, JPEG. Maksimal 2MB</small>
                
                <!-- Image Preview -->
                <div v-if="imagePreview" class="mt-3 text-center">
                  <img :src="imagePreview" alt="Preview" class="img-preview rounded" />
                </div>
              </div>

              <!-- Submit Button -->
              <div class="text-center mt-4">
                <button type="submit" class="btn btn-danger px-5 py-2 rounded-pill">
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

export default {
  name: "CreateEvent",
  components: {
    SidebarStaff
  },
  data() {
    return {
      form: {
        title: '',
        description: '',
        eventDate: '',
        eventTime: '',
        maxParticipants: '',
        photo: null
      },
      imagePreview: null
    };
  },
  methods: {
    handleFileUpload(event) {
      const file = event.target.files[0];
      this.form.photo = file;
      
      // Create image preview
      if (file) {
        const reader = new FileReader();
        reader.onload = e => {
          this.imagePreview = e.target.result;
        };
        reader.readAsDataURL(file);
      } else {
        this.imagePreview = null;
      }
    },
    submitEvent() {
      // Form validation
      if (!this.form.title || !this.form.description || !this.form.eventDate || 
          !this.form.eventTime || !this.form.maxParticipants || !this.form.photo) {
        alert("Mohon lengkapi semua field!");
        return;
      }
      
      // Create FormData object for file upload
      const formData = new FormData();
      formData.append('title', this.form.title);
      formData.append('description', this.form.description);
      formData.append('eventDate', this.form.eventDate);
      formData.append('eventTime', this.form.eventTime);
      formData.append('maxParticipants', this.form.maxParticipants);
      formData.append('photo', this.form.photo);
      
      // Here you would typically send the form data to your API
      console.log("Event data submitted:", this.form);
      
      // Show success message
      this.$swal({
        title: 'Sukses!',
        text: 'Event berhasil dibuat',
        icon: 'success',
        confirmButtonText: 'OK',
        confirmButtonColor: '#dc3545'
      }).then(() => {
        // Reset form
        this.resetForm();
        
        // Optional: Navigate to dashboard
        // this.$router.push('/DashboardStaff');
      });
    },
    resetForm() {
      this.form = {
        title: '',
        description: '',
        eventDate: '',
        eventTime: '',
        maxParticipants: '',
        photo: null
      };
      this.imagePreview = null;
      
      // Reset file input
      const fileInput = document.getElementById('photo');
      if (fileInput) fileInput.value = '';
    }
  }
};
</script>

<style scoped>
.content-container {
  padding-top: 60px;
  margin-left: 0;
  transition: margin-left 0.3s;
  background-color: #fdeeee;
}

.telkom-logo {
  height: 40px;
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

/* Form styling */
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

/* Responsive adjustments */
@media (min-width: 992px) {
  .content-container {
    margin-left: 0;
    padding-left: 20px;
    padding-right: 20px;
  }
}
</style>