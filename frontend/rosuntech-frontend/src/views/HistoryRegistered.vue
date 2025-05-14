<template>
  <div id="app" class="d-flex">
    <!-- Sidebar -->
    <Sidebar />

    <!-- Main Content -->
    <div class="content-wrapper container">
      <!-- Header Section -->


      <!-- History Events Section -->
      <div class="row justify-content-center g-4 mb-5">
        <div class="col-12 col-md-6" v-for="event in historyEvents" :key="event.id">
          <div class="event-card history-event-card d-flex flex-column">
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
                class="btn action-btn download-btn"
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
  }
};
</script>

<style scoped>
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
</style>