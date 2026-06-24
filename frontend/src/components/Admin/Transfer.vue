<template>
  <div id="app">
    <div class="sidebar">
      <h3>Employee Dashboard</h3>
      <ul>
        <li @click="goToCustomers">Customers</li>
        <li @click="goToTransactions">All Transactions</li>
        <li @click="goCustomersWithoutAccounts">Customers without accounts</li>
        <li @click="goTransfer">Transfer</li>
      </ul>
    </div>

    <div class="main">
      <div class="transfer-form">
        <h1>Transfer</h1>
        <form @submit.prevent="transferMoney">
          <div class="form-group">
            <label for="fromAccountIban">Source Account:</label>
            <input type="text" id="sourceAccount" v-model="fromAccountIban" required />
          </div>
          <div class="form-group">
            <label for="toAccountIban">Destination Account:</label>
            <input type="text" id="destinationAccount" v-model="toAccountIban" required />
          </div>
          <div class="form-group">
            <label for="amount">Amount:</label>
            <input type="number" id="transferAmount" v-model="transferAmount" required />
          </div>
          <button type="submit">Transfer</button>
        </form>
      </div>
      <router-view></router-view>
    </div>
  </div>
</template>

<script src="../../assets/component-code/Admin/Transfer.js"></script>

<style scoped src="../../assets/component-code/Admin/Transfer.css"></style>
