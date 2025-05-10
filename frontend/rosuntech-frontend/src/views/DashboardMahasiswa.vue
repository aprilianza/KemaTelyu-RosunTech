<template>
  <div class="container d-flex">
    <!-- Sidebar -->
    <Sidebar />

    <!-- Content -->
    <div class="content-wrapper container">
      <!-- User Profile Section -->
      <div class="row mb-4 d-flex justify-content-between align-items-center">
        <div class="col-12 col-md-4">
          <img
            :src="require(`@/assets/img/${user.photo}`)"
            class="img-fluid rounded-circle"
            alt="User Photo"
            width="200"
            height="200"
          />
        </div>
        <div class="col-12 col-md-8">
          <h3>{{ user.name }}</h3>
          <p><strong>NIM:</strong> {{ user.nim }}</p>
          <p><strong>Fakultas:</strong> {{ user.faculty }}</p>
          <!-- Tombol Get Certificate diarahkan ke halaman history -->
          <button
            class="btn btn-primary"
            @click="$router.push({ name: 'History' })"
          >
            Get Certificate
          </button>
        </div>
      </div>

      <!-- Popular Events Section -->
      <h4>Events</h4>
      <div class="row justify-content-center">
        <div
          class="col-12 col-md-6 mb-3"
          v-for="event in events"
          :key="event.id"
        >
          <div class="card">
            <img
              :src="event.image"
              class="card-img-top"
              alt="Event Image"
            />
            <div class="card-body">
              <h5 class="card-title">{{ event.title }}</h5>
              <button
                class="btn btn-info"
                @click="openModal(event)"
              >
                See Details
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Modal -->
      <div
        v-if="selectedEvent"
        class="modal fade show"
        style="display: block;"
        tabindex="-1"
        role="dialog"
      >
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">{{ selectedEvent.title }}</h5>
              <button
                type="button"
                class="btn-close"
                @click="closeModal"
                aria-label="Close"
              ></button>
            </div>
            <div class="modal-body">
              <!-- Tampilkan gambar event -->
              <img
                :src="selectedEvent.image"
                class="img-fluid mb-3 rounded"
                alt="Event Image"
              />

              <p><strong>Description:</strong> {{ selectedEvent.description }}</p>
              <p><strong>Date:</strong> {{ selectedEvent.date }}</p>
              <p><strong>Created by:</strong> {{ selectedEvent.createdBy }}</p>
            </div>
            <div class="modal-footer">
              <button
                type="button"
                class="btn btn-secondary"
                @click="closeModal"
              >
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
  name: 'DashboardMahasiswa',
  components: {
    Sidebar
  },
  data() {
    return {
      user: {
        name: 'Rahmat Widodo',
        nim: '123456789',
        faculty: 'Ilmu Komputer',
        photo: 'Screenshot 2025-05-09 at 14.38.06.png'
      },
      events: [
        {
          id: 1,
          title: 'Seminar Nasional Teknologi',
          image: 'https://via.placeholder.com/400x200 ',
          description:
            'Pembahasan teknologi terbaru di bidang AI dan Machine Learning.',
          date: '2025-06-15',
          createdBy: 'Fakultas Ilmu Komputer'
        },
        {
          id: 2,
          title: 'Workshop UI/UX Design',
          image: 'https://via.placeholder.com/400x200 ',
          description:
            'Pelatihan mendesain antarmuka pengguna yang responsif dan menarik.',
          date: '2025-07-01',
          createdBy: 'Prodi Desain Digital'
        }
      ],
      selectedEvent: null // Untuk menyimpan event yang dipilih
    };
  },
  methods: {
    openModal(event) {
      this.selectedEvent = event;
    },
    closeModal() {
      this.selectedEvent = null;
    }
  }
};
</script>

<style scoped>
.content-wrapper {
  flex-grow: 1;
  margin-top: 100px;
}

/* Center heading */
h4.text-center {
  text-align: center;
}

/* Make cards centered too */
.row.justify-content-center {
  display: flex;
  justify-content: center;
}

.card-img-top {
  height: 200px;
  object-fit: cover;
}

.modal-backdrop {
  z-index: 1040;
}
</style>