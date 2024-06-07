import request from '@/utils/request'

/**
 * 获取所有图书类别对应书本的数量API
 * @returns 
 */
export const getBookCategoryDataAPI = () => {
  return request({
    url: '/chart/bookCategory',
    method: 'get',
  })
}

/**
 * 获取所有读者类别对应读者的数量API
 * @returns 
 */
export const getReaderCategoryDataAPI = () => {
  return request({
    url: '/chart/readerCategory',
    method: 'get',
  })
}

/**
 * 获取近day天的借书数统计表API
 * @param params day
 * @returns 
 */
export const getLendDataAPI = (day: number) => {
  console.log('获取近day天的借书数统计表API', day)
  console.log(day)
  return request({
    url: '/chart/dayNLend',
    method: 'get',
    params: { day }
  })
}

/**
 * 获取近day天的还书数统计表API
 * @param params day
 * @returns 
 */
export const getReturnDataAPI = (day: number) => {
  console.log('获取近day天的还书数统计表API', day)
  console.log(day)
  return request({
    url: '/chart/dayNReturn',
    method: 'get',
    params: { day }
  })
}

/**
 * 获取图书销量(借书量)top API
 * @returns 
 */
export const getBookTopDataAPI = (day: number) => {
  console.log('获取图书销量(借书量)top API', day)
  return request({
    url: '/chart/bookTop',
    method: 'get',
    params: { day }
  })
}

/**
 * 获取读者借书排行榜(借书量)top API
 * @returns 
 */
export const getReaderTopDataAPI = (day: number) => {
  console.log('获取读者借书排行榜(借书量)top API', day)
  return request({
    url: '/chart/readerTop',
    method: 'get',
    params: { day }
  })
}