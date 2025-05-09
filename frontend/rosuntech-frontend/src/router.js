import { createRouter, createWebHistory } from 'vue-router'
import UserLoginPage from './views/UserLoginPage.vue'
import DashboardMahasiswa from './views/DashboardMahasiswa.vue'
import HistoryRegistered from './views/HistoryRegistered.vue'

const routes = [
  { path: '/', name: 'Login', component: UserLoginPage },
  { path: '/Home', name: 'Home', component: DashboardMahasiswa },
  { path: '/History', name: 'History', component: HistoryRegistered },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
