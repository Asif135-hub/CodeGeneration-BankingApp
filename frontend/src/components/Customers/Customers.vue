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
      <h1 class="transactionHead">Customers</h1>
      <div v-if="customers.length > 0 && selectedCustomerIndex === null">
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Account Number</th>
              <th>Account Type</th>
              <th>Daily Limit</th>
              <th>Absolute Limit</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(customer, index) in customers" :key="index">
              <td>{{ customer.customerName }}</td>
              <td>{{ customer.IBAN }}</td>
              <td>{{customer.accountType}}</td>
              <td>{{ customer.dailyLimit }}</td>
              <td>{{ customer.absoluteLimit }}</td>
              <td>
                <button class="btn" @click="viewCustomerDetails(index)">Transactions</button>
                <button class="btn" @click.stop="openDailyLimitForm(customer.accountId)">Edit Daily Limit</button>
                <button class="btn" @click.stop="openAbsoluteLimitForm(customer.accountId)">Edit Absolute Limit</button>
                <button class="btn" @click.stop="closeAccount(customer.customerId)">Close Account</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <p v-else-if="customers.length === 0">No customers available</p>
      <CustomerTransactions
        v-if="selectedCustomerIndex !== null"
        :customerId="customers[selectedCustomerIndex].customerId"
        :customerName="customers[selectedCustomerIndex].customerName"
        @back="selectedCustomerIndex = null"
      />
      <div v-if="showDailyLimitForm">
        <h2 class="transactionHead">Update Daily Limit</h2>
        <form class="transactionHead" @submit.prevent="updateDailyLimit">
          <label for="DailyLimit">New Daily Limit:</label>
          <input type="number" id="DailyLimit" v-model="newDailyLimit" required />
          <button type="submit">Update</button>
        </form>
      </div>
      <div v-if="showAbsoluteLimitForm">
        <h2 class="transactionHead">Update Absolute Limit</h2>
        <form class="transactionHead" @submit.prevent="updateAbsoluteLimit">
          <label for="AbsoluteLimit">New Absolute Limit:</label>
          <input type="number" id="AbsoluteLimit" v-model="newAbsoluteLimit" required />
          <button type="submit">Update</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script src="../../assets/component-code/Customers/Customers.js"></script>

<style scoped src="../../assets/component-code/Customers/Customers.css"></style>
