<template>
  <div id="app">
    <div class="sidebar">
      <h3>Employee Dashboard</h3>
      <ul>
        <li @click="goToCustomers">Customers</li>
        <li @click="goToTransactions">All Transactions</li>
        <li @click="goCustomersWithoutAccounts">Customers-without-accounts</li>
        <li @click="goTransfer">Transfer</li>
      </ul>
    </div>
    <div class="main">
      <router-view></router-view>
    </div>
  </div>
</template>

<script src="../assets/component-code/EmployeeView.js"></script>


<style scoped src="../assets/component-code/EmployeeView.css"></style>
