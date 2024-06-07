<script setup lang="ts">
import { reactive, ref } from 'vue'
import { getBookCategoryAPI } from '@/api/bookCategory'
import { getBookPageAPI, addBookAPI, updateBookStatusAPI, deleteBooksAPI } from '@/api/book'
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox, ElTable } from 'element-plus'

// ------ .d.ts 属性类型接口 ------
// 接收到不在接口中定义的属性的数据，ts会报错，但是类型推断错误不会妨碍接收，控制台还是能打印的
interface Book {
  id: number
  name: string
  categoryId: number
  author: string
  press: string
  publishDate: string
  price: number
  pageNumber: number
  keywords: string
  createTime: string
  status: number
  notes: string
}
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

// ------ 配置 ------
const dialogFormVisible = ref(false)
const formLabelWidth = '140px'

// ------ 数据 ------
// dialog中的表单信息
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
// 当前页的书籍列表
const bookList = ref<Book[]>([])
// 书籍id对应的分类列表，即categoryId字段不能只展示id值，应该根据id查询到对应的分类名进行回显
const categoryList = ref<Category[]>([])
// 分页参数
const pageData = reactive({
  name: '',
  categoryId: '',
  status: '',
  page: 1,
  pageSize: 6,
})
// // 输入框：模糊查询的关键词
// const input = ref()
// const inputRef = ref<HTMLInputElement | null>(null)
const isValidForm = ref()
const total = ref(0)
const options = [
  {
    value: '1',
    label: '已借出',
  },
  {
    value: '0',
    label: '未借出',
  }
]
const multiTableRef = ref<InstanceType<typeof ElTable>>()
const multiSelection = ref<Book[]>([])

// 表单校验
const rules = {
  name: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  categoryId: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  author: [
    { required: true, trigger: 'blur', message: '不能为空' },
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
// 页面初始化: 获取书籍分类列表
const init = async () => {
  const { data: res_category } = await getBookCategoryAPI({ page: 1, pageSize: 100 })
  console.log('分类列表')
  console.log(res_category.data)
  categoryList.value = res_category.data.records
  console.log('categoryList: ', categoryList.value)
}
// 刷新页面的分页数据
const showPageList = async () => {
  const { data: res } = await getBookPageAPI(pageData)
  console.log('书籍列表')
  console.log(res.data)
  bookList.value = res.data.records
  total.value = res.data.total
}
init() // 页面初始化，写在这里时的生命周期是beforecreated/created的时候
showPageList() // 页面一开始就要展示分页书籍列表

const handleCurrentChange = (val: any) => {
  console.log(`当前页: ${val}`)
  showPageList()
}
const handleSizeChange = (val: any) => {
  console.log(`每页 ${val} 条`)
  showPageList()
}
const handleSelectionChange = (val: Book[]) => {
  multiSelection.value = val
  console.log('value', val)
  console.log('multiSelection.value', multiSelection.value)
}

// 新增书籍
const add_btn = async () => {
  try {
    const valid = await isValidForm.value.validate()
    if (valid) {
      const date = new Date(form.publishDate)
      // 将日期转换为年月日格式(不能换行，空格和\n会被算进去...)
      form.publishDate = date.getFullYear() + '-' +
        String(date.getMonth() + 1).padStart(2, '0') + '-' +
        String(date.getDate()).padStart(2, '0')
      console.log('要提交的表单信息', form)
      const { data: res } = await addBookAPI(form)
      if (res.code == 1) return false
      console.log(res)
      dialogFormVisible.value = false
      // 提示新增成功
      ElMessage({
        message: '新增书籍成功！',
        type: 'success',
      })
      // 新增书籍后刷新页面，更新数据
      showPageList()
    } else {
      console.log('error submit!')
      return false;
    }
  } catch (error) {
    console.error('An error occurred during form validation:', error)
  }
}

// 模糊查询书籍列表
const search_btn = async () => {
  console.log('查询书籍的条件')
  console.log(pageData)
  let res = await getBookPageAPI(pageData)
  // 删除后刷新页面，更新数据
  console.log(res)
  bookList.value = res.data.data.records
  total.value = res.data.data.total
  showPageList()
}

// 修改书籍(路径传参，到update页面后，根据id查询书籍信息，回显到表单中)
const router = useRouter()
const update_btn = (row: any) => {
  console.log('要修改的行数据')
  console.log(row)
  router.push({
    name: 'bookUpdate',
    query: {
      id: row.id
    }
  })
}

// 修改书籍状态
const change_btn = async (row: any) => {
  console.log('要修改的行数据')
  console.log(row)
  await updateBookStatusAPI(row.id)
  // 修改后刷新页面，更新数据
  showPageList()
  ElMessage({
    type: 'success',
    message: '修改成功',
  })
}

// 删除书籍
const delete_btn = async (row: any) => {
  ElMessageBox.confirm(
    '是否删除该书籍？',
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
      await deleteBooksAPI(row.id)
      // 删除后刷新页面，更新数据
      showPageList()
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
// 批量删除书籍
const deleteBatch = (row?: any) => {
  console.log('要删除的行数据')
  console.log(row)
  ElMessageBox.confirm(
    '是否批量删除选中的书籍？',
    'Warning',
    {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  )
    .then(async () => {
      // 1. 没传入行数据，批量删除
      if (row == undefined) {
        console.log(multiSelection.value)
        if (multiSelection.value.length == 0) {
          ElMessage({
            type: 'warning',
            message: '请先选择要删除的书籍',
          })
          return
        }
        // 拿到当前 multiSelection.value 的所有id，然后调用批量删除接口
        let ids: any = []
        multiSelection.value.map(item => {
          ids.push(item.id)
        })
        ids = ids.join(',')
        console.log('ids', ids)
        let res = await deleteBooksAPI(ids)
        if (res.data.code != 0) return
      }
      // 2. 传入行数据，单个删除
      else {
        console.log('id包装成数组，然后调用批量删除接口')
        console.log(row.id)
        let res = await deleteBooksAPI(row.id)
        if (res.data.code != 0) return
      }
      // 删除后刷新页面，更新数据
      showPageList()
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
  <el-dialog v-model="dialogFormVisible" title="添加图书" class="my-info-dialog">
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
    <template #footer>
      <div class="dialog-footer">
        <el-button plain type="info" @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="add_btn()">确认</el-button>
      </div>
    </template>
  </el-dialog>

  <el-card class="my-card">
    <div class="horizontal">
      <el-input style="width: 160px; margin: 20px" v-model="pageData.name" class="input" placeholder="请输入书名" />
      <el-select style="width: 160px; margin: 20px" clearable v-model="pageData.categoryId" placeholder="选择分类类型">
        <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-select clearable v-model="pageData.status" placeholder="选择书籍状态" style="width: 160px; margin: 20px">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-button style="margin: 20px 30px; width:100px" round type="success" @click="search_btn()">查询书籍</el-button>
      <el-button style="margin: 20px 20px; width:100px" round type="danger" @click="deleteBatch()">批量删除</el-button>
      <el-button style="margin: 20px 20px; width:100px" type="primary" @click="dialogFormVisible = true">
        <el-icon>
          <Plus style="width: 10em; height: 10em; margin-right: 3px" />
        </el-icon>新增书籍
      </el-button>
    </div>

    <el-table ref="multiTableRef" :data="bookList" stripe border @selection-change="handleSelectionChange"
      style="width: 100%">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="书号" />
      <el-table-column prop="name" label="书名" width="100px" />
      <el-table-column prop="categoryId" label="所属分类" width="90">
        <template #default="scope">
          <!-- 遍历categoryList，找到categoryId对应的name，  !.表示必然存在id，因为添加时就是根据已有id添加的 -->
          {{ categoryList.find(item => item.id === scope.row.categoryId)!.name }}
        </template>
      </el-table-column>
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="press" label="出版社" width="150" />
      <el-table-column prop="publishDate" label="出版年" width="130px" />
      <el-table-column prop="price" label="价格" />
      <el-table-column prop="pageNumber" label="页码数" />
      <el-table-column prop="keywords" label="关键词" />
      <el-table-column prop="createTime" label="创建时间" width="150px" />
      <el-table-column prop="status" label="状态" />
      <el-table-column prop="notes" label="备注" width="200px" />
      <el-table-column label="操作" width="200px" fixed="right">
        <!-- scope 的父组件是 el-table -->
        <template #default="scope">
          <el-button @click="update_btn(scope.row)" type="primary">修改</el-button>
          <!-- <el-button @click="change_btn(scope.row)" type="primary">
            {{ scope.row.status === 1 ? '归还' : '借出' }}</el-button> -->
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
.el-dialog {
  width: 600px;
  border-radius: 10px;
}
/* 
.el-form {
  display: flex;
  flex-direction: column;
} */

.my-info-dialog .el-dialog__body {
  max-height: 350px;
  overflow: auto;
}
</style>