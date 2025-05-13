<template>
  <!-- Gambar Vector 1 - Pojok Kiri Atas -->
  <img src="@/assets/img/Vector-landing1.png" class="vector-img vector-top" />

  <!-- Card Login -->
  <div class="login-container">
    <div class="card shadow-lg p-4">
      <div class="card-body">
        <!-- Form Login -->
        <h3 class="card-title text-center mb-1">Selamat Datang di </h3>
        <h1 class="text-center mb-4 fw-bold">
          <span class="kema-text">Kema</span><span class="telyu-text">Telyu</span>
        </h1>

        <form @submit.prevent="login">
          <!-- Email -->
          <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input
              type="email"
              id="email"
              class="form-control"
              v-model="email"
              placeholder="Masukkan Email"
              required
            />
          </div>

          <!-- Password -->
          <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input
              type="password"
              id="password"
              class="form-control"
              v-model="password"
              placeholder="Masukkan Password"
              required
            />
          </div>

          <!-- Button -->
          <button type="submit" class="btn btn-primary w-100">Login</button>
        </form>
      </div>
    </div>
  </div>

  <!-- Gambar Vector 2 - Pojok Kanan Bawah -->
  <img src="@/assets/img/vector-landing2.png" class="vector-img vector-bottom" />
</template>

<script>
import { loginUser } from '@/api/auth';

export default {
  name: 'LoginPage',
  data() {
    return {
      email: '',
      password: ''
    };
  },
  methods: {
    async login() {
      try {
        const { data } = await loginUser(this.email, this.password);

        localStorage.setItem('user', JSON.stringify(data.user));

        if (data.user.role === 'MAHASISWA') {
          this.$router.push('/home');
        } else if (data.user.role === 'STAFF') {
          this.$router.push('/dashboard-staff');
        } else {
          this.$router.push('/');
        }
      } catch (err) {
        let errorMessage = 'Terjadi kesalahan saat login.';

        if (err.response && err.response.status === 401) {
          errorMessage = err.response.data || 'Email atau password salah';
        }

        // Tampilkan SweetAlert2
        this.$swal.fire({
          icon: 'error',
          title: 'Login Failed',
          text: errorMessage,
          confirmButtonText: 'OK',
          customClass: {
            confirmButton: 'btn btn-secondary'
          }
        });
      }
    }
  }
};
</script>

<style scoped>


.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: v-bind("$colors.secondary");
  padding: 20px;
  z-index: 1;
}

.card {
  max-width: 400px;
  width: 100%;
  border-radius: 12px;
  background-color: #fff;
  padding: 2rem;
  z-index: 2;
  position: relative;
}

.card-title {
  font-size: 1.5rem;
  font-weight: 500;
}

.kema-text {
  color: v-bind("$colors.third");
}

.telyu-text {
  color: v-bind("$colors.primary");
}

.btn-primary {
  background-color: v-bind("$colors.primary");
  border: none;
  border-radius: 30px;
  padding: 12px 20px;
}

.btn-primary:hover {
  background-color: v-bind("$colors.fourth");
}

.vector-img {
  position: absolute;
  z-index: 0;
  pointer-events: none;
}

.vector-top {
  top: 0;
  left: 0;
  width: 100%;
  height: auto;
}

.vector-bottom {
  bottom: 0;
  right: 0;
  width: 100%;
  height: auto;
}
</style>