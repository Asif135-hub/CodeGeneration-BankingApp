<template>
  <section>
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-6 d-flex align-items-center justify-content-center">
          <div class="text-center">
            <h1 class="welcome-text">Welcome to ABN AMRO Bank</h1>
            <img src="../assets/images/banking.png" alt="Bank Image" class="rounded img-fluid" />
          </div>
        </div>
        <div class="col-md-6 d-flex align-items-center justify-content-center">
          <div class="login-form d-flex flex-column justify-content-center">
            <h2 class="text-center"><strong>Login</strong></h2>
            <form @submit.prevent="validateLogin">
              <div class="mb-3">
                <label for="inputUsername" class="form-label">Email</label>
                <input v-model="username" id="inputUsername" type="email" class="form-control" required />
                <div v-if="errors.username" class="text-danger">{{ errors.username }}</div>
              </div>
              <div class="mb-3">
                <label for="inputPassword" class="form-label">Password</label>
                <input v-model="password" id="inputPassword" type="password" class="form-control" required />
                <div v-if="errors.password" class="text-danger">{{ errors.password }}</div>
              </div>
              <button type="submit" class="btn btn-primary btn-lg w-100">Login</button>
              <router-link to="/register" class="btn btn-link w-100 mt-2">Don't have an account? Register!</router-link>
              <router-link to="/atm/login" class="btn btn-link w-100">Go to ATM</router-link>
              <div v-if="errors.other" class="text-danger mt-3">{{ errors.other }}</div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import { useUserStore } from '@/stores/User';
import { useRouter } from 'vue-router';

export default {
  name: 'Login',
  data() {
    return { username: '', password: '', errors: { username: null, password: null, other: null } };
  },
  setup() {
    return { router: useRouter(), userStore: useUserStore() };
  },
  methods: {
    async validateLogin() {
      this.errors = { username: null, password: null, other: null };
      if (!this.username.trim()) { this.errors.username = 'Email is required'; return; }
      if (!this.password.trim()) { this.errors.password = 'Password is required'; return; }
      const result = await this.userStore.login({ username: this.username, password: this.password });
      if (result.success) {
        this.router.push(result.user.role === 'CUSTOMER' ? '/customerDashboard' : '/');
      } else {
        this.errors.other = result.message;
      }
    },
  },
};
</script>

<style scoped>
.welcome-text { color: #60BFC1; font-size: 60px; font-weight: bold; }
.login-form { background-color: #fff; padding: 40px; border-radius: 20px; width: 80%; max-width: 400px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
.btn-primary { background-color: #60BFC1; color: #fff; font-weight: bold; border: none; }
.btn-primary:hover { background-color: #3a7e80; }
</style>
