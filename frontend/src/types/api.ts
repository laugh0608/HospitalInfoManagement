/** 统一 API 响应格式 */
export interface Result<T = unknown> {
  code: number;
  message: string;
  data: T;
  timestamp: number;
}

/** 分页查询参数 */
export interface PageQuery {
  page: number;
  size: number;
  sort?: string;
}

/** 分页响应数据 */
export interface PageResult<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
