import api from '@/axios.js';
import { defineStore } from 'pinia';

export function decodeToken(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(c =>
            '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
        ).join(''));
        const decoded = JSON.parse(jsonPayload);
        if (decoded.exp * 1000 < Date.now()) return null;
        return decoded;
    } catch { return null; }
}

export const useUserStore = defineStore('user', {
    state: () => ({
        user: (() => {
            try {
                const u = JSON.parse(localStorage.getItem('user'));
                return u && u.role ? u : null;
            } catch {
                return null;
            }
        })(), token: localStorage.getItem('token') || null,
    }),
    getters: {
        isAuthenticated: (state) => !!state.user,
        isEmployee: (state) => state.user?.role === 'EMPLOYEE',
        isCustomer: (state) => !!state.user && state.user.role === 'CUSTOMER', userName: (state) => state.user ? `${state.user.firstName} ${state.user.lastName}` : '',
    },
    actions: {
        setUser(user) {
            this.user = user;
            localStorage.setItem('user', JSON.stringify(user));
        },
        setToken(token) {
            this.token = token;
            localStorage.setItem('token', token);
        },
        logout() {
            this.user = null;
            this.token = null;
            localStorage.removeItem('user');
            localStorage.removeItem('token');
        },
        async login({ username, password }) {
            try {
                const response = await api.post('/auth/login', { email: username, password });
                const { token, role } = response.data;
                const decoded = decodeToken(token);


                if (!decoded) throw new Error('Failed to decode token');
                const user = {
                    email: decoded.sub,
                    role,
                    firstName: decoded.firstName || '',
                    lastName: decoded.lastName || '',
                };
                this.setToken(token);
                this.setUser(user);
                return { success: true, user };
            } catch (error) {
                return { success: false, message: error.response?.data?.message || 'Login failed' };
            }
        },
    },
});
