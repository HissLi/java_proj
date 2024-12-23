<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import * as echarts from 'echarts'

const lengthValues = ref<number[]>([])
const lengthRange = ref<string>('100') // 默认长度范围
const lengthRanges = [
  { value: '100', label: '100' },
  { value: '200', label: '200' },
  { value: '500', label: '500' },
  { value: '1000', label: '1000' }
]
const lengthData = ref<number[]>([])
const lengthLabels = ref<string[]>([])

const fetchData = async () => {
  const url = 'http://localhost:8080/api/get_answer_length'

  try {
    const response = await fetch(url)
    if (response.ok) {
      const data = await response.json() // 假设返回的是 JSON 格式
      lengthValues.value = data
      console.log('Length Values:', lengthValues.value) // 调试信息
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
  let maxLength: number = 0
  let step: number = 0

  switch (lengthRange.value) {
    case '100':
      maxLength = 100
      step = 1
      break
    case '200':
      maxLength = 200
      step = 2
      break
    case '500':
      maxLength = 500
      step = 5
      break
    case '1000':
      maxLength = 1000
      step = 10
      break
  }

  const numBuckets = Math.ceil(maxLength / step)
  counts = new Array(numBuckets).fill(0)
  labels = Array.from({ length: numBuckets }, (_, i) => `${i * step} - ${(i + 1) * step}`)

  lengthValues.value.forEach(value => {
    if (value <= maxLength) {
      const index = Math.floor(value / step)
      counts[index]++
    }
  })

  lengthData.value = counts
  lengthLabels.value = labels
}

const renderChart = () => {
  const chartDom = document.getElementById('chart') as HTMLElement
  const myChart = echarts.init(chartDom)
  const option = {
    visualMap: {
      show: false,
      type: 'continuous',
      min: 0,
      max: Math.max(...lengthData.value),
      inRange: {
        color: ['#d94e5d','#eac736','#50a3ba']
      }
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: lengthLabels.value
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: lengthData.value,
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
watch(lengthRange, () => {
  updateChartData()
  renderChart()
})
</script>

<template>
  <div>
    <div class="centered-container">
      <el-select
        v-model="lengthRange"
        placeholder="Select"
        style="width: 240px; margin-top: 10px"
      >
        <el-option
          v-for="range in lengthRanges"
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
