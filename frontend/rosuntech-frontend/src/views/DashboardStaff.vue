<template>
  <div class="d-flex">
    <!-- Sidebar -->
    <Sidebar />

    <!-- Main Content -->
    <div class="content-wrapper container">
      <!-- User Info + Create Button -->
      <div class="row mb-5 d-flex align-items-center">
        <div class="col-12 col-md-4 text-center">
          <img
            :src="require(`@/assets/img/${user.photo}`)"
            class="img-fluid rounded-circle profile-photo"
            alt="User Photo"
          />
        </div>
        <div class="col-12 col-md-8 d-flex justify-content-between align-items-center">
          <div>
            <h3>{{ user.name }}</h3>
            <p><strong>Divisi:</strong> {{ user.division }}</p>
          </div>
          <router-link to="/CreateEvent" class="btn btn-danger btn-lg">Create Event</router-link>
        </div>
      </div>

      <!-- Event List -->
      <h5 class="mb-3 text-white bg-danger p-2 rounded">Current Event You Created</h5>
      <div class="row justify-content-center g-4 mb-5">
        <div class="col-12 col-md-6" v-for="(event, index) in events" :key="index">
          <div class="event-card d-flex flex-column">
            <div class="image-wrapper mb-3">
              <img :src="require(`@/assets/img/${event.image}`)" alt="Event Image" />
            </div>
            <h5 class="event-title mb-1 text-truncate">{{ event.title }}</h5>
            <a
              href="#"
              class="text-start see-more mb-3"
              @click.prevent="openModal(event)"
            >
              See more
            </a>
            <div class="d-flex justify-content-end gap-3 mt-auto">
              <span class="badge date-badge">{{ event.date }}</span>
              <span class="badge time-badge">{{ event.time }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Modal -->
      <div v-if="selectedEvent" class="modal fade show d-block" tabindex="-1" role="dialog">
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
              <p><strong>Description:</strong> {{ selectedEvent.message }}</p>
              <p><strong>Date:</strong> {{ selectedEvent.date }}</p>
              <p><strong>Time:</strong> {{ selectedEvent.time }}</p>
              <div class="d-flex justify-content-end mt-4">
                <button class="btn btn-danger me-2" @click="goToMainPage">MAIN PAGE</button>
                <button class="btn btn-danger" @click="viewParticipant">VIEW PARTICIPANT</button>
              </div>
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
  name: "DashboardStaff",
  components: { Sidebar },
  data() {
    return {
      user: {
        name: "Aprilianza Muhammad Yusup",
        division: "Divisi Kehumasan",
        photo: "profile.png"
      },
      events: [
        {
          title: "Telkommetra Mengadakan Lomba Inovasi Digital untuk Mahasiswa Seluruh Indonesia",
          date: "21 Maret 2025",
          time: "09.00 WIB",
          image: "placeholder.jpg",
          message: "Lomba inovasi digital bertema 'Smart Campus' dengan hadiah jutaan rupiah."
        },
        {
          title: "Workshop UI/UX Design: Membangun Portofolio Profesional",
          date: "05 April 2025",
          time: "13.00 WIB",
          image: "placeholder.jpg",
          message: "Pelatihan intensif desain antarmuka pengguna dengan studi kasus nyata."
        },
        {
          title: "Seminar Big Data & Analytics di Era Industri 4.0",
          date: "18 April 2025",
          time: "10.00 WIB",
          image: "placeholder.jpg",
          message: "Mendalami penerapan big data untuk pengambilan keputusan bisnis."
        }
      ],
      selectedEvent: null
    };
  },
  methods: {
    openModal(event) {
      this.selectedEvent = event;
    },
    closeModal() {
      this.selectedEvent = null;
    },
    goToMainPage() {
      this.selectedEvent = null;
    },
    viewParticipant() {
      this.$router.push("/ViewParticipants");
    }
  }
};
</script>

<style scoped>
.content-wrapper {
  flex-grow: 1;
  margin-top: 100px;
}

.profile-photo {
  width: 200px;
  height: 200px;
  object-fit: cover;
}

.event-card {
  background-color: v-bind("$colors.fourth");
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
  display: block;
}

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

.date-badge,
.time-badge {
  background-color: #ffffff;
  color: v-bind("$colors.fourth");
  border-radius: 2rem;
  padding: 0.5rem 1rem;
  font-weight: 600;
  font-size: 0.9rem;
}

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