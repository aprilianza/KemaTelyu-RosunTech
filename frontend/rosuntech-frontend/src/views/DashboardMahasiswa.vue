<template>
  <div id="app" class="d-flex">
    <!-- Loading Screen untuk seluruh halaman -->
    <div v-if="isLoading" class="loading-overlay">
      <div class="loading-content">
        <div class="spinner-border text-primary mb-3" style="width: 3rem; height: 3rem;" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
        <h5 class="text-center">Memuat Dashboard...</h5>
      </div>
    </div>

    <!-- Main Content - hanya tampil jika sudah selesai loading -->
    <template v-else-if="isAuthenticated">
      <Sidebar />

      <div class="content-wrapper container">
        <!-- ================= USER PROFILE & STATS ================ -->
        <div class="user-profile-container mb-5">
          <div class="row g-4 align-items-center">
            <!-- User Profile -->
            <div class="col-12 col-lg-6">
              <div class="user-profile-card animate__animated animate__fadeInLeft">
                <div class="row align-items-center g-3">
                  <div class="col-auto">
                    <div class="profile-photo-container">
                      <img :src="profilePhoto" class="profile-photo rounded-circle" alt="Profile Photo" />
                    </div>
                  </div>
                  <div class="col">
                    <h3 class="fw-bold mb-2">{{ user.name }}</h3>
                    <p class="nim-text mb-2"><strong>NIM:</strong> {{ user.nim }}</p>
                    <span class="division-badge">
                      <i class="bi bi-mortarboard me-2"></i>{{ user.fakultas }}
                    </span>
                  </div>
                </div>
              </div>
            </div>

            <!-- Stats Cards -->
            <div class="col-12 col-lg-6">
              <div class="row g-3">
                <!-- Total Events -->
                <div class="col-6">
                  <div class="stats-card stats-events animate__animated animate__fadeInUp">
                    <div class="stats-icon">
                      <i class="bi bi-calendar-event"></i>
                    </div>
                    <div class="stats-content">
                      <h4 class="stats-number">{{ totalEvents }}</h4>
                      <p class="stats-label">Total Event</p>
                    </div>
                  </div>
                </div>

                <!-- History Link -->
                <div class="col-6" @click="$router.push({ name: 'History' })">
                  <div class="stats-card stats-history animate__animated animate__fadeInUp" style="animation-delay: 0.1s;">
                    <div class="stats-icon">
                      <i class="bi bi-clock-history"></i>
                    </div>
                    <div class="stats-content">
                      <h4 class="stats-label-main">Riwayat</h4>
                      <p class="stats-label">Lihat aktivitas</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- ================= EVENTS SECTION ================= -->
        <div class="events-section">
          <div class="section-header d-flex justify-content-between align-items-center mb-4">
            <h5 class="section-title mb-0">
              <i class="bi bi-calendar-event me-2"></i>EVENT TERSEDIA
            </h5>
          </div>

          <!-- Loading untuk events -->
          <div v-if="loadingEvents" class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
              <span class="visually-hidden">Loading...</span>
            </div>
            <p class="text-muted mt-2">Memuat event...</p>
          </div>

          <!-- ================= EVENT CARDS ================= -->
          <div v-else class="row justify-content-center g-4 mb-5">
            <div class="col-12 col-md-6 col-lg-4" v-for="(event, index) in events" :key="event.id">
              <div class="event-card animate__animated animate__fadeIn"
                   :style="{ 'animation-delay': index * 0.1 + 's' }">
                
                <!-- Event Image -->
                <div class="image-wrapper position-relative">
                  <img :src="`http://localhost:8888/${event.fotoPath}`" alt="Event Image" />
                  
                  <!-- Event Date Overlay -->
                  <div class="event-date-overlay">
                    <span class="date">{{ new Date(event.date).getDate() }}</span>
                    <span class="month">{{ new Date(event.date).toLocaleDateString('id-ID', { month: 'short' }) }}</span>
                  </div>

                </div>

                <!-- Event Content -->
                <div class="event-content">
                  <!-- Title and Actions -->
                  <div class="event-header">
                    <h5 class="event-title">{{ event.title }}</h5>
                    <a href="#" class="see-more" @click.prevent="openModal(event)">
                      <i class="bi bi-arrow-right me-1"></i>Lihat detail
                    </a>
                  </div>

                  <!-- Event Meta Info -->
                  <div class="event-meta">
                    <div class="meta-item">
                      <div class="meta-icon">
                        <i class="bi bi-clock"></i>
                      </div>
                      <span class="meta-text">{{ event.time.substring(0, 5) }}</span>
                    </div>
                    
                    <div class="meta-item">
                      <div class="meta-icon">
                        <i class="bi bi-people"></i>
                      </div>
                      <span class="meta-text">Max {{ event.maxParticipant }} peserta</span>
                    </div>
                  </div>

                  <!-- Action Button -->
                  <div class="event-actions mt-auto">
                    <button 
                      class="btn btn-register" 
                      @click="registerToEvent(event.id)">
                      <i class="bi bi-plus-circle me-2"></i>Daftar Event
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- ================= DETAIL MODAL ================= -->
        <div v-if="selectedEvent" class="modern-modal-container animate__animated animate__fadeIn">
          <div class="modern-modal-backdrop" @click="closeModal"></div>
          <div class="modern-modal animate__animated animate__zoomIn">
            <button type="button" class="modern-modal-close" @click="closeModal">
              <span class="close-icon">&times;</span>
            </button>

            <!-- Modal Image -->
            <div class="modern-modal-image">
              <img :src="`http://localhost:8888/${selectedEvent.fotoPath}`" alt="Event Image" />
              <div class="event-date-badge">
                <div class="date-content">
                  <span class="day">{{ new Date(selectedEvent.date).getDate() }}</span>
                  <span class="month">{{ new Date(selectedEvent.date).toLocaleDateString('id-ID', { month: 'long' }) }}</span>
                  <span class="year">{{ new Date(selectedEvent.date).getFullYear() }}</span>
                </div>
              </div>
            </div>

            <!-- Modal Content -->
            <div class="modern-modal-content">
              <h2 class="modal-event-title">{{ selectedEvent.title }}</h2>

              <div class="modal-event-metadata">
                <div class="metadata-item">
                  <span class="metadata-icon calendar-icon"></span>
                  <span>{{ formatDate(selectedEvent.date) }}</span>
                </div>
                <div class="metadata-item">
                  <span class="metadata-icon clock-icon"></span>
                  <span>{{ selectedEvent.time.substring(0, 5) }}</span>
                </div>
                <div class="metadata-item">
                  <span class="metadata-icon users-icon"></span>
                  <span>Max {{ selectedEvent.maxParticipant }} peserta</span>
                </div>
              </div>

              <div class="modal-event-description">
                <h5 class="description-title">Deskripsi Event</h5>
                <p>{{ selectedEvent.description }}</p>
              </div>

              <div class="modern-modal-actions">
                <button type="button" class="btn btn-outline-secondary" @click="closeModal">
                  Tutup
                </button>
                <button 
                  v-if="!selectedEvent.registered" 
                  type="button" 
                  class="btn btn-register" 
                  @click="registerToEvent(selectedEvent.id)">
                  <i class="bi bi-plus-circle me-2"></i>Daftar Event
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue';
import api from '@/api/axios';
import { getEvents } from '@/api/event';

export default {
  name: 'DashboardMahasiswa',
  components: { Sidebar },
  data() {
    return {
      isLoading: true,
      isAuthenticated: false,
      loadingEvents: false,
      user: JSON.parse(localStorage.getItem('user')) || {
        name: '', nim: '', fakultas: '', fotoPath: ''
      },
      events: [],
      selectedEvent: null
    };
  },
  computed: {
    totalEvents() {
      return this.events.length;
    },
    profilePhoto() {
      if (this.user.fotoPath?.startsWith('http')) return this.user.fotoPath;
      if (this.user.fotoPath) {
        return `http://localhost:8888/${this.user.fotoPath}`;
      }
      return require('@/assets/img/profile.png');
    }
  },
  async mounted() {
    await this.initializePage();
  },
  methods: {
    async initializePage() {
      try {
        this.isLoading = true;
        
        // Check authentication
        await this.checkAuthentication();
        
        if (this.isAuthenticated) {
          await this.fetchEvents();
        }
      } catch (error) {
        console.error('Error initializing page:', error);
      } finally {
        this.isLoading = false;
      }
    },

    async checkAuthentication() {
      const token = localStorage.getItem('token');
      if (!token) {
        this.$router.push('/');
        return;
      }
      
      try {
        const res = await api.get('/api/auth/me');
        this.user = res.data;
        this.isAuthenticated = true;
      } catch (err) {
        console.error('Authentication failed:', err);
        this.$router.push('/');
      }
    },

    async fetchEvents() {
      this.loadingEvents = true;
      try {
        const res = await getEvents();
        this.events = res.data.map(event => ({
          ...event,
          registered: event.registrations?.some(reg => reg.nim === this.user.nim && reg.status === 'APPROVED') || false
        }));
      } catch (error) {
        console.error("Error fetching events:", error);
      } finally {
        this.loadingEvents = false;
      }
    },

    async registerToEvent(eventId) {
      try {
        const event = this.events.find(e => e.id === eventId);
        if (!event) {
          this.$swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Event tidak ditemukan.',
            confirmButtonText: 'OK'
          });
          return;
        }

        // Check capacity
        const currentRegistrations = this.events.find(e => e.id === eventId)?.registrations || [];
        const registeredCount = currentRegistrations.filter(r => r.status === 'APPROVED' || r.status === 'PENDING').length;

        if (registeredCount >= event.maxParticipant) {
          this.$swal.fire({
            icon: 'error',
            title: 'Pendaftaran Gagal',
            text: 'Maaf, kapasitas peserta untuk event ini sudah penuh.',
            confirmButtonText: 'OK'
          });
          return;
        }

        const token = localStorage.getItem('token');
        if (!token) throw new Error("User belum login");
        
        const res = await api.post(`/api/events/${eventId}/register`, {}, {
          headers: { Authorization: `Bearer ${token}` }
        });

        if (res.data.status) {
          this.$swal.fire({
              icon: 'success',
              title: 'Pendaftaran Berhasil',
              text: `Pendaftaran Anda sudah masuk, ditunggu persetujuannya ya`,
              confirmButtonText: 'OK'
            });
        } 
        this.closeModal();
        this.fetchEvents();
      } catch (error) {
        console.error('Gagal mendaftar:', error);
        let message = 'Pendaftaran gagal.';
        if (error.response && error.response.data && error.response.data.message) {
          message = error.response.data.message;
        }

        this.$swal.fire({
          icon: 'error',
          title: 'Pendaftaran Gagal',
          text: message,
          confirmButtonText: 'OK'
        });
      }
    },

    openModal(event) {
      this.selectedEvent = event;
      document.body.style.overflow = 'hidden';
    },

    closeModal() {
      this.selectedEvent = null;
      document.body.style.overflow = '';
    },
    
    formatDate(dateStr) {
      return new Date(dateStr).toLocaleDateString('id-ID', {
        day: 'numeric', month: 'long', year: 'numeric'
      });
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
        if (!user.role || user.role.toUpperCase() !== 'MAHASISWA') {
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
@import 'https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css';

.loading-overlay {
 position: fixed;
 top: 0;
 left: 0;
 width: 100vw;
 height: 100vh;
 background: rgba(255, 241, 241, 0.95);
 backdrop-filter: blur(5px);
 display: flex;
 justify-content: center;
 align-items: center;
 z-index: 9999;
}

.loading-content {
 text-align: center;
 background: white;
 padding: 2rem;
 border-radius: 16px;
 box-shadow: 0 8px 32px rgba(179, 2, 2, 0.1);
 border: 1px solid rgba(255, 241, 241, 0.5);
}

.loading-content h5 {
 color: #868686;
 margin-bottom: 0.5rem;
 font-weight: 600;
}

/* ================= MAIN LAYOUT ================= */
.content-wrapper {
 padding-top: 120px;
 padding-bottom: 3rem;
}

/* ================= USER PROFILE SECTION ================= */
.user-profile-container {
 margin-bottom: 2rem;
}

.user-profile-card {
 background: linear-gradient(135deg, #2c3e50, #b30202);
 border-radius: 20px;
 padding: 2rem;
 color: white;
 box-shadow: 0 8px 32px rgba(179, 2, 2, 0.3);
 border: 1px solid rgba(255, 241, 241, 0.3);
 transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.user-profile-card:hover {
 transform: translateY(-5px);
 box-shadow: 0 12px 40px rgba(179, 2, 2, 0.4);
}

.profile-photo-container {
 display: inline-block;
 padding: 4px;
 border-radius: 50%;
 background: linear-gradient(45deg, #fff, rgba(255, 241, 241, 0.8));
 box-shadow: 0 4px 20px rgba(179, 2, 2, 0.2);
}

.profile-photo {
 width: 100px;
 height: 100px;
 object-fit: cover;
 border: 3px solid white;
 transition: transform 0.3s ease;
}

.profile-photo:hover {
 transform: scale(1.05);
}

.nim-text {
 color: rgba(255, 255, 255, 0.9);
 font-size: 1rem;
}

.division-badge {
 display: inline-flex;
 align-items: center;
 background: rgba(255, 255, 255, 0.25);
 color: white;
 padding: 0.5rem 1rem;
 border-radius: 25px;
 font-weight: 500;
 font-size: 0.9rem;
 backdrop-filter: blur(10px);
 border: 1px solid rgba(255, 255, 255, 0.3);
}

/* ================= STATS CARDS ================= */
.stats-card {
 background: white;
 border-radius: 16px;
 padding: 1.5rem;
 height: 100%;
 display: flex;
 align-items: center;
 box-shadow: 0 4px 20px rgba(179, 2, 2, 0.08);
 border: 1px solid rgba(255, 241, 241, 0.5);
 transition: all 0.3s ease;
 cursor: pointer;
}

.stats-card:hover {
 transform: translateY(-5px);
 box-shadow: 0 8px 30px rgba(179, 2, 2, 0.15);
}

.stats-events {
 border-left: 4px solid #868686;
}

.stats-events:hover {
 background: linear-gradient(135deg, #868686, #666);
 color: white;
}

.stats-history {
 border-left: 4px solid #DD3232;
}

.stats-history:hover {
 background: linear-gradient(135deg, #DD3232, #B30202);
 color: white;
}

.stats-icon {
 font-size: 2rem;
 margin-right: 1rem;
 opacity: 0.8;
}

.stats-content {
 flex: 1;
}

.stats-number {
 font-size: 2rem;
 font-weight: 700;
 margin: 0;
 line-height: 1;
}

.stats-label {
 font-size: 0.9rem;
 margin: 0;
 opacity: 0.8;
}

.stats-label-main {
 font-size: 1.2rem;
 font-weight: 600;
 margin: 0;
 line-height: 1;
}

/* ================= SECTION HEADER ================= */
.section-header {
 border-bottom: 2px solid rgba(134, 134, 134, 0.2);
 padding-bottom: 0.75rem;
}

.section-title {
 color: #868686;
 font-weight: 600;
 text-transform: uppercase;
 letter-spacing: 0.5px;
 position: relative;
 padding-left: 0.6rem;
}

.section-title::before {
 content: '';
 position: absolute;
 left: 0;
 top: 0;
 height: 100%;
 width: 4px;
 background: #868686;
 border-radius: 4px;
}

/* ================= EVENT CARDS ================= */
.event-card {
 background: white;
 border-radius: 20px;
 overflow: hidden;
 transition: all 0.3s ease;
 box-shadow: 0 4px 20px rgba(179, 2, 2, 0.08);
 border: 1px solid rgba(255, 241, 241, 0.5);
 height: 100%;
 display: flex;
 flex-direction: column;
}

.event-card:hover {
 transform: translateY(-8px);
 box-shadow: 0 12px 40px rgba(179, 2, 2, 0.15);
 border-color: rgba(221, 50, 50, 0.3);
}

.image-wrapper {
 height: 200px;
 overflow: hidden;
 position: relative;
}

.image-wrapper img {
 width: 100%;
 height: 100%;
 object-fit: cover;
 transition: transform 0.5s ease;
}

.image-wrapper:hover img {
 transform: scale(1.08);
}

.event-date-overlay {
 position: absolute;
 top: 15px;
 right: 15px;
 background: white;
 color: #868686;
 border-radius: 12px;
 padding: 0.5rem 0.8rem;
 display: flex;
 flex-direction: column;
 align-items: center;
 min-width: 60px;
 box-shadow: 0 4px 20px rgba(179, 2, 2, 0.15);
 backdrop-filter: blur(10px);
}

.event-date-overlay .date {
 font-size: 1.4rem;
 font-weight: 700;
 line-height: 1;
 color: #B30202;
}

.event-date-overlay .month {
 font-size: 0.8rem;
 text-transform: uppercase;
 opacity: 0.8;
 color: #868686;
}

.registration-status-badge {
 position: absolute;
 top: 15px;
 left: 15px;
 background: linear-gradient(135deg, #DD3232, #B30202);
 color: white;
 padding: 0.4rem 0.8rem;
 border-radius: 20px;
 font-size: 0.8rem;
 font-weight: 600;
 box-shadow: 0 4px 15px rgba(179, 2, 2, 0.3);
}

.event-content {
 padding: 1.5rem;
 flex: 1;
 display: flex;
 flex-direction: column;
}

.event-header {
 margin-bottom: 1rem;
}

.event-title {
 font-size: 1.25rem;
 font-weight: 700;
 color: #2c3e50;
 margin-bottom: 0.5rem;
 line-height: 1.3;
 display: -webkit-box;
 -webkit-line-clamp: 2;
 -webkit-box-orient: vertical;
 overflow: hidden;
}

.see-more {
 color: #868686;
 font-size: 0.9rem;
 text-decoration: none;
 font-weight: 500;
 display: inline-flex;
 align-items: center;
 transition: all 0.3s ease;
}

.see-more:hover {
 color: #B30202;
 text-decoration: none;
 transform: translateX(5px);
}

.event-meta {
 display: flex;
 flex-direction: column;
 gap: 0.8rem;
 margin-bottom: 1.5rem;
}

.meta-item {
 display: flex;
 align-items: center;
 gap: 0.7rem;
 padding: 0.6rem 0.8rem;
 background-color: #FFF1F1;
 border-radius: 10px;
 border-left: 3px solid #DD3232;
}

.meta-icon {
 width: 18px;
 height: 18px;
 color: #B30202;
 flex-shrink: 0;
 display: flex;
 align-items: center;
 justify-content: center;
}

.meta-text {
 font-size: 0.9rem;
 font-weight: 500;
 color: #495057;
}

.event-actions {
 margin-top: auto;
}

.btn-register {
 background: linear-gradient(135deg, #DD3232, #B30202);
 color: white;
 border: none;
 padding: 0.75rem 1.5rem;
 border-radius: 25px;
 font-weight: 600;
 font-size: 0.9rem;
 width: 100%;
 transition: all 0.3s ease;
 display: flex;
 align-items: center;
 justify-content: center;
}

.btn-register:hover {
 background: linear-gradient(135deg, #B30202, #901a1a);
 transform: translateY(-2px);
 box-shadow: 0 6px 20px rgba(179, 2, 2, 0.4);
 color: white;
}

.btn-registered {
 background: linear-gradient(135deg, #868686, #666);
 color: white;
 border: none;
 padding: 0.75rem 1.5rem;
 border-radius: 25px;
 font-weight: 600;
 font-size: 0.9rem;
 width: 100%;
 display: flex;
 align-items: center;
 justify-content: center;
 cursor: not-allowed;
 opacity: 0.8;
}

/* ================= MODAL STYLES ================= */
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
 backdrop-filter: blur(8px);
 z-index: 1051;
}

.modern-modal {
 background-color: white;
 width: 90%;
 max-width: 800px;
 border-radius: 20px;
 overflow: hidden;
 z-index: 1052;
 position: relative;
 box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
 max-height: 90vh;
 overflow-y: auto;
 scrollbar-width: none;
 -ms-overflow-style: none;
}

.modern-modal::-webkit-scrollbar {
 display: none;
}

.modern-modal-close {
 position: absolute;
 top: 20px;
 right: 20px;
 background: rgba(0, 0, 0, 0.7);
 border: 2px solid rgba(255, 255, 255, 0.3);
 width: 40px;
 height: 40px;
 border-radius: 50%;
 display: flex;
 align-items: center;
 justify-content: center;
 cursor: pointer;
 z-index: 1060;
 box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
 transition: all 0.3s ease;
 backdrop-filter: blur(10px);
}

.modern-modal-close:hover {
 background: rgba(0, 0, 0, 0.9);
 transform: scale(1.1);
 border-color: rgba(255, 255, 255, 0.5);
}

.close-icon {
 color: white;
 font-size: 20px;
 line-height: 1;
 font-weight: bold;
}

.modern-modal-image {
 width: 100%;
 height: 300px;
 overflow: hidden;
 position: relative;
 background: linear-gradient(135deg, #FFF1F1 0%, #DD3232 100%);
}

.modern-modal-image img {
 width: 100%;
 height: 100%;
 object-fit: cover;
 display: block;
}

.event-date-badge {
 position: absolute;
 top: 20px;
 left: 20px;
 background: white;
 border-radius: 16px;
 padding: 1rem 1.5rem;
 box-shadow: 0 8px 30px rgba(179, 2, 2, 0.15);
 backdrop-filter: blur(10px);
}

.date-content {
 display: flex;
 flex-direction: column;
 align-items: center;
 line-height: 1.2;
}

.date-content .day {
 font-size: 2rem;
 font-weight: 800;
 color: #B30202;
 line-height: 1;
}

.date-content .month {
 font-size: 1rem;
 font-weight: 600;
 text-transform: uppercase;
 color: #868686;
 margin-top: 0.2rem;
}

.date-content .year {
 font-size: 0.8rem;
 color: #868686;
 margin-top: 0.1rem;
}

.modern-modal-content {
 padding: 2.5rem;
}

.modal-event-title {
 font-size: 2.2rem;
 font-weight: 800;
 margin-bottom: 1.5rem;
 color: #2c3e50;
 line-height: 1.2;
}

.modal-event-metadata {
 display: flex;
 flex-wrap: wrap;
 gap: 2rem;
 margin-bottom: 2rem;
 color: #495057;
 font-weight: 500;
}

.metadata-item {
 display: flex;
 align-items: center;
 gap: 0.7rem;
 padding: 0.8rem 1.2rem;
 background: #FFF1F1;
 border-radius: 12px;
 border-left: 4px solid #DD3232;
}

.metadata-icon {
 display: inline-block;
 width: 20px;
 height: 20px;
 background-position: center;
 background-repeat: no-repeat;
 background-size: contain;
 color: #B30202;
}

.calendar-icon {
 background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='%23B30202' class='bi bi-calendar' viewBox='0 0 16 16'%3E%3Cpath d='M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5zM1 4v10a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V4H1z'/%3E%3C/svg%3E");
}

.clock-icon {
 background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='%23B30202' class='bi bi-clock' viewBox='0 0 16 16'%3E%3Cpath d='M8 3.5a.5.5 0 0 0-1 0V9a.5.5 0 0 0 .252.434l3.5 2a.5.5 0 0 0 .496-.868L8 8.71V3.5z'/%3E%3Cpath d='M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zm7-8A7 7 0 1 1 1 8a7 7 0 0 1 14 0z'/%3E%3C/svg%3E");
}

.users-icon {
 background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='%23B30202' class='bi bi-people' viewBox='0 0 16 16'%3E%3Cpath d='M15 14s1 0 1-1-1-4-5-4-5 3-5 4 1 1 1 1h8zm-7.978-1A.261.261 0 0 1 7 12.996c.001-.264.167-1.03.76-1.72C8.312 10.629 9.282 10 11 10c1.717 0 2.687.63 3.24 1.276.593.69.758 1.457.76 1.72l-.008.002a.274.274 0 0 1-.014.002H7.022zM11 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4zm3-2a3 3 0 1 1-6 0 3 3 0 0 1 6 0zM6.936 9.28a5.88 5.88 0 0 0-1.23-.247A7.35 7.35 0 0 0 5 9c-4 0-5 3-5 4 0 .667.333 1 1 1h4.216A2.238 2.238 0 0 1 5 13c0-1.01.377-2.042 1.09-2.904.243-.294.526-.569.846-.816zM4.92 10A5.493 5.493 0 0 0 4 13H1c0-.26.164-1.03.76-1.724.545-.636 1.492-1.256 3.16-1.275zM1.5 5.5a3 3 0 1 1 6 0 3 3 0 0 1-6 0zm3-2a2 2 0 1 0 0 4 2 2 0 0 0 0-4z'/%3E%3C/svg%3E");
}

.modal-event-description {
 margin-bottom: 2rem;
}

.description-title {
 font-size: 1.3rem;
 font-weight: 700;
 color: #2c3e50;
 margin-bottom: 1rem;
 padding-bottom: 0.5rem;
 border-bottom: 2px solid rgba(221, 50, 50, 0.2);
}

.modal-event-description p {
 font-size: 1.1rem;
 line-height: 1.7;
 color: #555;
 margin: 0;
}

.registration-info-card {
 background: linear-gradient(135deg, #FFF1F1, rgba(221, 50, 50, 0.1));
 border: 1px solid rgba(221, 50, 50, 0.2);
 border-radius: 16px;
 padding: 1.5rem;
 margin-bottom: 2rem;
}

.registration-info-header {
 display: flex;
 align-items: center;
 margin-bottom: 0.5rem;
 color: #B30202;
 font-size: 1.1rem;
}

.registration-info-text {
 color: #B30202;
 margin: 0;
 font-size: 0.95rem;
 line-height: 1.5;
}

.modern-modal-actions {
 display: flex;
 justify-content: flex-end;
 gap: 1rem;
 margin-top: 2rem;
 padding-top: 2rem;
 border-top: 1px solid #e9ecef;
}

.btn-outline-secondary {
 color: #868686;
 border-color: #868686;
 padding: 0.75rem 1.5rem;
 border-radius: 25px;
 font-weight: 600;
 transition: all 0.3s ease;
}

.btn-outline-secondary:hover {
 background-color: #868686;
 color: white;
 transform: translateY(-2px);
 box-shadow: 0 4px 15px rgba(134, 134, 134, 0.3);
}

/* ================= RESPONSIVE DESIGN ================= */
@media (max-width: 768px) {
 .content-wrapper {
   padding-top: 100px;
 }
 
 .user-profile-card {
   padding: 1.5rem;
 }
 
 .profile-photo {
   width: 80px;
   height: 80px;
 }
 
 .stats-card {
   padding: 1rem;
 }
 
 .stats-number {
   font-size: 1.5rem;
 }
 
 .event-card {
   margin-bottom: 1rem;
 }
 
 .image-wrapper {
   height: 180px;
 }
 
 .event-content {
   padding: 1rem;
 }
 
 .event-meta {
   gap: 0.6rem;
 }
 
 .meta-item {
   padding: 0.5rem 0.7rem;
 }
 
 .modal-event-metadata {
   gap: 1rem;
   flex-direction: column;
 }
 
 .modal-event-title {
   font-size: 1.6rem;
 }
 
 .modern-modal {
   width: 95%;
   margin: 1rem;
 }
 
 .modern-modal-content {
   padding: 1.5rem;
 }
 
 .modern-modal-image {
   height: 200px;
 }
 
 .modern-modal-actions {
   flex-direction: column;
   gap: 0.5rem;
 }
 
 .btn-register,
 .btn-outline-secondary {
   width: 100%;
 }
}

@media (max-width: 576px) {
 .user-profile-row {
   text-align: center;
 }
 
 .stats-card {
   text-align: center;
 }
 
 .stats-icon {
   margin-right: 0;
   margin-bottom: 0.5rem;
 }
 
 .event-date-overlay {
   top: 10px;
   right: 10px;
   padding: 0.4rem 0.6rem;
   min-width: 50px;
 }
 
 .event-date-overlay .date {
   font-size: 1.2rem;
 }
 
 .event-date-overlay .month {
   font-size: 0.7rem;
 }
 
 .registration-status-badge {
   top: 10px;
   left: 10px;
   padding: 0.3rem 0.6rem;
   font-size: 0.7rem;
 }
}

/* ================= ANIMATION ENHANCEMENTS ================= */
.animate__fadeIn {
 --animate-duration: 0.8s;
}

.animate__fadeInLeft {
 --animate-duration: 0.8s;
}

.animate__fadeInUp {
 --animate-duration: 0.6s;
}

.animate__zoomIn {
 --animate-duration: 0.5s;
}

/* ================= LOADING STATES ================= */
.spinner-border {
 width: 3rem;
 height: 3rem;
 border-width: 0.3em;
 color: #B30202;
}

/* ================= UTILITY CLASSES ================= */
.text-primary {
 color: #B30202 !important;
}

/* ================= SCROLL BEHAVIOR ================= */
html {
 scroll-behavior: smooth;
}

body.modal-open {
 overflow: hidden;
}

/* ================= FOCUS STATES ================= */
.btn:focus,
.stats-card:focus {
 outline: 2px solid rgba(179, 2, 2, 0.5);
 outline-offset: 2px;
}

/* ================= HOVER EFFECTS ================= */
.event-card .meta-item:hover {
 background-color: rgba(221, 50, 50, 0.1);
 border-left-color: #B30202;
}

.metadata-item:hover {
 background-color: rgba(221, 50, 50, 0.1);
 transform: translateX(5px);
 transition: all 0.3s ease;
}
</style>