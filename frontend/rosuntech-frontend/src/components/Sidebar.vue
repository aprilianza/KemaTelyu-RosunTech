<template>
  <div>
    <!-- Header dengan Hamburger + Judul + Page Name -->
    <div class="sidebar-header">
      <div class="d-flex align-items-center">
        <button
          class="btn open-sidebar-btn"
          @click="toggleSidebar"
          :aria-expanded="isOpen"
          v-html="sidebarButtonIcon"
        ></button>
        <span class="sidebar-title">
          <span class="text-kema">Kema</span><span class="text-telyu">telyu</span>
        </span>
      </div>
      <div class="page-name">
        <span class="page-name-text">{{ currentPageName }}</span>
      </div>
    </div>
    


    <!-- Sidebar -->
    <div :class="['sidebar', isOpen ? 'open' : '']">

      <!-- Menu -->
      <nav class="sidebar-nav">
      <ul class="nav flex-column mt-5">
        <li class="nav-item">
          <router-link class="nav-link d-flex align-items-center" :to="{ name: 'Home' }">
            <font-awesome-icon :icon="['fas', 'home']" class="menu-icon" />
            <span class="nav-text">Home</span>
          </router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link d-flex align-items-center" :to="{ name: 'History' }">
            <font-awesome-icon :icon="['fas', 'history']" class="menu-icon" />
            <span class="nav-text">Riwayat</span>
          </router-link>
        </li>
        <!-- You can add more menu items here with icons -->
      </ul>
    </nav>

      <!-- Logout di bagian bawah sidebar -->
      <div class="mt-auto p-3">
        <a href="#" @click.prevent="confirmLogout" class="nav-link d-flex align-items-center text-danger">
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
  name: 'SidebarAll',
  data() {
    return {
      isOpen: false,
    };
  },
  computed: {
    sidebarButtonIcon() {
      return this.isOpen ? '&laquo;' : '&#9776;';
    },
    currentPageName() {
      // Get current route name and format it
      const routeName = this.$route.name || '';
      return routeName;
    }
  },
  methods: {
    toggleSidebar() {
      this.isOpen = !this.isOpen;
    },
     /* menghapus token & profil di localStorage */
    clearAuthData() {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    },
   confirmLogout() {
      Swal.fire({
        title: 'Apakah anda yakin ingin logout?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Ya',
        cancelButtonText: 'Batal',
        customClass: {
          confirmButton: 'btn btn-danger me-2',
          cancelButton: 'btn btn-secondary'
        },
        buttonsStyling: false
      }).then(result => {
        if (result.isConfirmed) {
          this.clearAuthData();               // ⬅️ panggil di sini
          this.$router.push({ name: 'Login' });
        }
      });
    }
  }
};
</script>

<style scoped>
/* Header dengan hamburger, title, dan page name */
.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 15px 20px;
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  z-index: 1001;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
}

.open-sidebar-btn {
  font-size: 24px;
  background-color: transparent;
  border: none;
  color: #333;
  cursor: pointer;
  margin-right: 10px;
}

.sidebar-title {
  margin-left: 10px;
  font-weight: bold;
  font-size: 30px;
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
  font-weight: bold;
  font-size: 24px;
  background: linear-gradient(90deg, v-bind('$colors.primary'), v-bind('$colors.third'));
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  padding-right: 20px;
}

/* Sidebar styles */
.sidebar {
  background-color: #ffffff; /* Disamakan dengan header */
  position: fixed;
  top: 60px;
  left: -250px;
  width: 250px;
  height: calc(100vh - 60px);
  transition: left 0.3s ease;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  padding-top: 10px;
  overflow-y: auto;
  box-shadow: 1px 0 4px rgba(0, 0, 0, 0.1); /* Agar lebih jelas batasnya */
}

.sidebar.open {
  left: 0;
}

/* Navigation Items */
.nav-item {
  margin-bottom: 10px;
}

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

.menu-icon {
  font-size: 18px;
  width: 25px;
  margin-right: 12px;
  text-align: center;
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