<script setup lang="ts">
import { reactive, ref } from 'vue'
import { addBookCategoryAPI, getBookCategoryAPI, deleteBookCategoryAPI } from '@/api/bookCategory'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'

// ------ .d.ts 属性类型接口 ------
interface BookCategoryDTO {
  name: string
  keywords: string
  notes: string
}
interface BookCategory {
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
const form = reactive<BookCategoryDTO>({
  name: '',
  keywords: '',
  notes: ''
})
// 当前页的分类列表
const bookCategoryList = ref<BookCategory[]>([])
// 带查询条件的分页参数
const pageData = reactive({
  name: '',
  keywords: '',
  page: 1,
  pageSize: 6
})
const total = ref(0)

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

// ------ 方法 ------
const init = async () => {
  // 参数解构再传进去，不用传total
  console.log('当前页和每页显示数量', pageData)
  const { data: res } = await getBookCategoryAPI(pageData)
  console.log(res)
  console.log('分类列表')
  console.log(res.data)
  bookCategoryList.value = res.data.records
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

// 新增书籍分类
const add_btn = async () => {
  try {
    const valid = await isValidForm.value.validate()
    if (valid) {
      console.log('要提交的表单信息', form)
      const { data: res } = await addBookCategoryAPI(form)
      console.log(res)
      if (res.code === 1) return false
      dialogFormVisible.value = false
      // 提示新增成功
      ElMessage({
        message: '新增书籍成功！',
        type: 'success',
      })
      // 新增书籍后刷新页面，更新数据
      init()
    } else {
      console.log('error submit!')
      return false;
    }
  } catch (error) {
    console.error('An error occurred during form validation:', error)
  }
}

// 修改分类(路径传参，到update页面后，根据id查询分类信息，回显到表单中)
const router = useRouter()
const update_btn = (row: any) => {
  console.log('要修改的行数据')
  console.log(row)
  router.push({
    name: 'bookCategoryUpdate',
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
      await deleteBookCategoryAPI(row.id)
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
      <el-form-item label="分类名" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="关键词" :label-width="formLabelWidth" prop="keywords">
        <el-input v-model="form.keywords" autocomplete="off" />
      </el-form-item>
      <el-form-item label="备注" :label-width="formLabelWidth" prop="notes">
        <el-input v-model="form.notes" autocomplete="off" />
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
      <el-input style="width: 220px; margin: 0 30px" v-model="pageData.name" class="input" placeholder="请输入分类名" />
      <el-input style="width: 220px; margin: 0 30px" v-model="pageData.keywords" class="input" placeholder="请输入关键词" />
      <el-button style="margin: 20px 50px; width:100px" round type="success" @click="init()">查询分类</el-button>
      <el-button style="margin: 20px 100px; width:100px" type="primary" @click="dialogFormVisible = true">
        <el-icon>
          <Plus style="width: 10em; height: 10em; margin-right: 3px" />
        </el-icon>添加分类
      </el-button>
    </div>
    <el-table :data="bookCategoryList">
      <el-table-column prop="id" label="id" width="100px" />
      <el-table-column prop="name" label="分类名" width="100px" />
      <el-table-column prop="keywords" label="关键词" width="200px" />
      <el-table-column prop="notes" label="备注" width="400px" />
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

    <!-- element ui 官方推荐使用 v-model 双向绑定 而不是使用事件监听 -->
    <!-- 但是为了监听后还要调用相关函数，看来只能用事件了... -->
    <!-- 有没有办法让v-model的值发生改变时自动触发更新函数？ -->
    <el-pagination class="pagination" background layout="total, sizes, prev, pager, next, jumper" :total="total"
      :page-sizes="[2, 4, 6, 8]" v-model:current-page="pageData.page" v-model:page-size="pageData.pageSize"
      @current-change="handleCurrentChange" @size-change="handleSizeChange" />
  </el-card>
</template>


<style lang="less" scoped>
// element-plus的样式修改
.my-card{
  margin: 20px;
  padding: 20px;
  border-radius: 10px;
}

.el-dialog {
  width: 600px;
  border-radius: 10px;
}

.dialog-footer {
  display: flex;
  justify-content: center;
  margin: 20px 0;

  .el-button {
    width: 150px;
  }
}

.el-form {
  display: flex;
  flex-direction: column;
}

.el-table {
  width: 90%;
  height: 400px;
  margin: 3rem auto;
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
</style>