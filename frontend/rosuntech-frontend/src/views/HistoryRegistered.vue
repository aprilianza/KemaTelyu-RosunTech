<template>
  <div id="app" class="d-flex">
    <!-- Sidebar -->
    <Sidebar />

    <!-- Main Content -->
    <div class="content-wrapper container-fluid py-4">
      <!-- History Events Section -->
      <div class="history-wrapper">
        <!-- Loading State -->
        <div v-if="loading" class="loading-state">
          <div class="spinner-border text-danger" role="status">
            <span class="visually-hidden">Loading...</span>
          </div>
          <p class="mt-3 text-danger">Memuat data...</p>
        </div>

        <!-- Empty State -->
        <div v-if="!loading && historyEvents.length === 0" class="empty-history">
          <div class="empty-icon">
            <i class="bi bi-calendar-x"></i>
          </div>
          <h3 class="empty-title">Belum ada riwayat pendaftaran</h3>
          <p class="empty-text">Anda belum mendaftar event apapun.</p>
          <router-link to="/events" class="btn btn-primary mt-3"> Jelajahi Event </router-link>
        </div>

        <!-- Events Grid -->
        <div v-if="!loading && historyEvents.length > 0" class="history-grid">
          <div v-for="(event, index) in historyEvents" :key="event.id" class="history-item">
            <div
              class="event-card animate__animated animate__fadeInUp"
              :class="{
                'bg-approved': event.status === 'Diterima',
                'bg-pending': event.status === 'Menunggu',
                'bg-rejected': event.status === 'Ditolak',
              }"
              :style="{ 'animation-delay': index * 0.1 + 's' }"
            >
              <div class="card-header">
                <h5 class="event-title">{{ event.title }}</h5>
                <div class="event-date">
                  <i class="bi bi-calendar"></i>
                  {{ formatDate(event.dateCreated) }}
                </div>
              </div>

              <div class="card-body">
                <p class="event-description" v-if="event.description">
                  {{ truncateDescription(event.description) }}
                </p>
              </div>

              <div class="card-footer">
                <div class="status-container">
                  <span
                    class="status-badge"
                    :class="{
                      'status-approved': event.status === 'Diterima',
                      'status-pending': event.status === 'Menunggu',
                      'status-rejected': event.status === 'Ditolak',
                    }"
                  >
                    {{ event.status }}
                  </span>
                </div>

                <div class="action-buttons">
                  <button v-if="event.status === 'Diterima' && event.certificateId" class="btn btn-download" @click="downloadCertificate(event)">
                    <i class="bi bi-file-earmark-pdf"></i>
                    Sertifikat
                  </button>

                  <button v-if="event.status === 'Menunggu'" class="btn btn-cancel" @click="cancelRegistration(event.id)">
                    <i class="bi bi-x-circle"></i>
                    Batalkan
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
      historyEvents: [],
      previousRegistrations: [],
      error: null,
      loading: false,
      user: JSON.parse(localStorage.getItem('user')) || { name: 'User' },
    };
  },

  created() {
    this.restoreLocal();
  },
  mounted() {
    this.injectAnimateCSS();
    this.fetchRegistrations();
    this.poller = setInterval(this.fetchRegistrations, 30_000);
  },
  beforeUnmount() {
    clearInterval(this.poller);
  },

  methods: {
    /* ---------------- localStorage ---------------- */
    restoreLocal() {
      try {
        const saved = JSON.parse(localStorage.getItem('historyEvents') || '[]');
        this.historyEvents = saved;
        this.previousRegistrations = [...saved];
      } catch {
        /* ignore */
      }
    },

    /* ---------------- FETCH list ---------------- */
    async fetchRegistrations() {
      this.loading = true;
      try {
        const { data } = await getMyRegistrations();
        const list = Array.isArray(data) ? data : data?.message || [];
        const map = { APPROVED: 'Diterima', PENDING: 'Menunggu', REJECTED: 'Ditolak' };

        const mapped = list.map((r) => ({
          id: r.registrationId,
          eventId: r.eventId,
          title: r.eventName,
          description: r.eventDesc,
          dateCreated: r.registrationAt,
          status: map[r.status] || r.status,
          certificateId: r.certificateId ?? null,
        }));

        mapped.forEach((n) => {
          const old = this.previousRegistrations.find((o) => o.id === n.id);
          if (old && old.status !== n.status) this.statusPopup(n);
        });

        this.historyEvents = mapped;
        this.previousRegistrations = [...mapped];
        localStorage.setItem('historyEvents', JSON.stringify(mapped));
      } catch (e) {
        console.error(e);
        this.error = 'Gagal mengambil data registrasi.';
      } finally {
        this.loading = false;
      }
    },

    /* ---------------- CANCEL ---------------- */
    async cancelRegistration(id) {
      const ok = await this.$swal.fire({
        title: 'Batalkan Pendaftaran?',
        text: 'Anda yakin ingin membatalkan pendaftaran ini?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Ya, Batalkan!',
        cancelButtonText: 'Tidak',
      });

      if (!ok.isConfirmed) return;

      try {
        await deleteRegistration(id);
        await this.fetchRegistrations();
        this.$swal.fire({
          position: 'top-end',
          icon: 'success',
          title: 'Pendaftaran dibatalkan',
          showConfirmButton: false,
          timer: 1500,
          toast: true,
        });
      } catch (e) {
        console.error(e);
        this.$swal.fire({
          icon: 'error',
          title: 'Gagal',
          text: 'Gagal membatalkan pendaftaran. Silakan coba lagi.',
        });
      }
    },

    /* ---------------- DOWNLOAD ---------------- */
    async downloadCertificate(ev) {
      try {
        const { default: api } = await import('@/api/axios');
        const res = await api.get(`/api/certificates/${ev.certificateId}/download`, { responseType: 'blob' });

        const blob = res.data instanceof Blob ? res.data : new Blob([res.data], { type: 'application/pdf' });

        const clean = (str) => str.replace(/[^a-z0-9]/gi, '_').toLowerCase();
        const filename = `sertifikat_${clean(ev.title)}_${clean(this.user.name)}.pdf`;

        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = filename;
        document.body.appendChild(link);
        link.click();

        setTimeout(() => {
          URL.revokeObjectURL(url);
          link.remove();
        }, 100);

        this.$swal.fire({
          position: 'top-end',
          icon: 'success',
          title: 'Download dimulai',
          showConfirmButton: false,
          timer: 1500,
          toast: true,
        });
      } catch (e) {
        console.error(e);
        this.$swal.fire({
          icon: 'error',
          title: 'Gagal Download',
          text: 'Sertifikat tidak tersedia atau terjadi kesalahan.',
        });
      }
    },

    /* ---------------- Helpers ---------------- */
    formatDate(dateString) {
      if (!dateString) return '';
      const options = {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      };
      return new Date(dateString).toLocaleDateString('id-ID', options);
    },

    truncateDescription(text) {
      if (!text) return '';
      return text.length > 120 ? text.substring(0, 120) + '...' : text;
    },

    statusPopup(ev) {
      const icon = ev.status === 'Diterima' ? 'success' : ev.status === 'Menunggu' ? 'info' : 'error';

      this.$swal.fire({
        position: 'top-end',
        icon,
        title: `Status berubah: ${ev.status}`,
        text: `Pendaftaran "${ev.title}" sekarang ${ev.status.toLowerCase()}.`,
        showConfirmButton: false,
        timer: 3000,
        toast: true,
      });
    },

    injectAnimateCSS() {
      if (!document.getElementById('animate-css')) {
        const link = document.createElement('link');
        link.id = 'animate-css';
        link.rel = 'stylesheet';
        link.href = 'https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css';
        document.head.appendChild(link);
      }
    },
  },
  beforeRouteEnter(to, from, next) {
    const token = localStorage.getItem('token');
    const userData = localStorage.getItem('user');

    if (!token) {
      next('/');
      return;
    }

    if (userData) {
      try {
        const user = JSON.parse(userData);
        if (!user.role || user.role.toUpperCase() !== 'MAHASISWA') {
          next('/');
          return;
        }
      } catch (error) {
        next('/');
        return;
      }
    }

    next();
  },
};
</script>

<style scoped>
/* Base Styles */
.content-wrapper {
  flex-grow: 1;
  margin-top: 90px;
  padding: 0 20px;
  width: 100%;
  max-width: 1400px;
  margin-left: auto;
  margin-right: auto;
}

.history-header {
  text-align: center;
  margin-bottom: 3rem;
}

.history-header h1 {
  font-size: 2.5rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.history-header p {
  font-size: 1.2rem;
  color: #7f8c8d;
}

/* Loading State */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  color: #3498db;
}

/* Empty State */
.empty-history {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  text-align: center;
  padding: 2rem;
}

.empty-icon {
  font-size: 4rem;
  color: #bdc3c7;
  margin-bottom: 1.5rem;
}

.empty-title {
  font-size: 1.8rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.empty-text {
  font-size: 1.1rem;
  color: #7f8c8d;
  max-width: 500px;
  margin-bottom: 1.5rem;
}

/* History Grid */
.history-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 1.5rem;
  padding: 0.5rem;
}

.history-item {
  transition: transform 0.3s ease;
}

.history-item:hover {
  transform: translateY(-5px);
}

/* Event Card */
.event-card {
  display: flex;
  flex-direction: column;
  height: 100%;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.event-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.card-header {
  padding: 1.5rem 1.5rem 0.5rem;
}

.card-body {
  padding: 0 1.5rem;
  flex-grow: 1;
}

.card-footer {
  padding: 1rem 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px solid rgba(255, 255, 255, 0.2);
}

.event-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: inherit;
}

.event-date {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
  color: inherit;
  opacity: 0.9;
}

.event-description {
  font-size: 0.95rem;
  line-height: 1.5;
  color: inherit;
  opacity: 0.9;
  margin-bottom: 1rem;
}

/* Status Badge */
.status-badge {
  padding: 0.4rem 1rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.status-approved {
  background-color: rgba(255, 255, 255, 0.2);
  color: white;
}

.status-pending {
  background-color: rgba(0, 0, 0, 0.1);
  color: #212529;
}

.status-rejected {
  background-color: rgba(255, 255, 255, 0.2);
  color: white;
}

/* Action Buttons */
.action-buttons {
  display: flex;
  gap: 0.75rem;
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  font-size: 0.85rem;
  font-weight: 500;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-download {
  background-color: rgba(255, 255, 255, 0.9);
  color: #27ae60;
}

.btn-download:hover {
  background-color: white;
  color: #219653;
}

.btn-cancel {
  background-color: rgba(255, 255, 255, 0.9);
  color: #e74c3c;
}

.btn-cancel:hover {
  background-color: white;
  color: #c0392b;
}

/* Background Colors */
.bg-approved {
  background: linear-gradient(135deg, #27ae60, #2ecc71);
  color: white;
}

.bg-pending {
  background: linear-gradient(135deg, #f39c12, #f1c40f);
  color: #2c3e50;
}

.bg-rejected {
  background: linear-gradient(135deg, #e74c3c, #c0392b);
  color: white;
}

/* Responsive Adjustments */
@media (max-width: 1200px) {
  .history-grid {
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  }
}

@media (max-width: 992px) {
  .content-wrapper {
    margin-top: 70px;
    padding: 0 15px;
  }

  .history-header h1 {
    font-size: 2rem;
  }
}

@media (max-width: 768px) {
  .history-grid {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  }

  .card-footer {
    flex-direction: column;
    gap: 1rem;
    align-items: flex-start;
  }

  .action-buttons {
    width: 100%;
    justify-content: flex-end;
  }
}

@media (max-width: 576px) {
  .history-header h1 {
    font-size: 1.8rem;
  }

  .history-header p {
    font-size: 1rem;
  }

  .history-grid {
    grid-template-columns: 1fr;
  }

  .event-card {
    border-radius: 10px;
  }
}

/* Animations */
.animate__animated {
  --animate-duration: 0.6s;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate__fadeInUp {
  animation-name: fadeInUp;
}
</style>
