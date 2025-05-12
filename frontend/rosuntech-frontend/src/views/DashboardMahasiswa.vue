<template>
  <div class="d-flex">
    <!-- Sidebar -->
    <Sidebar />

    <!-- Main Content -->
    <div class="content-wrapper container">
      <!-- User Profile -->
      <div class="row mb-5 d-flex align-items-center">
        <div class="col-12 col-md-4 text-center">
          <img
            :src="require(`@/assets/img/${user.photo}`)"
            class="img-fluid rounded-circle profile-photo"
            alt="User Photo"
          />
        </div>
        <div class="col-12 col-md-8">
          <h3>{{ user.name }}</h3>
          <p><strong>NIM:</strong> {{ user.nim }}</p>
          <p><strong>Fakultas:</strong> {{ user.faculty }}</p>
          <button class="btn btn-primary" @click="$router.push({ name: 'History' })">
            Get Certificates
          </button>
        </div>
      </div>

      <!-- Events Section -->
      <h4 class=" mb-4">Events</h4>
      <div class="row justify-content-center g-4 mb-5">
        <div class="col-12 col-md-6" v-for="event in events" :key="event.id">
          <div class="event-card d-flex flex-column">
            <!-- Foto Event -->
            <div class="image-wrapper mb-3">
              <img
                :src="require(`@/assets/img/${event.image}`)"
                alt="Event Image"
              />
            </div>
            <!-- Judul & See more -->
            <h5 class="event-title mb-1 text-truncate">{{ event.title }}</h5>
            <a href="#" class="text-start see-more mb-3" @click.prevent="openModal(event)">
              See more
            </a>
            <!-- Tanggal & Waktu -->
            <div class="d-flex justify-content-end gap-3 mt-auto">
              <span class="badge date-badge">{{ event.date }}</span>
              <span class="badge time-badge">{{ event.time }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Event Detail Modal -->
      <div
        v-if="selectedEvent"
        class="modal fade show d-block"
        tabindex="-1"
        role="dialog"
      >
        <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
          <div class="modal-content">
            <div class="modal-header text-white">
              <h5 class="modal-title">{{ selectedEvent.title }}</h5>
              <button
                type="button"
                class="btn-close btn-close-white"
                @click="closeModal"
                aria-label="Close"
              ></button>
            </div>
            <div class="modal-body">
              <img
                :src="require(`@/assets/img/${selectedEvent.image}`)"
                class="img-fluid mb-4 rounded"
                alt="Event Image"
              />
              <p><strong>Description:</strong> {{ selectedEvent.description }}</p>
              <p><strong>Date:</strong> {{ selectedEvent.date }}</p>
              <p><strong>Time:</strong> {{ selectedEvent.time }}</p>
              <p><strong>Created by:</strong> {{ selectedEvent.createdBy }}</p>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="closeModal">
                Close
              </button>
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
import Sidebar from "@/components/Sidebar.vue";

export default {
  name: "DashboardMahasiswa",
  components: { Sidebar },
  data() {
    return {
      user: {
        name: "Rahmat Widodo",
        nim: "123456789",
        faculty: "Ilmu Komputer",
        photo: "profile.png", 
      },
      events: [
        {
          id: 1,
          title:
            "Telkommetra Mengadakan Lomba Inovasi Digital untuk Mahasiswa Seluruh Indonesia",
          image: "placeholder.jpg",     // taruh dummy image di /src/assets/img/
          description:
            "Lomba inovasi digital bertema 'Smart Campus' dengan hadiah jutaan rupiah.",
          date: "21 Maret 2025",
          time: "09.00 WIB",
          createdBy: "Fakultas Ilmu Komputer",
        },
        {
          id: 2,
          title: "Workshop UI/UX Design: Membangun Portofolio Profesional",
          image: "placeholder.jpg",
          description:
            "Pelatihan intensif desain antarmuka pengguna dengan studi kasus nyata.",
          date: "05 April 2025",
          time: "13.00 WIB",
          createdBy: "Prodi Desain Digital",
        },
        {
          id: 3,
          title: "Seminar Big Data & Analytics di Era Industri 4.0",
          image: "placeholder.jpg",
          description:
            "Mendalami penerapan big data untuk pengambilan keputusan bisnis.",
          date: "18 April 2025",
          time: "10.00 WIB",
          createdBy: "Himpunan Mahasiswa TI",
        },
      ],
      selectedEvent: null,
    };
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
  width: 200px;
  height: 200px;
  object-fit: cover;
}

/* Event Card */
.event-card {
  background-color: v-bind("$colors.fourth");
  border-radius: 1.5rem;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  height: 100%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* Image wrapper keeps margin around the img */
.image-wrapper {
  width: 100%;
  overflow: hidden;
  border-radius: 1rem;
}
.image-wrapper img {
  width: 100%;
  height: 180px;
  object-fit: cover;
  display: block;
}

/* Title & See More */
.event-title {
  color: #ffffff;
  font-size: 1.125rem;
}
.see-more {
  color: rgba(255, 255, 255, 0.85);
  font-size: 0.9rem;
  text-decoration: none;
  text-align: center;
}

/* Badges */
.date-badge,
.time-badge {
  background-color: #ffffff;
  color: v-bind("$colors.fourth");
  border-radius: 2rem;
  padding: 0.5rem 1rem;
  font-weight: 600;
  font-size: 0.9rem;
}

/* Modal tweaks */
.modal-header {
  border-bottom: none;
  border-top-left-radius: 0.5rem;
  border-top-right-radius: 0.5rem;
  background-color: v-bind("$colors.primary");
}
.modal-content {
  border-radius: 0.75rem;
}
.modal-body p {
  margin-bottom: 1rem;
}
.modal-footer {
  border-top: none;
  justify-content: space-between;
}
</style>
