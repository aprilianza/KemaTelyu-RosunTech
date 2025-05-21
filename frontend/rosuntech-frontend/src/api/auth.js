import axios from 'axios';

export async function loginUser(email, password) {
  try {
    const response = await axios.post('/api/auth/login', {
      email,
      password
    });

    const { token, user } = response.data;

    if (token) {
      localStorage.setItem('token', token); // ✅ token untuk Authorization
    }

    if (user) {
      localStorage.setItem('user', JSON.stringify(user)); // ✅ user info kalau mau ditampilin
    }

    return response;
  } catch (err) {
    console.error('Login gagal:', err);
    throw err;
  }
}
