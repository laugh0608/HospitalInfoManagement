# Security 配置说明

## 认证方式

项目使用 **JWT 无状态认证**，CSRF 已禁用。

- Token 存储在请求头：`Authorization: Bearer <token>`
- Token 有效期：24 小时（可配置）
- 前端使用 localStorage 存储 Token

### 公开接口（无需认证）

| 路径 | 说明 |
|------|------|
| `/api/v1/users/login` | 用户登录 |
| `/api/v1/users/register` | 用户注册 |
| `/api/v1/hello` | 测试接口 |
| `/api/v2/hello` | 测试接口 |
| `/api/hello` | 测试接口 |
| `/swagger-ui/**` | Swagger UI |
| `/v3/api-docs/**` | OpenAPI 文档 |

## CORS（跨域资源共享）配置

### 当前配置
- **允许的源**：`http://localhost:3000`、`http://127.0.0.1:3000`、`http://localhost:5173`、`http://127.0.0.1:5173`
- **允许的方法**：GET、POST、PUT、DELETE、PATCH、OPTIONS
- **允许的请求头**：`*`（所有）
- **暴露的响应头**：`Authorization`
- **允许携带凭证**：`true`（支持 Cookie、Authorization header）
- **预检请求缓存**：3600 秒（1 小时）

### 生产环境调整
生产环境需要修改 `SecurityConfig.java` 中的 `allowedOrigins`：
```java
configuration.setAllowedOrigins(Arrays.asList(
    "https://yourdomain.com",           // 生产域名
    "https://www.yourdomain.com"
));
```

## CSRF（跨站请求伪造）保护

### 当前配置
- **CSRF 已禁用**：项目使用 JWT 无状态认证，不依赖 Cookie/Session，因此无需 CSRF 保护
- 配置位置：`SecurityConfig.java` 中 `.csrf(AbstractHttpConfigurer::disable)`

## 前端配置

### Vite 代理配置
```typescript
// vite.config.ts
export default defineConfig({
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

### Axios 全局配置
```typescript
// src/api/index.ts
import axios from 'axios';
import { getToken, removeToken } from '@/utils/token';

const http = axios.create({
  baseURL: '/api/v1',
  timeout: 15000,
});

// 请求拦截器：注入 Token
http.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 响应拦截器：统一错误处理（401 自动跳登录页）
http.interceptors.response.use(
  (response) => { /* 业务错误判断 */ },
  (error) => { /* HTTP 错误处理 */ }
);
```

## 常见问题

### 1. CORS 错误：No 'Access-Control-Allow-Origin' header
**原因**：前端请求的源不在 `allowedOrigins` 列表中
**解决**：在 `SecurityConfig.java` 中添加对应的源

### 2. CSRF token 验证失败
**原因**：
- Cookie 中没有 XSRF-TOKEN
- 请求头中没有 X-XSRF-TOKEN
- Token 不匹配

**解决**：
- 确保请求携带 `credentials: 'include'` 或 `withCredentials: true`
- 确保从 Cookie 中正确读取并设置到请求头

### 3. Preflight 请求失败（OPTIONS）
**原因**：预检请求被拦截
**解决**：已在 `allowedMethods` 中添加 OPTIONS，无需额外配置

## 安全建议

1. **生产环境**：修改 `allowedOrigins` 为实际域名，不使用通配符 `*`
2. **HTTPS**：生产环境必须使用 HTTPS
3. **JWT 密钥**：使用强密钥，定期轮换
4. **敏感操作**：重要操作添加二次验证
5. **日志审计**：记录所有修改操作的日志（已通过 AuditLogger 实现）
