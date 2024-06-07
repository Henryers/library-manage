<script setup lang="ts">

import { reactive, ref } from 'vue'
import { addReaderCategoryAPI, getReaderCategoryAPI, deleteReaderCategoryAPI } from '@/api/readerCategory'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'

// ------ .d.ts 属性类型接口 ------
interface ReaderCategoryDTO {
  name: string
  amount: number
  lendPeriod: string
  effectPeriod: string
  notes: string
}
interface ReaderCategory {
  id: number
  name: string
  keywords: string
  notes: string
}

// ------ 配置 ------
const dialogFormVisible = ref(false)
const formLabelWidth = '140px'
const isValidForm = ref()

// ------ 数据 ------
// dialog中的表单信息
const form = reactive<ReaderCategoryDTO>({
  name: '',
  amount: 0,
  lendPeriod: '',
  effectPeriod: '',
  notes: ''
})
// 当前页的分类列表
const readerCategoryList = ref<ReaderCategory[]>([])
// 带查询条件的分页参数
const pageData = reactive({
  page: 1,
  pageSize: 6,
  name: ''
})
const total = ref(0)
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

// ------ 方法 ------

// 页面初始化，就根据token去获取用户信息，才能实现如果没有token/token过期，刚开始就能够跳转到登录页
const init = async () => {
  // 参数解构再传进去，不用传total
  const { data: res } = await getReaderCategoryAPI(pageData)
  console.log(res)
  console.log('分类列表')
  console.log(res.data)
  readerCategoryList.value = res.data.records
  total.value = res.data.total
}

init()  // 页面初始化/分页查询，写在这里时的生命周期是beforecreated/created的时候

// 监听翻页和每页显示数量的变化
const handleCurrentChange = (val: number) => {
  pageData.page = val
  // 条件分页查询
  init()
}

const handleSizeChange = (val: number) => {
  pageData.pageSize = val
  // 条件分页查询
  init()
}

// 新增读者分类
const add_btn = async () => {
  try {
    const valid = await isValidForm.value.validate()
    if (valid) {
      const date = new Date(form.effectPeriod)
      // 将日期转换为年月日格式
      form.effectPeriod = date.getFullYear() + '-' +
        String(date.getMonth() + 1).padStart(2, '0') + '-' +
        String(date.getDate()).padStart(2, '0')
      console.log('要提交的表单信息', form)
      const { data: res } = await addReaderCategoryAPI(form)
      console.log("cnm!!!!" + res)
      if (res.code === 1) return false
      dialogFormVisible.value = false
      // 提示新增成功
      ElMessage({
        message: '新增读者分类成功！',
        type: 'success',
      })
      // 新增书籍后刷新页面，更新数据
      init()
    } else {
      console.log('error submit!');
      return false;
    }
  } catch (error) {
    console.error('An error occurred during form validation:', error);
  }
}

// 修改分类(路径传参，到update页面后，根据id查询分类信息，回显到表单中)
const router = useRouter()
const update_btn = (row: any) => {
  console.log('要修改的行数据')
  console.log(row)
  router.push({
    name: 'readerCategoryUpdate',
    query: {
      id: row.id
    }
  })
}

// 删除分类
const delete_btn = (row: any) => {
  console.log('要删除的行数据')
  console.log(row)
  ElMessageBox.confirm(
    '该操作会永久删除分类，是否继续？',
    'Warning',
    {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  )
    .then(async () => {
      console.log('要删除的行数据')
      console.log(row)
      await deleteReaderCategoryAPI(row.id)
      // 删除后刷新页面，更新数据
      init()
      ElMessage({
        type: 'success',
        message: '删除成功',
      })
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消删除',
      })
    })
}
</script>

<template>
  <el-dialog v-model="dialogFormVisible" title="添加图书分类" class="my-info-dialog">
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
        <el-input v-model="form.notes" type="textarea" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button plain type="info" @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="add_btn()">确认</el-button>
      </div>
    </template>
  </el-dialog>

  <el-card class="my-card">
    <div class="horizontal">
      <el-input style="width: 300px; margin: 20px" v-model="pageData.name" class="input" placeholder="请输入分类名" />
      <el-button style="margin: 20px 50px; width:100px" round type="success" @click="init()">查询分类</el-button>
      <el-button style="margin: 20px 100px; width:100px" type="primary" @click="dialogFormVisible = true">
        <el-icon>
          <Plus style="width: 10em; height: 10em; margin-right: 3px" />
        </el-icon>添加分类
      </el-button>
    </div>
    <el-table :data="readerCategoryList">
      <el-table-column prop="id" label="id" />
      <el-table-column prop="name" label="分类名" />
      <el-table-column prop="amount" label="最大借书数量" width="120px" />
      <el-table-column prop="lendPeriod" label="借书时间" />
      <el-table-column prop="effectPeriod" label="借书有效期" width="120px" />
      <el-table-column prop="notes" label="备注" width="300px" />
      <el-table-column label="操作" width="200px">
        <!-- scope 的父组件是 el-table -->
        <template #default="scope">
          <el-button @click="update_btn(scope.row)" type="primary">修改</el-button>
          <el-button @click="delete_btn(scope.row)" type="danger">删除</el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description=" 没有数据" />
      </template>
    </el-table>

    <el-pagination class="pagination" background layout="total, sizes, prev, pager, next, jumper" :total="total"
      :page-sizes="[2, 4, 6, 8]" v-model:current-page="pageData.page" v-model:page-size="pageData.pageSize"
      @current-change="handleCurrentChange" @size-change="handleSizeChange" />
  </el-card>
</template>


<style lang="less" scoped>
// element-plus的样式修改
.el-table {
  width: 90%;
  height: 400px;
  margin: 2rem auto;
  text-align: center;
}

.el-button {
  width: 45px;
  font-size: 12px;
}

.el-pagination {
  justify-content: center;
}

// 自定义样式
img {
  width: 50px;
  height: 50px;
}

.add_btn {
  width: 100px;
  height: 40px;
  margin-left: 900px;
}

.dialog-footer {
  display: flex;
  justify-content: center;
  margin: 20px 0;

  .el-button {
    width: 150px;
  }
}
</style>

<style>
/* .el-form {
  display: flex;
  flex-direction: column;
} */
</style>
