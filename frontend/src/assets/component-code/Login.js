import { useUserStore } from '@/stores/User';
import { useRouter } from 'vue-router';

export default {
  name: 'Login',
  data() {
    return { username: '', password: '', errors: { username: null, password: null, other: null } };
  },
  setup() {
    return { router: useRouter(), userStore: useUserStore() };
  },
  methods: {
    async validateLogin() {
      this.errors = { username: null, password: null, other: null };
      if (!this.username.trim()) { this.errors.username = 'Email is required'; return; }
      if (!this.password.trim()) { this.errors.password = 'Password is required'; return; }
      const result = await this.userStore.login({ username: this.username, password: this.password });
      if (result.success) {
        this.router.push(result.user.role === 'CUSTOMER' ? '/customerDashboard' : '/');
      } else {
        this.errors.other = result.message;
      }
    },
  },
};
