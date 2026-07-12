# 📦 Android APK 构建指南

由于服务器环境没有 Android SDK，需要在本地电脑上构建 APK。

## 🚀 快速构建步骤

### 步骤1：下载整个项目
```bash
# 将项目下载到本地电脑
# 项目路径：/vol1/@appshare/com.dustinky.qwenpaw/.qwenpaw/workspaces/default/step-analyzer-android/
```

### 步骤2：用 Android Studio 打开
1. 启动 Android Studio
2. 选择 "Open an existing project"
3. 选择 `step-analyzer-android` 文件夹
4. 等待 Gradle 同步完成（首次需要下载依赖）

### 步骤3：构建 APK
**方法A：通过菜单**
```
Build → Build Bundle(s) / APK(s) → Build APK(s)
```

**方法B：通过终端**
```bash
# 在 Android Studio 终端中运行
./gradlew assembleDebug
```

### 步骤4：找到 APK 文件
```
app/build/outputs/apk/debug/app-debug.apk
```

### 步骤5：复制到目标位置
```bash
# 复制到 /vol1/1000/Qwen/
cp app/build/outputs/apk/debug/app-debug.apk /vol1/1000/Qwen/
```

---

## 📱 安装到手机

### 方法1：直接安装
1. 将 APK 文件传输到手机
2. 在手机上打开文件管理器
3. 点击 APK 文件进行安装

### 方法2：使用 ADB 安装
```bash
# 连接手机，开启USB调试
adb devices

# 安装 APK
adb install app-debug.apk
```

### 方法3：Android Studio 直接运行
1. 连接手机（开启USB调试）
2. 点击 Android Studio 的运行按钮 ▶
3. 选择你的设备
4. 自动安装并启动

---

## ⚠️ 前提条件

### 必须安装
1. **JDK 8+**
   - 下载：https://adoptium.net/
   
2. **Android Studio**
   - 下载：https://developer.android.com/studio
   - 包含 Android SDK

### 手机设置
1. **开启USB调试**
   - 设置 → 关于手机 → 连续点击"版本号"7次
   - 设置 → 开发者选项 → 开启"USB调试"

2. **允许安装未知来源应用**
   - 设置 → 安全 → 开启"未知来源"

---

## 🔧 常见问题

### Q: Gradle 同步失败
**A:** 检查网络连接，或配置镜像源

### Q: 找不到 SDK
**A:** 在 Android Studio 中配置 SDK 路径：
```
File → Project Structure → SDK Location
```

### Q: 安装时提示签名冲突
**A:** 先卸载旧版本，或修改包名

---

## 📋 文件清单

项目包含以下关键文件：
```
step-analyzer-android/
├── app/src/main/java/com/stepanalyzer/app/
│   ├── MainActivity.java        # 主界面
│   ├── ViewerActivity.java      # 3D查看器
│   └── SettingsActivity.java    # 设置页面
├── app/src/main/res/layout/     # 界面布局
├── app/src/main/assets/         # Web资源
├── app/build.gradle             # 应用配置
└── build.gradle                 # 项目配置
```

---

**需要帮助吗？** 如果遇到问题，可以告诉我具体的错误信息！
