export default {
  methods: {
    goToCustomers() {
      this.$router.push({ path: '/employees/customer-accounts' });
    },
    goToTransactions() {
      this.$router.push({ path: '/transactions' });
    },
    goCustomersWithoutAccounts() {
      this.$router.push({ path: '/employees/customers-without-accounts' });
    },
    goTransfer() {
      this.$router.push({ path: '/transfer' });
    }
  }
};
