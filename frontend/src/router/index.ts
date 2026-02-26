import { createRouter, createWebHistory } from 'vue-router';
import type { RouteRecordRaw } from 'vue-router';
import { getToken } from '@/utils/token';
import { useUserStore } from '@/stores/user';
import MainLayout from '@/layouts/MainLayout.vue';

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/LoginView.vue'),
    meta: { title: '登录', hidden: true },
  },
  {
    path: '/',
    component: MainLayout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/DashboardView.vue'),
        meta: { title: '仪表盘', icon: 'Odometer' },
      },
      {
        path: 'patient',
        name: 'Patient',
        component: () => import('@/views/patient/PatientView.vue'),
        meta: { title: '患者管理', icon: 'User' },
      },
      {
        path: 'doctor',
        name: 'Doctor',
        component: () => import('@/views/doctor/DoctorView.vue'),
        meta: { title: '医生管理', icon: 'UserFilled' },
      },
      {
        path: 'department',
        name: 'Department',
        component: () => import('@/views/department/DepartmentView.vue'),
        meta: { title: '科室管理', icon: 'OfficeBuilding' },
      },
      {
        path: 'appointment',
        name: 'Appointment',
        component: () => import('@/views/appointment/AppointmentView.vue'),
        meta: { title: '预约挂号', icon: 'Calendar' },
      },
      {
        path: 'medical-record',
        name: 'MedicalRecord',
        component: () => import('@/views/medical-record/MedicalRecordView.vue'),
        meta: { title: '病历管理', icon: 'Document' },
      },
      {
        path: 'medicine',
        name: 'Medicine',
        component: () => import('@/views/medicine/MedicineView.vue'),
        meta: { title: '药品管理', icon: 'FirstAidKit' },
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 导航守卫
router.beforeEach(async (to, _from, next) => {
  const token = getToken();

  if (to.path === '/login') {
    // 已登录用户访问登录页，重定向到首页
    if (token) {
      next({ path: '/' });
    } else {
      next();
    }
    return;
  }

  // 未登录，跳转登录页
  if (!token) {
    next({ path: '/login', query: { redirect: to.fullPath } });
    return;
  }

  // 已有 Token，尝试获取用户信息
  const userStore = useUserStore();
  if (!userStore.user) {
    try {
      await userStore.fetchUserInfo();
      next();
    } catch {
      // 获取用户信息失败（Token 过期等），登出并跳转登录页
      userStore.logout();
      next({ path: '/login', query: { redirect: to.fullPath } });
    }
  } else {
    next();
  }
});

export default router;
