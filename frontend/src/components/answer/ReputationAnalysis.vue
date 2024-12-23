<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import * as echarts from 'echarts'

const reputationValues = ref<number[]>([])
const reputationRange = ref<string>('10000') // 默认声望范围
const reputationRanges = [
  { value: '100', label: '100' },
  { value: '500', label: '500' },
  { value: '1000', label: '1000' },
  { value: '5000', label: '5000' },
  { value: '10000', label: '10000' }
]
const reputationData = ref<number[]>([])
const reputationLabels = ref<string[]>([])

const fetchData = async () => {
  const url = 'http://localhost:8080/api/get_reputation_value'

  try {
    const response = await fetch(url)
    if (response.ok) {
      const data = await response.json() // 假设返回的是 JSON 格式
      reputationValues.value = data
      console.log('Reputation Values:', reputationValues.value) // 调试信息
      updateChartData()
      renderChart()
    } else {
      console.error('Failed to fetch data')
    }
  } catch (error) {
    console.error('Error:', error)
  }
}

const updateChartData = () => {
  let counts: number[] = []
  let labels: string[] = []
  let maxReputation: number = 0
  let step: number = 0

  switch (reputationRange.value) {
    case '100':
      maxReputation = 100
      step = 1
      break
    case '500':
      maxReputation = 500
      step = 5
      break
    case '1000':
      maxReputation = 1000
      step = 10
      break
    case '5000':
      maxReputation = 5000
      step = 50
      break
    case '10000':
      maxReputation = 10000
      step = 100
      break
  }

  const numBuckets = Math.ceil(maxReputation / step)
  counts = new Array(numBuckets).fill(0)
  labels = Array.from({ length: numBuckets }, (_, i) => `${i * step} - ${(i + 1) * step}`)

  reputationValues.value.forEach(value => {
    if (value <= maxReputation) {
      const index = Math.floor(value / step)
      counts[index]++
    }
  })

  reputationData.value = counts
  reputationLabels.value = labels
}

const renderChart = () => {
  const chartDom = document.getElementById('chart') as HTMLElement
  const myChart = echarts.init(chartDom)
  const option = {
    visualMap: {
      show: false,
      type: 'continuous',
      min: 0,
      max: Math.max(...reputationData.value),
      inRange: {
        color: ['#d94e5d','#eac736','#50a3ba']
      }
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: reputationLabels.value
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: reputationData.value,
        type: 'line',
        showSymbol: false,
        lineStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              { offset: 0, color: '#d94e5d' },
              { offset: 0.5, color: '#eac736' },
              { offset: 1, color: '#50a3ba' }
            ]
          }
        }
      }
    ]
  }
  myChart.setOption(option)
}

onMounted(fetchData)
watch(reputationRange, () => {
  updateChartData()
  renderChart()
})
</script>

<template>
  <div>
    <div class="centered-container">
      <el-select
        v-model="reputationRange"
        placeholder="Select"
        style="width: 240px; margin-top: 10px"
      >
        <el-option
          v-for="range in reputationRanges"
          :key="range.value"
          :label="range.label"
          :value="range.value"
        />
      </el-select>
    </div>
    <div id="chart" style="width: 100%; height: 400px; margin-top: 10px;"></div>
  </div>
</template>

<style scoped>
.centered-container {
  display: flex;
  justify-content: center;
  width: 100%;
}
</style>