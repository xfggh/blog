import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

import Home from './../views/Home/Home.vue'
import Tools from './../views/Tools/Tools.vue'
import MD5 from './../views/Tools/components/MD5.vue'

const routes: Array<RouteRecordRaw> = [
  { path: '/', name: 'Home', component: Home },
  {
    path: '/tools', name: 'Tools', component: Tools, 
    children: [
      { path: 'md5', name: 'MD5', component: MD5 }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
