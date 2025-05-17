import request from '@/utils/request'



// 获取辅导员班级的学生列表
export function getStudents(params) {
  return request({
    url: '/api/counselor/students',
    method: 'get',
    params
  })
}

// 获取就业信息列表
export function getEmploymentRecords(params) {
  return request({
    url: '/api/counselor/employment',
    method: 'get',
    params
  })
}

// 编辑就业信息
export function editEmployment(id, data) {
  return request({
    url: `/api/counselor/employment/${id}`,
    method: 'put',
    data
  })
}

// 删除就业信息
export function deleteEmployment(id, params) {
  return request({
    url: `/api/counselor/employment/${id}`,
    method: 'delete',
    params
  })
}

// 获取反馈列表
export function getFeedbackRecords(params) {
  return request({
    url: '/api/counselor/feedback',
    method: 'get',
    params
  })
}

// 删除反馈
export function deleteFeedback(id, params) {
  return request({
    url: `/api/counselor/feedback/${id}`,
    method: 'delete',
    params
  })
}

// 审核反馈
export function approveFeedback(id, data) {
  return request({
    url: `/api/counselor/feedback/approve/${id}`,
    method: 'put',
    params: data
  })
}

// 获取反馈详情
export function getFeedbackDetail(id, params) {
  return request({
    url: `/api/counselor/feedback/detail/${id}`,
    method: 'get',
    params
  })
}

// 获取分布统计数据
export function getDistributionStatistics(dimension, year) {
  return request({
    url: '/api/counselor/statistics/distribution',
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
    url: '/api/counselor/statistics/trend',
    method: 'get',
    params: {
      startYear,
      endYear
    }
  })
}

// 审核就业信息
export function approveEmployment(id, data) {
  return request({
    url: `/api/counselor/employment/approve/${id}`,
    method: 'put',
    params: data
  })
}

// 获取辅导员列表（包含权限信息）
export function getCounselorListWithUserInfo() {
  return request({
    url: '/api/counselor/listWithUserInfo',
    method: 'get'
  })
}