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
      辅导员端 - 就业信息管理
    </template>
    
    <div class="employment">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>就业信息列表</span>
            <div class="search-box">
              <el-input
                v-model="searchText"
                placeholder="搜索学生/公司/职位"
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
          :data="filteredEmployment"
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
          <template #actions="{ row }">
            <el-button 
              size="small" 
              type="success" 
              v-if="row.status !== 'approved'"
              @click="handleAudit(row)"
            >
              审核
            </el-button>
            <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </DataTable>
      </el-card>
      
      <!-- 编辑就业信息表单 -->
      <el-dialog
        title="编辑就业信息"
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
          <el-form-item label="学生" prop="student_id">
            <el-select v-model="form.student_id" placeholder="请选择学生" style="width: 100%" disabled>
              <el-option 
                v-for="student in students" 
                :key="student.student_id" 
                :label="`${student.name} (${student.student_id})`" 
                :value="student.student_id" 
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" placeholder="请输入姓名" />
          </el-form-item>
          
          <el-form-item label="专业" prop="major">
            <el-input v-model="form.major" placeholder="请输入专业" />
          </el-form-item>
          
          <el-form-item label="专业类别" prop="major_category">
            <el-select v-model="form.major_category" placeholder="请选择专业类别" style="width: 100%">
              <el-option label="理工类" value="science" />
              <el-option label="文史类" value="humanities" />
              <el-option label="体育类" value="sports" />
              <el-option label="艺术类" value="arts" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="企业性质" prop="company_nature">
            <el-input v-model="form.company_nature" placeholder="请输入企业性质" />
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
          
          <el-form-item label="审核状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
              <el-option label="已审核" value="approved" />
              <el-option label="待审核" value="pending" />
            </el-select>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Document, ChatDotRound, PieChart, Search } from '@element-plus/icons-vue'
import Layout from '@/components/Layout.vue'
import DataTable from '@/components/DataTable.vue'
import { getEmploymentRecords, auditEmployment, editEmployment, deleteEmployment } from '@/api/counselor'
import { getStudents } from '@/api/counselor'

// 表格列定义
const columns = [
  { prop: 'student_id', label: '学号', width: '100' },
  { prop: 'name', label: '姓名', width: '100' },
  { prop: 'major', label: '专业' },
  { prop: 'major_category', label: '专业类别', formatter: (row) => {
    const map = { science: '理工类', humanities: '文史类', sports: '体育类', arts: '艺术类' }
    return map[row.major_category] || row.major_category
  }},
  { prop: 'company', label: '公司名称' },
  { prop: 'position', label: '职位' },
  { prop: 'salary', label: '月薪' },
  { prop: 'entry_date', label: '入职日期' },
  { prop: 'region', label: '地区' },
  { prop: 'status', label: '状态', slot: 'status' }
]

const loading = ref(false)
const submitting = ref(false)
const employmentList = ref([])
const students = ref([])
const dialogVisible = ref(false)
const searchText = ref('')
const filterStatus = ref('')
const currentRecordId = ref(null)
const formRef = ref(null)

// 表单数据
const form = reactive({
  student_id: '',
  name: '',
  major: '',
  major_category: '',
  company_nature: '',
  company: '',
  position: '',
  salary: 0,
  entry_date: '',
  region: '',
  status: ''
})

// 表单验证规则
const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  major: [{ required: true, message: '请输入专业', trigger: 'blur' }],
  major_category: [{ required: true, message: '请选择专业类别', trigger: 'change' }],
  company_nature: [{ required: true, message: '请输入企业性质', trigger: 'blur' }],
  company: [{ required: true, message: '请输入公司名称', trigger: 'blur' }],
  position: [{ required: true, message: '请输入职位', trigger: 'blur' }],
  salary: [{ required: true, message: '请输入月薪', trigger: 'blur' }],
  entry_date: [{ required: true, message: '请选择入职日期', trigger: 'change' }],
  region: [{ required: true, message: '请输入地区', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

// 过滤后的就业信息列表
const filteredEmployment = computed(() => {
  let result = employmentList.value
  
  // 筛选状态
  if (filterStatus.value) {
    result = result.filter(item => item.status === filterStatus.value)
  }
  
  // 搜索文本
  if (searchText.value) {
    const search = searchText.value.toLowerCase()
    result = result.filter(item => 
      item.name.toLowerCase().includes(search) ||
      item.company.toLowerCase().includes(search) ||
      item.position.toLowerCase().includes(search)
    )
  }
  
  return result
})

// 获取就业信息列表
const fetchEmploymentRecords = async () => {
  loading.value = true
  try {
    const res = await getEmploymentRecords()
    if (res.code === 200) {
      employmentList.value = res.data
    }
  } catch (error) {
    ElMessage.error(error.message || '获取就业信息失败')
  } finally {
    loading.value = false
  }
}

// 获取学生列表（用于表单选择）
const fetchStudents = async () => {
  try {
    const res = await getStudents()
    if (res.code === 200) {
      students.value = res.data
    }
  } catch (error) {
    ElMessage.error(error.message || '获取学生列表失败')
  }
}

// 处理编辑
const handleEdit = (row) => {
  dialogVisible.value = true
  currentRecordId.value = row.record_id
  
  // 填充表单
  Object.keys(form).forEach(key => {
    form[key] = row[key]
  })
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    
    try {
      const res = await editEmployment(currentRecordId.value, form)
      if (res.code === 200) {
        ElMessage.success('更新成功')
        dialogVisible.value = false
        fetchEmploymentRecords() // 刷新列表
      }
    } catch (error) {
      ElMessage.error(error.message || '编辑失败')
    } finally {
      submitting.value = false
    }
  })
}

// 处理审核
const handleAudit = (row) => {
  ElMessageBox.confirm('确定审核通过该就业信息吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await auditEmployment(row.record_id)
      if (res.code === 200) {
        ElMessage.success('审核成功')
        fetchEmploymentRecords() // 刷新列表
      }
    } catch (error) {
      ElMessage.error(error.message || '审核失败')
    }
  }).catch(() => {})
}

// 处理删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除该就业信息吗？此操作不可恢复', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteEmployment(row.record_id)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchEmploymentRecords() // 刷新列表
      }
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  }).catch(() => {})
}

// 处理搜索
const handleSearch = () => {
  // 搜索逻辑在computed中处理
}

onMounted(() => {
  fetchEmploymentRecords()
  fetchStudents()
})
</script>

<style scoped>
.employment {
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
</style>
