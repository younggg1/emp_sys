import axios from 'axios'

// 获取本校学生列表
export const getStudents = async () => {
  try {
    const response = await axios.get('/admin/students')
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取学生列表失败' }
  }
}

// 查询本校就业信息
export const getEmploymentRecords = async (year) => {
  try {
    const response = await axios.get('/admin/employment', {
      params: { year }
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取就业信息失败' }
  }
}

// 查询本校反馈信息
export const getFeedbackRecords = async (year) => {
  try {
    const response = await axios.get('/admin/feedback', {
      params: { year }
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取反馈信息失败' }
  }
}

// 获取就业分布统计
export const getDistributionStatistics = async (type, year) => {
  try {
    const response = await axios.get('/admin/statistics/distribution', {
      params: { type, year }
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取就业分布统计失败' }
  }
}

// 获取就业趋势统计
export const getTrendStatistics = async (startYear, endYear) => {
  try {
    const response = await axios.get('/admin/statistics/trend', {
      params: { start_year: startYear, end_year: endYear }
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取就业趋势统计失败' }
  }
}

// 添加用户
export const addUser = async (userData) => {
  try {
    const response = await axios.post('/admin/user/add', userData)
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '添加用户失败' }
  }
}

// 重置密码
export const resetPassword = async (userId, newPassword) => {
  try {
    const response = await axios.post('/admin/user/reset-password', {
      user_id: userId,
      new_password: newPassword
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '重置密码失败' }
  }
}

// 删除用户
export const deleteUser = async (userId) => {
  try {
    const response = await axios.delete(`/admin/user/delete/${userId}`)
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '删除用户失败' }
  }
}

// 分配权限
export const assignPermission = async (userId, canEdit, canDelete) => {
  try {
    const response = await axios.post('/admin/permission/assign', {
      user_id: userId,
      can_edit: canEdit,
      can_delete: canDelete
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '分配权限失败' }
  }
}

// 更新系统设置
export const updateSettings = async (requireCaptcha, requireApproval) => {
  try {
    const response = await axios.post('/admin/settings/update', {
      require_captcha: requireCaptcha,
      require_approval: requireApproval
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '更新系统设置失败' }
  }
} 
