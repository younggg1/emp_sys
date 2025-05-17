<template>
  <div class="permission-manage">
    <el-card class="table-card">
      <div class="table-header">
        <h3>辅导员权限管理</h3>
      </div>

      <el-table :data="counselorList" border style="width: 100%" v-loading="loading">
        <el-table-column type="index" width="50" label="序号"></el-table-column>
        <el-table-column prop="username" label="工号"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column label="权限" width="300">
          <template #default="scope">
            <el-switch
              v-model="scope.row.permission"
              active-value="Y"
              inactive-value="N"
              active-text="可删除本班数据"
              @change="handlePermissionChange(scope.row)"
            ></el-switch>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getCounselorListWithUserInfo, updatePermission } from '@/api/admin'

// 表格数据
const counselorList = ref([])
const loading = ref(false)

// 获取辅导员列表
const fetchCounselorList = async () => {
  loading.value = true
  try {
    const res = await getCounselorListWithUserInfo()
    console.log('获取到的辅导员列表数据:', res.data)
    if (res.code === 200) {
      counselorList.value = res.data
      console.log('处理后的辅导员列表数据:', counselorList.value)
    } else {
      ElMessage.error(res.msg || '获取辅导员列表失败')
    }
  } catch (error) {
    console.error('获取辅导员列表失败:', error)
    ElMessage.error('获取辅导员列表失败')
  } finally {
    loading.value = false
  }
}

// 处理权限变更
const handlePermissionChange = async (row) => {
  try {
    console.log('更新权限，参数:', { counselor_id: row.counselor_id, permission: row.permission })
    const res = await updatePermission(row.counselor_id, { permission: row.permission })
    console.log('更新权限响应:', res)
    if (res.code === 200) {
      ElMessage.success('权限更新成功')
    } else {
      ElMessage.error(res.msg || '权限更新失败')
      // 恢复原状态
      row.permission = row.permission === 'Y' ? 'N' : 'Y'
    }
  } catch (error) {
    console.error('权限更新失败:', error)
    ElMessage.error('权限更新失败')
    // 恢复原状态
    row.permission = row.permission === 'Y' ? 'N' : 'Y'
  }
}

onMounted(() => {
  fetchCounselorList()
})
</script>

<style scoped>
.permission-manage {
  padding: 0;
}

.table-card {
  margin-bottom: 20px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.table-header h3 {
  margin: 0;
  font-size: 18px;
}
</style> 