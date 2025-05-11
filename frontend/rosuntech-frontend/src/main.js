import { createApp } from 'vue'
import App from './App.vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import router from './router'
import Swal from 'sweetalert2'
import colors from './utils/colors'
// Buat aplikasi Vue dan simpan ke dalam variabel app
const app = createApp(App)
app.config.globalProperties.$colors = colors

// Tambahkan SweetAlert2 sebagai properti global
app.config.globalProperties.$swal = Swal

// Gunakan router dan mount aplikasi
app.use(router).mount('#app')