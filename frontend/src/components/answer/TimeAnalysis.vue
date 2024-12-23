<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import * as echarts from 'echarts'

const elapsedTime = ref<number[]>([])
const timeRange = ref<string>('1 Hour') // 默认时间范围
const timeRanges = [
  { value: '30 Minutes', label: '30 Minutes' },
  { value: '1 Hour', label: '1 Hour' },
  { value: '1 Day', label: '1 Day' },
  { value: '1 Week', label: '1 Week' },
  { value: '1 Month', label: '1 Month' },
  { value: '1 Year', label: '1 Year' }
]
const timeData = ref<number[]>([])
const timeLabels = ref<string[]>([])

const fetchData = async () => {
  const url = 'http://localhost:8080/api/get_elapsed_time'

  try {
    const response = await fetch(url)
    if (response.ok) {
      const data = await response.json() // 假设返回的是 JSON 格式
      elapsedTime.value = data
      console.log('Elapsed Time:', elapsedTime.value) // 调试信息
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
  switch (timeRange.value) {
    case '30 Minutes':
      counts = new Array(60).fill(0)
      labels = Array.from({ length: 30 }, (_, i) => `Minute ${i + 1}`)
      elapsedTime.value.forEach(time => {
        const index = Math.floor(time / 30)
        if (index < 60) counts[index]++
      })
      break
    case '1 Hour':
      counts = new Array(60).fill(0)
      labels = Array.from({ length: 60 }, (_, i) => `Minute ${i + 1}`)
      elapsedTime.value.forEach(time => {
        const index = Math.floor(time / 60)
        if (index < 60) counts[index]++
      })
      break
    case '1 Day':
      counts = new Array(24).fill(0)
      labels = Array.from({ length: 24 }, (_, i) => `Hour ${i + 1}`)
      elapsedTime.value.forEach(time => {
        const index = Math.floor(time / (60 * 60))
        if (index < 24) counts[index]++
      })
      break
    case '1 Week':
      counts = new Array(168).fill(0)
      labels = Array.from({ length: 168 }, (_, i) => `Hour ${i + 1}`)
      elapsedTime.value.forEach(time => {
        const index = Math.floor(time / (60 * 60))
        if (index < 168) counts[index]++
      })
      break
    case '1 Month':
      counts = new Array(30).fill(0)
      labels = Array.from({ length: 30 }, (_, i) => `Day ${i + 1}`)
      elapsedTime.value.forEach(time => {
        const index = Math.floor(time / (24 * 60 * 60))
        if (index < 30) counts[index]++
      })
      break
    case '1 Year':
      counts = new Array(52).fill(0)
      labels = Array.from({ length: 52 }, (_, i) => `Week ${i + 1}`)
      elapsedTime.value.forEach(time => {
        const index = Math.floor(time / (7 * 24 * 60 * 60))
        if (index < 52) counts[index]++
      })
      break
  }
  timeData.value = counts
  timeLabels.value = labels
}

const renderChart = () => {
  const chartDom = document.getElementById('chart') as HTMLElement
  const myChart = echarts.init(chartDom)
  const option = {
    visualMap: {
      show: false,
      type: 'continuous',
      min: 0,
      max: Math.max(...timeData.value),
      inRange: {
        color: ['#d94e5d','#eac736','#50a3ba']
      }
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: timeLabels.value
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: timeData.value,
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
watch(timeRange, () => {
  updateChartData()
  renderChart()
})
</script>

<template>
  <div>
    <div class="centered-container">
      <el-select
        v-model="timeRange"
        placeholder="Select"
        style="width: 240px; margin-top: 10px"
      >
        <el-option
          v-for="range in timeRanges"
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