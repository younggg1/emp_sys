<template>
  <div class="login-form">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="学校" prop="schoolId" >
        <el-select 
          v-model="form.schoolId" 
          placeholder="请选择学校" 
          style="width: 100%"
          :teleported="false"
          :popper-class="'custom-select-dropdown'"
          :popper-options="{
            modifiers: [
              {
                name: 'offset',
                options: {
                  offset: [0, 8],
                },
              },
            ],
          }"
        >
          <el-option
            v-for="item in schools"
            :key="item.schoolId"
            :label="item.schoolName"
            :value="item.schoolId"
          />
        </el-select>
      </el-form-item>
      
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
        <!-- 不要的图标prefix-icon="InfoFilled" -->
        <el-input
          v-model="form.captcha"
          placeholder="请输入验证码"
          style="margin-right: 10px; width: 50%; height: 40px;"
          clearable
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
import { ref, reactive, onMounted,watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store'
import { getSchools, login, getSettings ,getCaptcha} from '@/api/auth'
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
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
})

// 获取验证码组件生成的验证码值
const getCode = (value) => {
  validCodeValue.value = value;
};

// 获取学校列表
const fetchData = async () => {
  try {
    // 获取学校列表
    const schoolsRes = await getSchools()
    if (schoolsRes.code === 200) {
      schools.value = schoolsRes.data
    } else {
      ElMessage.error(schoolsRes.message || '加载学校列表失败')
    }
  } catch (error) {
    ElMessage.error(error.message || '加载数据失败')
  }
}

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
      console.log('发送登录请求:', {
    schoolId: form.schoolId,
    username: form.username,
    password: form.password
  })
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
      console.error('登录错误详情:', error)
      if (captchaEnabled.value) {
        loginValidCode.value.refreshCode()
      }
    } finally {
      loading.value = false
    }
  })
}

onMounted(async () => {
  fetchData()

})
watch(() => form.schoolId, async (newSchoolId) => {
  if (newSchoolId) {
    try {
      const captchaRes = await getCaptcha(newSchoolId)
      if (captchaRes.code === 200) {
        captchaEnabled.value = captchaRes.data
      }
    } catch (error) {
      ElMessage.error(error.message || '加载验证码设置失败')
    }
  }
})
</script>

<style scoped>
.login-form {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px;
}

/* 添加下拉菜单的样式 */
:deep(.custom-select-dropdown .el-select-dropdown__wrap) {
  max-height: 120px !important; /* 强制设置较小的高度以确保出现滚动条 */
}

:deep(.custom-select-dropdown .el-select-dropdown__list) {
  padding: 0;
}

:deep(.custom-select-dropdown .el-select-dropdown__item) {
  padding: 8px 16px;
  height: 40px; /* 固定每个选项的高度 */
  line-height: 24px;
}
</style> 
