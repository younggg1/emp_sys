<template>
  <div class="user-manage">
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="角色">
          <el-select v-model="filterForm.role" placeholder="请选择角色" clearable style="width: 200px">
            <el-option label="学生" value="student"></el-option>
            <el-option label="辅导员" value="counselor"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="filterForm.keyword" placeholder="用户名/姓名"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="table-header">
        <h3>用户列表</h3>
        <div class="action-buttons">
          <el-button type="primary" @click="showAddStudentDialog">添加学生</el-button>
          <el-button type="success" @click="showAddCounselorDialog">添加辅导员</el-button>
        </div>
      </div>

      <el-table :data="userList" border style="width: 100%" v-loading="loading">
        <el-table-column type="index" width="50" label="序号"></el-table-column>
        <el-table-column prop="username" label="学号/工号"></el-table-column>
        <el-table-column prop="display_name" label="姓名"></el-table-column>
        <el-table-column prop="role" label="角色">
          <template #default="scope">
            <el-tag :type="getRoleType(scope.row.role)">
              {{ getRoleName(scope.row.role) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="display_class_name" label="班级信息" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.role === 'student'">{{ scope.row.display_class_name }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="display_college" label="学院" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.role === 'student'">{{ scope.row.display_college }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="display_major" label="专业" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.role === 'student'">{{ scope.row.display_major }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="created_at" label="创建时间">
          <template #default="scope">
            {{ formatDate(scope.row.created_at) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280">
          <template #default="scope">
            <el-button 
              type="primary" 
              link 
              @click="handleEdit(scope.row)"
              :disabled="scope.row.role === 'admin'"
            >编辑</el-button>
            
            <el-button 
              type="danger" 
              link 
              @click="handleDelete(scope.row)"
              :disabled="scope.row.role === 'admin'"
            >删除</el-button>
          </template>
        </el-table-column>
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

    <!-- 添加学生对话框 -->
    <el-dialog v-model="addStudentDialogVisible" title="添加学生" width="500px">
      <el-form :model="studentForm" :rules="studentRules" ref="studentFormRef" label-width="100px">
        <el-form-item label="学号" prop="username">
          <el-input v-model="studentForm.username" placeholder="请输入学号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="studentForm.password" type="password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="studentForm.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="班级" prop="class_name">
          <el-input v-model="studentForm.class_name" placeholder="请输入班级"></el-input>
        </el-form-item>
        <el-form-item label="学院" prop="college">
          <el-input v-model="studentForm.college" placeholder="请输入学院"></el-input>
        </el-form-item>
        <el-form-item label="专业" prop="major">
          <el-input v-model="studentForm.major" placeholder="请输入专业"></el-input>
        </el-form-item>
        <el-form-item label="辅导员" prop="counselor_id">
          <el-select v-model="studentForm.counselor_id" placeholder="请选择辅导员" style="width: 100%">
            <el-option
              v-for="item in counselorList"
              :key="item.counselor_id"
              :label="item.name"
              :value="item.counselor_id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addStudentDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitAddStudent" :loading="submitLoading">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 添加辅导员对话框 -->
    <el-dialog v-model="addCounselorDialogVisible" title="添加辅导员" width="500px">
      <el-form :model="counselorForm" :rules="counselorRules" ref="counselorFormRef" label-width="100px">
        <el-form-item label="工号" prop="username">
          <el-input v-model="counselorForm.username" placeholder="请输入工号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="counselorForm.password" type="password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="counselorForm.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addCounselorDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitAddCounselor" :loading="submitLoading">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑用户对话框 -->
<el-dialog v-model="editDialogVisible" :title="editForm.role === 'student' ? '编辑学生' : '编辑辅导员'" width="500px">
  <el-form :model="editForm" :rules="editForm.role === 'student' ? studentRules : counselorRules" ref="editFormRef" label-width="100px">
    <el-form-item label="学号/工号" prop="username">
      <el-input v-model="editForm.username" placeholder="请输入学号/工号" :disabled="true"></el-input>
    </el-form-item>
    <el-form-item label="姓名" prop="name">
      <el-input v-model="editForm.name" placeholder="请输入姓名"></el-input>
    </el-form-item>
    <template v-if="editForm.role === 'student'">
      <el-form-item label="班级" prop="class_name">
        <el-input v-model="editForm.class_name" placeholder="请输入班级"></el-input>
      </el-form-item>
      <el-form-item label="学院" prop="college">
        <el-input v-model="editForm.college" placeholder="请输入学院"></el-input>
      </el-form-item>
      <el-form-item label="专业" prop="major">
        <el-input v-model="editForm.major" placeholder="请输入专业"></el-input>
      </el-form-item>
      <el-form-item label="辅导员" prop="counselor_id">
        <el-select v-model="editForm.counselor_id" placeholder="请选择辅导员" style="width: 100%">
          <el-option
            v-for="item in counselorList"
            :key="item.counselor_id"
            :label="item.name"
            :value="item.counselor_id"
          ></el-option>
        </el-select>
      </el-form-item>
    </template>
  </el-form>
  <template #footer>
    <span class="dialog-footer">
      <el-button @click="editDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitEdit" :loading="submitLoading">确定</el-button>
    </span>
  </template>
</el-dialog>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, addCounselor , getCounselorList ,addStudent,updateUser,deleteUser} from '@/api/admin'

// 筛选表单
const filterForm = reactive({
  role: '',
  keyword: ''
})

// 表格数据
const userList = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 对话框控制
const addStudentDialogVisible = ref(false)
const addCounselorDialogVisible = ref(false)
const submitLoading = ref(false)

// 表单引用
const studentFormRef = ref(null)
const counselorFormRef = ref(null)

// 学生表单数据
const studentForm = reactive({
  username: '',
  password: '',
  name: '',
  class_name: '',
  college: '',
  major: '',
  counselor_id: ''
})

// 辅导员表单数据
const counselorForm = reactive({
  username: '',
  password: '',
  name: ''
})
// 编辑对话框控制
const editDialogVisible = ref(false)
const editFormRef = ref(null)

// 编辑表单数据
const editForm = reactive({
  user_id: '',
  username: '',
  name: '',
  class_name: '',
  role: '',
  college: '',
  major: '',
  counselor_id: ''
})

// 处理编辑
const handleEdit = (row) => {
  editForm.user_id = row.user_id
  editForm.username = row.username
  editForm.name = row.display_name
  editForm.role = row.role
  if (row.role === 'student') {
    editForm.class_name = row.display_class_name
    editForm.college = row.display_college
    editForm.major = row.display_major
    editForm.counselor_id = row.counselor_id
    fetchCounselorList()
  }
  editDialogVisible.value = true
}

// 提交编辑
const submitEdit = () => {
  editFormRef.value.validate(async (valid) => {
    if (editForm.role === 'admin') {
      ElMessage.warning('不能编辑管理员账号')
      return
    }
    if (valid) {
      submitLoading.value = true
      try {
        const res = await updateUser(editForm.user_id, {
          name: editForm.name,
          class_name: editForm.class_name,
          college: editForm.college,
          major: editForm.major,
          counselor_id: editForm.counselor_id
        })
        if (res.code === 200) {
          ElMessage.success('更新成功')
          editDialogVisible.value = false
          fetchUserList()
        } else {
          ElMessage.error(res.msg || '更新失败')
        }
      } catch (error) {
        console.error('更新失败:', error)
        ElMessage.error('更新失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}
// 辅导员列表
const counselorList = ref([])

// 表单验证规则
const studentRules = {
  username: [
    { required: true, message: '请输入学号', trigger: 'blur' },
   
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
 
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  class_name: [
    { required: true, message: '请输入班级', trigger: 'blur' }
  ],
  college: [
    { required: true, message: '请输入学院', trigger: 'blur' }
  ],
  major: [
    { required: true, message: '请输入专业', trigger: 'blur' }
  ],
  counselorId: [
    { required: true, message: '请选择辅导员', trigger: 'change' }
  ]
}

const counselorRules = {
  username: [
    { required: true, message: '请输入工号', trigger: 'blur' },
    
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
   
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ]
}


onMounted(() => {
  fetchUserList()
  fetchCounselorList()
})

// 获取用户列表
const fetchUserList = async () => {
  loading.value = true
  try {
    const res = await getUserList({
      page: currentPage.value,
      size: pageSize.value,
      role: filterForm.role,
      keyword: filterForm.keyword
    })
    if (res.code === 200 && res.data) {
      console.log('用户列表数据:', res.data.records)
      userList.value = res.data.records
      total.value = res.data.total
    } else {
      userList.value = []
      total.value = 0
      ElMessage.error(res.msg || '获取用户列表失败')
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 获取辅导员列表
const fetchCounselorList = async () => {
  try {
    const res = await getCounselorList()
    if (res.code === 200) {
      counselorList.value = res.data
    } else {
      ElMessage.error(res.msg || '获取辅导员列表失败')
    }
  } catch (error) {
    console.error('获取辅导员列表失败:', error)
    ElMessage.error('获取辅导员列表失败')
  }
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1
  fetchUserList()
}

// 重置筛选
const resetFilter = () => {
  filterForm.role = ''
  filterForm.keyword = ''
  currentPage.value = 1
  fetchUserList()
}

// 监听筛选条件变化
watch([() => filterForm.role, () => filterForm.keyword], () => {
  handleSearch()
})

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  fetchUserList()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  fetchUserList()
}

// 显示添加学生对话框
const showAddStudentDialog = () => {
  Object.keys(studentForm).forEach(key => {
    studentForm[key] = ''
  })
  addStudentDialogVisible.value = true
  fetchCounselorList() 
}

// 显示添加辅导员对话框
const showAddCounselorDialog = () => {
  Object.keys(counselorForm).forEach(key => {
    counselorForm[key] = ''
  })
  addCounselorDialogVisible.value = true
}

// 提交添加学生
const submitAddStudent = () => {
  studentFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const res = await addStudent({
          username: studentForm.username,
          password: studentForm.password,
          name: studentForm.name,
          grade: '2024',
          class_name: studentForm.class_name,
          college: studentForm.college,
          major: studentForm.major,
          counselor_id: studentForm.counselor_id
        })
        
        if (res.code === 200) {
          ElMessage.success('添加学生成功')
          addStudentDialogVisible.value = false
          fetchUserList()
        } else {
          ElMessage.error(res.msg || '添加学生失败')
        }
      } catch (error) {
        console.error('添加学生失败:', error)
        ElMessage.error('添加学生失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 提交添加辅导员
const submitAddCounselor = () => {
  counselorFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const res = await addCounselor({
          username: counselorForm.username,
          password: counselorForm.password,
          name: counselorForm.name
        })
        
        if (res.code === 200) {
          ElMessage.success('添加辅导员成功')
          addCounselorDialogVisible.value = false
          fetchUserList()
          fetchCounselorList()
        } else {
          ElMessage.error(res.msg || '添加辅导员失败')
        }
      } catch (error) {
        console.error('添加辅导员失败:', error)
        ElMessage.error('添加辅导员失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}



// 重置密码
const resetPassword = (row) => {
  if (row.role === 'admin') {
    ElMessage.warning('不能重置管理员密码')
    return
  }
  ElMessageBox.confirm(`确定要重置用户"${row.name}"的密码吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 这里应该是调用API重置密码
    ElMessage.success('密码重置成功')
  }).catch(() => {})
}

// 删除用户
const handleDelete = (row) => {
  if (row.role === 'admin') {
    ElMessage.warning('不能删除管理员账号')
    return
  }
  // 处理删除
  ElMessageBox.confirm(
    '确定要删除该用户吗？',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const res = await deleteUser(row.user_id)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchUserList()
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 获取角色类型
const getRoleType = (role) => {
  switch (role) {
    case 'student':
      return 'primary'
    case 'counselor':
      return 'success'
    case 'admin':
      return 'danger'
    default:
      return 'info'
  }
}

// 获取角色名称
const getRoleName = (role) => {
  switch (role) {
    case 'student':
      return '学生'
    case 'counselor':
      return '辅导员'
    case 'admin':
      return '管理员'
    default:
      return role
  }
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return '-'
  const d = new Date(date)
  return d.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}
</script>

<style scoped>
.user-manage {
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

.action-buttons {
  display: flex;
  gap: 10px;
}
</style> 