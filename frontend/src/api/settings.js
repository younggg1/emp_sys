import request from '@/utils/request'

/**
 * 获取系统设置
 * @returns {Promise<*>}
 */
export function getSystemSettings() {
  return request({
    url: '/api/settings/get',
    method: 'get'
  })
}

/**
 * 更新验证码设置
 * @param {boolean} requireCaptcha 是否启用验证码
 * @returns {Promise<*>}
 */
export function updateCaptchaSetting(requireCaptcha) {
  return request({
    url: '/api/settings/captcha',
    method: 'post',
    data: {
      requireCaptcha
    }
  })
} 