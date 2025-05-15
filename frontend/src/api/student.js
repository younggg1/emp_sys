import axios from 'axios'

// 获取学生信息
export const getStudentInfo = async () => {
  try {
    const response = await axios.get('/student/info')
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取学生信息失败' }
  }
}

// 登记就业信息
export const registerEmployment = async (employmentData) => {
  try {
    const response = await axios.post('/employment/register', employmentData)
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '就业信息登记失败' }
  }
}

// 提交就业反馈
export const submitFeedback = async (feedbackData) => {
  try {
    const response = await axios.post('/feedback/submit', feedbackData)
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '反馈提交失败' }
  }
}

// 获取就业信息
export const getEmploymentRecords = async () => {
  try {
    const response = await axios.get('/student/employment')
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取就业信息失败' }
  }
}

// 获取反馈记录
export const getFeedbackRecords = async () => {
  try {
    const response = await axios.get('/student/feedback')
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '获取反馈记录失败' }
  }
}

// 更新就业信息
export const updateEmployment = async (recordId, employmentData) => {
  try {
    const response = await axios.put(`/employment/update/${recordId}`, employmentData)
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '更新就业信息失败' }
  }
}

// 更新反馈
export const updateFeedback = async (feedbackId, feedbackData) => {
  try {
    const response = await axios.put(`/feedback/update/${feedbackId}`, feedbackData)
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '更新反馈失败' }
  }
}

// 删除就业信息
export const deleteEmployment = async (recordId) => {
  try {
    const response = await axios.delete(`/employment/delete/${recordId}`)
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '删除就业信息失败' }
  }
}

// 删除反馈
export const deleteFeedback = async (feedbackId) => {
  try {
    const response = await axios.delete(`/feedback/delete/${feedbackId}`)
    return response.data
  } catch (error) {
    throw error.response?.data || { message: '删除反馈失败' }
  }
} 
