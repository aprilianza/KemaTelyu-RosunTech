<template>
  <div class="container mt-5">
    <!-- Tombol Back -->
    <div class="mb-4">
        <button @click="$router.push({ name: 'Home' })" class="btn btn-outline-secondary">
        &larr; Back to Home
        </button>
    </div>

    <!-- Judul Halaman -->
    <h4>Registered Events</h4>

    <!-- Daftar Event -->
    <div class="row">
      <div
        class="col-12 col-md-6 mb-3"
        v-for="event in historyEvents"
        :key="event.id"
      >
        <div class="card bg-light">
          <div class="card-body d-flex flex-column">
            <h5 class="card-title">{{ event.title }}</h5>
            <p>{{ event.dateCreated }}</p>

            <!-- Baris untuk status dan tombol -->
            <div class="d-flex justify-content-between align-items-center">
              <!-- Logo Approve berdasarkan status -->
              <div
                v-if="event.status === 'Diterima'"
                class="d-flex align-items-center"
              >
                ✅ <strong class="ms-1">Status:</strong> {{ event.status }}
              </div>
              <div v-else class="d-flex align-items-center">
                ⚠️ <strong class="ms-1">Status:</strong> {{ event.status }}
              </div>

              <!-- Tombol Get!! hanya muncul jika status Diterima -->
              <button
                v-if="event.status === 'Diterima'"
                class="btn btn-primary btn-sm"
              >
                Get!!
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HistoryRegisteredEvents',
  data() {
    return {
      historyEvents: [
        {
          id: 1,
          title: 'Event 1',
          dateCreated: '2023-05-09',
          status: 'Diterima'
        },
        {
          id: 2,
          title: 'Event 2',
          dateCreated: '2023-05-09',
          status: 'Menunggu'
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
.card {
  border-radius: 12px;
  min-height: 140px;
}
</style>