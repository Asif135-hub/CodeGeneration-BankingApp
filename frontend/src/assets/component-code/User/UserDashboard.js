import { useUserStore } from '@/stores/User';

export default {
  name: 'CustomerDashboard',
  data() {
    return {
      sidebarCollapsed: false,
      menuItems: [
        { path: '/customerDashboard', icon: 'ðŸ ', label: 'Home' },
        { path: '/transaction-history', icon: 'ðŸ“‹', label: 'Transaction History' },
        { path: '/fund-transfer', icon: 'âž¡ï¸', label: 'Transfer to Others' },
        { path: '/fund-transfer-own', icon: 'ðŸ”„', label: 'Transfer Own Accounts' },
      ],
    };
  },
  setup() {
    return { userStore: useUserStore() };
  },
  computed: {
    initials() {
      const name = this.userStore.userName || '';
      return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2) || '?';
    },
  },
  methods: {
    logoutHandler() {
      this.userStore.logout();
      this.$router.push('/');
    },
  },
};
