<template>
  <div class="user-manage">
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="角色">
          <el-select v-model="filterForm.role" placeholder="请选择角色" clearable>
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
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="role" label="角色">
          <template #default="scope">
            <el-tag :type="scope.row.role === 'student' ? 'primary' : 'success'">
              {{ scope.row.role === 'student' ? '学生' : '辅导员' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="classInfo" label="班级信息" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.role === 'student'">{{ scope.row.classInfo }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="college" label="学院" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.role === 'student'">{{ scope.row.college }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="major" label="专业" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.role === 'student'">{{ scope.row.major }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间"></el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-button type="primary" link @click="resetPassword(scope.row)">重置密码</el-button>
            <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
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
        <el-form-item label="班级" prop="className">
          <el-input v-model="studentForm.className" placeholder="请输入班级"></el-input>
        </el-form-item>
        <el-form-item label="学院" prop="college">
          <el-input v-model="studentForm.college" placeholder="请输入学院"></el-input>
        </el-form-item>
        <el-form-item label="专业" prop="major">
          <el-input v-model="studentForm.major" placeholder="请输入专业"></el-input>
        </el-form-item>
        <el-form-item label="辅导员" prop="counselorId">
          <el-select v-model="studentForm.counselorId" placeholder="请选择辅导员" style="width: 100%">
            <el-option
              v-for="item in counselorList"
              :key="item.counselorId"
              :label="item.name"
              :value="item.counselorId"
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

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
  className: '',
  college: '',
  major: '',
  counselorId: ''
})

// 辅导员表单数据
const counselorForm = reactive({
  username: '',
  password: '',
  name: ''
})

// 辅导员列表
const counselorList = ref([])

// 表单验证规则
const studentRules = {
  username: [
    { required: true, message: '请输入学号', trigger: 'blur' },
    { min: 5, max: 20, message: '长度在 5 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  className: [
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
    { min: 5, max: 20, message: '长度在 5 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ]
}

// 模拟数据加载
onMounted(() => {
  fetchUserList()
  fetchCounselorList()
})

// 获取用户列表
const fetchUserList = () => {
  loading.value = true
  // 这里应该是从API获取数据
  setTimeout(() => {
    userList.value = [
      {
        id: 1,
        username: '20210001',
        name: '张三',
        role: 'student',
        classInfo: '计算机2021级1班',
        college: '计算机学院',
        major: '计算机科学与技术',
        createdAt: '2023-01-01'
      },
      {
        id: 2,
        username: '20210002',
        name: '李四',
        role: 'student',
        classInfo: '计算机2021级2班',
        college: '计算机学院',
        major: '软件工程',
        createdAt: '2023-01-02'
      },
      {
        id: 3,
        username: 'teacher001',
        name: '王老师',
        role: 'counselor',
        createdAt: '2023-01-03'
      }
    ]
    total.value = 3
    loading.value = false
  }, 500)
}

// 获取辅导员列表
const fetchCounselorList = () => {
  // 这里应该是从API获取数据
  counselorList.value = [
    { counselorId: 1, name: '王老师' },
    { counselorId: 2, name: '李老师' }
  ]
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
  handleSearch()
}

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
  studentFormRef.value.validate((valid) => {
    if (valid) {
      submitLoading.value = true
      // 这里应该是调用API添加学生
      setTimeout(() => {
        ElMessage.success('添加学生成功')
        addStudentDialogVisible.value = false
        submitLoading.value = false
        fetchUserList()
      }, 500)
    }
  })
}

// 提交添加辅导员
const submitAddCounselor = () => {
  counselorFormRef.value.validate((valid) => {
    if (valid) {
      submitLoading.value = true
      // 这里应该是调用API添加辅导员
      setTimeout(() => {
        ElMessage.success('添加辅导员成功')
        addCounselorDialogVisible.value = false
        submitLoading.value = false
        fetchUserList()
      }, 500)
    }
  })
}

// 重置密码
const resetPassword = (row) => {
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
  ElMessageBox.confirm(`确定要删除用户"${row.name}"吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 这里应该是调用API删除用户
    ElMessage.success('删除成功')
    fetchUserList()
  }).catch(() => {})
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