<template>
  <div>
    <div v-if="loading" class="text-center mt-5">
      <div class="spinner-border text-primary" style="width:3rem;height:3rem;"></div>
      <p class="mt-3 text-muted">Loading…</p>
    </div>

    <div v-else>
      <!-- Welcome hero -->
      <div class="welcome-hero p-4 p-md-5 rounded-4 mb-4 d-flex align-items-center justify-content-between">
        <div>
          <p class="text-teal mb-1 fw-semibold">Good day 👋</p>
          <h2 class="fw-bold text-white mb-1">{{ userStore.userName }}</h2>
          <p class="text-muted-light mb-0">Manage your finances easily from your dashboard.</p>
        </div>
        <div class="hero-icon d-none d-md-flex">🏦</div>
      </div>

      <!-- No accounts state -->
      <div v-if="accounts.length === 0" class="info-card p-4 rounded-4 text-center mb-4">
        <div style="font-size:2.5rem" class="mb-2">💳</div>
        <h5 class="fw-bold mb-1">No accounts yet</h5>
        <p class="text-muted mb-3">Please contact a bank employee to open an account.</p>
       
      </div>

      <!-- View Accounts button -->
      <div v-if="accounts.length > 0 && !showAccounts" class="text-center mb-4">
        <button class="btn-view-accounts" @click="showAccounts = true">
          💳 View My Accounts
        </button>
      </div>

      <!-- Account cards -->
      <div v-if="showAccounts" class="mb-4">
        <div class="d-flex align-items-center justify-content-between mb-3">
          <h5 class="fw-bold mb-0">Your Accounts</h5>
          <button class="btn-text" @click="showAccounts = false">Hide ↑</button>
        </div>
        <div class="row g-3">
          <div class="col-md-6" v-for="account in accounts" :key="account.id">
            <div class="account-card p-4 rounded-4 h-100"
              :class="account.accountType === 'SAVINGS' ? 'savings-card' : 'current-card'">
              <span class="acct-type-badge mb-3 d-inline-block">
                {{'💳' }}
              </span>
              <div class="iban-text mb-3">{{ account.iban }}</div>
              <div class="balance-label">Balance</div>
              <div class="balance-amount mb-3">€{{ formatAmount(account.balance) }}</div>
              <div class="row g-2">
                <div class="col-6">
                  <div class="limit-box p-2 rounded-3">
                    <div class="limit-label">Daily Limit</div>
                    <div class="limit-value">€{{ formatAmount(account.dailyLimit) }}</div>
                  </div>
                </div>
                <div class="col-6">
                  <div class="limit-box p-2 rounded-3">
                    <div class="limit-label">Absolute Limit</div>
                    <div class="limit-value">€{{ formatAmount(account.absoluteLimit) }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Quick Actions -->
      <h5 class="fw-bold mb-3">Quick Actions</h5>
      <div class="row g-3">
        <div class="col-md-4">
          <router-link to="/transaction-history" class="quick-action d-flex align-items-center p-3 rounded-3 text-decoration-none">
            <span class="action-icon me-3">📋</span>
            <div>
              <div class="fw-bold text-dark">Transaction History</div>
              <div class="text-muted small">View all past transactions</div>
            </div>
          </router-link>
        </div>
        <div class="col-md-4">
          <router-link to="/fund-transfer" class="quick-action d-flex align-items-center p-3 rounded-3 text-decoration-none">
            <span class="action-icon me-3">➡️</span>
            <div>
              <div class="fw-bold text-dark">Transfer to Others</div>
              <div class="text-muted small">Send money to another customer</div>
            </div>
          </router-link>
        </div>
        <div class="col-md-4">
          <router-link to="/fund-transfer-own" class="quick-action d-flex align-items-center p-3 rounded-3 text-decoration-none">
            <span class="action-icon me-3">🔄</span>
            <div>
              <div class="fw-bold text-dark">Transfer Own Accounts</div>
              <div class="text-muted small">Move funds between your accounts</div>
            </div>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/axios.js';
import { useUserStore } from '@/stores/User';

export default {
  name: 'CustomerHome',
  data() { return { accounts: [], loading: true, showAccounts: false }; },
  setup() { return { userStore: useUserStore() }; },
  async mounted() {
    try {
      const r = await api.get('/customers/accounts');
      this.accounts = r.data;
    } catch (e) { console.error(e); }
    finally { this.loading = false; }
  },
  methods: {
    formatAmount(val) {
      return Number(val).toLocaleString('nl-NL', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    },
  },
};
</script>

<style scoped>
.welcome-hero {
  background: linear-gradient(135deg, #0d3349 0%, #1a4a6b 100%);
}
.text-teal { color: #60BFC1; }
.text-muted-light { color: rgba(255,255,255,0.55); font-size: 0.9rem; }
.hero-icon { font-size: 4rem; opacity: 0.3; }

.info-card { background: #fff; box-shadow: 0 2px 16px rgba(0,0,0,0.06); }

.btn-view-accounts {
  background: linear-gradient(135deg, #0d3349, #1a4a6b);
  color: #fff;
  border: none;
  padding: 14px 40px;
  border-radius: 50px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 4px 16px rgba(13,51,73,0.25);
}
.btn-view-accounts:hover { transform: translateY(-2px); box-shadow: 0 6px 20px rgba(13,51,73,0.35); }

.btn-text { background: none; border: none; color: #60BFC1; font-weight: 600; cursor: pointer; font-size: 0.9rem; }

.current-card { background: linear-gradient(135deg, #1a4a7a 0%, #2d6fad 100%); color: #fff; }
.savings-card  { background: linear-gradient(135deg, #1a6b4a 0%, #2da870 100%); color: #fff; }
.acct-type-badge { background: rgba(255,255,255,0.2); color: #fff; padding: 4px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: 600; }
.iban-text { font-size: 0.85rem; opacity: 0.8; font-family: monospace; letter-spacing: 0.5px; }
.balance-label { font-size: 0.75rem; opacity: 0.7; text-transform: uppercase; letter-spacing: 1px; }
.balance-amount { font-size: 2rem; font-weight: 700; }
.limit-box { background: rgba(255,255,255,0.15); }
.limit-label { font-size: 0.7rem; opacity: 0.75; }
.limit-value { font-weight: 600; font-size: 0.9rem; }

.quick-action { background: #fff; border: 1px solid #e9ecef; transition: all 0.2s; box-shadow: 0 1px 4px rgba(0,0,0,0.04); }
.quick-action:hover { border-color: #60BFC1; transform: translateY(-2px); box-shadow: 0 4px 12px rgba(96,191,193,0.2); }
.action-icon { font-size: 1.8rem; }
</style>