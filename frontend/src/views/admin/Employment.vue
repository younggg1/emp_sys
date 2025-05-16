<template>
  <div class="employment-manage">
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="学院">
          <el-select v-model="filterForm.college" placeholder="请选择学院" clearable @change="handleCollegeChange">
            <el-option
              v-for="item in collegeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="专业">
          <el-select v-model="filterForm.major" placeholder="请选择专业" clearable :disabled="!filterForm.college">
            <el-option
              v-for="item in majorOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="班级">
          <el-select v-model="filterForm.class" placeholder="请选择班级" clearable>
            <el-option
              v-for="item in classOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="就业状态">
          <el-select v-model="filterForm.status" placeholder="请选择状态" clearable>
            <el-option label="已就业" value="employed"></el-option>
            <el-option label="未就业" value="unemployed"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="filterForm.keyword" placeholder="姓名/学号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
          <el-button type="success" @click="handleExport">导出数据</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="table-header">
        <h3>就业信息列表</h3>
      </div>

      <el-table :data="employmentList" border style="width: 100%" v-loading="loading">
        <el-table-column type="index" width="50" label="序号"></el-table-column>
        <el-table-column prop="student_id" label="学号" width="100"></el-table-column>
        <el-table-column prop="name" label="姓名" width="100"></el-table-column>
        <el-table-column prop="class_name" label="班级"></el-table-column>
        <el-table-column prop="college" label="学院"></el-table-column>
        <el-table-column prop="major" label="专业"></el-table-column>
        <el-table-column prop="employment_status" label="就业状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.employment_status === 'employed' ? 'success' : 'info'">
              {{ scope.row.employment_status === 'employed' ? '已就业' : '未就业' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="查看详情" width="100" align="center">
          <template #default="scope">
            <el-button 
              type="primary" 
              link 
              size="small" 
              @click="handleViewDetails(scope.row)"
              :disabled="scope.row.employment_status !== 'employed'"
            >
              查看详情
            </el-button>
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

    <!-- 就业详情对话框 -->
    <el-dialog
      title="就业详情" 
      v-model="detailsDialogVisible" 
      width="600px"
      :destroy-on-close="true"
    >
      <div v-loading="detailsLoading">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="姓名" :span="1">{{ detailsForm.name }}</el-descriptions-item>
          <el-descriptions-item label="学号" :span="1">{{ detailsForm.student_id }}</el-descriptions-item>
          <el-descriptions-item label="班级" :span="1">{{ detailsForm.class_name }}</el-descriptions-item>
          <el-descriptions-item label="专业" :span="1">{{ detailsForm.major }}</el-descriptions-item>
          <el-descriptions-item label="专业类别" :span="2">
            {{ getMajorCategoryText(detailsForm.major_category) }}
          </el-descriptions-item>
          <el-descriptions-item label="公司名称" :span="2">{{ detailsForm.company }}</el-descriptions-item>
          <el-descriptions-item label="公司性质" :span="1">{{ detailsForm.company_nature }}</el-descriptions-item>
          <el-descriptions-item label="工作职位" :span="1">{{ detailsForm.position }}</el-descriptions-item>
          <el-descriptions-item label="工作地区" :span="1">{{ detailsForm.region }}</el-descriptions-item>
          <el-descriptions-item label="入职日期" :span="1">{{ detailsForm.entry_date }}</el-descriptions-item>
          <el-descriptions-item label="月薪" :span="2">
            {{ detailsForm.salary ? `${detailsForm.salary.toLocaleString()}元` : '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">{{ detailsForm.created_at }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

// 筛选表单
const filterForm = reactive({
  college: '',
  major: '',
  class: '',
  status: '',
  keyword: ''
})

// 表格数据
const employmentList = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 详情对话框控制
const detailsDialogVisible = ref(false)
const detailsLoading = ref(false)
const detailsForm = reactive({
  name: '',
  student_id: '',
  class_name: '',
  major: '',
  major_category: '',
  company: '',
  company_nature: '',
  position: '',
  region: '',
  entry_date: '',
  salary: 0,
  created_at: ''
})

// 下拉选项
const collegeOptions = ref([
  { label: '计算机学院', value: '计算机学院' },
  { label: '理学院', value: '理学院' },
  { label: '经济学院', value: '经济学院' }
])

const majorOptions = ref([])
const classOptions = ref([
  { label: '计算机2021级1班', value: '计算机2021级1班' },
  { label: '计算机2021级2班', value: '计算机2021级2班' },
  { label: '软件工程2021级1班', value: '软件工程2021级1班' }
])

// 获取专业类别文本
const getMajorCategoryText = (category) => {
  const categoryMap = {
    'science': '理工类',
    'humanities': '文史类',
    'sports': '体育类',
    'arts': '艺术类'
  }
  return categoryMap[category] || category
}

// 学院变更处理
const handleCollegeChange = () => {
  filterForm.major = ''
  
  // 根据选择的学院加载对应的专业选项
  if (filterForm.college === '计算机学院') {
    majorOptions.value = [
      { label: '计算机科学与技术', value: '计算机科学与技术' },
      { label: '软件工程', value: '软件工程' },
      { label: '网络工程', value: '网络工程' }
    ]
  } else if (filterForm.college === '理学院') {
    majorOptions.value = [
      { label: '数学与应用数学', value: '数学与应用数学' },
      { label: '物理学', value: '物理学' },
      { label: '化学', value: '化学' }
    ]
  } else if (filterForm.college === '经济学院') {
    majorOptions.value = [
      { label: '经济学', value: '经济学' },
      { label: '金融学', value: '金融学' },
      { label: '国际经济与贸易', value: '国际经济与贸易' }
    ]
  } else {
    majorOptions.value = []
  }
}

// 模拟数据加载
onMounted(() => {
  fetchEmploymentList()
})

// 获取就业信息列表
const fetchEmploymentList = () => {
  loading.value = true
  // 这里应该是从API获取数据
  setTimeout(() => {
    employmentList.value = [
      {
        id: 1,
        student_id: '20210001',
        name: '张三',
        class_name: '计算机2021级1班',
        college: '计算机学院',
        major: '计算机科学与技术',
        employment_status: 'employed',
        record_id: 101
      },
      {
        id: 2,
        student_id: '20210002',
        name: '李四',
        class_name: '计算机2021级1班',
        college: '计算机学院',
        major: '计算机科学与技术',
        employment_status: 'employed',
        record_id: 102
      },
      {
        id: 3,
        student_id: '20210003',
        name: '王五',
        class_name: '计算机2021级2班',
        college: '计算机学院',
        major: '软件工程',
        employment_status: 'unemployed',
        record_id: null
      }
    ]
    total.value = 3
    loading.value = false
  }, 500)
}

// 查看详情
const handleViewDetails = (row) => {
  if (row.employment_status !== 'employed') {
    ElMessage.warning('该学生尚未就业，无法查看详情')
    return
  }
  
  detailsLoading.value = true
  detailsDialogVisible.value = true
  
  // 这里应该是调用API获取详情
  setTimeout(() => {
    Object.assign(detailsForm, {
      name: row.name,
      student_id: row.student_id,
      class_name: row.class_name,
      major: row.major,
      major_category: 'science',
      company: '华为技术有限公司',
      company_nature: '私企',
      position: '软件工程师',
      region: '深圳',
      entry_date: '2023-07-01',
      salary: 15000,
      created_at: '2023-05-15 10:30:00'
    })
    detailsLoading.value = false
  }, 500)
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1
  fetchEmploymentList()
}

// 重置筛选
const resetFilter = () => {
  Object.keys(filterForm).forEach(key => {
    filterForm[key] = ''
  })
  handleSearch()
}

// 导出数据
const handleExport = () => {
  ElMessage.success('就业数据导出功能已触发')
  // 实际导出功能在后端实现
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  fetchEmploymentList()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  fetchEmploymentList()
}
</script>

<style scoped>
.employment-manage {
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