<script lang="ts" setup>
import { reactive, ref } from 'vue'
import { getReaderCategoryByIdAPI, updateReaderCategoryAPI } from '@/api/readerCategory'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

// ------ .d.ts 属性类型接口 ------
interface ReaderCategoryDTO {
  name: string
  amount: number
  lendPeriod: string
  effectPeriod: string
  notes: string
}

// ------ 数据 ------
const formLabelWidth = '140px'
const id = ref()
// 表单信息
const form = reactive<ReaderCategoryDTO>({
  name: '',
  amount: 0,
  lendPeriod: '',
  effectPeriod: '',
  notes: ''
})
// const inputRef = ref<HTMLInputElement | null>(null)
const isValidForm = ref()

// 表单校验
const rules = {
  name: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  amount: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  lendPeriod: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  effectPeriod: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  notes: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ]
}
const router = useRouter()
const route = useRoute()

// 修改员工
const submit = async () => {
  try {
    const valid = await isValidForm.value.validate();
    if (valid) {
      const date = new Date(form.effectPeriod)
      form.effectPeriod = date.getFullYear() + '-' +
        String(date.getMonth() + 1).padStart(2, '0') + '-' +
        String(date.getDate()).padStart(2, '0')
      const params = {
        id: id.value,
        ...form
      }
      console.log(params)
      // 在这里执行表单提交操作，比如调用updateUser(form)方法等
      await updateReaderCategoryAPI(params)
      // 然后进行 消息提示，页面跳转 等操作
      ElMessage({
        message: '修改成功！',
        type: 'success',
      })
      router.push({
        path: '/readerCategory',
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
    path: '/readerCategory',
  })
}

const init = async () => {
  console.log(route.query)
  if (route.query) {
    id.value = route.query.id || 0;
    let readerCategory = await getReaderCategoryByIdAPI(id.value)
    // 真服了，下面这种常规写法丢失响应式！因为对象重新赋值会分配新的引用地址，其指向的对象是新对象，因此丢失响应式！
    // form = User.data.data
    // 重新赋值，不改变引用的写法
    Object.assign(form, readerCategory.data.data)
    console.log(form)
  } else {
    console.log('没有id')
  }
  console.log(id)
}

init()
</script>

<template>
  <el-card class="my-card">
    <el-form :model="form" :rules="rules" ref="isValidForm">
      <el-form-item label="name" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="amount" :label-width="formLabelWidth" prop="amount">
        <el-input v-model="form.amount" autocomplete="off" />
      </el-form-item>
      <el-form-item label="lendPeriod" :label-width="formLabelWidth" prop="lendPeriod">
        <el-input v-model="form.lendPeriod" autocomplete="off" />
      </el-form-item>
      <el-form-item label="effectPeriod" :label-width="formLabelWidth" prop="effectPeriod">
        <el-date-picker v-model="form.effectPeriod" type="date" placeholder="请选择有效借书日期" style="width: 100%" />
      </el-form-item>
      <el-form-item label="notes" :label-width="formLabelWidth" prop="notes">
        <el-input v-model="form.notes" autocomplete="off" />
      </el-form-item>
    </el-form>
    <el-button type="success" @click="submit">修改</el-button>
    <el-button plain type="info" @click="cancel">取消</el-button>
  </el-card>
</template>

<style scoped>
img {
  width: 60px;
  height: 50px;
  margin-right: 20px;
}
</style>

<style>
/* .el-form {
  display: flex;
  flex-direction: column;
} */
</style>
