import { createRouter, createWebHistory } from 'vue-router'
import UserLoginPage from './views/UserLoginPage.vue'
import RegisterPage from './views/RegisterPage.vue'

const routes = [
  { path: '/', name: 'Login', component: UserLoginPage },
  { path: '/Register', name: 'Register', component: RegisterPage }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
