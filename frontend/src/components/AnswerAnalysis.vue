<template>
  <div class="horizontal-container">
    <el-text class="title">
      Best Answer Distribution by
    </el-text>
    <el-select
      v-model="selectedOption"
      placeholder="Select"
      style="width: 240px; margin-left: 10px"
    >
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      />
    </el-select>
  </div>
  <component :is="currentComponent" />
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import TimeAnalysis from './answer/TimeAnalysis.vue'
import ReputationAnalysis from './answer/ReputationAnalysis.vue'
import LengthAnalysis from './answer/LengthAnalysis.vue'
import CodingAnalysis from './answer/CodingAnalysis.vue'

const selectedOption = ref('Time')

const options = [
  { value: 'Time', label: 'Time' },
  { value: 'Reputation', label: 'Reputation' },
  { value: 'Length', label: 'Length' },
  { value: 'Coding', label: 'Coding' }
]

const currentComponent = computed(() => {
  switch (selectedOption.value) {
    case 'Reputation':
      return ReputationAnalysis
    case 'Length':
      return LengthAnalysis
    case 'Coding':
      return CodingAnalysis
    default:
      return TimeAnalysis
  }
})
</script>

<style scoped>
.horizontal-container {
  display: flex;
  align-items: center;
}
.title {
  font-size: 28px;
  margin-left: 10px;
  margin-top: 10px;
}
</style>