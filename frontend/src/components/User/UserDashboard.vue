<template>
  <div class="dashboard-wrapper">
    <!-- Sidebar -->
    <nav id="sidebar" :class="{ collapsed: sidebarCollapsed }">
      <div class="sidebar-header d-flex align-items-center justify-content-between px-3 py-3">
        <span v-if="!sidebarCollapsed" class="sidebar-title">ABN AMRO</span>
        <button class="collapse-btn" @click="sidebarCollapsed = !sidebarCollapsed">
          {{ sidebarCollapsed ? 'â†’' : 'â†' }}
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
          <span>ðŸšª</span>
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

<script src="../../assets/component-code/User/UserDashboard.js"></script>

<style scoped src="../../assets/component-code/User/UserDashboard.css"></style>