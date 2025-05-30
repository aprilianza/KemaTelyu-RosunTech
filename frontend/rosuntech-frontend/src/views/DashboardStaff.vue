<template>
  <div class="d-flex">
    <!-- Sidebar -->
    <SidebarStaff />
    <!-- Main Content -->
    <div class="content-wrapper container py-5">
      <!-- User Info + Create Button -->
      <div class="user-profile-container mb-5">
        <div class="row g-4 align-items-center">
          <div class="col-12 col-md-4 text-center">
            <div class="profile-photo-container">
              <img :src="`http://localhost:8888/${user.photo}`" class="img-fluid rounded-circle profile-photo shadow" alt="User Photo" />
            </div>
          </div>
          <div class="col-12 col-md-8">
            <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center">
              <div class="user-info mb-3 mb-md-0 text-center text-md-start">
                <h3 class="fw-bold mb-2">{{ user.name }}</h3>
                <div class="division-badge"><i class="bi bi-building me-2"></i>{{ user.division }}</div>
              </div>
              <router-link to="/CreateEvent" class="btn btn-lg create-event-btn"> <i class="bi bi-plus-circle me-2"></i>Buat Event </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- Event List -->
      <div class="events-section">
        <div class="section-header d-flex justify-content-between align-items-center mb-4">
          <h5 class="section-title mb-0"><i class="bi bi-calendar-event me-2"></i>Event yang Anda Buat</h5>
          <div class="event-counter badge bg-light text-primary">{{ events.length }} Event</div>
        </div>

        <div v-if="loadingEvents" class="text-center py-5">
          <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">Loading...</span>
          </div>
        </div>

        <div v-else class="row justify-content-center g-4 mb-5">
          <div class="col-12 col-md-6 col-lg-4" v-for="event in events" :key="event.id">
            <div class="event-card d-flex flex-column h-100">
              <div class="image-wrapper mb-3 position-relative">
                <img :src="getEventImage(event.fotoPath)" alt="Event Image" class="rounded-top" />
                <div class="event-date-overlay">
                  <span class="date">{{ formatDate(event.date).day }}</span>
                  <span class="month">{{ formatDate(event.date).month }}</span>
                </div>
                <div class="event-actions-dropdown">
                  <button class="btn btn-sm btn-actions" @click.stop="toggleActionsMenu(event.id)">
                    <i class="bi bi-three-dots-vertical"></i>
                  </button>
                  <div v-if="showActionsMenu === event.id" class="actions-menu" @click.stop>
                    <button class="action-item" @click="openEditModal(event)">
                      <i class="bi bi-pencil me-2"></i>Edit
                    </button>
                    <button class="action-item text-danger" @click="confirmDeleteEvent(event.id)">
                      <i class="bi bi-trash me-2"></i>Hapus
                    </button>
                  </div>
                </div>
              </div>
              <div class="card-body d-flex flex-column">
                <div class="badges-container mb-2">
                  <span class="badge time-badge me-2"> <i class="bi bi-clock me-1"></i>{{ formatTime(event.time) }} </span>
                  <span class="badge participant-badge"> <i class="bi bi-people me-1"></i>Maks {{ event.maxParticipant }} </span>
                </div>
                <h5 class="event-title mb-2">{{ event.title }}</h5>
                <p class="event-description text-truncate mb-3">{{ event.description }}</p>
                <div class="mt-auto d-flex justify-content-end">
                  <button class="btn btn-light btn-sm" @click="openModal(event)"><i class="bi bi-eye me-1"></i> Detail</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Detail Event Modal -->
      <div v-if="selectedEvent" class="modern-modal-container animate__animated animate__fadeIn">
        <div class="modern-modal-backdrop" @click="closeModal"></div>
        <div class="modern-modal animate__animated animate__zoomIn">
          <button type="button" class="modern-modal-close" @click="closeModal">
            <span class="close-icon">&times;</span>
          </button>

          <div class="modern-modal-image">
            <img :src="getEventImage(selectedEvent.fotoPath)" alt="Event Image" />
            <div class="event-date-badge">
              <div class="date-content">
                <span class="day">{{ formatDate(selectedEvent.date).day }}</span>
                <span class="month">{{ formatDate(selectedEvent.date).month }}</span>
                <span class="year">{{ formatDate(selectedEvent.date).year }}</span>
              </div>
            </div>
          </div>

          <div class="modern-modal-content">
            <h2 class="modal-event-title">{{ selectedEvent.title }}</h2>

            <div class="modal-event-metadata">
              <div class="metadata-item">
                <span class="metadata-icon calendar-icon"></span>
                <span>{{ formatFullDate(selectedEvent.date) }}</span>
              </div>
              <div class="metadata-item">
                <span class="metadata-icon clock-icon"></span>
                <span>{{ formatTime(selectedEvent.time) }}</span>
              </div>
              <div class="metadata-item">
                <span class="metadata-icon users-icon"></span>
                <span>Maks {{ selectedEvent.maxParticipant }} Peserta</span>
              </div>
            </div>

            <div class="modal-event-description">
              <p>{{ selectedEvent.description }}</p>
            </div>

            <!-- Participants Section -->
            <div class="participants-section mt-4">
              <h4 class="participants-title">
                <i class="bi bi-people-fill me-2"></i>Peserta
                <span class="participant-count">({{ selectedEvent.participants.length }})</span>
              </h4>

              <div class="table-responsive mt-3">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th scope="col">No</th>
                      <th scope="col">Nama</th>
                      <th scope="col">Email</th>
                      <th scope="col">NIM</th>
                      <th scope="col">Fakultas</th>
                      <th scope="col">Status</th>
                      <th scope="col">Aksi</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(participant, idx) in selectedEvent.participants" :key="participant.id">
                      <td>{{ idx + 1 }}</td>
                      <td>
                        <div class="d-flex align-items-center">
                          <img :src="getUserImage(participant.fotoPath)" class="rounded-circle me-2" width="30" height="30" alt="Foto Peserta">
                          {{ participant.name }}
                        </div>
                      </td>
                      <td>{{ participant.email }}</td>
                      <td>{{ participant.nim }}</td>
                      <td>{{ participant.fakultas }}</td>
                      <td>
                        <span :class="getStatusBadgeClass(getRegistrationStatus(participant.nim))">
                          {{ formatStatus(getRegistrationStatus(participant.nim)) }}
                        </span>
                      </td>
                      <td>
                        <div class="btn-group">
                          <button 
                            class="btn btn-sm btn-success" 
                            @click="updateParticipantStatus(participant.nim, 'APPROVED')" 
                            :disabled="getRegistrationStatus(participant.nim) === 'APPROVED'">
                            <i class="bi bi-check-circle"></i>
                          </button>                          
                          <button
                            class="btn btn-sm btn-danger" 
                            @click="updateParticipantStatus(participant.nim, 'REJECTED')" 
                            :disabled="getRegistrationStatus(participant.nim) === 'REJECTED'">
                            <i class="bi bi-x-circle"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                    <tr v-if="selectedEvent.participants.length === 0">
                      <td colspan="7" class="text-center py-3">Belum ada peserta yang terdaftar</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <div class="modern-modal-actions">
              <button class="btn btn-outline-danger" @click="closeModal">Tutup</button>
              <button class="btn btn-danger" @click="confirmDeleteEvent(selectedEvent.id)">Hapus Event</button>
            </div>
          </div>
        </div>
      </div>

      <!-- Edit Event Modal -->
      <div v-if="showEditModal" class="modern-modal-container animate__animated animate__fadeIn">
        <div class="modern-modal-backdrop" @click="closeEditModal"></div>
        <div class="modern-modal animate__animated animate__zoomIn">
          <button type="button" class="modern-modal-close" @click="closeEditModal">
            <span class="close-icon">&times;</span>
          </button>

          <div class="modern-modal-content">
            <h2 class="modal-event-title mb-4">Edit Event</h2>

            <form @submit.prevent="handleUpdateEvent">
              <div class="row g-3">
                <div class="col-md-6">
                  <label for="edit-title" class="form-label">Judul Event</label>
                  <input type="text" class="form-control" id="edit-title" v-model="editForm.title" required>
                </div>
                <div class="col-md-6">
                  <label for="edit-max-participant" class="form-label">Maksimal Peserta</label>
                  <input type="number" class="form-control" id="edit-max-participant" v-model="editForm.maxParticipant" min="1" required>
                </div>
                <div class="col-md-6">
                  <label for="edit-date" class="form-label">Tanggal</label>
                  <input type="date" class="form-control" id="edit-date" v-model="editForm.date" required>
                </div>
                <div class="col-md-6">
                  <label for="edit-time" class="form-label">Waktu</label>
                  <input type="time" class="form-control" id="edit-time" v-model="editForm.time" required>
                </div>
                <div class="col-12">
                  <label for="edit-description" class="form-label">Deskripsi</label>
                  <textarea class="form-control" id="edit-description" rows="3" v-model="editForm.description" required></textarea>
                </div>
                <div class="col-12">
                  <label for="edit-image" class="form-label">Gambar Event</label>
                  <input type="file" class="form-control" id="edit-image" @change="handleImageChange" accept="image/*">
                  <div class="form-text">Biarkan kosong jika tidak ingin mengubah gambar</div>
                </div>
                <div class="col-12 mt-4">
                  <div class="d-flex justify-content-end gap-2">
                    <button type="button" class="btn btn-outline-secondary" @click="closeEditModal">Batal</button>
                    <button type="submit" class="btn btn-primary" :disabled="updatingEvent">
                      <span v-if="updatingEvent" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                      Simpan Perubahan
                    </button>
                  </div>
                </div>
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
import axios from 'axios';
import Swal from 'sweetalert2';
import { 
  getEventsStaff, 
  deleteEvent, 
  updateParticipantStatus as apiUpdateParticipantStatus,
  updateEvent
} from '@/api/event';

export default {
  name: 'DashboardStaff',
  components: { SidebarStaff },
  data() {
    return {
      isLoading: true,
      isAuthenticated: false,
      loadingEvents: false,
      user: {
        name: '',
        division: '',
        photo: '',
      },
      events: [],
      selectedEvent: null,
      showActionsMenu: null,
      showEditModal: false,
      updatingEvent: false,
      editForm: {
        id: null,
        title: '',
        description: '',
        date: '',
        time: '',
        maxParticipant: 10,
        image: null
      }
    };
  },
  async mounted() {
    await this.checkAuthentication();
    await this.fetchEvents();
    document.addEventListener('click', this.closeActionsMenu);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.closeActionsMenu);
  },
  methods: {
    /* AUTENTIKASI & FETCH EVENTS */
    async checkAuthentication() {
      try {
        const token = localStorage.getItem('token');
        const userData = localStorage.getItem('user');
        if (!token) {
          await this.showAuthError('Autentikasi Diperlukan', 'Silakan login untuk mengakses halaman ini');
          this.redirectToLogin();
          return;
        }
        let user = null;
        if (userData) {
          try { user = JSON.parse(userData); }
          catch (err) {
            console.error(err);
            await this.showAuthError('Data User Tidak Valid', 'Silakan login kembali');
            this.redirectToLogin();
            return;
          }
        }
        if (!user || !user.role || user.role.toUpperCase() !== 'STAFF') {
          await this.showAuthError('Akses Ditolak', 'Anda memerlukan hak akses staff');
          this.redirectToLogin();
          return;
        }
        const resp = await axios.get('/api/auth/me', {
          headers: { Authorization: `Bearer ${token}` },
        });
        if (resp.status === 200) {
          this.user = {
            ...this.user,
            ...resp.data,
            name: resp.data.name || this.user.name,
            division: resp.data.divisi || this.user.division,
            photo: resp.data.fotoPath || this.user.photo,
          };
          this.isAuthenticated = true;
          this.isLoading = false;
        } else {
          await this.showAuthError('Autentikasi Gagal', 'Gagal memverifikasi kredensial Anda');
          this.redirectToLogin();
        }
      } catch (error) {
        console.error(error);
        const code = error.response?.status;
        if (code === 401) {
          await this.showAuthError('Sesi Habis', 'Silakan login kembali');
        } else if (code === 403) {
          await this.showAuthError('Akses Ditolak', 'Anda tidak memiliki izin');
        } else {
          await this.showAuthError('Kesalahan Server', 'Coba lagi nanti');
        }
        this.clearAuthData();
        this.redirectToLogin();
      }
    },

    async fetchEvents() {
      this.loadingEvents = true;
      try {
        const resp = await getEventsStaff();
        this.events = resp.data.message;
      } catch (error) {
        console.error(error);
        Swal.fire({ icon: 'error', title: 'Error', text: 'Gagal memuat event.' });
      } finally {
        this.loadingEvents = false;
      }
    },

    /* EVENT ACTIONS */
    toggleActionsMenu(eventId) {
      this.showActionsMenu = this.showActionsMenu === eventId ? null : eventId;
    },

    closeActionsMenu() {
      this.showActionsMenu = null;
    },

    openEditModal(event) {
      this.editForm = {
        id: event.id,
        title: event.title,
        description: event.description,
        date: event.date.split('T')[0],
        time: event.time.substring(0, 5),
        maxParticipant: event.maxParticipant,
        image: null
      };
      this.showEditModal = true;
      this.closeActionsMenu();
    },

    closeEditModal() {
      this.showEditModal = false;
      this.editForm = {
        id: null,
        title: '',
        description: '',
        date: '',
        time: '',
        maxParticipant: 10,
        image: null
      };
    },

    handleImageChange(event) {
      this.editForm.image = event.target.files[0];
    },

    async handleUpdateEvent() {
      this.updatingEvent = true;
      try {
        const formData = new FormData();
        formData.append('title', this.editForm.title);
        formData.append('description', this.editForm.description);
        formData.append('date', this.editForm.date);
        formData.append('time', this.editForm.time + ':00');
        formData.append('maxParticipant', this.editForm.maxParticipant);
        if (this.editForm.image) {
          formData.append('image', this.editForm.image);
        }

        const resp = await updateEvent(this.editForm.id, formData);
        
        // Update local events data
        const updatedEvent = resp.data.message;
        const index = this.events.findIndex(e => e.id === updatedEvent.id);
        if (index !== -1) {
          this.events[index] = updatedEvent;
        }

        Swal.fire({
          toast: true,
          position: 'top-end',
          icon: 'success',
          title: 'Event berhasil diperbarui',
          showConfirmButton: false,
          timer: 2000
        });

        this.closeEditModal();
      } catch (error) {
        console.error(error);
        Swal.fire({
          icon: 'error',
          title: 'Gagal memperbarui event',
          text: error.response?.data?.message || 'Terjadi kesalahan saat memperbarui event'
        });
      } finally {
        this.updatingEvent = false;
      }
    },

    /* MODAL */
    async openModal(event) {
      this.selectedEvent = event;
      document.body.classList.add('modal-open');
    },
    closeModal() {
      this.selectedEvent = null;
      document.body.classList.remove('modal-open');
    },

    /* FORMATTER */
    getEventImage(fotoPath) {
      return fotoPath ? `http://localhost:8888/${fotoPath}` : require('@/assets/img/placeholder.jpg');
    },
    getUserImage(fotoPath) {
      return `http://localhost:8888/${fotoPath}`;
    },
    formatDate(dateString) {
      const d = new Date(dateString);
      return {
        day:   d.toLocaleDateString('id-ID', { day: 'numeric' }),
        month: d.toLocaleDateString('id-ID', { month: 'long' }),
        year:  d.toLocaleDateString('id-ID', { year: 'numeric' }),
      };
    },
    formatFullDate(dateString) {
      return new Date(dateString).toLocaleDateString('id-ID', {
        day: 'numeric', month: 'long', year: 'numeric'
      });
    },
    formatTime(timeString) {
      return timeString ? timeString.substring(0, 5) : '';
    },
    formatStatus(status) {
      const map = { 'APPROVED': 'Diterima', 'REJECTED': 'Ditolak', 'PENDING': 'Menunggu' };
      return map[status] || status;
    },
    getRegistrationStatus(nim) {
      if (!this.selectedEvent?.registrations) return 'PENDING';
      const r = this.selectedEvent.registrations.find(x => x.mahasiswaNim === nim);
      return r?.status || 'PENDING';
    },

    /* HAPUS EVENT */
    async confirmDeleteEvent(eventId) {
      const result = await Swal.fire({
        title: 'Hapus Event',
        text: 'Tindakan ini tidak dapat dibatalkan.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Ya, hapus!',
        cancelButtonText: 'Batal',
      });
      if (result.isConfirmed) {
        try {
          await deleteEvent(eventId);
          this.events = this.events.filter(e => e.id !== eventId);
          if (this.selectedEvent?.id === eventId) this.closeModal();
          if (this.showEditModal && this.editForm.id === eventId) this.closeEditModal();
          Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: 'Event berhasil dihapus', showConfirmButton: false, timer: 2000 });
        } catch (error) {
          console.error(error);
          Swal.fire({ icon: 'error', title: 'Error', text: 'Gagal menghapus event.' });
        }
      }
    },

    /* APPROVE / REJECT PARTICIPANT */
    async updateParticipantStatus(nim, newStatus) {
      try {
        const registration = this.selectedEvent.registrations.find(r => r.mahasiswaNim === nim);
        if (!registration) throw new Error('Registrasi tidak ditemukan');

        // Panggil API approve/reject
        const resp = await apiUpdateParticipantStatus(registration.id, newStatus);

        // Update state lokal
        const idx = this.selectedEvent.registrations.findIndex(r => r.mahasiswaNim === nim);
        if (idx !== -1) {
          this.selectedEvent.registrations[idx].status   = newStatus;
          this.selectedEvent.registrations[idx].verified = (newStatus === 'APPROVED');
        }

        // Toast dengan message dari backend
        Swal.mixin({
          toast: true, position: 'top-end', showConfirmButton: false,
          timer: 3000, timerProgressBar: true
        }).fire({
          icon: 'success',
          title: resp.data.message
        });

      } catch (error) {
        console.error('Error updating participant status:', error);
        Swal.fire({ icon: 'error', title: 'Error', text: 'Gagal memperbarui status peserta.' });
      }
    },

    getStatusBadgeClass(status) {
      switch (status) {
        case 'APPROVED': return 'badge bg-success';
        case 'PENDING':  return 'badge bg-secondary';
        case 'REJECTED': return 'badge bg-danger';
        default:         return 'badge bg-secondary';
      }
    },

    /* AUTH HELPERS */
    async showAuthError(title, text) {
      await Swal.fire({
        icon: 'error',
        title,
        text,
        confirmButtonText: 'Ke Halaman Login',
        allowOutsideClick: false,
        customClass: { confirmButton: 'btn btn-danger' },
      });
    },

    clearAuthData() {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    },

    redirectToLogin() {
      this.isLoading = false;
      this.isAuthenticated = false;
      this.$router.push('/');
    },
  },

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
.content-wrapper {
  flex-grow: 1;
  margin-top: 80px;
}

/* User Profile */
.profile-photo-container {
  display: inline-block;
  padding: 4px;
  border-radius: 50%;
  background-image: linear-gradient(45deg, #b30202, #dd3232);
}

.profile-photo {
  width: 180px;
  height: 180px;
  object-fit: cover;
  border: 4px solid white;
}

.user-info h3 {
  color: #212121;
  font-size: 1.75rem;
}



.division-badge {
  display: inline-block;
  background-color: rgba(179, 2, 2, 0.1);
  color: #b30202;
  padding: 0.5rem 1rem;
  border-radius: 2rem;
  font-weight: 500;
}

.create-event-btn {
  border-radius: 50px;
  padding: 0.75rem 1.5rem;
  font-weight: 600;
  background-color: #b30202;
  color: #fff1f1;
  box-shadow: 0 4px 10px rgba(179, 2, 2, 0.3);
  transition: all 0.3s ease;
  border: none;
}

.create-event-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(179, 2, 2, 0.4);
  background-color: #dd3232;
  color: white;
}

/* Section Header */
.section-header {
  border-bottom: 2px solid rgba(179, 2, 2, 0.2);
  padding-bottom: 0.75rem;
}

.section-title {
  color: #b30202;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  position: relative;
  padding-left: 0.5rem;
}

.section-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  width: 4px;
  background-color: #b30202;
  border-radius: 4px;
}

.event-counter {
  font-size: 0.85rem;
  padding: 0.4rem 0.8rem;
  color: #b30202 !important;
}

/* Event Cards */
.event-card {
  background: linear-gradient(135deg, #2c3e50, #b30202);
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
}

.event-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
}

.image-wrapper {
  height: 180px;
  overflow: hidden;
  position: relative;
}

.image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.event-card:hover .image-wrapper img {
  transform: scale(1.05);
}

.event-date-overlay {
  position: absolute;
  top: 15px;
  right: 15px;
  background-color: white;
  color: #b30202;
  border-radius: 8px;
  padding: 0.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 50px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.event-date-overlay .date {
  font-size: 1.2rem;
  font-weight: 700;
  line-height: 1;
}

.event-date-overlay .month {
  font-size: 0.8rem;
  text-transform: uppercase;
}

/* Event Actions Dropdown */
.event-actions-dropdown {
  position: absolute;
  top: 15px;
  left: 15px;
  z-index: 10;
}

.btn-actions {
  background-color: rgba(255, 255, 255, 0.9);
  border: none;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0;
  color: #333;
  transition: all 0.2s ease;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.btn-actions:hover {
  background-color: white;
  transform: scale(1.1);
  color: #b30202;
}

.btn-actions:focus {
  outline: none;
}

.actions-menu {
  position: absolute;
  top: 100%;
  left: 0;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  min-width: 140px;
  z-index: 100;
  overflow: hidden;
  margin-top: 5px;
  animation: fadeIn 0.2s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

.action-item {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 8px 16px;
  text-align: left;
  border: none;
  background: none;
  transition: background-color 0.2s ease;
  color: #495057;
  font-size: 0.9rem;
  white-space: nowrap;
}

.action-item i {
  font-size: 1rem;
  margin-right: 8px;
}

.action-item:hover {
  background-color: #f8f9fa;
  color: #b30202;
}

.actions-menu::before {
  content: '';
  position: absolute;
  top: -5px;
  left: 15px;
  width: 0;
  height: 0;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-bottom: 5px solid white;
}

.card-body {
  padding: 1.5rem;
}

.badges-container {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.time-badge,
.participant-badge {
  background-color: rgba(255, 255, 255, 0.15);
  color: rgba(255, 255, 255, 0.9);
  border-radius: 50px;
  padding: 0.35rem 0.75rem;
  font-size: 0.75rem;
  font-weight: 500;
}

.event-title {
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
  line-height: 1.4;
  max-height: 3.1em;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.event-description {
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.9rem;
}

/* Modern Modal Styles */
.modern-modal-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1050;
}

.modern-modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(5px);
  z-index: 1051;
}

.modern-modal {
  background-color: white;
  width: 90%;
  max-width: 800px;
  border-radius: 1.5rem;
  overflow: hidden;
  z-index: 1052;
  position: relative;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
  max-height: 90vh;
  overflow-y: auto;
}

/* Enhanced Close Button */
.modern-modal-close {
  position: absolute;
  top: 15px;
  right: 15px;
  background-color: rgba(0, 0, 0, 0.6);
  border: 2px solid white;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 1060;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
}

.modern-modal-close:hover {
  background-color: rgba(0, 0, 0, 0.8);
  transform: scale(1.1);
}

.close-icon {
  color: white;
  font-size: 24px;
  line-height: 1;
  font-weight: bold;
}

.modern-modal-image {
  width: 100%;
  height: auto;
  overflow: hidden;
  position: relative;
}

.modern-modal-image img {
  width: 100%;
  height: auto;
}

.event-date-badge {
  position: absolute;
  top: 20px;
  left: 20px;
  background-color: white;
  border-radius: 10px;
  padding: 0.75rem 1rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.date-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  line-height: 1.2;
}

.date-content .day {
  font-size: 1.5rem;
  font-weight: 700;
  color: #b30202;
}

.date-content .month {
  font-size: 0.9rem;
  font-weight: 600;
  text-transform: uppercase;
  color: #495057;
}

.date-content .year {
  font-size: 0.8rem;
  color: #868686;
}

.modern-modal-content {
  padding: 2rem;
}

.modal-event-title {
  font-size: 1.8rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  color: #333;
}

.modal-event-metadata {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
  margin-bottom: 1.5rem;
  color: #868686;
}

.metadata-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

/* Custom icons */
.metadata-icon {
  display: inline-block;
  width: 20px;
  height: 20px;
  background-position: center;
  background-repeat: no-repeat;
  background-size: contain;
}

.calendar-icon {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='currentColor' class='bi bi-calendar' viewBox='0 0 16 16'%3E%3Cpath d='M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5zM1 4v10a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V4H1z'/%3E%3C/svg%3E");
}

.clock-icon {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='currentColor' class='bi bi-clock' viewBox='0 0 16 16'%3E%3Cpath d='M8 3.5a.5.5 0 0 0-1 0V9a.5.5 0 0 0 .252.434l3.5 2a.5.5 0 0 0 .496-.868L8 8.71V3.5z'/%3E%3Cpath d='M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zm7-8A7 7 0 1 1 1 8a7 7 0 0 1 14 0z'/%3E%3C/svg%3E");
}

.users-icon {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='currentColor' class='bi bi-people' viewBox='0 0 16 16'%3E%3Cpath d='M15 14s1 0 1-1-1-4-5-4-5 3-5 4 1 1 1 1h8zm-7.978-1A.261.261 0 0 1 7 12.996c.001-.264.167-1.03.76-1.72C8.312 10.629 9.282 10 11 10c1.717 0 2.687.63 3.24 1.276.593.69.758 1.457.76 1.72l-.008.002a.274.274 0 0 1-.014.002H7.022zM11 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4zm3-2a3 3 0 1 1-6 0 3 3 0 0 1 6 0zM6.936 9.28a5.88 5.88 0 0 0-1.23-.247A7.35 7.35 0 0 0 5 9c-4 0-5 3-5 4 0 .667.333 1 1 1h4.216A2.238 2.238 0 0 1 5 13c0-1.01.377-2.042 1.09-2.904.243-.294.526-.569.846-.816zM4.92 10A5.493 5.493 0 0 0 4 13H1c0-.26.164-1.03.76-1.724.545-.636 1.492-1.256 3.16-1.275zM1.5 5.5a3 3 0 1 1 6 0 3 3 0 0 1-6 0zm3-2a2 2 0 1 0 0 4 2 2 0 0 0 0-4z'/%3E%3C/svg%3E");
}

.modal-event-description {
  margin-bottom: 1rem;
  line-height: 1.6;
  color: #555;
}

/* Participants Section */
.participants-section {
  background-color: #f8f9fa;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.participants-title {
  color: #333;
  font-size: 1.25rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
}

.participant-count {
  font-size: 0.9rem;
  color: #868686;
  margin-left: 0.5rem;
}

.table th {
  font-weight: 600;
  color: #495057;
  background-color: rgba(179, 2, 2, 0.05);
}

.table td {
  vertical-align: middle;
}

.btn-group .btn {
  border-radius: 4px;
  padding: 0.25rem 0.5rem;
  margin: 0 0.1rem;
}

.modern-modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 2rem;
}

.btn-outline-danger {
  color: #b30202;
  border-color: #b30202;
}

.btn-outline-danger:hover {
  background-color: #b30202;
  color: white;
}

.btn-danger {
  background-color: #b30202;
  border-color: #b30202;
}

.btn-danger:hover {
  background-color: #dd3232;
  border-color: #dd3232;
}

/* Edit Modal Specific Styles */
.modern-modal-content form {
  padding: 0;
}

.modern-modal-content .form-label {
  font-weight: 600;
  color: #495057;
  margin-bottom: 0.5rem;
}

.modern-modal-content .form-control {
  border: 1px solid #ced4da;
  border-radius: 8px;
  padding: 0.75rem 1rem;
  transition: all 0.3s ease;
}

.modern-modal-content .form-control:focus {
  border-color: #b30202;
  box-shadow: 0 0 0 0.25rem rgba(179, 2, 2, 0.25);
}

.modern-modal-content textarea.form-control {
  min-height: 120px;
}

.modern-modal-content .form-text {
  color: #6c757d;
  font-size: 0.85rem;
  margin-top: 0.25rem;
}

/* Loading state for update button */
.btn-primary:disabled {
  background-color: #b30202;
  opacity: 0.8;
}

.spinner-border {
  vertical-align: middle;
  margin-right: 5px;
}

/* Form validation states */
.is-invalid {
  border-color: #dc3545 !important;
}

.invalid-feedback {
  color: #dc3545;
  font-size: 0.85rem;
  margin-top: 0.25rem;
}

/* Edit modal specific adjustments */
#edit-image {
  padding: 0.5rem;
}

/* Button hover effects */
.btn-outline-secondary:hover {
  background-color: #6c757d;
  color: white;
}

.btn-primary {
  background-color: #b30202;
  border-color: #b30202;
}

.btn-primary:hover {
  background-color: #dd3232;
  border-color: #dd3232;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .modern-modal {
    width: 95%;
    max-height: 85vh;
  }
  
  .modal-event-metadata {
    flex-direction: column;
    gap: 0.75rem;
  }
  
  .event-actions-dropdown {
    top: 10px;
    left: 10px;
  }
  
  .btn-actions {
    width: 28px;
    height: 28px;
    font-size: 0.9rem;
  }
  
  .modern-modal-content {
    padding: 1.5rem;
  }
  
  .modal-event-title {
    font-size: 1.5rem;
  }
}

/* Animation delays for staggered effect */
.animate__fadeIn {
  --animate-duration: 0.6s;
}

.animate__zoomIn {
  --animate-duration: 0.5s;
}

body.modal-open {
  overflow: hidden;
}

/* Date and time picker styling */
input[type="date"]::-webkit-calendar-picker-indicator,
input[type="time"]::-webkit-calendar-picker-indicator {
  filter: invert(0.5);
  cursor: pointer;
}

input[type="date"]:focus::-webkit-calendar-picker-indicator,
input[type="time"]:focus::-webkit-calendar-picker-indicator {
  filter: invert(0.5) drop-shadow(0 0 2px rgba(179, 2, 2, 0.5));
}

/* File input customization */
.form-control[type="file"] {
  padding: 0.375rem;
}

.form-control[type="file"]::file-selector-button {
  padding: 0.375rem 0.75rem;
  margin-right: 0.75rem;
  background-color: #f8f9fa;
  border: 1px solid #ced4da;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.form-control[type="file"]::file-selector-button:hover {
  background-color: #e9ecef;
  border-color: #adb5bd;
}

/* Status badges */
.badge.bg-success {
  background-color: #28a745 !important;
}

.badge.bg-secondary {
  background-color: #6c757d !important;
}

.badge.bg-danger {
  background-color: #dc3545 !important;
}
</style>