/**
 * 全局配置文件
 */

// API基础URL
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'  // 从环境变量读取，未设置时使用默认后端地址

// 请求超时时间（毫秒）
export const REQUEST_TIMEOUT = import.meta.env.VITE_REQUEST_TIMEOUT || 10000

// 是否使用模拟数据
export const USE_MOCK = import.meta.env.VITE_USE_MOCK === 'true'

// 其他全局配置... 