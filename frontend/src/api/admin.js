import request, { mockRequest } from '@/utils/request'
import { USE_MOCK } from '@/config'

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

// 查询本校反馈信息
export const getFeedbackRecordsAsync = async (year) => {
  try {
    const response = await request({
      url: '/admin/feedback',
      method: 'get',
      params: { year }
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取反馈信息失败' }
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

// 审核就业信息
export function auditEmployment(id) {
  return request({
    url: `/api/admin/employment/${id}/audit`,
    method: 'post'
  })
}

// 编辑就业信息
export function editEmployment(id, data) {
  return request({
    url: `/api/admin/employment/${id}`,
    method: 'put',
    data
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

// 添加历年就业数据记录
export function addHistoryRecord(data) {
  return request({
    url: '/api/admin/history',
    method: 'post',
    data
  })
}

// 更新历年就业数据记录
export function updateHistoryRecord(id, data) {
  return request({
    url: `/api/admin/history/${id}`,
    method: 'put',
    data
  })
}

// 删除历年就业数据记录
export function deleteHistoryRecord(id) {
  return request({
    url: `/api/admin/history/${id}`,
    method: 'delete'
  })
}

// 导出历年就业数据
export function exportHistoryData() {
  return request({
    url: '/api/admin/history/export',
    method: 'get',
    responseType: 'blob'
  })
}

// 获取用户列表
export function getUserList(params) {
  return request({
    url: '/api/admin/users',
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

// 模拟接口 - 获取历年就业数据
export function mockGetHistoryData() {
  // 根据环境变量配置决定是否使用模拟数据
  if (USE_MOCK) {
    return mockRequest([
      { id: 1, year: 2023, total_students: 1200, employed_students: 1080, employment_rate: 90, avg_salary: 8500 },
      { id: 2, year: 2022, total_students: 1150, employed_students: 1012, employment_rate: 88, avg_salary: 8200 },
      { id: 3, year: 2021, total_students: 1100, employed_students: 968, employment_rate: 88, avg_salary: 7800 },
      { id: 4, year: 2020, total_students: 1050, employed_students: 924, employment_rate: 88, avg_salary: 7500 },
      { id: 5, year: 2019, total_students: 1000, employed_students: 920, employment_rate: 92, avg_salary: 7200 },
    ])
  } else {
    // 不使用模拟数据时，调用真实API
    return request({
      url: '/api/admin/history',
      method: 'get'
    })
  }
} 
