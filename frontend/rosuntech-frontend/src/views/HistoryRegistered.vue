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
            <p class="event-date mb-2">Tanggal Registrasi: {{ event.dateCreated }}</p>
            
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
                 @click="downloadCertificate(event)"
              >
                Download
              </button>
              <button
                v-if="event.status === 'Menunggu'"
                class="btn action-btn cancel-btn"
                @click="cancelRegistration(event.id)"
              >
                Batal
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
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';

export default {
  name: 'HistoryRegisteredEvents',
  components: {
    Sidebar
  },
  data() {
    return {
      historyEvents: [],
      
    };
  },
  created() {
    const savedHistory = localStorage.getItem('historyEvents');
    if (savedHistory) {
      try {
        this.historyEvents = JSON.parse(savedHistory);
      } catch {
        this.historyEvents = [];
      }
    }

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

    saveHistoryToLocalStorage() {
    localStorage.setItem('historyEvents', JSON.stringify(this.historyEvents));
  },

     loadAnimateCSS() {
      if (!document.getElementById('animate-css')) {
        const link = document.createElement('link');
        link.id = 'animate-css';
        link.rel = 'stylesheet';
        link.href = 'https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css';
        document.head.appendChild(link);
      }
    },
    async downloadCertificate(event) {
      try {
        // Misalnya ambil data sertifikat dari backend
        const response = await api.get(`/certificates/${event.id}`);
        const certificateData = response.data;

        // Buat elemen sertifikat yang tersembunyi untuk dijadikan canvas
        const container = document.createElement('div');
        container.style.width = '800px';
        container.style.padding = '20px';
        container.style.backgroundColor = '#fff';

        // Contoh layout sertifikat sederhana (bisa kustom sesuai kebutuhan)
        container.innerHTML = `
          <div style="text-align: center; font-family: Arial, sans-serif;">
            <h1>SERTIFIKAT</h1>
            <p>Dengan bangga diberikan kepada:</p>
            <h2>${certificateData.name}</h2>
            <p>Untuk partisipasi dalam event:</p>
            <h3>${certificateData.eventTitle}</h3>
            <p>Tanggal: ${certificateData.eventDate}</p>
            <img src="${certificateData.photoUrl}" style="width: 200px; margin-top: 20px;" />
            <p>Telkom University</p>
          </div>
        `;

        document.body.appendChild(container);

        // Generate canvas dari elemen
        const canvas = await html2canvas(container, { scale: 2 });
        const imgData = canvas.toDataURL('image/png');

        // Buat PDF
        const pdf = new jsPDF({
          orientation: 'landscape',
          unit: 'px',
          format: [canvas.width, canvas.height]
        });

        pdf.addImage(imgData, 'PNG', 0, 0, canvas.width, canvas.height);

        // Download PDF
        pdf.save(`sertifikat_${certificateData.name}.pdf`);

        // Hapus elemen sementara
        document.body.removeChild(container);

      } catch (error) {
        console.error('Gagal download sertifikat:', error);
        this.$swal.fire({
          icon: 'error',
          title: 'Gagal Download',
          text: 'Tidak dapat mengunduh sertifikat saat ini. Silakan coba lagi.',
          confirmButtonText: 'OK'
        });
      }
    },
    cancelRegistration(eventId) {
      // Hapus event dengan id = eventId yang statusnya 'Menunggu'
      this.historyEvents = this.historyEvents.filter(
        event => !(event.id === eventId && event.status === 'Menunggu')
      );
      this.saveHistoryToLocalStorage();
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