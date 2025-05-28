<template>
  <div id="app" class="d-flex">
    <!-- Sidebar -->
    <Sidebar />

    <!-- Main Content -->
    <div class="content-wrapper container-fluid py-4">
      <!-- Header Section -->

      

      <!-- History Events Section -->
      <div class="history-wrapper">
      <div class="row justify-content-center g-4 mb-5">
       
         <div v-if="historyEvents.length === 0" class="empty-history-message">
             Belum ada data history registrasi.
        </div>

  <div v-if="historyEvents.length > 0" class="history-grid">
  <div class="col-12 col-md-6" v-for="(event, index) in historyEvents" :key="event.id">
  <div class="event-card history-event-card animate__animated animate__fadeIn" 
    :class="{
    'bg-approved': event.status === 'Diterima',
    'bg-pending': event.status === 'Menunggu',
    'bg-rejected': event.status === 'Ditolak'
  }"
    :style="{'animation-delay': index * 0.1 + 's'}">
      <h5 class="event-title mb-3">{{ event.title }}</h5>
      <p class="event-date mb-2">Tanggal Registrasi: {{ event.dateCreated }}</p>

      <!-- Status Badge -->
      <div class="d-flex align-items-center mb-3">
        <span :class="[
            'status-badge',
            event.status === 'Diterima'
              ? 'status-approved'
              : event.status === 'Menunggu'
              ? 'status-pending'
              : 'status-rejected',
          ]"
        >
          {{ event.status }}
        </span>
      </div>

      <!-- Action Buttons -->
      <div class="d-flex justify-content-end gap-3 mt-auto">
        <button
          v-if="event.status === 'Diterima' && event.certificateId"
          class="btn action-btn download-btn"
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
      historyEvents: [],
      previousRegistrations: [],
      error: null,
      loading: false,
      user: JSON.parse(localStorage.getItem('user')) || { name: 'User', nim: '', fakultas: '' },
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
    console.log('Response data:', response.data);
    console.log('Message array:', response.data?.message);
    const registrations = response.data.message || response.data || [];
    const newRegs = registrations.map(reg => {
      console.log('REG DATA:', reg);
      let statusText = '';
      if (reg.status === 'APPROVED') statusText = 'Diterima';
      else if (reg.status === 'PENDING') statusText = 'Menunggu';
      else if (reg.status === 'REJECTED') statusText = 'Ditolak';

      return {
        id: reg.eventId || reg.id || 0,                 // pakai eventId untuk id
        title: reg.eventName || reg.eventTitle || 'Tidak diketahui',           // pakai eventName untuk judul event
        description: reg.eventDesc,        // kalau perlu tampilkan deskripsi
        dateCreated: reg.registrationAt || reg.date || '',  // tanggal pendaftaran
        status: statusText,
        certificateId: reg.certificateId ?? null,
      };
    });

    // Notifikasi perubahan status
    newRegs.forEach(newReg => {
      const oldReg = this.previousRegistrations.find(e => e.id === newReg.id);
      if (oldReg && oldReg.status !== newReg.status) {
        this.showStatusPopup(newReg);
      }
    });

    this.historyEvents = newRegs;
    this.previousRegistrations = [...newRegs];
    localStorage.setItem('historyEvents', JSON.stringify(newRegs));
  } catch (error) {
    this.error = 'Gagal mengambil data registrasi.';
    console.error('Error fetchRegistrations:', error);
  } finally {
    this.loading = false;
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
  
  try {
    // Request file PDF dari backend, response bertipe blob
    const response = await api.get(`/api/certificates/${event.certificateId}/download`, {
      responseType: 'blob' // sangat penting agar respon diterima sebagai file/binary
    });

    // Buat URL sementara dari blob
    const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));

    // Buat elemen <a> untuk trigger download
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `${this.user.name}-${event.title}.pdf`); // nama file yang diunduh
    document.body.appendChild(link);
    link.click();

    // Bersihkan elemen dan URL object
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);

    this.$swal.fire({
      icon: 'success',
      title: 'Download Berhasil',
      text: 'Sertifikat sudah tersimpan di perangkat Anda.',
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
    async cancelRegistration(eventId) {
    const confirmed = await this.$swal.fire({
      icon: 'warning',
      title: 'Batalkan Pendaftaran',
      text: 'Apakah Anda yakin ingin membatalkan pendaftaran event ini?',
      showCancelButton: true,
      cancelButtonText: 'Tidak',
      confirmButtonText: 'Ya',
      
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
    
  }
  
};
</script>

<style scoped>
/* Import Animate.css */
@import 'https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css';

.content-wrapper {
  flex-grow: 1;
  margin-top: 90px;
  max-width: 900px;
  padding: 0 15px;
}

.history-wrapper {
  padding: 0;
}

.history-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(600px, max-content));
  justify-content: start;
  gap: 2rem; /* jarak antar card */
  padding: 0;
}

.history-header {
  text-align: center;
  font-weight: 700;
  font-size: 2rem;
  margin-bottom: 2rem;
  color: #b22222; /* merah gelap */
  text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.1);
}

/* Event Card for History */
.event-card {
  width: 1200px;
  display: flex !important;
  flex-direction: column !important;
  max-width: 850px !important;
  border-radius: 20px !important;
  padding: 1.7rem 1.8rem !important;
  transition: transform 0.3s ease, box-shadow 0.3s ease !important;
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

.bg-approved {
  background-color: #28a745 !important; /* hijau */
  color: white !important;
 box-shadow: 0 10px 20px rgba(40, 167, 69, 0.5) !important;
}

.bg-pending {
  background-color: #ffc107 !important; /* kuning */
  color: #212529 !important;
  box-shadow: 0 10px 20px rgba(255, 193, 7, 0.5) !important;
}

.bg-rejected {
  background-color: #dc3545 !important; /* merah */
  color: white !important;
  box-shadow: 0 10px 20px rgba(255, 7, 7, 0.57) !important;
}


.event-card:hover {
  transform: translateY(-8px) !important;
  box-shadow: 0 18px 30px rgba(0,0,0,0.2) !important;
}

.empty-history-message {
  height: 60vh;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.25rem;
  color: #6c757d;
  text-align: center;
}

.history-event-card {
  min-height: 180px;
}

.event-title {
  font-size: 1.3rem;
  font-weight: 700;
   color: inherit !important;
  margin-bottom: 0.6rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.event-date {
  font-size: 0.95rem;
  color: inherit !important;
  margin-bottom: 1rem;
  font-weight: 400;
}

/* Status Badge */
.status-container {
  gap: 10px;
}

.status-icon {
  width: 28px;
  height: 28px;
  stroke-width: 3;
  stroke-linejoin: round;
  stroke-linecap: round;
}

.status-icon.approved {
  stroke: #28a745;
}

.status-icon.pending {
  stroke: #ffc107;
}

.status-rejected {
  background-color: #f87171; /* merah */
  color: white;
}

.status-badge {
   padding: 0.5rem 1.3rem;
  border-radius: 2rem;
  font-weight: 700;
  font-size: 1rem;
  text-transform: uppercase;
  user-select: none;
  transition: background-color 0.3s ease, color 0.3s ease;
  box-shadow: 0 2px 5px rgba(0,0,0,0.15);
  color: white;
}

.status-badge:hover {
  filter: brightness(1.15);
}

.status-approved {
  background-color: #1b5e20;
}

.status-pending {
 background-color: #c49000;
  color: white;
}

.status-rejected {
  background-color: #9f1a28; /* merah */
  color: white;
}

/* Action Buttons */
.action-btn {
  border-radius: 2rem;
  padding: 0.5rem 1.6rem;
  font-weight: 700;
  font-size: 0.95rem;
  border: 1.5px solid;
  background: white;
  box-shadow: 0 4px 8px rgba(0,0,0,0.25);
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: background-color 0.3s ease, box-shadow 0.3s ease, color 0.3s ease;
}

.action-btn-approved {
  color: #1b4d20;
  background-color: #d4edda;
  border-color: #1b4d20;
  box-shadow: 0 4px 10px rgba(27, 77, 32, 0.25);
}

.action-btn-approved:hover {
  background-color: #1b4d20;
  color: white;
  box-shadow: 0 6px 18px rgba(27, 77, 32, 0.7);
  border-color: #144d14;
}

.action-btn-pending {
  color: #b28700;
  background-color: #fff3cd;
  border-color: #b28700;
  box-shadow: 0 4px 10px rgba(178, 135, 0, 0.25);
}
.action-btn-pending:hover {
  background-color: #b28700;
  color: white;
  box-shadow: 0 6px 18px rgba(178, 135, 0, 0.7);
  border-color: #8c6700;
}

.action-btn-rejected {
  color: #7b121f;
  background-color: #f8d7da;
  border-color: #7b121f;
  box-shadow: 0 4px 10px rgba(123, 18, 31, 0.25);
}
.action-btn-rejected:hover {
  background-color: #7b121f;
  color: white;
  box-shadow: 0 6px 18px rgba(123, 18, 31, 0.7);
  border-color: #5e0c16;
}

.action-btn i {
  font-size: 1.1rem;
}

.action-btn:hover {
  color: white;
  box-shadow: 0 6px 16px rgba(0,0,0,0.3);
}

.bg-approved .action-btn:hover {
  background-color: #1b4d20; /* hijau gelap */
  border-color: #144d14;
  box-shadow: 0 6px 18px rgba(27, 77, 32, 0.7);
}

/* Hover for pending (yellow background card) */
.bg-pending .action-btn:hover {
  background-color: #b28700; /* kuning gelap */
  border-color: #8c6700;
  box-shadow: 0 6px 18px rgba(178, 135, 0, 0.7);
}

/* Hover for rejected (red background card) */
.bg-rejected .action-btn:hover {
  background-color: #7b121f; /* merah gelap */
  border-color: #5e0c16;
  box-shadow: 0 6px 18px rgba(123, 18, 31, 0.7);
}

.bg-approved:hover {
  box-shadow: 0 18px 40px rgba(40, 167, 69, 0.7), 0 18px 18px rgba(0,0,0,0.2) !important;
}

.bg-pending:hover {
  box-shadow: 0 18px 40px rgba(255, 193, 7, 0.7), 0 18px 18px rgba(0,0,0,0.2) !important;
}

.bg-rejected:hover {
  box-shadow: 0 18px 40px rgba(220, 53, 69, 0.7), 0 18px 18px rgba(0,0,0,0.2) !important;
}

/* Gap utility */
.gap-3 {
  gap: 10rem;
}

/* Animation settings */
.animate__fadeIn {
  --animate-duration: 0.8s;
}

</style>