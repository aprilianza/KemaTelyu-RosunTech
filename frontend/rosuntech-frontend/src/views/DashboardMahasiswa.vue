<template>
  <div id="app" class="d-flex">
    <!-- Sidebar -->
    <Sidebar />

    <!-- Main Content -->
    <div class="content-wrapper container">
      <!-- User Profile, Total Event, and History Section -->
      <div class="row mb-5 animate__animated animate__fadeIn">
        <!-- User Profile Section -->
        <div class="col-lg-6 col-md-12 mb-3">
          <div class="card user-profile-card h-100 animate__animated animate__fadeInLeft">
            <div class="card-body">
              <div class="row align-items-center">
                <div class="col-auto">
                  <img :src="require(`@/assets/img/${user.photo || 'profile.png'}`)"
                    class="profile-photo rounded-circle" alt="Profile Photo" />
                </div>
                <div class="col text-start">
                  <h3>{{ user.name }}</h3>
                  <p><strong>NIM:</strong> {{ user.nim }}</p>
                  <p><strong>Fakultas:</strong> {{ user.fakultas }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Total Event Card -->
        <div class="col-lg-3 col-md-6 mb-3">
          <div class="card total-event-card h-100 animate__animated animate__fadeInRight">
            <div class="card-body text-center d-flex flex-column justify-content-center">
              <h1 class="total-events">{{ totalEvents }}</h1>
              <h5>Total Events</h5>
            </div>
          </div>
        </div>

        <!-- History Card -->
        <div class="col-lg-3 col-md-6 mb-3" @click="$router.push({ name: 'History' })">
          <div class="card history-card h-100 animate__animated animate__fadeInRight">
            <div class="card-body d-flex flex-column align-items-center justify-content-center">
              <img src="@/assets/img/history.png" alt="History" class="history-icon" />
              <h5 class="mt-3">History</h5>
            </div>
          </div>
        </div>
      </div>

      <!-- Events Section -->
      <h2 class="mb-4 animate__animated animate__fadeIn">Events</h2>
      <div class="row justify-content-center g-4 mb-5">
        <div class="col-12 col-md-6" v-for="(event, index) in events" :key="event.id">
          <div class="event-card d-flex flex-column animate__animated animate__fadeIn" 
               :style="{'animation-delay': index * 0.1 + 's'}">
            <div class="image-wrapper mb-3 hover-zoom">
              <img :src="require(`@/assets/img/${event.image}`)" alt="Event Image" />
            </div>
            <h5 class="event-title mb-1 text-truncate">{{ event.title }}</h5>
            <a href="#" class="text-start see-more mb-3" @click.prevent="openModal(event)">
              lihat detail
            </a>
            <div class="d-flex justify-content-end gap-3 mt-auto">
              <span class="badge date-badge">{{ event.date }}</span>
              <span class="badge time-badge">{{ event.time }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Modern Event Detail Modal -->
      <div v-if="selectedEvent" class="modern-modal-container animate__animated animate__fadeIn">
        <div class="modern-modal-backdrop" @click="closeModal"></div>
        <div class="modern-modal animate__animated animate__zoomIn">
          <button type="button" class="modern-modal-close" @click="closeModal">
            <span class="close-icon">&times;</span>
          </button>
          
          <div class="modern-modal-image">
            <img :src="require(`@/assets/img/${selectedEvent.image}`)" alt="Event Image" />
          </div>
          
          <div class="modern-modal-content">
            <h2 class="modal-event-title">{{ selectedEvent.title }}</h2>
            
            <div class="modal-event-metadata">
              <div class="metadata-item">
                <span class="metadata-icon calendar-icon"></span>
                <span>{{ selectedEvent.date }}</span>
              </div>
              <div class="metadata-item">
                <span class="metadata-icon clock-icon"></span>
                <span>{{ selectedEvent.time }}</span>
              </div>
              <div class="metadata-item">
                <span class="metadata-icon users-icon"></span>
                <span>{{ selectedEvent.createdBy }}</span>
              </div>
            </div>
            
            <div class="modal-event-description">
              <p>{{ selectedEvent.description }}</p>
            </div>
            
            <div class="modern-modal-actions">
              <button type="button" class="btn register-btn">
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

export default {
  name: 'DashboardMahasiswa',
  components: { Sidebar },
  data() {
    return {
      user: { name: '', nim: '', fakultas: '', photo: '' }, // data user di-load dari backend
      events: [
        {
          id: 1,
          title: 'Telkommetra Mengadakan Lomba Inovasi Digital untuk Mahasiswa Seluruh Indonesia',
          image: 'placeholder.jpg',
          description: "Lomba inovasi digital bertema 'Smart Campus' dengan hadiah jutaan rupiah.",
          date: '21 Maret 2025',
          time: '09.00 WIB',
          createdBy: 'Fakultas Ilmu Komputer'
        },
        {
          id: 2,
          title: 'Workshop UI/UX Design: Membangun Portofolio Profesional',
          image: 'placeholder.jpg',
          description: 'Pelatihan intensif desain antarmuka pengguna dengan studi kasus nyata.',
          date: '05 April 2025',
          time: '13.00 WIB',
          createdBy: 'Prodi Desain Digital'
        },
        {
          id: 3,
          title: 'Seminar Big Data & Analytics di Era Industri 4.0',
          image: 'placeholder.jpg',
          description: 'Mendalami penerapan big data untuk pengambilan keputusan bisnis.',
          date: '18 April 2025',
          time: '10.00 WIB',
          createdBy: 'Himpunan Mahasiswa TI'
        }
      ],
      selectedEvent: null,
    };
  },
  computed: {
    totalEvents() {
      return this.events.length;
    },
  },
  mounted() {
    // ⬇️ Redirect ke /login kalau token tidak ada
    const token = localStorage.getItem('token');
    if (!token) {
      this.$router.push('/');
      return;
    }

    // Lanjut proses normal
    this.fetchCurrentUser();
    this.loadFontAwesome();
  },
  methods: {
    loadFontAwesome() {
      // If you're not already loading FontAwesome elsewhere, add this method
      if (!document.getElementById('font-awesome-css')) {
        const link = document.createElement('link');
        link.id = 'font-awesome-css';
        link.rel = 'stylesheet';
        link.href = 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css';
        document.head.appendChild(link);
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
    openModal(event) {
      this.selectedEvent = event;
      document.body.style.overflow = 'hidden'; // Prevent scrolling when modal is open
    },
    closeModal() {
      this.selectedEvent = null;
      document.body.style.overflow = ''; // Re-enable scrolling
    }
  }
};
</script>

<style scoped>
/* Import Animate.css */
@import 'https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css';

.content-wrapper {
  flex-grow: 1;
  margin-top: 100px;
}

/* Profile */
.profile-photo {
  width: 150px;
  height: 150px;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.profile-photo:hover {
  transform: scale(1.05);
}

/* Card for User Profile */
.user-profile-card {
  background-color: #ffffff;
  border-radius: 1rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.user-profile-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

/* History & Total Event Card */
.history-card,
.total-event-card {
  background-color: v-bind('$colors.primary');
  color: white;
  border-radius: 1rem;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.history-card:hover,
.total-event-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
}

.total-event-card {
  cursor: default;
}

.history-card .card-body,
.total-event-card .card-body {
  text-align: center;
}

.history-icon {
  width: 170px;
  height: 140px;
  transition: transform 0.3s ease;
}

.history-card:hover .history-icon {
  transform: scale(1.1);
}

h5 {
  font-size: 1.2rem;
}

h1 {
  font-size: 125px;
}

/* Event Card */
.event-card {
  background-color: v-bind('$colors.fourth');
  border-radius: 1.5rem;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  height: 100%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  animation: popupEffect 0.6s ease-out forwards;
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
  transform: translateY(-7px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15);
}

.image-wrapper {
  width: 100%;
  overflow: hidden;
  border-radius: 1rem;
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
  color: #ffffff;
}

.see-more {
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.9rem;
  text-decoration: none;
  text-align: center;
  transition: color 0.3s ease;
}

.see-more:hover {
  color: #ffffff;
  text-decoration: underline;
}

.date-badge,
.time-badge {
  background-color: #ffffff;
  color: v-bind('$colors.primary');
  border-radius: 2rem;
  padding: 0.5rem 1rem;
  font-weight: 600;
  font-size: 0.9rem;
  transition: transform 0.3s ease;
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
}

.modern-modal-image img {
  width: 100%;
  height: auto;
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
  color: #666;
}

.metadata-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

/* Custom icons instead of FontAwesome */
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
  margin-bottom: 2rem;
  line-height: 1.6;
  color: #555;
}

.modern-modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 2rem;
}

.register-btn {
  background-color: v-bind('$colors.primary');
  color: white;
  border: none;
  padding: 0.5rem 1.5rem;
  border-radius: 2rem;
  font-weight: 600;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: v-bind('$colors.primary');
  opacity: 0.9;
}

/* Animation delays for staggered effect */
.animate__fadeIn {
  --animate-duration: 0.8s;
}
</style>