<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { OfficeBuilding } from '@element-plus/icons-vue';
import type { FormInstance, FormRules } from 'element-plus';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

const formRef = ref<FormInstance>();
const loading = ref(false);

const loginForm = reactive({
  username: '',
  password: '',
});

const rules = reactive<FormRules>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' },
  ],
});

async function handleLogin() {
  const valid = await formRef.value?.validate().catch(() => false);
  if (!valid) return;

  loading.value = true;
  try {
    await userStore.login(loginForm);
    const redirect = (route.query.redirect as string) || '/console';
    router.push(redirect);
  } finally {
    loading.value = false;
  }
}

function goHome() {
  router.push('/');
}
</script>

<template>
  <div class="login-page">
    <div class="login-card">
      <!-- 左侧：系统信息 -->
      <div class="card-left">
        <div class="brand-logo" @click="goHome">
          <div class="brand-icon">
            <el-icon :size="32" color="#fff"><OfficeBuilding /></el-icon>
          </div>
        </div>
        <h1 class="brand-title">社区医院<br>信息管理系统</h1>
        <p class="brand-desc">
          关爱健康，服务社区。为居民提供便捷、专业、温暖的医疗健康服务。
        </p>
        <div class="brand-features">
          <div class="feature-item">
            <span class="feature-dot"></span>
            <span>在线预约挂号</span>
          </div>
          <div class="feature-item">
            <span class="feature-dot"></span>
            <span>电子病历管理</span>
          </div>
          <div class="feature-item">
            <span class="feature-dot"></span>
            <span>智能药品管理</span>
          </div>
          <div class="feature-item">
            <span class="feature-dot"></span>
            <span>多科室协同</span>
          </div>
        </div>
      </div>

      <!-- 右侧：登录表单 -->
      <div class="card-right">
        <h2 class="form-title">欢迎登录</h2>
        <p class="form-subtitle">请输入您的账号信息</p>
        <el-form
          ref="formRef"
          :model="loginForm"
          :rules="rules"
          size="large"
          @keyup.enter="handleLogin"
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="用户名"
              prefix-icon="User"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="密码"
              prefix-icon="Lock"
              show-password
            />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              class="login-btn"
              :loading="loading"
              @click="handleLogin"
            >
              登 录
            </el-button>
          </el-form-item>
        </el-form>
        <div class="form-footer">
          <span class="back-home" @click="goHome">返回首页</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #d8f3dc 0%, #b7e4c7 40%, #95d5b2 100%);
}

.login-card {
  display: flex;
  width: 820px;
  min-height: 480px;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.12);
}

/* 左侧系统信息 */
.card-left {
  flex: 0 0 340px;
  background: linear-gradient(160deg, #2d6a4f 0%, #40916c 50%, #52b788 100%);
  padding: 48px 36px;
  display: flex;
  flex-direction: column;
  color: #fff;
}

.brand-logo {
  margin-bottom: 32px;
  cursor: pointer;
}

.brand-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(4px);
}

.brand-title {
  font-size: 26px;
  font-weight: 700;
  line-height: 1.4;
  margin-bottom: 16px;
  letter-spacing: 1px;
}

.brand-desc {
  font-size: 14px;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.85);
  margin-bottom: 32px;
}

.brand-features {
  margin-top: auto;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.9);
}

.feature-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #d8f3dc;
  flex-shrink: 0;
}

/* 右侧登录表单 */
.card-right {
  flex: 1;
  background: #fff;
  padding: 48px 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.form-title {
  font-size: 24px;
  font-weight: 600;
  color: #2d3436;
  margin-bottom: 8px;
}

.form-subtitle {
  font-size: 14px;
  color: #636e72;
  margin-bottom: 32px;
}

.login-btn {
  width: 100%;
  background-color: #52b788;
  border-color: #52b788;
}

.login-btn:hover {
  background-color: #40916c;
  border-color: #40916c;
}

.form-footer {
  text-align: center;
  margin-top: 16px;
}

.back-home {
  font-size: 13px;
  color: #636e72;
  cursor: pointer;
  transition: color 0.2s;
}

.back-home:hover {
  color: #52b788;
}

/* 响应式：小屏幕隐藏左侧 */
@media (max-width: 768px) {
  .login-card {
    width: 90%;
    max-width: 420px;
  }

  .card-left {
    display: none;
  }
}
</style>
