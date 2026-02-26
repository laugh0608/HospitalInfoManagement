import 'vue-router';

declare module 'vue-router' {
  interface RouteMeta {
    /** 菜单标题 */
    title?: string;
    /** Element Plus 图标名 */
    icon?: string;
    /** 是否在侧边栏隐藏 */
    hidden?: boolean;
  }
}
