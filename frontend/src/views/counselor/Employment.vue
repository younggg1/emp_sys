<template>
  <div class="employment">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span><el-icon class="header-icon"><briefcase /></el-icon> 就业信息列表</span>
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
        :showActions="false"
      >
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
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Briefcase, Search, Delete, Check } from '@element-plus/icons-vue'
import DataTable from '@/components/DataTable.vue'
import { getEmploymentRecords, deleteEmployment, getStudents, approveEmployment, getCounselorListWithUserInfo } from '@/api/counselor'
import { getUserId } from '@/utils/user'

// 表格列定义
const columns = [
  { prop: 'studentId', label: '学号', width: '120' },
  { prop: 'name', label: '姓名', width: '100' },
  { prop: 'major', label: '专业' },
  { prop: 'company_nature', label: '企业性质' },
  { prop: 'company', label: '公司名称' },
  { prop: 'position', label: '职位' },
  { prop: 'salary', label: '月薪' ,width:'70'},
  { prop: 'entry_date', label: '入职日期' },
  { prop: 'region', label: '地区' ,width:'80'},
  { prop: 'status', label: '状态', slot: 'status', width: '100' },
  { prop: 'actions', label: '操作', width: '220', slot: 'actions' }
]

const loading = ref(false)
const employmentList = ref([])
const students = ref([])
const searchText = ref('')
const filterStatus = ref('')
const hasDeletePermission = ref(false)

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
    // 获取当前登录的辅导员ID
    const counselorId = getUserId()
    if (!counselorId) {
      ElMessage.error('未获取到辅导员ID，请重新登录')
      return
    }
    
    // 调用API获取就业信息列表
    const res = await getEmploymentRecords({ counselorId })
    
    if (res.code === 200) {
      employmentList.value = res.data
    } else {
      ElMessage.error(res.message || '获取就业信息失败')
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
    // 获取当前登录的辅导员ID
    const counselorId = getUserId()
    if (!counselorId) {
      ElMessage.error('未获取到辅导员ID，请重新登录')
      return
    }
    
    // 调用API获取学生列表
    const res = await getStudents({ counselorId })
    
    if (res.code === 200) {
      students.value = res.data
    } else {
      ElMessage.error(res.message || '获取学生列表失败')
    }
  } catch (error) {
    ElMessage.error(error.message || '获取学生列表失败')
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

// 审核通过
const handleApprove = async (row) => {
  try {
    // 获取当前辅导员ID
    const counselorId = getUserId();
    if (!counselorId) {
      ElMessage.error('用户未登录或登录信息已失效');
      return;
    }
    
    const res = await approveEmployment(row.record_id, {
      status: 'approved',
      counselorId
    });
    
    if (res.code === 200) {
      ElMessage.success('审核通过成功');
      // 更新本地数据
      const index = employmentList.value.findIndex(item => item.record_id === row.record_id);
      if (index !== -1) {
        employmentList.value[index].status = 'approved';
      }
    } else {
      ElMessage.error(res.message || '审核失败');
    }
  } catch (error) {
    console.error('审核就业信息失败:', error);
    ElMessage.error(error.message || '审核失败');
  }
}

// 处理删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除该就业信息吗？此操作不可恢复', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const counselorId = getUserId()
      if (!counselorId) {
        ElMessage.error('未获取到辅导员ID，请重新登录')
        return
      }
      
      // 调用API删除数据
      const res = await deleteEmployment(row.record_id, { counselorId })
      
      if (res.code === 200) {
        ElMessage.success('删除成功')
        
        // 从本地数据中移除
        const index = employmentList.value.findIndex(item => item.record_id === row.record_id)
        if (index !== -1) {
          employmentList.value.splice(index, 1)
        }
      } else {
        // 处理没有权限或记录不存在的情况
        ElMessage.error(res.message || '删除失败，可能没有权限或记录不存在')
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
  fetchCounselorPermission()
})
</script>

<style scoped>
.employment {
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
</style>
