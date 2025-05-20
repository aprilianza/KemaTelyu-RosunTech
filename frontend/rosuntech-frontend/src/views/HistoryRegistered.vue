<template>
  <div id="app" class="d-flex">
    <!-- Sidebar -->
    <Sidebar />

    <!-- Main Content -->
    <div class="content-wrapper container">
      <!-- Header Section -->

      <!-- History Events Section -->
      <div class="row justify-content-center g-4 mb-5">
        <div class="col-12 col-md-6" v-for="(event, index) in historyEvents" :key="event.id">
          <div class="event-card history-event-card d-flex flex-column animate__animated animate__fadeIn"
               :style="{'animation-delay': index * 0.1 + 's'}">
            <h5 class="event-title mb-3">{{ event.title }}</h5>
            <p class="event-date mb-2">Registration Date: {{ event.dateCreated }}</p>
            
            <!-- Status Badge -->
            <div class="d-flex align-items-center mb-3">
              <span :class="['status-badge', 
                event.status === 'Diterima' ? 'status-approved' : 'status-pending']">
                {{ event.status }}
              </span>
            </div>

            <!-- Action Buttons -->
            <div class="d-flex justify-content-end gap-3 mt-auto">
              <button
                v-if="event.status === 'Diterima'"
                class="btn action-btn download-btn "
              >
                Download
              </button>
              <button
                v-if="event.status === 'Menunggu'"
                class="btn action-btn cancel-btn"
              >
                Cancel
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

export default {
  name: 'HistoryRegisteredEvents',
  components: {
    Sidebar
  },
  data() {
    return {
      historyEvents: [
        {
          id: 1,
          title: 'Workshop UI/UX Design: Membangun Portofolio Profesional',
          dateCreated: '9 May 2023',
          status: 'Diterima'
        },
        {
          id: 2,
          title: 'Seminar Big Data & Analytics di Era Industri 4.0',
          dateCreated: '9 May 2023',
          status: 'Menunggu'
        },
        {
          id: 3,
          title: 'Telkommetra Mengadakan Lomba Inovasi Digital untuk Mahasiswa Seluruh Indonesia',
          dateCreated: '10 May 2023',
          status: 'Diterima'
        }
      ]
    };
  },
  created() {
    if (this.$route.params.event) {
      const event = this.$route.params.event;
      this.historyEvents.push(event);
    }
  },
  mounted() {
    // Make sure Animate.css is loaded
    this.loadAnimateCSS();
  },
  methods: {
    loadAnimateCSS() {
      if (!document.getElementById('animate-css')) {
        const link = document.createElement('link');
        link.id = 'animate-css';
        link.rel = 'stylesheet';
        link.href = 'https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css';
        document.head.appendChild(link);
      }
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

/* Event Card for History */
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

.history-event-card {
  min-height: 180px;
}

.event-title {
  color: #ffffff;
  font-size: 1.2rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.event-date {
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.9rem;
}

/* Status Badge */
.status-badge {
  padding: 0.5rem 1.2rem;
  border-radius: 2rem;
  font-weight: 600;
  font-size: 0.9rem;
  transition: transform 0.3s ease;
}

.status-badge:hover {
  transform: scale(1.05);
}

.status-approved {
  background-color: #28a745;
  color: white;
}

.status-pending {
  background-color: #ffc107;
  color: #212529;
}

/* Action Buttons */
.action-btn {
  border-radius: 2rem;
  padding: 0.5rem 1.2rem;
  font-weight: 600;
  font-size: 0.9rem;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.download-btn {
  background-color: #ffffff;
  color: v-bind('$colors.primary');
}

.cancel-btn {
  background-color: #ffffff;
  color: v-bind('$colors.primary');
}

/* Gap utility */
.gap-3 {
  gap: 1rem;
}

/* Animation settings */
.animate__fadeIn {
  --animate-duration: 0.8s;
}

</style>