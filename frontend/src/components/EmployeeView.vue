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

<script>
export default {
  methods: {
    goToCustomers() {
      this.$router.push({ path: '/employees/customer-accounts' });
    },
    goToTransactions() {
      this.$router.push({ path: '/transactions' });
    },
    goCustomersWithoutAccounts() {
      this.$router.push({ path: '/employees/customers-without-accounts' });
    },
    goTransfer() {
      this.$router.push({ path: '/transfer' });
    }
  }
};
</script>


<style scoped>
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

table th, table td {
  border: 1px solid #ddd;
  padding: 8px;
}

table th {
  background-color: #f2f2f2;
}
</style>
