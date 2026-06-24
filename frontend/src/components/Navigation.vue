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

<script src="../assets/component-code/Navigation.js"></script>

