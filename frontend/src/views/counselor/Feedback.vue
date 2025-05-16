<template>
  <div class="feedback">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span><el-icon class="header-icon"><chat-square /></el-icon> 就业反馈列表</span>
          <div class="search-box">
            <el-input
              v-model="searchText"
              placeholder="搜索学生/公司"
              style="width: 250px; margin-right: 10px;"
              clearable
              @input="handleSearch"
            >
              <template #prefix>
                <el-icon><search /></el-icon>
              </template>
            </el-input>
            <el-select v-model="filterStatus" placeholder="状态筛选" clearable @change="handleSearch">
              <el-option label="全部" value="" />
              <el-option label="已审核" value="approved" />
              <el-option label="待审核" value="pending" />
            </el-select>
          </div>
        </div>
      </template>
      
      <DataTable
        :data="filteredFeedback"
        :columns="columns"
        :loading="loading"
        @delete="handleDelete"
      >
        <template #status="{ row }">
          <el-tag :type="row.status === 'approved' ? 'success' : 'warning'">
            {{ row.status === 'approved' ? '已审核' : '待审核' }}
          </el-tag>
        </template>
        <template #ratings="{ row }">
          <div class="ratings">
            <div>企业：{{ row.company_rating }}星</div>
            <div>薪资：{{ row.salary_rating }}星</div>
            <div>工作：{{ row.job_rating }}星</div>
          </div>
        </template>
        <template #major_match="{ row }">
          {{ getMajorMatchText(row.major_match) }}
        </template>
        <template #actions="{ row }">
          <el-button 
            size="small" 
            type="primary" 
            @click="viewDetail(row)"
          >
            <el-icon><view /></el-icon> 查看
          </el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">
            <el-icon><delete /></el-icon> 删除
          </el-button>
        </template>
      </DataTable>
    </el-card>
    
    <!-- 查看反馈详情对话框 -->
    <el-dialog
      title="反馈详情"
      v-model="dialogVisible"
      width="50%"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="学生学号">{{ currentFeedback.student_id }}</el-descriptions-item>
        <el-descriptions-item label="学生姓名">{{ currentFeedback.student_name }}</el-descriptions-item>
        <el-descriptions-item label="提交日期">{{ currentFeedback.submit_date }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentFeedback.status === 'approved' ? 'success' : 'warning'">
            {{ currentFeedback.status === 'approved' ? '已审核' : '待审核' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="就业企业" :span="2">{{ currentFeedback.company }}</el-descriptions-item>
        <el-descriptions-item label="企业评价">
          <el-rate v-model="currentFeedback.company_rating" disabled show-score />
        </el-descriptions-item>
        <el-descriptions-item label="薪资满意度">
          <el-rate v-model="currentFeedback.salary_rating" disabled show-score />
        </el-descriptions-item>
        <el-descriptions-item label="工作内容满意度">
          <el-rate v-model="currentFeedback.job_rating" disabled show-score />
        </el-descriptions-item>
        <el-descriptions-item label="专业对口程度">
          {{ getMajorMatchText(currentFeedback.major_match) }}
        </el-descriptions-item>
        <el-descriptions-item label="反馈内容" :span="2">
          <pre class="feedback-content">{{ currentFeedback.content }}</pre>
        </el-descriptions-item>
      </el-descriptions>
      
      <template #footer>
        <el-button @click="dialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ChatSquare, Search, View, Delete } from '@element-plus/icons-vue'
import DataTable from '@/components/DataTable.vue'
import { getFeedbackRecords, deleteFeedback, mockGetFeedbackRecords } from '@/api/counselor'

// 表格列定义
const columns = [
  { prop: 'student_id', label: '学号', width: '120' },
  { prop: 'student_name', label: '姓名', width: '100' },
  { prop: 'submit_date', label: '提交日期', width: '120' },
  { prop: 'company', label: '就业企业' },
  { prop: 'ratings', label: '评分', slot: 'ratings' },
  { prop: 'major_match', label: '专业对口', slot: 'major_match' },
  { prop: 'status', label: '状态', slot: 'status' }
]

const loading = ref(false)
const feedbackList = ref([])
const dialogVisible = ref(false)
const searchText = ref('')
const filterStatus = ref('')
const currentFeedback = ref({})

// 专业对口程度映射
const getMajorMatchText = (value) => {
  const map = { 
    perfect: '完全对口', 
    good: '基本对口', 
    partial: '部分对口', 
    none: '不对口' 
  }
  return map[value] || value
}

// 过滤后的反馈列表
const filteredFeedback = computed(() => {
  let result = feedbackList.value
  
  // 筛选状态
  if (filterStatus.value) {
    result = result.filter(item => item.status === filterStatus.value)
  }
  
  // 搜索文本
  if (searchText.value) {
    const search = searchText.value.toLowerCase()
    result = result.filter(item => 
      item.student_name.toLowerCase().includes(search) ||
      item.company.toLowerCase().includes(search)
    )
  }
  
  return result
})

// 获取反馈列表
const fetchFeedbackRecords = async () => {
  loading.value = true
  try {
    // 使用模拟数据
    const res = mockGetFeedbackRecords()
    if (res.code === 200) {
      feedbackList.value = res.data
    }
  } catch (error) {
    ElMessage.error(error.message || '获取反馈信息失败')
  } finally {
    loading.value = false
  }
}

// 处理删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除该反馈吗？此操作不可恢复', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 模拟成功响应
      ElMessage.success('删除成功')
      // 从本地数据中移除
      const index = feedbackList.value.findIndex(item => item.feedback_id === row.feedback_id)
      if (index !== -1) {
        feedbackList.value.splice(index, 1)
      }
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  }).catch(() => {})
}

// 查看详情
const viewDetail = (row) => {
  currentFeedback.value = { ...row }
  dialogVisible.value = true
}

// 处理搜索
const handleSearch = () => {
  // 搜索逻辑在computed中处理
}

onMounted(() => {
  fetchFeedbackRecords()
})
</script>

<style scoped>
.feedback {
  max-width: 1200px;
  margin: 0 auto;
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

.search-box {
  display: flex;
  align-items: center;
}

.ratings {
  line-height: 1.6;
}

.feedback-content {
  margin: 0;
  white-space: pre-wrap;
  font-family: inherit;
  color: #606266;
  line-height: 1.6;
}

:deep(.el-descriptions__label) {
  font-weight: bold;
}
</style> 
