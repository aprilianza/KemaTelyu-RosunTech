import { createRouter, createWebHistory } from 'vue-router'
import UserLoginPage from './views/UserLoginPage.vue'
import DashboardMahasiswa from './views/DashboardMahasiswa.vue'
import HistoryRegistered from './views/HistoryRegistered.vue'
import DashboardStaff from './views/DashboardStaff.vue'
import CreateEvent from './views/CreateEvent.vue'

const routes = [
  { path: '/', name: 'Login', component: UserLoginPage },
  { path: '/Home', name: 'Home', component: DashboardMahasiswa },
  { path: '/History', name: 'History', component: HistoryRegistered },
  { path: '/DashboardStaff', name: 'DashboardStaff', component: DashboardStaff },
  { path: '/CreateEvent', name: 'CreateEvent', component: CreateEvent },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
