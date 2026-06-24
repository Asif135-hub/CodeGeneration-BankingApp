import axios from "axios";
import { onMounted, ref, reactive } from "vue";

export default {
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
  },
  setup() {
    const customers = ref([]);
    const modalCustomer = ref(null);
    const form = reactive({
      dailyLimit: 0,
      absoluteLimitForCurrent: 0,
      absoluteLimitForSaving: 0,
    });

    onMounted(async () => {
      try {
        const response = await axios.get(
          "http://localhost:8080/employees/customers-without-accounts"
        );
        if (response.data && response.data.length) {
          customers.value = response.data.map((customer) => ({
            ...customer,
            age: calculateAge(customer.dob), 
          }));
        } else {
          console.error("No customers found");
        }
      } catch (error) {
        console.error("Failed to fetch customer accounts:", error);
      }
    });

    function calculateAge(dob) {
      const birthDate = new Date(dob);
      const difference = Date.now() - birthDate.getTime();
      const ageDate = new Date(difference);
      return Math.abs(ageDate.getUTCFullYear() - 1970);
    }

    function openModal(customerId) {
      const customer = customers.value.find((c) => c.userId === customerId);
      modalCustomer.value = customer;
      if (customer) {
        $("#approveModal").modal("show");
      }
    }

    async function approveCustomer(customerId) {
      const data = {
        dailyLimit: form.dailyLimit,
        absoluteLimitForCurrent: form.absoluteLimitForCurrent,
        absoluteLimitForSaving: form.absoluteLimitForSaving,
      };
      try {
        const response = await axios.put(
          `http://localhost:8080/employees/customers-without-accounts/${customerId}/approve-signup`,
          data
        );
        if (response.status === 200) {
          customers.value = customers.value.filter(
            (customer) => customer.userId !== customerId
          );
          $("#approveModal").modal("hide");
          alert("Customer approved and account created.");
        }
      } catch (error) {
        console.error("Failed to approve customer:", error);
        alert("Failed to approve customer.");
      }
    }

    return { customers, modalCustomer, form, openModal, approveCustomer };
  },
};
