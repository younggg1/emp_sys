<template>
  <div class="login-form">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="学校" prop="schoolId">
        <el-select v-model="form.schoolId" placeholder="请选择学校" style="width: 100%">
          <el-option
            v-for="item in schools"
            :key="item.school_id"
            :label="item.school_name"
            :value="item.school_id"
          />
        </el-select>
      </el-form-item>
      
      <el-form-item label="角色" prop="role">
        <el-radio-group v-model="form.role">
          <el-radio label="student">学生</el-radio>
          <el-radio label="counselor">辅导员</el-radio>
          <el-radio label="admin">管理员</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item :label="form.role === 'student' ? '学号' : form.role === 'counselor' ? '工号' : '账号'" prop="username">
        <el-input v-model="form.username" :placeholder="form.role === 'student' ? '请输入学号' : form.role === 'counselor' ? '请输入工号' : '请输入账号'" />
      </el-form-item>
      
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" type="password" placeholder="请输入密码" />
      </el-form-item>
      
      <el-form-item prop="captcha" v-if="captchaEnabled">
        <el-input
          v-model="form.captcha"
          placeholder="请输入验证码"
          style="margin-right: 10px; width: 50%; height: 50px;"
          clearable
          prefix-icon="InfoFilled"
        />
        <div style="width: 130px; height: 32px;background-color: aliceblue;" v-if="captchaEnabled">
          <valid-code ref="loginValidCode" @update:value="getCode" />
        </div>
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" :loading="loading" @click="handleLogin">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>


<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store'
import { getSchools, login, getSettings } from '@/api/auth'
import ValidCode from '@/components/ValidCode.vue'
import axios from 'axios'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref(null)
const loginValidCode = ref(null) // 验证码组件的 ref
const validCodeValue = ref('') // 存储验证码的实际值
const captchaEnabled = ref(false) // 验证码开关
const loading = ref(false)
const schools = ref([])
const settings = ref({
  require_captcha: false,
  require_approval: true
})

const form = reactive({
  schoolId: '',
  role: 'student',
  username: '',
  password: '',
  captcha: ''
})

const rules = reactive({
  schoolId: [
    { required: true, message: '请选择学校', trigger: 'change' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ],
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    {
      validator: (rule, value) => value.toLowerCase() === validCodeValue.value.toLowerCase(),
      message: '验证码错误',
      trigger: 'blur',
    },
  ]
})

// 获取验证码组件生成的验证码值
const getCode = (value) => {
  validCodeValue.value = value;
};

// 获取学校列表和系统设置
const fetchData = async () => {
  try {
    // 获取学校列表
    const schoolsRes = await getSchools()
    if (schoolsRes.code === 200) {
      schools.value = schoolsRes.data
    }
    
    // 获取系统设置
    const settingsRes = await getSettings()
    if (settingsRes.code === 200) {
      settings.value = settingsRes.data
      captchaEnabled.value = settings.value.require_captcha
    }
  } catch (error) {
    ElMessage.error(error.message || '加载数据失败')
  }
}

// 处理登录
const handleLogin = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    // 如果开启了验证码，先验证验证码
    if (captchaEnabled.value) {
      const userInput = form.captcha.toLowerCase()
      const currentCode = validCodeValue.value.toLowerCase()
      
      if (userInput !== currentCode) {
        ElMessage.error('验证码错误')
        loginValidCode.value.refreshCode()
        return
      }
    }
    
    loading.value = true
    
    try {
      const res = await login(form.schoolId, form.username, form.password)
      
      if (res.code === 200) {
        userStore.setUser(res.data)
        
        ElMessage.success('登录成功')
        
        // 根据用户角色跳转到不同页面
        const redirectPath = `/${form.role}`
        router.push(redirectPath)
      }
    } catch (error) {
      ElMessage.error(error.message || '登录失败')
      if (captchaEnabled.value) {
        loginValidCode.value.refreshCode()
      }
    } finally {
      loading.value = false
    }
  })
}

// 组件挂载时检查验证码设置
onMounted(async () => {
  fetchData()
  
  // 单独检查验证码设置（如果需要）
  try {
    const res = await axios.get('/settings')
    captchaEnabled.value = res.data
    console.log('验证码是否启用:', captchaEnabled.value)
  } catch (error) {
    console.error('获取验证码设置失败', error)
  }
})
</script>

<style scoped>
.login-form {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px;
}
</style> 
