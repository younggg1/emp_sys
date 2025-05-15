import axios from 'axios'

// 获取本班学生列表
export const getStudents = async () => {
  try {
    const response = await axios.get('/counselor/students')
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取学生列表失败' }
  }
}

// 获取本班就业信息
export const getEmploymentRecords = async () => {
  try {
    const response = await axios.get('/counselor/employment')
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取就业信息失败' }
  }
}

// 获取本班反馈信息
export const getFeedbackRecords = async () => {
  try {
    const response = await axios.get('/counselor/feedback')
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取反馈信息失败' }
  }
}

// 审核就业信息
export const auditEmployment = async (recordId) => {
  try {
    const response = await axios.post(`/counselor/employment/audit/${recordId}`, {
      status: 'approved'
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '审核失败' }
  }
}

// 审核反馈信息
export const auditFeedback = async (feedbackId) => {
  try {
    const response = await axios.post(`/counselor/feedback/audit/${feedbackId}`, {
      status: 'approved'
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '审核失败' }
  }
}

// 编辑就业信息（需要权限）
export const editEmployment = async (recordId, employmentData) => {
  try {
    const response = await axios.put(`/counselor/employment/edit/${recordId}`, employmentData)
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '编辑失败' }
  }
}

// 删除就业信息（需要权限）
export const deleteEmployment = async (recordId) => {
  try {
    const response = await axios.delete(`/counselor/employment/delete/${recordId}`)
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '删除失败' }
  }
}

// 删除反馈信息（需要权限）
export const deleteFeedback = async (feedbackId) => {
  try {
    const response = await axios.delete(`/counselor/feedback/delete/${feedbackId}`)
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '删除失败' }
  }
}

// 获取历年就业信息
export const getHistoryEmployment = async (year) => {
  try {
    const response = await axios.get('/counselor/statistics/history', {
      params: { year }
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取历年就业信息失败' }
  }
}

// 获取就业分布统计
export const getDistributionStatistics = async (type, year) => {
  try {
    const response = await axios.get('/counselor/statistics/distribution', {
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
    const response = await axios.get('/counselor/statistics/trend', {
      params: { start_year: startYear, end_year: endYear }
    })
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取就业趋势统计失败' }
  }
} 
