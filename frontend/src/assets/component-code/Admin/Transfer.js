import axios from "axios";

export default {
  data() {
    return {
      fromAccountIban: "",
      toAccountIban: "",
      transferAmount: 0,
    };
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

    transferMoney() {
      const token = localStorage.getItem("token"); 
      if (!token) {
        alert("User is not authenticated. Please log in.");
        return;
      }

      axios
        .post(
          "http://localhost:8080/customers",
          {
            fromAccountIban: this.fromAccountIban,
            toAccountIban: this.toAccountIban,
            transferAmount: this.transferAmount,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`, 
              "Content-Type": "application/json", 
            },
          }
        )
        .then((response) => {
          alert("Transfer successful");
          this.fromAccountIban = "";
          this.toAccountIban = "";
          this.transferAmount = 0;
        })
        .catch((error) => {
          console.error("There was an error with the transfer:", error);
          let errorMessage = "An unexpected error occurred. Please try again.";
          if (error.response) {
            switch (error.response.status) {
              case 400: 
                errorMessage = error.response.data;
                break;
              case 401:
                errorMessage = "Authentication failed. Please log in again.";
                break;
              case 404:
                errorMessage = "Account information could not be found.";
                break;
              case 500:
                errorMessage = "Server error occurred. Please contact support.";
                break;
              default:
                errorMessage =
                  "Unexpected error occurred: " +
                  (error.response.data.message
                    ||
                    "No additional information available."
                  );
            }
          } else {
            errorMessage =
              "Unable to connect to the server. Please check your network connection.";
          }
          alert(`${errorMessage}`);
        });
    },
  },
};
