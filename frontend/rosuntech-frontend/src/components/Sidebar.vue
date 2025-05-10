<template>
  <div>
    <!-- Header dengan Hamburger + Judul -->
    <div class="sidebar-header">
      <button
        class="btn open-sidebar-btn"
        @click="toggleSidebar"
        :aria-expanded="isOpen"
        v-html="sidebarButtonIcon"
      ></button>
      <span class="sidebar-title">Kematelyu</span>
    </div>

    <!-- Sidebar -->
    <div :class="['sidebar', isOpen ? 'open' : '']">
      <!-- Menu -->
      <ul class="nav flex-column mt-5">
        <li class="nav-item">
          <router-link class="nav-link d-flex align-items-center" :to="{ name: 'Home' }">
            <font-awesome-icon icon="home" class="me-2" />
            Home
          </router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link d-flex align-items-center" :to="{ name: 'History' }">
            <font-awesome-icon icon="calendar-check" class="me-2" />
            History
          </router-link>
        </li>
      </ul>

      <!-- Logout di bagian bawah sidebar -->
      <div class="mt-auto p-3">
        <router-link class="nav-link d-flex align-items-center text-danger" :to="{ name: 'Login' }">
          <font-awesome-icon icon="sign-out-alt" class="me-2" />
          Logout
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "SidebarAll",
  data() {
    return {
      isOpen: false // Default state untuk sidebar (tertutup)
    };
  },
  computed: {
    // Mengembalikan ikon sesuai status sidebar
    sidebarButtonIcon() {
      return this.isOpen ? '&laquo;' : '&#9776;';
    }
  },
  methods: {
    toggleSidebar() {
      this.isOpen = !this.isOpen;
    }
  }
};
</script>

<style scoped>
/* Header dengan hamburger dan title */
.sidebar-header {
  display: flex;
  align-items: center;
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
  font-size: 18px;
  color: #007bff;
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
  color: #007bff !important;
  background-color: #e9ecef;
}
</style>