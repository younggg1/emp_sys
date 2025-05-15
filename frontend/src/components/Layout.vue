<template>
  <div class="layout">
    <el-container style="height: 100vh;">
      <el-aside width="200px" class="layout-aside">
        <div class="logo">就业调查系统</div>
        <el-menu
          :default-active="activeMenu"
          class="menu"
          :router="true"
          :unique-opened="true"
          background-color="#001529"
          text-color="#fff"
          active-text-color="#409EFF"
        >
          <slot name="menu"></slot>
        </el-menu>
      </el-aside>
      
      <el-container>
        <el-header class="layout-header">
          <div class="header-title">
            <slot name="header-title">大学毕业生就业情况调查系统</slot>
          </div>
          <div class="user-info">
            <el-dropdown @command="handleCommand">
              <span class="user-info-text">
                {{ userRole }} <el-icon><arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <el-main class="layout-main">
          <slot></slot>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store'
import { ArrowDown } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 计算当前活动菜单
const activeMenu = computed(() => {
  return route.path
})

// 计算用户角色显示文本
const userRole = computed(() => {
  const roleMap = {
    student: '学生',
    counselor: '辅导员',
    admin: '管理员'
  }
  return roleMap[userStore.role] || '用户'
})

// 处理下拉菜单命令
const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      userStore.clearUser()
      router.push('/')
    }).catch(() => {})
  }
}
</script>

<style scoped>
.layout-aside {
  background-color: #001529;
  color: #fff;
}

.logo {
  height: 60px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.menu {
  border-right: none;
}

.layout-header {
  background-color: #fff;
  color: #333;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #e6e6e6;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-info-text {
  cursor: pointer;
  display: flex;
  align-items: center;
}

.layout-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style> 
