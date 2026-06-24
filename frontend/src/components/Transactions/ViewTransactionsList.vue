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
      <h1 class="transactionHead">All Transactions</h1>
      <div class="main" v-if="transactions.length > 0">
        <table>
          <thead>
            <tr>
              <th>From Account</th>
              <th>To Account</th>
              <th>Transfer Amount</th>
              <th>Timestamp</th>
              <th>User Initiating Transfer</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(transaction, index) in transactions" :key="index">
              <td>{{ transaction.fromAccountIban }}</td>
              <td>{{ transaction.toAccountIban }}</td>
              <td>{{ transaction.transferAmount }}</td>
              <td>{{ transaction.currentTime }}</td>
              <td>
                {{
                  `${transaction.firstName} ${transaction.lastName} / ${transaction.initiatedBy}`
                }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <p v-else>No transactions available</p>
    </div>
  </div>
</template>

<script src="../../assets/component-code/Transactions/ViewTransactionsList.js"></script>


<style scoped src="../../assets/component-code/Transactions/ViewTransactionsList.css"></style>

