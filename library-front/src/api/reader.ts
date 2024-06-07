import request from '@/utils/request'

/**
 * 分页获取所有读者数据
 * @param params 分页查询的条件
 * @returns 
 */
export const getReaderPageAPI = (params: any) => {
  console.log(params)
  return request({
    url: '/reader/page',
    method: 'get',
    params
  })
}

/**
 * 新增读者
 * @param params 
 * @returns 
 */
export const addReaderAPI = (params: any) => {
  console.log(params)
  return request({
    url: '/reader',
    method: 'post',
    data: params
  })
}

/**
 * 根据id获取读者信息
 * @param id 
 * @returns 
 */
export const getReaderByIdAPI = (id: number) => {
  return request({
    url: `/reader/${id}`,
    method: 'get',
  })
}

/**
 * 更新读者信息
 * @param params 
 * @returns 
 */
export const updateReaderAPI = (params: any) => {
  console.log(params)
  console.log({ ...params })
  return request({
    url: '/reader',
    method: 'put',
    data: { ...params }
  })
}

/**
 * 根据ids批量删除读者信息
 * @param ids 
 * @returns 
 */
export const deleteReadersAPI = (ids: string) => {
  return request({
    url: '/reader',
    method: 'delete',
    params: { ids }
  })
}
