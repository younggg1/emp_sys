import axios from 'axios'
import { ElMessage } from 'element-plus'
import { API_BASE_URL, REQUEST_TIMEOUT } from '@/config'

// 创建axios实例
const service = axios.create({
  baseURL: API_BASE_URL || 'http://localhost:8080', // 确保默认连接到后端
  timeout: REQUEST_TIMEOUT,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 在发送请求之前做些什么
    // 如果需要身份验证，可以通过其他方式获取
    return config
  },
  error => {
    // 对请求错误做些什么
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果请求的是二进制文件（例如导出功能）
    if (response.config.responseType === 'blob') {
      return response
    }
    
    // 判断响应状态 - 这里假设后端返回的数据格式为 {code: number, message: string, data: any}
    if (res.code !== 200) {
      ElMessage({
        message: res.message || '系统错误',
        type: 'error',
        duration: 5 * 1000
      })
      
      // 401: 未授权; 403: 权限不足
      if (res.code === 401 || res.code === 403) {
        // 可以在这里处理登出逻辑
        window.location.href = '/'
      }
      
      return Promise.reject(new Error(res.message || '系统错误'))
    } else {
      return res
    }
  },
  error => {
    console.error('响应错误:', error)
    
    // 处理网络错误
    let message = '网络错误，请稍后重试'
    if (error.response) {
      switch (error.response.status) {
        case 400:
          message = '请求错误'
          break
        case 401:
          message = '未授权，请重新登录'
          // 简化登出逻辑
          window.location.href = '/'
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求地址不存在'
          break
        case 500:
          message = '服务器内部错误'
          break
        default:
          message = `请求失败: ${error.response.status}`
      }
    }
    
    ElMessage({
      message: error.response?.data?.message || message,
      type: 'error',
      duration: 5 * 1000
    })
    
    return Promise.reject(error)
  }
)

// 处理模拟数据的函数
export function mockRequest(mockData) {
  return new Promise(resolve => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: 'success',
        data: mockData
      })
    }, 500)
  })
}

export default service 