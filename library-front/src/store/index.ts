import { defineStore } from 'pinia'
import type { ManagerInfo } from '@/types/manager'
import { ref } from 'vue'

// Pinia 会自动将 Composition API 这些相关函数自动识别为状态管理的相关内容
// ref -> state,  computed -> getters,  methods -> actions    无 mutation 概念
// 等函数转换为响应式数据
export const useManagerStore = defineStore('managerInfo', {
  state: () => {
    const managerInfo = ref<ManagerInfo | null>(null)
    return { managerInfo }
  },
  persist: true // 持久化存储
})