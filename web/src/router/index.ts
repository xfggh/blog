import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

import Home from './../views/Home/Home.vue'

import Tools from './../views/Tools/Tools.vue'
import Hash from './../views/Tools/pages/Hash.vue'
import Base64 from './../views/Tools/pages/Base64.vue'
import ApacheTools from './../views/Tools/pages/ApacheTools.vue'
import Writing from './../views/Tools/pages/Writing.vue'

const routes: Array<RouteRecordRaw> = [
  { path: '/', name: 'Home', component: Home },
  {
    path: '/tools', name: 'Tools', component: Tools, 
    children: [
      { path: 'code/hash', name: 'Hash', component: Hash },
      { path: 'code/base64', name: 'Base64', component: Base64 },
      { path: 'writing', name: 'Writing', component: Writing },
      { path: 'apache', name: 'ApacheTools', component: ApacheTools },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
