import Vue from 'vue'
import App from './App'
import router from './router'
import '@/assets/css/global.css'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import Home from "./views/User";


Vue.use(ElementUI,{size:'small'});

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  render: h => h(App)
})
