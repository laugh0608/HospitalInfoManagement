import http from '@/api';
import type { Result } from '@/types/api';
import type { LoginRequest, AuthResponse } from '@/types/auth';
import type { SysUser } from '@/types/entity';

/** 用户登录 */
export function login(data: LoginRequest) {
  return http.post<Result<AuthResponse>>('/users/login', data);
}

/** 用户注册 */
export function register(data: { username: string; password: string; email: string }) {
  return http.post<Result<SysUser>>('/users/register', data);
}

/** 获取当前登录用户信息 */
export function getCurrentUser() {
  return http.get<Result<SysUser>>('/users/me');
}
