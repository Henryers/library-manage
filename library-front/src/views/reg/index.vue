<script setup lang="ts" name:="my-register">
// 导出是命名导出，所以这里导入要加{}
import { registerAPI } from '@/api/manager'
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const form = ref({ // 表单的数据对象
  name: '', // 用户名
  password: '', // 密码
  repassword: '' // 确认密码
})
// 表单校验的ref
const registerRef = ref()

// 自定义校验规则: 两次密码是否一致
// 注意：必须在data函数里定义此箭头函数，才能确保this.from能使用，从而获取到password的值
const samePwd = (rule: any, value: any, callback: any) => {
  if (value !== form.value.password) {
    // 如果验证失败，则调用 回调函数时，指定一个 Error 对象。
    callback(new Error('两次输入的密码不一致!'))
  } else {
    // 如果验证成功，则直接调用 callback 回调函数即可。
    callback()
  }
}
const rules = { // 表单的规则检验对象
  name: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    {
      pattern: /^[a-zA-Z0-9]{1,10}$/,
      message: '用户名必须是1-10的大小写字母数字',
      trigger: 'blur'
    }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: /^\S{6,15}$/,
      message: '密码必须是6-15的非空字符',
      trigger: 'blur'
    }
  ],
  repassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { pattern: /^\S{6,15}$/, message: '密码必须是6-15的非空字符', trigger: 'blur' },
    { validator: samePwd, trigger: 'blur' }
  ]
}

const router = useRouter()

const registerFn = async () => {
  // 先校验输入格式是否合法
  const valid = await registerRef.value.validate()
  if (valid) {
    // 通过校验，拿到绑定的数据
    console.log('注册的表单ref:  ', registerRef)
    console.log('form:  ', form)
    console.log('form.value:  ', form.value)
    // 1.调用注册接口，通过接口的return request，拿到promise对象
    const { data: res } = await registerAPI(form.value)
    console.log(res)
    // 2.注册失败，提示用户
    // elementUI还在Vue的原型链上添加了弹窗提示，$message属性
    if (res.code !== 0) {
      console.log('注册失败！')
      return ElMessage.error(res.msg) // 把alert换成elmessage!
    }
    // 3.注册成功，提示用户
    ElMessage.success('注册成功!')
    // 4.路由跳转到登录页面
    router.push('/login')
  } else {
    return false // 阻止默认提交行为（表单下面红色提示）
  }
}
</script>

<template>
  <!-- 注册页面的整体盒子 -->
  <div class="background">
    <!-- 注册的盒子 -->
    <div class="reg-box">
      <!-- 标题“后台管理系统(图片)”的盒子 -->
      <div class="title-box">注 册</div>
      <!-- 注册的表单区域 -->
      <!-- el-form 自带校验能力，所以直接自定义规则就行(不用什么自定义监听之类的) -->
      <el-form :model="form" label-width="0px" :rules="rules" ref="registerRef">
        <el-form-item prop="name">
          <el-input placeholder="请输入用户名" v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" placeholder="请输入密码" v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item prop="repassword">
          <el-input type="password" placeholder="请再次确认密码" v-model="form.repassword"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="btn-reg" @click="registerFn">注册</el-button>
          <el-link class="router" type="info" @click="router.push('/login')">去登录</el-link>
        </el-form-item>
      </el-form>
    </div>
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

.reg-box {
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
  box-shadow: #aaaaaa 0 0 100px;

  .title-box {
    height: 60px;
    line-height: 60px;
    font-size: 20px;
    font-weight: bold;
    text-align: center;
  }

  .btn-reg {
    width: 100%; // 可以让其占满一行，不用考虑怎么变成块级然后独占一行之类的...
  }

  .router {
    text-align: left;
  }
}
</style>

<style>
/* .el-form {
  display: flex;
  flex-direction: column;
} */
</style>
