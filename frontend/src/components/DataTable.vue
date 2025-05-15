<template>
  <div class="data-table">
    <el-table
      :data="data"
      style="width: 100%"
      v-loading="loading"
      border
      stripe
      :max-height="maxHeight"
    >
      <el-table-column
        v-for="column in columns"
        :key="column.prop"
        :prop="column.prop"
        :label="column.label"
        :width="column.width"
        :fixed="column.fixed"
        :sortable="column.sortable"
        :formatter="column.formatter"
      >
        <template #default="scope" v-if="column.slot">
          <slot :name="column.slot" :row="scope.row" :index="scope.$index"></slot>
        </template>
      </el-table-column>
      
      <template v-if="showActions">
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <slot name="actions" :row="scope.row" :index="scope.$index">
              <el-button size="small" type="primary" @click="$emit('edit', scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="$emit('delete', scope.row)">删除</el-button>
            </slot>
          </template>
        </el-table-column>
      </template>
    </el-table>
    
    <div class="pagination" v-if="showPagination">
      <el-pagination
        :current-page="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="$emit('size-change', $event)"
        @current-change="$emit('current-change', $event)"
      />
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

defineEmits(['edit', 'delete', 'size-change', 'current-change'])

defineProps({
  data: {
    type: Array,
    default: () => []
  },
  columns: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  },
  showActions: {
    type: Boolean,
    default: true
  },
  maxHeight: {
    type: [String, Number],
    default: '650'
  },
  showPagination: {
    type: Boolean,
    default: false
  },
  currentPage: {
    type: Number,
    default: 1
  },
  pageSize: {
    type: Number, 
    default: 10
  },
  total: {
    type: Number,
    default: 0
  }
})
</script>

<style scoped>
.data-table {
  margin-bottom: 20px;
}
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 
