<template>
  <div class="container mt-4">
    <!-- Header -->
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div class="card p-3" style="width: 300px;">
        <div class="d-flex align-items-center">
          <img src="https://via.placeholder.com/60" alt="Profile" class="rounded-circle me-3" />
          <div>
            <h5 class="mb-0">Aprilianza Muhammad Yusup</h5>
            <small>Divisi Kehumasan</small>
          </div>
        </div>
      </div>
      <button class="btn btn-danger btn-lg">Create Event</button>
    </div>

    <!-- Event Section -->
    <h5 class="mb-3 text-white bg-danger p-2 rounded">Current Event You Created</h5>
    <div class="row">
      <div class="col-md-4 mb-4" v-for="(event, index) in events" :key="index">
        <div class="card">
          <img :src="event.image" class="card-img-top" alt="Event Image" />
          <div class="card-body">
            <h6 class="card-title">{{ event.title }}</h6>
            <p class="card-text text-muted">{{ event.description }}</p>
            <div class="d-flex justify-content-between align-items-center">
              <span class="badge bg-light text-dark">{{ event.date }}</span>
              <span class="badge bg-light text-dark">{{ event.time }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "HomePage",
  data() {
    return {
      events: [
        {
          title: "Telkom Metra mengadakan lomba yang...",
          description: "See more",
          date: "21 Maret 2025",
          time: "09.00 WIB",
          image: "https://via.placeholder.com/400x200"
        },
        {
          title: "Telkom Metra mengadakan lomba yang...",
          description: "See more",
          date: "21 Maret 2025",
          time: "09.00 WIB",
          image: "https://via.placeholder.com/400x200"
        },
        {
          title: "Telkom Metra mengadakan lomba yang...",
          description: "See more",
          date: "21 Maret 2025",
          time: "09.00 WIB",
          image: "https://via.placeholder.com/400x200"
        }
      ]
    };
  }
};
</script>

<style scoped>
.card-title {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>