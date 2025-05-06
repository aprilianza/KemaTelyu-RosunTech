import { createRouter, createWebHistory } from 'vue-router'
import UserLoginPage from './views/UserLoginPage.vue'

const routes = [
  { path: '/', name: 'Login', component: UserLoginPage },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
