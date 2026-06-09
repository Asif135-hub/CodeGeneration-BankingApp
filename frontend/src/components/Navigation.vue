<template>
  <nav v-if="!hideNavBar" class="navbar navbar-expand-md navbar-dark bg-dark">
    <div class="container-fluid">
      <ul class="navbar-nav me-auto mb-2 mb-md-0">
        <li v-if="!isAuthenticated && !store.isLoggedIn" class="nav-item">
          <router-link to="/" class="nav-link" active-class="active">Home</router-link>
        </li>
   
        <li v-if="isAuthenticated" class="nav-item">
          <router-link to="/customerDashboard" class="nav-link" active-class="active">Customer Dashboard</router-link>
        </li>
        <li v-if="!isAuthenticated && !isAtmLogin" class="nav-item">
          <router-link to="/login" class="nav-link" active-class="active"
                       @click.native="handleRegularLoginClick">Login</router-link>
        </li>
        <li v-if="!isAtmUser && !isCustomer && !store.isLoggedIn && !isAuthenticated" class="nav-item">
          <router-link to="/atm/login" class="nav-link" active-class="active">ATM Login</router-link>
        </li>
      </ul>
      <div class="d-flex">
        <span v-if="store.isLoggedIn" class="navbar-text text-white me-3">Hello, {{ store.user.firstName }} {{
            store.user.lastName }}</span>
        <button v-if="store.isLoggedIn" @click="AtmLogout" class="btn btn-outline-light">Logout</button>
      </div>
      <div class="d-flex">
        <span v-if="isAuthenticated" class="navbar-text text-white me-3">Hello, {{ userName }}</span>
        <button v-if="isAuthenticated" @click="logoutHandler" class="btn btn-outline-light">Logout</button>
      </div>
    </div>
  </nav>
</template>

<script>
import { useUserStore } from '@/stores/User';
import { useStore } from '@/stores/customer';
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

export default {
  name: 'Navigation',
  setup() {
    const userStore = useUserStore();
    const customerStore = useStore();
    const store = useStore();
    const route = useRoute();
    const router = useRouter();
    const hideNavBar = ref(false);
    const isAtmLogin = ref(false);

    watch(
        () => route.path,
        (newPath) => {
          hideNavBar.value = newPath === '/pending-approval';
          isAtmLogin.value = newPath.startsWith('/atm');
        },
        { immediate: true }
    );

    const AtmLogout = () => {
      store.logout();
      router.push('/');
    };

    return { store, userStore, customerStore, hideNavBar, isAtmLogin, route, router, AtmLogout };
  },
  computed: {
    isAuthenticated() {
      return this.userStore.isAuthenticated;
    },
    isEmployee() {
      return this.userStore.isEmployee;
    },
    isCustomer() {
      console.log('userStore user:', this.userStore.user);
      return this.userStore.isCustomer;
    },
    userName() {
      return this.userStore.userName;
    },
    isAtmUser() {
      return this?.customerStore?.isAuthenticated;
    }
  },
  methods: {
    logoutHandler() {
      this.userStore.logout();
      this.$router.push('/');
    },
    handleRegularLoginClick() {
      if (this.userStore.isLoggedIn) {
        this.userStore.logout();
      }
      this.$router.push('/login');
    }
  },
};
</script>

<style scoped></style>