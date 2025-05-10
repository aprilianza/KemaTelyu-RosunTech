<template>
  <div class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card shadow-lg p-4" style="max-width: 400px; width: 100%; border-radius: 12px;">
      <div class="card-body">
        <h3 class="card-title text-center mb-4">Login to Your Account</h3>

        <form @submit.prevent="login">
          <!-- Email -->
          <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input
              type="email"
              id="email"
              class="form-control"
              v-model="email"
              placeholder="Enter your email"
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
              placeholder="Enter your password"
              required
            />
          </div>

          <!-- Button -->
          <button type="submit" class="btn btn-primary w-100">Login</button>
        </form>
      </div>
    </div>
  </div>
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
        /* --- panggil backend --- */
        const { data } = await loginUser(this.email, this.password);

        alert(data.message); // "Login berhasil"

        /* simpan info user (tanpa password memang!) */
        localStorage.setItem('user', JSON.stringify(data.user));

        /* redirect sesuai role */
        if (data.user.role === 'MAHASISWA') {
          this.$router.push('/home');
        } else if (data.user.role === 'STAFF') {
          this.$router.push('/dashboard-staff');
        } else {
          this.$router.push('/');
        }
      } catch (err) {
        if (err.response && err.response.status === 401) {
          alert(err.response.data); // "Email tidak ditemukan" / "Password salah"
        } else {
          alert('Terjadi kesalahan saat login.');
        }
      }
    }
  }
};
</script>

<style scoped>
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
</style>
