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

<script>
import axios from "axios";

export default {
  data() {
    return {
      transactions: [],
    };
  },
  mounted() {
    this.fetchTransactions();
  },
  methods: {
    goToCustomers() {
      this.$router.push({ path: "/employees/customer-accounts" });
    },
    goToTransactions() {
      this.$router.push({ path: "/transactions" });
    },
    goCustomersWithoutAccounts() {
      this.$router.push({ path: "/employees/customers-without-accounts" });
    },
    goTransfer() {
      this.$router.push({ path: "/transfer" });
    },
    fetchTransactions() {

      axios
          .get("http://localhost:8080/transactions")
          .then((response) => {
            this.transactions = response.data;
          })
          .catch((error) => {
            console.error("There was a problem with the Axios request:", error);
          });
    },
  },
};
</script>


<style scoped>
.transactionHead {
  margin: auto 20px;
}

table {
  border-collapse: collapse;
  width: 90%;
  margin: auto 20px;
}

th,
td {
  border: 1px solid black;
  padding: 8px;
  text-align: left;
}

thead {
  background-color: #f2f2f2;
}

body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
}

#app {
  display: flex;
}

.sidebar {
  background-color: #333;
  color: #fff;
  width: 250px;
  padding: 20px;
}

.sidebar h2 {
  margin-bottom: 20px;
}

.sidebar ul {
  list-style: none;
  padding: 0;
}

.sidebar ul li {
  cursor: pointer;
  padding: 10px;
  margin-bottom: 5px;
}

.sidebar ul li:hover {
  background-color: #555;
}

.main {
  flex: 1;
  padding: 20px;
}

.main h1 {
  margin-bottom: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

table th,
table td {
  border: 1px solid #ddd;
  padding: 8px;
}

table th {
  background-color: #f2f2f2;
}
</style>

