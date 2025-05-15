import axios from 'axios'

export const login = async (schoolId, username, password) => {
  try {
    const response = await axios.post('/login', {
      school_id: schoolId,
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

export const getSchools = async () => {
  try {
    const response = await axios.get('/schools')
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取学校列表失败' }
  }
} 
