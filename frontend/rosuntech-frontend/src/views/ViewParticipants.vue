<template>
  <div class="d-flex flex-column">
    <!-- Main Content -->
    <div class="content-wrapper container py-5">
      <!-- Header Image -->
      <div class="header-image mb-4">
        <img :src="require('@/assets/img/event-banner.jpg')" class="img-fluid rounded" alt="Event Banner" />
      </div>

      <!-- Event Title and Message -->
      <div class="mb-4">
        <h5 class="fw-bold">Event B</h5>
        <p class="text-muted">Kamu tidak bisa mengubah Event ini karena belum mendaftar sebagai berikut</p>
      </div>

      <!-- Participants List -->
      <div class="participants-section bg-white p-4 rounded shadow-sm">
        <h6 class="fw-bold mb-3">PARTICIPANTS</h6>
        <div
          class="participant-card d-flex justify-content-between align-items-center mb-3 border rounded p-3"
          v-for="(participant, index) in participants"
          :key="index"
        >
          <div class="d-flex align-items-center">
            <img
              :src="require(`@/assets/img/${participant.photo}`)"
              alt="Participant Photo"
              class="rounded-circle me-3"
              width="50"
              height="50"
            />
            <div>
              <div class="fw-bold">{{ participant.name }}</div>
              <div class="text-muted small">{{ participant.nim }} - {{ participant.major }}</div>
            </div>
          </div>
          <button class="btn btn-danger rounded-circle" @click="removeParticipant(index)">X</button>
        </div>
      </div>

      <!-- Back to Dashboard Button -->
      <div class="mt-4 text-end">
        <router-link to="/DashboardStaff" class="btn btn-outline-danger">
          Kembali ke Dashboard
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ViewParticipants",
  data() {
    return {
      participants: [
        {
          name: "Aprilianza Muhammad Yusup",
          nim: "13021908232737",
          major: "S1 Informatika",
          photo: "profile.png"
        },
        {
          name: "Aprilianza Muhammad Yusup",
          nim: "13021908232737",
          major: "S1 Informatika",
          photo: "profile.png"
        },
        {
          name: "Aprilianza Muhammad Yusup",
          nim: "13021908232737",
          major: "S1 Informatika",
          photo: "profile.png"
        }
      ]
    };
  },
  methods: {
    removeParticipant(index) {
      this.participants.splice(index, 1);
    }
  }
};
</script>

<style scoped>
.content-wrapper {
  flex-grow: 1;
  background-color: #f8f9fa;
  /* Removed margin-left to accommodate the full-width layout without the sidebar */
}

.header-image img {
  width: 100%;
  max-height: 250px;
  object-fit: cover;
}

.participant-card img {
  object-fit: cover;
}
</style>
