# API 设计规范

> 文档版本：v1.0
> 最后更新：2026-02-25

---

## 1. RESTful 风格

| 方法 | 说明 | 幂等性 |
|------|------|--------|
| GET | 查询资源 | 幂等 |
| POST | 创建资源 | 非幂等 |
| PUT | 完整更新资源 | 幂等 |
| PATCH | 部分更新资源 | 非幂等 |
| DELETE | 删除资源 | 幂等 |

---

## 2. URL 命名规范

- 资源名称使用复数形式：`/api/patients`
- 层级关系用路径表示：`/api/patients/{id}/medical-records`
- 动名词结合：`/api/appointments/{id}/confirm`

---

## 3. 分页、排序、过滤

```
GET /api/patients?page=0&size=20&sort=name,asc&gender=男
```

| 参数 | 说明 | 默认值 |
|------|------|--------|
| page | 页码（从 0 开始） | 0 |
| size | 每页数量 | 20 |
| sort | 排序字段及方向 | createdAt,desc |

---

## 4. 响应格式

### 成功响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [],
    "totalElements": 100,
    "totalPages": 5,
    "page": 0,
    "size": 20
  },
  "timestamp": 1708838400000
}
```

### 错误响应

```json
{
  "code": 404,
  "message": "资源未找到",
  "data": null,
  "timestamp": 1708838400000
}
```

---

## 5. 错误码定义

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 201 | 创建成功 |
| 204 | 删除成功（无返回内容） |
| 400 | 请求参数错误 |
| 401 | 未授权（登录过期） |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 409 | 资源冲突（如重复数据） |
| 500 | 服务器内部错误 |

---

## 相关文件

| 文件 | 说明 |
|------|------|
| [接口清单](endpoints.md) | 完整的 API 接口列表 |
| [项目规划](../project-plan.md) | 项目整体规划 |
| [后端架构](../architecture/backend.md) | 后端架构设计 |