<template>
  <div class="employment">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span><el-icon class="header-icon"><briefcase /></el-icon> 就业信息</span>
          <el-button 
            type="primary" 
            @click="showDialog('add')" 
            :disabled="employmentList.length > 0"
            :title="employmentList.length > 0 ? '您已经登记过就业信息，如需修改请使用编辑功能' : '登记就业信息'"
          >
            <el-icon><plus /></el-icon> 登记就业信息
          </el-button>
        </div>
      </template>
      
      <DataTable
        :data="employmentList"
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
    
    <!-- 添加/编辑就业信息表单 -->
    <el-dialog
      :title="dialogType === 'add' ? '登记就业信息' : '编辑就业信息'"
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
        <el-form-item label="企业性质" prop="company_nature">
          <el-select v-model="form.company_nature" placeholder="请选择企业性质" style="width: 100%">
            <el-option label="国企" value="国企" />
            <el-option label="私企" value="私企" />
            <el-option label="外企" value="外企" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="公司名称" prop="company">
          <el-input v-model="form.company" placeholder="请输入公司名称" />
        </el-form-item>
        
        <el-form-item label="职位" prop="position">
          <el-input v-model="form.position" placeholder="请输入职位" />
        </el-form-item>
        
        <el-form-item label="月薪" prop="salary">
          <el-input-number v-model="form.salary" :min="0" :precision="2" :step="1000" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="入职日期" prop="entry_date">
          <el-date-picker
            v-model="form.entry_date"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="地区" prop="region">
          <el-input v-model="form.region" placeholder="请输入地区（城市）" />
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
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Briefcase, Plus } from '@element-plus/icons-vue'
import DataTable from '@/components/DataTable.vue'
import { 
  getEmploymentRecords, 
  addEmploymentRecord, 
  updateEmploymentRecord, 
  deleteEmploymentRecord 
} from '@/api/student'
import { getUserId } from '@/utils/user'

// 格式化日期，将Date对象或ISO字符串转为YYYY-MM-DD字符串
const formatDate = (date) => {
  if (!date) return '';
  
  if (typeof date === 'string') {
    // 处理ISO日期字符串
    return date.split('T')[0];
  }
  
  // 处理Date对象
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

// 表格列定义
const columns = [
  { prop: 'company_nature', label: '企业性质' },
  { prop: 'company', label: '公司名称' },
  { prop: 'position', label: '职位' },
  { prop: 'salary', label: '月薪' },
  { prop: 'entry_date', label: '入职日期', formatter: (row) => {
    if (!row.entry_date) return '';
    return formatDate(row.entry_date);
  }},
  { prop: 'region', label: '地区' },
  { prop: 'status', label: '状态', slot: 'status' }
]

const loading = ref(false)
const submitting = ref(false)
const employmentList = ref([])
const dialogVisible = ref(false)
const dialogType = ref('add') // 'add' 或 'edit'
const formRef = ref(null)
const currentRecordId = ref(null)

// 表单数据
const form = reactive({
  company_nature: '',
  company: '',
  position: '',
  salary: 0,
  entry_date: '',
  region: '',
  student_id: null
})

// 表单验证规则
const rules = {
  company_nature: [{ required: true, message: '请输入企业性质', trigger: 'blur' }],
  company: [{ required: true, message: '请输入公司名称', trigger: 'blur' }],
  position: [{ required: true, message: '请输入职位', trigger: 'blur' }],
  salary: [{ required: true, message: '请输入月薪', trigger: 'blur' }],
  entry_date: [{ required: true, message: '请选择入职日期', trigger: 'change' }],
  region: [{ required: true, message: '请输入地区', trigger: 'blur' }]
}

// 获取就业信息列表
const fetchEmploymentRecords = async () => {
  loading.value = true
  try {
    const userId = getUserId()
    if (!userId) {
      ElMessage.error('用户未登录或用户ID不存在')
      return
    }
    
    const res = await getEmploymentRecords(userId)
    if (res.code === 200) {
      employmentList.value = res.data
    } else {
      ElMessage.error(res.message || '获取就业信息失败')
    }
  } catch (error) {
    console.error('获取就业信息失败:', error)
    ElMessage.error(error.message || '获取就业信息失败')
  } finally {
    loading.value = false
  }
}

// 显示添加/编辑对话框
const showDialog = (type, record) => {
  // 如果是添加新记录，先检查是否已存在记录
  if (type === 'add' && employmentList.value.length > 0) {
    ElMessage.warning('您已经登记过就业信息，如需修改请使用编辑功能')
    return
  }
  
  dialogType.value = type
  dialogVisible.value = true
  
  if (type === 'add') {
    // 重置表单
    if (formRef.value) {
      formRef.value.resetFields()
    }
    form.student_id = getUserId()
    currentRecordId.value = null
  } else if (type === 'edit' && record) {
    // 填充表单
    form.company_nature = record.company_nature
    form.company = record.company
    form.position = record.position
    form.salary = record.salary
    form.entry_date = formatDate(record.entry_date)
    form.region = record.region
    form.student_id = record.student_id
    currentRecordId.value = record.record_id
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
        const res = await addEmploymentRecord(form)
        if (res.code === 200) {
          ElMessage.success('登记成功')
          dialogVisible.value = false
          fetchEmploymentRecords() // 刷新列表
        } else {
          ElMessage.error(res.message || '登记失败')
        }
      } else {
        // 编辑
        const updateRecord = { ...form, record_id: currentRecordId.value }
        const res = await updateEmploymentRecord(updateRecord)
        if (res.code === 200) {
          ElMessage.success('更新成功')
          dialogVisible.value = false
          fetchEmploymentRecords() // 刷新列表
        } else {
          ElMessage.error(res.message || '更新失败')
        }
      }
    } catch (error) {
      console.error('操作失败:', error)
      ElMessage.error(error.message || '操作失败')
    } finally {
      submitting.value = false
    }
  })
}

// 处理编辑
const handleEdit = (row) => {
  if (row.status === 'approved') {
    ElMessage.warning('已审核的记录不能修改')
    return
  }
  showDialog('edit', row)
}

// 处理删除
const handleDelete = (row) => {
  if (row.status === 'approved') {
    ElMessage.warning('已审核的记录不能删除')
    return
  }
  
  ElMessageBox.confirm('确定删除该就业信息吗？此操作不可恢复', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteEmploymentRecord(row.record_id, row.student_id)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchEmploymentRecords() // 刷新列表
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error(error.message || '删除失败')
    }
  }).catch(() => {
    // 用户取消操作
  })
}

onMounted(() => {
  fetchEmploymentRecords()
})
</script>

<style scoped>
.employment {
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
</style> 
