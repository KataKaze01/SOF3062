import { createRouter, createWebHistory } from 'vue-router'
import ThemMoi from './components/ThemMoi.vue'
import LietKe from './components/LietKe.vue'
import ChiTiet from './components/ChiTiet.vue'

const routes = [
  { path: '/', redirect: '/lietke' },
  { path: '/themmoi', component: ThemMoi },
  { path: '/lietke', component: LietKe },
  { path: '/chitiet/:ma', component: ChiTiet, props: true }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router