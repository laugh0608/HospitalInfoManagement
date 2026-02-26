<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import {
  Odometer,
  User,
  UserFilled,
  OfficeBuilding,
  Calendar,
  Document,
  FirstAidKit,
  Fold,
  Expand,
  ArrowDown,
} from '@element-plus/icons-vue';

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();

const isCollapsed = ref(false);

// 图标映射
const iconMap: Record<string, typeof Odometer> = {
  Odometer,
  User,
  UserFilled,
  OfficeBuilding,
  Calendar,
  Document,
  FirstAidKit,
};

// 从路由配置中获取菜单项
const menuItems = computed(() => {
  const mainRoute = router.options.routes.find((r) => r.path === '/console');
  if (!mainRoute || !mainRoute.children) return [];
  return mainRoute.children.filter((child) => !child.meta?.hidden);
});

// 当前激活的菜单
const activeMenu = computed(() => route.path);

// 显示的用户名
const displayName = computed(() => {
  return userStore.user?.profile?.realName || userStore.user?.username || '用户';
});

function handleLogout() {
  userStore.logout();
  router.push('/');
}
</script>

<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapsed ? '64px' : '220px'" class="layout-aside">
      <div class="logo-area">
        <span v-if="!isCollapsed" class="logo-text">社区医院管理</span>
        <span v-else class="logo-text-mini">医</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapsed"
        router
        background-color="#1b4332"
        text-color="rgba(255, 255, 255, 0.65)"
        active-text-color="#fff"
        class="aside-menu"
      >
        <el-menu-item
          v-for="item in menuItems"
          :key="item.path"
          :index="'/console/' + item.path"
        >
          <el-icon>
            <component :is="iconMap[item.meta?.icon as string]" />
          </el-icon>
          <template #title>{{ item.meta?.title }}</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧区域 -->
    <el-container>
      <!-- 顶栏 -->
      <el-header class="layout-header">
        <div class="header-left">
          <el-icon
            class="collapse-btn"
            @click="isCollapsed = !isCollapsed"
          >
            <Fold v-if="!isCollapsed" />
            <Expand v-else />
          </el-icon>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleLogout">
            <span class="user-info">
              {{ displayName }}
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区 -->
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout-container {
  height: 100vh;
}

.layout-aside {
  background-color: #1b4332;
  transition: width 0.3s;
  overflow: hidden;
}

.logo-area {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo-text {
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  white-space: nowrap;
}

.logo-text-mini {
  color: #fff;
  font-size: 20px;
  font-weight: 700;
}

.aside-menu {
  border-right: none;
}

.aside-menu:not(.el-menu--collapse) {
  width: 220px;
}

/* 菜单激活项高亮 */
.aside-menu :deep(.el-menu-item.is-active) {
  background-color: #52b788 !important;
}

.aside-menu :deep(.el-menu-item:hover) {
  background-color: rgba(82, 183, 136, 0.3) !important;
}

.layout-header {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  z-index: 1;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  color: #2d3436;
}

.collapse-btn:hover {
  color: #52b788;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
  font-size: 14px;
  color: #2d3436;
}

.user-info:hover {
  color: #52b788;
}

.layout-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
