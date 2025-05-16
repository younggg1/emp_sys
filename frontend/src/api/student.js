import axios from 'axios'
import request from '@/utils/request'

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
