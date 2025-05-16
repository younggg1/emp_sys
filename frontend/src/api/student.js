import axios from 'axios'
import request from '@/utils/request'

// 模拟学生信息数据
export const mockStudentInfo = () => {
  return {
    studentId: '20230001',
    name: '张三',
    className: '计算机科学与技术2班',
    college: '信息科学与工程学院',
    major: '计算机科学与技术',
    counselorName: '李老师',
    employment_status: 'employed'
  }
}

// 模拟就业信息列表
export const mockEmploymentRecords = () => {
  return [
    {
      id: '1',
      name: '张三',
      major: '计算机科学与技术',
      major_category: 'science',
      company_nature: '互联网',
      company: '腾讯科技',
      position: '软件工程师',
      salary: 15000,
      entry_date: '2023-07-01',
      region: '深圳',
      status: 'approved'
    },
    {
      id: '2',
      name: '张三',
      major: '计算机科学与技术',
      major_category: 'science',
      company_nature: '互联网',
      company: '阿里巴巴',
      position: '前端开发',
      salary: 16000,
      entry_date: '2023-08-15',
      region: '杭州',
      status: 'pending'
    }
  ]
}

// 模拟反馈列表
export const mockFeedbackRecords = () => {
  return [
    {
      feedback_id: '1',
      submit_date: '2023-08-01',
      company: '腾讯科技',
      company_rating: 5,
      salary_rating: 4,
      job_rating: 5,
      major_match: 'perfect',
      content: '工作环境很好，团队氛围融洽，技术氛围浓厚。',
      status: 'approved'
    },
    {
      feedback_id: '2',
      submit_date: '2023-09-15',
      company: '阿里巴巴',
      company_rating: 4,
      salary_rating: 5,
      job_rating: 4,
      major_match: 'good',
      content: '工作强度较大，但能学到很多东西，职业发展前景好。',
      status: 'pending'
    }
  ]
}

/**
 * 获取学生个人信息
 * @param userId 用户ID
 * @returns {Promise<*>}
 */
export function getStudentInfo(userId) {
  return request({
    url: '/api/student/info',
    method: 'get',
    params: { userId }
  })
}

/**
 * 获取就业信息列表
 * @param {number} studentId 学生ID
 * @returns {Promise<*>}
 */
export function getEmploymentRecords(studentId) {
  return request({
    url: '/api/employment/list',
    method: 'get',
    params: { studentId }
  })
}

/**
 * 分页获取就业信息列表
 * @param {number} studentId 学生ID
 * @param {number} current 当前页码
 * @param {number} size 每页大小
 * @returns {Promise<*>}
 */
export function getEmploymentRecordsPage(studentId, current = 1, size = 10) {
  return request({
    url: '/api/employment/page',
    method: 'get',
    params: { studentId, current, size }
  })
}

/**
 * 获取就业信息详情
 * @param {number} recordId 记录ID
 * @returns {Promise<*>}
 */
export function getEmploymentRecordDetail(recordId) {
  return request({
    url: `/api/employment/detail/${recordId}`,
    method: 'get'
  })
}

/**
 * 添加就业信息
 * @param {Object} record 就业信息记录
 * @returns {Promise<*>}
 */
export function addEmploymentRecord(record) {
  return request({
    url: '/api/employment/add',
    method: 'post',
    data: record
  })
}

/**
 * 更新就业信息
 * @param {Object} record 就业信息记录
 * @returns {Promise<*>}
 */
export function updateEmploymentRecord(record) {
  return request({
    url: '/api/employment/update',
    method: 'put',
    data: record
  })
}

/**
 * 删除就业信息
 * @param {number} recordId 记录ID
 * @param {number} studentId 学生ID
 * @returns {Promise<*>}
 */
export function deleteEmploymentRecord(recordId, studentId) {
  return request({
    url: `/api/employment/delete/${recordId}`,
    method: 'delete',
    params: { studentId }
  })
}

// 获取反馈记录
export const mockGetFeedbackRecords = async (studentId) => {
  try {
    // 模拟API调用
    return {
      code: 200,
      data: mockFeedbackRecords()
    }

    // 实际API调用
    // const response = await axios.get(`/student/feedback?studentId=${studentId}`)
    // return response.data
  } catch (error) {
    console.error('获取反馈记录失败:', error)
    throw error.response?.data || { message: '获取反馈记录失败' }
  }
}

// 登记就业信息
export const registerEmployment = async (studentId, employmentData) => {
  try {
    // 模拟API调用
    return {
      code: 200,
      message: '就业信息登记成功',
      data: {
        ...employmentData,
        id: Date.now().toString(),
        status: 'pending'
      }
    }

    // 实际API调用
    // const response = await axios.post(`/employment/register?studentId=${studentId}`, employmentData)
    // return response.data
  } catch (error) {
    console.error('就业信息登记失败:', error)
    throw error.response?.data || { message: '就业信息登记失败' }
  }
}

// 提交就业反馈
export const submitFeedback = async (studentId, feedbackData) => {
  try {
    // 模拟API调用
    return {
      code: 200,
      message: '反馈提交成功',
      data: {
        ...feedbackData,
        feedback_id: Date.now().toString(),
        created_at: new Date(),
        updated_at: new Date(),
        status: 'pending'
      }
    }

    // 实际API调用
    // const response = await axios.post(`/feedback/submit?studentId=${studentId}`, feedbackData)
    // return response.data
  } catch (error) {
    console.error('反馈提交失败:', error)
    throw error.response?.data || { message: '反馈提交失败' }
  }
}

// 更新就业信息
export const updateEmployment = async (studentId, recordId, employmentData) => {
  try {
    // 模拟API调用
    return {
      code: 200,
      message: '就业信息更新成功',
      data: {
        ...employmentData,
        id: recordId,
        status: 'pending'
      }
    }

    // 实际API调用
    // const response = await axios.put(`/employment/update/${recordId}?studentId=${studentId}`, employmentData)
    // return response.data
  } catch (error) {
    console.error('更新就业信息失败:', error)
    throw error.response?.data || { message: '更新就业信息失败' }
  }
}

// 更新反馈
export const updateFeedback = async (studentId, feedbackId, feedbackData) => {
  try {
    // 模拟API调用
    return {
      code: 200,
      message: '反馈更新成功',
      data: {
        ...feedbackData,
        feedback_id: feedbackId,
        status: 'pending'
      }
    }

    // 实际API调用
    // const response = await axios.put(`/feedback/update/${feedbackId}?studentId=${studentId}`, feedbackData)
    // return response.data
  } catch (error) {
    console.error('更新反馈失败:', error)
    throw error.response?.data || { message: '更新反馈失败' }
  }
}

// 删除就业信息
export const deleteEmployment = async (studentId, recordId) => {
  try {
    // 模拟API调用
    return {
      code: 200,
      message: '就业信息删除成功'
    }

    // 实际API调用
    // const response = await axios.delete(`/employment/delete/${recordId}?studentId=${studentId}`)
    // return response.data
  } catch (error) {
    console.error('删除就业信息失败:', error)
    throw error.response?.data || { message: '删除就业信息失败' }
  }
}

// 删除反馈
export const deleteFeedback = async (studentId, feedbackId) => {
  try {
    // 模拟API调用
    return {
      code: 200,
      message: '反馈删除成功'
    }

    // 实际API调用
    // const response = await axios.delete(`/feedback/delete/${feedbackId}?studentId=${studentId}`)
    // return response.data
  } catch (error) {
    console.error('删除反馈失败:', error)
    throw error.response?.data || { message: '删除反馈失败' }
  }
}

/**
 * 获取反馈列表
 * @param {number} studentId 学生ID
 * @returns {Promise<*>}
 */
export function getFeedbackRecords(studentId) {
  return request({
    url: '/api/feedback/list',
    method: 'get',
    params: { studentId }
  })
}

/**
 * 分页获取反馈列表
 * @param {number} studentId 学生ID
 * @param {number} current 当前页码
 * @param {number} size 每页大小
 * @returns {Promise<*>}
 */
export function getFeedbackRecordsPage(studentId, current = 1, size = 10) {
  return request({
    url: '/api/feedback/page',
    method: 'get',
    params: { studentId, current, size }
  })
}

/**
 * 获取反馈详情
 * @param {number} feedbackId 反馈ID
 * @returns {Promise<*>}
 */
export function getFeedbackRecordDetail(feedbackId) {
  return request({
    url: `/api/feedback/detail/${feedbackId}`,
    method: 'get'
  })
}

/**
 * 添加反馈
 * @param {Object} record 反馈记录
 * @returns {Promise<*>}
 */
export function addFeedbackRecord(record) {
  return request({
    url: '/api/feedback/add',
    method: 'post',
    data: record
  })
}

/**
 * 更新反馈
 * @param {Object} record 反馈记录
 * @returns {Promise<*>}
 */
export function updateFeedbackRecord(record) {
  return request({
    url: '/api/feedback/update',
    method: 'put',
    data: record
  })
}

/**
 * 删除反馈
 * @param {number} feedbackId 反馈ID
 * @param {number} studentId 学生ID
 * @returns {Promise<*>}
 */
export function deleteFeedbackRecord(feedbackId, studentId) {
  return request({
    url: `/api/feedback/delete/${feedbackId}`,
    method: 'delete',
    params: { studentId }
  })
} 
