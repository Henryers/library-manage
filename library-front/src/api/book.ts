import request from '@/utils/request'

/**
 * 分页获取所有书籍数据
 * @param params 分页查询的条件
 * @returns 
 */
export const getBookPageAPI = (params: any) => {
  console.log('根据该条件获取分页书籍列表API', params)
  console.log(params)
  return request({
    url: '/book/page',
    method: 'get',
    params
  })
}

/**
 * 新增书籍
 * @param params 
 * @returns 
 */
export const addBookAPI = (params: any) => {
  console.log('新增书籍API', params)
  return request({
    url: '/book',
    method: 'post',
    data: params
  })
}

/**
 * 根据id获取书籍信息
 * @param id 
 * @returns 
 */
export const getBookByIdAPI = (id: number) => {
  return request({
    url: `/book/${id}`,
    method: 'get',
  })
}

/**
 * 更新书籍信息
 * @param params 
 * @returns 
 */
export const updateBookAPI = (params: any) => {
  console.log(params)
  console.log({ ...params })
  return request({
    url: '/book',
    method: 'put',
    data: { ...params }
  })
}

/**
 * 更新书籍状态
 * @param params 
 * @returns 
 */
export const updateBookStatusAPI = (id: number) => {
  return request({
    url: `/book/status/${id}`,
    method: 'put',
  })
}

/**
 * 根据ids批量删除书籍信息
 * @param id 
 * @returns 
 */
export const deleteBooksAPI = (ids: string) => {
  return request({
    url: '/book',
    method: 'delete',
    params: { ids }
  })
}
