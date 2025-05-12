<template>
  <div class="container mt-4">
    <!-- Header -->
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div class="card p-3" style="width: 300px;">
        <div class="d-flex align-items-center">
          <img src="data:image/jpeg;base64,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" alt="Profile" class="rounded-circle me-3" />
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