import { createApp } from 'vue'
import App from './App.vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import router from './router'
import Swal from 'sweetalert2'
import colors from './utils/colors'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import 'bootstrap-icons/font/bootstrap-icons.css';

import { 
  faHome, 
  faHistory, 
  faSignOutAlt,
  faCalendarAlt,
  faClock,
  faUsers,
  faPlus,
  faCalendarPlus,
  faEdit
} from '@fortawesome/free-solid-svg-icons'

library.add(
  faHome, 
  faHistory, 
  faSignOutAlt,
  faCalendarAlt,
  faClock,
  faUsers,
  faPlus,
  faCalendarPlus,
  faEdit
)

// Buat aplikasi Vue dan simpan ke dalam variabel app
const app = createApp(App)
app.config.globalProperties.$colors = colors
app.component('font-awesome-icon', FontAwesomeIcon)
// Tambahkan SweetAlert2 sebagai properti global
app.config.globalProperties.$swal = Swal

// Gunakan router dan mount aplikasi
app.use(router).mount('#app')