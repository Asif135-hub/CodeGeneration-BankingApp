<template>
  <div class="m-3">
    <h2>Transfer to Another Customer</h2>

    <form @submit.prevent="searchCustomer()" class="row g-3 mb-3">
      <div class="col-md-4">
        <label class="form-label">First Name</label>
        <input type="text" class="form-control" v-model="search.firstName">
      </div>
      <div class="col-md-4">
        <label class="form-label">Last Name</label>
        <input type="text" class="form-control" v-model="search.lastName">
      </div>
      <div class="col-md-4 d-flex align-items-end">
        <button type="submit" class="btn btn-secondary w-100">Search Customer</button>
      </div>
    </form>

    <div v-if="searchResults.length > 0" class="mb-3">
      <h5>Search Results</h5>
      <div v-for="r in searchResults" :key="r.iban" class="d-flex align-items-center gap-2 mb-1">
        <span>{{ r.firstName }} {{ r.lastName }} â€” <strong>{{ r.iban }}</strong></span>
        <button class="btn btn-sm btn-outline-primary" @click="transfer.toIban = r.iban">Use this IBAN</button>
      </div>
    </div>
    <div v-if="searchError" class="alert alert-warning">{{ searchError }}</div>

    <form @submit.prevent="transferFunds" class="row g-3 mt-2">
      <div class="col-md-4">
        <label class="form-label">From IBAN</label>
        <select class="form-select" v-model="transfer.fromIban" required>
          <option v-for="acc in myAccounts" :key="acc.id" :value="acc.iban">{{ acc.iban }} (â‚¬{{ acc.balance }})</option>
        </select>
      </div>
      <div class="col-md-4">
        <label class="form-label">To IBAN</label>
        <input type="text" class="form-control" v-model="transfer.toIban" required placeholder="NL91ABNA0417164300" />
      </div>
      <div class="col-md-2">
        <label class="form-label">Amount (â‚¬)</label>
        <input type="number" class="form-control" v-model.number="transfer.amount" min="0.01" step="0.01" required>
      </div>
      <div class="col-md-2 d-flex align-items-end">
        <button type="submit" class="btn btn-primary w-100">Transfer</button>
      </div>
    </form>

    <div v-if="successMsg" class="alert alert-success mt-3">{{ successMsg }}</div>
    <div v-if="transferError" class="alert alert-danger mt-3">{{ transferError }}</div>
  </div>
</template>

<script src="../../assets/component-code/User/FundTransfer.js"></script>
