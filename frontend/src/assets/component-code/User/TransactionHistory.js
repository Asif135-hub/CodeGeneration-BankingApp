import api from '@/axios.js';

export default {
  name: 'TransactionHistory',
  data() {
    return {
      filters: { start: '', end: '', fromIban: '', amount: null, amountOp: '' },
      transactions: [], loading: false, page: 0, hasMore: false,
    };
  },
  methods: {
    async fetchTransactions(skipValidation = false) {

      if (
        !skipValidation &&
        !this.filters.start &&
        !this.filters.end &&
        !this.filters.fromIban &&
        (this.filters.amount === null || this.filters.amount === '') &&
        !this.filters.amountOp
      ) {
        alert('Please enter at least one filter before searching.');
        return;
      }


      this.loading = true;
      try {
        const params = { page: this.page, size: 10, sort: 'Descending' };
        if (this.filters.start) params.start = this.filters.start;
        if (this.filters.end) params.end = this.filters.end;
        if (this.filters.fromIban) params.fromIban = this.filters.fromIban;
        if (this.filters.amount) params.amount = this.filters.amount;
        if (this.filters.amountOp) params.amountOp = this.filters.amountOp;
        const response = await api.get('/customers/transactions', { params });
        this.transactions = response.data.content;
        this.hasMore = !response.data.last;
      } catch (e) {
        console.error(e);
      } finally {
        this.loading = false;
      }
    },
    badgeClass(type) {
      return { 'bg-success': type === 'DEPOSIT', 'bg-danger': type === 'WITHDRAW', 'bg-primary': type === 'TRANSFER' };
    }
  },
  mounted() { this.fetchTransactions(true); },
};
