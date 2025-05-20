<template>
  <div class="container py-4" style="background-color: #fdeeee; min-height: 100vh;">
    <!-- Header -->
    <div class="d-flex justify-content-between align-items-center mb-4">
     <img src="https://upload.wikimedia.org/wikipedia/commons/0/03/Logo_Telkom_University_potrait.png" alt="Telkom Logo" style="height: 40px;" />
    <h5 class="text-danger fw-bold">Kema<span class="text-dark">Telyu</span></h5>
    <router-link to="/DashboardStaff" class="text-danger fw-bold text-decoration-none">Home</router-link>
    </div>


    <!-- Create Event Button -->
    <div class="text-center mb-4">
      <button class="btn btn-danger btn-lg px-5">Create Event</button>
    </div>

    <!-- Form -->
    <form @submit.prevent="submitEvent" class="bg-white p-4 rounded shadow-sm">
      <!-- Title -->
      <div class="mb-4">
        <label for="title" class="form-label fw-semibold">Judul</label>
        <input type="text" id="title" v-model="form.title" class="form-control bg-light border-0" required />
      </div>

      <!-- Upload Photo -->
      <div class="mb-4">
        <label for="photo" class="form-label fw-semibold">Upload Foto</label>
        <input type="file" id="photo" @change="handleFileUpload" class="form-control bg-light border-0" required />
      </div>

      <!-- Max Participants -->
      <div class="mb-4">
        <label for="maxParticipants" class="form-label fw-semibold">Maks Participants</label>
        <input type="number" id="maxParticipants" v-model="form.maxParticipants" class="form-control bg-light border-0" required />
      </div>

      <!-- Submit -->
      <div class="text-center">
        <button type="submit" class="btn btn-danger px-5 rounded-pill">POST</button>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  name: "CreateEvent",
  data() {
    return {
      form: {
        title: '',
        photo: null,
        maxParticipants: ''
      }
    };
  },
  methods: {
    handleFileUpload(event) {
      this.form.photo = event.target.files[0];
    },
    submitEvent() {
      // Lakukan validasi & upload di sini
      console.log("Event data submitted:", this.form);
      alert("Event berhasil dibuat!");
    }
  }
};
</script>

<style scoped>
input.form-control:focus {
  box-shadow: none;
}
</style>