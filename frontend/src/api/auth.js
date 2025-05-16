import axios from 'axios'
import request from '@/utils/request'

/**
 * 登录API - 调用后端接口
 * @param username 用户名
 * @param password 密码
 * @param role 角色
 * @returns {Promise<*>}
 */
export const login = async (username, password, role) => {
  try {
    const response = await request({
      url: '/api/login',
      method: 'post',
      data: {
        username,
        password,
        role
      }
    })
    return response
  } catch (error) {
    throw error.message || '登录失败，请稍后再试'
  }
}

// 模拟登录功能，在后端接口未完成时使用
export const mockLogin = (username, password, role) => {
  // 模拟成功响应
  return {
    code: 200,
    data: {
      user_id: '1001',
      role: role,
      username: username
    }
  }
} 