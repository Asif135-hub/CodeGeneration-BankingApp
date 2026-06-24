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

<script src="../assets/component-code/Login.js"></script>

<style scoped src="../assets/component-code/Login.css"></style>
