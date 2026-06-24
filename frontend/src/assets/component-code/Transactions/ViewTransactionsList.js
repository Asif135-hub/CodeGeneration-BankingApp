import axios from "axios";

export default {
  data() {
    return {
      transactions: [],
    };
  },
  mounted() {
    this.fetchTransactions();
  },
  methods: {
    goToCustomers() {
      this.$router.push({ path: "/employees/customer-accounts" });
    },
    goToTransactions() {
      this.$router.push({ path: "/transactions" });
    },
    goCustomersWithoutAccounts() {
      this.$router.push({ path: "/employees/customers-without-accounts" });
    },
    goTransfer() {
      this.$router.push({ path: "/transfer" });
    },
    fetchTransactions() {

      axios
          .get("http://localhost:8080/customers/transactions")
          .then((response) => {
            this.transactions = response.data;
          })
          .catch((error) => {
            console.error("There was a problem with the Axios request:", error);
          });
    },
  },
};
