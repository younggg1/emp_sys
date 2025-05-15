<template>
  <Layout>
    <template #menu>
      <el-menu-item index="/counselor">
        <el-icon><user /></el-icon>
        <span>班级学生</span>
      </el-menu-item>
      <el-menu-item index="/counselor/employment">
        <el-icon><document /></el-icon>
        <span>就业信息管理</span>
      </el-menu-item>
      <el-menu-item index="/counselor/feedback">
        <el-icon><chat-dot-round /></el-icon>
        <span>就业反馈管理</span>
      </el-menu-item>
      <el-menu-item index="/counselor/statistics">
        <el-icon><pie-chart /></el-icon>
        <span>统计分析</span>
      </el-menu-item>
    </template>

    <template #header-title>
      辅导员端 - 统计分析
    </template>
    
    <div class="statistics">
      <!-- 筛选条件 -->
      <el-card class="filter-card">
        <template #header>
          <div class="card-header">
            <span>筛选条件</span>
          </div>
        </template>
        <el-form :inline="true" :model="filterForm" label-width="100px">
          <el-form-item label="毕业年份">
            <el-select v-model="filterForm.year" placeholder="选择毕业年份" @change="handleFilterChange">
              <el-option 
                v-for="year in yearOptions" 
                :key="year" 
                :label="year + '年'" 
                :value="year" 
              />
            </el-select>
          </el-form-item>
          <el-form-item label="分析维度">
            <el-select v-model="filterForm.dimension" placeholder="选择分析维度" @change="handleFilterChange">
              <el-option label="专业类别分布" value="major_category" />
              <el-option label="地区分布" value="region" />
              <el-option label="企业性质分布" value="company_nature" />
              <el-option label="薪资分布" value="salary" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleFilterChange">分析</el-button>
          </el-form-item>
        </el-form>
      </el-card>
      
      <!-- 就业分布统计 -->
      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <span>{{ getChartTitle() }}</span>
            <el-radio-group v-model="chartType" @change="handleChartTypeChange">
              <el-radio-button label="pie">饼图</el-radio-button>
              <el-radio-button label="bar">柱状图</el-radio-button>
            </el-radio-group>
          </div>
        </template>
        <div v-loading="loading">
          <StatChart
            :chart-type="chartType"
            :title="getChartTitle()"
            :data="chartData"
            :x-axis-data="chartXAxis"
            height="400px"
          />
        </div>
      </el-card>
      
      <!-- 就业趋势分析 -->
      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <span>就业趋势分析（近5年）</span>
          </div>
        </template>
        <div v-loading="trendLoading">
          <StatChart
            chart-type="line"
            title="历年就业率变化"
            :data="trendData"
            :x-axis-data="trendXAxis"
            height="400px"
          />
        </div>
      </el-card>
      
      <!-- 就业满意度分析 -->
      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <span>学生就业满意度分析</span>
          </div>
        </template>
        <div v-loading="feedbackLoading">
          <el-row :gutter="20">
            <el-col :span="8" v-for="(item, index) in satisfactionData" :key="index">
              <el-card shadow="hover" class="satisfaction-card">
                <template #header>
                  <div class="satisfaction-header">
                    <h3>{{ item.title }}</h3>
                    <div class="satisfaction-value">{{ item.average.toFixed(1) }}</div>
                  </div>
                </template>
                <el-progress :percentage="(item.average / 5) * 100" :color="getSatisfactionColor(item.average)" />
              </el-card>
            </el-col>
          </el-row>
          <el-divider />
          <div>
            <h3>专业对口程度分布</h3>
            <StatChart
              chart-type="pie"
              :data="majorMatchData"
              height="300px"
            />
          </div>
        </div>
      </el-card>
    </div>
  </Layout>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { User, Document, ChatDotRound, PieChart } from '@element-plus/icons-vue'
import Layout from '@/components/Layout.vue'
import StatChart from '@/components/StatChart.vue'
import { getDistributionStatistics, getTrendStatistics } from '@/api/counselor'
import { getFeedbackRecords } from '@/api/counselor'

// 图表相关
const loading = ref(false)
const trendLoading = ref(false)
const feedbackLoading = ref(false)
const chartType = ref('pie')
const chartData = ref([])
const chartXAxis = ref([])
const trendData = ref([])
const trendXAxis = ref([])
const majorMatchData = ref([])
const satisfactionData = ref([
  { title: '企业满意度', key: 'company_rating', average: 0 },
  { title: '薪资满意度', key: 'salary_rating', average: 0 },
  { title: '工作内容满意度', key: 'job_rating', average: 0 }
])

// 年份选项
const currentYear = new Date().getFullYear()
const yearOptions = Array.from({ length: 10 }, (_, i) => currentYear - i)

// 筛选表单
const filterForm = reactive({
  year: currentYear,
  dimension: 'major_category'
})

// 获取图表标题
const getChartTitle = () => {
  const dimensionMap = {
    major_category: '专业类别',
    region: '地区',
    company_nature: '企业性质',
    salary: '薪资'
  }
  
  return `${filterForm.year}年就业${dimensionMap[filterForm.dimension] || ''}分布`
}

// 处理筛选条件变化
const handleFilterChange = () => {
  fetchDistributionData()
}

// 获取就业分布数据
const fetchDistributionData = async () => {
  loading.value = true
  try {
    const res = await getDistributionStatistics(filterForm.dimension, filterForm.year)
    if (res.code === 200) {
      if (chartType.value === 'pie') {
        chartData.value = res.data.map(item => ({
          label: item.label,
          value: item.value
        }))
      } else {
        chartXAxis.value = res.data.map(item => item.label)
        chartData.value = res.data.map(item => item.value)
      }
    }
  } catch (error) {
    ElMessage.error(error.message || '获取统计数据失败')
  } finally {
    loading.value = false
  }
}

// 获取就业趋势数据
const fetchTrendData = async () => {
  trendLoading.value = true
  try {
    // 获取近5年的数据
    const endYear = currentYear
    const startYear = endYear - 4
    
    const res = await getTrendStatistics(startYear, endYear)
    if (res.code === 200) {
      trendXAxis.value = res.data.map(item => `${item.year}年`)
      trendData.value = res.data.map(item => item.rate)
    }
  } catch (error) {
    ElMessage.error(error.message || '获取趋势数据失败')
  } finally {
    trendLoading.value = false
  }
}

// 获取反馈满意度数据
const fetchFeedbackData = async () => {
  feedbackLoading.value = true
  try {
    const res = await getFeedbackRecords()
    if (res.code === 200) {
      const feedbacks = res.data
      
      // 计算平均满意度
      satisfactionData.value.forEach(item => {
        const total = feedbacks.reduce((sum, feedback) => sum + feedback[item.key], 0)
        item.average = feedbacks.length ? total / feedbacks.length : 0
      })
      
      // 计算专业对口分布
      const majorMatchCount = {
        perfect: 0,
        good: 0,
        partial: 0,
        none: 0
      }
      
      feedbacks.forEach(feedback => {
        if (majorMatchCount.hasOwnProperty(feedback.major_match)) {
          majorMatchCount[feedback.major_match]++
        }
      })
      
      majorMatchData.value = [
        { label: '完全对口', value: majorMatchCount.perfect },
        { label: '基本对口', value: majorMatchCount.good },
        { label: '部分对口', value: majorMatchCount.partial },
        { label: '不对口', value: majorMatchCount.none }
      ]
    }
  } catch (error) {
    ElMessage.error(error.message || '获取反馈数据失败')
  } finally {
    feedbackLoading.value = false
  }
}

// 处理图表类型变化
const handleChartTypeChange = () => {
  fetchDistributionData()
}

// 获取满意度颜色
const getSatisfactionColor = (value) => {
  if (value >= 4) return '#67C23A'
  if (value >= 3) return '#409EFF'
  if (value >= 2) return '#E6A23C'
  return '#F56C6C'
}

onMounted(() => {
  fetchDistributionData()
  fetchTrendData()
  fetchFeedbackData()
})
</script>

<style scoped>
.statistics {
  max-width: 1200px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-card,
.chart-card {
  margin-bottom: 20px;
}

.satisfaction-card {
  margin-bottom: 20px;
}

.satisfaction-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.satisfaction-header h3 {
  margin: 0;
}

.satisfaction-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}
</style> 
