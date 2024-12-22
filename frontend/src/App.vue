<script setup lang="ts">
import { ref, onMounted } from 'vue'

const message = ref('')

onMounted(async () => {
  try {
    const response = await fetch('http://localhost:8080/hello')
    if (response.ok) {
      message.value = await response.text()
    } else {
      console.error('Failed to fetch data')
    }
  } catch (error) {
    console.error('Error:', error)
  }
})
</script>

<template>
  <el-main>
    <p>{{ message }}</p>
  </el-main>
</template>

<style scoped></style>
