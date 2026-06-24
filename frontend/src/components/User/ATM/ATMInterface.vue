<template>
  <div class="atm-wrapper">

    <!-- Top bar -->
    <div class="atm-topbar d-flex align-items-center justify-content-between px-4 py-3">
      <div>
        <p class="text-teal mb-0 fw-semibold small">ABN AMRO ATM</p>
        <h5 class="fw-bold text-white mb-0">{{ store.user?.email }}</h5>
      </div>
      <button class="btn-logout" @click="logout">ðŸšª Exit ATM</button>
    </div>

    <div class="atm-body p-4">

      <div v-if="loading" class="text-center mt-5">
        <div class="spinner-border text-primary" style="width:3rem;height:3rem;"></div>
        <p class="mt-3 text-muted">Loading accountsâ€¦</p>
      </div>

      <div v-else-if="store.accounts.length === 0" class="info-card p-4 rounded-4 text-center">
        <div style="font-size:2.5rem" class="mb-2">ðŸ’³</div>
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
               ðŸ’³ 
              </span>
              <div class="iban-text mb-3">{{ account.iban }}</div>
              <div class="balance-label">Balance</div>
              <div class="balance-amount mb-4">â‚¬{{ formatAmount(account.balance) }}</div>
              <div class="d-flex gap-2">
                <button class="atm-action-btn deposit-btn flex-fill" @click="openModal(account, 'deposit')">
                  ðŸ’° Deposit
                </button>
                <button
                  class="atm-action-btn withdraw-btn flex-fill"
                  @click="openModal(account, 'withdraw')"
                  :disabled="Number(account.balance) <= 0"
                >
                  ðŸ’¸ Withdraw
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
            {{ modal.type === 'deposit' ? 'ðŸ’° Deposit' : 'ðŸ’¸ Withdraw' }}
          </h5>
          <button class="btn-close-modal" @click="closeModal">âœ•</button>
        </div>

        <div class="iban-pill mb-3">{{ modal.account?.iban }}</div>

        <div v-if="modal.msg" :class="modal.error ? 'alert alert-danger' : 'alert alert-success'" class="mb-3">
          {{ modal.msg }}
        </div>

       <div v-if="!modal.msg">
        <label class="form-label fw-semibold mb-1">Amount (â‚¬)</label>
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
            {{ txLoading ? 'Processingâ€¦' : 'Confirm' }}
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script src="../../../assets/component-code/User/ATM/ATMInterface.js"></script>

<style scoped src="../../../assets/component-code/User/ATM/ATMInterface.css"></style>