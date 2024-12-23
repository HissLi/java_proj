<script setup lang="ts">
import { ref } from 'vue'
import { Cpu } from '@element-plus/icons-vue'
import Analysis from './DataAnalysis.vue'

const currentTopic = ref('Topic')

function updateTopic(topic: string) {
  currentTopic.value = topic
}
</script>

<template>
  <div class="container">
    <div id="header">
      <el-icon style="font-size: 48px"><Cpu /></el-icon>
      <el-text class="mx-1" style="font-size: 48px">StackOverflow Data Analysis System</el-text>
    </div>
    <div class="content">
      <div id="asider" style="display: flex; flex-direction: column">
        <el-button type="" text style="font-size: 36px; margin-top: 20px;" @click="updateTopic('Topic')">Topic</el-button>
        <el-button type="" text style="font-size: 36px; margin-top: 40px;" @click="updateTopic('Mistake')">Mistake</el-button>
        <el-button type="" text style="font-size: 36px; margin-top: 40px;" @click="updateTopic('Answer')">Answer</el-button>
      </div>
      <div id="body">
        <Analysis :currentTopic="currentTopic" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: rgb(235.9, 245.3, 255);
}

#header {
  height: 100px;
}

.content {
  display: flex;
  flex: 1;
}

#asider {
  width: 200px;
  background-color: rgb(216.8, 235.6, 255);
}

#body {
  flex: 1;
  background-color: #ffffff;
}
</style>