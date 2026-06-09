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

<script>
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import CustomerTransactions from './CustomerTransaction.vue';

export default {
  components: {
    CustomerTransactions,
  },
  setup() {
    const router = useRouter();
    const customers = ref([]);
    const selectedCustomerIndex = ref(null);
    const showDailyLimitForm = ref(false);
    const showAbsoluteLimitForm = ref(false);
    const accountIdToUpdate = ref(null);
    const newDailyLimit = ref(null);
    const newAbsoluteLimit = ref(null);

    onMounted(() => {
      fetchCustomers();
    });

    const fetchCustomers = async () => {
      try {
        const response = await axios.get("http://localhost:8080/employees/customer-accounts");
        if (response.data && response.data.length) {
          customers.value = response.data;
        } else {
          console.error("No customers found");
        }
      } catch (error) {
        console.error("Failed to fetch customer accounts:", error);
      }
    };

    const viewCustomerDetails = (index) => {
      selectedCustomerIndex.value = index;
    };

    const openDailyLimitForm = (accountId) => {
      accountIdToUpdate.value = accountId;
      showDailyLimitForm.value = true;
      showAbsoluteLimitForm.value = false; 
    };

    const openAbsoluteLimitForm = (accountId) => {
      accountIdToUpdate.value = accountId;
      showAbsoluteLimitForm.value = true;
      showDailyLimitForm.value = false;  
    };

    const updateDailyLimit = async () => {
      try {
        await axios.put("http://localhost:8080/employees/customer-accounts", {
          accountId: accountIdToUpdate.value,
          dailyLimit: newDailyLimit.value,
        });
        alert("Daily limit updated successfully.");
        showDailyLimitForm.value = false;
        fetchCustomers(); 
      } catch (error) {
        alert("Failed to update daily limit: " + error.response.data);
      }
    };

    const updateAbsoluteLimit = async () => {
      try {
        await axios.put("http://localhost:8080/employees/customer-accounts", {
          accountId: accountIdToUpdate.value,
          absoluteLimit: newAbsoluteLimit.value,
        });
        alert("Absolute limit updated successfully.");
        showAbsoluteLimitForm.value = false;
        fetchCustomers(); 
      } catch (error) {
        alert("Failed to update absolute limit: " + error.response.data);
      }
    };

    const closeAccount = async (customerId) => {
      try {
        const response = await axios.delete(`http://localhost:8080/employees/close-account/${customerId}`);
        if (response.status === 200) {
          alert("Customer account status updated to rejected.");
          customers.value = customers.value.filter(customer => customer.customerId !== customerId);
        } else {
          throw new Error('Failed to close the account with provided customerId.');
        }
      } catch (error) {
        alert("Failed to update account status: " + (error.response ? error.response.data : error.message));
      }
    };


    const goToCustomers = () => {
      router.push({ path: "/employees/customer-accounts" });
    };

    const goToTransactions = () => {
      router.push({ path: "/transactions" });
    };

    const goCustomersWithoutAccounts = () => {
      router.push({ path: "/employees/customers-without-accounts" });
    };

    const goTransfer = () => {
      router.push({ path: "/transfer" });
    };

    return {
      customers,
      selectedCustomerIndex,
      viewCustomerDetails,
      openDailyLimitForm,
      openAbsoluteLimitForm,
      showDailyLimitForm,
      showAbsoluteLimitForm,
      updateDailyLimit,
      updateAbsoluteLimit,
      newDailyLimit,
      newAbsoluteLimit,
      fetchCustomers,
      goToCustomers,
      goToTransactions,
      goCustomersWithoutAccounts,
      goTransfer,
      closeAccount,
    };
  },
};
</script>

<style scoped>
#app {
  display: flex;
}

.sidebar {
  background-color: #333;
  color: #fff;
  width: 250px;
  padding: 20px;
}

.sidebar h3 {
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

.transactionHead {
  margin: 30px 20px;
}

table {
  border-collapse: collapse;
  width: 90%;
  margin: 50px 20px;
}

th, td {
  border: 1px solid black;
  padding: 8px;
  text-align: left;
}

thead {
  background-color: #f2f2f2;
}

.btn {
  border: 1px solid black;
  background-color: #f2f2f2;
  margin: 5px 5px;
}
</style>
