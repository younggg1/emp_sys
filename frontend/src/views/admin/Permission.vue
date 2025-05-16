<template>
  <div class="permission-manage">
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="辅导员姓名">
          <el-input v-model="filterForm.name" placeholder="请输入辅导员姓名"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="table-header">
        <h3>辅导员权限管理</h3>
      </div>

      <el-table :data="counselorList" border style="width: 100%" v-loading="loading">
        <el-table-column type="index" width="50" label="序号"></el-table-column>
        <el-table-column prop="username" label="工号"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column label="编辑权限">
          <template #default="scope">
            <el-switch
              v-model="scope.row.canEdit"
              @change="(val) => handlePermissionChange(scope.row, 'edit', val)"
              :active-text="'允许'"
              :inactive-text="'禁止'"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="删除权限">
          <template #default="scope">
            <el-switch
              v-model="scope.row.canDelete"
              @change="(val) => handlePermissionChange(scope.row, 'delete', val)"
              :active-text="'允许'"
              :inactive-text="'禁止'"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="updatedAt" label="更新时间"></el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        ></el-pagination>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

// 筛选表单
const filterForm = reactive({
  name: ''
})

// 表格数据
const counselorList = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 模拟数据加载
onMounted(() => {
  fetchCounselorList()
})

// 获取辅导员列表及权限
const fetchCounselorList = () => {
  loading.value = true
  // 这里应该是从API获取数据
  setTimeout(() => {
    counselorList.value = [
      {
        id: 1,
        username: 'teacher001',
        name: '王老师',
        canEdit: true,
        canDelete: false,
        updatedAt: '2023-05-01 10:00:00'
      },
      {
        id: 2,
        username: 'teacher002',
        name: '李老师',
        canEdit: false,
        canDelete: false,
        updatedAt: '2023-05-01 11:00:00'
      },
      {
        id: 3,
        username: 'teacher003',
        name: '张老师',
        canEdit: true,
        canDelete: true,
        updatedAt: '2023-05-01 12:00:00'
      }
    ]
    total.value = 3
    loading.value = false
  }, 500)
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1
  fetchCounselorList()
}

// 重置筛选
const resetFilter = () => {
  filterForm.name = ''
  handleSearch()
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  fetchCounselorList()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  fetchCounselorList()
}

// 处理权限变更
const handlePermissionChange = (row, type, value) => {
  // 这里应该是调用API更新权限
  const actionText = type === 'edit' ? '编辑' : '删除'
  const statusText = value ? '授予' : '取消'
  
  ElMessage.success(`已${statusText}辅导员 ${row.name} 的${actionText}权限`)
  
  // 更新显示的时间
  row.updatedAt = new Date().toLocaleString()
}
</script>

<style scoped>
.permission-manage {
  padding: 0;
}

.filter-card {
  margin-bottom: 20px;
}

.filter-form {
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
}

.table-card {
  margin-bottom: 20px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.table-header h3 {
  margin: 0;
  font-size: 18px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 