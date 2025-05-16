<template>
  <div class="login-container">
    <div class="login-card">
      <h1 class="login-title">大学毕业生就业情况调查系统</h1>
      <LoginForm />
    </div>
  </div>
</template>
 
<script setup>
import LoginForm from '@/components/LoginForm.vue'
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
}

.login-card {
  width: 100%;
  max-width: 600px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 30px;
}

.login-title {
  text-align: center;
  margin-bottom: 30px;
  color: #409EFF;
}
</style> 
