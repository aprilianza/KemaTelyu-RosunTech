<template>
  <div id="app" class="d-flex">
    <!-- Sidebar -->
    <Sidebar />

    <!-- Main Content -->
    <div class="content-wrapper container">
      <!-- User Profile, Total Event, and History Section -->
      <div class="row mb-5">
        <!-- User Profile Section -->
        <div class="col-lg-6 col-md-12 mb-3">
          <div class="card user-profile-card h-100">
            <div class="card-body">
              <div class="row align-items-center">
                <div class="col-auto">
                  <img :src="require(`@/assets/img/${user.photo}`)" class="profile-photo rounded-circle" alt="Profile Photo" />
                </div>
                <div class="col text-start">
                  <h3>{{ user.name }}</h3>
                  <p><strong>NIM:</strong> {{ user.nim }}</p>
                  <p><strong>Fakultas:</strong> {{ user.faculty }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Total Event Card -->
        <div class="col-lg-3 col-md-6 mb-3">
          <div class="card total-event-card h-100">
            <div class="card-body text-center d-flex flex-column justify-content-center">
              <h1 class="total-events">{{ totalEvents }}</h1>
              <h5>Total Events</h5>
            </div>
          </div>
        </div>

        <!-- History Card -->
        <div class="col-lg-3 col-md-6 mb-3">
          <div class="card history-card h-100" @click="$router.push({ name: 'History' })">
            <div class="card-body text-center d-flex flex-column justify-content-center">
              <i class="bi bi-clock history-icon fs-1"><img src="@/assets/img/history.png" alt="" /></i>
              <h5 class="mt-2">History</h5>
            </div>
          </div>
        </div>
      </div>

      <!-- Events Section -->
      <h4 class="mb-4">Events</h4>
      <div class="row justify-content-center g-4 mb-5">
        <div class="col-12 col-md-6" v-for="event in events" :key="event.id">
          <div class="event-card d-flex flex-column">
            <!-- Foto Event -->
            <div class="image-wrapper mb-3">
              <img :src="require(`@/assets/img/${event.image}`)" alt="Event Image" />
            </div>
            <!-- Judul & See more -->
            <h5 class="event-title mb-1 text-truncate">{{ event.title }}</h5>
            <a href="#" class="text-start see-more mb-3" @click.prevent="openModal(event)"> See more </a>
            <!-- Tanggal & Waktu -->
            <div class="d-flex justify-content-end gap-3 mt-auto">
              <span class="badge date-badge">{{ event.date }}</span>
              <span class="badge time-badge">{{ event.time }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Event Detail Modal -->
      <div v-if="selectedEvent" class="modal fade show d-block" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
          <div class="modal-content">
            <div class="modal-header text-white">
              <h5 class="modal-title">{{ selectedEvent.title }}</h5>
              <button type="button" class="btn-close btn-close-white" @click="closeModal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
              <img :src="require(`@/assets/img/${selectedEvent.image}`)" class="img-fluid mb-4 rounded" alt="Event Image" />
              <p><strong>Description:</strong> {{ selectedEvent.description }}</p>
              <p><strong>Date:</strong> {{ selectedEvent.date }}</p>
              <p><strong>Time:</strong> {{ selectedEvent.time }}</p>
              <p><strong>Created by:</strong> {{ selectedEvent.createdBy }}</p>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="closeModal">Close</button>
              <button type="button" class="btn btn-success">Register</button>
            </div>
          </div>
        </div>
      </div>
      <div v-if="selectedEvent" class="modal-backdrop fade show"></div>
    </div>
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue';

export default {
  name: 'DashboardMahasiswa',
  components: { Sidebar },
  data() {
    return {
      user: {
        name: 'Rahmat Widodo',
        nim: '123456789',
        faculty: 'Ilmu Komputer',
        photo: 'profile.png',
      },
      events: [
        {
          id: 1,
          title: 'Telkommetra Mengadakan Lomba Inovasi Digital untuk Mahasiswa Seluruh Indonesia',
          image: 'placeholder.jpg', 
          description: "Lomba inovasi digital bertema 'Smart Campus' dengan hadiah jutaan rupiah.",
          date: '21 Maret 2025',
          time: '09.00 WIB',
          createdBy: 'Fakultas Ilmu Komputer',
        },
        {
          id: 2,
          title: 'Workshop UI/UX Design: Membangun Portofolio Profesional',
          image: 'placeholder.jpg',
          description: 'Pelatihan intensif desain antarmuka pengguna dengan studi kasus nyata.',
          date: '05 April 2025',
          time: '13.00 WIB',
          createdBy: 'Prodi Desain Digital',
        },
        {
          id: 3,
          title: 'Seminar Big Data & Analytics di Era Industri 4.0',
          image: 'placeholder.jpg',
          description: 'Mendalami penerapan big data untuk pengambilan keputusan bisnis.',
          date: '18 April 2025',
          time: '10.00 WIB',
          createdBy: 'Himpunan Mahasiswa TI',
        },
      ],
      selectedEvent: null,
    };
  },
  computed: {
    totalEvents() {
      // Menghitung jumlah event secara dinamis
      return this.events.length;
    },
  },
  methods: {
    openModal(event) {
      this.selectedEvent = event;
    },
    closeModal() {
      this.selectedEvent = null;
    },
  },
};
</script>

<style scoped>
.content-wrapper {
  flex-grow: 1;
  margin-top: 100px;
}

/* Profile */
.profile-photo {
  width: 150px;
  height: 150px;
  object-fit: cover;
}

/* Card for User Profile */
.user-profile-card {
  background-color: #ffffff;
  border-radius: 1rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

/* History & Total Event Card */
.history-card,
.total-event-card {
  background-color: v-bind('$colors.primary'); /* Example primary color */
  color: white;
  border-radius: 1rem;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

.total-event-card {
  cursor: default;
}

.history-card .card-body,
.total-event-card .card-body {
  text-align: center;
}

.history-icon,
.total-event-icon {
  font-size: 3rem;
}

h5 {
  font-size: 1.2rem;
}
h1{
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
}

.image-wrapper {
  width: 100%;
  overflow: hidden;
  border-radius: 1rem;
}
.image-wrapper img {
  width: 100%;
  height: 180px;
  object-fit: cover;
}

.event-title {
  color: #ffffff;
}

.see-more {
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.9rem;
  text-decoration: none;
  text-align: center;
}

.date-badge,
.time-badge {
  background-color: #ffffff;
  color: v-bind('$colors.primary');
  border-radius: 2rem;
  padding: 0.5rem 1rem;
  font-weight: 600;
  font-size: 0.9rem;
}

.modal-header {
  background-color: v-bind('$colors.primary');
}
</style>