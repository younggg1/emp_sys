import request from '@/utils/request'

// 获取辅导员班级的学生列表
export function getStudents() {
  return request({
    url: '/api/counselor/students',
    method: 'get'
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
export function deleteEmployment(id) {
  return request({
    url: `/api/counselor/employment/${id}`,
    method: 'delete'
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
export function deleteFeedback(id) {
  return request({
    url: `/api/counselor/feedback/${id}`,
    method: 'delete'
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

// 模拟接口 - 获取学生列表
export function mockGetStudents() {
  return {
    code: 200,
    message: '获取成功',
    data: [
      { 
        student_id: '202001001', 
        name: '张三', 
        gender: '男', 
        class_name: '计算机2001班', 
        college: '计算机学院', 
        major: '计算机科学与技术', 
        employment_status: 'employed', 
        phone: '13800138001', 
        email: 'zhangsan@example.com' 
      },
      { 
        student_id: '202001002', 
        name: '李四', 
        gender: '女', 
        class_name: '计算机2001班', 
        college: '计算机学院', 
        major: '计算机科学与技术', 
        employment_status: 'unemployed', 
        phone: '13800138002', 
        email: 'lisi@example.com' 
      },
      { 
        student_id: '202001003', 
        name: '王五', 
        gender: '男', 
        class_name: '软件2001班', 
        college: '计算机学院', 
        major: '软件工程', 
        employment_status: 'employed', 
        phone: '13800138003', 
        email: 'wangwu@example.com' 
      },
      { 
        student_id: '202001004', 
        name: '赵六', 
        gender: '女', 
        class_name: '软件2001班', 
        college: '计算机学院', 
        major: '软件工程', 
        employment_status: 'employed', 
        phone: '13800138004', 
        email: 'zhaoliu@example.com' 
      },
      { 
        student_id: '202001005', 
        name: '孙七', 
        gender: '男', 
        class_name: '人工智能2001班', 
        college: '计算机学院', 
        major: '人工智能', 
        employment_status: 'unemployed', 
        phone: '13800138005', 
        email: 'sunqi@example.com' 
      }
    ]
  }
}

// 模拟接口 - 获取就业记录
export function mockGetEmploymentRecords() {
  return {
    code: 200,
    message: '获取成功',
    data: [
      {
        record_id: 1,
        student_id: '202001001',
        name: '张三',
        major: '计算机科学与技术',
        major_category: 'science',
        company_nature: '互联网',
        company: '阿里巴巴',
        position: '前端开发工程师',
        salary: 15000,
        entry_date: '2023-07-01',
        region: '杭州',
        status: 'approved'
      },
      {
        record_id: 2,
        student_id: '202001003',
        name: '王五',
        major: '软件工程',
        major_category: 'science',
        company_nature: '互联网',
        company: '腾讯',
        position: '后端开发工程师',
        salary: 16000,
        entry_date: '2023-07-15',
        region: '深圳',
        status: 'approved'
      },
      {
        record_id: 3,
        student_id: '202001004',
        name: '赵六',
        major: '软件工程',
        major_category: 'science',
        company_nature: '互联网',
        company: '百度',
        position: '算法工程师',
        salary: 18000,
        entry_date: '2023-08-01',
        region: '北京',
        status: 'pending'
      }
    ]
  }
}

// 模拟接口 - 获取反馈记录
export function mockGetFeedbackRecords() {
  return {
    code: 200,
    message: '获取成功',
    data: [
      {
        feedback_id: 1,
        student_id: '202001001',
        student_name: '张三',
        company: '阿里巴巴',
        submit_date: '2023-09-01',
        company_rating: 4,
        salary_rating: 4,
        job_rating: 5,
        major_match: 'perfect',
        content: '工作氛围很好，技术氛围浓厚，待遇不错，很满意目前的工作。',
        status: 'approved'
      },
      {
        feedback_id: 2,
        student_id: '202001003',
        student_name: '王五',
        company: '腾讯',
        submit_date: '2023-09-05',
        company_rating: 5,
        salary_rating: 5,
        job_rating: 4,
        major_match: 'good',
        content: '公司福利好，薪资满意，但是加班比较多。总体来说还是很满意的。',
        status: 'approved'
      },
      {
        feedback_id: 3,
        student_id: '202001004',
        student_name: '赵六',
        company: '百度',
        submit_date: '2023-09-10',
        company_rating: 3,
        salary_rating: 4,
        job_rating: 3,
        major_match: 'partial',
        content: '工作内容与所学专业相关性一般，但薪资还不错，公司环境也挺好的。',
        status: 'pending'
      }
    ]
  }
} 
