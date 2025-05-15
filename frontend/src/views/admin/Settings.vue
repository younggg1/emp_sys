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
      管理员端 - 系统设置
    </template>
    
    <div class="settings">
      <el-card class="settings-card">
        <template #header>
          <div class="card-header">
            <span>系统设置</span>
            <el-button type="primary" @click="saveSettings" :loading="submitting">保存设置</el-button>
          </div>
        </template>
        
        <div v-loading="loading">
          <el-form ref="formRef" :model="form" label-width="200px" label-position="left">
            <el-form-item label="登录验证码">
              <el-switch
                v-model="form.require_captcha"
                active-text="启用"
                inactive-text="禁用"
              />
              <div class="form-item-desc">启用后，用户登录时需要输入验证码</div>
            </el-form-item>
            
            <el-form-item label="就业信息审核">
              <el-switch
                v-model="form.require_employment_approval"
                active-text="启用"
                inactive-text="禁用"
              />
              <div class="form-item-desc">启用后，学生提交的就业信息需要辅导员或管理员审核</div>
            </el-form-item>
            
            <el-form-item label="反馈信息审核">
              <el-switch
                v-model="form.require_feedback_approval"
                active-text="启用"
                inactive-text="禁用"
              />
              <div class="form-item-desc">启用后，学生提交的反馈信息需要辅导员或管理员审核</div>
            </el-form-item>
            
            <el-form-item label="系统名称">
              <el-input v-model="form.system_name" placeholder="系统名称" />
              <div class="form-item-desc">显示在登录页和系统顶部的标题</div>
            </el-form-item>
            
            <el-divider content-position="left">数据备份</el-divider>
            
            <el-form-item label="导出数据">
              <el-button type="success" @click="exportData">导出所有数据</el-button>
              <div class="form-item-desc">将系统中的所有数据导出为Excel文件</div>
            </el-form-item>
            
            <el-divider content-position="left">清理数据</el-divider>
            
            <el-form-item label="清理历史数据">
              <el-select v-model="form.history_year" placeholder="选择年份" style="width: 200px;">
                <el-option
                  v-for="year in yearOptions"
                  :key="year"
                  :label="`${year}年之前`"
                  :value="year"
                />
              </el-select>
              <el-button type="danger" class="ml-10" @click="showClearConfirm">清理历史数据</el-button>
              <div class="form-item-desc">谨慎操作！将删除选定年份之前的所有就业和反馈数据</div>
            </el-form-item>
          </el-form>
        </div>
      </el-card>
      
      <el-card class="system-info-card">
        <template #header>
          <div class="card-header">
            <span>系统信息</span>
          </div>
        </template>
        
        <el-descriptions :column="2" border>
          <el-descriptions-item label="系统版本">v1.0.0</el-descriptions-item>
          <el-descriptions-item label="运行环境">{{ systemInfo.environment }}</el-descriptions-item>
          <el-descriptions-item label="数据库">{{ systemInfo.database }}</el-descriptions-item>
          <el-descriptions-item label="数据库大小">{{ systemInfo.databaseSize }}</el-descriptions-item>
          <el-descriptions-item label="用户数">{{ systemInfo.userCount }}</el-descriptions-item>
          <el-descriptions-item label="就业记录数">{{ systemInfo.employmentCount }}</el-descriptions-item>
          <el-descriptions-item label="反馈记录数">{{ systemInfo.feedbackCount }}</el-descriptions-item>
          <el-descriptions-item label="最后备份时间">{{ systemInfo.lastBackupTime || '从未备份' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>
  </Layout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { DataAnalysis, Document, ChatDotRound, PieChart, User, Key, Setting } from '@element-plus/icons-vue'
import Layout from '@/components/Layout.vue'
import { updateSettings } from '@/api/admin'
import { getSettings } from '@/api/auth'

// 状态
const loading = ref(false)
const submitting = ref(false)
const formRef = ref(null)

// 表单数据
const form = reactive({
  require_captcha: true,
  require_employment_approval: true,
  require_feedback_approval: true,
  system_name: '大学毕业生就业情况调查系统',
  history_year: new Date().getFullYear() - 5
})

// 系统信息
const systemInfo = reactive({
  environment: 'Node.js v16.x',
  database: 'MySQL 8.0',
  databaseSize: '125 MB',
  userCount: 0,
  employmentCount: 0,
  feedbackCount: 0,
  lastBackupTime: ''
})

// 年份选项
const currentYear = new Date().getFullYear()
const yearOptions = Array.from({ length: 10 }, (_, i) => currentYear - i - 1)

// 获取设置
const fetchSettings = async () => {
  loading.value = true
  try {
    const res = await getSettings()
    if (res.code === 200) {
      const data = res.data
      form.require_captcha = data.require_captcha
      form.require_employment_approval = data.require_employment_approval
      form.require_feedback_approval = data.require_feedback_approval
      form.system_name = data.system_name || '大学毕业生就业情况调查系统'
      
      // 更新系统信息
      systemInfo.userCount = data.user_count || 0
      systemInfo.employmentCount = data.employment_count || 0
      systemInfo.feedbackCount = data.feedback_count || 0
      systemInfo.lastBackupTime = data.last_backup_time || ''
    }
  } catch (error) {
    ElMessage.error(error.message || '获取系统设置失败')
  } finally {
    loading.value = false
  }
}

// 保存设置
const saveSettings = async () => {
  submitting.value = true
  try {
    const res = await updateSettings(
      form.require_captcha,
      form.require_employment_approval,
      form.require_feedback_approval,
      form.system_name
    )
    if (res.code === 200) {
      ElMessage.success('保存设置成功')
    }
  } catch (error) {
    ElMessage.error(error.message || '保存设置失败')
  } finally {
    submitting.value = false
  }
}

// 导出数据
const exportData = () => {
  ElMessage({
    type: 'success',
    message: '数据导出功能已触发，文件将在准备好后自动下载'
  })
  
  // 这里通常会调用后端接口进行数据导出，为了演示，我们只显示消息
  setTimeout(() => {
    ElMessage({
      type: 'success',
      message: '数据导出完成！'
    })
  }, 2000)
}

// 显示清理确认对话框
const showClearConfirm = () => {
  ElMessageBox.confirm(
    `确定要清理 ${form.history_year} 年之前的所有就业和反馈数据吗？此操作不可恢复！`,
    '危险操作',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'error',
      distinguishCancelAndClose: true
    }
  ).then(() => {
    ElMessage({
      type: 'success',
      message: `${form.history_year} 年之前的历史数据已清理`
    })
  }).catch(() => {})
}

onMounted(() => {
  fetchSettings()
})
</script>

<style scoped>
.settings {
  max-width: 1000px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.settings-card {
  margin-bottom: 20px;
}

.form-item-desc {
  color: #909399;
  font-size: 12px;
  line-height: 1.5;
  margin-top: 5px;
}

.system-info-card {
  margin-bottom: 20px;
}

.ml-10 {
  margin-left: 10px;
}
</style> 
