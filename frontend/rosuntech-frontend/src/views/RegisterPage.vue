<template>
    <div class="container d-flex justify-content-center align-items-center min-vh-100">
      <div class="card shadow-lg p-4" style="max-width: 400px; width: 100%; border-radius: 12px;">
        <div class="card-body">
          <h3 class="card-title text-center mb-4">Create Your Account</h3>
          <form @submit.prevent="register">
            <!-- Name Input -->
            <div class="mb-3">
              <label for="name" class="form-label">Full Name</label>
              <input type="text" id="name" class="form-control" v-model="name" placeholder="Enter your full name" required />
            </div>
  
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
  
            <!-- Confirm Password Input -->
            <div class="mb-3">
              <label for="confirmPassword" class="form-label">Confirm Password</label>
              <input type="password" id="confirmPassword" class="form-control" v-model="confirmPassword" placeholder="Confirm your password" required />
            </div>
  
            <!-- Submit Button -->
            <div class="d-flex justify-content-between">
              <button type="submit" class="btn btn-primary w-100">Sign Up</button>
            </div>
          </form>
  
          <!-- Login Link -->
          <div class="text-center mt-3">
            <p class="mb-0">Already have an account? <router-link to="/" class="text-primary">Login</router-link></p>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'RegisterPage',
    data() {
      return {
        name: '',
        email: '',
        password: '',
        confirmPassword: ''
      };
    },
    methods: {
      register() {
        // Validasi form
        if (this.password !== this.confirmPassword) {
          alert('Passwords do not match!');
          return;
        }
  
        // Simulasi register (sesuaikan dengan backend nantinya)
        if (this.name && this.email && this.password) {
          alert('Registration Successful');
          this.$router.push('/login'); // Redirect ke halaman login
        } else {
          alert('Please fill in all fields!');
        }
      }
    }
  };
  </script>
  
  <style scoped>
  /* Styling untuk register page */
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
  