<template>
  <div class="login-form">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="角色" prop="role">
        <el-radio-group v-model="form.role">
          <el-radio label="student">学生</el-radio>
          <el-radio label="counselor">辅导员</el-radio>
          <el-radio label="admin">学校管理员</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item :label="form.role === 'student' ? '学号' : form.role === 'counselor' ? '工号' : '账号'" prop="username">
        <el-input style="margin-right: 10px;  height: 40px;" clearable v-model="form.username" :placeholder="form.role === 'student' ? '请输入学号' : form.role === 'counselor' ? '请输入工号' : '请输入账号'" />
      </el-form-item>
      
      <el-form-item label="密码" prop="password">
        <el-input style="margin-right: 10px; height: 40px;" clearable show-password v-model="form.password" type="password" placeholder="请输入密码" />
      </el-form-item>
      
      <el-form-item prop="captcha" v-if="captchaEnabled">
        <el-input
          v-model="form.captcha"
          placeholder="请输入验证码"
          style="margin-right: 10px; width: 50%; height: 40px;"
          clearable
        />
        <div style="width: 130px; height: 32px;background-color: aliceblue;">
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
import { setUser } from '@/utils/user'
import { login, mockLogin } from '@/api/auth'
import ValidCode from '@/components/ValidCode.vue'

const router = useRouter()

const formRef = ref(null)
const loginValidCode = ref(null) // 验证码组件的 ref
const validCodeValue = ref('') // 存储验证码的实际值
const captchaEnabled = ref(true) // 验证码开关，默认启用
const loading = ref(false)

const form = reactive({
  role: 'student',
  username: '',
  password: '',
  captcha: ''
})

const rules = reactive({
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
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
})

// 获取验证码组件生成的验证码值
const getCode = (value) => {
  validCodeValue.value = value;
};

// 处理登录
const handleLogin = async () => {
  if (!formRef.value) return
  
  // 手动检查验证码
  if (captchaEnabled.value) {
    if (!form.captcha) {
      ElMessage.error('请输入验证码')
      return
    }
    
    const userInput = form.captcha.toLowerCase()
    const currentCode = validCodeValue.value.toLowerCase()
      
    if (userInput !== currentCode) {
      ElMessage.error('验证码错误')
      loginValidCode.value.refreshCode()
      return
    }
  }
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    
    try {
      // 调用后端登录API
      const res = await login(form.username, form.password, form.role)
      
      if (res.code === 200) {
        // 使用用户管理工具保存用户信息
        setUser(res.data)
        
        ElMessage.success('登录成功')
        
        // 根据用户角色跳转到不同页面
        const redirectPath = `/${form.role}`
        router.push(redirectPath)
      } else {
        ElMessage.error(res.message || '登录失败')
        if (captchaEnabled.value) {
          loginValidCode.value.refreshCode()
        }
      }
    } catch (error) {
      ElMessage.error(error.message || '登录失败')
      console.error('登录错误详情:', error)
      if (captchaEnabled.value) {
        loginValidCode.value.refreshCode()
      }
    } finally {
      loading.value = false
    }
  })
}

onMounted(() => {
  // 页面加载时刷新验证码
  if (captchaEnabled.value && loginValidCode.value) {
    loginValidCode.value.refreshCode()
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
