<template>
  <div class="m-3">
    <h2>Transaction History</h2>
    <form @submit.prevent="fetchTransactions()"
 class="row g-3 mb-3">
      <div class="col-md-3">
        <label class="form-label">Start Date</label>
        <input type="date" class="form-control" v-model="filters.start">
      </div>
      <div class="col-md-3">
        <label class="form-label">End Date</label>
        <input type="date" class="form-control" v-model="filters.end">
      </div>
      <div class="col-md-3">
        <label class="form-label">IBAN</label>
        <input type="text" class="form-control" v-model="filters.fromIban" placeholder="Filter by IBAN">
      </div>
      <div class="col-md-3">
        <label class="form-label">Amount</label>
        <input type="number" class="form-control" v-model.number="filters.amount" min="0">
      </div>
      <div class="col-md-3">
        <label class="form-label">Amount Filter</label>
        <select class="form-select" v-model="filters.amountOp">
          <option value="EqualTo">Equal To</option>
          <option value="GreaterThan">Greater Than</option>
          <option value="LessThan">Less Than</option>
        </select>
      </div>
      <div class="col-md-2 d-flex align-items-end">
        <button type="submit" class="btn btn-primary w-100">Filter</button>
      </div>
    </form>

    <div v-if="loading" class="text-center"><div class="spinner-border"></div></div>
    <table v-else class="table table-striped table-hover">
      <thead>
        <tr>
          <th>Date</th><th>From IBAN</th><th>To IBAN</th><th>Amount</th><th>Type</th><th>Initiated By</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="transactions.length === 0"><td colspan="6" class="text-center">No transactions found</td></tr>
        <tr v-for="tx in transactions" :key="tx.id">
          <td>{{ new Date(tx.timestamp).toLocaleString() }}</td>
          <td>{{ tx.fromIban || '-' }}</td>
          <td>{{ tx.toIban || '-' }}</td>
          <td>â‚¬{{ tx.amount }}</td>
          <td><span :class="badgeClass(tx.type)" class="badge">{{ tx.type }}</span></td>
          <td>{{ tx.initiatedBy }}</td>
        </tr>
      </tbody>
    </table>

    <div class="d-flex gap-2 mt-2">
      <button class="btn btn-outline-secondary" :disabled="page === 0" @click="page--; fetchTransactions(skipValidation = true)">â† Prev</button>
      <span class="align-self-center">Page {{ page + 1 }}</span>
      <button class="btn btn-outline-secondary" :disabled="!hasMore" @click="page++; fetchTransactions(skipValidation = true)">Next â†’</button>
    </div>
  </div>
</template>

<script src="../../assets/component-code/User/TransactionHistory.js"></script>
