<script lang="ts" setup>
import { reactive, ref } from 'vue'
import { getBookCategoryByIdAPI, updateBookCategoryAPI } from '@/api/bookCategory'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

// ------ .d.ts 属性类型接口 ------
interface BookCategoryDTO {
  name: string
  keywords: string
  notes: string
}

const formLabelWidth = '140px'
const id = ref()
// 表单信息
const form = reactive<BookCategoryDTO>({
  name: '',
  keywords: '',
  notes: ''
})
// const inputRef = ref<HTMLInputElement | null>(null)
const isValidForm = ref()

// 表单校验
const rules = {
  name: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  keywords: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  notes: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ]
}
const router = useRouter()
const route = useRoute()

// ------ 方法 ------
const init = async () => {
  console.log(route.query)
  if (route.query) {
    id.value = route.query.id || 0
    let BookCategory = await getBookCategoryByIdAPI(id.value)
    // 真服了，下面这种常规写法丢失响应式！因为对象重新赋值会分配新的引用地址，其指向的对象是新对象，因此丢失响应式！
    // form = User.data.data
    // 重新赋值，不改变引用的写法
    Object.assign(form, BookCategory.data.data)
    console.log(form)
  } else {
    console.log('没有id')
  }
  console.log(id)
}
init()

// 修改单个书籍分类信息
const submit = async () => {
  try {
    const valid = await isValidForm.value.validate()
    if (valid) {
      const params = {
        id: id.value,
        ...form
      }
      console.log('bc-params:', params)
      await updateBookCategoryAPI(params)
      // 然后进行 消息提示，页面跳转 等操作
      ElMessage({
        message: '修改成功！',
        type: 'success',
      })
      router.push({
        path: '/bookCategory',
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
    path: '/bookCategory',
  })
}
</script>

<template>
  <el-card class="my-card">
    <el-form :model="form" :rules="rules" ref="isValidForm">
      <el-form-item label="name" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="keywords" :label-width="formLabelWidth" prop="keywords">
        <el-input v-model="form.keywords" autocomplete="off" />
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
  margin-right: 20px
}
</style>

<style>
/* .el-form {
  display: flex;
  flex-direction: column;
} */
</style>
