<template>
  <div>
    <h2 class="transactionHead">Transactions for {{ customerName }}</h2>
    <div v-if="transactions.length > 0">
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
    <button class="backBtn" @click="goBack">Back to Customers</button>
  </div>
</template>
  <script src="../../assets/component-code/Customers/CustomerTransaction.js"></script>
  
<style scoped src="../../assets/component-code/Customers/CustomerTransaction.css"></style>
  