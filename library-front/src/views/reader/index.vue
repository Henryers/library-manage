<script setup lang="ts">
import { reactive, ref } from 'vue'
import { getReaderCategoryAPI } from '@/api/readerCategory'
import { getReaderPageAPI, addReaderAPI, deleteReadersAPI } from '@/api/reader'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, ElTable } from 'element-plus'

// ------ .d.ts 属性类型接口 ------
// 接收到不在接口中定义的属性的数据，ts会报错，但是类型推断错误不会妨碍接收，控制台还是能打印的
interface Reader {
  id: number
  name: string
  categoryId: number
  sex: number
  wAddress: string
  hAddress: string
  phone: string
  email: string
  createTime: string
  notes: string
}
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

// 配置
const dialogFormVisible = ref(false)
const formLabelWidth = '140px'

// ------ 数据 ------
// dialog中的表单信息
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
// 当前页的书籍列表
const readerList = ref<Reader[]>([])
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
const total = ref(0)
const multiTableRef = ref<InstanceType<typeof ElTable>>()
const multiSelection = ref<Reader[]>([])

// 表单校验
const rules = {
  name: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  categoryId: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  sex: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  wAddress: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  hAddress: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  phone: [
    { required: true, trigger: 'blur', message: '不能为空' },
    { pattern: /^[1][3,4,5,7,8][0-9]{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' },
  ],
  email: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  notes: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ]
}

// 初始化时拿到分类列表，得到 id-name 对应关系
const init = async () => {
  const { data: res_category } = await getReaderCategoryAPI({ page: 1, pageSize: 100 })   // 分页加载员工
  console.log(res_category)
  categoryList.value = res_category.data.records
  console.log('categoryList: ', categoryList.value)
}
// 刷新页面的分页数据
const showPageList = async () => {
  console.log('根据该条件获取分页书籍列表', pageData)
  const { data: res } = await getReaderPageAPI(pageData)
  readerList.value = res.data.records
  console.log('书籍列表', readerList.value)
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
const handleSelectionChange = (val: Reader[]) => {
  multiSelection.value = val
  console.log('value', val)
  console.log('multiSelection.value', multiSelection.value)
}

// 新增借阅人
const add_btn = async () => {
  try {
    const valid = await isValidForm.value.validate()
    if (valid) {
      dialogFormVisible.value = false
      const { data: res } = await addReaderAPI(form)
      if (res.code == 1) return false
      console.log(res)
      // 提示新增成功
      ElMessage({
        message: '新增借阅人成功！',
        type: 'success',
      })
      // 新增借阅人后刷新页面，更新数据
      showPageList()
    } else {
      console.log('error submit!')
      return false;
    }
  } catch (error) {
    console.error('An error occurred during form validation:', error)
  }
}

// 模糊查询读者列表
const search_btn = async () => {
  console.log('查询书籍的条件')
  console.log(pageData)
  const { data: res } = await getReaderPageAPI(pageData)
  // 删除后刷新页面，更新数据
  console.log(res)
  readerList.value = res.data.records
  total.value = res.data.total
  showPageList()
}

// 修改借阅人(路径传参，到update页面后，根据id查询借阅人信息，回显到表单中)
const router = useRouter()
const update_btn = (row: any) => {
  console.log('要修改的行数据')
  console.log(row)
  router.push({
    name: 'readerUpdate',
    query: {
      id: row.id
    }
  })
}

// 删除借阅人
const delete_btn = async (row: any) => {
  ElMessageBox.confirm(
    '是否删除该借阅人？',
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
      await deleteReadersAPI(row.id)
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

// 批量删除借阅人
const deleteBatch = (row?: any) => {
  console.log('要删除的行数据')
  console.log(row)
  ElMessageBox.confirm(
    '是否批量删除选中的读者？',
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
            message: '请先选择要删除的读者',
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
        let res = await deleteReadersAPI(ids)
        if (res.data.code != 0) return
      }
      // 2. 传入行数据，单个删除
      else {
        console.log('id包装成数组，然后调用批量删除接口')
        console.log(row.id)
        let res = await deleteReadersAPI(row.id)
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
  <el-dialog v-model="dialogFormVisible" title="添加读者" width="500">
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
    <template #footer>
      <div class="dialog-footer">
        <el-button plain type="info" @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="add_btn()">确认</el-button>
      </div>
    </template>
  </el-dialog>

  <el-card class="my-card">
    <div class="horizontal">
      <el-input style="width: 160px; margin: 20px" v-model="pageData.name" class="input" placeholder="请输入读者名" />
      <el-select style="width: 160px; margin: 20px" clearable v-model="pageData.categoryId" placeholder="选择分类类型">
        <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-button style="margin: 20px 30px; width:100px" round type="success" @click="search_btn()">查询读者</el-button>
      <el-button style="margin: 20px 20px; width:100px" round type="danger" @click="deleteBatch()">批量删除</el-button>
      <el-button style="margin: 20px 20px; width:100px" type="primary" @click="dialogFormVisible = true">
        <el-icon>
          <Plus style="width: 10em; height: 10em; margin-right: 3px" />
        </el-icon>新增读者
      </el-button>
    </div>

    <el-table ref="multiTableRef" :data="readerList" stripe border @selection-change="handleSelectionChange"
      style="width: 100%">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="读者号" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="categoryId" label="所属分类" width="100px">
        <template #default="scope">
          <!-- 遍历categoryList，找到categoryId对应的name，  !.表示必然存在id，因为添加时就是根据已有id添加的 -->
          {{ categoryList.find(item => item.id === scope.row.categoryId)!.name }}
        </template>
      </el-table-column>
      <el-table-column prop="sex" label="性别">
        <template #default="scope">
          {{scope.row.sex === 1 ? '男' : '女'}}
        </template>
      </el-table-column>
      <el-table-column prop="wAddress" label="工作单位" width="120px" />
      <el-table-column prop="hAddress" label="家庭住址" width="120px" />
      <el-table-column prop="phone" label="电话" width="120px" />
      <el-table-column prop="email" label="邮箱" width="120px" />
      <el-table-column prop="createTime" label="创建时间" width="120px" />
      <el-table-column prop="notes" label="备注" width="150px" />
      <el-table-column label="操作" width="200px" fixed="right">
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
