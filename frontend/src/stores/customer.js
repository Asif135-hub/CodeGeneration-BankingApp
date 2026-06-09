// import api from '@/axios.js';
// import { defineStore } from 'pinia';
// import { decodeToken } from './User';



// export const useStore = defineStore('customer', {
//     state: () => ({
//         accounts: [],
//         atmUser: null,
//         isLoggedIn: false,
//         user: JSON.parse(localStorage.getItem('user')) || null,
//         token: localStorage.getItem('token') || null,
//     }),
//     getters: {
//         isAuthenticated: (state) => !!state.user,
//         isEmployee: (state) => state.user?.role === 'EMPLOYEE',
//         isCustomer: (state) => state.user?.role === 'CUSTOMER',
//         userName: (state) => state.user ? `${state.user.firstName} ${state.user.lastName}` : '',
//     },
//     actions: {
//         async login(email, password) {
//             try {
//                 const response = await api.post('/atm/login', { email, password });
//                 const { token, role } = response.data;
//                 localStorage.setItem('token', token);
//                 this.isLoggedIn = true;
//                 const decoded = decodeToken(token);
//                 if (!decoded) throw new Error('Failed to decode token');
//                 const user = { email: decoded.sub, role, firstName: '', lastName: '' };

//                 this.user = user;
//                 this.token = token;
//                 this.isLoggedIn = true;
//                 localStorage.removeItem('user');

//                 localStorage.setItem('user', JSON.stringify(user));

//                 await this.fetchAccounts();

//             } catch (error) {

//                 throw new Error(error.response?.data?.message || 'Login failed');
//             }
//         },

//         async fetchAccounts() {
//             const token = localStorage.getItem('atm_token');
//             const res = await api.get('/customers/accounts', {
//                 headers: { Authorization: `Bearer ${token}` }
//             });
//             this.accounts = res.data;
//         },
//         async fetchAtmAccounts(email, password) {
//             try {
//                 const authRes = await api.post('/auth/login', { email, password });
//                 localStorage.setItem('atm_token', authRes.data.token);
//                 const res = await api.get('/customers/accounts', {
//                     headers: { Authorization: `Bearer ${authRes.data.token}` }
//                 });
//                 this.accounts = res.data;
//             } catch (e) {
//                 console.error('Failed to fetch ATM accounts:', e);
//             }
//         },
//         async deposit(iban, amount) {
//             try {
//                 const res = await api.post('/atm/deposit', { iban, amount });
//                 await this.refreshAccounts();
//                 return res.data;
//             } catch (error) {
//                 alert(error.response?.data?.message || 'Deposit failed');
//                 return null;
//             }
//         },
//         async withdraw(iban, amount) {
//             try {
//                 const res = await api.post('/atm/withdraw', { iban, amount });
//                 await this.refreshAccounts();
//                 return res.data;
//             } catch (error) {
//                 alert(error.response?.data?.message || 'Withdraw failed');
//                 return null;
//             }
//         },
//         async refreshAccounts() {
//             const token = localStorage.getItem('atm_token');
//             if (!token) return;
//             const res = await api.get('/customers/accounts', {
//                 headers: { Authorization: `Bearer ${token}` }
//             });
//             this.accounts = res.data;
//         },
//         logout() {
//             this.atmUser = null;
//             this.accounts = [];
//             this.isLoggedIn = false;
//             this.user = null;
//             this.token = null;

//             localStorage.removeItem('token');
//             localStorage.removeItem('user');
//         },
//     },
// });


import api from '@/axios.js';
import { defineStore } from 'pinia';
import { decodeToken } from './User';

export const useStore = defineStore('customer', {
    state: () => ({
        accounts: [],
        atmUser: null,
        isLoggedIn: false,
        user: (() => { try { return JSON.parse(localStorage.getItem('atm_user')) || null; } catch { return null; } })(),
        token: localStorage.getItem('atm_token') || null,
    }),
    getters: {
        isAuthenticated: (state) => !!state.user && state.isLoggedIn,
        isEmployee: (state) => state.user?.role === 'EMPLOYEE',
        isCustomer: (state) => state.user?.role === 'CUSTOMER',
        userName: (state) => state.user ? `${state.user.firstName} ${state.user.lastName}` : '',
    },
    actions: {
        async login(email, password) {
            try {
                const response = await api.post('/atm/login', { email, password });
                const { token, role } = response.data;
                const decoded = decodeToken(token);
                if (!decoded) throw new Error('Failed to decode token');

                const user = {
                    email: decoded.sub,
                    role,
                    firstName: decoded.firstName || '',
                    lastName: decoded.lastName || '',
                };

                this.user = user;
                this.token = token;
                this.isLoggedIn = true;

                localStorage.setItem('atm_token', token);
                localStorage.setItem('atm_user', JSON.stringify(user));

                await this.fetchAccounts();
            } catch (error) {
                throw new Error(error.response?.data?.message || 'Login failed');
            }
        },

        async fetchAccounts() {
            const token = localStorage.getItem('atm_token');
            const res = await api.get('/customers/accounts', {
                headers: { Authorization: `Bearer ${token}` }
            });
            this.accounts = res.data;
        },

        async deposit(iban, amount) {
            try {
                const res = await api.post('/atm/deposit', { iban, amount });
                await this.refreshAccounts();
                return res.data;
            } catch (error) {
                alert(error.response?.data?.message || 'Deposit failed');
                return null;
            }
        },

        async withdraw(iban, amount) {
            try {
                const res = await api.post('/atm/withdraw', { iban, amount });
                await this.refreshAccounts();
                return res.data;
            } catch (error) {
                alert(error.response?.data?.message || 'Withdraw failed');
                return null;
            }
        },

        async refreshAccounts() {
            const token = localStorage.getItem('atm_token');
            if (!token) return;
            const res = await api.get('/customers/accounts', {
                headers: { Authorization: `Bearer ${token}` }
            });
            this.accounts = res.data;
        },

        logout() {
            this.atmUser = null;
            this.accounts = [];
            this.isLoggedIn = false;
            this.user = null;
            this.token = null;
            localStorage.removeItem('atm_token');
            localStorage.removeItem('atm_user');
        },
    },
});