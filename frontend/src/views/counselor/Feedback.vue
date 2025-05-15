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
      辅导员端 - 就业反馈管理
    </template>
    
    <div class="feedback">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>就业反馈列表</span>
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
              <div>企业：{{ row.company_rating }}?</div>
              <div>薪资：{{ row.salary_rating }}?</div>
              <div>工作：{{ row.job_rating }}?</div>
            </div>
          </template>
          <template #major_match="{ row }">
            {{ getMajorMatchText(row.major_match) }}
          </template>
          <template #actions="{ row }">
            <el-button 
              size="small" 
              type="success" 
              v-if="row.status !== 'approved'"
              @click="handleAudit(row)"
            >
              审核
            </el-button>
            <el-button 
              size="small" 
              type="primary" 
              @click="viewDetail(row)"
            >
              查看
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
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
          <el-button 
            type="success" 
            v-if="currentFeedback.status !== 'approved'"
            @click="handleAuditInDetail"
          >
            审核通过
          </el-button>
        </template>
      </el-dialog>
    </div>
  </Layout>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Document, ChatDotRound, PieChart, Search } from '@element-plus/icons-vue'
import Layout from '@/components/Layout.vue'
import DataTable from '@/components/DataTable.vue'
import { getFeedbackRecords, auditFeedback, deleteFeedback } from '@/api/counselor'

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
    const res = await getFeedbackRecords()
    if (res.code === 200) {
      feedbackList.value = res.data
    }
  } catch (error) {
    ElMessage.error(error.message || '获取反馈信息失败')
  } finally {
    loading.value = false
  }
}

// 处理审核
const handleAudit = (row) => {
  ElMessageBox.confirm('确定审核通过该反馈吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await auditFeedback(row.feedback_id)
      if (res.code === 200) {
        ElMessage.success('审核成功')
        fetchFeedbackRecords() // 刷新列表
      }
    } catch (error) {
      ElMessage.error(error.message || '审核失败')
    }
  }).catch(() => {})
}

// 详情页中的审核
const handleAuditInDetail = async () => {
  try {
    const res = await auditFeedback(currentFeedback.value.feedback_id)
    if (res.code === 200) {
      ElMessage.success('审核成功')
      dialogVisible.value = false
      fetchFeedbackRecords() // 刷新列表
    }
  } catch (error) {
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
      const res = await deleteFeedback(row.feedback_id)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchFeedbackRecords() // 刷新列表
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
}

.search-box {
  display: flex;
  align-items: center;
}

.ratings {
  line-height: 1.5;
}

.feedback-content {
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.5;
  margin: 0;
  font-family: inherit;
}
</style> 
