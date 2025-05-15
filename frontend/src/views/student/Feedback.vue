<template>
  <Layout>
    <template #menu>
      <el-menu-item index="/student">
        <el-icon><house /></el-icon>
        <span>个人信息</span>
      </el-menu-item>
      <el-menu-item index="/student/employment">
        <el-icon><document /></el-icon>
        <span>就业信息管理</span>
      </el-menu-item>
      <el-menu-item index="/student/feedback">
        <el-icon><chat-dot-round /></el-icon>
        <span>就业反馈管理</span>
      </el-menu-item>
    </template>

    <template #header-title>
      学生端 - 就业反馈管理
    </template>
    
    <div class="feedback">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>就业反馈</span>
            <el-button type="primary" @click="showDialog('add')">提交就业反馈</el-button>
          </div>
        </template>
        
        <DataTable
          :data="feedbackList"
          :columns="columns"
          :loading="loading"
          @edit="handleEdit"
          @delete="handleDelete"
        >
          <template #status="{ row }">
            <el-tag :type="row.status === 'approved' ? 'success' : 'warning'">
              {{ row.status === 'approved' ? '已审核' : '待审核' }}
            </el-tag>
          </template>
        </DataTable>
      </el-card>
      
      <!-- 添加/编辑反馈表单 -->
      <el-dialog
        :title="dialogType === 'add' ? '提交就业反馈' : '编辑就业反馈'"
        v-model="dialogVisible"
        width="50%"
      >
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="120px"
          label-position="right"
        >
          <el-form-item label="就业企业" prop="company">
            <el-input v-model="form.company" placeholder="请输入就业企业名称" />
          </el-form-item>
          
          <el-form-item label="企业评价" prop="company_rating">
            <el-rate v-model="form.company_rating" :max="5" show-text :texts="['很差', '较差', '一般', '较好', '很好']" />
          </el-form-item>
          
          <el-form-item label="薪资满意度" prop="salary_rating">
            <el-rate v-model="form.salary_rating" :max="5" show-text :texts="['很不满意', '不满意', '一般', '满意', '很满意']" />
          </el-form-item>
          
          <el-form-item label="工作内容满意度" prop="job_rating">
            <el-rate v-model="form.job_rating" :max="5" show-text :texts="['很不满意', '不满意', '一般', '满意', '很满意']" />
          </el-form-item>
          
          <el-form-item label="专业对口程度" prop="major_match">
            <el-select v-model="form.major_match" placeholder="请选择专业对口程度" style="width: 100%">
              <el-option label="完全对口" value="perfect" />
              <el-option label="基本对口" value="good" />
              <el-option label="部分对口" value="partial" />
              <el-option label="不对口" value="none" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="反馈内容" prop="content">
            <el-input
              v-model="form.content"
              type="textarea"
              :rows="5"
              placeholder="请输入详细反馈内容，如工作体验、职业发展、对学校培养的建议等"
            />
          </el-form-item>
        </el-form>
        
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitting">确定</el-button>
        </template>
      </el-dialog>
    </div>
  </Layout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { House, Document, ChatDotRound } from '@element-plus/icons-vue'
import Layout from '@/components/Layout.vue'
import DataTable from '@/components/DataTable.vue'
import { getFeedbackRecords, submitFeedback, updateFeedback, deleteFeedback } from '@/api/student'

// 表格列定义
const columns = [
  { prop: 'submit_date', label: '提交日期', width: '120' },
  { prop: 'company', label: '就业企业' },
  { prop: 'company_rating', label: '企业评价', formatter: (row) => `${row.company_rating}星` },
  { prop: 'salary_rating', label: '薪资满意度', formatter: (row) => `${row.salary_rating}星` },
  { prop: 'job_rating', label: '工作内容满意度', formatter: (row) => `${row.job_rating}星` },
  { prop: 'major_match', label: '专业对口程度', formatter: (row) => {
    const map = { perfect: '完全对口', good: '基本对口', partial: '部分对口', none: '不对口' }
    return map[row.major_match] || row.major_match
  }},
  { prop: 'content', label: '反馈内容', width: '300' },
  { prop: 'status', label: '状态', slot: 'status' }
]

const loading = ref(false)
const submitting = ref(false)
const feedbackList = ref([])
const dialogVisible = ref(false)
const dialogType = ref('add') // 'add' 或 'edit'
const formRef = ref(null)
const currentFeedbackId = ref(null)

// 表单数据
const form = reactive({
  company: '',
  company_rating: 3,
  salary_rating: 3,
  job_rating: 3,
  major_match: '',
  content: ''
})

// 表单验证规则
const rules = {
  company: [{ required: true, message: '请输入就业企业名称', trigger: 'blur' }],
  company_rating: [{ required: true, message: '请评价企业', trigger: 'change' }],
  salary_rating: [{ required: true, message: '请评价薪资满意度', trigger: 'change' }],
  job_rating: [{ required: true, message: '请评价工作内容满意度', trigger: 'change' }],
  major_match: [{ required: true, message: '请选择专业对口程度', trigger: 'change' }],
  content: [{ required: true, message: '请输入反馈内容', trigger: 'blur' }]
}

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

// 显示添加/编辑对话框
const showDialog = (type, record) => {
  dialogType.value = type
  dialogVisible.value = true
  
  if (type === 'add') {
    // 重置表单
    if (formRef.value) {
      formRef.value.resetFields()
    }
    form.company_rating = 3
    form.salary_rating = 3
    form.job_rating = 3
    currentFeedbackId.value = null
  } else if (type === 'edit' && record) {
    // 填充表单
    Object.keys(form).forEach(key => {
      form[key] = record[key]
    })
    currentFeedbackId.value = record.feedback_id
  }
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    
    try {
      if (dialogType.value === 'add') {
        // 添加
        const res = await submitFeedback(form)
        if (res.code === 200) {
          ElMessage.success('提交成功')
          dialogVisible.value = false
          fetchFeedbackRecords() // 刷新列表
        }
      } else {
        // 编辑
        const res = await updateFeedback(currentFeedbackId.value, form)
        if (res.code === 200) {
          ElMessage.success('更新成功')
          dialogVisible.value = false
          fetchFeedbackRecords() // 刷新列表
        }
      }
    } catch (error) {
      ElMessage.error(error.message || '操作失败')
    } finally {
      submitting.value = false
    }
  })
}

// 处理编辑
const handleEdit = (row) => {
  if (row.status === 'approved') {
    ElMessage.warning('已审核的反馈不能修改')
    return
  }
  showDialog('edit', row)
}

// 处理删除
const handleDelete = (row) => {
  if (row.status === 'approved') {
    ElMessage.warning('已审核的反馈不能删除')
    return
  }
  
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
</style> 
