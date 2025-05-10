import axios from 'axios';

export async function loginUser(email, password) {
  return axios.post('/api/auth/login', {
    email,
    password
  });
}
