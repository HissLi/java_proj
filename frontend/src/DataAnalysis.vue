<script setup lang="ts">
import { defineProps, computed } from 'vue'
import TopicAnalysis from './components/TopicAnalysis.vue'
import MistakeAnalysis from './components/MistakeAnalysis.vue'
import AnswerAnalysis from './components/AnswerAnalysis.vue'

const props = defineProps<{ currentTopic: string }>()

const currentComponent = computed(() => {
  switch (props.currentTopic) {
    case 'Topic':
      return TopicAnalysis
    case 'Mistake':
      return MistakeAnalysis
    case 'Answer':
      return AnswerAnalysis
    default:
      return null
  }
})
</script>

<template>
  <component :is="currentComponent" />
</template>

<style scoped></style>