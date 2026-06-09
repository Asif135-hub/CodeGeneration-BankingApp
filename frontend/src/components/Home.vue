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

<script>
export default {
  name: "Home",
};
</script>

<style scoped>
/* Main Header */
.welcome-text {
  font-size: 3.5rem;
  font-weight: 700;
  color: #007b8f;
}

/* Register Card */
.register-box {
  border: 2px solid #e0f7fa;
  border-radius: 15px;
  transition: transform 0.3s;
  background-color: #f9f9f9;
}
.register-box:hover {
  transform: translateY(-5px);
}
.register-box .card-title {
  font-size: 1.5rem;
  font-weight: 600;
}
.btn-custom {
  background-color: #007b8f;
  color: white;
  font-size: 1.1rem;
  padding: 10px 20px;
  border-radius: 8px;
}
.btn-custom:hover {
  background-color: #005f6b;
}

/* What We Offer */
.what-we-offer-box {
  border: 2px solid #e0f2f1;
  border-radius: 15px;
  padding: 20px;
  background-color: #ffffff;
}
.what-we-offer-box .card-title {
  font-size: 2rem;
}

/* Offer Images */
.image-container {
  position: relative;
}
.text-box {
  position: absolute;
  bottom: 0;
  width: 100%;
  background-color: rgba(0, 123, 143, 0.9);
  color: white;
  padding: 10px;
  font-size: 0.95rem;
  text-align: center;
}

/* Login Box */
.login-box {
  background-color: #f5f5f5;
  padding: 20px 30px;
  border-radius: 15px;
  font-size: 1.25rem;
}
.login-link {
  color: #007b8f;
  font-weight: bold;
}
.login-link:hover {
  color: #005f6b;
  text-decoration: underline;
}
</style>
