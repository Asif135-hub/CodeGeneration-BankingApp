import api from '@/axios.js';
import { useUserStore } from '@/stores/User';

export default {
  name: 'CustomerHome',
  data() { return { accounts: [], loading: true, showAccounts: false }; },
  setup() { return { userStore: useUserStore() }; },
  async mounted() {
    try {
      const r = await api.get('/customers/accounts');
      this.accounts = r.data;
    } catch (e) { console.error(e); }
    finally { this.loading = false; }
  },
  methods: {
    formatAmount(val) {
      return Number(val).toLocaleString('nl-NL', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    },
  },
};
