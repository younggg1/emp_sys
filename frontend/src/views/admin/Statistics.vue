<template>
  <div class="statistics">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>就业情况概览</span>
              <el-input
                v-model="yearFilter"
                placeholder="请输入年份"
                style="width: 180px"
                @keyup.enter="fetchAndUpdateData"
              >
                <template #append>
                  <el-button @click="fetchAndUpdateData">
                    <el-icon><Search /></el-icon>
                  </el-button>
                </template>
              </el-input>
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
      <el-col :span="8">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>就业地区分布</span>
            </div>
          </template>
          <div ref="regionChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>企业性质分布</span>
            </div>
          </template>
          <div ref="companyChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>薪资区间分布</span>
            </div>
          </template>
          <div ref="salaryChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ArrowUp, ArrowDown, Search } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getCompanyNatureStats, getSalaryStats, getRegionStats, getBasicStats } from '@/api/admin'
import { ElMessage } from 'element-plus'

// 年份输入
const yearFilter = ref('2025')

// 概览数据
const summaryData = ref([
  { label: '毕业生总数', value: '0人', trend: 0 },
  { label: '就业人数', value: '0人', trend: 0 },
  { label: '就业率', value: '0%', trend: 0 },
  { label: '平均薪资', value: '0元', trend: 0 }
])

// 图表实例和引用
const regionChartRef = ref(null)
const companyChartRef = ref(null)
const salaryChartRef = ref(null)

// 各图表实例
let regionChart = null
let companyChart = null
let salaryChart = null

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
      formatter: '{a} <br/>{b}: {c}人 ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
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
          show: true,
          position: 'outside',
          formatter: '{b}: {c}人'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '16',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: true
        },
        data: []
      }
    ]
  })

  // 初始化企业性质分布图表
  companyChart = echarts.init(companyChartRef.value)
  companyChart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c}人 ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '企业性质',
        type: 'pie',
        radius: '60%',
        center: ['55%', '50%'],
        data: [],
        label: {
          show: true,
          position: 'outside',
          formatter: '{b}: {c}人'
        },
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
        data: ['5000以下', '5000-8000', '8000-12000', '12000以上'],
        axisTick: {
          alignWithLabel: true
        }
      }
    ],
    yAxis: [
      {
        type: 'value',
        name: '人数'
      }
    ],
    series: [
      {
        name: '人数',
        type: 'bar',
        barWidth: '60%',
        data: [],
        label: {
          show: true,
          position: 'top',
          formatter: '{c}人'
        }
      }
    ]
  })
}

// 获取数据并更新图表
const fetchAndUpdateData = async () => {
  try {
    // 获取基础统计数据
    const basicStatsResponse = await getBasicStats();
    const basicStats = basicStatsResponse.data;
    
    // 更新概览数据
    summaryData.value = [
      { 
        label: '毕业生总数', 
        value: `${basicStats.total_students}人`, 
        trend: 0 
      },
      { 
        label: '就业人数', 
        value: `${basicStats.total_employed}人`, 
        trend: 0 
      },
      { 
        label: '就业率', 
        value: `${basicStats.employment_rate}%`, 
        trend: 0 
      },
      { 
        label: '平均薪资', 
        value: `${Math.round(basicStats.avg_salary)}元`, 
        trend: 0 
      }
    ]

    // 获取并更新图表数据
    const [companyNatureResponse, salaryResponse, regionResponse] = await Promise.all([
      getCompanyNatureStats(yearFilter.value),
      getSalaryStats(yearFilter.value),
      getRegionStats(yearFilter.value)
    ])

    // 更新企业性质分布图表
    if (companyNatureResponse.data && Array.isArray(companyNatureResponse.data)) {
      companyChart.setOption({
        series: [{
          data: companyNatureResponse.data
        }]
      })
    }

    // 更新薪资分布图表
    if (salaryResponse.data && Array.isArray(salaryResponse.data)) {
      salaryChart.setOption({
        series: [{
          data: salaryResponse.data
        }]
      })
    }

    // 更新地区分布图表
    if (regionResponse.data && Array.isArray(regionResponse.data)) {
      regionChart.setOption({
        series: [{
          data: regionResponse.data
        }]
      })
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
    ElMessage.error(error.response?.data?.message || '获取统计数据失败，请稍后重试')
  }
}

// 生成随机颜色
const getRandomColor = () => {
  const colors = ['#91cc75', '#5470c6', '#fac858', '#ee6666', '#73c0de', '#3ba272']
  return colors[Math.floor(Math.random() * colors.length)]
}

// 窗口大小变化时调整图表
const handleResize = () => {
  regionChart && regionChart.resize()
  companyChart && companyChart.resize()
  salaryChart && salaryChart.resize()
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