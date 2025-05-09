<template>
    <div class="container mt-5">
      <!-- User Profile Section -->
      <div class="row mb-4">
        <div class="col-12 col-md-4">
          <img :src="user.photo" class="img-fluid rounded-circle" alt="User Photo" />
        </div>
        <div class="col-12 col-md-8">
          <h3>{{ user.name }}</h3>
          <p><strong>NIM:</strong> {{ user.nim }}</p>
          <p><strong>Fakultas:</strong> {{ user.faculty }}</p>
          <button class="btn btn-primary">Get Certificate</button>
        </div>
      </div>
  
      <!-- Popular Events Section -->
      <h4>Popular Events</h4>
      <div class="row">
        <div class="col-12 col-md-6 mb-3" v-for="event in events" :key="event.id">
          <div class="card">
            <img :src="event.image" class="card-img-top" alt="Event Image" />
            <div class="card-body">
              <h5 class="card-title">{{ event.title }}</h5>
              <button class="btn btn-info">See More!</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'Dashboard',
    data() {
      return {
        user: {
          name: 'Rahmat Widodo',
          nim: '123456789',
          faculty: 'Ilmu Komputer',
          photo: 'https://via.placeholder.com/150'
        },
        events: [
          {
            id: 1,
            title: 'Event 1',
            image: 'https://via.placeholder.com/400x200'
          },
          {
            id: 2,
            title: 'Event 2',
            image: 'https://via.placeholder.com/400x200'
          }
        ]
      };
    }
  };
  </script>
  
  <style scoped>
  .card-img-top {
    height: 200px;
    object-fit: cover;
  }
  </style>
  