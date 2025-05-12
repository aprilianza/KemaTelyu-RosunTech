import { createRouter, createWebHistory } from 'vue-router'
import UserLoginPage from './views/UserLoginPage.vue'
import DashboardMahasiswa from './views/DashboardMahasiswa.vue'
import HistoryRegistered from './views/HistoryRegistered.vue'
import DashboardStaff from './views/DashboardStaff.vue'

const routes = [
  { path: '/', name: 'Login', component: UserLoginPage },
  { path: '/Home', name: 'Home', component: DashboardMahasiswa },
  { path: '/History', name: 'History', component: HistoryRegistered },
  { path: '/History', name: 'DashboardStaff', component: DashboardStaff },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
