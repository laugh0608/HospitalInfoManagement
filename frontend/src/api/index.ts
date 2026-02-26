import axios from 'axios';
import { ElMessage } from 'element-plus';
import { getToken, removeToken } from '@/utils/token';
import type { Result } from '@/types/api';

const http = axios.create({
  baseURL: '/api/v1',
  timeout: 15000,
});

// 请求拦截器：注入 Token
http.interceptors.request.use(
  (config) => {
    const token = getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

// 响应拦截器：统一错误处理
http.interceptors.response.use(
  (response) => {
    const res = response.data as Result;

    // 业务错误（HTTP 200 但 code 非 200）
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败');
      return Promise.reject(new Error(res.message || '请求失败'));
    }

    return response;
  },
  (error) => {
    if (error.response) {
      const status = error.response.status;

      if (status === 401) {
        removeToken();
        // 避免重复跳转
        if (window.location.pathname !== '/login') {
          window.location.href = '/login';
        }
        ElMessage.error('登录已过期，请重新登录');
      } else if (status === 403) {
        ElMessage.error('没有权限执行此操作');
      } else {
        // 尝试从响应体提取错误信息
        const data = error.response.data as Result | undefined;
        const msg = data?.message || error.message || '请求失败';
        ElMessage.error(msg);
      }
    } else {
      ElMessage.error('网络异常，请检查网络连接');
    }

    return Promise.reject(error);
  },
);

export default http;
