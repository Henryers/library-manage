<script lang="ts" setup>
import { reactive, ref } from 'vue'
import { getBookCategoryAPI } from '@/api/bookCategory'
import { getBookByIdAPI, updateBookAPI } from '@/api/book'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

// ------ .d.ts 属性类型接口 ------
// 接收到不在接口中定义的属性的数据，ts会报错，但是类型推断错误不会妨碍接收，控制台还是能打印的
interface BookDTO {
  name: string
  categoryId: number
  author: string
  press: string
  publishDate: string
  price: number
  pageNumber: number
  keywords: string
  notes: string
}
interface Category {
  id: number
  name: string
}

// const showComponent = ref(true)
const formLabelWidth = '140px'
const id = ref()
// 书籍id对应的分类列表，即categoryId字段不能只展示id值，应该根据id查询到对应的分类名进行回显
const categoryList = ref<Category[]>([])
// 表单信息
const form = reactive<BookDTO>({
  name: '',
  categoryId: 1,
  author: '',
  press: '',
  publishDate: '',
  price: 0,
  pageNumber: 0,
  keywords: '',
  notes: ''
})
const isValidForm = ref()

// 表单校验
const rules = {
  name: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  categoryId: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  author: [
    { required: true, trigger: 'blur', message: '不能为空' }
  ],
  press: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  publishDate: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  price: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  pageNumber: [
    { required: true, trigger: 'blur', message: '不能为空' }
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

// 修改书籍信息后提交
const submit = async () => {
  try {
    const valid = await isValidForm.value.validate()
    if (valid) {
      console.log('submit successfully!')
      const date = new Date(form.publishDate)
      // 将日期转换为年月日格式(不能换行，空格和\n会被算进去...)
      form.publishDate = date.getFullYear() + '-' +
        String(date.getMonth() + 1).padStart(2, '0') + '-' +
        String(date.getDate()).padStart(2, '0')
      await updateBookAPI(form)
      // 然后进行 消息提示，页面跳转 等操作
      ElMessage({
        message: '修改成功！',
        type: 'success',
      })
      router.push({
        path: '/book',
      })
    } else {
      console.log('form not valid!')
      return false;
    }
  } catch (error) {
    console.error('An error occurred during form validation:', error);
  }
}
// 取消修改
const cancel = () => {
  router.push({
    path: '/book',
  })
}

const init = async () => {
  // 1、获取书籍分类列表
  const { data: res_category } = await getBookCategoryAPI({ page: 1, pageSize: 100 })
  console.log('分类列表')
  console.log(res_category.data)
  categoryList.value = res_category.data.records
  console.log('categoryList: ', categoryList.value)
  // 2、根据路径中的id获取书籍信息，回显到表单中
  console.log(route.query)
  if (route.query) {
    id.value = route.query.id || 0;
    const { data: res } = await getBookByIdAPI(id.value)
    Object.assign(form, res.data)
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
      <el-form-item label="书名" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="分类" :label-width="formLabelWidth" prop="categoryId">
        <el-select clearable v-model="form.categoryId" placeholder="选择分类类型">
          <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="作者" :label-width="formLabelWidth" prop="author">
        <el-input v-model="form.author" autocomplete="off" />
      </el-form-item>
      <el-form-item label="出版社" :label-width="formLabelWidth" prop="press">
        <el-input v-model="form.press" autocomplete="off" />
      </el-form-item>
      <el-form-item label="出版日期" :label-width="formLabelWidth" prop="publishDate">
        <el-date-picker v-model="form.publishDate" type="date" placeholder="请选择出版日期" style="width: 100%" />
      </el-form-item>
      <el-form-item label="价格" :label-width="formLabelWidth" prop="price">
        <el-input v-model="form.price" autocomplete="off" />
      </el-form-item>
      <el-form-item label="页码数" :label-width="formLabelWidth" prop="pageNumber">
        <el-input v-model="form.pageNumber" autocomplete="off" />
      </el-form-item>
      <el-form-item label="关键词" :label-width="formLabelWidth" prop="keywords">
        <el-input v-model="form.keywords" autocomplete="off" />
      </el-form-item>
      <el-form-item label="备注" :label-width="formLabelWidth" prop="notes">
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
