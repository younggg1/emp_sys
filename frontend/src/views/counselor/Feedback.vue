<template>
  <div class="feedback">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span><el-icon class="header-icon"><chat-square /></el-icon> 就业反馈列表</span>
          <div class="search-box">
            <el-input
              v-model="searchText"
              placeholder="搜索学生/班级"
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
        :showActions="false"
      >
        <template #student="{ row }">
          <div>
            <div>{{ row.student_name }}</div>
            <div class="text-secondary">{{ row.student_number }}</div>
          </div>
        </template>
        <template #created_at="{ row }">
          {{ formatDate(row.created_at) }}
        </template>
        <template #class_info="{ row }">
          <div>
            <div>{{ row.class_name }}</div>
            <div class="text-secondary">{{ row.major }}</div>
          </div>
        </template>
        
        <template #status="{ row }">
          <el-tag :type="row.status === 'approved' ? 'success' : 'warning'">
            {{ row.status === 'approved' ? '已审核' : '待审核' }}
          </el-tag>
        </template>
        <template #actions="{ row }">
          <el-button 
            v-if="row.status === 'pending'" 
            size="small" 
            type="success" 
            @click="handleApprove(row)"
          >
            <el-icon><check /></el-icon> 审核
          </el-button>
          <el-button 
            size="small" 
            type="danger" 
            :disabled="!hasDeletePermission"
            @click="handleDelete(row)"
          >
            <el-icon><delete /></el-icon> 删除
          </el-button>
        </template>
      </DataTable>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ChatSquare, Search, Delete, Check } from '@element-plus/icons-vue'
import DataTable from '@/components/DataTable.vue'
import { getFeedbackRecords, deleteFeedback, approveFeedback, getCounselorListWithUserInfo } from '@/api/counselor'
import { getUserId } from '@/utils/user'

// 表格列定义
const columns = [
  { prop: 'student', label: '学生', width: '150', slot: 'student' },
  { prop: 'class_info', label: '班级/专业', width: '180', slot: 'class_info' },
  { prop: 'created_at', label: '提交日期', width: '180', slot: 'created_at' },
  { prop: 'stage', label: '就业阶段', width: '120' },
  { prop: 'content', label: '反馈内容', minWidth: '300' },
  { prop: 'status', label: '状态', width: '100', slot: 'status' },
  { prop: 'actions', label: '操作', width: '220', slot: 'actions' }
]

const loading = ref(false)
const feedbackList = ref([])
const searchText = ref('')
const filterStatus = ref('')
const hasDeletePermission = ref(false)

// 日期格式化
const formatDate = (dateStr) => {
  if (!dateStr) return ''; 
  const date = new Date(dateStr);
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
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
      (item.student_name && item.student_name.toLowerCase().includes(search)) ||
      (item.student_number && item.student_number.toLowerCase().includes(search)) ||
      (item.major && item.major.toLowerCase().includes(search)) ||
      (item.class_name && item.class_name.toLowerCase().includes(search))
    )
  }
  
  return result
})

// 获取反馈列表
const fetchFeedbackRecords = async () => {
  loading.value = true
  try {
    // 获取当前辅导员ID
    const counselorId = getUserId()
    if (!counselorId) {
      ElMessage.error('用户未登录或登录信息已失效')
      return
    }
    
    // 从后端获取数据
    const res = await getFeedbackRecords({ counselorId })
    if (res.code === 200) {
      feedbackList.value = res.data
    } else {
      ElMessage.error(res.message || '获取反馈信息失败')
    }
  } catch (error) {
    console.error('获取反馈信息失败:', error)
    ElMessage.error(error.message || '获取反馈信息失败')
  } finally {
    loading.value = false
  }
}

// 获取辅导员权限
const fetchCounselorPermission = async () => {
  try {
    const counselorId = getUserId()
    if (!counselorId) {
      ElMessage.error('用户未登录或登录信息已失效')
      return
    }
    
    const res = await getCounselorListWithUserInfo()
    if (res.code === 200) {
      const currentCounselor = res.data.find(c => c.counselor_id === counselorId)
      hasDeletePermission.value = currentCounselor?.permission === 'Y'
    }
  } catch (error) {
    console.error('获取辅导员权限失败:', error)
  }
}

// 处理审核
const handleApprove = async (row) => {
  try {
    // 获取当前辅导员ID
    const counselorId = getUserId()
    if (!counselorId) {
      ElMessage.error('用户未登录或登录信息已失效')
      return
    }
    
    const res = await approveFeedback(row.feedback_id, {
      status: 'approved',
      counselorId
    })
    
    if (res.code === 200) {
      ElMessage.success('审核通过成功')
      // 更新本地数据
      const index = feedbackList.value.findIndex(item => item.feedback_id === row.feedback_id)
      if (index !== -1) {
        feedbackList.value[index].status = 'approved'
      }
    } else {
      ElMessage.error(res.message || '审核失败')
    }
  } catch (error) {
    console.error('审核反馈失败:', error)
    ElMessage.error(error.message || '审核失败')
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
      // 获取当前辅导员ID
      const counselorId = getUserId()
      if (!counselorId) {
        ElMessage.error('用户未登录或登录信息已失效')
        return
      }
      
      const res = await deleteFeedback(row.feedback_id, { counselorId })
      
      if (res.code === 200) {
        ElMessage.success('删除成功')
        // 从本地数据中移除
        const index = feedbackList.value.findIndex(item => item.feedback_id === row.feedback_id)
        if (index !== -1) {
          feedbackList.value.splice(index, 1)
        }
      } else {
        // 处理没有权限或记录不存在的情况
        ElMessage.error(res.message || '删除失败，可能没有权限或记录不存在')
      }
    } catch (error) {
      console.error('删除反馈失败:', error)
      ElMessage.error(error.message || '删除失败')
    }
  }).catch(() => {})
}

// 处理搜索
const handleSearch = () => {
  // 搜索逻辑在computed中处理
}

onMounted(() => {
  fetchFeedbackRecords()
  fetchCounselorPermission()
})
</script>

<style scoped>
.feedback {
  max-width: 100%;
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

.text-secondary {
  font-size: 12px;
  color: #909399;
}
</style> 
