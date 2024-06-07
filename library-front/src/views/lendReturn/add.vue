<script setup lang="ts">
import { reactive, ref } from 'vue'
import { getBookCategoryAPI } from '@/api/bookCategory'
import { getReaderCategoryAPI } from '@/api/readerCategory'
import { getBookPageAPI } from '@/api/book'
import { getReaderPageAPI } from '@/api/reader'
import { addBorrowAPI } from '@/api/lendReturn'
import { ElMessage, ElTable } from 'element-plus'

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

// ------ 数据 ------
// 1、书籍
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
const bookList = ref<Book[]>([])
// 书籍id对应的分类列表，即categoryId字段不能只展示id值，应该根据id查询到对应的分类名进行回显
const bookCategoryList = ref<BookCategory[]>([])
// 书籍分页参数(只展示未借出的书籍)
const bookPageData = reactive({
  name: '',
  categoryId: '',
  status: 0,
  page: 1,
  pageSize: 6,
})
const bookTotal = ref(0)
const singleBookRef = ref<InstanceType<typeof ElTable>>()

// 2、读者
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
const readerList = ref<Reader[]>([])
// 读者id对应的分类列表，即categoryId字段不能只展示id值，应该根据id查询到对应的分类名进行回显
const readerCategoryList = ref<ReaderCategory[]>([])
// 读者分页参数
const readerPageData = reactive({
  name: '',
  categoryId: '',
  page: 1,
  pageSize: 6,
})
const readerTotal = ref(0)
const singleReaderRef = ref<InstanceType<typeof ElTable>>()

// 3、借还书
// 表单信息
const form = reactive<LendReturnDTO>({
  rId: 1,
  bId: 1,
  lendDate: '',
  returnDate: '',
  status: 0, // 默认0代表出借中
  notes: '',
})
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
  ]
}

// ------ 方法 ------
// 页面初始化: 获取书籍分类、读者分类列表
const init = async () => {
  const { data: bookCategory } = await getBookCategoryAPI({ page: 1, pageSize: 100 })
  bookCategoryList.value = bookCategory.data.records
  console.log('bookCategoryList: ', bookCategoryList.value)
  const { data: readerCategory } = await getReaderCategoryAPI({ page: 1, pageSize: 100 })
  readerCategoryList.value = readerCategory.data.records
  console.log('readerCategoryList: ', readerCategoryList.value)
}
// 刷新书籍分页数据
const showBookPage = async () => {
  console.log('根据该条件获取分页书籍列表', bookPageData)
  const { data: res } = await getBookPageAPI(bookPageData)
  console.log('书籍列表')
  console.log(res.data)
  bookList.value = res.data.records
  bookTotal.value = res.data.total
}
// 刷新读者分页数据
const showReaderPage = async () => {
  console.log('根据该条件获取分页读者列表', readerPageData)
  const { data: res } = await getReaderPageAPI(readerPageData)
  console.log('书籍列表')
  console.log(res.data)
  readerList.value = res.data.records
  readerTotal.value = res.data.total
}
init() // 页面初始化，写在这里时的生命周期是beforecreated/created的时候
showBookPage() // 页面一开始就要展示分页书籍列表
showReaderPage() // 页面一开始就要展示分页读者列表

const disabledDateLend = (time: Date) => {
  return time.getTime() > Date.now()
}

const bookCurrentChange = (val: any) => {
  console.log(`当前页: ${val}`)
  showBookPage()
}
// 单选书籍
const singleBookChange = (val: Book) => {
  selectedBook.value = val
}
// 清空书籍单选状态
const clearBook = (row?: Book) => {
  singleBookRef.value!.setCurrentRow(row)
}
const readerCurrentChange = (val: any) => {
  console.log(`当前页: ${val}`)
  showReaderPage()
}
// 单选读者
const singleReaderChange = (val: Reader) => {
  selectedReader.value = val
}
// 清空读者单选状态
const clearReader = (row?: Reader) => {
  singleReaderRef.value!.setCurrentRow(row)
}


// 模糊查询书籍列表
const search_book_btn = async () => {
  console.log('查询书籍的条件')
  console.log(bookPageData)
  const { data: res } = await getBookPageAPI(bookPageData)
  // 删除后刷新页面，更新数据
  console.log(res)
  bookList.value = res.data.records
  bookTotal.value = res.data.total
  showBookPage()
}
// 模糊查询读者列表
const search_reader_btn = async () => {
  console.log('查询读者的条件')
  console.log(readerPageData)
  const { data: res } = await getReaderPageAPI(readerPageData)
  // 删除后刷新页面，更新数据
  console.log(res)
  readerList.value = res.data.records
  readerTotal.value = res.data.total
  showBookPage()
}

// 新增借还书记录
const add_btn = async () => {
  try {
    const valid = await isValidForm.value.validate()
    console.log('新增借还书记录,提交的表单form: ', form)
    if (valid) {
      if (form.returnDate && form.lendDate > form.returnDate) {
        ElMessage({
          message: '还书日期不能早于借书日期！',
          type: 'error',
        })
        return false
      }
      // 将书籍id和读者id赋值给form
      form.bId = selectedBook.value.id
      form.rId = selectedReader.value.id
      if (!form.bId || !form.rId) {
        ElMessage({
          message: '必须先选择借书人和书籍！',
          type: 'error',
        })
        return false
      }
      // 将日期转换为年月日格式(不能换行，空格和\n会被算进去...)
      let date = new Date(form.lendDate)
      form.lendDate = date.getFullYear() + '-' +
        String(date.getMonth() + 1).padStart(2, '0') + '-' +
        String(date.getDate()).padStart(2, '0')
      if (form.returnDate) {
        date = new Date(form.returnDate)
        form.returnDate = date.getFullYear() + '-' +
          String(date.getMonth() + 1).padStart(2, '0') + '-' +
          String(date.getDate()).padStart(2, '0')
      }
      const { data: res } = await addBorrowAPI(form)
      if (res.code == 1) return false
      console.log(res)
      // 提示新增成功
      ElMessage({
        message: '新增借还书记录成功！',
        type: 'success',
      })
      // 新增后刷新页面，更新数据
      showBookPage()
    } else {
      console.log('error submit!')
      return false;
    }
  } catch (error) {
    console.error('An error occurred during form validation:', error)
  }
}
</script>

<template>
  <el-card class="my-card">
    <div class="horizontal">
      <el-form :model="form" :rules="rules" ref="isValidForm" class="my-form">
        <el-form-item label="借书人编号" label-width="100px" prop="rId">
          <div class="selected">{{ selectedReader?.id }}</div>
        </el-form-item>
        <el-form-item label="书籍编号" label-width="100px" prop="bId">
          <div class="selected">{{ selectedBook?.id }}</div>
        </el-form-item>
        <el-form-item label="借书日期" label-width="100px" prop="lendDate">
          <el-date-picker v-model="form.lendDate" type="date" :disabled-date="disabledDateLend" placeholder="请选择借书日期"
            style="width: 100%" />
        </el-form-item>
        <!-- <el-form-item label="还书日期" label-width="100px" prop="returnDate">
          <el-date-picker v-model="form.returnDate" type="date" placeholder="请选择还书日期" style="width: 100%" />
        </el-form-item> -->
        <el-button style="margin: 0 60px 15px; width:120px" round type="primary" @click="add_btn()">添加借阅记录</el-button>
      </el-form>
    </div>

    <div class="reader">
      <div class="horizontal">
        <el-input style="width: 120px; margin: 20px" v-model="readerPageData.name" class="input" placeholder="请输入读者名" />
        <el-select style="width: 150px; margin: 20px" clearable v-model="readerPageData.categoryId"
          placeholder="选择分类类型">
          <el-option v-for="item in readerCategoryList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-button style="margin: 20px 30px; width:100px" round type="success"
          @click="search_reader_btn()">查询读者</el-button>
      </div>

      <el-table ref="singleReaderRef" :data="readerList" highlight-current-row stripe border
        @current-change="singleReaderChange" style="width: 90%; height: 350px;">
        <el-table-column prop="id" label="读者号" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="categoryId" label="所属分类">
          <template #default="scope">
            <!-- 遍历categoryList，找到categoryId对应的name，  !.表示必然存在id，因为添加时就是根据已有id添加的 -->
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
      <div style="margin-top: 20px">
        <el-button type="info" @click="clearReader()">清除单选读者</el-button>
      </div>
      <el-pagination class="pagination" layout="total, prev, pager, next, jumper" :total="readerTotal" :page-size="6"
        v-model:current-page="readerPageData.page" @current-change="readerCurrentChange" />
    </div>

    <div class="book">
      <div class="horizontal">
        <el-input style="width: 120px; margin: 10px" v-model="bookPageData.name" class="input" placeholder="请输入书名" />
        <el-select style="width: 150px; margin: 10px" clearable v-model="bookPageData.categoryId" placeholder="选择分类类型">
          <el-option v-for="item in bookCategoryList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-button style="margin: 20px 30px; width:100px" round type="success"
          @click="search_book_btn()">查询书籍</el-button>
      </div>

      <el-table ref="singleBookRef" :data="bookList" highlight-current-row stripe border
        @current-change="singleBookChange" style="width: 90%; height: 350px; justify-content: center">
        <el-table-column prop="id" label="书号" />
        <el-table-column prop="name" label="书名" />
        <el-table-column prop="categoryId" label="所属分类">
          <template #default="scope">
            <!-- 遍历categoryList，找到categoryId对应的name，  !.表示必然存在id，因为添加时就是根据已有id添加的 -->
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
        <el-table-column prop="price" label="价格" />
        <el-table-column prop="keywords" label="关键词" />
        <template #empty>
          <el-empty description=" 没有数据" />
        </template>
      </el-table>
      <div style="margin-top: 20px">
        <el-button type="info" @click="clearBook()">清除单选书籍</el-button>
      </div>
      <el-pagination class="pagination" layout="total, prev, pager, next, jumper" :total="bookTotal" :page-size="6"
        v-model:current-page="bookPageData.page" @current-change="bookCurrentChange" />
    </div>

  </el-card>
</template>

<style scoped>
.horizontal {
  margin-top: 20px;
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
  width: 60px;
  height: 30px;
  line-height: 30px;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 0 10px;
  margin: 0 10px;
}

.my-form{
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-left: 100px;
}

.el-table {
  margin: 0 auto;
}
</style>

<style>
.my-info-dialog .el-dialog__body {
  max-height: 350px;
  overflow: auto;
}
</style>