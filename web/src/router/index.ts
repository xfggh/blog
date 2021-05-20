import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

import Home from './../views/Home/Home.vue'

import Tools from './../views/Tools/Tools.vue'
import Hash from './../views/Tools/pages/Hash.vue'
import Base64 from './../views/Tools/pages/Base64.vue'
import ApacheTools from './../views/Tools/pages/ApacheTools.vue'
import Writing from './../views/Tools/pages/Writing.vue'
import KeyBoard from './../views/Tools/pages/KeyBoard.vue'

import Admin from './../views/Admin/Admin.vue'
import EbookManage from './../views/Admin/EbookManage/EbookManage.vue'
import CategoryManage from './../views/Admin/CategoryManage/CategoryManage.vue'
import DocManage from './../views/Admin/DocManage/DocManage.vue'

const routes: Array<RouteRecordRaw> = [
  { path: '/', name: 'Home', component: Home },
  {
    path: '/tools', name: 'Tools', component: Tools, 
    children: [ // 这个地方好像不用路由：使用 v-show 显示
      { path: 'code/hash', name: 'Hash', component: Hash },
      { path: 'code/base64', name: 'Base64', component: Base64 },
      { path: 'writing', name: 'Writing', component: Writing },
      { path: 'apache', name: 'ApacheTools', component: ApacheTools },
      { path: 'key-board', name: 'KeyBoard', component: KeyBoard },
    ]
  },
  { 
    path: '/admin', name: 'Admin', component: Admin,
    children: [
      { path: 'ebook-manage', name: 'EbookManage', component: EbookManage },
      { path: 'category', name: 'CategoryManage', component: CategoryManage },
      { path: 'doc', name: 'DocManage', component: DocManage },
      { path: '*', redirect: '/admin' }, // 不生效
    ],
    redirect: '/admin/ebook-manage',
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
