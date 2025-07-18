﻿<template>
  <div class="dashboard">
    <el-card class="info-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span><el-icon class="header-icon"><user /></el-icon> 个人信息</span>
        </div>
      </template>
      <div v-loading="loading">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="学号">{{ studentInfo.studentId }}</el-descriptions-item>
          <el-descriptions-item label="姓名">{{ studentInfo.name }}</el-descriptions-item>
          <el-descriptions-item label="班级">{{ studentInfo.class_name }}</el-descriptions-item>
          <el-descriptions-item label="学院">{{ studentInfo.college }}</el-descriptions-item>
          <el-descriptions-item label="专业">{{ studentInfo.major }}</el-descriptions-item>
          <el-descriptions-item label="辅导员">{{ studentInfo.counselorName }}</el-descriptions-item>
          <el-descriptions-item label="就业状态">
            <el-tag :type="studentInfo.employment_status === 'employed' ? 'success' : 'info'">
              {{ studentInfo.employment_status === 'employed' ? '已就业' : '未就业' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        <br>
        <h5>如果上述信息与本人不符请联系学校管理员修改</h5>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { User } from '@element-plus/icons-vue'
import { getStudentInfo } from '@/api/student'
import { getUserId } from '@/utils/user'

const loading = ref(false)
const studentInfo = ref({
  studentId: '',
  name: '',
  class_name: '',
  college: '',
  major: '',
  counselorName: '',
  employment_status: 'unemployed'
})


// 获取学生信息
const fetchStudentInfo = async () => {
  loading.value = true
  try {
    // 从用户工具中获取用户 ID
    const userId = getUserId()
    if (!userId) {
      ElMessage.error('用户未登录或用户ID不存在')
      return
    }
    
    console.log('正在请求用户ID:', userId)
    
    // 调用实际API
    const res = await getStudentInfo(userId)
    console.log('获取到的学生信息:', res)
    
    if (res.code === 200) {
      studentInfo.value = res.data
      console.log('更新后的学生信息组件数据:', studentInfo.value)
      loading.value = false
    } else {
      ElMessage.error(res.message || '获取学生信息失败')
      loading.value = false
    }
  } catch (error) {
    console.error('获取学生信息错误:', error)
    ElMessage.error(error.message || '获取个人信息失败')
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

:deep(.el-descriptions__label) {
  font-weight: bold;
}
</style> 
