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

<script src="../../../assets/component-code/User/ATM/ATMLogin.js"></script>

<style scoped src="../../../assets/component-code/User/ATM/ATMLogin.css"></style>
