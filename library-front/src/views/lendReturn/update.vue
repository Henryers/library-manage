<script lang="ts" setup>
import { reactive, ref } from 'vue'
import { getBookCategoryAPI } from '@/api/bookCategory'
import { getReaderCategoryAPI } from '@/api/readerCategory'
import { getBookByIdAPI } from '@/api/book'
import { getReaderByIdAPI } from '@/api/reader'
import { getBorrowByIdAPI, updateBorrowAPI } from '@/api/lendReturn'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

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
interface BookCategory {
  id: number
  name: string
}
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
interface ReaderCategory {
  id: number
  name: string
}
interface LendReturnDTO {
  rId: number
  bId: number
  lendDate: string
  returnDate: string
  status: number
  notes: string
}

// 1、读者
// 当前选择的读者，防止未定义报错，要先给个初始值
const selectedReader = ref<Reader>({
  id: 0,
  name: '',
  categoryId: 1,
  sex: 1,
  wAddress: '',
  hAddress: '',
  phone: '',
  email: '',
  createTime: '',
  notes: ''
})
// 当前页的读者列表
// const readerList = ref<Reader[]>([])
// 读者id对应的分类列表，即categoryId字段不能只展示id值，应该根据id查询到对应的分类名进行回显
const readerCategoryList = ref<ReaderCategory[]>([])
// 读者分页参数
// const readerPageData = reactive({
//   name: '',
//   categoryId: '',
//   page: 1,
//   pageSize: 6,
// })
// const readerTotal = ref(0)

// 2、书籍
// 当前选择的书籍，防止未定义报错，要先给个初始值
const selectedBook = ref<Book>({
  id: 0,
  name: '',
  categoryId: 1,
  author: '',
  press: '',
  publishDate: '',
  price: 0,
  pageNumber: 0,
  keywords: '',
  createTime: '',
  status: 0,
  notes: ''
})
// 当前页的书籍列表
// const bookList = ref<Book[]>([])
// 书籍id对应的分类列表，即categoryId字段不能只展示id值，应该根据id查询到对应的分类名进行回显
const bookCategoryList = ref<BookCategory[]>([])
// 书籍分页参数(只展示未借出的书籍)，因为你不能借已经借出的书籍
// 如果不小心点到了其他未借出的书籍而改不回来？退出不要改不就行了！
// const bookPageData = reactive({
//   name: '',
//   categoryId: '',
//   status: 0,
//   page: 1,
//   pageSize: 6,
// })
// const bookTotal = ref(0)

const id = ref()
// 表单信息
const form = reactive<LendReturnDTO>({
  rId: 1,
  bId: 1,
  lendDate: '',
  returnDate: '',
  status: 1, //  0: 出借中  1：正常归还  2：逾期归还  3：丢失无法归还  4：损坏归还  5：其他(备注说明)
  notes: '',
})
const statusList = [
  { label: '出借中', value: 0 },
  { label: '正常归还', value: 1 },
  { label: '逾期归还', value: 2 },
  { label: '丢失无法归还', value: 3 },
  { label: '损坏归还', value: 4 },
  { label: '其他(备注说明)', value: 5 },
]
const isValidForm = ref()

// 表单校验
const rules = {
  rId: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  bId: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  lendDate: [
    { required: true, trigger: 'blur', message: '不能为空' }
  ],
  returnDate: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ]
}
const router = useRouter()
const route = useRoute()

// ------ 方法 ------
// 页面初始化:
const init = async () => {
  // 1、获取书籍分类、读者分类列表
  const { data: bookCategory } = await getBookCategoryAPI({ page: 1, pageSize: 100 })
  bookCategoryList.value = bookCategory.data.records
  console.log('bookCategoryList: ', bookCategoryList.value)
  const { data: readerCategory } = await getReaderCategoryAPI({ page: 1, pageSize: 100 })
  readerCategoryList.value = readerCategory.data.records
  console.log('readerCategoryList: ', readerCategoryList.value)
  // 2、根据路径中的id获取借还书记录信息，回显到表单中
  console.log(route.query)
  if (route.query) {
    id.value = route.query.id || 0
    const { data: res } = await getBorrowByIdAPI(id.value)
    Object.assign(form, res.data)
    console.log('回显的lendReturn表单', form)
    let date = new Date(form.lendDate)
    form.lendDate = date.toDateString()
    selectedBook.value.id = res.data.bId
    selectedReader.value.id = res.data.rId
    // 还要根据书籍id和读者id查询书籍和读者信息
    const { data: book } = await getBookByIdAPI(selectedBook.value.id)
    Object.assign(selectedBook.value, book.data)
    const { data: reader } = await getReaderByIdAPI(selectedReader.value.id)
    Object.assign(selectedReader.value, reader.data)
  } else {
    console.log('没有id')
  }
}
// 刷新书籍分页数据
// const showBookPage = async () => {
//   // 就只展示一条书籍记录
//   console.log('根据该条件获取分页书籍列表', bookPageData)
//   const { data: res } = await getBookPageAPI(bookPageData)
//   console.log('书籍列表')
//   console.log(res.data)
//   bookList.value = res.data.records
//   bookTotal.value = res.data.total
// }
// // 刷新读者分页数据
// const showReaderPage = async () => {
//   console.log('根据该条件获取分页读者列表', readerPageData)
//   const { data: res } = await getReaderPageAPI(readerPageData)
//   console.log('读者列表')
//   console.log(res.data)
//   readerList.value = res.data.records
//   readerTotal.value = res.data.total
// }
init() // 页面初始化，写在这里时的生命周期是beforecreated/created的时候
// showBookPage() // 页面一开始就要展示分页书籍列表
// showReaderPage() // 页面一开始就要展示分页读者列表

// onMounted(async() => {
//   console.log('onMounted')
//   await init() // 页面初始化，写在这里时的生命周期是mounted的时候
//   await showBookPage() // 页面一开始就要展示分页书籍列表
//   await showReaderPage() // 页面一开始就要展示分页读者列表
// })

const disabledDateLend = (time: Date) => {
  return time.getTime() > Date.now()
}
const disabledDateReturn = (time: Date) => {
  return time.getTime() > Date.now() || time.getTime() < new Date(form.lendDate).getTime()
}
// const bookCurrentChange = (val: any) => {
//   console.log(`当前页: ${val}`)
//   showBookPage()
// }
// 单选书籍
// const singleBookChange = (val: Book) => {
//   selectedBook.value = val
// }
// const readerCurrentChange = (val: any) => {
//   console.log(`当前页: ${val}`)
//   showBookPage()
// }
// 单选读者
// const singleReaderChange = (val: Reader) => {
//   selectedReader.value = val
// }

// 模糊查询书籍列表
// const search_book_btn = async () => {
//   console.log('查询书籍的条件')
//   console.log(bookPageData)
//   const { data: res } = await getBookPageAPI(bookPageData)
//   // 删除后刷新页面，更新数据
//   console.log(res)
//   bookList.value = res.data.records
//   bookTotal.value = res.data.total
//   showBookPage()
// }
// 模糊查询读者列表
// const search_reader_btn = async () => {
//   console.log('查询读者的条件')
//   console.log(readerPageData)
//   const { data: res } = await getReaderPageAPI(readerPageData)
//   // 删除后刷新页面，更新数据
//   console.log(res)
//   readerList.value = res.data.records
//   readerTotal.value = res.data.total
//   showBookPage()
// }

// 修改借还书记录后提交
const submit = async () => {
  try {
    const valid = await isValidForm.value.validate()
    if (valid) {
      console.log('submit successfully!')
      if (form.status == 0) {
        ElMessage({
          message: '归还书籍，状态不能为出借中！',
          type: 'error',
        })
        return false
      } else if (form.status == 5 && form.notes == '') {
        ElMessage({
          message: '选择其他状态时，请在备注中说明原因！',
          type: 'error',
        })
        return false
      }
      // if (form.lendDate > form.returnDate) {
      //   ElMessage({
      //     message: '还书日期不能早于借书日期！',
      //     type: 'error',
      //   })
      //   return false
      // }
      // 将书籍id和读者id赋值给form
      form.bId = selectedBook.value.id
      form.rId = selectedReader.value.id
      // 将日期转换为年月日格式(不能换行，空格和\n会被算进去...)
      let date = new Date(form.lendDate)
      form.lendDate = date.getFullYear() + '-' +
        String(date.getMonth() + 1).padStart(2, '0') + '-' +
        String(date.getDate()).padStart(2, '0')
      date = new Date(form.returnDate)
      form.returnDate = date.getFullYear() + '-' +
        String(date.getMonth() + 1).padStart(2, '0') + '-' +
        String(date.getDate()).padStart(2, '0')

      const { data: res } = await updateBorrowAPI(form)
      if (res.code == 1) return false
      // 然后进行 消息提示，页面跳转 等操作
      ElMessage({
        message: '修改成功！',
        type: 'success',
      })
      router.push({
        path: '/lendReturn',
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
    path: '/lendReturn',
  })
}
</script>

<template>
  <el-card class="my-card">
    <div class="horizontal">
      <el-form :model="form" :rules="rules" ref="isValidForm" class="my-form">
        <!-- 顶部form第一行 -->
        <el-row :gutter="30">
          <el-form-item label="借书人编号" label-width="120px" prop="rId">
            <div class="selected">{{ selectedReader?.id }}</div>
          </el-form-item>
          <el-form-item label="书籍编号" label-width="120px" prop="bId">
            <div class="selected">{{ selectedBook?.id }}</div>
          </el-form-item>
          <el-form-item label="借书日期" label-width="110px" prop="lendDate">
            <el-date-picker v-model="form.lendDate" type="date" :disabled-date="disabledDateLend" placeholder="请选择借书日期"
              style="width: 100%" />
          </el-form-item>
        </el-row>
        <!-- 顶部form第二行 -->
        <el-row :gutter="30">
          <el-form-item label="还书日期" label-width="108px" prop="returnDate">
            <el-date-picker v-model="form.returnDate" type="date" :disabled-date="disabledDateReturn"
              placeholder="请选择还书日期" style="width: 100%" />
          </el-form-item>
          <el-form-item label="归还状态" label-width="100px" prop="status">
            <el-select style="width: 160px;" v-model="form.status">
              <el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="备注" label-width="70px" prop="notes">
            <el-input v-model="form.notes" autocomplete="off" />
          </el-form-item>
        </el-row>
      </el-form>
      <el-button type="success" @click="submit">还书</el-button>
      <el-button plain type="info" @click="cancel">取消</el-button>
    </div>

    <el-table :data="[selectedReader]" highlight-current-row stripe border style="width: 90%; height: 120px;">
      <el-table-column prop="id" label="读者号" width="80px" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="categoryId" label="所属分类">
        <template #default="scope">
          <span v-if="readerCategoryList.length > 0">
            {{ readerCategoryList.find(item => item.id === scope.row.categoryId)!.name }}
          </span>
          <span v-else>
            数据加载中...
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="sex" label="性别" width="80px">
        <template #default="scope">
          {{ scope.row.sex === 1 ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column prop="wAddress" label="工作单位" />
      <el-table-column prop="hAddress" label="家庭住址" />
      <el-table-column prop="phone" label="电话" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="notes" label="备注" />
      <template #empty>
        <el-empty description=" 没有数据" />
      </template>
    </el-table>
    <el-table :data="[selectedBook]" highlight-current-row stripe border
      style="width: 90%; height: 120px; justify-content: center">
      <el-table-column prop="id" label="书号" width="80px" />
      <el-table-column prop="name" label="书名" />
      <el-table-column prop="categoryId" label="所属分类">
        <template #default="scope">
          <span v-if="bookCategoryList.length > 0">
            {{ bookCategoryList.find(item => item.id === scope.row.categoryId)!.name }}
          </span>
          <span v-else>
            数据加载中...
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="press" label="出版社" />
      <el-table-column prop="publishDate" label="出版年" width="130px" />
      <el-table-column prop="price" label="价格" />
      <el-table-column prop="pageNumber" label="页码总数" />
      <el-table-column prop="keywords" label="关键词" />
      <el-table-column prop="createTime" label="创建时间" width="150px" />
      <template #empty>
        <el-empty description=" 没有数据" />
      </template>
    </el-table>

    <!-- <div class="reader">
      <div class="horizontal">
        <el-input style="width: 120px; margin: 20px" v-model="readerPageData.name" class="input" placeholder="请输入读者名" />
        <el-select style="width: 150px; margin: 20px" clearable v-model="readerPageData.categoryId"
          placeholder="选择分类类型">
          <el-option v-for="item in readerCategoryList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-button style="margin: 20px 30px; width:100px" round type="primary"
          @click="search_reader_btn()">查询读者</el-button>
      </div>

      <el-table :data="readerList" highlight-current-row stripe border @current-change="singleReaderChange"
        style="width: 90%; height: 400px;">
        <el-table-column prop="id" label="读者号" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="categoryId" label="所属分类">
          <template #default="scope">
            <span v-if="readerCategoryList.length > 0">
              {{ readerCategoryList.find(item => item.id === scope.row.categoryId)!.name }}
            </span>
            <span v-else>
              数据加载中...
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="sex" label="性别">
          <template #default="scope">
            {{ scope.row.sex === 1 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话" />
        <el-table-column prop="notes" label="备注" />
        <template #empty>
          <el-empty description=" 没有数据" />
        </template>
      </el-table>

      <el-pagination class="pagination" layout="total, prev, pager, next, jumper" :total="readerTotal" :page-size="6"
        v-model:current-page="bookPageData.page" @current-change="readerCurrentChange" />
    </div>

    <div class="book">
      <div class="horizontal">
        <el-input style="width: 120px; margin: 10px" v-model="bookPageData.name" class="input" placeholder="请输入书名" />
        <el-select style="width: 150px; margin: 10px" clearable v-model="bookPageData.categoryId" placeholder="选择分类类型">
          <el-option v-for="item in bookCategoryList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-button style="margin: 20px 30px; width:80px" round type="primary"
          @click="search_book_btn()">查询书籍</el-button>
      </div>

      <el-table :data="bookList" highlight-current-row stripe border @current-change="singleBookChange"
        style="width: 90%; height: 400px; justify-content: center">
        <el-table-column prop="id" label="书号" />
        <el-table-column prop="name" label="书名" />
        <el-table-column prop="categoryId" label="所属分类">
          <template #default="scope">
            <span v-if="bookCategoryList.length > 0">
              {{ bookCategoryList.find(item => item.id === scope.row.categoryId)!.name }}
            </span>
            <span v-else>
              数据加载中...
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="author" label="作者" />
        <el-table-column prop="press" label="出版社" />
        <el-table-column prop="publishDate" label="出版年" width="130px" />
        <el-table-column prop="price" label="价格" />
        <el-table-column prop="pageNumber" label="页码总数" />
        <el-table-column prop="keywords" label="关键词" />
        <el-table-column prop="createTime" label="创建时间" width="150px" />
        <template #empty>
          <el-empty description=" 没有数据" />
        </template>
      </el-table>

      <el-pagination class="pagination" layout="total, prev, pager, next, jumper" :total="bookTotal" :page-size="6"
        v-model:current-page="bookPageData.page" @current-change="bookCurrentChange" />
    </div> -->
  </el-card>
</template>

<style scoped>
.horizontal {
  margin-top: 20px;
}

.my-form {
  display: flex;
  flex-direction: column;
  width: 80%;
  margin: 0 auto;
  border-radius: 10px;
}

.el-table {
  margin: 20px auto;
}

.book {
  display: inline-block;
  width: 50%;
}

.reader {
  display: inline-block;
  width: 50%;
}

.selected {
  display: inline-block;
  width: 120px;
  height: 30px;
  line-height: 30px;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 0 10px;
  margin: 0 10px;
}
</style>

<style></style>
