# Security 配置说明

## CORS（跨域资源共享）配置

### 当前配置
- **允许的源**：`http://localhost:3000`、`http://127.0.0.1:3000`
- **允许的方法**：GET、POST、PUT、DELETE、PATCH、OPTIONS
- **允许的请求头**：
  - `Authorization` - 用于 JWT 或 Bearer token
  - `Content-Type` - 请求内容类型
  - `X-Requested-With` - AJAX 请求标识
  - `X-XSRF-TOKEN` - CSRF token
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
- **默认启用 CSRF 保护**
- **GET 请求**：不需要 CSRF token（安全读取操作）
- **POST/PUT/DELETE/PATCH 请求**：需要 CSRF token
- **测试接口**：`/api/hello` 暂时忽略 CSRF（仅用于测试连通性）

### CSRF Token 使用方式

#### 方式 1：使用 Cookie（推荐）
Spring Security 会自动在 Cookie 中设置 `XSRF-TOKEN`，前端需要：

1. 从 Cookie 中读取 `XSRF-TOKEN`
2. 在请求头中添加 `X-XSRF-TOKEN`

**前端示例（fetch）：**
```javascript
// 获取 CSRF token
function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
}

// 发送 POST 请求
fetch('/api/patients', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'X-XSRF-TOKEN': getCookie('XSRF-TOKEN')
  },
  credentials: 'include',  // 携带 Cookie
  body: JSON.stringify(data)
});
```

**前端示例（axios）：**
```javascript
import axios from 'axios';

// 全局配置
axios.defaults.withCredentials = true;
axios.defaults.xsrfCookieName = 'XSRF-TOKEN';
axios.defaults.xsrfHeaderName = 'X-XSRF-TOKEN';

// 发送请求
axios.post('/api/patients', data);
```

#### 方式 2：从响应头获取
后端可以在响应头中返回 CSRF token（已配置 `exposedHeaders`）

### 需要 CSRF 保护的接口

所有修改数据的接口都需要 CSRF token：
- ✅ POST `/api/patients` - 创建患者
- ✅ PUT `/api/patients/{id}` - 更新患者
- ✅ DELETE `/api/patients/{id}` - 删除患者
- ❌ GET `/api/patients` - 查询患者（不需要）
- ❌ GET `/api/hello` - 测试接口（已忽略）

### 禁用 CSRF（仅特殊情况）

如果某些接口确实不需要 CSRF 保护（如公开 API），可在 `SecurityConfig.java` 中添加：
```java
.csrf(csrf -> csrf
    .ignoringRequestMatchers(
        "/api/hello",
        "/api/public/**"  // 公开 API 路径
    )
)
```

## 前端配置建议

### Vite 代理配置（已配置）
```typescript
// vite.config.ts
export default defineConfig({
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:5000',
        changeOrigin: true
      }
    }
  }
})
```

### Axios 全局配置
```typescript
// src/utils/request.ts
import axios from 'axios';

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
  withCredentials: true,  // 携带 Cookie
  xsrfCookieName: 'XSRF-TOKEN',
  xsrfHeaderName: 'X-XSRF-TOKEN'
});

export default request;
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

1. ✅ **生产环境**：修改 `allowedOrigins` 为实际域名，不使用通配符 `*`
2. ✅ **HTTPS**：生产环境必须使用 HTTPS，Cookie 设置 `Secure` 标志
3. ✅ **认证**：后续添加 JWT 或 Session 认证机制
4. ✅ **敏感操作**：重要操作添加二次验证
5. ✅ **日志审计**：记录所有修改操作的日志
