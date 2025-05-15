<template>
  <div
    class="ValidCode disabled-select"
    style="width: 100%; height: 100%"
    @click="refreshCode"
    :key="refreshKey"
  >
    <span
      v-for="(item, index) in codeList"
      :key="index"
      :style="getStyle(item)"
    >{{ item.code }}</span>
  </div>
</template>

<script setup>
import { ref, onMounted, defineEmits } from 'vue'

// 定义emit
const emit = defineEmits(['update:value'])

// 定义响应式数据
const length = 4 // 验证码的长度
const codeList = ref([]) // 验证码字符列表
const refreshKey = ref(0) // 用于强制刷新验证码

// 生成验证码
const createdCode = () => {
  const len = length
  const chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz0123456789'
  const charsLen = chars.length
  const newCodeList = []

  for (let i = 0; i < len; i++) {
    const rgb = [
      Math.round(Math.random() * 220),
      Math.round(Math.random() * 240),
      Math.round(Math.random() * 200),
    ]
    newCodeList.push({
      code: chars.charAt(Math.floor(Math.random() * charsLen)),
      color: `rgb(${rgb})`,
      padding: `${Math.floor(Math.random() * 10)}px`,
      transform: `rotate(${Math.floor(Math.random() * 90) - Math.floor(Math.random() * 90)}deg)`,
    })
  }

  codeList.value = newCodeList
  const newCode = newCodeList.map(item => item.code).join('')
  emit('update:value', newCode) // 派发事件
  return newCode // 返回生成的验证码
}

// 刷新验证码
const refreshCode = () => {
  createdCode()
  refreshKey.value += 1
}

// 获取当前验证码
const getCurrentCode = () => {
  return codeList.value.map(item => item.code).join('').toLowerCase()
}

// 获取验证码样式
const getStyle = (data) => {
  return `color: ${data.color}; font-size: 18px; padding: ${data.padding}; transform: ${data.transform}`
}

// 暴露方法给父组件
defineExpose({
  refreshCode,
  getCurrentCode
})

onMounted(() => {
  createdCode()
})
</script>

<style>
.ValidCode {
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}
.ValidCode span {
  display: inline-block;
  font-size: 18px;
}
</style>