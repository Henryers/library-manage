<script setup lang="ts" name="layout">
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ref, reactive } from 'vue'
import { useManagerStore } from '@/store'
import { updateAPI } from '@/api/manager'

// ------ data ------
const formLabelWidth = '80px'
const managerStore = useManagerStore()

const form = reactive({
  name: '',
  oldPwd: '',
  newPwd: '',
  rePwd: '',
})
const tmpform = ref()

// 自定义校验规则: 两次密码是否一致
const samePwd = (rules: any, value: any, callback: any) => {
  if (value !== form.newPwd) {
    // 如果验证失败，则调用 回调函数时，指定一个 Error 对象。
    callback(new Error('两次输入的密码不一致!'))
  } else {
    // 如果验证成功，则直接调用 callback 回调函数即可。
    callback()
  }
}
const rules = { // 表单的规则检验对象
  name: [
    { required: true, message: '不能为空', trigger: 'blur' },
  ],
  oldPwd: [
    { required: true, message: '请输入原密码', trigger: 'blur' },
    { pattern: /^\S{6,15}$/, message: '原密码必须是6-15的非空字符', trigger: 'blur' }
  ],
  newPwd: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { pattern: /^\S{6,15}$/, message: '新密码必须是6-15的非空字符', trigger: 'blur' }
  ],
  rePwd: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { pattern: /^\S{6,15}$/, message: '新密码必须是6-15的非空字符', trigger: 'blur' },
    { validator: samePwd, trigger: 'blur' }
  ]
}

// ------ method ------
const router = useRouter()

// 修改管理员信息
const submit = async () => {
  try {
    const valid = await tmpform.value.validate();
    if (valid) {
      console.log('submit successfully!');
      const submitForm = {
        name: form.name,
        oldPwd: form.oldPwd,
        newPwd: form.newPwd,
      }
      console.log('要提交的表单信息')
      console.log(submitForm)
      // 在这里执行表单提交操作，比如调用updateUser(form)方法等
      const { data: res } = await updateAPI(submitForm)
      if (res.code != 0) return   // 密码错误信息会在相应拦截器中捕获并提示
      // 然后进行 消息提示，store更新，页面跳转 等操作
      ElMessage({
        type: 'success',
        message: '修改成功，请重新登录~',
      })
      managerStore.managerInfo!.name = form.name
      router.push({
        path: '/login',
      })
    } else {
      console.log('form not valid!');
      return false;
    }
  } catch (error) {
    console.error('An error occurred during form validation:', error);
  }
}
// 取消修改
const cancel = () => {
  router.push({
    path: '/home',
  })
}
</script>

<template>
  <el-card class="my-card">
    <h1>修改个人信息</h1>
    <el-form :model="form" :rules="rules" ref="tmpform" class="my-form">
      <el-form-item label="名称" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="原密码" :label-width="formLabelWidth" prop="oldPwd">
        <el-input type="password" v-model="form.oldPwd" autocomplete="off" />
      </el-form-item>
      <el-form-item label="新密码" :label-width="formLabelWidth" prop="newPwd">
        <el-input type="password" v-model="form.newPwd" autocomplete="off" />
      </el-form-item>
      <el-form-item label="确认密码" :label-width="formLabelWidth" prop="rePwd">
        <el-input type="password" v-model="form.rePwd" autocomplete="off" />
      </el-form-item>
    </el-form>
    <el-button type="success" @click="submit">确认修改</el-button>
    <el-button plain type="info" @click="cancel">取消修改</el-button>
  </el-card>
</template>

<style scoped>
.my-form {
  width: 600px;
  margin: 0 auto;
}
</style>

<style>
/* .el-form {
  display: flex;
  flex-direction: column;
} */
</style>
