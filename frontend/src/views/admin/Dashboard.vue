<template>
  <Layout>
    <template #menu>
      <el-menu-item index="/admin">
        <el-icon><data-analysis /></el-icon>
        <span>系统概览</span>
      </el-menu-item>
      <el-menu-item index="/admin/employment">
        <el-icon><document /></el-icon>
        <span>就业信息管理</span>
      </el-menu-item>
      <el-menu-item index="/admin/feedback">
        <el-icon><chat-dot-round /></el-icon>
        <span>就业反馈管理</span>
      </el-menu-item>
      <el-menu-item index="/admin/statistics">
        <el-icon><pie-chart /></el-icon>
        <span>统计分析</span>
      </el-menu-item>
      <el-menu-item index="/admin/user-manage">
        <el-icon><user /></el-icon>
        <span>用户管理</span>
      </el-menu-item>
      <el-menu-item index="/admin/permission">
        <el-icon><key /></el-icon>
        <span>权限管理</span>
      </el-menu-item>
      <el-menu-item index="/admin/settings">
        <el-icon><setting /></el-icon>
        <span>系统设置</span>
      </el-menu-item>
    </template>

    <template #header-title>
      管理员端 - 系统概览
    </template>
    
    <div class="dashboard">
      <!-- 整体统计 -->
      <el-row :gutter="20" class="stat-row">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-card-content">
              <div class="stat-card-icon student-icon">
                <el-icon><user /></el-icon>
              </div>
              <div class="stat-card-info">
                <div class="stat-card-title">学生总数</div>
                <div class="stat-card-value">{{ stats.studentCount }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-card-content">
              <div class="stat-card-icon employment-icon">
                <el-icon><document /></el-icon>
              </div>
              <div class="stat-card-info">
                <div class="stat-card-title">就业信息</div>
                <div class="stat-card-value">{{ stats.employmentCount }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-card-content">
              <div class="stat-card-icon feedback-icon">
                <el-icon><chat-dot-round /></el-icon>
              </div>
              <div class="stat-card-info">
                <div class="stat-card-title">反馈总数</div>
                <div class="stat-card-value">{{ stats.feedbackCount }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-card-content">
              <div class="stat-card-icon rate-icon">
                <el-icon><data-analysis /></el-icon>
              </div>
              <div class="stat-card-info">
                <div class="stat-card-title">就业率</div>
                <div class="stat-card-value">{{ stats.employmentRate }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <!-- 就业待审核数据 -->
      <el-card class="pending-card">
        <template #header>
          <div class="card-header">
            <span>待审核就业信息</span>
            <el-button type="primary" size="small" @click="goToEmployment">查看全部</el-button>
          </div>
        </template>
        <div v-loading="employmentLoading">
          <el-empty v-if="pendingEmployment.length === 0" description="暂无待审核数据" />
          <el-table v-else :data="pendingEmployment" style="width: 100%">
            <el-table-column prop="student_id" label="学号" width="120" />
            <el-table-column prop="name" label="姓名" width="100" />
            <el-table-column prop="company" label="就业企业" />
            <el-table-column prop="position" label="职位" />
            <el-table-column prop="submit_date" label="提交日期" width="120" />
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button type="primary" size="small" @click="goToEmployment">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-card>
      
      <!-- 反馈待审核数据 -->
      <el-card class="pending-card">
        <template #header>
          <div class="card-header">
            <span>待审核反馈信息</span>
            <el-button type="primary" size="small" @click="goToFeedback">查看全部</el-button>
          </div>
        </template>
        <div v-loading="feedbackLoading">
          <el-empty v-if="pendingFeedback.length === 0" description="暂无待审核数据" />
          <el-table v-else :data="pendingFeedback" style="width: 100%">
            <el-table-column prop="student_id" label="学号" width="120" />
            <el-table-column prop="student_name" label="姓名" width="100" />
            <el-table-column prop="company" label="就业企业" />
            <el-table-column prop="submit_date" label="提交日期" width="120" />
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button type="primary" size="small" @click="goToFeedback">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-card>
    </div>
  </Layout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { DataAnalysis, Document, ChatDotRound, PieChart, User, Key, Setting } from '@element-plus/icons-vue'
import Layout from '@/components/Layout.vue'
import { getEmploymentRecords, getFeedbackRecords, getStudents } from '@/api/admin'

const router = useRouter()

// 状态
const employmentLoading = ref(false)
const feedbackLoading = ref(false)
const pendingEmployment = ref([])
const pendingFeedback = ref([])
const stats = reactive({
  studentCount: 0,
  employmentCount: 0,
  feedbackCount: 0,
  employmentRate: 0
})

// 获取待审核就业信息
const fetchPendingEmployment = async () => {
  employmentLoading.value = true
  try {
    const currentYear = new Date().getFullYear()
    const res = await getEmploymentRecords(currentYear)
    if (res.code === 200) {
      // 筛选出待审核的信息，并只显示最近10条
      pendingEmployment.value = res.data
        .filter(item => item.status === 'pending')
        .slice(0, 5)
      
      // 更新统计信息
      stats.employmentCount = res.data.length
    }
  } catch (error) {
    ElMessage.error(error.message || '获取就业信息失败')
  } finally {
    employmentLoading.value = false
  }
}

// 获取待审核反馈信息
const fetchPendingFeedback = async () => {
  feedbackLoading.value = true
  try {
    const currentYear = new Date().getFullYear()
    const res = await getFeedbackRecords(currentYear)
    if (res.code === 200) {
      // 筛选出待审核的信息，并只显示最近10条
      pendingFeedback.value = res.data
        .filter(item => item.status === 'pending')
        .slice(0, 5)
      
      // 更新统计信息
      stats.feedbackCount = res.data.length
    }
  } catch (error) {
    ElMessage.error(error.message || '获取反馈信息失败')
  } finally {
    feedbackLoading.value = false
  }
}

// 获取学生统计
const fetchStudentStats = async () => {
  try {
    const res = await getStudents()
    if (res.code === 200) {
      stats.studentCount = res.data.length
      
      // 计算就业率
      const employedCount = res.data.filter(student => student.employment_status === 'employed').length
      stats.employmentRate = Math.round((employedCount / res.data.length) * 100) || 0
    }
  } catch (error) {
    ElMessage.error(error.message || '获取学生统计失败')
  }
}

// 跳转到就业信息管理页面
const goToEmployment = () => {
  router.push('/admin/employment')
}

// 跳转到反馈管理页面
const goToFeedback = () => {
  router.push('/admin/feedback')
}

onMounted(() => {
  fetchPendingEmployment()
  fetchPendingFeedback()
  fetchStudentStats()
})
</script>

<style scoped>
.dashboard {
  max-width: 1200px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  height: 120px;
}

.stat-card-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stat-card-icon {
  width: 64px;
  height: 64px;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 15px;
  color: white;
  font-size: 30px;
}

.student-icon {
  background-color: #409EFF;
}

.employment-icon {
  background-color: #67C23A;
}

.feedback-icon {
  background-color: #E6A23C;
}

.rate-icon {
  background-color: #F56C6C;
}

.stat-card-title {
  font-size: 16px;
  color: #909399;
  margin-bottom: 10px;
}

.stat-card-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.pending-card {
  margin-bottom: 20px;
}
</style> 
