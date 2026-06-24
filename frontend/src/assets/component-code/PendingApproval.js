import { useUserStore } from '@/stores/User';
import { useRouter } from 'vue-router';

export default {
  name: 'PendingApproval',
  setup() {
    const userStore = useUserStore();
    const router = useRouter();

    const logoutHandler = () => {
      userStore.logout(); 
      router.push('/'); 
    };

    return { logoutHandler };
  }
}
