<template>
  <section>
    <div class="container">
      <div v-if="!showSuccessMessage">
        <h1 class="text-center welcome-text">Register New Customer</h1>
        <div class="row justify-content-center">
          <div class="col-md-6">
            <form @submit.prevent="registerCustomer" class="mt-4">
              <div class="mb-3 row">
                <div class="col-md-6">
                  <label class="form-label">First Name</label>
                  <input v-model="form.firstName" type="text" class="form-control" required />
                </div>
                <div class="col-md-6">
                  <label class="form-label">Last Name</label>
                  <input v-model="form.lastName" type="text" class="form-control" required />
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label">Email</label>
                <input v-model="form.email" type="email" class="form-control" required />
              </div>
              <div class="mb-3 row">
                <div class="col-md-6">
                  <label class="form-label">Password</label>
                  <input v-model="form.password" type="password" class="form-control" required
                    title="Min 8 chars, 1 uppercase, 1 number" />
                </div>
                <div class="col-md-6">
                  <label class="form-label">Confirm Password</label>
                  <input v-model="confirmPassword" type="password" class="form-control" required />
                  <div v-if="confirmPassword && form.password !== confirmPassword" class="text-danger">Passwords do not match</div>
                </div>
              </div>
              <div class="mb-3 row">
                <div class="col-md-6">
                  <label class="form-label">BSN Number (8-9 digits)</label>
                  <input v-model="form.bsn" type="text" class="form-control" required maxlength="9" />
                </div>
                <div class="col-md-6">
                  <label class="form-label">Phone Number (e.g. +31612345678)</label>
                  <input v-model="form.phoneNumber" type="text" class="form-control"  placeholder="31612345678"/>
                </div>
              </div>
              
              <div v-if="errorMessage" class="alert alert-danger">{{ errorMessage }}</div>
              <button type="submit" class="btn btn-primary btn-lg w-100" :disabled="!passwordsMatch">Register</button>
              <button type="button" class="btn btn-secondary btn-lg w-100 mt-2" @click="$router.push('/login')">Back to Login</button>
            </form>
          </div>
        </div>
      </div>
      <div v-if="showSuccessMessage" class="alert alert-success mt-4">
        <p style="font-size:20px">Registration successful! Your account is pending approval. You can login once approved.</p>
        <button class="btn btn-primary" @click="$router.push('/login')">Back to Login</button>
      </div>
    </div>
  </section>
</template>

<script src="../assets/component-code/Register.js"></script>

<style scoped src="../assets/component-code/Register.css"></style>
