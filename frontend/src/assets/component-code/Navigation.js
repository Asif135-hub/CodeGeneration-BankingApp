import { useUserStore } from '@/stores/User';
import { useStore } from '@/stores/customer';
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

export default {
  name: 'Navigation',
  setup() {
    const userStore = useUserStore();
    const customerStore = useStore();
    const store = useStore();
    const route = useRoute();
    const router = useRouter();
    const hideNavBar = ref(false);
    const isAtmLogin = ref(false);

    watch(
        () => route.path,
        (newPath) => {
          hideNavBar.value = newPath === '/pending-approval';
          isAtmLogin.value = newPath.startsWith('/atm');
        },
        { immediate: true }
    );

    const AtmLogout = () => {
      store.logout();
      router.push('/');
    };

    return { store, userStore, customerStore, hideNavBar, isAtmLogin, route, router, AtmLogout };
  },
  computed: {
    isAuthenticated() {
      return this.userStore.isAuthenticated;
    },
    isEmployee() {
      return this.userStore.isEmployee;
    },
    isCustomer() {
      console.log('userStore user:', this.userStore.user);
      return this.userStore.isCustomer;
    },
    userName() {
      return this.userStore.userName;
    },
    isAtmUser() {
      return this?.customerStore?.isAuthenticated;
    }
  },
  methods: {
    logoutHandler() {
      this.userStore.logout();
      this.$router.push('/');
    },
    handleRegularLoginClick() {
      if (this.userStore.isLoggedIn) {
        this.userStore.logout();
      }
      this.$router.push('/login');
    }
  },
};
