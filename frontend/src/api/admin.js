import request, { mockRequest } from '@/utils/request'

// 获取本校学生列表
export function getStudents() {
  return request({
    url: '/api/admin/students',
    method: 'get'
  })
}

// 查询本校就业信息
export const getEmploymentRecordsAsync = async (year) => {
  try {
    const response = await request({
      url: '/admin/employment',
      method: 'get',
      params: { year }
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取就业信息失败' }
  }
}



// 获取就业分布统计
export const getDistributionStatisticsAsync = async (type, year) => {
  try {
    const response = await request({
      url: '/admin/statistics/distribution',
      method: 'get',
      params: { type, year }
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取就业分布统计失败' }
  }
}

// 获取就业趋势统计
export const getTrendStatisticsAsync = async (startYear, endYear) => {
  try {
    const response = await request({
      url: '/admin/statistics/trend',
      method: 'get',
      params: { start_year: startYear, end_year: endYear }
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取就业趋势统计失败' }
  }
}

// 获取就业信息列表
export function getEmploymentRecords(params) {
  return request({
    url: '/api/admin/employment',
    method: 'get',
    params
  })
}





// 删除就业信息
export function deleteEmployment(id) {
  return request({
    url: `/api/admin/employment/${id}`,
    method: 'delete'
  })
}

// 获取反馈列表
export function getFeedbackRecords(params) {
  return request({
    url: '/api/admin/feedback',
    method: 'get',
    params
  })
}

// 审核反馈
export function auditFeedback(id) {
  return request({
    url: `/api/admin/feedback/${id}/audit`,
    method: 'post'
  })
}

// 删除反馈
export function deleteFeedback(id) {
  return request({
    url: `/api/admin/feedback/${id}`,
    method: 'delete'
  })
}

// 获取分布统计数据
export function getDistributionStatistics(dimension, year) {
  return request({
    url: '/api/admin/statistics/distribution',
    method: 'get',
    params: {
      dimension,
      year
    }
  })
}

// 获取趋势统计数据
export function getTrendStatistics(startYear, endYear) {
  return request({
    url: '/api/admin/statistics/trend',
    method: 'get',
    params: {
      startYear,
      endYear
    }
  })
}

// 获取历年就业数据
export function getHistoryData() {
  return request({
    url: '/api/admin/history',
    method: 'get'
  })
}

// 更新历年就业数据
export function updateHistoryData(id, data) {
  return request({
    url: `/api/admin/history/${id}`,
    method: 'put',
    data
  })
}

// 删除历年就业数据
export function deleteHistoryData(id) {
  return request({
    url: `/api/admin/history/${id}`,
    method: 'delete'
  })
}



// 获取用户列表
export function getUserList(params) {
  return request({
    url: '/api/admin/getUsersList',
    method: 'get',
    params
  })
}

// 添加用户
export function addUser(data) {
  return request({
    url: '/api/admin/users',
    method: 'post',
    data
  })
}

// 更新用户
export function updateUser(id, data) {
  return request({
    url: `/api/admin/users/${id}`,
    method: 'put',
    data
  })
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: `/api/admin/users/${id}`,
    method: 'delete'
  })
}

// 重置用户密码
export function resetUserPassword(id, newPassword) {
  return request({
    url: `/api/admin/users/${id}/reset-password`,
    method: 'post',
    data: {
      new_password: newPassword
    }
  })
}

// 获取系统设置
export function getSystemSettings() {
  return request({
    url: '/api/admin/settings',
    method: 'get'
  })
}

// 更新系统设置
export function updateSystemSettings(data) {
  return request({
    url: '/api/admin/settings',
    method: 'put',
    data
  })
}

// 分配权限
export const assignPermission = async (userId, canEdit, canDelete) => {
  try {
    const response = await request({
      url: '/admin/permission/assign',
      method: 'post',
      data: {
        user_id: userId,
        can_edit: canEdit,
        can_delete: canDelete
      }
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '分配权限失败' }
  }
}

// 更新系统设置
export const updateSettings = async (requireCaptcha, requireApproval) => {
  try {
    const response = await request({
      url: '/admin/settings/update',
      method: 'post',
      data: {
        require_captcha: requireCaptcha,
        require_approval: requireApproval
      }
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '更新系统设置失败' }
  }
}



// 获取企业性质分布统计
export function getCompanyNatureStats(year) {
  return request({
    url: '/api/admin/statistics/company-nature',
    method: 'get',
    params: { year }
  })
}

// 获取薪资分布统计
export function getSalaryStats(year) {
  return request({
    url: '/api/admin/statistics/salary',
    method: 'get',
    params: { year }
  })
}

// 获取地区分布统计
export function getRegionStats(year) {
  return request({
    url: '/api/admin/statistics/region',
    method: 'get',
    params: { year }
  })
} 

// 添加辅导员
export function addCounselor(data) {
  return request({
    url: '/api/admin/counselor',
    method: 'post',
    data: {
      username: data.username,
      password: data.password
    },
    params: {
      name: data.name
    }
  })
}

// 获取辅导员列表
export function getCounselorList() {
  return request({
    url: '/api/admin/getCounselors',
    method: 'get'
  })
}

// 添加学生
export function addStudent(data) {
  return request({
    url: '/api/admin/addStudent',
    method: 'post',
    data
  })
}