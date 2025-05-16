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
            @change="handleCaptchaChange"
          ></el-switch>
          <div class="form-item-desc">开启后，登录页面将显示验证码，用户需要输入正确的验证码才能登录</div>
        </el-form-item>
      </el-form>

      <el-divider></el-divider>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getSystemSettings, updateCaptchaSetting } from '@/api/settings'

// 设置表单数据
const settingsForm = reactive({
  requireCaptcha: false
})

const loading = ref(false)

// 页面加载时获取系统设置
onMounted(() => {
  fetchSettings()
})

// 获取系统设置
const fetchSettings = async () => {
  loading.value = true
  try {
    const res = await getSystemSettings()
    
    if (res.code === 200 && res.data) {
      settingsForm.requireCaptcha = res.data.requireCaptcha
    } else {
      ElMessage.error(res.message || '获取系统设置失败')
    }
  } catch (error) {
    console.error('获取系统设置错误:', error)
    ElMessage.error('获取系统设置失败，请稍后再试')
  } finally {
    loading.value = false
  }
}

// 处理验证码设置变更
const handleCaptchaChange = async () => {
  loading.value = true
  try {
    const res = await updateCaptchaSetting(settingsForm.requireCaptcha)
    
    if (res.code === 200) {
      const actionText = settingsForm.requireCaptcha ? '启用' : '关闭'
      ElMessage.success(`已${actionText}登录验证码`)
    } else {
      // 如果更新失败，还原开关状态
      settingsForm.requireCaptcha = !settingsForm.requireCaptcha
      ElMessage.error(res.message || '更新验证码设置失败')
    }
  } catch (error) {
    console.error('更新验证码设置错误:', error)
    // 如果发生错误，还原开关状态
    settingsForm.requireCaptcha = !settingsForm.requireCaptcha
    ElMessage.error('更新验证码设置失败，请稍后再试')
  } finally {
    loading.value = false
  }
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
</style>