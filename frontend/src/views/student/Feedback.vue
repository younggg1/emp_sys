<template>
  <div class="feedback">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span><el-icon class="header-icon"><chat-square /></el-icon> 就业反馈</span>
          <el-button type="primary" @click="checkEmploymentStatusAndShowDialog" :disabled="isLoading">
            <el-icon><plus /></el-icon> 提交就业反馈
          </el-button>
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
        <el-form-item label="就业阶段" prop="stage">
          <el-select v-model="form.stage" placeholder="请选择就业阶段" style="width: 100%">
            <el-option 
              v-for="stage in allStages" 
              :key="stage" 
              :label="stage" 
              :value="stage" 
              :disabled="dialogType === 'add' && usedStages.includes(stage)"
            />
          </el-select>
          <div v-if="dialogType === 'add'" class="stage-tip">
            <small v-if="usedStages.length > 0">
              注意：已提交过 {{ usedStages.join('、') }} 阶段的反馈，如需修改请使用编辑功能
            </small>
          </div>
        </el-form-item>
        
        <el-form-item label="反馈内容" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="6"
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
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ChatSquare, Plus } from '@element-plus/icons-vue'
import DataTable from '@/components/DataTable.vue'
import { getFeedbackRecords, addFeedbackRecord, updateFeedbackRecord, deleteFeedbackRecord, getStudentInfo, getEmploymentRecords } from '@/api/student'
import { getUserId } from '@/utils/user'

// 表格列定义
const columns = [
  { prop: 'created_at', label: '提交日期', width: '150', formatter: (row) => {
    return new Date(row.created_at).toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  }},
  { prop: 'stage', label: '就业阶段', width: '120', formatter: (row) => {
    const stageMap = {
      '试用期': '试用期',
      '正式工作': '正式工作',
      '已离职': '已离职'
    }
    return stageMap[row.stage] || row.stage
  }},
  { prop: 'content', label: '反馈内容', minWidth: '300' },
  { prop: 'status', label: '状态', width: '100', slot: 'status' }
]

const loading = ref(false)
const submitting = ref(false)
const feedbackList = ref([])
const dialogVisible = ref(false)
const dialogType = ref('add') // 'add' 或 'edit'
const formRef = ref(null)
const currentUser = ref(null)
const isLoading = ref(false)

// 表单数据
const form = reactive({
  student_id: null,
  stage: '',
  content: '',
  status: 'pending'
})

// 已使用的阶段
const usedStages = ref([])

// 所有可选的就业阶段
const allStages = ['试用期', '正式工作', '已离职']

// 表单验证规则
const rules = {
  stage: [{ required: true, message: '请选择就业阶段', trigger: 'change' }],
  content: [
    { required: true, message: '请输入反馈内容', trigger: 'blur' }

  ]
}

// 检查就业状态并显示对话框
const checkEmploymentStatusAndShowDialog = async () => {
  isLoading.value = true
  try {
    // 获取当前用户ID
    const userId = getUserId()
    console.log('当前用户ID:', userId)
    
    if (!userId) {
      ElMessage.error('用户未登录或登录信息已失效')
      return
    }
    
    // 检查是否有任何阶段还没有反馈记录
    usedStages.value = feedbackList.value.map(item => item.stage)
    const allStages = ['试用期', '正式工作', '已离职']
    const unusedStages = allStages.filter(stage => !usedStages.value.includes(stage))
    
    if (unusedStages.length === 0) {
      // 所有阶段都已有反馈
      ElMessage.warning('所有就业阶段都已有反馈记录，请使用编辑功能修改已有反馈')
      return
    }
    
    // 显示对话框，并设置可选阶段
    showDialog('add', null, unusedStages)
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
    console.error('操作失败:', error)
  } finally {
    isLoading.value = false
  }
}

// 获取反馈列表
const fetchFeedbackRecords = async () => {
  loading.value = true
  try {
    // 获取当前用户ID
    const userId = getUserId()
    console.log('获取反馈列表使用的用户ID:', userId)
    
    if (!userId) {
      ElMessage.error('用户未登录或登录信息已失效')
      return
    }
    
    // 直接使用用户ID请求反馈列表
    console.log('使用的ID:', userId)
    const res = await getFeedbackRecords(userId)
    console.log('获取反馈列表结果:', res)
    
    if (res.code === 200) {
      feedbackList.value = res.data
      // 更新已使用的阶段
      usedStages.value = feedbackList.value.map(item => item.stage)
    } else {
      console.warn('获取反馈列表接口返回错误:', res.message)
    }
  } catch (error) {
    console.error('获取反馈列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 显示添加/编辑对话框
const showDialog = (type, record, availableStages = null) => {
  dialogType.value = type
  dialogVisible.value = true
  
  if (type === 'add') {
    // 重置表单
    if (formRef.value) {
      formRef.value.resetFields()
    }
    
    // 设置学生ID
    form.student_id = currentUser.value?.student_id || getUserId()
    form.status = 'pending'
    
    // 如果提供了可用阶段，自动选择第一个
    if (availableStages && availableStages.length > 0) {
      form.stage = availableStages[0]
    }
  } else if (type === 'edit' && record) {
    // 填充表单
    form.stage = record.stage
    form.content = record.content
    form.student_id = record.student_id
    form.status = 'pending' // 编辑后状态重置为待审核
  }
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    
    try {
      // 设置创建/更新时间
      const now = new Date()
      
      // 获取用户ID
      const userId = getUserId()
      if (!userId) {
        ElMessage.error('用户未登录或登录信息已失效')
        return
      }
      
      // 使用当前学生ID或用户ID
      const studentId = currentUser.value?.student_id || userId
      console.log('提交表单使用的学生ID:', studentId)
      
      if (dialogType.value === 'add') {
        // 添加前检查该阶段是否已有反馈
        const existingFeedback = feedbackList.value.find(item => item.stage === form.stage)
        if (existingFeedback) {
          ElMessage.warning(`该就业阶段(${form.stage})已有反馈记录，请使用编辑功能修改`)
          submitting.value = false
          return
        }
        
        // 添加
        const feedbackData = {
          ...form,
          student_id: studentId,
          created_at: now,
          updated_at: now
        }
        
        console.log('准备提交的反馈数据:', feedbackData)
        const res = await addFeedbackRecord(feedbackData)
        console.log('提交反馈结果:', res)
        
        if (res.code === 200) {
          ElMessage.success('提交成功')
          dialogVisible.value = false
          fetchFeedbackRecords() // 刷新列表
        } else {
          ElMessage.error(res.message || '提交失败')
        }
      } else {
        // 编辑
        const feedbackData = {
          ...form,
          feedback_id: currentEditId.value,
          student_id: studentId,
          updated_at: now
        }
        
        console.log('准备更新的反馈数据:', feedbackData)
        const res = await updateFeedbackRecord(feedbackData)
        console.log('更新反馈结果:', res)
        
        if (res.code === 200) {
          ElMessage.success('更新成功')
          dialogVisible.value = false
          fetchFeedbackRecords() // 刷新列表
        } else {
          ElMessage.error(res.message || '更新失败')
        }
      }
    } catch (error) {
      ElMessage.error(error.message || '操作失败')
      console.error('表单提交失败:', error)
    } finally {
      submitting.value = false
    }
  })
}

// 当前编辑的反馈ID
const currentEditId = ref(null)

// 处理编辑
const handleEdit = (row) => {
  if (row.status === 'approved') {
    ElMessage.warning('已审核的反馈不能修改')
    return
  }
  
  currentEditId.value = row.feedback_id
  showDialog('edit', row)
}

// 处理删除
const handleDelete = (row) => {
  // if (row.status === 'approved') {
  //   ElMessage.warning('已审核的反馈不能删除')
  //   return
  // }
  
  ElMessageBox.confirm('确定删除该反馈吗？此操作不可恢复', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 获取用户ID
      const userId = getUserId()
      if (!userId) {
        ElMessage.error('用户未登录或登录信息已失效')
        return
      }
      
      // 使用当前学生ID或用户ID
      const studentId = currentUser.value?.student_id || userId
      console.log('删除反馈使用的学生ID:', studentId, '反馈ID:', row.feedback_id)
      
      const res = await deleteFeedbackRecord(row.feedback_id, studentId)
      console.log('删除反馈结果:', res)
      
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchFeedbackRecords() // 刷新列表
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
      console.error('删除反馈失败:', error)
    }
  }).catch(() => {})
}

onMounted(async () => {
  // 获取反馈列表
  fetchFeedbackRecords()
  
  // 尝试获取学生信息，但不阻止页面功能
  try {
    const userId = getUserId()
    if (userId) {
      const res = await getStudentInfo(userId)
      if (res.code === 200) {
        currentUser.value = res.data
        console.log('获取到学生信息:', currentUser.value)
      }
    }
  } catch (error) {
    console.warn('获取学生信息失败，但不影响页面功能:', error)
  }
})
</script>

<style scoped>
.feedback {
  max-width: 90%;
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
</style> 
