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
              event.status === 'Diterima' ? 'status-approved' : 
              event.status === 'Menunggu' ? 'status-pending' : 'status-rejected']">
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
import { getMyRegistrations } from '@/api/registration';

export default {
  name: 'HistoryRegisteredEvents',
  components: {
    Sidebar
  },
  data() {
    return {
      registrations: [],
      previousRegistrations: [],
      error: null,
      loading: false,
    };
  },
  created() {
    this.loadFromLocalStorage();
  },
  mounted() {
    this.loadAnimateCSS();
    this.fetchRegistrations();
    this.pollingInterval = setInterval(() => {
      this.fetchRegistrations();
    }, 30000);
  },

  beforeUnmount() {
    clearInterval(this.pollingInterval);
  },

  methods: {
     loadFromLocalStorage() {
      const saved = localStorage.getItem('historyEvents');
      if (saved) {
        try {
          this.registrations = JSON.parse(saved);
          this.previousRegistrations = [...this.registrations];
        } catch {
          this.registrations = [];
          this.previousRegistrations = [];
        }
      }
    },
    async fetchHistoryEvents() {
  try {
    const response = await getMyRegistrations();
    const newEvents = response.data.message.map(reg => {
      let statusText = '';
      if (reg.status === 'APPROVED') statusText = 'Diterima';
      else if (reg.status === 'PENDING') statusText = 'Menunggu';
      else if (reg.status === 'REJECTED') statusText = 'Ditolak';

      return {
        id: reg.id,
        title: reg.eventTitle,
        dateCreated: reg.date,
        status: statusText,
      };
    });

    // ...set state dan localStorage seperti biasa
    this.historyEvents = newEvents;
    this.previousEvents = [...newEvents];
    localStorage.setItem('historyEvents', JSON.stringify(this.historyEvents));

  } catch (error) {
    console.error('Gagal fetch history registrasi:', error);
    // fallback handling (misal localStorage atau dummy data)
  }
},
    async fetchRegistrations() {
  this.loading = true;
  this.error = null;
  try {
    const response = await getMyRegistrations(); // panggil API dari registration.js
    const newRegs = response.data.message.map(reg => {
      let statusText = '';
      if (reg.status === 'APPROVED') statusText = 'Diterima';
      else if (reg.status === 'PENDING') statusText = 'Menunggu';
      else if (reg.status === 'REJECTED') statusText = 'Ditolak';

      return {
        id: reg.id,
        title: reg.eventTitle,
        dateCreated: reg.date,
        status: statusText,
      };
    });

    // Notifikasi perubahan status
    newRegs.forEach(newReg => {
      const oldReg = this.previousRegistrations.find(e => e.id === newReg.id);
      if (oldReg && oldReg.status !== newReg.status) {
        this.showStatusPopup(newReg);
      }
    });

    this.registrations = newRegs;
    this.previousRegistrations = [...newRegs];
    localStorage.setItem('historyEvents', JSON.stringify(newRegs));
  } catch (error) {
    this.error = 'Gagal mengambil data registrasi.';
    console.error('Error fetchRegistrations:', error);
  }
},

    showStatusPopup(event) {
    this.$swal.fire({
      icon: event.status === 'Diterima' ? 'success' :
          event.status === 'Menunggu' ? 'info' : 'error',
      title: `Status Anda: ${event.status}`,
      text: `Status registrasi untuk "${event.title}" sekarang adalah "${event.status}".`,
      confirmButtonText: 'OK'
    });
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
  console.log('Download certificate called for event id:', event.id);
  try {
    // Request file PDF dari backend, response bertipe blob
    const response = await api.get(`/certificates/${event.id}/download`, {
      responseType: 'blob' // sangat penting agar respon diterima sebagai file/binary
    });

    // Buat URL sementara dari blob
    const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));

    // Buat elemen <a> untuk trigger download
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `sertifikat-${event.id}.pdf`); // nama file yang diunduh
    document.body.appendChild(link);
    link.click();

    // Bersihkan elemen dan URL object
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);

    this.$swal.fire({
      icon: 'success',
      title: 'Download Berhasil',
      text: 'Sertifikat sudah tersimpan di direktori perangkat Anda.',
      confirmButtonText: 'OK'
    });

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
      this.registrations = this.registrations.filter(
        event => !(event.id === eventId && event.status === 'Menunggu')
      );
      localStorage.setItem('historyEvents', JSON.stringify(this.registrations));
    },
    
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

.status-rejected {
  background-color: #dc3545; /* merah */
  color: white;
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