import Vue from 'vue'
import VueRouter from "vue-router";
import App from './App.vue'
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue';
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import StatsPage from "./components/StatsPage";
import ResultsPage from "./components/ResultsPage";
import PlayersPage from "./components/PlayersPage";
import HomePage from "./components/HomePage";

Vue.config.productionTip = false;

Vue.use(BootstrapVue);
Vue.use(IconsPlugin);
Vue.use(VueRouter);

const routes = [
  { path: '/', component: HomePage },
  { path: '/results', component: ResultsPage },
  { path: '/players', component: PlayersPage },
  { path: '/stats/:statType', component: StatsPage, props: true },
];

const router = new VueRouter({
  routes
});

new Vue({
  router,
  render: h => h(App),
}).$mount('#app');
