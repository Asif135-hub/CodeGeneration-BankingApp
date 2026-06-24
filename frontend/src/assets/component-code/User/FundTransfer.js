import api from '@/axios.js';

export default {
  name: 'FundTransfer',
  data() {
    return {
      search: { firstName: '', lastName: '' },
      searchResults: [], searchError: '',
      myAccounts: [],
      transfer: { fromIban: '', toIban: '', amount: null },
      successMsg: '', transferError: '',
    };
  },
  methods: {
    async searchCustomer() {

       if (!this.search.firstName && !this.search.lastName) {
        this.searchError = 'Please enter a first or last name.';
        return;
      }

      this.searchError = ''; this.searchResults = [];
      try {
        const res = await api.get('/customers/search', { params: this.search });
        if (res.data.length === 0) this.searchError = 'No customers found';
        else this.searchResults = res.data;
      } catch (e) { this.searchError = 'Search failed'; }
    },
    async transferFunds() {
      this.successMsg = ''; this.transferError = '';
      try {
        const res = await api.post('/customers/transactions', this.transfer);
        this.successMsg = `Transferred â‚¬${res.data.amount} successfully!`;
        await this.loadAccounts();
      } catch (e) {
        this.transferError = e.response?.data?.message || 'Transfer failed';
      }
    },
    async loadAccounts() {
      const res = await api.get('/customers/accounts');
      this.myAccounts = res.data;
      if (this.myAccounts.length) this.transfer.fromIban = this.myAccounts[0].iban;
    },
  },
  mounted() { this.loadAccounts(); },
};
