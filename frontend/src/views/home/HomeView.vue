<script setup lang="ts">
import { useRouter } from 'vue-router';
import {
  OfficeBuilding,
  Calendar,
  UserFilled,
  Document,
  Phone,
  Location,
  Clock,
} from '@element-plus/icons-vue';

const router = useRouter();

const services = [
  {
    icon: OfficeBuilding,
    title: '科室介绍',
    desc: '涵盖内科、外科、儿科等多个科室，提供专业诊疗服务',
  },
  {
    icon: Calendar,
    title: '预约挂号',
    desc: '在线预约挂号，减少排队等候时间，就诊更高效',
  },
  {
    icon: UserFilled,
    title: '医生团队',
    desc: '经验丰富的专业医疗团队，为您的健康保驾护航',
  },
  {
    icon: Document,
    title: '健康资讯',
    desc: '提供健康知识科普与疾病预防指南，关注社区健康',
  },
];

function goLogin() {
  router.push('/login');
}
</script>

<template>
  <div class="home-page">
    <!-- 顶部导航栏 -->
    <header class="home-nav">
      <div class="nav-inner">
        <div class="nav-logo">
          <div class="logo-icon">
            <el-icon :size="24" color="#fff"><OfficeBuilding /></el-icon>
          </div>
          <span class="logo-title">社区医院信息管理系统</span>
        </div>
        <el-button type="primary" @click="goLogin">管理后台登录</el-button>
      </div>
    </header>

    <!-- Hero 区域 -->
    <section class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">关爱健康，服务社区</h1>
        <p class="hero-subtitle">
          我们致力于为社区居民提供便捷、专业、温暖的医疗健康服务，守护每一个家庭的幸福与安康。
        </p>
        <div class="hero-actions">
          <el-button type="primary" size="large" round>预约挂号</el-button>
          <el-button size="large" round class="btn-outline">了解更多</el-button>
        </div>
      </div>
    </section>

    <!-- 功能服务卡片区 -->
    <section class="services-section">
      <h2 class="section-title">我们的服务</h2>
      <el-row :gutter="24" justify="center">
        <el-col
          v-for="item in services"
          :key="item.title"
          :xs="24"
          :sm="12"
          :md="6"
        >
          <el-card shadow="hover" class="service-card">
            <div class="service-icon">
              <el-icon :size="36"><component :is="item.icon" /></el-icon>
            </div>
            <h3 class="service-title">{{ item.title }}</h3>
            <p class="service-desc">{{ item.desc }}</p>
          </el-card>
        </el-col>
      </el-row>
    </section>

    <!-- 医院介绍区 -->
    <section class="about-section">
      <el-row :gutter="48" align="middle">
        <el-col :xs="24" :md="12">
          <h2 class="section-title" style="text-align: left;">关于我们</h2>
          <p class="about-text">
            社区医院始建于 2005 年，是一所集医疗、预防、保健、康复为一体的综合性社区卫生服务机构。
            医院拥有一支高素质的医疗团队，配备先进的诊疗设备，开设内科、外科、妇科、儿科、中医科等多个科室。
          </p>
          <p class="about-text">
            我们秉承「以患者为中心」的服务理念，致力于为社区居民提供全方位、全周期的健康管理服务，
            努力打造群众满意的社区健康守护者。
          </p>
        </el-col>
        <el-col :xs="24" :md="12">
          <div class="about-image-placeholder">
            <el-icon :size="64" color="#52b788"><OfficeBuilding /></el-icon>
            <span>医院风貌</span>
          </div>
        </el-col>
      </el-row>
    </section>

    <!-- 底部信息栏 -->
    <footer class="home-footer">
      <div class="footer-inner">
        <el-row :gutter="32">
          <el-col :xs="24" :sm="8">
            <div class="footer-item">
              <el-icon :size="18"><Location /></el-icon>
              <span>地址：XX市XX区XX街道XX号</span>
            </div>
          </el-col>
          <el-col :xs="24" :sm="8">
            <div class="footer-item">
              <el-icon :size="18"><Phone /></el-icon>
              <span>联系电话：0123-45678900</span>
            </div>
          </el-col>
          <el-col :xs="24" :sm="8">
            <div class="footer-item">
              <el-icon :size="18"><Clock /></el-icon>
              <span>工作时间：周一至周日 8:00 - 17:30</span>
            </div>
          </el-col>
        </el-row>
        <div class="footer-copyright">
          &copy; 2025 社区医院信息管理系统 版权所有
        </div>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.home-page {
  min-height: 100vh;
  background-color: #fafaf5;
  color: #2d3436;
}

/* 顶部导航 */
.home-nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.nav-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.nav-logo {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-icon {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  background: #52b788;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-title {
  font-size: 18px;
  font-weight: 600;
  color: #2d3436;
}

/* Hero 区域 */
.hero-section {
  margin-top: 64px;
  padding: 100px 24px 80px;
  background: linear-gradient(135deg, #52b788 0%, #95d5b2 50%, #d8f3dc 100%);
  text-align: center;
}

.hero-content {
  max-width: 700px;
  margin: 0 auto;
}

.hero-title {
  font-size: 42px;
  font-weight: 700;
  color: #fff;
  margin-bottom: 20px;
  letter-spacing: 2px;
}

.hero-subtitle {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.9);
  line-height: 1.8;
  margin-bottom: 36px;
}

.hero-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
}

.hero-actions .el-button--primary {
  background-color: #f4845f;
  border-color: #f4845f;
}

.hero-actions .el-button--primary:hover {
  background-color: #e07350;
  border-color: #e07350;
}

.btn-outline {
  background: transparent !important;
  border-color: #fff !important;
  color: #fff !important;
}

.btn-outline:hover {
  background: rgba(255, 255, 255, 0.15) !important;
}

/* 服务卡片区 */
.services-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 80px 24px;
}

.section-title {
  text-align: center;
  font-size: 28px;
  font-weight: 600;
  color: #2d3436;
  margin-bottom: 48px;
}

.service-card {
  text-align: center;
  padding: 20px 0;
  margin-bottom: 24px;
  border-radius: 12px;
  transition: transform 0.3s ease;
  cursor: pointer;
}

.service-card:hover {
  transform: translateY(-6px);
}

.service-icon {
  color: #52b788;
  margin-bottom: 16px;
}

.service-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 10px;
}

.service-desc {
  font-size: 14px;
  color: #636e72;
  line-height: 1.6;
  padding: 0 12px;
}

/* 医院介绍区 */
.about-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 60px 24px 80px;
}

.about-text {
  font-size: 15px;
  line-height: 1.8;
  color: #636e72;
  margin-bottom: 16px;
}

.about-image-placeholder {
  width: 100%;
  height: 320px;
  background: linear-gradient(135deg, #d8f3dc 0%, #b7e4c7 100%);
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
  color: #52b788;
  font-size: 16px;
  font-weight: 500;
}

/* 底部信息栏 */
.home-footer {
  background: #2d3436;
  color: rgba(255, 255, 255, 0.75);
  padding: 40px 24px 24px;
}

.footer-inner {
  max-width: 1200px;
  margin: 0 auto;
}

.footer-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  font-size: 14px;
}

.footer-copyright {
  text-align: center;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  font-size: 13px;
  color: rgba(255, 255, 255, 0.5);
}
</style>
