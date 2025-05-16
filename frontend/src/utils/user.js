/**
 * 用户信息管理工具
 * 提供用户登录状态和信息的管理功能
 */

// 用户信息
let userData = null

// 检查是否已登录
export function checkLoggedIn() {
  const storedUser = localStorage.getItem('userData')
  if (storedUser) {
    userData = JSON.parse(storedUser)
    return true
  }
  return false
}

// 设置用户信息
export function setUser(user) {
  userData = user
  localStorage.setItem('userData', JSON.stringify(user))
}

// 清除用户信息
export function clearUser() {
  userData = null
  localStorage.removeItem('userData')
}

// 获取用户信息
export function getUser() {
  if (!userData) {
    checkLoggedIn()
  }
  return userData
}

// 获取用户角色
export function getUserRole() {
  return getUser()?.role
}

// 获取用户名
export function getUsername() {
  return getUser()?.username
}

// 获取用户ID
export function getUserId() {
  return getUser()?.user_id
} 