<template>
  <section>
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-6 d-flex align-items-center justify-content-center">
          <div class="text-center">
            <h1 class="welcome-text">ABN AMRO Bank ATM</h1>
            <img src="../../../assets/images/banking.png" class="img-fluid mt-3" style="width:60%" />
          </div>
        </div>
        <div class="col-md-6 d-flex align-items-center justify-content-center">
          <div class="login-form">
            <h2 class="text-center"><strong>ATM Login</strong></h2>
            <form @submit.prevent="handleSubmit">
              <div class="mb-3">
                <label class="form-label">Email</label>
                <input v-model="email" type="email" class="form-control" required />
              </div>
              <div class="mb-3">
                <label class="form-label">Password</label>
                <input v-model="password" type="password" class="form-control" required />
              </div>
              <button type="submit" class="btn btn-primary btn-lg w-100">Login</button>
              <router-link to="/login" class="btn btn-link w-100 mt-2">Back to Bank Login</router-link>
              <div v-if="loginError" class="text-danger mt-3">{{ loginError }}</div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import { useStore } from '@/stores/customer.js';
import { useRouter } from 'vue-router';

export default {
  name: 'ATMLogin',
  data() { return { email: '', password: '', loginError: null }; },
  setup() { return { store: useStore(), router: useRouter() }; },
  methods: {
    async handleSubmit() {
      this.loginError = null;
      try {
        await this.store.login(this.email, this.password);
        this.router.push('/atm');
      } catch (e) { this.loginError = e.message; }
    },
  },
};
</script>

<style scoped>
.welcome-text { color: #60BFC1; font-size: 50px; font-weight: bold; }
.login-form { background-color: #fff; padding: 40px; border-radius: 5px; width: 80%; max-width: 400px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
.btn-primary { background-color: #00585a; color: #fff; font-weight: bold; border: none; }
.btn-primary:hover { background-color: #3a7e80; }
</style>
