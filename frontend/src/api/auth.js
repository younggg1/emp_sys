import axios from 'axios'

export const login = async (schoolId, username, password) => {
  try {
    const response = await axios.post('/login', {
      schoolId,
      username,
      password
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '登录失败，请稍后再试' }
  }
}

export const getSettings = async () => {
  try {
    const response = await axios.get('/settings')
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取设置失败' }
  }
}
export const getCaptcha = async (schoolId) => {
  try {
    // 拼接 URL 参数
    const response = await axios.get(`/captcha?schoolId=${schoolId}`)
    return response.data
  } catch (error) {
    console.error('获取验证码设置失败:', error)
    throw error.response?.data || { message: '获取验证码设置失败' }
  }
}
export const getSchools = async () => {
  try {
    const response = await axios.get('/schools')
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取学校列表失败' }
  }
} 
