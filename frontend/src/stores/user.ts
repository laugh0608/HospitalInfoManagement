import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getToken, setToken, removeToken } from '@/utils/token';
import { login as loginApi, getCurrentUser } from '@/api/modules/auth';
import type { LoginRequest } from '@/types/auth';
import type { SysUser } from '@/types/entity';

export const useUserStore = defineStore('user', () => {
  const token = ref<string | null>(getToken());
  const user = ref<SysUser | null>(null);

  /** 登录 */
  async function login(form: LoginRequest) {
    const { data: res } = await loginApi(form);
    token.value = res.data.token;
    setToken(res.data.token);
  }

  /** 获取用户信息 */
  async function fetchUserInfo() {
    const { data: res } = await getCurrentUser();
    user.value = res.data;
  }

  /** 登出 */
  function logout() {
    token.value = null;
    user.value = null;
    removeToken();
  }

  return { token, user, login, fetchUserInfo, logout };
});
