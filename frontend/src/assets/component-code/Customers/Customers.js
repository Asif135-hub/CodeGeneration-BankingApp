import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import CustomerTransactions from '../../../components/Customers/CustomerTransaction.vue';

export default {
  components: {
    CustomerTransactions,
  },
  setup() {
    const router = useRouter();
    const customers = ref([]);
    const selectedCustomerIndex = ref(null);
    const showDailyLimitForm = ref(false);
    const showAbsoluteLimitForm = ref(false);
    const accountIdToUpdate = ref(null);
    const newDailyLimit = ref(null);
    const newAbsoluteLimit = ref(null);

    onMounted(() => {
      fetchCustomers();
    });

    const fetchCustomers = async () => {
      try {
        const response = await axios.get("http://localhost:8080/employees/customer-accounts");
        if (response.data && response.data.length) {
          customers.value = response.data;
        } else {
          console.error("No customers found");
        }
      } catch (error) {
        console.error("Failed to fetch customer accounts:", error);
      }
    };

    const viewCustomerDetails = (index) => {
      selectedCustomerIndex.value = index;
    };

    const openDailyLimitForm = (accountId) => {
      accountIdToUpdate.value = accountId;
      showDailyLimitForm.value = true;
      showAbsoluteLimitForm.value = false; 
    };

    const openAbsoluteLimitForm = (accountId) => {
      accountIdToUpdate.value = accountId;
      showAbsoluteLimitForm.value = true;
      showDailyLimitForm.value = false;  
    };

    const updateDailyLimit = async () => {
      try {
        await axios.put("http://localhost:8080/employees/customer-accounts", {
          accountId: accountIdToUpdate.value,
          dailyLimit: newDailyLimit.value,
        });
        alert("Daily limit updated successfully.");
        showDailyLimitForm.value = false;
        fetchCustomers(); 
      } catch (error) {
        alert("Failed to update daily limit: " + error.response.data);
      }
    };

    const updateAbsoluteLimit = async () => {
      try {
        await axios.put("http://localhost:8080/employees/customer-accounts", {
          accountId: accountIdToUpdate.value,
          absoluteLimit: newAbsoluteLimit.value,
        });
        alert("Absolute limit updated successfully.");
        showAbsoluteLimitForm.value = false;
        fetchCustomers(); 
      } catch (error) {
        alert("Failed to update absolute limit: " + error.response.data);
      }
    };

    const closeAccount = async (customerId) => {
      try {
        const response = await axios.delete(`http://localhost:8080/employees/close-account/${customerId}`);
        if (response.status === 200) {
          alert("Customer account status updated to rejected.");
          customers.value = customers.value.filter(customer => customer.customerId !== customerId);
        } else {
          throw new Error('Failed to close the account with provided customerId.');
        }
      } catch (error) {
        alert("Failed to update account status: " + (error.response ? error.response.data : error.message));
      }
    };


    const goToCustomers = () => {
      router.push({ path: "/employees/customer-accounts" });
    };

    const goToTransactions = () => {
      router.push({ path: "/transactions" });
    };

    const goCustomersWithoutAccounts = () => {
      router.push({ path: "/employees/customers-without-accounts" });
    };

    const goTransfer = () => {
      router.push({ path: "/transfer" });
    };

    return {
      customers,
      selectedCustomerIndex,
      viewCustomerDetails,
      openDailyLimitForm,
      openAbsoluteLimitForm,
      showDailyLimitForm,
      showAbsoluteLimitForm,
      updateDailyLimit,
      updateAbsoluteLimit,
      newDailyLimit,
      newAbsoluteLimit,
      fetchCustomers,
      goToCustomers,
      goToTransactions,
      goCustomersWithoutAccounts,
      goTransfer,
      closeAccount,
    };
  },
};
