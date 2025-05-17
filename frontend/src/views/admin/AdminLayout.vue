<template>
  <div class="admin-layout">
    <el-container style="height: 100vh;">
      <!-- 顶部导航栏 -->
      <el-header class="layout-header">
        <div class="logo">大学毕业生就业情况调查系统</div>
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
      
      <el-container class="main-container">
        <!-- 左侧菜单栏 -->
        <el-aside width="220px" class="layout-aside">
          <el-menu
            :default-active="activeMenu"
            class="el-menu-vertical"
            :router="true"
            :unique-opened="true"
            background-color="#304156"
            text-color="#bfcbd9"
            active-text-color="#409EFF"
          >
            <div class="menu-header">
              <span>管理员端</span>
            </div>
            <el-menu-item index="/admin/statistics">
              <el-icon><pie-chart /></el-icon>
              <span>就业数据统计</span>
            </el-menu-item>
            <el-menu-item index="/admin/history">
              <el-icon><calendar /></el-icon>
              <span>历年就业数据</span>
            </el-menu-item>
            <el-menu-item index="/admin/user-manage">
              <el-icon><user /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/permission-manage">
              <el-icon><user /></el-icon>
              <span>权限管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/settings">
              <el-icon><setting /></el-icon>
              <span>系统设置</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        
        <!-- 右侧内容区域 -->
        <el-main class="layout-main">
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getUser, clearUser } from '@/utils/user'
import { Document, ChatDotRound, PieChart, Calendar, User, Setting, ArrowDown } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()

// 计算当前活动菜单
const activeMenu = computed(() => {
  return route.path
})

// 计算用户角色显示文本
const userRole = computed(() => {
  const user = getUser()
  const roleMap = {
    student: '学生',
    counselor: '辅导员',
    admin: '管理员'
  }
  return `${user?.username || '未知用户'} (${roleMap[user?.role] || '用户'})`
})

// 处理下拉菜单命令
const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      clearUser()
      router.push('/')
    }).catch(() => {})
  }
}
</script>

<style scoped>
.admin-layout {
  width: 100%;
  height: 100%;
}

.layout-header {
  background-color: #3c8dbc;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 60px;
  line-height: 60px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.logo {
  font-size: 18px;
  font-weight: bold;
  color: #fff;
}

.main-container {
  height: calc(100vh - 60px);
}

.layout-aside {
  background-color: #304156;
  overflow: hidden;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
  transition: width 0.3s;
}

.menu-header {
  height: 50px;
  line-height: 50px;
  padding-left: 20px;
  font-size: 14px;
  color: #bfcbd9;
  border-bottom: 1px solid #1f2d3d;
  font-weight: bold;
}

.el-menu-vertical {
  border-right: none;
  height: 100%;
}

.el-menu-vertical .el-menu-item {
  height: 56px;
  line-height: 56px;
}

.el-menu-vertical .el-menu-item:hover {
  background-color: #263445 !important;
}

.el-menu-vertical .el-menu-item.is-active {
  background-color: #1f2d3d !important;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-info-text {
  cursor: pointer;
  color: #fff;
  display: flex;
  align-items: center;
  gap: 5px;
}

.layout-main {
  background-color: #f5f7fa;
  padding: 20px;
  overflow-y: auto;
}

.el-menu-item {
  display: flex;
  align-items: center;
}

.el-menu-item .el-icon {
  margin-right: 10px;
}
</style> 