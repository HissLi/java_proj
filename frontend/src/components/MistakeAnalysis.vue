<script setup lang="ts">
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'

const issues = ref<string[]>([])
const counts = ref<number[]>([])
const tags = ref<string[]>([]) // 存储从接口获取的 tags
const selectedTag = ref<string>('') // 存储当前选择的 tag

// 获取 large_tags 数据
const fetchTags = async () => {
  const url = 'http://localhost:8080/api/get_large_tags'
  try {
    const response = await fetch(url)
    if (response.ok) {
      const data = await response.json() // 假设返回的是字符串数组
      tags.value = data
      selectedTag.value = data[0] // 设置默认值为 tags 的第一项
      fetchData(selectedTag.value) // 初始加载时根据第一个标签请求数据
    } else {
      console.error('Failed to fetch tags')
    }
  } catch (error) {
    console.error('Error fetching tags:', error)
  }
}

// 获取具体数据
const fetchData = async (tag: string) => {
  const url = `http://localhost:8080/api/get_top_mistake/${tag}`

  try {
    const response = await fetch(url)
    if (response.ok) {
      const data = await response.json() // 假设返回的是 JSON 格式
      issues.value = []
      counts.value = []
      data.forEach((item: string) => {
        const issueMatch = item.match(/Issue: (.+) Count:/)
        const countMatch = item.match(/Count: (\d+)/)
        if (issueMatch && countMatch) {
          issues.value.push(issueMatch[1])
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

// 当下拉框值改变时调用 fetchData
const onTagChange = (newTag: string) => {
  selectedTag.value = newTag
  fetchData(newTag) // 根据新的 tag 更新请求数据
}

onMounted(fetchTags)

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
        data: issues.value,
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
    <el-text class="mx-1 title-text">
      Top 10 mistakes in 
    </el-text>
    <!-- 下拉框 -->
    <el-select 
      v-model="selectedTag" 
      @change="onTagChange" 
      placeholder="Select a tag" 
      class="tag-select"
    >
      <el-option
        v-for="tag in tags"
        :key="tag"
        :label="tag"
        :value="tag"
      />
    </el-select>
  </div>
  <div id="chart" style="width: 100%; height: 400px;"></div>
</template>

<style scoped>
.horizontal-container {
  display: flex;
  align-items: center;
  gap: 10px; /* 增加元素间的间距 */
}

.title-text {
  font-size: 28px;
  margin-left: 10px;
  margin-top: 10px;
  flex-shrink: 0; /* 防止被压缩 */
}

.tag-select {
  margin-left: 20px;
  max-width: 200px; /* 设置最大宽度 */
  flex-grow: 0; /* 防止下拉框占据多余空间 */
}
</style>
