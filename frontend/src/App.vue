﻿<template>
  <el-config-provider :locale="zhCn">
    <RouterView />
  </el-config-provider>
</template>
<script setup>
import { RouterView } from 'vue-router'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
</script>
<style>
#app {
  height: 100%;
}
</style>
