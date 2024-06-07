import request from '@/utils/request' // 引入自定义的axios函数

/**
 * 登录接口（这是JSDoc注释）
 * @param {*} param0 {username: 用户名, password: 密码}
 * @returns Promise对象
 */
export const loginAPI = (params: any) => {
  return request({
    url: '/manager/login',
    method: 'post',
    data: { ...params }
  })
}

/**
 * 注册接口
 * @param params 
 * @returns 
 */
export const registerAPI = (params: any) => {
  console.log(params)
  console.log({ ...params })
  return request({
    url: '/manager/register',
    method: 'post',
    data: { ...params }
  })
}

/**
 * 修改密码接口
 * @returns 
 */
export const updateAPI = (params: any) => {
  console.log('修改密码')
  return request({
    url: '/manager',
    method: 'put',
    data: { ...params }
  })
}