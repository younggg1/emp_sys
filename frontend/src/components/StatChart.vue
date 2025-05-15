<template>
  <div
    class="stat-chart"
    :style="{ height: height, width: width }"
    ref="chartDom"
  ></div>
</template>

<script setup>
import { ref, onMounted, watch, defineProps, defineExpose } from 'vue'
import * as echarts from 'echarts/core'
import { PieChart, LineChart, BarChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'

// 注册必须的组件
echarts.use([
  PieChart,
  LineChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  CanvasRenderer
])

const props = defineProps({
  chartType: {
    type: String,
    default: 'pie', // pie, line, bar
    validator: (value) => ['pie', 'line', 'bar'].includes(value)
  },
  title: {
    type: String,
    default: ''
  },
  data: {
    type: Array,
    default: () => []
  },
  xAxisData: {
    type: Array,
    default: () => []
  },
  height: {
    type: String,
    default: '400px'
  },
  width: {
    type: String,
    default: '100%'
  }
})

const chartDom = ref(null)
let chart = null

// 初始化图表
const initChart = () => {
  if (!chartDom.value) return
  
  chart = echarts.init(chartDom.value)
  updateChart()
  
  // 响应窗口大小变化
  window.addEventListener('resize', () => {
    chart && chart.resize()
  })
}

// 更新图表数据
const updateChart = () => {
  if (!chart) return
  
  const option = {
    title: {
      text: props.title,
      left: 'center'
    },
    tooltip: {
      trigger: props.chartType === 'pie' ? 'item' : 'axis'
    },
    legend: {
      orient: 'horizontal',
      bottom: 10
    }
  }
  
  if (props.chartType === 'pie') {
    option.series = [
      {
        type: 'pie',
        radius: '50%',
        data: props.data.map(item => ({
          name: item.label,
          value: item.value
        })),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  } else if (props.chartType === 'line' || props.chartType === 'bar') {
    option.xAxis = {
      type: 'category',
      data: props.xAxisData
    }
    option.yAxis = {
      type: 'value'
    }
    option.series = [
      {
        type: props.chartType,
        data: props.data
      }
    ]
  }
  
  chart.setOption(option)
}

// 手动更新图表方法
const refreshChart = () => {
  if (chart) {
    updateChart()
  }
}

// 当数据改变时更新图表
watch(
  () => props.data,
  () => updateChart(),
  { deep: true }
)

// 当图表类型改变时更新图表
watch(
  () => props.chartType,
  () => updateChart()
)

// 当组件挂载完成初始化图表
onMounted(() => {
  initChart()
})

defineExpose({
  refreshChart
})
</script>

<style scoped>
.stat-chart {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style> 
