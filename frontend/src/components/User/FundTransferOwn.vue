<template>
  <div class="m-3">
    <h2>Transfer Between Your Accounts</h2>
    <form @submit.prevent="transferFunds" class="row g-3">
      <div class="col-md-5">
        <label class="form-label">From Account</label>
        <select class="form-select" v-model="transfer.fromIban" required>
          <option v-for="acc in accounts" :key="acc.id" :value="acc.iban">{{ acc.iban }} (€{{ acc.balance }})</option>
        </select>
      </div>
      <div class="col-md-5">
        <label class="form-label">To Account</label>
        <select class="form-select" v-model="transfer.toIban" required>
          <option v-for="acc in accounts" :key="acc.id" :value="acc.iban">{{ acc.iban }} (€{{ acc.balance }})</option>
        </select>
      </div>
      <div class="col-md-2">
        <label class="form-label">Amount (€)</label>
        <input type="number" class="form-control" v-model.number="transfer.amount" min="0.01" step="0.01" required>
      </div>
      <div class="col-12">
        <button type="submit" class="btn btn-primary">Transfer</button>
      </div>
    </form>
    <div v-if="successMsg" class="alert alert-success mt-3">{{ successMsg }}</div>
    <div v-if="errorMsg" class="alert alert-danger mt-3">{{ errorMsg }}</div>
  </div>
</template>

<script>
import api from '@/axios.js';

export default {
  name: 'FundTransferOwn',
  data() {
    return { accounts: [], transfer: { fromIban: '', toIban: '', amount: null }, successMsg: '', errorMsg: '' };
  },
  methods: {
    async transferFunds() {
      this.successMsg = ''; this.errorMsg = '';
      if (this.transfer.fromIban === this.transfer.toIban) { this.errorMsg = 'From and To accounts must be different'; return; }
      try {
        const res = await api.post('/transactions', this.transfer);
        this.successMsg = `Transferred €${res.data.amount} successfully!`;
        await this.loadAccounts();
      } catch (e) { this.errorMsg = e.response?.data?.message || 'Transfer failed'; }
    },
    async loadAccounts() {
      const res = await api.get('/customers/accounts');
      this.accounts = res.data;
    },
  },
  mounted() { this.loadAccounts(); },
};
</script>
