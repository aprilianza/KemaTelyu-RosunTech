<template>
  <div class="event-container">
    <div v-for="(event, index) in events" :key="index" class="event-item">
      <div class="event-title">{{ event.title }}</div>
      <div class="event-date">Date Created: {{ event.dateCreated }}</div>
      <div class="approval">
        <span>Logo Approve?</span>
        <button @click="approveLogo(event.id)">Get!!</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      events: [
        { id: 1, title: 'Judul Event 1', dateCreated: '2025-05-01' },
        { id: 2, title: 'Judul Event 2', dateCreated: '2025-05-02' },
        { id: 3, title: 'Judul Event 3', dateCreated: '2025-05-03' }
      ]
    };
  },
  methods: {
    approveLogo(eventId) {
      alert(`Logo approved for event ID: ${eventId}`);
    }
  }
};
</script>

<style scoped>
.event-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.event-item {
  background-color: #f0f0f0;
  padding: 10px;
  border-radius: 5px;
}

.event-title {
  font-weight: bold;
}

.event-date {
  color: #777;
}

.approval {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

button {
  background-color: #4caf50;
  color: white;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 3px;
}

button:hover {
  background-color: #45a049;
}
</style>
