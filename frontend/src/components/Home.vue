<template>
  <section>
    <div class="container py-5">
      <h2 class="welcome-text mb-5 text-center">Welcome to The ABN AMRO Bank</h2>

      <div class="row gy-4">
        <!-- Register Box -->
        <div class="col-md-4">
          <div class="card register-box shadow-sm h-100">
            <div class="card-body d-flex flex-column justify-content-between">
              <h5 class="card-title text-primary">Become a member of ABN AMRO community</h5>
              <p class="card-text">Create an account and enjoy our digital banking experience.</p>
              <router-link to="/register" class="btn btn-custom">Register</router-link>
            </div>
          </div>
        </div>

        <!-- Offerings -->
        <div class="col-md-8">
          <div class="card what-we-offer-box shadow-sm">
            <div class="card-body">
              <h2 class="card-title text-center text-secondary fw-bold mb-4">What do we offer?</h2>
              <div class="row g-4">
                <div class="col-md-6">
                  <h4 class="text-primary text-center">Saving Account</h4>
                  <div class="image-container position-relative rounded overflow-hidden">
                    <img src="../assets/images/saving.jpg" alt="Saving Account" class="img-fluid w-100 rounded">
                    <div class="text-box bg-info text-white p-3">
                      <p class="mb-0">Enjoy unlimited transactions, overdrafts, and more with our Saving Account.</p>
                    </div>
                  </div>
                </div>
                <div class="col-md-6">
                  <h4 class="text-primary text-center">Current Account</h4>
                  <div class="image-container position-relative rounded overflow-hidden">
                    <img src="../assets/images/current.png" alt="Current Account" class="img-fluid w-100 rounded">
                    <div class="text-box bg-info text-white p-3">
                      <p class="mb-0">Simplify your daily banking with powerful tools via our Current Account.</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Login Box -->
      <div class="row mt-5 justify-content-center">
        <div class="col-md-6 text-center login-box shadow-sm">
          <p class="mb-0">Already have an account? <router-link to="/login" class="login-link">Login</router-link></p>
        </div>
      </div>
    </div>
  </section>
</template>

<script src="../assets/component-code/Home.js"></script>

<style scoped src="../assets/component-code/Home.css"></style>
