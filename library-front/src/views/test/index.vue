<script setup lang="ts">
import { reactive, ref } from 'vue'
import { getBookPageList } from '@/api/book'
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus'

// 配置
const dialogFormVisible = ref(false)
const formLabelWidth = '140px'

// 响应式数据对象
// reactive一般是对象，如果是对象数组，需要作为对象的一个属性arr，并在外层包裹一层对象
// 套一层还有个好处：不会丢失响应式，因为重新赋值的是 info.arr 而不是 info， info 的引用地址不变
// 列表信息
const bookList = reactive({
  arr: []
})
// 表单信息
const form = reactive({
  id: '',
  category: '',
  title: '',
  press: '',
  book_year: '',
  author: '',
  price: '',
  book_total: ''
})
const inputRef = ref<HTMLInputElement | null>(null)
const isValidForm = ref()
const currentPage = ref(1)
const pageSize = ref(2)
const total = ref()

// 表单校验
const rules = {
  id: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  category: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  title: [
    { required: true, trigger: 'blur', message: '不能为空' }
  ],
  press: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  book_year: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  author: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ],
  price: [
    { required: true, trigger: 'blur', message: '不能为空' }
  ],
  book_total: [
    { required: true, trigger: 'blur', message: '不能为空' },
  ]
}

// 初始化时展示所有员工的列表
const init = async () => {
  let resultBook = await getBookPageList({ page: currentPage.value, pageSize: pageSize.value })   // 分页加载员工
  console.log(resultBook)
  bookList.arr = resultBook.data.data.records // 后端数组数据作为reactive对象的一个属性arr的值
  total.value = resultBook.data.data.total
  console.log(bookList)
}
// 初始化
init()

const handleCurrentChange = (val: any) => {
  console.log(`当前页: ${val}`);
  init()
}
const handleSizeChange = (val: any) => {
  console.log(`每页 ${val} 条`);
  init()
}
</script>

<template>
  <el-button plain @click="dialogFormVisible = true">
    新增员工
  </el-button>
  <!-- <el-dialog v-model="dialogFormVisible" title="Shipping address" width="500">
    <el-form :model="form" :rules="rules" ref="isValidForm">
      <el-form-item label="name" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="age" :label-width="formLabelWidth" prop="age">
        <el-input v-model="form.age" autocomplete="off" />
      </el-form-item>
      <el-form-item label="gender" :label-width="formLabelWidth" prop="gender">
        <el-select v-model="form.gender" placeholder="请选择性别">
          <el-option label="男" value="1" />
          <el-option label="女" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="phone" :label-width="formLabelWidth" prop="phone">
        <el-input v-model="form.phone" autocomplete="off" />
      </el-form-item>
      <el-form-item label="pic" :label-width="formLabelWidth" prop="pic">
        <img class="the_img" v-if="!form.pic" src="../../assets/image/user_default.png" alt="" />
        <img class="the_img" v-else :src="form.pic" alt="" />
        <input type="file" accept="image/*" style="display: none" ref="inputRef" @change="onFileChange" />
        <el-button type="primary" icon="el-icon-plus" @click="chooseImg">选择图片</el-button>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="add_btn()">
          Confirm
        </el-button>
      </div>
    </template>
  </el-dialog> -->
  <el-table :data="bookList.arr" style="width: 100%">
    <el-table-column prop="id" label="书号" />
    <el-table-column prop="category" label="类别" />
    <el-table-column prop="title" label="书名" />
    <el-table-column prop="press" label="出版社" />
    <el-table-column prop="book_year" label="出版年" />
    <el-table-column prop="author" label="作者" />
    <el-table-column prop="price" label="价格" />
    <el-table-column prop="book_total" label="数量" />
    <el-table-column label="操作">
      <template>
        <el-button type="primary">修改</el-button>
        <el-button type="primary">删除</el-button>
      </template>
    </el-table-column>
    <template #empty>
      <el-empty description=" 没有数据" />
    </template>
  </el-table>
  <!-- element ui 官方推荐使用 v-model 双向绑定 而不是使用事件监听 -->
  <!-- 但是为了监听后还要调用相关函数，看来只能用事件了... -->
  <!-- 有没有办法让v-model的值发生改变时自动触发更新函数？ -->
  <el-pagination class="page" background layout="total, sizes, prev, pager, next, jumper" :total="total"
    :page-sizes="[2, 4, 6, 8]" v-model:current-page="currentPage" v-model:page-size="pageSize"
    @current-change="handleCurrentChange" @size-change="handleSizeChange" />
  <div>当前页为：{{ currentPage }}</div>
</template>

<style scoped>
.page {
  justify-content: center;
}
</style>