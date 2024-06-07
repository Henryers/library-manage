import request from '@/utils/request'

/**
 * 添加分类
 * @param params 添加分类的DTO对象
 * @returns
 */
export const addReaderCategoryAPI = (params: any) => {
  return request({
    url: '/readerCategory',
    method: 'post',
    data: { ...params }
  })
}

/**
 * 获取所有书籍分类（前端id-name对应关系，通过映射进行文字展示）
 * @returns 
 */
export const getReaderCategoryAPI = (params: any) => {
  return request({
    url: '/readerCategory/page',
    method: 'get',
    params
  })
}

/**
 * 根据id获取分类信息，用于回显
 * @param id 分类id
 * @returns
 */
export const getReaderCategoryByIdAPI = (id: number) => {
  return request({
    url: `/readerCategory/${id}`,
    method: 'get'
  })
}

/**
 * 修改分类信息
 * @param params 更新分类信息的DTO对象
 * @returns
 */
export const updateReaderCategoryAPI = (params: any) => {
  return request({
    url: '/readerCategory',
    method: 'put',
    data: { ...params }
  })
}

/**
 * 修改分类状态
 * @param params 分类id
 * @returns
 */
export const updateReaderCategoryStatusAPI = (id: number) => {
  console.log('发请求啊！', id)
  return request({
    url: `/readerCategory/status/${id}`,
    method: 'put'
  })
}

/**
 * 根据id删除分类
 * @param id 分类id
 * @returns
 */
export const deleteReaderCategoryAPI = (id: number) => {
  return request({
    url: `/readerCategory/${id}`,
    method: 'delete'
  })
}
