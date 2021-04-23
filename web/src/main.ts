import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// 完整引入 antd vue
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';

// 全局引入 图标文件
import * as Icons from '@ant-design/icons-vue';

import axios from 'axios';
axios.defaults.baseURL = process.env.VUE_APP_SERVER;

// axios 拦截器
axios.interceptors.request.use((config) => {
    console.log('请求参数：', config);
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(response => {
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误：', error);
    return Promise.reject(error);
});

const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app');

const icons:any = Icons;
for(const i in icons){
    app.component(i, icons[i]);
}

console.log('环境：', process.env.NODE_ENV, ':' , process.env.VUE_APP_SERVER);