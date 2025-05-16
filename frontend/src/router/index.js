import { createRouter, createWebHistory } from 'vue-router'
import { checkLoggedIn, getUserRole } from '@/utils/user'
import LoginView from '../views/LoginView.vue'
import StudentLayout from '../views/student/StudentLayout.vue'
import CounselorLayout from '../views/counselor/CounselorLayout.vue'
import AdminLayout from '../views/admin/AdminLayout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: LoginView
    },
    {
      path: '/student',
      component: StudentLayout,
      meta: { requiresAuth: true, role: 'student' },
      children: [
        {
          path: '',
          name: 'studentHome',
          component: () => import('../views/student/Dashboard.vue')
        },
        {
          path: 'employment',
          name: 'studentEmployment',
          component: () => import('../views/student/Employment.vue')
        },
        {
          path: 'feedback',
          name: 'studentFeedback',
          component: () => import('../views/student/Feedback.vue')
        }
      ]
    },
    {
      path: '/counselor',
      component: CounselorLayout,
      meta: { requiresAuth: true, role: 'counselor' },
      children: [
        {
          path: '',
          name: 'counselorHome',
          component: () => import('../views/counselor/Dashboard.vue')
        },
        {
          path: 'employment',
          name: 'counselorEmployment',
          component: () => import('../views/counselor/Employment.vue')
        },
        {
          path: 'feedback',
          name: 'counselorFeedback',
          component: () => import('../views/counselor/Feedback.vue')
        },
        {
          path: 'statistics',
          name: 'counselorStatistics',
          component: () => import('../views/counselor/Statistics.vue')
        }
      ]
    },
    {
      path: '/admin',
      component: AdminLayout,
      meta: { requiresAuth: true, role: 'admin' },
      children: [
        {
          path: '',
          redirect: '/admin/employment'
        },
        {
          path: 'employment',
          name: 'adminEmployment',
          component: () => import('../views/admin/Employment.vue')
        },
        {
          path: 'feedback',
          name: 'adminFeedback',
          component: () => import('../views/admin/Feedback.vue')
        },
        {
          path: 'statistics',
          name: 'adminStatistics',
          component: () => import('../views/admin/Statistics.vue')
        },
        {
          path: 'history',
          name: 'adminHistory',
          component: () => import('../views/admin/History.vue')
        },
        {
          path: 'user-manage',
          name: 'adminUserManage',
          component: () => import('../views/admin/UserManage.vue')
        },
        {
          path: 'settings',
          name: 'adminSettings',
          component: () => import('../views/admin/Settings.vue')
        }
      ]
    }
  ]
})

// 全局前置守卫，验证用户身份和权限
router.beforeEach((to, from, next) => {
  // 检查路由是否需要身份验证
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // 检查是否已登录
    if (!checkLoggedIn()) {
      // 未登录，重定向到登录页
      next({ name: 'login' })
    } else {
      // 已登录，检查角色权限
      const userRole = getUserRole()
      if (to.meta.role && to.meta.role !== userRole) {
        // 角色不匹配，重定向到对应角色的首页
        next({ path: `/${userRole}` })
      } else {
        // 权限验证通过
        next()
      }
    }
  } else {
    // 不需要身份验证的路由
    next()
  }
})

export default router
