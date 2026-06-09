<template>
  <div class="atm-wrapper">

    <!-- Top bar -->
    <div class="atm-topbar d-flex align-items-center justify-content-between px-4 py-3">
      <div>
        <p class="text-teal mb-0 fw-semibold small">ABN AMRO ATM</p>
        <h5 class="fw-bold text-white mb-0">{{ store.user?.email }}</h5>
      </div>
      <button class="btn-logout" @click="logout">🚪 Exit ATM</button>
    </div>

    <div class="atm-body p-4">

      <div v-if="loading" class="text-center mt-5">
        <div class="spinner-border text-primary" style="width:3rem;height:3rem;"></div>
        <p class="mt-3 text-muted">Loading accounts…</p>
      </div>

      <div v-else-if="store.accounts.length === 0" class="info-card p-4 rounded-4 text-center">
        <div style="font-size:2.5rem" class="mb-2">💳</div>
        <h5 class="fw-bold mb-1">No accounts found</h5>
        <p class="text-muted mb-0">Please contact a bank employee to open an account.</p>
      </div>

      <div v-else>
        <!-- Account cards -->
        <div class="d-flex align-items-center justify-content-between mb-3">
          <h5 class="fw-bold mb-0">Accounts</h5>
        </div>
        <div class="row g-3 mb-4">
          <div class="col-md-6" v-for="account in store.accounts" :key="account.id">
            <div
              class="account-card p-4 rounded-4 h-100"
              :class="account.accountType === 'SAVINGS' ? 'savings-card' : 'current-card'"
            >
              <span class="acct-type-badge mb-3 d-inline-block">
               💳 
              </span>
              <div class="iban-text mb-3">{{ account.iban }}</div>
              <div class="balance-label">Balance</div>
              <div class="balance-amount mb-4">€{{ formatAmount(account.balance) }}</div>
              <div class="d-flex gap-2">
                <button class="atm-action-btn deposit-btn flex-fill" @click="openModal(account, 'deposit')">
                  💰 Deposit
                </button>
                <button
                  class="atm-action-btn withdraw-btn flex-fill"
                  @click="openModal(account, 'withdraw')"
                  :disabled="Number(account.balance) <= 0"
                >
                  💸 Withdraw
                </button>
              </div>
            </div>
          </div>
        </div>

     
      </div>
    </div>

    <!-- Transaction Modal -->
    <div v-if="modal.show" class="modal-overlay" @click.self="closeModal">
      <div class="modal-box rounded-4 p-4">
        <div class="d-flex align-items-center justify-content-between mb-3">
          <h5 class="fw-bold mb-0">
            {{ modal.type === 'deposit' ? '💰 Deposit' : '💸 Withdraw' }}
          </h5>
          <button class="btn-close-modal" @click="closeModal">✕</button>
        </div>

        <div class="iban-pill mb-3">{{ modal.account?.iban }}</div>

        <div v-if="modal.msg" :class="modal.error ? 'alert alert-danger' : 'alert alert-success'" class="mb-3">
          {{ modal.msg }}
        </div>

       <div v-if="!modal.msg">
        <label class="form-label fw-semibold mb-1">Amount (€)</label>
        <input
          type="number"
          class="form-control form-control-lg mb-4"
          v-model.number="modal.amount"
          min="0.01"
          step="0.01"
          placeholder="0.00"
        />
      </div>

        <div class="d-flex gap-2">
          <button class="btn-cancel flex-fill" @click="closeModal">Cancel</button>
          <button
            v-if="!modal.msg"
            class="btn-confirm flex-fill"
            :class="modal.type === 'deposit' ? 'btn-confirm-deposit' : 'btn-confirm-withdraw'"
            @click="submitModal"
            :disabled="txLoading"
          >
            {{ txLoading ? 'Processing…' : 'Confirm' }}
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { useStore } from '@/stores/customer.js';
import { useRouter } from 'vue-router';

export default {
  name: 'ATMInterface',
  data() {
    return {
      loading: false,
      txLoading: false,
      modal: { show: false, type: '', account: null, amount: null, msg: '', error: false },
    };
  },
  setup() { return { store: useStore(), router: useRouter() }; },
  async mounted() {
    this.loading = true;
    await this.store.fetchAccounts();
    this.loading = false;
  },
  methods: {
    formatAmount(val) {
      return Number(val).toLocaleString('nl-NL', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    },
    openModal(account, type) {
      this.modal = { show: true, type, account, amount: null, msg: '', error: false };
    },
    closeModal() { this.modal.show = false; },
    async submitModal() {
      if (!this.modal.amount || this.modal.amount <= 0) {
        this.modal.msg = 'Enter a valid amount'; this.modal.error = true; return;
      }
      this.txLoading = true;
      const result = this.modal.type === 'deposit'
        ? await this.store.deposit(this.modal.account.iban, this.modal.amount)
        : await this.store.withdraw(this.modal.account.iban, this.modal.amount);
      this.txLoading = false;
      if (result) { this.modal.msg = result.message || 'Success!'; this.modal.error = false; this.modal.amount = null; }
      else { this.modal.msg = 'Transaction failed'; this.modal.error = true; }
    },
    logout() { this.store.logout(); this.router.push('/atm/login'); },
  },
};
</script>

<style scoped>
.atm-wrapper { min-height: 100vh; background: #f4f6f9; }

/* Top bar */
.atm-topbar { background: linear-gradient(135deg, #0d3349 0%, #1a4a6b 100%); }
.text-teal { color: #60BFC1; }
.btn-logout {
  background: rgba(255,255,255,0.1); border: 1px solid rgba(255,255,255,0.2);
  color: #fff; padding: 8px 18px; border-radius: 50px; font-size: 0.85rem;
  font-weight: 600; cursor: pointer; transition: all 0.2s;
}
.btn-logout:hover { background: rgba(255,80,80,0.3); border-color: transparent; }

.atm-body { max-width: 960px; margin: 0 auto; }

/* Info card */
.info-card { background: #fff; box-shadow: 0 2px 16px rgba(0,0,0,0.06); }

/* Account cards */
.current-card { background: linear-gradient(135deg, #1a4a7a 0%, #2d6fad 100%); color: #fff; }
.savings-card  { background: linear-gradient(135deg, #1a6b4a 0%, #2da870 100%); color: #fff; }
.acct-type-badge { background: rgba(255,255,255,0.2); color: #fff; padding: 4px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: 600; }
.iban-text { font-size: 0.85rem; opacity: 0.8; font-family: monospace; letter-spacing: 0.5px; }
.balance-label { font-size: 0.75rem; opacity: 0.7; text-transform: uppercase; letter-spacing: 1px; }
.balance-amount { font-size: 2rem; font-weight: 700; }

.atm-action-btn {
  border: none; border-radius: 10px; padding: 10px 0; font-weight: 600; font-size: 0.9rem; cursor: pointer; transition: all 0.2s;
}
.deposit-btn  { background: rgba(255,255,255,0.9); color: #1a6b4a; }
.withdraw-btn { background: rgba(255,255,255,0.2); color: #fff; border: 1px solid rgba(255,255,255,0.4); }
.deposit-btn:hover  { background: #fff; }
.withdraw-btn:hover { background: rgba(255,255,255,0.3); }
.withdraw-btn:disabled { opacity: 0.4; cursor: not-allowed; }

/* Quick actions */
.quick-action { background: #fff; border: 1px solid #e9ecef; transition: all 0.2s; box-shadow: 0 1px 4px rgba(0,0,0,0.04); }
.quick-action:hover { border-color: #60BFC1; transform: translateY(-2px); box-shadow: 0 4px 12px rgba(96,191,193,0.2); }
.action-icon { font-size: 1.8rem; }

/* Modal */
.modal-overlay {
  position: fixed; inset: 0; background: rgba(0,0,0,0.5);
  display: flex; align-items: center; justify-content: center; z-index: 1000;
}
.modal-box { background: #fff; width: 100%; max-width: 420px; box-shadow: 0 8px 40px rgba(0,0,0,0.2); }
.btn-close-modal { background: #f1f3f5; border: none; width: 32px; height: 32px; border-radius: 50%; cursor: pointer; font-size: 0.85rem; }
.iban-pill { background: #f1f3f5; border-radius: 20px; padding: 6px 16px; font-family: monospace; font-size: 0.85rem; display: inline-block; }
.btn-cancel { background: #f1f3f5; border: none; padding: 12px; border-radius: 10px; font-weight: 600; cursor: pointer; }
.btn-confirm { border: none; padding: 12px; border-radius: 10px; font-weight: 600; color: #fff; cursor: pointer; transition: opacity 0.2s; }
.btn-confirm:disabled { opacity: 0.6; cursor: not-allowed; }
.btn-confirm-deposit  { background: linear-gradient(135deg, #1a6b4a, #2da870); }
.btn-confirm-withdraw { background: linear-gradient(135deg, #7a1a1a, #ad2d2d); }
</style>