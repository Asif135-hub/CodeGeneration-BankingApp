<template>
  <div>
    <div v-if="loading" class="text-center mt-5">
      <div class="spinner-border text-primary" style="width:3rem;height:3rem;"></div>
      <p class="mt-3 text-muted">Loadingâ€¦</p>
    </div>

    <div v-else>
      <!-- Welcome hero -->
      <div class="welcome-hero p-4 p-md-5 rounded-4 mb-4 d-flex align-items-center justify-content-between">
        <div>
          <p class="text-teal mb-1 fw-semibold">Good day ðŸ‘‹</p>
          <h2 class="fw-bold text-white mb-1">{{ userStore.userName }}</h2>
          <p class="text-muted-light mb-0">Manage your finances easily from your dashboard.</p>
        </div>
        <div class="hero-icon d-none d-md-flex">ðŸ¦</div>
      </div>

      <!-- No accounts state -->
      <div v-if="accounts.length === 0" class="info-card p-4 rounded-4 text-center mb-4">
        <div style="font-size:2.5rem" class="mb-2">ðŸ’³</div>
        <h5 class="fw-bold mb-1">No accounts yet</h5>
        <p class="text-muted mb-3">Please contact a bank employee to open an account.</p>
       
      </div>

      <!-- View Accounts button -->
      <div v-if="accounts.length > 0 && !showAccounts" class="text-center mb-4">
        <button class="btn-view-accounts" @click="showAccounts = true">
          ðŸ’³ View My Accounts
        </button>
      </div>

      <!-- Account cards -->
      <div v-if="showAccounts" class="mb-4">
        <div class="d-flex align-items-center justify-content-between mb-3">
          <h5 class="fw-bold mb-0">Your Accounts</h5>
          <button class="btn-text" @click="showAccounts = false">Hide â†‘</button>
        </div>
        <div class="row g-3">
          <div class="col-md-6" v-for="account in accounts" :key="account.id">
            <div class="account-card p-4 rounded-4 h-100"
              :class="account.accountType === 'SAVINGS' ? 'savings-card' : 'current-card'">
              <span class="acct-type-badge mb-3 d-inline-block">
                {{'ðŸ’³' }}
              </span>
              <div class="iban-text mb-3">{{ account.iban }}</div>
              <div class="balance-label">Balance</div>
              <div class="balance-amount mb-3">â‚¬{{ formatAmount(account.balance) }}</div>
              <div class="row g-2">
                <div class="col-6">
                  <div class="limit-box p-2 rounded-3">
                    <div class="limit-label">Daily Limit</div>
                    <div class="limit-value">â‚¬{{ formatAmount(account.dailyLimit) }}</div>
                  </div>
                </div>
                <div class="col-6">
                  <div class="limit-box p-2 rounded-3">
                    <div class="limit-label">Absolute Limit</div>
                    <div class="limit-value">â‚¬{{ formatAmount(account.absoluteLimit) }}</div>
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
            <span class="action-icon me-3">ðŸ“‹</span>
            <div>
              <div class="fw-bold text-dark">Transaction History</div>
              <div class="text-muted small">View all past transactions</div>
            </div>
          </router-link>
        </div>
        <div class="col-md-4">
          <router-link to="/fund-transfer" class="quick-action d-flex align-items-center p-3 rounded-3 text-decoration-none">
            <span class="action-icon me-3">âž¡ï¸</span>
            <div>
              <div class="fw-bold text-dark">Transfer to Others</div>
              <div class="text-muted small">Send money to another customer</div>
            </div>
          </router-link>
        </div>
        <div class="col-md-4">
          <router-link to="/fund-transfer-own" class="quick-action d-flex align-items-center p-3 rounded-3 text-decoration-none">
            <span class="action-icon me-3">ðŸ”„</span>
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

<script src="../../assets/component-code/User/CustomerHome.js"></script>

<style scoped src="../../assets/component-code/User/CustomerHome.css"></style>