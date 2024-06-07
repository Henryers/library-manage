<script setup lang="ts" name="layout">
import { RouterView, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useManagerStore } from '@/store'

// ------ data ------
const managerStore = useManagerStore()
const menuList = [
  {
    title: '数据统计',
    path: '/home',
    icon: 'pieChart',
  },
  {
    title: '图书分类',
    path: '/bookCategory',
    icon: 'memo',
  },
  {
    title: '图书列表',
    path: '/book',
    icon: 'collection',
  },
  {
    title: '读者分类',
    path: '/readerCategory',
    icon: 'postcard',
  },
  {
    title: '读者列表',
    path: '/reader',
    icon: 'user',
  },
  {
    title: '借书还书',
    path: '/lendReturn',
    icon: 'reading',
    children: {
      0: {
        subpath: '/lendReturn/add',
        title: '新增借书',
        icon: 'operation',
        children: null
      },
      1: {
        subpath: '/lendReturn',
        title: '记录展示/还书',
        icon: 'tickets',
        children: null
      }
    }
  },
  // {
  //   title: '测试页面',
  //   path: '/test',
  //   icon: 'el-icon-s-order',
  // },
  {
    title: '个人设置',
    path: '/manager',
    icon: 'setting',
  },
]

// ------ method ------
const router = useRouter()

const quitFn = () => {
  // 为了让用户体验更好，来个确认提示框
  ElMessageBox.confirm(
    '走了，爱是会消失的吗?',
    '退出登录',
    {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  )
    .then(() => {
      ElMessage({
        type: 'success',
        message: '退出成功',
      })
      // 清除用户信息，包括token
      managerStore.managerInfo = null
      console.log(managerStore)
      router.push('/login')
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '已取消退出',
      })
    })
}
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <h1 class="h1">图 书 管 理 系 统</h1>
        <el-dropdown>
          <el-button type="primary">
            {{ managerStore.managerInfo ? managerStore.managerInfo.name : '未登录' }}
            <el-icon class="el-icon--right"><arrow-down /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="quitFn">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-container class="sidebar">
        <!-- 左侧导航菜单区域 -->
        <el-menu default-active="/home" class="el-menu-vertical-demo" background-color="#556677" text-color="#fff"
          active-text-color="#ffd04b" unique-opened router>
          <!-- 加了router模式，就会在激活导航时以 :index 作为path进行路径跳转（nb!不用自己写路由了!） -->
          <!-- 根据不同情况选择menu-item/submenu进行遍历，所以外层套template遍历，里面组件做判断看是否该次遍历到自己 -->
          <template v-for="item in menuList">
            <el-menu-item v-if="!item.children" :index="item.path" :key="item.path">
              <el-icon>
                <component :is="item.icon" />
              </el-icon>
              <span>{{ item.title }}</span>
            </el-menu-item>
            <el-sub-menu v-else :index="item.path" :key="item.title">
              <!-- 有子菜单的侧边栏项 -->
              <template #title>
                <el-icon>
                  <component :is="item.icon" />
                </el-icon>
                <span>{{ item.title }}</span>
              </template>
              <!-- 该项下的子菜单 -->
              <el-menu-item v-for="obj in item.children" :index="obj.subpath" :key="obj.subpath">
                <el-icon>
                  <component :is="obj.icon" />
                </el-icon>
                <span>{{ obj.title }}</span>
              </el-menu-item>
            </el-sub-menu>
          </template>
        </el-menu>
        <!-- 右侧内容和底部栏 -->
        <el-container class="mycontainer">
          <el-main>
            <router-view></router-view>
          </el-main>
          <el-footer>© 2024.5.23 library Tech and Class. All rights reserved.</el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<style lang="less" scoped>
.common-layout {
  height: 100%;
  background-color: #eee;
}

.el-header {
  position: relative;
  background-color: #334455;
  color: #eeeeee;
  text-align: center;
  line-height: 60px;
  height: 8vh;
}

.h1 {
  display: inline-block;
  margin: 0;
  text-align: center;
  font-size: 24px;
}

.sidebar {
  display: flex;
  height: 92vh;
}

.el-dropdown {
  position: absolute;
  right: 0;

  .el-button {
    margin: 14px 40px;
    background-color: #22aaff;
    border-color: #22aaff;
    color: #fff;
  }
}

.el-menu {
  width: 200px;
  background-color: #445566;
}

.mycontainer {
  display: flex;
  flex: 6;
  flex-direction: column;
}

.el-main {
  flex: 1;
  padding: 0;
  background-color: #eeeeee;
  color: #333;
  text-align: center;
  line-height: 80px;
}

a {
  display: block;
  height: 5rem;
  color: rgb(0, 209, 118);
  font-size: 20px;
  font-weight: bold;
  text-decoration: none;
}

a:hover {
  background-color: rgb(87, 123, 108);
  color: #6bffce;
}

.el-footer {
  background-color: #ddd;
  font-size: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>

