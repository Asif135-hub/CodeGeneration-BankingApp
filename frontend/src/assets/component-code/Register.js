import api from '@/axios.js';

export default {
  name: 'Register',
  data() {
    return {
      form: { firstName: '', lastName: '', email: '', password: '', bsn: '', phoneNumber: '' },      confirmPassword: '',
      showSuccessMessage: false,
      errorMessage: '',
    };
  },
  computed: {
    passwordsMatch() { return this.form.password === this.confirmPassword; }
  },
  methods: {
    async registerCustomer() {
      this.errorMessage = '';
     
      try {
        await api.post('/auth/register', this.form);
        this.showSuccessMessage = true;
      } catch (error) {
        this.errorMessage = error.response?.data?.message || JSON.stringify(error.response?.data) || 'Registration failed';
      }
    },
  },
};
