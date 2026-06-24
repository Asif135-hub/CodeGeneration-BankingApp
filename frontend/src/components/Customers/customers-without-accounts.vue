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
<script src="../../assets/component-code/Customers/customers-without-accounts.js"></script>



<style scoped src="../../assets/component-code/Customers/customers-without-accounts.css"></style>
