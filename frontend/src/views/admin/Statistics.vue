<template>
  <div class="statistics">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>就业情况概览</span>
              <el-select v-model="yearFilter" placeholder="选择年份" style="width: 120px">
                <el-option
                  v-for="item in yearOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </div>
          </template>
          
          <el-row :gutter="20">
            <el-col :span="6" v-for="(item, index) in summaryData" :key="index">
              <div class="summary-item" :class="`summary-item-${index}`">
                <div class="summary-value">{{ item.value }}</div>
                <div class="summary-label">{{ item.label }}</div>
                <div class="summary-trend" v-if="item.trend !== 0">
                  <el-icon :class="item.trend > 0 ? 'trend-up' : 'trend-down'">
                    <component :is="item.trend > 0 ? 'ArrowUp' : 'ArrowDown'" />
                  </el-icon>
                  <span>{{ Math.abs(item.trend) }}%</span>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>就业地区分布</span>
            </div>
          </template>
          <div ref="regionChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>企业性质分布</span>
            </div>
          </template>
          <div ref="companyChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>薪资区间分布</span>
            </div>
          </template>
          <div ref="salaryChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>专业类别分布</span>
            </div>
          </template>
          <div ref="majorChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>学院就业率对比</span>
            </div>
          </template>
          <div ref="collegeChartRef" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>月度就业人数趋势</span>
            </div>
          </template>
          <div ref="trendChartRef" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

// 年份下拉选项
const yearFilter = ref('2023')
const yearOptions = [
  { label: '2023年', value: '2023' },
  { label: '2022年', value: '2022' },
  { label: '2021年', value: '2021' },
  { label: '2020年', value: '2020' }
]

// 概览数据
const summaryData = ref([
  { label: '毕业生总数', value: '2,568人', trend: 0 },
  { label: '就业人数', value: '2,380人', trend: 5.2 },
  { label: '就业率', value: '92.7%', trend: 3.1 },
  { label: '平均薪资', value: '8,450元', trend: 7.6 }
])

// 图表实例和引用
const regionChartRef = ref(null)
const companyChartRef = ref(null)
const salaryChartRef = ref(null)
const majorChartRef = ref(null)
const collegeChartRef = ref(null)
const trendChartRef = ref(null)

// 各图表实例
let regionChart = null
let companyChart = null
let salaryChart = null
let majorChart = null
let collegeChart = null
let trendChart = null

// 监听年份变化
watch(yearFilter, () => {
  // 重新获取数据并更新图表
  fetchAndUpdateData()
})

// 初始化所有图表
const initCharts = () => {
  // 初始化地区分布图表
  regionChart = echarts.init(regionChartRef.value)
  regionChart.setOption({
    title: {
      text: '',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      data: ['北京', '上海', '广州', '深圳', '杭州', '成都', '武汉', '其他']
    },
    series: [
      {
        name: '就业地区',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '16',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 450, name: '北京' },
          { value: 410, name: '上海' },
          { value: 380, name: '广州' },
          { value: 350, name: '深圳' },
          { value: 300, name: '杭州' },
          { value: 250, name: '成都' },
          { value: 150, name: '武汉' },
          { value: 90, name: '其他' }
        ]
      }
    ]
  })

  // 初始化企业性质分布图表
  companyChart = echarts.init(companyChartRef.value)
  companyChart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      data: ['国企', '私企', '外企', '事业单位', '公务员', '其他']
    },
    series: [
      {
        name: '企业性质',
        type: 'pie',
        radius: '60%',
        center: ['55%', '50%'],
        data: [
          { value: 680, name: '私企' },
          { value: 520, name: '国企' },
          { value: 420, name: '外企' },
          { value: 310, name: '事业单位' },
          { value: 280, name: '公务员' },
          { value: 170, name: '其他' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  })

  // 初始化薪资区间分布图表
  salaryChart = echarts.init(salaryChartRef.value)
  salaryChart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: [
      {
        type: 'category',
        data: ['<5k', '5k-8k', '8k-10k', '10k-15k', '15k-20k', '>20k'],
        axisTick: {
          alignWithLabel: true
        }
      }
    ],
    yAxis: [
      {
        type: 'value'
      }
    ],
    series: [
      {
        name: '人数',
        type: 'bar',
        barWidth: '60%',
        data: [
          { value: 320, itemStyle: { color: '#91cc75' } },
          { value: 650, itemStyle: { color: '#5470c6' } },
          { value: 720, itemStyle: { color: '#fac858' } },
          { value: 420, itemStyle: { color: '#ee6666' } },
          { value: 180, itemStyle: { color: '#73c0de' } },
          { value: 90, itemStyle: { color: '#3ba272' } }
        ]
      }
    ]
  })

  // 初始化专业类别分布图表
  majorChart = echarts.init(majorChartRef.value)
  majorChart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      data: ['理工类', '文史类', '体育类', '艺术类']
    },
    series: [
      {
        name: '专业类别',
        type: 'pie',
        radius: '60%',
        center: ['55%', '50%'],
        data: [
          { value: 1420, name: '理工类' },
          { value: 680, name: '文史类' },
          { value: 125, name: '体育类' },
          { value: 155, name: '艺术类' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  })

  // 初始化学院就业率对比图表
  collegeChart = echarts.init(collegeChartRef.value)
  collegeChart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['总人数', '就业人数', '就业率']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: [
      {
        type: 'category',
        data: ['计算机学院', '经济学院', '理学院', '文学院', '工学院', '艺术学院', '体育学院', '医学院']
      }
    ],
    yAxis: [
      {
        type: 'value',
        name: '人数',
        position: 'left'
      },
      {
        type: 'value',
        name: '就业率(%)',
        position: 'right',
        min: 70,
        max: 100
      }
    ],
    series: [
      {
        name: '总人数',
        type: 'bar',
        stack: 'total',
        barWidth: 30,
        emphasis: {
          focus: 'series'
        },
        data: [420, 360, 320, 310, 280, 180, 150, 130]
      },
      {
        name: '就业人数',
        type: 'bar',
        stack: 'total',
        barWidth: 30,
        emphasis: {
          focus: 'series'
        },
        data: [410, 330, 290, 280, 250, 150, 130, 120]
      },
      {
        name: '就业率',
        type: 'line',
        yAxisIndex: 1,
        data: [97.6, 91.7, 90.6, 90.3, 89.3, 83.3, 86.7, 92.3],
        label: {
          show: true,
          formatter: '{c}%'
        }
      }
    ]
  })

  // 初始化月度就业趋势图表
  trendChart = echarts.init(trendChartRef.value)
  trendChart.setOption({
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['就业人数']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '就业人数',
        type: 'line',
        stack: 'Total',
        smooth: true,
        lineStyle: {
          width: 3,
          shadowColor: 'rgba(0,0,0,0.3)',
          shadowBlur: 10,
          shadowOffsetY: 8
        },
        data: [180, 120, 130, 110, 100, 320, 580, 420, 190, 95, 80, 55],
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: 'rgba(80, 141, 255, 0.8)'
            },
            {
              offset: 1,
              color: 'rgba(80, 141, 255, 0.1)'
            }
          ])
        }
      }
    ]
  })
}

// 获取数据并更新图表
const fetchAndUpdateData = () => {
  // 这里应该调用API根据选择的年份获取数据
  // 模拟不同年份的数据
  if (yearFilter.value === '2022') {
    summaryData.value = [
      { label: '毕业生总数', value: '2,450人', trend: -2.5 },
      { label: '就业人数', value: '2,210人', trend: -3.8 },
      { label: '就业率', value: '90.2%', trend: -1.2 },
      { label: '平均薪资', value: '7,850元', trend: 4.2 }
    ]
  } else if (yearFilter.value === '2021') {
    summaryData.value = [
      { label: '毕业生总数', value: '2,280人', trend: 1.8 },
      { label: '就业人数', value: '2,050人', trend: 2.1 },
      { label: '就业率', value: '89.9%', trend: 0.5 },
      { label: '平均薪资', value: '7,530元', trend: 3.0 }
    ]
  } else if (yearFilter.value === '2020') {
    summaryData.value = [
      { label: '毕业生总数', value: '2,120人', trend: -5.2 },
      { label: '就业人数', value: '1,870人', trend: -8.7 },
      { label: '就业率', value: '88.2%', trend: -3.5 },
      { label: '平均薪资', value: '7,310元', trend: -1.8 }
    ]
  } else {
    summaryData.value = [
      { label: '毕业生总数', value: '2,568人', trend: 4.8 },
      { label: '就业人数', value: '2,380人', trend: 7.7 },
      { label: '就业率', value: '92.7%', trend: 2.8 },
      { label: '平均薪资', value: '8,450元', trend: 7.6 }
    ]
  }

  // 更新其他图表数据...
  // 此处仅是示例，实际需要根据API返回数据更新各个图表
}

// 窗口大小变化时调整图表
const handleResize = () => {
  regionChart && regionChart.resize()
  companyChart && companyChart.resize()
  salaryChart && salaryChart.resize()
  majorChart && majorChart.resize()
  collegeChart && collegeChart.resize()
  trendChart && trendChart.resize()
}

onMounted(() => {
  // 初始化图表
  initCharts()
  
  // 监听窗口大小变化
  window.addEventListener('resize', handleResize)
  
  // 获取初始数据
  fetchAndUpdateData()
})

</script>

<style scoped>
.statistics {
  max-width: 1200px;
  margin: 0 auto;
}

.chart-card {
  margin-bottom: 20px;
  border-radius: 4px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  color: #303133;
}

:deep(.el-card__header) {
  padding: 16px 20px;
  border-bottom: 1px solid #ebeef5;
  background-color: #f8f9fc;
}

.summary-item {
  padding: 20px;
  border-radius: 8px;
  color: #fff;
  height: 100px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.summary-item-0 {
  background: linear-gradient(135deg, #50a5f1, #5081f1);
}

.summary-item-1 {
  background: linear-gradient(135deg, #42d392, #647eff);
}

.summary-item-2 {
  background: linear-gradient(135deg, #e6a23c, #f56c6c);
}

.summary-item-3 {
  background: linear-gradient(135deg, #8e44ad, #bd93f9);
}

.summary-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 5px;
}

.summary-label {
  font-size: 14px;
}

.summary-trend {
  margin-top: 10px;
  display: flex;
  align-items: center;
  font-size: 12px;
}

.trend-up {
  color: #72f8ba;
  margin-right: 4px;
}

.trend-down {
  color: #f58989;
  margin-right: 4px;
}
</style> 