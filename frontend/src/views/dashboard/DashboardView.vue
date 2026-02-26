<script setup lang="ts">
import { User, UserFilled, OfficeBuilding, FirstAidKit } from '@element-plus/icons-vue';
</script>

<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" color="#409eff"><User /></el-icon>
            <div class="stat-info">
              <div class="stat-label">患者总数</div>
              <div class="stat-value">--</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" color="#67c23a"><UserFilled /></el-icon>
            <div class="stat-info">
              <div class="stat-label">医生总数</div>
              <div class="stat-value">--</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" color="#e6a23c"><OfficeBuilding /></el-icon>
            <div class="stat-info">
              <div class="stat-label">科室总数</div>
              <div class="stat-value">--</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" color="#f56c6c"><FirstAidKit /></el-icon>
            <div class="stat-info">
              <div class="stat-label">药品总数</div>
              <div class="stat-value">--</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.dashboard {
  padding: 4px;
}

.stat-card {
  margin-bottom: 20px;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
}
</style>
