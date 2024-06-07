<script lang="ts" setup>
import { reactive, ref } from 'vue'
import { getReaderCategoryAPI } from '@/api/readerCategory'
import { getReaderByIdAPI, updateReaderAPI } from '@/api/reader'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

// ------ .d.ts 属性类型接口 ------
// 接收到不在接口中定义的属性的数据，ts会报错，但是类型推断错误不会妨碍接收，控制台还是能打印的
interface ReaderDTO {
  name: string
  categoryId: number
  sex: number
  wAddress: string
  hAddress: string
  phone: string
  email: string
  notes: string
}
interface Category {
  id: number
  name: string
}

const formLabelWidth = '140px'
const id = ref()
// 读者id对应的分类列表，即categoryId字段不能只展示id值，应该根据id查询到对应的分类名进行回显
const categoryList = ref<Category[]>([])
// 表单信息
const form = reactive<ReaderDTO>({
  name: '',
  categoryId: 1,
  sex: 1,
  wAddress: '',
  hAddress: '',
  phone: '',
  email: '',
  notes: '',
})
// 性别列表
const sexList = [
  {
    id: 1,
    name: '男',
  },
  {
    id: 0,
    name: '女',
  }
]
const isValidForm = ref()

// 表单校验
const rules = {
  name: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  categoryId: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  sex: [
    { required: true, trigger: 'blur', message: '不能为空' }
  ],
  wAdderss: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  hAddress: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  phone: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  email: [
    { required: true, trigger: 'blur', message: '不能为空' }
  ],
  notes: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ]
}

const router = useRouter()
const route = useRoute()

// 修改读者信息后提交
const submit = async () => {
  try {
    const valid = await isValidForm.value.validate()
    if (valid) {
      console.log('submit successfully!')
      const { data: res } = await updateReaderAPI(form)
      if (res.code == 1) return false
      // 然后进行 消息提示，页面跳转 等操作
      ElMessage({
        message: '修改成功！',
        type: 'success',
      })
      router.push({
        path: '/reader',
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
    path: '/reader',
  })
}

const init = async () => {
  // 1、获取读者分类列表
  const { data: res_category } = await getReaderCategoryAPI({ page: 1, pageSize: 100 })
  console.log('分类列表')
  console.log(res_category.data)
  categoryList.value = res_category.data.records
  console.log('categoryList: ', categoryList.value)
  // 2、根据路径中的id获取读者信息，回显到表单中
  console.log(route.query)
  if (route.query) {
    id.value = route.query.id || 0;
    const { data: res } = await getReaderByIdAPI(id.value)
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
      <el-form-item label="name" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="categoryId" :label-width="formLabelWidth" prop="categoryId">
        <el-select clearable v-model="form.categoryId" placeholder="选择分类类型">
          <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="sex" :label-width="formLabelWidth" prop="sex">
        <el-select clearable v-model="form.sex" placeholder="选择性别">
          <el-option v-for="item in sexList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="wAddress" :label-width="formLabelWidth" prop="wAddress">
        <el-input v-model="form.wAddress" autocomplete="off" />
      </el-form-item>
      <el-form-item label="hAddress" :label-width="formLabelWidth" prop="hAddress">
        <el-input v-model="form.hAddress" autocomplete="off" />
      </el-form-item>
      <el-form-item label="phone" :label-width="formLabelWidth" prop="phone">
        <el-input v-model="form.phone" autocomplete="off" />
      </el-form-item>
      <el-form-item label="email" :label-width="formLabelWidth" prop="email">
        <el-input v-model="form.email" autocomplete="off" />
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
