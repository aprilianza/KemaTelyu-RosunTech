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
      <ul class="nav flex-column mt-5">
        <li class="nav-item">
          <router-link class="nav-link d-flex align-items-center" :to="{ name: 'Home' }">
            <font-awesome-icon :icon="['fas', 'home']" class="menu-icon" />
            <span>Home</span>
          </router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link d-flex align-items-center" :to="{ name: 'History' }">
            <font-awesome-icon :icon="['fas', 'history']" class="menu-icon" />
            <span>Riwayat</span>
          </router-link>
        </li>
        <!-- You can add more menu items here with icons -->
      </ul>

      <!-- Logout di bagian bawah sidebar -->
      <div class="mt-auto p-3">
        <a href="#" @click.prevent="confirmLogout" class="nav-link d-flex align-items-center text-danger">
          <font-awesome-icon :icon="['fas', 'sign-out-alt']" class="menu-icon" />
          <span>Logout</span>
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
        buttonsStyling: false,
        dangerMode: true,
      }).then((result) => {
        if (result.isConfirmed) {
          // Redirect ke halaman login
          this.$router.push({ name: 'Login' });

          // Atau panggil action Vuex/Pinia jika perlu
          // this.logout(); 
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

.nav-link {
  font-size: 16px;
  color: #333;
  padding-left: 20px;
  display: block;
  text-decoration: none;
  border-radius: 5px;
  transition: background 0.2s;
}

.nav-link:hover {
  background-color: #e9ecef;
}

.nav-link.router-link-exact-active,
.nav-link.router-link-active {
  font-weight: bold;
  color: v-bind('$colors.primary') !important;
  background-color: #e9ecef;
}
.menu-icon {
  font-size: 18px;
  width: 25px;
  margin-right: 12px;
  text-align: center;
}
</style>