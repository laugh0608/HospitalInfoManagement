# IDEA 中文乱码解决方案

## 问题现象
在 IntelliJ IDEA 中打开 `application.properties` 文件，中文显示为乱码，右下角显示编码为 `ISO-8859-1`。

## 立即解决方法

### 方法 1：重新加载文件（最快）

1. 在 IDEA 中打开乱码的 `.properties` 文件
2. 点击右下角的编码显示（显示为 `ISO-8859-1`）
3. 选择 **"Reload with 'UTF-8'"**（不是 Convert！）
4. 文件应该正常显示

### 方法 2：设置 IDEA 全局编码

**步骤：**

1. **打开设置**
   - Windows/Linux: `File` → `Settings`
   - Mac: `IntelliJ IDEA` → `Preferences`
   - 快捷键: `Ctrl + Alt + S` (Windows/Linux) 或 `⌘ + ,` (Mac)

2. **配置文件编码**
   - 导航到: `Editor` → `File Encodings`

3. **设置以下选项**
   ```
   Global Encoding: UTF-8
   Project Encoding: UTF-8
   Default encoding for properties files: UTF-8

   ✅ 勾选 "Transparent native-to-ascii conversion"
   ```

4. **点击 Apply 和 OK**

5. **重新打开项目**
   - `File` → `Invalidate Caches...`
   - 选择 `Invalidate and Restart`

### 方法 3：单独设置文件编码

1. 右键点击 `application.properties` 文件
2. 选择 `File Encoding` → `UTF-8`
3. 弹出对话框选择 **"Reload"**（不是 Convert）

## 为什么会出现这个问题？

### 原因 1：IDEA 默认 Properties 文件编码
IDEA 可能默认将 `.properties` 文件识别为 `ISO-8859-1`，这是 Java Properties 文件的传统编码。

### 原因 2：项目编码设置未配置
项目初次导入时，IDEA 可能使用系统默认编码。

### 原因 3：Git 换行符问题
Windows 和 Linux 的换行符不同，可能影响编码识别。

## 预防措施

### 1. 项目已添加 .editorconfig
项目根目录的 `.editorconfig` 文件已配置：
```ini
[*.properties]
charset = utf-8
```

IDEA 会自动识别这个配置（需要安装 EditorConfig 插件，IDEA 2020+ 默认已安装）。

### 2. 团队统一配置
建议团队成员都按照上述步骤设置 IDEA 的文件编码。

### 3. 检查 Git 配置
项目已配置 `.gitattributes`，确保文件在不同系统间保持正确编码。

## 验证配置成功

设置完成后，检查以下内容：

1. **文件编码显示**
   - 打开 `application.properties`
   - 右下角应显示 `UTF-8`

2. **中文正常显示**
   ```properties
   # 社区医院病人跟踪信息管理系统 - 应用配置
   ```
   中文应该清晰可读，不是乱码

3. **新建文件默认编码**
   - 新建 `.properties` 文件
   - 右下角应显示 `UTF-8`

## 常见问题

### Q1: 选择 "Convert" 还是 "Reload"？
**A:**
- **Reload**: 重新用新编码读取文件（文件本身正确，只是显示错误）✅
- **Convert**: 转换文件编码（会修改文件内容）❌

我们的情况应该选择 **Reload**，因为文件本身就是 UTF-8，只是 IDEA 识别错了。

### Q2: 设置后还是乱码怎么办？
**A:** 尝试以下步骤：
1. 关闭 IDEA
2. 删除项目的 `.idea` 文件夹
3. 重新导入项目
4. 重新设置文件编码

### Q3: 为什么有些中文还是乱码？
**A:** 如果之前选择了 "Convert"，文件内容可能已经损坏。需要从 Git 恢复：
```bash
git checkout -- backend/src/main/resources/application.properties
git checkout -- backend/src/main/resources/application-example.properties
```

### Q4: Properties 文件可以用 UTF-8 吗？
**A:**
- **传统方式**: Java Properties 文件使用 `ISO-8859-1` + Unicode 转义（`\uXXXX`）
- **现代方式**: Spring Boot 支持 UTF-8 编码的 Properties 文件
- **推荐**: 开启 IDEA 的 "Transparent native-to-ascii conversion"，自动处理转换

## IDEA 设置截图说明

### 1. File Encodings 设置位置
```
File → Settings → Editor → File Encodings
```

### 2. 应该看到的配置
```
Global Encoding:                     UTF-8
Project Encoding:                    UTF-8
Default encoding for properties:     UTF-8

☑ Transparent native-to-ascii conversion
☑ Create UTF-8 files with BOM:      never
```

### 3. 文件编码快速切换
- 点击右下角编码显示
- 列表会显示当前编码和可选编码
- 选择 UTF-8 → Reload

## 其他编辑器

### VS Code
1. 安装 EditorConfig 插件
2. 打开文件，点击右下角编码
3. 选择 "Reopen with Encoding" → "UTF-8"

### Notepad++
1. Encoding → Character Sets → Chinese → GB2312 (如果是乱码先这样)
2. Encoding → Convert to UTF-8

## 技术原理

### Java Properties 文件编码历史
- **Java 8 及以前**: Properties 文件必须是 `ISO-8859-1`，中文需要转义为 `\uXXXX`
- **Java 9+**: `PropertyResourceBundle` 支持 UTF-8
- **Spring Boot**: 默认支持 UTF-8 Properties 文件

### IDEA 的 "Transparent native-to-ascii conversion"
- 开启后：显示时看到中文，保存时自动转为 `\uXXXX`
- 关闭后：直接保存 UTF-8 中文（Spring Boot 支持）

**我们的选择**: 关闭转换，直接使用 UTF-8（更现代、更易读）

## 总结

**最简单的解决方法**：
1. IDEA 右下角点击 `ISO-8859-1`
2. 选择 `Reload with 'UTF-8'`
3. 完成！

如果还有问题，按照"方法 2"设置全局编码即可。
