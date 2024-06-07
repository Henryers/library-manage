import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: () => import('../views/layout/index.vue'),
      redirect: '/home',
      children: [
        {
          path: '/home',
          name: 'home',
          // lazy loading
          component: () => import('../views/home/index.vue')
        },
        {
          path: '/bookCategory',
          name: 'bookCategory',
          component: () => import('../views/bookCategory/index.vue')
        },
        {
          path: '/bookCategory/update',
          name: 'bookCategoryUpdate',
          component: () => import('../views/bookCategory/update.vue')
        },
        {
          path: '/book',
          name: 'book',
          component: () => import('../views/book/index.vue')
        },
        {
          path: '/book/update',
          name: 'bookUpdate',
          component: () => import('../views/book/update.vue')
        },
        {
          path: '/readerCategory',
          name: 'readerCategory',
          component: () => import('../views/readerCategory/index.vue')
        },
        {
          path: '/readerCategory/update',
          name: 'readerCategoryUpdate',
          component: () => import('../views/readerCategory/update.vue')
        },
        {
          path: '/reader',
          name: 'reader',
          component: () => import('../views/reader/index.vue')
        },
        {
          path: '/reader/update',
          name: 'readerUpdate',
          component: () => import('../views/reader/update.vue')
        },
        {
          path: '/lendReturn',
          name: 'lendReturn',
          component: () => import('../views/lendReturn/index.vue')
        },
        {
          path: '/lendReturn/add',
          name: 'lendReturnAdd',
          component: () => import('../views/lendReturn/add.vue')
        },
        {
          path: '/lendReturn/update',
          name: 'lendReturnUpdate',
          component: () => import('../views/lendReturn/update.vue')
        },
        {
          path: '/manager',
          name: 'manager',
          component: () => import('../views/manager/index.vue')
        },
        {
          path: '/test',
          name: 'test',
          component: () => import('../views/test/index.vue')
        },
      ]
    },
    {
      path: '/login',
      name: 'login',
      // lazy loading
      component: () => import('../views/login/index.vue')
    },
    {
      path: '/reg',
      name: 'reg',
      // lazy loading
      component: () => import('../views/reg/index.vue')
    }
  ]
})

export default router
