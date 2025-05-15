<template>
  <Layout>
    <template #menu>
      <el-menu-item index="/student">
        <el-icon><house /></el-icon>
        <span>个人信息</span>
      </el-menu-item>
      <el-menu-item index="/student/employment">
        <el-icon><document /></el-icon>
        <span>就业信息管理</span>
      </el-menu-item>
      <el-menu-item index="/student/feedback">
        <el-icon><chat-dot-round /></el-icon>
        <span>就业反馈管理</span>
      </el-menu-item>
    </template>

    <template #header-title>
      学生端 - 个人信息
    </template>
    
    <div class="dashboard">
      <el-card class="info-card">
        <template #header>
          <div class="card-header">
            <span>个人信息</span>
          </div>
        </template>
        <div v-loading="loading">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="学号">{{ studentInfo.student_id }}</el-descriptions-item>
            <el-descriptions-item label="姓名">{{ studentInfo.name }}</el-descriptions-item>
            <el-descriptions-item label="班级">{{ studentInfo.class_name }}</el-descriptions-item>
            <el-descriptions-item label="学院">{{ studentInfo.college }}</el-descriptions-item>
            <el-descriptions-item label="专业">{{ studentInfo.major }}</el-descriptions-item>
            <el-descriptions-item label="辅导员">{{ studentInfo.counselor_name }}</el-descriptions-item>
            <el-descriptions-item label="就业状态">
              <el-tag :type="studentInfo.employment_status === 'employed' ? 'success' : 'info'">
                {{ studentInfo.employment_status === 'employed' ? '已就业' : '未就业' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </el-card>
    </div>
  </Layout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { House, Document, ChatDotRound } from '@element-plus/icons-vue'
import Layout from '@/components/Layout.vue'
import { getStudentInfo } from '@/api/student'

const loading = ref(false)
const studentInfo = ref({
  student_id: '',
  name: '',
  class_name: '',
  college: '',
  major: '',
  counselor_name: '',
  employment_status: 'unemployed'
})

// 获取学生信息
const fetchStudentInfo = async () => {
  loading.value = true
  try {
    const res = await getStudentInfo()
    if (res.code === 200) {
      studentInfo.value = res.data
    }
  } catch (error) {
    ElMessage.error(error.message || '获取个人信息失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchStudentInfo()
})
</script>

<style scoped>
.dashboard {
  max-width: 1000px;
  margin: 0 auto;
}

.info-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 
