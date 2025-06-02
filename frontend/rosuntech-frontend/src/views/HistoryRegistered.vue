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
  <div class="col-20" v-for="(event, index) in historyEvents" :key="event.id">
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
import { getMyRegistrations, deleteRegistration } from '@/api/registration';

export default {
  name: 'HistoryRegisteredEvents',
  components: { Sidebar },

  data() {
    return {
      historyEvents        : [],
      previousRegistrations: [],
      error   : null,
      loading : false,
      user    : JSON.parse(localStorage.getItem('user')) || { name: 'User' },
    };
  },

  created() { this.restoreLocal(); },
  mounted() {
    this.injectAnimateCSS();
    this.fetchRegistrations();
    this.poller = setInterval(this.fetchRegistrations, 30_000);
  },
  beforeUnmount() { clearInterval(this.poller); },

  methods: {
    /* ---------------- localStorage ---------------- */
    restoreLocal() {
      try {
        const saved = JSON.parse(localStorage.getItem('historyEvents') || '[]');
        this.historyEvents         = saved;
        this.previousRegistrations = [...saved];
      } catch { /* ignore */ }
    },

    /* ---------------- FETCH list ---------------- */
    async fetchRegistrations() {
      this.loading = true;
      try {
        const { data } = await getMyRegistrations();
        const list = Array.isArray(data) ? data : data?.message || [];
        const map  = { APPROVED:'Diterima', PENDING:'Menunggu', REJECTED:'Ditolak' };

        const mapped = list.map(r => ({
          id           : r.registrationId,
          eventId      : r.eventId,
          title        : r.eventName,
          description  : r.eventDesc,
          dateCreated  : r.registrationAt,
          status       : map[r.status] || r.status,
          certificateId: r.certificateId ?? null,
        }));

        mapped.forEach(n => {
          const old = this.previousRegistrations.find(o => o.id === n.id);
          if (old && old.status !== n.status) this.statusPopup(n);
        });

        this.historyEvents         = mapped;
        this.previousRegistrations = [...mapped];
        localStorage.setItem('historyEvents', JSON.stringify(mapped));
      } catch (e) {
        console.error(e);
        this.error = 'Gagal mengambil data registrasi.';
      } finally { this.loading = false; }
    },

    /* ---------------- CANCEL ---------------- */
    async cancelRegistration(id) {
      const ok = await this.$swal.fire({
        icon:'warning', title:'Batalkan Pendaftaran', text:'Yakin?',
        showCancelButton:true, confirmButtonText:'Ya', cancelButtonText:'Tidak'
      });
      if (!ok.isConfirmed) return;

      try {
        await deleteRegistration(id);
        await this.fetchRegistrations();
        this.$swal.fire({ icon:'success', title:'Berhasil dibatalkan',
                          timer:1600, showConfirmButton:false });
      } catch (e) {
        console.error(e);
        this.$swal.fire({ icon:'error', title:'Gagal', text:'Silakan coba lagi.' });
      }
    },

    /* ---------------- DOWNLOAD ---------------- */
    async downloadCertificate(ev) {
      try {
        const { default: api } = await import('@/api/axios');
        const res  = await api.get(
          `/api/certificates/${ev.certificateId}/download`,
          { responseType:'blob' });

        const blob = res.data instanceof Blob
                   ? res.data
                   : new Blob([res.data], { type:'application/pdf' });

        /* FE bikin nama file sendiri */
        const clean = str => str.replace(/\s+/g, '_');
        const filename = `${clean(this.user.name)}-${clean(ev.title)}-${ev.certificateId}.pdf`;

        const url  = URL.createObjectURL(blob);
        const link = Object.assign(document.createElement('a'),
                                   { href:url, download:filename });
        document.body.appendChild(link);
        link.click();
        setTimeout(() => { URL.revokeObjectURL(url); link.remove(); }, 8000);

        this.$swal.fire({ icon:'success', title:'Download Berhasil',
                          text:'Sertifikat tersimpan.' });

      } catch (e) {
        console.error(e);
        this.$swal.fire({ icon:'error', title:'Gagal Download', text:'Silakan coba lagi.' });
      }
    },

    /* ---------------- Misc helpers ---------------- */
    statusPopup(ev) {
      const icon = ev.status === 'Diterima' ? 'success'
                : ev.status === 'Menunggu' ? 'info' : 'error';
      this.$swal.fire({ icon, title:`Status: ${ev.status}`,
                        text:`"${ev.title}" sekarang ${ev.status}.` });
    },
    injectAnimateCSS() {
      if (!document.getElementById('animate-css')) {
        const l=document.createElement('link');
        l.id='animate-css'; l.rel='stylesheet';
        l.href='https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css';
        document.head.appendChild(l);
      }
    },
  },
};
</script>



<style scoped>
/* Import Animate.css */
@import 'https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css';

.content-wrapper {
  flex-grow: 1;
  margin-top: 90px;
  max-width: 1000px;
  padding: 0 15px;
  margin-left: auto;
  margin-right: auto;
}

.history-wrapper {
  padding: 0;
}

.history-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr)); /* responsif */
  justify-content: start;
  gap: 2rem; /* jarak antar card */
  padding: 0;
  width: 100%; /* pastikan memenuhi container */
  box-sizing: border-box;
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
  display: flex !important;
  flex-direction: column !important;
  border-radius: 20px !important;
  padding: 1.7rem 1.8rem !important;
  transition: transform 0.3s ease, box-shadow 0.3s ease !important;
 
  box-sizing: border-box;
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