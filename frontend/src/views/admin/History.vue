<template>
  <div class="history">
    <el-card shadow="hover" class="filter-card">
      <template #header>
        <div class="card-header">
          <span><el-icon class="header-icon"><calendar /></el-icon> 历年就业数据管理</span>
          <div>
            <el-button type="primary" @click="handleAddRecord">
              <el-icon><plus /></el-icon> 添加数据
            </el-button>
            <el-button type="success" @click="handleExport">
              <el-icon><download /></el-icon> 导出数据
            </el-button>
          </div>
        </div>
      </template>
      
      <el-tabs v-model="activeTab" @tab-click="handleTabChange">
        <el-tab-pane label="数据列表" name="list">
          <el-table
            :data="historyData"
            border
            style="width: 100%"
            v-loading="loading"
          >
            <el-table-column prop="year" label="年份" width="100" sortable />
            <el-table-column prop="total_students" label="毕业生总数" width="120" sortable />
            <el-table-column prop="employed_students" label="就业人数" width="120" sortable />
            <el-table-column prop="employment_rate" label="就业率" sortable>
              <template #default="scope">
                {{ scope.row.employment_rate }}%
              </template>
            </el-table-column>
            <el-table-column prop="avg_salary" label="平均薪资" sortable>
              <template #default="scope">
                {{ scope.row.avg_salary.toLocaleString() }}元
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button size="small" type="primary" @click="handleEdit(scope.row)">
                  <el-icon><edit /></el-icon> 编辑
                </el-button>
                <el-button size="small" type="danger" @click="handleDelete(scope.row)">
                  <el-icon><delete /></el-icon> 删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        
        <el-tab-pane label="趋势分析" name="trend">
          <div class="chart-container">
            <el-row :gutter="20">
              <el-col :span="24">
                <StatChart
                  chart-type="line"
                  title="历年就业率变化"
                  :data="trendData.rate"
                  :x-axis-data="trendData.years"
                  height="400px"
                />
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="12">
                <StatChart
                  chart-type="bar"
                  title="历年毕业生人数"
                  :data="trendData.students"
                  :x-axis-data="trendData.years"
                  height="300px"
                />
              </el-col>
              <el-col :span="12">
                <StatChart
                  chart-type="line"
                  title="历年平均薪资"
                  :data="trendData.salary"
                  :x-axis-data="trendData.years"
                  height="300px"
                />
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
    
    <!-- 添加/编辑记录对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
        label-position="right"
      >
        <el-form-item label="年份" prop="year">
          <el-input-number v-model="form.year" :min="2000" :max="2100" :precision="0" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="毕业生总数" prop="total_students">
          <el-input-number v-model="form.total_students" :min="0" :precision="0" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="就业人数" prop="employed_students">
          <el-input-number 
            v-model="form.employed_students" 
            :min="0" 
            :max="form.total_students" 
            :precision="0" 
            style="width: 100%" 
          />
        </el-form-item>
        
        <el-form-item label="就业率" prop="employment_rate">
          <el-input-number 
            v-model="form.employment_rate" 
            :min="0" 
            :max="100" 
            :precision="2" 
            style="width: 100%"
            :disabled="true"
          />
        </el-form-item>
        
        <el-form-item label="平均薪资" prop="avg_salary">
          <el-input-number v-model="form.avg_salary" :min="0" :precision="2" :step="500" style="width: 100%" />
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
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Calendar, Plus, Download, Edit, Delete } from '@element-plus/icons-vue'
import StatChart from '@/components/StatChart.vue'
import { getHistoryData, addHistoryRecord, updateHistoryRecord, deleteHistoryRecord, mockGetHistoryData } from '@/api/admin'

// 数据加载状态
const loading = ref(false)
const submitting = ref(false)

// 表格数据
const historyData = ref([])

// 当前激活的tab
const activeTab = ref('list')

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('添加历年就业数据')
const formRef = ref(null)
const currentId = ref(null)

// 表单数据
const form = reactive({
  year: new Date().getFullYear(),
  total_students: 0,
  employed_students: 0,
  employment_rate: 0,
  avg_salary: 0
})

// 表单验证规则
const rules = {
  year: [{ required: true, message: '请输入年份', trigger: 'blur' }],
  total_students: [{ required: true, message: '请输入毕业生总数', trigger: 'blur' }],
  employed_students: [{ required: true, message: '请输入就业人数', trigger: 'blur' }],
  avg_salary: [{ required: true, message: '请输入平均薪资', trigger: 'blur' }]
}

// 趋势数据
const trendData = computed(() => {
  const sortedData = [...historyData.value].sort((a, b) => a.year - b.year)
  return {
    years: sortedData.map(item => `${item.year}年`),
    rate: sortedData.map(item => item.employment_rate),
    students: sortedData.map(item => item.total_students),
    salary: sortedData.map(item => item.avg_salary)
  }
})

// 监听就业人数和总人数变化，自动计算就业率
watch([() => form.total_students, () => form.employed_students], () => {
  if (form.total_students > 0) {
    form.employment_rate = ((form.employed_students / form.total_students) * 100).toFixed(2)
  } else {
    form.employment_rate = 0
  }
})

// 获取历年就业数据
const fetchHistoryData = async () => {
  loading.value = true
  try {
    // 使用模拟数据或实际API调用
    const res = await mockGetHistoryData()
    historyData.value = res.data || []
  } catch (error) {
    ElMessage.error(error.message || '获取数据失败')
  } finally {
    loading.value = false
  }
}

// 处理添加记录
const handleAddRecord = () => {
  dialogTitle.value = '添加历年就业数据'
  currentId.value = null
  
  // 重置表单
  Object.keys(form).forEach(key => {
    form[key] = key === 'year' ? new Date().getFullYear() : 0
  })
  
  dialogVisible.value = true
}

// 处理编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑历年就业数据'
  currentId.value = row.id
  
  // 填充表单
  Object.keys(form).forEach(key => {
    form[key] = row[key]
  })
  
  dialogVisible.value = true
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    
    try {
      // 模拟成功响应
      ElMessage.success(currentId.value ? '更新成功' : '添加成功')
      
      // 模拟添加/更新数据
      if (currentId.value) {
        // 更新现有记录
        const index = historyData.value.findIndex(item => item.id === currentId.value)
        if (index !== -1) {
          historyData.value[index] = { ...form, id: currentId.value }
        }
      } else {
        // 添加新记录
        const maxId = historyData.value.reduce((max, item) => Math.max(max, item.id), 0)
        historyData.value.push({ ...form, id: maxId + 1 })
      }
      
      dialogVisible.value = false
    } catch (error) {
      ElMessage.error(error.message || (currentId.value ? '编辑失败' : '添加失败'))
    } finally {
      submitting.value = false
    }
  })
}

// 处理删除
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除 ${row.year} 年的就业数据吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 模拟成功响应
      ElMessage.success('删除成功')
      
      // 从本地数据中移除
      const index = historyData.value.findIndex(item => item.id === row.id)
      if (index !== -1) {
        historyData.value.splice(index, 1)
      }
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  }).catch(() => {})
}

// 处理导出
const handleExport = () => {
  ElMessage.success('数据导出功能已触发')
  // 实际导出功能在后端实现
}

// 处理Tab切换
const handleTabChange = () => {
  // 可以在切换到趋势分析时执行特定逻辑
}

onMounted(() => {
  fetchHistoryData()
})
</script>

<style scoped>
.history {
  max-width: 1200px;
  margin: 0 auto;
}

.filter-card {
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

.chart-container {
  padding: 10px;
}
</style> 