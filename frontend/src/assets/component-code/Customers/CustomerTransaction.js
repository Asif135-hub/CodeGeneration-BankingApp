import axios from "axios";
import { onMounted, ref } from "vue";

export default {
  props: {
    customerId: {
      type: String,
      required: true,
    },
    customerName: {
      type: String,
      required: true,
    },
  },
  setup(props, { emit }) {
    const transactions = ref([]);

    onMounted(async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/customers/${props.customerId}/transactions`
        );
        transactions.value = response.data;
      } catch (error) {
        console.error("Failed to fetch transactions:", error);
      }
    });

    function goBack() {
      emit("back");
    }

    return { transactions, goBack };
  },
};
