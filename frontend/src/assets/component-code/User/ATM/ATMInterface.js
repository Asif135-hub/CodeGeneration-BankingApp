import { useStore } from '@/stores/customer.js';
import { useRouter } from 'vue-router';

export default {
  name: 'ATMInterface',
  data() {
    return {
      loading: false,
      txLoading: false,
      modal: { show: false, type: '', account: null, amount: null, msg: '', error: false },
    };
  },
  setup() { return { store: useStore(), router: useRouter() }; },
  async mounted() {
    this.loading = true;
    await this.store.fetchAccounts();
    this.loading = false;
  },
  methods: {
    formatAmount(val) {
      return Number(val).toLocaleString('nl-NL', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    },
    openModal(account, type) {
      this.modal = { show: true, type, account, amount: null, msg: '', error: false };
    },
    closeModal() { this.modal.show = false; },
    async submitModal() {
      if (!this.modal.amount || this.modal.amount <= 0) {
        this.modal.msg = 'Enter a valid amount'; this.modal.error = true; return;
      }
      this.txLoading = true;
      const result = this.modal.type === 'deposit'
        ? await this.store.deposit(this.modal.account.iban, this.modal.amount)
        : await this.store.withdraw(this.modal.account.iban, this.modal.amount);
      this.txLoading = false;
      if (result) { this.modal.msg = result.message || 'Success!'; this.modal.error = false; this.modal.amount = null; }
      else { this.modal.msg = 'Transaction failed'; this.modal.error = true; }
    },
    logout() { this.store.logout(); this.router.push('/atm/login'); },
  },
};
