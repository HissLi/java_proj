<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import * as echarts from 'echarts'

const tags = ref<string[]>([])
const counts = ref<number[]>([])
const selectedOption = ref('Question Numbers')

const options = [
  {
    value: 'Question Numbers',
    label: 'Question Numbers',
  },
  {
    value: 'User Engagement',
    label: 'User Engagement',
  },
]

const fetchData = async () => {
  const url = selectedOption.value === 'Question Numbers'
    ? 'http://localhost:8080/api/get_tag_by_count'
    : 'http://localhost:8080/api/get_tag_by_engagement'

  try {
    const response = await fetch(url)
    if (response.ok) {
      const data = await response.json() // 假设返回的是 JSON 格式
      tags.value = []
      counts.value = []
      data.forEach((item: string) => {
        const tagMatch = item.match(/Tag: (.+) Count:/)
        const countMatch = item.match(/Count: (\d+)/)
        if (tagMatch && countMatch) {
          tags.value.push(tagMatch[1])
          counts.value.push(parseInt(countMatch[1]))
        }
      })
      drawChart()
    } else {
      console.error('Failed to fetch data')
    }
  } catch (error) {
    console.error('Error:', error)
  }
}

onMounted(fetchData)

watch(selectedOption, fetchData)

const drawChart = () => {
  const chartDom = document.getElementById('chart') as HTMLElement
  const myChart = echarts.init(chartDom)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow',
      },
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%', // 增加底部空间以显示旋转后的标签
      containLabel: true,
    },
    xAxis: [
      {
        type: 'category',
        data: tags.value,
        axisTick: {
          alignWithLabel: true,
        },
        axisLabel: {
          interval: 0, // 显示所有标签
          rotate: 45, // 旋转标签以防止重叠
        },
      },
    ],
    yAxis: [
      {
        type: 'value',
      },
    ],
    series: [
      {
        name: 'Count',
        type: 'bar',
        barWidth: '60%',
        data: counts.value,
      },
    ],
  }
  myChart.setOption(option)
}
</script>

<template>
  <div class="horizontal-container">
    <el-text class="mx-1" style="font-size: 28px; margin-left: 10px; margin-top: 10px">
      Here are top 10 topics in StackOverflow sorted by
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
  <div id="chart" style="width: 100%; height: 400px;"></div>
</template>

<style scoped>
.horizontal-container {
  display: flex;
  align-items: center;
}
</style>