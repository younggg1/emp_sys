import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    userId: null,
    schoolId: null,
    role: null,
    isLoggedIn: false
  }),
  
  actions: {
    setUser(userData) {
      this.userId = userData.user_id
      this.schoolId = userData.school_id
      this.role = userData.role
      this.isLoggedIn = true
      
      // 存储到localStorage，以便刷新后恢复状态
      localStorage.setItem('userData', JSON.stringify(userData))
    },
    
    clearUser() {
      this.userId = null
      this.schoolId = null
      this.role = null
      this.isLoggedIn = false
      
      // 清除localStorage
      localStorage.removeItem('userData')
      localStorage.removeItem('token')
    },
    
    checkLoggedIn() {
      const userData = localStorage.getItem('userData')
      if (userData) {
        const parsedData = JSON.parse(userData)
        this.userId = parsedData.user_id
        this.schoolId = parsedData.school_id
        this.role = parsedData.role
        this.isLoggedIn = true
        return true
      }
      return false
    }
  }
}) 
