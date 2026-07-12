# STEP模具分析 - 安卓应用

这是一个基于Android的STEP文件模具分析应用程序，提供了完整的模具分析功能。

## 功能特性

### 核心功能
- 📁 **文件选择**：支持从设备存储选择.step和.stp格式文件
- 🔍 **模具分析**：
  - 拔模角分析
  - 壁厚分析
  - 倒扣检测
  - 分型面分析
- 💰 **成本估算**：模具报价预估
- 🏭 **机台推荐**：根据产品尺寸推荐合适的注塑机台
- 🎨 **3D可视化**：基于Three.js的3D模型查看器

### 辅助功能
- 📊 **历史记录**：保存分析历史
- ⚙️ **设置**：主题、语言、渲染质量等设置
- 📤 **导出报告**：导出PDF格式的分析报告
- 🔗 **分享**：分享分析结果

## 技术架构

### 前端（Android）
- **最低SDK版本**：24 (Android 7.0)
- **目标SDK版本**：34 (Android 14)
- **UI框架**：Material Design Components
- **3D渲染**：WebView + Three.js

### 后端（可选）
- **文件解析**：Python + OpenCASCADE
- **Web API**：FastAPI/Flask
- **部署**：Docker容器

## 项目结构

```
step-analyzer-android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/stepanalyzer/app/
│   │   │   │   ├── MainActivity.java          # 主界面
│   │   │   │   ├── ViewerActivity.java        # 3D查看器
│   │   │   │   └── SettingsActivity.java      # 设置页面
│   │   │   ├── res/
│   │   │   │   ├── layout/                    # 布局文件
│   │   │   │   ├── values/                    # 资源值
│   │   │   │   ├── drawable/                  # 图标资源
│   │   │   │   └── xml/                       # XML配置
│   │   │   └── assets/
│   │   │       └── step_viewer.html           # 3D查看器HTML
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle                               # 项目构建脚本
├── settings.gradle                            # 项目设置
├── gradle.properties                          # Gradle属性
└── README.md                                  # 项目说明
```

## 构建说明

### 前提条件
1. **Android Studio**：推荐使用最新稳定版
2. **JDK 8+**：Java开发工具包
3. **Android SDK**：API Level 34

### 构建步骤

1. **克隆项目**
```bash
git clone <repository-url>
cd step-analyzer-android
```

2. **用Android Studio打开**
   - 打开Android Studio
   - 选择 "Open an existing project"
   - 选择项目目录

3. **同步Gradle**
   - 等待Gradle同步完成
   - 如果提示缺少SDK，按提示安装

4. **构建APK**
   - 点击菜单 Build > Build Bundle(s) / APK(s) > Build APK(s)
   - 等待构建完成
   - APK文件位于 `app/build/outputs/apk/debug/`

5. **安装到设备**
   - 连接Android设备或启动模拟器
   - 点击 Run > Run 'app'
   - 或者直接安装APK文件

### 命令行构建

```bash
# 构建Debug版本
./gradlew assembleDebug

# 构建Release版本
./gradlew assembleRelease

# 清理项目
./gradlew clean
```

## 使用指南

### 基本使用流程

1. **启动应用**
   - 打开"STEP模具分析"应用
   - 点击文件选择区域

2. **选择文件**
   - 在文件浏览器中选择.step或.stp文件
   - 文件大小建议不超过50MB

3. **配置分析选项**
   - 选择开模方向（默认Z轴正方向）
   - 其他分析参数

4. **开始分析**
   - 点击"开始分析"按钮
   - 等待分析完成

5. **查看结果**
   - 查看各项分析结果
   - 点击"查看3D模型"查看模型可视化

6. **导出报告**
   - 点击"导出报告"生成PDF报告
   - 保存或分享报告

### 3D查看器操作

- **旋转**：拖动屏幕旋转模型
- **缩放**：双指捏合缩放
- **重置**：点击重置按钮恢复默认视角
- **线框模式**：切换实体/线框显示

## 后端集成

### 单机模式
应用内置了模拟分析功能，无需后端服务即可体验基本功能。

### 完整模式
要启用完整的STEP文件分析功能，需要部署后端服务：

1. **部署后端API**
```bash
cd ../step-analyzer-backend
python -m uvicorn app.main:app --host 0.0.0.0 --port 8000
```

2. **配置API地址**
   - 在应用设置中配置后端服务器地址
   - 默认地址：`http://localhost:8000`

3. **启用完整分析**
   - 选择文件后将自动上传到服务器
   - 服务器返回真实分析结果

## 自定义配置

### 修改API地址
在 `MainActivity.java` 中修改：
```java
private static final String API_BASE_URL = "http://your-server:8000";
```

### 修改默认设置
在 `res/values/strings.xml` 中修改应用字符串。

### 添加新功能
1. 在 `res/layout/` 中添加新布局
2. 在 `java/com/stepanalyzer/app/` 中添加Activity
3. 在 `AndroidManifest.xml` 中注册新Activity

## 故障排除

### 常见问题

**Q: 文件选择后没有反应**
A: 检查是否有存储权限，在设置中授予权限。

**Q: 3D模型显示为空白**
A: 检查网络连接，确保能访问CDN加载Three.js库。

**Q: 分析结果不准确**
A: 这是模拟数据，需要配置后端服务才能获得真实分析结果。

**Q: 应用闪退**
A: 检查设备Android版本是否满足最低要求（7.0+）。

## 性能优化

### 建议
1. 大文件（>20MB）建议在WiFi环境下分析
2. 3D查看时避免同时运行其他大型应用
3. 定期清理应用缓存

### 系统要求
- **Android版本**：7.0 (API 24) 及以上
- **RAM**：建议2GB以上
- **存储空间**：至少100MB可用空间

## 更新日志

### v1.0.0 (2026-07-12)
- 初始版本发布
- 支持STEP文件选择
- 实现基本分析界面
- 添加3D模型查看器
- 支持设置页面

## 许可证

MIT License

## 联系方式

如有问题或建议，请通过以下方式联系：
- 邮箱：your-email@example.com
- GitHub：https://github.com/your-username

---

**提示**：这是一个学习项目，用于帮助模具工程师快速评估产品开模可行性。
