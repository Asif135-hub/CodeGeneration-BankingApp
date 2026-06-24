import { useStore } from '@/stores/customer.js';
import { useRouter } from 'vue-router';

export default {
  name: 'ATMLogin',
  data() { return { email: '', password: '', loginError: null }; },
  setup() { return { store: useStore(), router: useRouter() }; },
  methods: {
    async handleSubmit() {
      this.loginError = null;
      try {
        await this.store.login(this.email, this.password);
        this.router.push('/atm');
      } catch (e) { this.loginError = e.message; }
    },
  },
};
