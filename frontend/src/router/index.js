import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '@/stores/User';
import Home from '../components/Home.vue';
import Login from '../components/Login.vue';
import Register from '../components/Register.vue';
import PendingApproval from '../components/PendingApproval.vue';
import CustomerDashboard from '../components/User/UserDashboard.vue';
import CustomerHome from '../components/User/CustomerHome.vue';
import TransactionHistory from '../components/User/TransactionHistory.vue';
import FundTransfer from '../components/User/FundTransfer.vue';
import FundTransferOwn from '../components/User/FundTransferOwn.vue';
import ATMInterface from '../components/User/ATM/ATMInterface.vue';
import ATMLogin from '../components/User/ATM/ATMLogin.vue';

const routes = [
    { path: '/', component: Home },
    { path: '/login', component: Login },
    { path: '/register', component: Register },
    { path: '/pending-approval', component: PendingApproval },
    { path: '/atm/login', component: ATMLogin },
    { path: '/atm', component: ATMInterface },
    {
        path: '/customerDashboard',
        component: CustomerDashboard,
        meta: { requiresAuth: true, role: 'CUSTOMER' },
        children: [
            { path: '', component: CustomerHome },
            { path: '/transaction-history', component: TransactionHistory },
            { path: '/fund-transfer', component: FundTransfer },
            { path: '/fund-transfer-own', component: FundTransferOwn },
        ],
    },
];

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes,
});

router.beforeEach((to, from, next) => {
    const userStore = useUserStore();
    if (to.meta.requiresAuth && !userStore.isAuthenticated) {
        next('/login');
    } else if (to.meta.role && userStore.user?.role !== to.meta.role) {
        next('/login');
    } else {
        next();
    }
});

export default router;
