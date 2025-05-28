<template>
  <div id="app" class="d-flex">
    <Sidebar />

    <div class="content-wrapper container">
      <div class="row g-4 mb-5 animate__animated animate__fadeIn">
        <div class="col-lg-6 col-md-12 mb-3">
          <div class="card user-profile-card h-100 animate__animated animate__fadeInLeft">
            <div class="card-body">
              <div class="row align-items-center user-profile-row">
              <div class="col-auto">
                <div class="profile-photo-container">
                  <img :src="profilePhoto" class="profile-photo rounded-circle" alt="Profile Photo" />
                </div>
            </div>
                <div class="col text-start">
                  <h3 class="fw-bold mb-2">{{ user.name }}</h3>
                  <p><strong>NIM:</strong> {{ user.nim }}</p>
                  <span class="division-badge">{{ user.fakultas }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        

        <div class="col-lg-3 col-md-6 mb-3">
          <div class="card total-event-card h-100 animate__animated animate__fadeInRight">
            <div class="card-body text-center d-flex flex-column justify-content-center">
              <h1 class="total-events">{{ totalEvents }}</h1>
              <h5>Total Event</h5>
            </div>
          </div>
        </div>

        <div class="col-lg-3 col-md-6 mb-3" @click="$router.push({ name: 'History' })">
          <div class="card history-card h-100 animate__animated animate__fadeInRight" style="animation-delay: 0.2s;">
            <div class="card-body d-flex flex-column align-items-center justify-content-center">
              <img src="@/assets/img/history.png" alt="History" class="history-icon" />
              <h5 class="mt-3">Riwayat</h5>
            </div>
          </div>
        </div>
      </div>

     <div class="events-section">
  <div class="section-header d-flex justify-content-between align-items-center mb-4">
    <h5 class="section-title mb-0 text-uppercase fw-semibold">
      <i class="bi bi-calendar-event me-2"></i>EVENTS
    </h5>
  </div>
</div>

     <div v-if="loadingEvents" class="text-center py-5">
    <div class="spinner-border text-primary" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
    </div>
      <div v-else class="row justify-content-center g-4 mb-5">
        <div class="col-12 col-md-6" v-for="(event, index) in events" :key="event.id">
          <div class="event-card d-flex flex-column animate__animated animate__fadeIn" 
               :style="{'animation-delay': index * 0.1 + 's'}">
            <div class="image-wrapper hover-zoom">
              <img :src="`http://localhost:8888/${event.fotoPath}`" alt="Event Image" />
            </div>
           
            <h5 class="event-title">{{ event.title }}</h5>
            <a href="#" class="text-start see-more mb-3" @click.prevent="openModal(event)">
              lihat detail
            </a>

          

            <div class="d-flex justify-content-end align-items-center gap-2 mt-auto event-date-time-wrapper">
            <div class="time-box-small shadow-glow">
              <svg class="clock-icon-small" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <circle cx="12" cy="12" r="10"></circle>
              <polyline points="12 6 12 12 16 14"></polyline>
              </svg>
           <span class="time-text-small">{{ event.time }}</span>
          </div>
            <div class="date-box-small shadow-glow">
              <div class="date-day-small">{{ new Date(event.date).getDate() }}</div>
              <div class="date-month-year-small">
      {{ new Date(event.date).toLocaleDateString('id-ID', { month: 'short', year: 'numeric' }) }}
    </div>
            </div>
        </div>
      </div>
      </div>
    </div>

      <div v-if="selectedEvent" class="modern-modal-container animate__animated animate__fadeIn">
        <div class="modern-modal-backdrop" @click="closeModal"></div>
        <div class="modern-modal animate__animated animate__zoomIn">
          <button type="button" class="modern-modal-close" @click="closeModal">
            <span class="close-icon">&times;</span>
          </button>
          
        
          <div class="modern-modal-image">
            <img :src="`http://localhost:8888/${selectedEvent.fotoPath}`" alt="Event Image" />
          </div>

          <div class="modern-modal-content">
            <h2 class="modal-event-title">{{ selectedEvent.title }}</h2>

            <div class="modal-event-metadata">
              <div class="metadata-item">
                <span class="metadata-icon calendar-icon"></span>
                <span>{{ formatDate(selectedEvent.date) }}</span>
              </div>
              <div class="metadata-item">
                <span class="metadata-icon clock-icon"></span>
                <span>{{ selectedEvent.time }}</span>
              </div>
              <div class="metadata-item">
                <span class="metadata-icon users-icon"></span>
                <span>{{ selectedEvent.maxParticipant}}</span>
              </div>
            </div>

            <div class="modal-event-description">
              <p>{{ selectedEvent.description }}</p>
            </div>

            <div class="modern-modal-actions">
              <button type="button" class="btn register-btn" @click="registerToEvent(selectedEvent.id)">
                Register
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
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
  mounted() {
    const token = localStorage.getItem('token');
    if (!token) {
      this.$router.push('/');
      return;
    }
    this.fetchCurrentUser();
    this.fetchEvents();
    this.loadFontAwesome();
  },
  methods: {
    async fetchEvents() {
      try {
        const res = await getEvents();
       this.events = res.data.map(event => ({
          ...event,
          registered: event.registrations?.some(reg => reg.nim === this.user.nim && reg.status === 'APPROVED') || false
        }));
      } catch (error) {
        console.error("Error fetching events:", error);
      }
    },
    async fetchCurrentUser() {
      try {
        const res = await api.get('/api/auth/me');
        this.user = res.data;
      } catch (err) {
        console.error('Gagal ambil data user:', err);
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

    // Hitung jumlah peserta yang sudah daftar dengan status APPROVED atau PENDING
    const currentRegistrations = this.events.find(e => e.id === eventId)?.registrations || [];
    const registeredCount = currentRegistrations.filter(r => r.status === 'APPROVED' || r.status === 'PENDING').length;

    if (registeredCount >= event.maxParticipant) {
      this.$swal.fire({
        icon: 'error',
        title: 'Pendaftaran Gagal',
        text: 'Maaf, kapasitas peserta untuk event ini sudah penuh.',
        confirmButtonText: 'OK'
      });
      return; // stop proses pendaftaran
    }

        const token = localStorage.getItem('token');
        if (!token) throw new Error("User belum login");
        const res = await api.post(`/api/events/${eventId}/register`, {}, {
          headers: { Authorization: `Bearer ${token}` }
        });

      if (res.data.status) {
      if (res.data.status === 'PENDING') {
        // Notifikasi pendaftaran PENDING
        this.$swal.fire({
          icon: 'info',
          title: 'Pendaftaran Berhasil',
          text: `Pendaftaran Anda sudah masuk, ditunggu persetujuannya ya`,
          confirmButtonText: 'OK'
        });
      } else if (res.data.status === 'APPROVED') {
        this.$swal.fire({
          icon: 'success',
          title: 'Pendaftaran Disetujui',
          text: 'Pendaftaran berhasil dan disetujui!',
          confirmButtonText: 'OK'
        });
      }else if (res.data.status === 'REJECTED') {
        this.$swal.fire({
          icon: 'error',
          title: 'Pendaftaran Ditolak',
          text: 'Maaf, pendaftaran Anda ditolak oleh staff.',
          confirmButtonText: 'OK'
        });
      }  else {
        this.$swal.fire({
          icon: 'info',
          title: 'Pendaftaran Berhasil',
          text: `Pendaftaran Anda sudah masuk, ditunggu persetujuannya ya`,
          confirmButtonText: 'OK'
        });
      }
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
    loadFontAwesome() {
      if (!document.getElementById('font-awesome-css')) {
        const link = document.createElement('link');
        link.id = 'font-awesome-css';
        link.rel = 'stylesheet';
        link.href = 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css';
        document.head.appendChild(link);
      }
    },
    async cancelRegistration(eventId) {
    const confirmed = await this.$swal.fire({
      icon: 'warning',
      title: 'Batalkan Pendaftaran',
      text: 'Apakah Anda yakin ingin membatalkan pendaftaran event ini?',
      showCancelButton: true,
      confirmButtonText: 'Ya, batalkan',
      cancelButtonText: 'Tidak',
      reverseButtons: true,
    });

    if (confirmed.isConfirmed) {
      try {
        // Panggil API hapus registration
        await this.$api.delete(`/registrations/${eventId}`);

        // Refresh data registrasi (fetch ulang dari backend)
        await this.fetchRegistrations();

        this.$swal.fire({
          icon: 'success',
          title: 'Pendaftaran berhasil dibatalkan',
          timer: 2000,
          showConfirmButton: false,
        });
      } catch (error) {
        console.error('Gagal membatalkan pendaftaran:', error);
        this.$swal.fire({
          icon: 'error',
          title: 'Gagal membatalkan pendaftaran',
          text: 'Silakan coba lagi nanti',
        });
      }
    }
  },
    
    formatDate(dateStr) {
      return new Date(dateStr).toLocaleDateString('id-ID', {
        day: 'numeric', month: 'long', year: 'numeric'
      });
    }
  }
};
</script>

<style scoped>
/* Import Animate.css */
@import 'https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css';

.content-wrapper {
  padding-top: 120px; /* tingkatkan dari 80px ke 100px atau lebih */
  padding-bottom: 3rem;
}

/* Profile */

.profile-photo-container {
  display: inline-block;
  padding: 4px;
  border-radius: 50%;
  background-image: linear-gradient(45deg, #b30202, #dd3232);
  box-shadow: 0 4px 10px rgba(179, 2, 2, 0.3);
}

.profile-photo {
  width: 172px;  /* dikurangi padding border */
  height: 172px;
  object-fit: cover;
  border: 4px solid white;
  transition: transform 0.3s ease;
  border-radius: 50%;
}


.profile-photo:hover {
  transform: scale(1.05);
}

.user-info h3 {
  font-weight: 700;
  font-size: 1.75rem;
  color: #212121;
  margin-bottom: 0.5rem;
}

.user-info p {
  font-size: 1.1rem;
  margin-bottom: 0.3rem;
  color: #444;
}

/* Card for User Profile */
.user-profile-row {
  gap: 1.5rem; /* beri jarak antara foto dan info */
}

.user-profile-card {
  background-color: #fff;
  border-radius: 1rem;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.user-profile-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

/* History & Total Event Card */
.history-card {
  background-color: rgba(229, 26, 26, 0.94);
  color: white;
  border-radius: 1rem;
  box-shadow: 0 4px 8px rgba(222, 11, 11, 0.47);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
}


.total-event-card {
  transform: translateY(-8px);
  background-color: v-bind('$colors.primary');
  color: white;
  border-radius: 1rem;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.total-event-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 20px rgba(26, 26, 26, 0.2);
}



.history-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 20px rgba(223, 9, 9, 0.2);
}



.total-event-card {
  transform: translateY(-8px);
  cursor: default;
}

.history-card .card-body,
.total-event-card .card-body {
  transform: translateY(-8px);
  text-align: center;
}

.history-icon {
  width: 170px;
  height: 140px;
  transition: transform 0.3s ease;
}

.history-card:hover .history-icon {
   transform: translateY(-8px);
}

h5 {
  font-size: 1.2rem;
}

h1 {
  font-size: 125px;
}

image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.image-wrapper:hover img {
  transform: scale(1.05);
}

.division-badge {
  display: inline-block;
  background-color: rgba(179, 2, 2, 0.1);
  color: #b30202;
  padding: 0.5rem 1rem;
  border-radius: 2rem;
  font-weight: 600;
  width: fit-content;
  font-size: 1rem;
}

/* Event Card */
.event-date-overlay {
 position: absolute;
  position: absolute;
  top: 15px;
  right: 15px;
  background-color: white;
  color: v-bind('$colors.primary');
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
  line-height: 1;
}

.event-date-overlay .month {
  font-size: 0.8rem;
  text-transform: uppercase;
}

.event-card {
  background: linear-gradient(135deg, #2c3e50, v-bind('$colors.primary'));
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);

}

@keyframes popupEffect {
  0% {
    transform: scale(0.5);
    opacity: 0;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

.event-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 30px rgba(0,0,0,0.2);
}

.image-wrapper {
  height: 180px;
  overflow: hidden;

}

.image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.image-wrapper:hover img {
  transform: scale(1.05);
}

.hover-zoom img {
  width: 100%;
  height: 180px;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.hover-zoom:hover img {
  transform: scale(1.1);
}

.event-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: white;
  margin: 0.5rem 1rem 0 1rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.see-more {
  color: rgba(243, 196, 196, 0.77);
  font-size: 0.9rem;
  text-decoration: none;
  margin-left: 1rem;
  margin-top: 0.25rem;
  display: inline-block;
  cursor: pointer;
}

.see-more:hover {
  color:rgb(240, 240, 240);
  text-decoration: underline;
}

.date-badge,
.time-badge {
  background-color: #fff;
  color: #b30202;
  border-radius: 2rem;
  padding: 0.5rem 1rem;
  font-weight: 600;
  font-size: 0.9rem;
  margin-right: 0.5rem;
  cursor: default;
  user-select: none;
}

.date-badge:hover,
.time-badge:hover {
  transform: scale(1.05);
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

.modern-modal-body {
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
  padding: 2rem;
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
  max-width: 700px;
  border-radius: 1.5rem;
  overflow: hidden;
  z-index: 1052;
  position: relative;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
}

/* Enhanced Close Button */
.modern-modal-close {
  position: absolute;
  top: 15px;
  right: 15px;
  background-color: rgba(179, 2, 2, 0.8);
  border: none;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  font-size: 24px;
  color: white;
  cursor: pointer;
  box-shadow: 0 2px 6px rgba(0,0,0,0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s ease, background-color 0.3s ease;
  z-index: 1060;
}

.modern-modal-close:hover {
  background-color: #b30202;
  transform: scale(1.15);
}

.event-date-time-wrapper {
  padding: 0 1rem 1rem 1rem;
  user-select: none;
  display: flex;
  justify-content: flex-end;
  align-items: flex-end; /* pastikan elemen rata bawah */
  gap: 0.75rem; /* jarak antar kotak */
}

.date-box-small {
 background: rgba(255, 255, 255, 0.15);
  border-radius: 8px;
  color: white;
  padding: 0.5rem 0.8rem; /* padding bawah ditambah agar kotak lebih tinggi */
  text-align: center;
  box-shadow: 0 0 8px rgba(0,0,0,0.2);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.date-box:hover, .time-box:hover {
  transform: scale(1.05);
  cursor: default;
  box-shadow: 0 0 20px rgba(244, 67, 54, 0.8);
}

.date-day-small {
  font-weight: 700;
  font-size: 1.5rem;
  line-height: 1;
  margin-bottom: 0;
}

.date-month-year-small {
  font-size: 0.75rem;
  text-transform: uppercase;
  opacity: 0.85;
  letter-spacing: 0.1em;
  margin-top: 0;
}

.time-box-small {
  display: flex;
  align-items: center; /* vertical tengah isi jam */
  background: rgba(255, 255, 255, 0.15);
  border-radius: 8px;
  padding: 0.5rem 0.9rem; /* padding bawah ditambah sama dengan date-box */
  box-shadow: 0 0 8px rgba(0,0,0,0.2);
  gap: 0.4rem;
}

.clock-icon-small {
  width: 18px;
  height: 18px;
  stroke: white;
  opacity: 0.8;
}

.time-text-small {
  font-weight: 600;
  font-size: 0.9rem;
  color: white;
  line-height: 1;
}

.close-icon {
  color: white;
  font-size: 24px;
  line-height: 1;
  font-weight: bold;
}

.modern-modal-image {
  width: 100%;
  max-height: 400px; /* batas tinggi modal */
  overflow-y: auto;  /* hanya scroll vertikal */
  overflow-x: hidden; /* hilangkan scroll horizontal */
  border-top-left-radius: 1.5rem;
  border-top-right-radius: 1.5rem;
  background-color: #f0f0f0;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0; /* hilangkan padding supaya foto penuh */
  box-sizing: border-box;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none;  /* Internet Explorer 10+ */
}

.modern-modal-image::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera */
}


.modern-modal-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  display: block;
}

.modern-modal-image:hover img {
  transform: scale(1.05);
}


.modern-modal-content {
  padding: 2rem 2.5rem 3rem 2.5rem;
  display: flex;
  flex-direction: column;
}

.modal-event-title {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 1rem;
  color: #b30202;
}

.modal-event-metadata {
  display: flex;
  gap: 2rem;
  margin-bottom: 1.5rem;
  color: #555;
  flex-wrap: wrap;
}

.metadata-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: 500;
}

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
  padding-left: 0.6rem;
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


/* Custom icons instead of FontAwesome */
.metadata-icon {
  width: 22px;
  height: 22px;
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
  flex-grow: 1;
  font-size: 1rem;
  line-height: 1.6;
  color: #333;
  margin-bottom: 2rem;
}

.modern-modal-actions {
  display: flex;
  justify-content: flex-end;
}

.register-btn {
  background-color: #b30202;
  color: white;
  border: none;
  padding: 0.75rem 2rem;
  border-radius: 3rem;
  font-weight: 700;
  font-size: 1.1rem;
  cursor: pointer;
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
}

.row.align-items-center {
  gap: 1rem; /* beri jarak antar kolom */
}

.register-btn:hover {
  background-color: #dd3232;
  box-shadow: 0 6px 12px rgba(179, 2, 2, 0.4);
}

.d-flex.justify-content-end.gap-3.mt-auto {
  padding: 0 1rem 1rem 1rem;
}

/* Animation delays for staggered effect */
.animate__fadeIn {
  --animate-duration: 0.8s;
}
</style>