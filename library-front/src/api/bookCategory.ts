import request from '@/utils/request'

/**
 * 添加分类
 * @param params 添加分类的DTO对象
 * @returns
 */
export const addBookCategoryAPI = (params: any) => {
  return request({
    url: '/bookCategory',
    method: 'post',
    data: { ...params }
  })
}

/**
 * 获取书籍分类（前端id-name对应关系，通过映射进行文字展示）
 * @returns 分页条件
 */
export const getBookCategoryAPI = (params: any) => {
  console.log('bc-params', params)
  return request({
    url: '/bookCategory/page',
    method: 'get',
    params
  })
}

/**
 * 根据id获取分类信息，用于回显
 * @param id 分类id
 * @returns
 */
export const getBookCategoryByIdAPI = (id: number) => {
  return request({
    url: `/bookCategory/${id}`,
    method: 'get'
  })
}

/**
 * 修改分类信息
 * @param params 更新分类信息的DTO对象
 * @returns
 */
export const updateBookCategoryAPI = (params: any) => {
  return request({
    url: '/bookCategory',
    method: 'put',
    data: { ...params }
  })
}

/**
 * 修改分类状态
 * @param params 分类id
 * @returns
 */
export const updateBookCategoryStatusAPI = (id: number) => {
  console.log('发请求啊！', id)
  return request({
    url: `/bookCategory/status/${id}`,
    method: 'put'
  })
}

/**
 * 根据id删除分类
 * @param id 分类id
 * @returns
 */
export const deleteBookCategoryAPI = (id: number) => {
  return request({
    url: `/bookCategory/${id}`,
    method: 'delete'
  })
}
