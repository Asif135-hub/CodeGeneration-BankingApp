<template>
  <div class="dashboard-wrapper">
    <!-- Sidebar -->
    <nav id="sidebar" :class="{ collapsed: sidebarCollapsed }">
      <div class="sidebar-header d-flex align-items-center justify-content-between px-3 py-3">
        <span v-if="!sidebarCollapsed" class="sidebar-title">ABN AMRO</span>
        <button class="collapse-btn" @click="sidebarCollapsed = !sidebarCollapsed">
          {{ sidebarCollapsed ? '→' : '←' }}
        </button>
      </div>

      <!-- User profile chip -->
      <div v-if="!sidebarCollapsed" class="user-chip mx-3 mb-3 p-3 rounded-3">
        <div class="user-avatar mb-1">{{ initials }}</div>
        <div class="user-name">{{ userStore.userName }}</div>
        <div class="user-role">Customer</div>
      </div>

      <ul class="list-unstyled px-2">
        <li v-for="item in menuItems" :key="item.path" class="mb-1">
          <router-link
            :to="item.path"
            class="menu-link d-flex align-items-center gap-2 px-3 py-2 rounded-2"
            :title="sidebarCollapsed ? item.label : ''"
          >
            <span class="menu-icon">{{ item.icon }}</span>
            <span v-if="!sidebarCollapsed" class="menu-label">{{ item.label }}</span>
          </router-link>
        </li>
      </ul>

      <div class="sidebar-footer px-2 mt-auto pb-3">
        <button
          class="logout-btn d-flex align-items-center gap-2 px-3 py-2 rounded-2 w-100"
          @click="logoutHandler"
        >
          <span>🚪</span>
          <span v-if="!sidebarCollapsed">Logout</span>
        </button>
      </div>
    </nav>

    <!-- Main content -->
    <div id="content" class="p-4 p-md-5 flex-grow-1">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import { useUserStore } from '@/stores/User';

export default {
  name: 'CustomerDashboard',
  data() {
    return {
      sidebarCollapsed: false,
      menuItems: [
        { path: '/customerDashboard', icon: '🏠', label: 'Home' },
        { path: '/transaction-history', icon: '📋', label: 'Transaction History' },
        { path: '/fund-transfer', icon: '➡️', label: 'Transfer to Others' },
        { path: '/fund-transfer-own', icon: '🔄', label: 'Transfer Own Accounts' },
      ],
    };
  },
  setup() {
    return { userStore: useUserStore() };
  },
  computed: {
    initials() {
      const name = this.userStore.userName || '';
      return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2) || '?';
    },
  },
  methods: {
    logoutHandler() {
      this.userStore.logout();
      this.$router.push('/');
    },
  },
};
</script>

<style scoped>
.dashboard-wrapper {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

#sidebar {
  width: 240px;
  min-width: 240px;
  background: linear-gradient(180deg, #0d3349 0%, #1a4a6b 100%);
  display: flex;
  flex-direction: column;
  transition: width 0.25s ease, min-width 0.25s ease;
  overflow: hidden;
}

#sidebar.collapsed {
  width: 64px;
  min-width: 64px;
}

#content {
  flex: 1;
  overflow-y: auto;
  background: #f4f6f9;
}

.sidebar-title {
  color: #60BFC1;
  font-weight: 700;
  font-size: 1.1rem;
  letter-spacing: 0.5px;
}

.collapse-btn {
  background: rgba(255,255,255,0.1);
  border: none;
  color: #fff;
  width: 28px;
  height: 28px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.85rem;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.collapse-btn:hover { background: rgba(255,255,255,0.2); }

.user-chip {
  background: rgba(255,255,255,0.07);
  text-align: center;
}
.user-avatar {
  width: 44px;
  height: 44px;
  background: #60BFC1;
  color: #0d3349;
  border-radius: 50%;
  font-weight: 700;
  font-size: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 6px;
}
.user-name { color: #fff; font-weight: 600; font-size: 0.9rem; }
.user-role { color: #60BFC1; font-size: 0.75rem; }

.menu-link {
  color: rgba(255,255,255,0.75);
  text-decoration: none;
  transition: all 0.15s;
  font-size: 0.9rem;
  white-space: nowrap;
}
.menu-link:hover {
  background: rgba(255,255,255,0.1);
  color: #fff;
}
.menu-link.router-link-exact-active {
  background: rgba(96,191,193,0.2);
  color: #60BFC1;
  font-weight: 600;
}
.menu-icon { font-size: 1.1rem; flex-shrink: 0; }
.menu-label { overflow: hidden; text-overflow: ellipsis; }

.sidebar-footer { margin-top: auto; }
.logout-btn {
  background: rgba(255,80,80,0.15);
  border: none;
  color: rgba(255,150,150,0.9);
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.15s;
  white-space: nowrap;
}
.logout-btn:hover {
  background: rgba(255,80,80,0.3);
  color: #fff;
}
</style>