<script setup lang="ts">
import { ref, onMounted } from 'vue'

const message = ref<string>('Loading...')
const error = ref<string>('')

const fetchHello = async () => {
  try {
    const response = await fetch('/api/hello')
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    const data = await response.text()
    message.value = data
  } catch (e) {
    error.value = e instanceof Error ? e.message : 'Unknown error'
    message.value = 'Failed to fetch message'
  }
}

onMounted(() => {
  fetchHello()
})
</script>

<template>
  <div class="container">
    <h1>Hospital Management System</h1>
    <div class="message-box">
      <h2>Backend Message:</h2>
      <p v-if="!error" class="message">{{ message }}</p>
      <p v-else class="error">{{ error }}</p>
    </div>
    <button @click="fetchHello" class="refresh-btn">Refresh</button>
  </div>
</template>

<style scoped>
.container {
  max-width: 800px;
  margin: 50px auto;
  padding: 20px;
  text-align: center;
  font-family: Arial, sans-serif;
}

h1 {
  color: #42b883;
  margin-bottom: 30px;
}

.message-box {
  background: #f5f5f5;
  border-radius: 8px;
  padding: 30px;
  margin: 20px 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

h2 {
  color: #333;
  margin-bottom: 15px;
}

.message {
  font-size: 20px;
  color: #42b883;
  font-weight: bold;
}

.error {
  color: #f56c6c;
  font-size: 16px;
}

.refresh-btn {
  background: #42b883;
  color: white;
  border: none;
  padding: 12px 30px;
  font-size: 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s;
}

.refresh-btn:hover {
  background: #359268;
}
</style>
