import request from '@/utils/request'

/**
 * 分页获取所有借还书数据
 * @param params 
 * @returns 
 */
export const getBorrowPageAPI = (params: any) => {
  console.log(params)
  return request({
    url: '/borrow/page',
    method: 'get',
    params
  })
}

/**
 * 添加借阅记录
 * @param params 
 * @returns 
 */
export const addBorrowAPI = (params: any) => {
  console.log(params)
  return request({
    url: '/borrow',
    method: 'post',
    data: params
  })
}

/**
 * 根据id获取借阅记录
 * @param id 
 * @returns 
 */
export const getBorrowByIdAPI = (id: number) => {
  return request({
    url: `/borrow/${id}`,
    method: 'get',
  })
}


// /**
//  * 根据bId(书籍id)和rId(读者id)获取借阅记录
//  * @param rId 
//  * @param bId 
//  * @returns 
//  */
// export const getBorrowByIdAPI = (rId: number, bId: number) => {
//   return request({
//     url: '/borrow',
//     method: 'get',
//     params: { rId, bId }
//   })
// }

/**
 * 更新借还书记录
 * @param params 
 * @returns 
 */
export const updateBorrowAPI = (params: any) => {
  return request({
    url: '/borrow',
    method: 'put',
    data: params
  })
}

/**
 * 根据ids批量删除借还书记录
 * @param ids
 * @returns 
 */
export const deleteBorrowsAPI = (ids: string) => {
  return request({
    url: '/borrow',
    method: 'delete',
    params: { ids }
  })
}
