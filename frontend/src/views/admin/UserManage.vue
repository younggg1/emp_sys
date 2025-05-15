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
      管理员端 - 用户管理
    </template>
    
    <div class="user-manage">
      <!-- 用户列表 -->
      <el-card>
        <template #header>
          <div class="card-header">
            <span>用户管理</span>
            <div class="filter-box">
              <el-select v-model="filterRole" placeholder="角色筛选" style="width: 120px; margin-right: 10px;" clearable @change="handleFilterChange">
                <el-option label="全部" value="" />
                <el-option label="学生" value="student" />
                <el-option label="辅导员" value="counselor" />
                <el-option label="管理员" value="admin" />
              </el-select>
              <el-input
                v-model="searchText"
                placeholder="搜索用户ID/姓名"
                style="width: 250px; margin-right: 10px;"
                clearable
                @input="handleFilterChange"
              >
                <template #prefix>
                  <el-icon><search /></el-icon>
                </template>
              </el-input>
              <el-button type="primary" @click="showAddDialog">添加用户</el-button>
            </div>
          </div>
        </template>
        
        <div v-loading="loading">
          <el-table :data="filteredUsers" style="width: 100%">
            <el-table-column prop="user_id" label="用户ID" width="120" />
            <el-table-column prop="name" label="姓名" width="120" />
            <el-table-column prop="role" label="角色" width="120">
              <template #default="scope">
                <el-tag :type="getRoleTagType(scope.row.role)">
                  {{ getRoleText(scope.row.role) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="department" label="院系/部门" />
            <el-table-column prop="create_time" label="创建时间" width="180" />
            <el-table-column prop="last_login" label="上次登录" width="180" />
            <el-table-column label="操作" width="240" fixed="right">
              <template #default="scope">
                <el-button size="small" type="primary" @click="handleResetPassword(scope.row)">重置密码</el-button>
                <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-card>
      
      <!-- 添加用户对话框 -->
      <el-dialog
        title="添加用户"
        v-model="addDialogVisible"
        width="50%"
      >
        <el-form
          ref="addFormRef"
          :model="addForm"
          :rules="addFormRules"
          label-width="100px"
        >
          <el-form-item label="用户名" prop="username">
            <el-input v-model="addForm.username" placeholder="请输入用户名" />
          </el-form-item>
          
          <el-form-item label="密码" prop="password">
            <el-input v-model="addForm.password" type="password" placeholder="请输入密码" show-password />
          </el-form-item>
          
          <el-form-item label="姓名" prop="name">
            <el-input v-model="addForm.name" placeholder="请输入姓名" />
          </el-form-item>
          
          <el-form-item label="角色" prop="role">
            <el-radio-group v-model="addForm.role">
              <el-radio label="student">学生</el-radio>
              <el-radio label="counselor">辅导员</el-radio>
              <el-radio label="admin">管理员</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="所属学校" prop="school_id">
            <el-select v-model="addForm.school_id" placeholder="请选择所属学校" style="width: 100%">
              <el-option
                v-for="school in schools"
                :key="school.school_id"
                :label="school.school_name"
                :value="school.school_id"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item 
            :label="addForm.role === 'student' ? '班级' : '部门'" 
            prop="department"
          >
            <el-input v-model="addForm.department" :placeholder="addForm.role === 'student' ? '请输入班级' : '请输入部门'" />
          </el-form-item>
          
          <el-form-item v-if="addForm.role === 'student'" label="专业" prop="major">
            <el-input v-model="addForm.major" placeholder="请输入专业" />
          </el-form-item>
          
          <el-form-item label="电话" prop="phone">
            <el-input v-model="addForm.phone" placeholder="请输入电话" />
          </el-form-item>
          
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="addForm.email" placeholder="请输入邮箱" />
          </el-form-item>
        </el-form>
        
        <template #footer>
          <el-button @click="addDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitAddForm" :loading="submitting">确定</el-button>
        </template>
      </el-dialog>
      
      <!-- 重置密码对话框 -->
      <el-dialog
        title="重置密码"
        v-model="resetPasswordDialogVisible"
        width="40%"
      >
        <el-form
          ref="resetFormRef"
          :model="resetForm"
          :rules="resetFormRules"
          label-width="100px"
        >
          <el-form-item label="用户">
            <span>{{ currentUser.name }} ({{ getRoleText(currentUser.role) }})</span>
          </el-form-item>
          
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="resetForm.newPassword" type="password" placeholder="请输入新密码" show-password />
          </el-form-item>
          
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="resetForm.confirmPassword" type="password" placeholder="请确认新密码" show-password />
          </el-form-item>
        </el-form>
        
        <template #footer>
          <el-button @click="resetPasswordDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitResetForm" :loading="submitting">确定</el-button>
        </template>
      </el-dialog>
    </div>
  </Layout>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { DataAnalysis, Document, ChatDotRound, PieChart, User, Key, Setting, Search } from '@element-plus/icons-vue'
import Layout from '@/components/Layout.vue'
import { getSchools } from '@/api/auth'
import { getStudents, addUser, resetPassword, deleteUser } from '@/api/admin'

// 状态
const loading = ref(false)
const submitting = ref(false)
const users = ref([])
const schools = ref([])
const filterRole = ref('')
const searchText = ref('')

// 添加用户对话框
const addDialogVisible = ref(false)
const addFormRef = ref(null)
const addForm = reactive({
  username: '',
  password: '',
  name: '',
  role: 'student',
  school_id: '',
  department: '',
  major: '',
  phone: '',
  email: ''
})

// 重置密码对话框
const resetPasswordDialogVisible = ref(false)
const resetFormRef = ref(null)
const resetForm = reactive({
  newPassword: '',
  confirmPassword: ''
})
const currentUser = ref({})

// 添加用户表单验证规则
const addFormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度应为3-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ],
  school_id: [
    { required: true, message: '请选择所属学校', trigger: 'change' }
  ],
  department: [
    { required: true, message: '请输入部门/班级', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

// 重置密码表单验证规则
const resetFormRules = {
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== resetForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 过滤后的用户列表
const filteredUsers = computed(() => {
  let result = users.value
  
  // 筛选角色
  if (filterRole.value) {
    result = result.filter(user => user.role === filterRole.value)
  }
  
  // 搜索文本
  if (searchText.value) {
    const search = searchText.value.toLowerCase()
    result = result.filter(user => 
      user.user_id.toString().includes(search) ||
      user.name.toLowerCase().includes(search)
    )
  }
  
  return result
})

// 获取角色文本
const getRoleText = (role) => {
  const roleMap = {
    'student': '学生',
    'counselor': '辅导员',
    'admin': '管理员'
  }
  return roleMap[role] || role
}

// 获取角色标签类型
const getRoleTagType = (role) => {
  const typeMap = {
    'student': '',
    'counselor': 'success',
    'admin': 'danger'
  }
  return typeMap[role] || ''
}

// 获取用户列表
const fetchUsers = async () => {
  loading.value = true
  try {
    const res = await getStudents() // 实际上这里会获取所有用户，不只是学生
    if (res.code === 200) {
      users.value = res.data
    }
  } catch (error) {
    ElMessage.error(error.message || '获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 获取学校列表
const fetchSchools = async () => {
  try {
    const res = await getSchools()
    if (res.code === 200) {
      schools.value = res.data
    }
  } catch (error) {
    ElMessage.error(error.message || '获取学校列表失败')
  }
}

// 显示添加用户对话框
const showAddDialog = () => {
  // 重置表单
  if (addFormRef.value) {
    addFormRef.value.resetFields()
  } else {
    Object.keys(addForm).forEach(key => {
      addForm[key] = key === 'role' ? 'student' : ''
    })
  }
  
  addDialogVisible.value = true
}

// 提交添加用户表单
const submitAddForm = async () => {
  if (!addFormRef.value) return
  
  await addFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    
    try {
      const res = await addUser(addForm)
      if (res.code === 200) {
        ElMessage.success('添加用户成功')
        addDialogVisible.value = false
        fetchUsers() // 刷新用户列表
      }
    } catch (error) {
      ElMessage.error(error.message || '添加用户失败')
    } finally {
      submitting.value = false
    }
  })
}

// 处理重置密码
const handleResetPassword = (user) => {
  currentUser.value = user
  
  // 重置表单
  resetForm.newPassword = ''
  resetForm.confirmPassword = ''
  
  resetPasswordDialogVisible.value = true
}

// 提交重置密码表单
const submitResetForm = async () => {
  if (!resetFormRef.value) return
  
  await resetFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    
    try {
      const res = await resetPassword(currentUser.value.user_id, resetForm.newPassword)
      if (res.code === 200) {
        ElMessage.success('重置密码成功')
        resetPasswordDialogVisible.value = false
      }
    } catch (error) {
      ElMessage.error(error.message || '重置密码失败')
    } finally {
      submitting.value = false
    }
  })
}

// 处理删除用户
const handleDelete = (user) => {
  ElMessageBox.confirm(
    `确定要删除用户 ${user.name} (${getRoleText(user.role)}) 吗？此操作不可恢复。`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const res = await deleteUser(user.user_id)
      if (res.code === 200) {
        ElMessage.success('删除用户成功')
        fetchUsers() // 刷新用户列表
      }
    } catch (error) {
      ElMessage.error(error.message || '删除用户失败')
    }
  }).catch(() => {})
}

// 处理筛选条件变化
const handleFilterChange = () => {
  // 筛选逻辑在computed中处理
}

onMounted(() => {
  fetchUsers()
  fetchSchools()
})
</script>

<style scoped>
.user-manage {
  max-width: 1200px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-box {
  display: flex;
  align-items: center;
}
</style> 
