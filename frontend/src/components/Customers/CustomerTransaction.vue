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
  <script>
import axios from "axios";
import { ref, onMounted } from "vue";

export default {
  props: {
    customerId: {
      type: String,
      required: true,
    },
    customerName: {
      type: String,
      required: true,
    },
  },
  setup(props, { emit }) {
    const transactions = ref([]);

    onMounted(async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/customers/${props.customerId}/transactions`
        );
        transactions.value = response.data;
      } catch (error) {
        console.error("Failed to fetch transactions:", error);
      }
    });

    function goBack() {
      emit("back");
    }

    return { transactions, goBack };
  },
};
</script>
  
<style scoped>
.backBtn{
    margin: auto 20px;
}
.transactionHead {
  margin: auto 20px;
}
table {
  border-collapse: collapse;
  width: 90%;
  margin: 40px 20px;
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
</style>
  