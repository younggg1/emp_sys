<template>
  <Layout>
    <template #menu>
      <el-menu-item index="/admin">
        <el-icon><data-analysis /></el-icon>
        <span>系统概览</span>
      </el-menu-item>
      <el-menu-item index="/admin/employment">
        <el-icon><document /></el-icon>
        <span>就业信息管理</span>
      </el-menu-item>
      <el-menu-item index="/admin/feedback">
        <el-icon><chat-dot-round /></el-icon>
        <span>就业反馈管理</span>
      </el-menu-item>
      <el-menu-item index="/admin/statistics">
        <el-icon><pie-chart /></el-icon>
        <span>统计分析</span>
      </el-menu-item>
      <el-menu-item index="/admin/user-manage">
        <el-icon><user /></el-icon>
        <span>用户管理</span>
      </el-menu-item>
      <el-menu-item index="/admin/permission">
        <el-icon><key /></el-icon>
        <span>权限管理</span>
      </el-menu-item>
      <el-menu-item index="/admin/settings">
        <el-icon><setting /></el-icon>
        <span>系统设置</span>
      </el-menu-item>
    </template>

    <template #header-title>
      管理员端 - 权限管理
    </template>
    
    <div class="permission">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>辅导员权限管理</span>
            <div class="filter-box">
              <el-input
                v-model="searchText"
                placeholder="搜索用户ID/姓名"
                style="width: 250px;"
                clearable
                @input="handleFilterChange"
              >
                <template #prefix>
                  <el-icon><search /></el-icon>
                </template>
              </el-input>
            </div>
          </div>
        </template>
        
        <div v-loading="loading">
          <el-table :data="filteredUsers" style="width: 100%">
            <el-table-column prop="user_id" label="用户ID" width="120" />
            <el-table-column prop="name" label="姓名" width="120" />
            <el-table-column prop="department" label="所属部门" />
            <el-table-column prop="can_edit" label="编辑权限">
              <template #default="scope">
                <el-switch
                  v-model="scope.row.can_edit"
                  @change="handlePermissionChange(scope.row, 'edit')"
                />
              </template>
            </el-table-column>
            <el-table-column prop="can_delete" label="删除权限">
              <template #default="scope">
                <el-switch
                  v-model="scope.row.can_delete"
                  @change="handlePermissionChange(scope.row, 'delete')"
                />
              </template>
            </el-table-column>
            <el-table-column prop="can_audit" label="审核权限">
              <template #default="scope">
                <el-switch
                  v-model="scope.row.can_audit"
                  @change="handlePermissionChange(scope.row, 'audit')"
                />
              </template>
            </el-table-column>
            <el-table-column prop="can_export" label="导出权限">
              <template #default="scope">
                <el-switch
                  v-model="scope.row.can_export"
                  @change="handlePermissionChange(scope.row, 'export')"
                />
              </template>
            </el-table-column>
          </el-table>
          
          <el-pagination
            v-if="totalUsers > pageSize"
            style="margin-top: 20px; text-align: right;"
            :current-page="currentPage"
            :page-sizes="[10, 20, 50]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="totalUsers"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
      
      <el-card class="role-card">
        <template #header>
          <div class="card-header">
            <span>角色默认权限</span>
          </div>
        </template>
        
        <el-form label-width="150px">
          <el-descriptions title="学生权限" :column="1" border>
            <el-descriptions-item label="查看个人信息">
              <el-tag type="success">允许</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="提交就业信息">
              <el-tag type="success">允许</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="提交反馈信息">
              <el-tag type="success">允许</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="编辑已提交信息">
              <el-tag type="info">仅未审核</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="删除已提交信息">
              <el-tag type="info">仅未审核</el-tag>
            </el-descriptions-item>
          </el-descriptions>
          
          <el-divider />
          
          <el-descriptions title="辅导员权限" :column="1" border>
            <el-descriptions-item label="查看本班学生">
              <el-tag type="success">允许</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="审核就业/反馈信息">
              <el-tag>按分配权限</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="编辑就业/反馈信息">
              <el-tag>按分配权限</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="删除就业/反馈信息">
              <el-tag>按分配权限</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="导出统计数据">
              <el-tag>按分配权限</el-tag>
            </el-descriptions-item>
          </el-descriptions>
          
          <el-divider />
          
          <el-descriptions title="管理员权限" :column="1" border>
            <el-descriptions-item label="系统设置">
              <el-tag type="success">允许</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="用户管理">
              <el-tag type="success">允许</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="权限分配">
              <el-tag type="success">允许</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="全部数据查看">
              <el-tag type="success">允许</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="全部数据编辑/删除">
              <el-tag type="success">允许</el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-form>
      </el-card>
    </div>
  </Layout>
</template>

<script setup>
import { ref, computed, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { DataAnalysis, Document, ChatDotRound, PieChart, User, Key, Setting, Search } from '@element-plus/icons-vue'
import Layout from '@/components/Layout.vue'
import { getStudents, assignPermission } from '@/api/admin'

// 状态
const loading = ref(false)
const searchText = ref('')
const users = ref([])
const currentPage = ref(1)
const pageSize = ref(10)

// 获取总用户数
const totalUsers = computed(() => filteredUsers.value.length)

// 过滤后的用户
const filteredUsers = computed(() => {
  if (!searchText.value) {
    return users.value.filter(user => user.role === 'counselor')
  }
  
  const search = searchText.value.toLowerCase()
  return users.value.filter(user => 
    user.role === 'counselor' && (
      user.user_id.toString().includes(search) ||
      user.name.toLowerCase().includes(search)
    )
  )
})

// 分页后的用户列表
const paginatedUsers = computed(() => {
  const startIndex = (currentPage.value - 1) * pageSize.value
  const endIndex = startIndex + pageSize.value
  return filteredUsers.value.slice(startIndex, endIndex)
})

// 获取用户列表
const fetchUsers = async () => {
  loading.value = true
  try {
    const res = await getStudents() // 这里获取所有用户，实际上应该只获取辅导员
    if (res.code === 200) {
      users.value = res.data
    }
  } catch (error) {
    ElMessage.error(error.message || '获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 处理权限变更
const handlePermissionChange = async (user, type) => {
  try {
    let canEdit = user.can_edit
    let canDelete = user.can_delete
    let canAudit = user.can_audit
    let canExport = user.can_export
    
    // 更新当前修改的权限
    if (type === 'edit') {
      canEdit = !canEdit
    } else if (type === 'delete') {
      canDelete = !canDelete
    } else if (type === 'audit') {
      canAudit = !canAudit
    } else if (type === 'export') {
      canExport = !canExport
    }
    
    const res = await assignPermission(
      user.user_id,
      canEdit,
      canDelete,
      canAudit,
      canExport
    )
    
    if (res.code === 200) {
      ElMessage.success('权限修改成功')
    }
  } catch (error) {
    ElMessage.error(error.message || '权限修改失败')
    // 恢复原来的状态
    if (type === 'edit') {
      user.can_edit = !user.can_edit
    } else if (type === 'delete') {
      user.can_delete = !user.can_delete
    } else if (type === 'audit') {
      user.can_audit = !user.can_audit
    } else if (type === 'export') {
      user.can_export = !user.can_export
    }
  }
}

// 处理筛选条件变化
const handleFilterChange = () => {
  currentPage.value = 1 // 重置到第一页
}

// 处理每页数量变化
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
}

// 处理页码变化
const handleCurrentChange = (page) => {
  currentPage.value = page
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.permission {
  max-width: 1200px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.role-card {
  margin-top: 20px;
}
</style> 
