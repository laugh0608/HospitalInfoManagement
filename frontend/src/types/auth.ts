/** 登录请求 */
export interface LoginRequest {
  username: string;
  password: string;
}

/** 认证响应 */
export interface AuthResponse {
  token: string;
  username: string;
  userId: number;
  roles: string[];
  expiration: number;
}

/** 角色代码枚举 */
export type RoleCode = 'ADMIN' | 'DOCTOR' | 'PATIENT';
