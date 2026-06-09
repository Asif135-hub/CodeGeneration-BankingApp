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
      <div v-if="customers.length > 0">
        <table>
          <thead>
            <tr>
              <th>Full-name</th>
              <th>Customer status</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(customer, index) in customers" :key="index">
              <td>{{ customer.name }}</td>
              <td>{{ customer.status }}</td>
              <td>
                <button @click="openModal(customer.userId)">
                  Approve Customer
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <p class="transactionHead" v-else>No customers available</p>

      <!-- Bootstrap Modal -->
      <div
        class="modal fade"
        id="approveModal"
        tabindex="-1"
        role="dialog"
        aria-labelledby="modalLabel"
        aria-hidden="true"
        ref="modal"
      >
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="modalLabel">
                Approve Account for {{ modalCustomer?.name }}
              </h5>
              <button
                type="button"
                class="close"
                data-dismiss="modal"
                aria-label="Close"
              >
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <form @submit.prevent="approveCustomer(modalCustomer.userId)">
                <div class="form-group">
                  <label for="dailyLimit">Daily Limit:</label>
                  <input
                    type="number"
                    class="form-control"
                    id="dailyLimit"
                    v-model="form.dailyLimit"
                  />
                </div>
                <div class="form-group">
                  <label for="absoluteLimitForCurrent"
                    >Absolute Limit for Current:</label
                  >
                  <input
                    type="number"
                    class="form-control"
                    id="absoluteLimitForCurrent"
                    v-model="form.absoluteLimitForCurrent"
                  />
                </div>
                <div class="form-group">
                  <label for="absoluteLimitForSaving"
                    >Absolute Limit for Saving:</label
                  >
                  <input
                    type="number"
                    class="form-control"
                    id="absoluteLimitForSaving"
                    v-model="form.absoluteLimitForSaving"
                  />
                </div>
                <button type="submit" class="btn btn-success">Approve</button>
                <button
                  type="button"
                  class="btn btn-secondary"
                  data-dismiss="modal"
                >
                  Cancel
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import axios from "axios";
import { onMounted, ref, reactive } from "vue";

export default {
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
  },
  setup() {
    const customers = ref([]);
    const modalCustomer = ref(null);
    const form = reactive({
      dailyLimit: 0,
      absoluteLimitForCurrent: 0,
      absoluteLimitForSaving: 0,
    });

    onMounted(async () => {
      try {
        const response = await axios.get(
          "http://localhost:8080/employees/customers-without-accounts"
        );
        if (response.data && response.data.length) {
          customers.value = response.data.map((customer) => ({
            ...customer,
            age: calculateAge(customer.dob), 
          }));
        } else {
          console.error("No customers found");
        }
      } catch (error) {
        console.error("Failed to fetch customer accounts:", error);
      }
    });

    function calculateAge(dob) {
      const birthDate = new Date(dob);
      const difference = Date.now() - birthDate.getTime();
      const ageDate = new Date(difference);
      return Math.abs(ageDate.getUTCFullYear() - 1970);
    }

    function openModal(customerId) {
      const customer = customers.value.find((c) => c.userId === customerId);
      modalCustomer.value = customer;
      if (customer) {
        $("#approveModal").modal("show");
      }
    }

    async function approveCustomer(customerId) {
      const data = {
        dailyLimit: form.dailyLimit,
        absoluteLimitForCurrent: form.absoluteLimitForCurrent,
        absoluteLimitForSaving: form.absoluteLimitForSaving,
      };
      try {
        const response = await axios.put(
          `http://localhost:8080/employees/customers-without-accounts/${customerId}/approve-signup`,
          data
        );
        if (response.status === 200) {
          customers.value = customers.value.filter(
            (customer) => customer.userId !== customerId
          );
          $("#approveModal").modal("hide");
          alert("Customer approved and account created.");
        }
      } catch (error) {
        console.error("Failed to approve customer:", error);
        alert("Failed to approve customer.");
      }
    }

    return { customers, modalCustomer, form, openModal, approveCustomer };
  },
};
</script>



<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

th {
  background-color: #f2f2f2;
}

tr:nth-child(even) {
  background-color: #f2f2f2;
}

tr:hover {
  background-color: #f5f5f5;
}

/* Adjusted modal styles to avoid overriding Bootstrap's modal styling */
.modal-content {
  border-radius: 0.3rem;
}

.modal-header,
.modal-body,
.modal-footer {
  padding: 1rem;
}

/* Style adjustments for form elements within the modal */
.modal form {
  margin-top: 1rem; /* Ensures spacing inside the modal */
}

.modal label {
  margin-top: 10px;
}

.modal input {
  margin-top: 5px;
  padding: 5px;
  border: 1px solid #ddd;
  border-radius: 3px;
}

.modal button {
  margin-top: 10px;
  padding: 5px 10px;
  border: none;
  border-radius: 3px;
  background-color: #007bff;
  color: white;
  cursor: pointer;
}

.modal button + button {
  margin-left: 10px;
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
</style>
