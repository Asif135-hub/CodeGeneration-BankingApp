import { createApp } from 'vue';
import { createPinia } from 'pinia';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';

import App from './App.vue';
import router from './router';
import './assets/main.css';

// Import Pinia store
import { useUserStore } from '@/stores/User';

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);
app.use(router);


// const userStore = useUserStore();
// userStore.initializeStore();


app.mount('#app');

// Initialize the store after the app has been mounted
// Set up Axios interceptor

