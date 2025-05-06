<template>
    <div class="container d-flex justify-content-center align-items-center min-vh-100">
      <div class="card shadow-lg p-4" style="max-width: 400px; width: 100%; border-radius: 12px;">
        <div class="card-body">
          <h3 class="card-title text-center mb-4">Login to Your Account</h3>
          <form @submit.prevent="login">
            <!-- Email Input -->
            <div class="mb-3">
              <label for="email" class="form-label">Email</label>
              <input type="email" id="email" class="form-control" v-model="email" placeholder="Enter your email" required />
            </div>
  
            <!-- Password Input -->
            <div class="mb-3">
              <label for="password" class="form-label">Password</label>
              <input type="password" id="password" class="form-control" v-model="password" placeholder="Enter your password" required />
            </div>
  
            <!-- Submit Button -->
            <div class="d-flex justify-content-between">
              <button type="submit" class="btn btn-primary w-100">Login</button>
            </div>
          </form>
  
          <!-- Register Link -->
          <div class="text-center mt-3">
            <p class="mb-0">Don't have an account? <router-link to="/register" class="text-primary">Sign Up</router-link></p>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'LoginPage',
    data() {
      return {
        email: '',
        password: ''
      };
    },
    methods: {
      login() {
        // Simulasi login (sesuaikan dengan backend nantinya)
        if (this.email === 'user@example.com' && this.password === 'password') {
          alert('Login Successful');
          this.$router.push('/dashboard'); // Redirect ke halaman dashboard
        } else {
          alert('Invalid credentials!');
        }
      }
    }
  }
  </script>
  
  <style scoped>
  /* Styling untuk login page */
  .container {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .card {
    border-radius: 12px;
  }
  
  .card-title {
    font-size: 1.5rem;
    font-weight: 500;
  }
  
  button {
    border-radius: 30px;
    padding: 12px 20px;
  }
  
  router-link {
    text-decoration: none;
  }
  </style>
  