<template>
  <div class="pending-approval">
    <h1 class="welcome-text">Welcome to FAFA Bank</h1>
    <h2>Your account is pending approval</h2>
    <p>Please wait for an administrator to approve your account. You will be able to use our services once your account is approved.</p>
    <button @click="logoutHandler" class="btn btn-outline-dark">Logout</button>
  </div>
</template>

<script src="../assets/component-code/PendingApproval.js"></script>


<style scoped src="../assets/component-code/PendingApproval.css"></style>
