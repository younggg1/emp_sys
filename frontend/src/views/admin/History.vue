<template>
  <div class="history-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>就业信息列表</span>
          <div class="search-box">
            <el-input
              v-model="selectedYear"
              placeholder="请输入年份（如：2024）"
              style="width: 300px;"
              @keyup.enter="handleYearChange"
              clearable
            >
              <template #append>
                <el-button @click="handleYearChange">
                  <el-icon><search /></el-icon>
                </el-button>
              </template>
            </el-input>
          </div>
        </div>
      </template>
      <el-table
        v-loading="loading"
        :data="filteredTableData"
        border
        style="width: 100%"
      >
        <el-table-column prop="student_id" label="学号" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="major" label="专业" />
        <el-table-column prop="company_nature" label="企业性质" />
        <el-table-column prop="company_name" label="公司名称" />
        <el-table-column prop="position" label="职位" />
        <el-table-column prop="salary" label="月薪" />
        <el-table-column prop="entry_date" label="入职日期" />
        <el-table-column prop="region" label="地区" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="scope">
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">
              <el-icon><delete /></el-icon> 删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Search } from '@element-plus/icons-vue'
import { getHistoryData, deleteHistoryData } from '@/api/admin'

const tableData = ref([])
const loading = ref(false)
const selectedYear = ref('')

// 根据选择的年份筛选数据
const filteredTableData = computed(() => {
  if (!selectedYear.value) {
    return tableData.value
  }
  return tableData.value.filter(item => {
    const entryDate = new Date(item.entry_date)
    return entryDate.getFullYear().toString() === selectedYear.value
  })
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getHistoryData()
    if (res.code === 200) {
      tableData.value = res.data
      console.log('tableData', tableData.value)
    } else {
      ElMessage.error(res.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 处理年份变化
const handleYearChange = () => {
  // 验证年份格式
  const yearRegex = /^\d{4}$/
  if (selectedYear.value && !yearRegex.test(selectedYear.value)) {
    ElMessage.warning('请输入正确的年份格式（如：2024）')
    return
  }
  
  // 验证年份范围
  const currentYear = new Date().getFullYear()
  const inputYear = parseInt(selectedYear.value)
  if (selectedYear.value && (inputYear < 2000 || inputYear > currentYear)) {
    ElMessage.warning(`年份范围应在2000-${currentYear}之间`)
    return
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteHistoryData(row.id)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchData()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.history-container {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.search-box {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 320px;
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