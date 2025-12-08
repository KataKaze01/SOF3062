import { createRouter, createWebHistory } from 'vue-router'
import CategoryPage from '@/views/CategoryPage.vue'
import ProductPage from '@/views/ProductPage.vue'
import UserPage from '@/views/UserPage.vue'

const routes = [
  { path: '/', redirect: '/categories' },
  { path: '/categories', component: CategoryPage },
  { path: '/products', component: ProductPage },
  { path: '/users', component: UserPage }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
