<template>
  <div class="dashboard">
    <el-card class="student-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span><el-icon class="header-icon"><user-filled /></el-icon> 班级学生信息</span>
          <el-input
            v-model="searchText"
            placeholder="搜索学生姓名/学号/专业"
            style="width: 300px"
            clearable
            @input="handleSearch"
          >
            <template #prefix>
              <el-icon><search /></el-icon>
            </template>
          </el-input>
        </div>
      </template>
      
      <DataTable
        :data="filteredStudents"
        :columns="columns"
        :loading="loading"
        :show-actions="false"
      >
        <template #employment_status="{ row }">
          <el-tag :type="row.employment_status === 'employed' ? 'success' : 'info'">
            {{ row.employment_status === 'employed' ? '已就业' : '未就业' }}
          </el-tag>
        </template>
      </DataTable>
    </el-card>
    
    <el-card class="stats-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span><el-icon class="header-icon"><data-analysis /></el-icon> 就业统计</span>
        </div>
      </template>
      <div class="stats-container">
        <div class="stat-item">
          <div class="stat-title">学生总数</div>
          <div class="stat-value">{{ stats.total }}</div>
        </div>
        <div class="stat-item">
          <div class="stat-title">已就业</div>
          <div class="stat-value">{{ stats.employed }}</div>
        </div>
        <div class="stat-item">
          <div class="stat-title">待就业</div>
          <div class="stat-value">{{ stats.unemployed }}</div>
        </div>
        <div class="stat-item">
          <div class="stat-title">就业率</div>
          <div class="stat-value">{{ stats.percentage }}%</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { UserFilled, Search, DataAnalysis } from '@element-plus/icons-vue'
import DataTable from '@/components/DataTable.vue'
import { getStudents, mockGetStudents } from '@/api/counselor'

// 表格列定义
const columns = [
  { prop: 'student_id', label: '学号', width: '120' },
  { prop: 'name', label: '姓名', width: '100' },
  { prop: 'gender', label: '性别', width: '80' },
  { prop: 'class_name', label: '班级' },
  { prop: 'college', label: '学院' },
  { prop: 'major', label: '专业' },
  { prop: 'employment_status', label: '就业状态', slot: 'employment_status' },
  { prop: 'phone', label: '联系电话' },
  { prop: 'email', label: '邮箱' }
]

const loading = ref(false)
const students = ref([])
const searchText = ref('')

// 过滤后的学生列表
const filteredStudents = computed(() => {
  if (!searchText.value) return students.value
  
  const search = searchText.value.toLowerCase()
  return students.value.filter(student => 
    student.name.toLowerCase().includes(search) ||
    student.student_id.toString().includes(search) ||
    student.major.toLowerCase().includes(search)
  )
})

// 统计数据
const stats = computed(() => {
  const total = students.value.length
  const employed = students.value.filter(s => s.employment_status === 'employed').length
  const unemployed = total - employed
  const percentage = total ? Math.round((employed / total) * 100) : 0
  
  return {
    total,
    employed,
    unemployed,
    percentage
  }
})

// 获取学生列表
const fetchStudents = async () => {
  loading.value = true
  try {
    // 使用模拟数据
    const res = mockGetStudents()
    if (res.code === 200) {
      students.value = res.data
    }
  } catch (error) {
    ElMessage.error(error.message || '获取学生信息失败')
  } finally {
    loading.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  // 搜索逻辑在computed中处理
}

onMounted(() => {
  fetchStudents()
})
</script>

<style scoped>
.dashboard {
  max-width: 1200px;
  margin: 0 auto;
}

.student-card {
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

.header-icon {
  margin-right: 6px;
  color: #409EFF;
}

:deep(.el-card__header) {
  padding: 16px 20px;
  border-bottom: 1px solid #ebeef5;
  background-color: #f8f9fc;
}

:deep(.el-table th) {
  background-color: #f5f7fa !important;
  color: #606266;
  font-weight: bold;
}

:deep(.el-table--border) {
  border-radius: 4px;
  overflow: hidden;
}

.stats-card {
  margin-bottom: 20px;
  border-radius: 4px;
}

.stats-container {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
}

.stat-item {
  text-align: center;
  padding: 20px;
  min-width: 150px;
}

.stat-title {
  color: #606266;
  font-size: 16px;
  margin-bottom: 10px;
}

.stat-value {
  color: #409EFF;
  font-size: 28px;
  font-weight: bold;
}
</style> 
