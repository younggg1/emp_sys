<template>
  <div class="settings-manage">
    <el-card class="settings-card">
      <div class="settings-header">
        <h3>系统参数设置</h3>
      </div>

      <el-form 
        :model="settingsForm" 
        label-width="180px" 
        class="settings-form"
        :disabled="loading"
      >
        <el-form-item label="登录验证码">
          <el-switch
            v-model="settingsForm.requireCaptcha"
            active-text="启用"
            inactive-text="关闭"
            @change="handleSettingChange('requireCaptcha')"
          ></el-switch>
          <div class="form-item-desc">开启后，登录页面将显示验证码，用户需要输入正确的验证码才能登录</div>
        </el-form-item>

        <el-form-item label="辅导员审核">
          <el-switch
            v-model="settingsForm.requireApproval"
            active-text="启用"
            inactive-text="关闭"
            @change="handleSettingChange('requireApproval')"
          ></el-switch>
          <div class="form-item-desc">开启后，学生提交的就业信息和反馈需要辅导员审核后才能生效</div>
        </el-form-item>
      </el-form>

      <el-divider></el-divider>

      <div class="settings-history">
        <h3>设置记录</h3>
        <el-timeline>
          <el-timeline-item
            v-for="(item, index) in settingsHistory"
            :key="index"
            :timestamp="item.time"
            :color="item.type === 'requireCaptcha' ? '#409EFF' : '#67C23A'"
          >
            {{ item.content }}
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

// 设置表单数据
const settingsForm = reactive({
  requireCaptcha: false,
  requireApproval: true
})

// 设置修改历史
const settingsHistory = ref([])
const loading = ref(false)

// 模拟数据加载
onMounted(() => {
  fetchSettings()
  fetchSettingsHistory()
})

// 获取系统设置
const fetchSettings = () => {
  loading.value = true
  // 这里应该是从API获取数据
  setTimeout(() => {
    settingsForm.requireCaptcha = true
    settingsForm.requireApproval = true
    loading.value = false
  }, 500)
}

// 获取设置变更历史
const fetchSettingsHistory = () => {
  // 这里应该是从API获取数据
  settingsHistory.value = [
    {
      type: 'requireCaptcha',
      content: '管理员(admin)启用了登录验证码',
      time: '2023-06-01 10:00:00'
    },
    {
      type: 'requireApproval',
      content: '管理员(admin)启用了辅导员审核',
      time: '2023-06-01 09:30:00'
    },
    {
      type: 'requireCaptcha',
      content: '管理员(admin)关闭了登录验证码',
      time: '2023-05-30 14:20:00'
    }
  ]
}

// 处理设置变更
const handleSettingChange = (type) => {
  loading.value = true
  // 这里应该是调用API更新设置
  
  const settingText = type === 'requireCaptcha' ? '登录验证码' : '辅导员审核'
  const actionText = settingsForm[type] ? '启用' : '关闭'
  
  setTimeout(() => {
    // 更新设置历史
    settingsHistory.value.unshift({
      type,
      content: `管理员(admin)${actionText}了${settingText}`,
      time: new Date().toLocaleString()
    })
    
    ElMessage.success(`已${actionText}${settingText}`)
    loading.value = false
  }, 500)
}
</script>

<style scoped>
.settings-manage {
  padding: 0;
}

.settings-card {
  margin-bottom: 20px;
}

.settings-header {
  margin-bottom: 30px;
}

.settings-header h3 {
  margin: 0;
  font-size: 18px;
}

.settings-form {
  max-width: 600px;
}

.form-item-desc {
  font-size: 12px;
  color: #909399;
  line-height: 1.5;
  margin-top: 5px;
}

.settings-history {
  margin-top: 30px;
}

.settings-history h3 {
  margin: 0 0 20px 0;
  font-size: 16px;
}
</style>