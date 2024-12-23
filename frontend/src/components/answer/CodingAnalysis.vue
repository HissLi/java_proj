<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'

const detailMatrix = ref<number[][]>([])

const fetchData = async () => {
  const url = 'http://localhost:8080/api/get_detail_matrix'

  try {
    const response = await fetch(url)
    if (response.ok) {
      const data = await response.json() // 假设返回的是 JSON 格式
      detailMatrix.value = data
      console.log('Detail Matrix:', detailMatrix.value) // 调试信息
      await nextTick() // 确保 DOM 元素已经渲染完成
      initChart()
    } else {
      console.error('Failed to fetch data')
    }
  } catch (error) {
    console.error('Error:', error)
  }
}

const initChart = () => {
  const chartDom = document.getElementById('chart') as HTMLElement
  if (!chartDom) {
    console.error('Chart DOM element not found')
    return
  }
  const myChart = echarts.init(chartDom)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['Accepted', 'Not accepted']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value'
    },
    yAxis: {
      type: 'category',
      data: ['Without code', 'With code']
    },
    series: [
      {
        name: 'Not accepted',
        type: 'bar',
        stack: 'total',
        label: {
          show: true,
          position: 'inside'
        },
        data: [detailMatrix.value[0][0], detailMatrix.value[0][1]]
      },
      {
        name: 'Accepted',
        type: 'bar',
        stack: 'total',
        label: {
          show: true,
          position: 'inside'
        },
        data: [detailMatrix.value[1][0], detailMatrix.value[1][1]]
      }
    ]
  }
  myChart.setOption(option)
}

onMounted(fetchData)
</script>

<template>
  <div>
    <div v-if="detailMatrix.length">
      <div id="chart" style="width: 600px; height: 400px;"></div>
    </div>
    <div v-else>
      <p>Loading...</p>
    </div>
  </div>
</template>

<style scoped>
</style>