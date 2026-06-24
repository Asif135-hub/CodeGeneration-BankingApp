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
        const res = await api.post('/customers/transactions', this.transfer);
        this.successMsg = `Transferred â‚¬${res.data.amount} successfully!`;
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
