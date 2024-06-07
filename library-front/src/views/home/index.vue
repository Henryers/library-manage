<script lang="ts" setup>
import { reactive, ref, onMounted, watchEffect } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import {
  getBookCategoryDataAPI, getReaderCategoryDataAPI, getLendDataAPI,
  getReturnDataAPI, getBookTopDataAPI, getReaderTopDataAPI
} from '@/api/data'
import * as echarts from 'echarts'

type Pie = { value: number, name: string }

// ------ 数据 ------
const daysOptions = [
  {
    value: 7,
    label: '近7天',
  },
  {
    value: 14,
    label: '近14天',
  },
  {
    value: 30,
    label: '近30天',
  },
]
const topOptions = [
  {
    value: 7,
    label: '周榜',
  },
  {
    value: 30,
    label: '月榜',
  },
  {
    value: 365,
    label: '年榜',
  },
  {
    value: 3000,
    label: '总榜',
  },
]
const lendDays = ref(7)
const lend_data_x = ref()
const lend_data_y = ref()

const returnDays = ref(7)
const return_data_x = ref()
const return_data_y = ref()

const book_pie_data = ref<Pie[]>([])
const reader_pie_data = ref<Pie[]>([])

const bookDays = ref(7)
const book_top_x = ref()
const book_top_y = ref()

const readerDays = ref(7)
const reader_top_x = ref()
const reader_top_y = ref()

// ------ 方法 ------
// 获取统计数据
const getBookCategoryData = async () => {
  const { data: res } = await getBookCategoryDataAPI()
  console.log('书籍分类', res)
  const book_pie_x = res.data.bookCategoryNames.split(",")
  const book_pie_y = res.data.bookCategoryNums.split(",")
  for (let i = 0; i < book_pie_x.length; i++) {
    book_pie_data.value.push({ value: book_pie_y[i], name: book_pie_x[i] })
  }
  console.log('book_pie_data', book_pie_data.value)
}
const getReaderCategoryData = async () => {
  const { data: res } = await getReaderCategoryDataAPI()
  console.log('读者分类', res)
  const reader_pie_x = res.data.readerCategoryNames.split(",")
  const reader_pie_y = res.data.readerCategoryNums.split(",")
  for (let i = 0; i < reader_pie_x.length; i++) {
    reader_pie_data.value.push({ value: reader_pie_y[i], name: reader_pie_x[i] })
  }
  console.log('reader_pie_data', reader_pie_data.value)
}
const getLendData = async () => {
  const { data: res } = await getLendDataAPI(lendDays.value)
  console.log('借书统计', res)
  lend_data_x.value = res.data.dateList.split(",")
  lend_data_y.value = res.data.lendOrReturnList.split(",")
}
const getReturnData = async () => {
  const { data: res } = await getReturnDataAPI(returnDays.value)
  console.log('还书统计', res)
  return_data_x.value = res.data.dateList.split(",")
  return_data_y.value = res.data.lendOrReturnList.split(",")
}
const getBookTopData = async () => {
  const { data: res } = await getBookTopDataAPI(bookDays.value)
  console.log('书籍人气Top', res)
  book_top_x.value = res.data.nameList.split(",")
  book_top_y.value = res.data.topList.split(",")
}
const getReaderTopData = async () => {
  const { data: res } = await getReaderTopDataAPI(readerDays.value)
  console.log('读者借书排行榜Top', res)
  reader_top_x.value = res.data.nameList.split(",")
  reader_top_y.value = res.data.topList.split(",")
}


// 加载/绘制图表
// 1、不同类别图书数量饼图
const loadBookPie = async () => {
  await getBookCategoryData()
  const bookPie = echarts.init(document.getElementById('bookPie')) // 默认主题为亮白色
  const bookPieOption = {
    title: {
      text: '不同类别图书数量统计',
      // subtext: 'my layout',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b} : {c} ({d}%)'
    },
    legend: {
      top: '15%',
      // orient: 'vertical',
      left: 'center',
    },
    series: [
      {
        name: '图书数量',
        type: 'pie',
        radius: '50%',
        center: ['50%', '65%'],
        data: book_pie_data.value,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  // 使用刚指定的配置项和数据显示图表。
  bookPie.setOption(bookPieOption)
}

// 2、不同类别读者数量饼图
const loadReaderPie = async () => {
  await getReaderCategoryData()
  const readerPie = echarts.init(document.getElementById('readerPie')) // 默认主题为亮白色
  const readerPieOption = {
    title: {
      text: '不同类别图书数量统计',
      subtext: 'my layout',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b} : {c} ({d}%)'
    },
    legend: {
      top: '15%',
      left: 'center',
    },
    series: [
      {
        name: '图书数量',
        type: 'pie',
        radius: ['20%', '55%'],
        center: ['50%', '65%'],
        data: reader_pie_data.value,
        itemStyle: {
          borderRadius: 6,
          borderColor: '#fff',
          borderWidth: 2,
        },
      }
    ]
  }
  // 使用刚指定的配置项和数据显示图表。
  readerPie.setOption(readerPieOption)
}

// 3、近期借书数量折线图
const loadLendChart = async () => {
  await getLendData()
  console.log('mounted还没拿到吗！', lend_data_x.value, lend_data_y.value)
  const lendChart = echarts.init(document.getElementById('lend')) // 默认主题为亮白色
  const lendOption = {
    title: {
      text: '近期借书数量统计',
      subtext: 'my layout',
      left: 'center'
    },
    xAxis: {
      type: 'category',
      data: lend_data_x.value,
    },
    yAxis: {
      type: 'value',
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        data: lend_data_y.value,
        type: 'line',
        itemStyle: {
          color: '#ffaa00' // 设置点的颜色
        },
        lineStyle: {
          width: 2,
          color: '#ffaa00'
        },
        areaStyle: {
          // opacity: 0.5,
          // 从上到下渐变，(0,0)是上部，(0,1)是下部
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: 'rgba(255, 221, 0, 1)'
            },
            {
              offset: 1,
              color: 'rgba(255, 221, 0, 0)'
            }
          ])
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 255, 0.5)'
          }
        }
      }
    ]
  }
  // 使用刚指定的配置项和数据显示图表
  lendChart.setOption(lendOption)
}

// 4、近期还书数量折线图
const loadReturnChart = async () => {
  await getReturnData()
  const returnChart = echarts.init(document.getElementById('return')) // 默认主题为亮白色
  const returnOption = {
    title: {
      text: '近期还书数量统计',
      subtext: 'my layout',
      left: 'center'
    },
    xAxis: {
      type: 'category',
      data: return_data_x.value,
    },
    yAxis: {
      type: 'value',
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        data: return_data_y.value,
        type: 'line',
        itemStyle: {
          color: '#00aaff' // 设置点的颜色
        },
        lineStyle: {
          width: 2,
          color: '#00aaff'
        },
        areaStyle: {
          // opacity: 0.5,
          // 从上到下渐变，(0,0)是上部，(0,1)是下部
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: 'rgba(0, 221, 255, 1)'
            },
            {
              offset: 1,
              color: 'rgba(0, 221, 255, 0)'
            }
          ])
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 255, 0.5)'
          }
        },
      }
    ]
  }
  // 使用刚指定的配置项和数据显示图表。
  returnChart.setOption(returnOption)
}

// 5、图书人气排行榜柱状图
const loadBookTopChart = async () => {
  await getBookTopData()
  console.log('mounted还没拿到吗！', book_top_x.value, book_top_y.value)
  const bookTopChart = echarts.init(document.getElementById('bookTop')) // 默认主题为亮白色
  const bookTopOption = {
    title: {
      text: '近期人气借阅书籍Top排行榜',
      subtext: 'my layout',
      left: 'center'
    },
    yAxis: {
      type: 'category',
      data: book_top_x.value,
      inverse: true, // 设置为true，使得数据倒序
    },
    xAxis: {
      type: 'value',
    },
    grid: {
      left: '5%',
      right: '10%',
      bottom: '3%',
      containLabel: true
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        data: book_top_y.value,
        type: 'bar',
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        },
        itemStyle: {
          color: (params: any) => {
            // 根据数据值动态设置颜色
            const colors = ['#992213', '#bb4413', '#dd7713', '#ee9913', '#eebb2F', '#eedd2F', '#ffee2F'] // 颜色数组
            return colors[params.dataIndex % colors.length] // 取余操作来循环轮流排列颜色
          }
        }
      }
    ]
  }
  // 使用刚指定的配置项和数据显示图表
  bookTopChart.setOption(bookTopOption)
}

// 6、读者借书排行榜柱状图
const loadReaderTopChart = async () => {
  await getReaderTopData()
  const readerTopChart = echarts.init(document.getElementById('readerTop')) // 默认主题为亮白色
  const readerTopOption = {
    title: {
      text: '近期读者借书Top排行榜',
      subtext: 'my layout',
      left: 'center'
    },
    yAxis: {
      type: 'category',
      data: reader_top_x.value,
      inverse: true, // 设置为true，使得数据倒序
    },
    xAxis: {
      type: 'value',
    },
    grid: {
      left: '5%',
      right: '10%',
      bottom: '3%',
      containLabel: true
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        data: reader_top_y.value,
        type: 'bar',
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        },
        itemStyle: {
          color: (params: any) => {
            // 根据数据值动态设置颜色
            const colors = ['#55ff66', '#55ee88', '#55dda5', '#55ccb9', '#55bbcc', '#5599dd', '#5577ee', '#5555ff'] // 颜色数组
            return colors[params.dataIndex % colors.length] // 取余操作来循环轮流排列颜色
          }
        }
      }
    ]
  }
  // 使用刚指定的配置项和数据显示图表。
  readerTopChart.setOption(readerTopOption)
}

// 挂载时才能获取dom元素
onMounted(() => {
  loadBookPie()
  loadReaderPie()
  loadLendChart()
  loadReturnChart()
  loadBookTopChart()
  loadReaderTopChart()
})

watchEffect(() => {
  console.log('watchEffect配置的回调执行了，根据情况动态加载，不用显式指明属性')
  loadLendChart()
  loadReturnChart()
  loadBookTopChart()
  loadReaderTopChart()
})
</script>

<template>
  <div class="page">
    <el-row :gutter="20">
      <div class="pie">
        <div id="bookPie"></div>
      </div>
      <div class="lendReturn">
        <el-select clearable v-model="lendDays" placeholder="选择天数">
          <el-option v-for="item in daysOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div id="lend"></div>
      </div>
    </el-row>
    <el-row :gutter="20">
      <div class="pie">
        <div id="readerPie"></div>
      </div>
      <div class="lendReturn">
        <el-select clearable v-model="returnDays" placeholder="选择天数">
          <el-option v-for="item in daysOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div id="return"></div>
      </div>
    </el-row>
    <el-row :gutter="20">
      <div class="top">
        <el-select clearable v-model="bookDays" placeholder="选择天数">
          <el-option v-for="item in topOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div id="bookTop"></div>
      </div>
      <div class="top">
        <el-select clearable v-model="readerDays" placeholder="选择天数">
          <el-option v-for="item in topOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div id="readerTop"></div>
      </div>
    </el-row>
  </div>
</template>

<style lang="less" scoped>
.page {
  margin: 20px;
  padding: 0;
  background-color: #eeeeee;
}

.el-select {
  margin: 20px;
  width: 100px;
  float: right;
  right: 40px;
}

.pie {
  display: inline-block;
  width: 440px;
  height: 440px;
  margin: 10px;
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
}

.lendReturn {
  display: inline-block;
  width: 700px;
  height: 440px;
  margin: 10px;
  background-color: #fff;
  border-radius: 10px;
  vertical-align: top;
}

.top {
  display: inline-block;
  width: 570px;
  height: 500px;
  margin: 10px;
  background-color: #fff;
  border-radius: 10px;
  vertical-align: top;
}

#bookPie {
  display: inline-block;
  width: 400px;
  height: 400px;
  background-color: #fff;
  border-radius: 10px;
}

#lend {
  display: inline-block;
  width: 700px;
  height: 350px;
  background-color: #fff;
  vertical-align: top;
}

#readerPie {
  display: inline-block;
  width: 400px;
  height: 400px;
  background-color: #fff;
}

#return {
  display: inline-block;
  width: 700px;
  height: 350px;
  background-color: #fff;
  vertical-align: top;
}

#bookTop {
  display: inline-block;
  width: 570px;
  height: 400px;
  background-color: #fff;
}

#readerTop {
  display: inline-block;
  width: 570px;
  height: 400px;
  background-color: #fff;
}
</style>

<!-- 全局样式 -->
<style>
.my-card {
  margin: 20px;
  padding: 20px;
  border-radius: 10px;
  /* justify-content: center; */
}

.pagination {
  justify-content: center;
}
</style>