<template>
  <div class="feedback-manage">
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
        <el-form-item label="反馈阶段">
          <el-select v-model="filterForm.stage" placeholder="请选择阶段" clearable>
            <el-option label="初期适应" value="initial"></el-option>
            <el-option label="中期评估" value="middle"></el-option>
            <el-option label="正式工作" value="formal"></el-option>
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
        <h3>就业反馈列表</h3>
      </div>

      <el-table :data="feedbackList" border style="width: 100%" v-loading="loading">
        <el-table-column type="index" width="50" label="序号"></el-table-column>
        <el-table-column prop="student_id" label="学号" width="100"></el-table-column>
        <el-table-column prop="name" label="姓名" width="100"></el-table-column>
        <el-table-column prop="class_name" label="班级"></el-table-column>
        <el-table-column prop="major" label="专业"></el-table-column>
        <el-table-column prop="stage" label="反馈阶段" width="100">
          <template #default="scope">
            <el-tag :type="getStageTagType(scope.row.stage)">
              {{ getStageText(scope.row.stage) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="created_at" label="提交时间" width="160"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'approved' ? 'success' : 'warning'">
              {{ scope.row.status === 'approved' ? '已审核' : '待审核' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center">
          <template #default="scope">
            <el-button 
              type="primary" 
              link 
              size="small" 
              @click="handleViewContent(scope.row)"
            >
              查看内容
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

    <!-- 反馈内容对话框 -->
    <el-dialog
      title="反馈内容详情" 
      v-model="contentDialogVisible" 
      width="600px"
      :destroy-on-close="true"
    >
      <div v-loading="contentLoading">
        <el-descriptions :column="2" border class="content-descriptions">
          <el-descriptions-item label="学生姓名" :span="1">{{ currentFeedback.name }}</el-descriptions-item>
          <el-descriptions-item label="学号" :span="1">{{ currentFeedback.student_id }}</el-descriptions-item>
          <el-descriptions-item label="班级" :span="1">{{ currentFeedback.class_name }}</el-descriptions-item>
          <el-descriptions-item label="专业" :span="1">{{ currentFeedback.major }}</el-descriptions-item>
          <el-descriptions-item label="反馈阶段" :span="1">{{ getStageText(currentFeedback.stage) }}</el-descriptions-item>
          <el-descriptions-item label="提交时间" :span="1">{{ currentFeedback.created_at }}</el-descriptions-item>
        </el-descriptions>

        <div class="feedback-content">
          <h4>反馈内容:</h4>
          <div class="content-box">{{ currentFeedback.content }}</div>
        </div>
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
  stage: '',
  keyword: ''
})

// 表格数据
const feedbackList = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 内容对话框控制
const contentDialogVisible = ref(false)
const contentLoading = ref(false)
const currentFeedback = reactive({
  name: '',
  student_id: '',
  class_name: '',
  major: '',
  stage: '',
  created_at: '',
  content: ''
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

// 获取反馈阶段文本
const getStageText = (stage) => {
  const stageMap = {
    'initial': '初期适应',
    'middle': '中期评估',
    'formal': '正式工作'
  }
  return stageMap[stage] || stage
}

// 获取反馈阶段标签类型
const getStageTagType = (stage) => {
  const typeMap = {
    'initial': 'info',
    'middle': 'warning',
    'formal': 'success'
  }
  return typeMap[stage] || 'info'
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
  fetchFeedbackList()
})

// 获取反馈列表
const fetchFeedbackList = () => {
  loading.value = true
  // 这里应该是从API获取数据
  setTimeout(() => {
    feedbackList.value = [
      {
        id: 1,
        student_id: '20210001',
        name: '张三',
        class_name: '计算机2021级1班',
        college: '计算机学院',
        major: '计算机科学与技术',
        stage: 'initial',
        created_at: '2023-07-15 10:20:30',
        status: 'approved'
      },
      {
        id: 2,
        student_id: '20210002',
        name: '李四',
        class_name: '计算机2021级1班',
        college: '计算机学院',
        major: '计算机科学与技术',
        stage: 'middle',
        created_at: '2023-08-10 14:30:45',
        status: 'approved'
      },
      {
        id: 3,
        student_id: '20210004',
        name: '赵六',
        class_name: '软件工程2021级1班',
        college: '计算机学院',
        major: '软件工程',
        stage: 'formal',
        created_at: '2023-09-05 09:15:20',
        status: 'pending'
      }
    ]
    total.value = 3
    loading.value = false
  }, 500)
}

// 查看反馈内容
const handleViewContent = (row) => {
  contentLoading.value = true
  contentDialogVisible.value = true
  
  // 这里应该是调用API获取详情
  setTimeout(() => {
    Object.assign(currentFeedback, {
      name: row.name,
      student_id: row.student_id,
      class_name: row.class_name,
      major: row.major,
      stage: row.stage,
      created_at: row.created_at,
      content: row.stage === 'initial' 
        ? '刚入职一个月，公司氛围很好，同事们都很热情帮助我适应工作环境。目前主要在学习公司的业务知识和技术栈，虽然有些挑战但很充实。希望在未来几个月能够更快地融入团队并做出贡献。'
        : row.stage === 'middle'
          ? '已入职三个月，逐渐熟悉了公司的工作流程和技术栈。现在已经能够独立完成一些开发任务，并参与了两个小型项目。工作压力适中，有加班但不算太多。薪资与当初承诺的一致，福利待遇也不错。'
          : '入职已满六个月，现在已经完全融入了团队，能够独立负责模块开发。公司的晋升机制比较透明，已经参与了年度绩效评估。整体来说对目前的工作状态很满意，希望能在这个平台上继续成长。'
    })
    contentLoading.value = false
  }, 500)
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1
  fetchFeedbackList()
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
  ElMessage.success('反馈数据导出功能已触发')
  // 实际导出功能在后端实现
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  fetchFeedbackList()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  fetchFeedbackList()
}
</script>

<style scoped>
.feedback-manage {
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

.content-descriptions {
  margin-bottom: 20px;
}

.feedback-content h4 {
  margin-top: 20px;
  margin-bottom: 10px;
  font-size: 16px;
  font-weight: bold;
}

.content-box {
  background-color: #f5f7fa;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 15px;
  min-height: 100px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
}
</style> 