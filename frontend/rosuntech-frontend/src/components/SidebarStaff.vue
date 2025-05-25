<template>
  <div>
    <!-- Header dengan Hamburger + Judul + Page Name -->
    <div class="sidebar-header">
      <div class="d-flex align-items-center">
        <button class="btn open-sidebar-btn" @click="toggleSidebar" :aria-expanded="isOpen" v-html="sidebarButtonIcon"></button>
        <span class="sidebar-title"> 
          <span class="text-kema">Kema</span><span class="text-telyu">telyu</span> 
        </span>
      </div>
      <div class="page-name">
        <span class="page-name-text">{{ currentPageName }}</span>
      </div>
    </div>

    <!-- Backdrop Overlay -->
    <div v-if="isOpen" class="sidebar-backdrop" @click="toggleSidebar"></div>

    <!-- Sidebar -->
    <div :class="['sidebar', isOpen ? 'open' : '']">
      <!-- Brand Section -->
      <div class="sidebar-brand">
        <div class="brand-icon">
          <div class="brand-dot"></div>
        </div>
        <span class="brand-text">
          <span class="text-kema">Kema</span><span class="text-telyu">telyu</span>
        </span>
      </div>

      <!-- Menu -->
      <nav class="sidebar-nav">
        <ul class="nav flex-column">
          <li class="nav-item">
            <router-link class="nav-link" :to="{ name: 'DashboardStaff' }">
              <div class="nav-icon-wrapper">
                <font-awesome-icon :icon="['fas', 'home']" class="menu-icon" />
              </div>
              <span class="nav-text">Dashboard</span>
              <div class="nav-indicator"></div>
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" :to="{ name: 'CreateEvent' }">
              <div class="nav-icon-wrapper">
                <font-awesome-icon :icon="['fas', 'calendar-alt']" class="menu-icon" />
              </div>
              <span class="nav-text">Buat Event</span>
              <div class="nav-indicator"></div>
            </router-link>
          </li>
        </ul>
      </nav>

      <!-- Logout di bagian bawah sidebar -->
      <div class="sidebar-footer">
        <a href="#" @click.prevent="logout" class="logout-link">
          <div class="nav-icon-wrapper">
            <font-awesome-icon :icon="['fas', 'sign-out-alt']" class="menu-icon" />
          </div>
          <span class="nav-text">Logout</span>
        </a>
      </div>
    </div>
  </div>
</template>

<script>
import Swal from 'sweetalert2';

export default {
  name: 'SidebarStaff',
  data() {
    return {
      isOpen: false,
    };
  },
  computed: {
    sidebarButtonIcon() {
      return this.isOpen ? '&times;' : '&#9776;';
    },
    currentPageName() {
      const routeName = this.$route.name || '';
      return routeName;
    },
  },
  methods: {
    toggleSidebar() {
      this.isOpen = !this.isOpen;
    },
    clearAuthData() {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    },

    async logout() {
      const result = await Swal.fire({
        title: 'Logout Confirmation',
        text: 'Are you sure you want to logout?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Yes, logout',
        cancelButtonText: 'Cancel',
      });

      if (result.isConfirmed) {
        this.clearAuthData();

        const Toast = Swal.mixin({
          toast: true,
          position: 'top-end',
          showConfirmButton: false,
          timer: 2000,
          timerProgressBar: true,
        });

        Toast.fire({
          icon: 'success',
          title: 'Logged out successfully',
        });

        setTimeout(() => {
          this.$router.push('/');
        }, 1000);
      }
    },
  },
};
</script>

<style scoped>
/* Header dengan hamburger, title, dan page name */
.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 24px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  z-index: 1001;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  transition: all 0.3s ease;
}

.open-sidebar-btn {
  font-size: 20px;
  background-color: transparent;
  border: none;
  color: #374151;
  cursor: pointer;
  margin-right: 12px;
  padding: 8px;
  border-radius: 8px;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
}

.open-sidebar-btn:hover {
  background-color: rgba(0, 0, 0, 0.05);
  transform: translateY(-1px);
}

.sidebar-title {
  font-weight: 700;
  font-size: 28px;
  letter-spacing: -0.5px;
}

.text-kema {
  color: v-bind('$colors.primary');
}

.text-telyu {
  color: v-bind('$colors.third');
}

/* Page Name Styling */
.page-name {
  display: flex;
  align-items: center;
}

.page-name-text {
  font-weight: 600;
  font-size: 20px;
  background: linear-gradient(135deg, v-bind('$colors.primary'), v-bind('$colors.third'));
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  padding-right: 24px;
}

/* Backdrop Overlay */
.sidebar-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.3);
  z-index: 999;
  opacity: 0;
  animation: fadeIn 0.3s ease forwards;
}

@keyframes fadeIn {
  to {
    opacity: 1;
  }
}

/* Sidebar styles */
.sidebar {
  background: linear-gradient(145deg, #ffffff 0%, #f8fafc 100%);
  position: fixed;
  top: 0;
  left: -280px;
  width: 280px;
  height: 100vh;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  z-index: 1000;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
  border-right: 1px solid rgba(0, 0, 0, 0.05);
}

.sidebar.open {
  left: 0;
  transform: translateX(0);
}

/* Brand Section */
.sidebar-brand {
  display: flex;
  align-items: center;
  padding: 32px 24px 24px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  margin-bottom: 8px;
}

.brand-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, v-bind('$colors.primary'), v-bind('$colors.third'));
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  position: relative;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.brand-dot {
  width: 8px;
  height: 8px;
  background: white;
  border-radius: 50%;
}

.brand-text {
  font-weight: 700;
  font-size: 20px;
  letter-spacing: -0.3px;
}

/* Navigation */
.sidebar-nav {
  flex: 1;
  padding: 16px 0;
  overflow-y: auto;
}

.nav-item {
  margin-bottom: 4px;
  padding: 0 16px;
}

.nav-link {
  display: flex;
  align-items: center;
  padding: 14px 16px;
  color: #6b7280;
  text-decoration: none;
  border-radius: 12px;
  transition: all 0.2s ease;
  position: relative;
  font-weight: 500;
  font-size: 15px;
  margin-bottom: 4px;
}

.nav-link:hover {
  background: linear-gradient(135deg, rgba(0, 0, 0, 0.03) 0%, rgba(0, 0, 0, 0.01) 100%);
  color: #374151;
  transform: translateX(4px);
}

.nav-link.router-link-exact-active,
.nav-link.router-link-active {
  background: linear-gradient(135deg, v-bind('$colors.primary') 0%, v-bind('$colors.third') 100%);
  color: white !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateX(0);
}

.nav-icon-wrapper {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.1);
  margin-right: 12px;
  transition: all 0.2s ease;
}

.nav-link.router-link-active .nav-icon-wrapper {
  background: rgba(255, 255, 255, 0.2);
}

.menu-icon {
  font-size: 16px;
}

.nav-text {
  flex: 1;
  font-weight: 500;
}

.nav-indicator {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: transparent;
  transition: all 0.2s ease;
}

.nav-link.router-link-active .nav-indicator {
  background: rgba(255, 255, 255, 0.8);
}

/* Footer / Logout */
.sidebar-footer {
  padding: 24px 16px 32px;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
}

.logout-link {
  display: flex;
  align-items: center;
  padding: 14px 16px;
  color: #ef4444;
  text-decoration: none;
  border-radius: 12px;
  transition: all 0.2s ease;
  font-weight: 500;
  font-size: 15px;
}

.logout-link:hover {
  background: rgba(239, 68, 68, 0.1);
  transform: translateX(4px);
}

.logout-link .nav-icon-wrapper {
  background: rgba(239, 68, 68, 0.1);
}

/* Mobile Responsiveness */
@media (max-width: 768px) {
  .sidebar {
    width: 100vw;
    left: -100vw;
  }
  
  .page-name-text {
    font-size: 16px;
  }
  
  .sidebar-title {
    font-size: 24px;
  }
}

/* Smooth animations */
* {
  box-sizing: border-box;
}

.sidebar * {
  transition: all 0.2s ease;
}
</style>