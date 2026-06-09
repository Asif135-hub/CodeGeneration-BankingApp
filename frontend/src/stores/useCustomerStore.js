// import { defineStore } from 'pinia';
// import axios from '../axios';
//
// export const useCustomerStore = defineStore('customerStore', {
//     state: () => ({
//         customers: [],
//     }),
//     actions: {
//         fetchCustomersWithoutAccounts() {
//             return axios.get('/employees/customer-accounts')
//                 .then(response => {
//                     this.customers = response.data;
//                 })
//                 .catch(error => {
//                     console.error('Failed to fetch customers:', error);
//                     throw error;
//                 });
//         },
//         approveSignup(userId, approveData) {
//             return axios.put(`/employees/customers-without-accounts/${userId}/approve-signup`, approveData)
//                 .then(response => {
//                     console.log('Signup approved:', response.data);
//                 })
//                 .catch(error => {
//                     console.error('Failed to approve signup:', error);
//                     throw error;
//                 });
//         }
//     }
// });
