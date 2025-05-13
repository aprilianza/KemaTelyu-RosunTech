import axios from 'axios';

// Login user dan langsung simpan token + user ke localStorage
export async function loginUser(email, password) {
  try {
    const response = await axios.post('/api/auth/login', {
      email,
      password
    });

    const token = response.data.token;
    const user = response.data.user;

    if (token) {
      localStorage.setItem('token', token); // simpan token
    }

    if (user) {
      localStorage.setItem('user', JSON.stringify(user)); // simpan data user
    }

    return response; // kirim balik buat dipakai di komponen kalau perlu
  } catch (err) {
    console.error('Login gagal:', err);
    throw err; // lempar error buat ditangani di komponen (alert, dll)
  }
}
