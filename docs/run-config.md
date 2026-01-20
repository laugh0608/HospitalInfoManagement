# 运行配置说明

## Java 25 本地访问警告问题

### 问题现象
启动时出现以下警告：
```
WARNING: A restricted method in java.lang.System has been called
WARNING: java.lang.System::load has been called by org.sqlite.SQLiteJDBCLoader
WARNING: Use --enable-native-access=ALL-UNNAMED to avoid a warning
```

### 解决方案

#### 方式 1：使用 Gradle 命令启动（推荐）
```bash
cd backend
./gradlew bootRun
```
已在 `build.gradle` 中配置 JVM 参数，Gradle 启动时自动应用。

#### 方式 2：IDE（IntelliJ IDEA）运行配置

**步骤：**
1. 打开 Run/Debug Configurations
2. 选择你的 Spring Boot 配置（HospitalApplication）
3. 找到 **VM options** 或 **JVM arguments** 字段
4. 添加以下参数：
   ```
   --enable-native-access=ALL-UNNAMED
   ```
5. 应用并保存

**完整 VM options 示例：**
```
--enable-native-access=ALL-UNNAMED -Dfile.encoding=UTF-8
```

#### 方式 3：直接运行 JAR
```bash
cd backend
./gradlew build
java --enable-native-access=ALL-UNNAMED -jar build/libs/hospital-0.0.1-SNAPSHOT.jar
```

## Redis 连接失败警告

### 问题现象
```
Redis health check failed
RedisConnectionFailureException: Unable to connect to Redis
```

### 原因
项目依赖中包含 `spring-boot-starter-data-redis`，Spring Boot Actuator 的健康检查会尝试连接 Redis。

### 解决方案
已在 `application.properties` 中禁用 Redis 自动配置：
```properties
spring.autoconfigure.exclude=\
  org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,\
  org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration
```

**注意：** 如果使用 IDE 运行，确保配置文件路径正确，或者在启动配置中指定 active profile。

### 验证方法
如果配置正确，启动日志不应该出现：
- ❌ Redis 连接失败的错误
- ❌ DataRedisReactiveHealthIndicator 相关日志

## Spring Security 默认密码警告

### 问题现象
```
Using generated security password: xxxx-xxxx-xxxx-xxxx
This generated password is for development use only.
```

### 解决方案
已在 `application.properties` 中配置默认用户：
```properties
spring.security.user.name=admin
spring.security.user.password=disabled
```

虽然配置了默认用户，但由于 `SecurityConfig.java` 中允许所有请求访问（`.permitAll()`），实际不需要登录。

## 完整的启动检查清单

启动后端后，确认以下内容：

### ✅ 正常情况
- [x] 端口 5000 启动成功
- [x] 数据库连接成功：`jdbc:sqlite:db/hospital.db`
- [x] Tomcat started on port 5000
- [x] Started HospitalApplication in X seconds

### ❌ 不应该出现
- [ ] Java 25 本地访问警告（如果使用 `./gradlew bootRun`）
- [ ] Redis 连接失败错误
- [ ] Spring Security 默认密码警告（已配置后不应再出现）

## IntelliJ IDEA 运行配置模板

### 创建 .run 配置文件

在项目根目录创建 `.run/HospitalApplication.run.xml`：

```xml
<component name="ProjectRunConfigurationManager">
  <configuration default="false" name="HospitalApplication" type="SpringBootApplicationConfigurationType" factoryName="Spring Boot">
    <option name="ACTIVE_PROFILES" />
    <option name="MAIN_CLASS_NAME" value="com.graduation.hospital.HospitalApplication" />
    <module name="hospital.main" />
    <option name="VM_PARAMETERS" value="--enable-native-access=ALL-UNNAMED -Dfile.encoding=UTF-8" />
    <extension name="coverage">
      <pattern>
        <option name="PATTERN" value="com.graduation.hospital.*" />
        <option name="ENABLED" value="true" />
      </pattern>
    </extension>
    <method v="2">
      <option name="Make" enabled="true" />
    </method>
  </configuration>
</component>
```

重启 IDE 后，配置会自动加载。

## 故障排查

### 1. 警告依然存在
**检查：**
- 是否使用 `./gradlew bootRun` 启动？
- IDE 配置中是否添加了 VM options？
- 是否重启了应用？

### 2. Redis 错误依然出现
**检查：**
- 确认 `application.properties` 中的 `spring.autoconfigure.exclude` 配置
- 确认没有其他配置文件覆盖（如 `application-dev.properties`）
- 重启应用

### 3. 配置不生效
**可能原因：**
- IDE 缓存：File > Invalidate Caches > Restart
- Gradle 缓存：`./gradlew clean`
- 配置文件路径错误

## 生产环境注意事项

生产环境使用 Docker 或其他方式部署时，确保在启动脚本中添加 JVM 参数：

**Docker Dockerfile 示例：**
```dockerfile
FROM eclipse-temurin:25-jre
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "--enable-native-access=ALL-UNNAMED", "-jar", "/app.jar"]
```

**systemd service 示例：**
```ini
[Service]
ExecStart=/usr/bin/java --enable-native-access=ALL-UNNAMED -jar /opt/hospital/hospital.jar
```
