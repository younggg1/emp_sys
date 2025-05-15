import axios from 'axios'

// 获取学生信息
export const getStudentInfo = async (studentId) => {
  try {
    const response = await axios.get(`/student/info?studentId=${studentId}`)
    return response.data
  } catch (error) {
    console.error('获取学生信息失败:', error)
    throw error.response?.data || { message: '获取学生信息失败' }
  }
}

// 登记就业信息
export const registerEmployment = async (studentId, employmentData) => {
  try {
    const response = await axios.post(`/employment/register?studentId=${studentId}`, employmentData)
    return response.data
  } catch (error) {
    console.error('就业信息登记失败:', error)
    throw error.response?.data || { message: '就业信息登记失败' }
  }
}

// 提交就业反馈
export const submitFeedback = async (studentId, feedbackData) => {
  try {
    const response = await axios.post(`/feedback/submit?studentId=${studentId}`, feedbackData)
    return response.data
  } catch (error) {
    console.error('反馈提交失败:', error)
    throw error.response?.data || { message: '反馈提交失败' }
  }
}

// 获取就业信息
export const getEmploymentRecords = async (studentId) => {
  try {
    const response = await axios.get(`/student/employment?studentId=${studentId}`)
    return response.data
  } catch (error) {
    console.error('获取就业信息失败:', error)
    throw error.response?.data || { message: '获取就业信息失败' }
  }
}

// 获取反馈记录
export const getFeedbackRecords = async (studentId) => {
  try {
    const response = await axios.get(`/student/feedback?studentId=${studentId}`)
    return response.data
  } catch (error) {
    console.error('获取反馈记录失败:', error)
    throw error.response?.data || { message: '获取反馈记录失败' }
  }
}

// 更新就业信息
export const updateEmployment = async (studentId, recordId, employmentData) => {
  try {
    const response = await axios.put(`/employment/update/${recordId}?studentId=${studentId}`, employmentData)
    return response.data
  } catch (error) {
    console.error('更新就业信息失败:', error)
    throw error.response?.data || { message: '更新就业信息失败' }
  }
}

// 更新反馈
export const updateFeedback = async (studentId, feedbackId, feedbackData) => {
  try {
    const response = await axios.put(`/feedback/update/${feedbackId}?studentId=${studentId}`, feedbackData)
    return response.data
  } catch (error) {
    console.error('更新反馈失败:', error)
    throw error.response?.data || { message: '更新反馈失败' }
  }
}

// 删除就业信息
export const deleteEmployment = async (studentId, recordId) => {
  try {
    const response = await axios.delete(`/employment/delete/${recordId}?studentId=${studentId}`)
    return response.data
  } catch (error) {
    console.error('删除就业信息失败:', error)
    throw error.response?.data || { message: '删除就业信息失败' }
  }
}

// 删除反馈
export const deleteFeedback = async (studentId, feedbackId) => {
  try {
    const response = await axios.delete(`/feedback/delete/${feedbackId}?studentId=${studentId}`)
    return response.data
  } catch (error) {
    console.error('删除反馈失败:', error)
    throw error.response?.data || { message: '删除反馈失败' }
  }
} 
