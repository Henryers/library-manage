<script setup lang="ts">
import { loginAPI } from '@/api/manager'
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useManagerStore } from '@/store'

const store = useManagerStore()

const form = ref({
  name: '',
  password: ''
});
// 表单校验的ref
const loginRef = ref()

const rules = {
  name: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9]{1,10}$/, message: '用户名必须是1-10的字母数字', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { pattern: /^\S{6,15}$/, message: '密码必须是6-15的非空字符', trigger: 'blur' }
  ]
}

const router = useRouter()

const loginFn = async () => {
  // 先校验输入格式是否合法
  const valid = await loginRef.value.validate()
  if (valid) {
    // 调用登录接口
    const { data: res } = await loginAPI(form.value)
    console.log(res)
    if (res.code !== 0) {
      // 登录失败，提示用户(响应拦截器统一处理)
      return false
    }
    // 登录成功，提示用户
    ElMessage.success('登录成功')
    // 把后端返回的token存储到Pinia里
    store.managerInfo = res.data
    // 跳转到首页
    router.push('/')
  } else {
    return false
  }
}
</script>

<template>
  <div class="background">
    <el-form label-width="0px" class="login-box" :model="form" :rules="rules" ref="loginRef">
      <div class="title-box">登 录</div>
      <el-form-item prop="name">
        <el-input v-model="form.name" placeholder="请输入账号"></el-input>
      </el-form-item>
      <el-form-item  prop="password">
        <el-input type="password" v-model="form.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item class="my-el-form-item">
        <el-button type="primary" class="btn-login" @click="loginFn">登录</el-button>
        <el-link type="info" @click="$router.push('/reg')">去注册</el-link>
      </el-form-item>
    </el-form>
  </div>
</template>


<style lang="less" scoped>
.background {
  width: 100%;
  height: 100vh;
  background-size: cover;
  background-image: url('../../assets/library.jpg');
}
.background::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  /* 黑色半透明 */
  z-index: 1;
  /* 确保伪元素在背景图之上 */
}

.login-box {
  z-index: 10;
  width: 400px;
  height: 340px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  padding: 0 30px;
  box-sizing: border-box;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: #aaaaaa 0 0 60px;

  .title-box {
    height: 100px;
    line-height: 100px;
    font-size: 20px;
    font-weight: bold;
    text-align: center;
  }

  // .my-el-form-item {
  //   width: 300px;
  //   margin-right: 0px;
  // }

  .btn-login {
    width: 100%;
  }
}
</style>

<style>
.el-form {
  display: flex;
  flex-direction: column;
}
</style>
