# STEP模具分析 - 安装说明

## 快速开始

### 方法1：使用Android Studio（推荐）

#### 步骤1：安装Android Studio
1. 访问 https://developer.android.com/studio
2. 下载并安装最新版本的Android Studio
3. 首次启动时会自动下载Android SDK

#### 步骤2：打开项目
1. 启动Android Studio
2. 选择 "Open an existing project"
3. 浏览到项目目录 `step-analyzer-android`
4. 点击 "OK"

#### 步骤3：等待同步
- Android Studio会自动下载依赖
- 首次同步可能需要几分钟时间
- 等待底部进度条完成

#### 步骤4：构建APK
1. 点击菜单栏 "Build" > "Build Bundle(s) / APK(s)" > "Build APK(s)"
2. 等待构建完成（通常需要1-3分钟）
3. 构建成功后会显示通知

#### 步骤5：安装到设备
**方法A：使用Android Studio**
- 连接Android设备（开启USB调试）
- 点击绿色的运行按钮 ▶
- 选择你的设备
- 等待安装完成

**方法B：手动安装APK**
1. 找到APK文件：`app/build/outputs/apk/debug/app-debug.apk`
2. 将APK传输到Android设备
3. 在设备上打开文件管理器
4. 点击APK文件进行安装

---

### 方法2：使用命令行

#### 前提条件
- 已安装JDK 8或更高版本
- 已安装Android SDK
- 已设置环境变量

#### 步骤1：配置环境
```bash
# 设置Android SDK路径（根据你的实际安装位置）
export ANDROID_HOME=$HOME/Library/Android/sdk  # macOS
# export ANDROID_HOME=$HOME/Android/Sdk  # Linux
# export ANDROID_HOME=%LOCALAPPDATA%\Android\Sdk  # Windows

# 添加到PATH
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

#### 步骤2：构建APK
```bash
cd step-analyzer-android

# 给构建脚本执行权限
chmod +x build.sh

# 运行构建
./build.sh
```

#### 步骤3：安装APK
```bash
# 确保设备已连接且开启USB调试
adb devices

# 安装APK
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## Android设备设置

### 开启USB调试
1. 打开设备的"设置"
2. 进入"关于手机"
3. 连续点击"版本号"7次
4. 返回"设置"主菜单
5. 进入"开发者选项"
6. 开启"USB调试"

### 安装未知来源应用
1. 打开"设置" > "安全"
2. 开启"未知来源"选项（不同设备位置可能不同）

---

## 常见问题

### Q: Android Studio提示"SDK not found"
**A:** 在Android Studio中配置SDK路径：
1. 打开 "File" > "Project Structure"
2. 选择 "SDK Location"
3. 设置Android SDK路径

### Q: 构建时报错"Could not resolve..."
**A:** 检查网络连接，可能需要配置代理或使用国内镜像源。

### Q: 设备无法识别
**A:** 
1. 确保已开启USB调试
2. 尝试更换USB数据线
3. 重启ADB服务：`adb kill-server && adb start-server`

### Q: 安装时提示"签名冲突"
**A:** 
1. 先卸载旧版本（如果已安装）
2. 或者在build.gradle中修改applicationId

### Q: 应用闪退
**A:**
1. 检查设备Android版本是否≥7.0
2. 查看Logcat日志：`adb logcat | grep stepanalyzer`
3. 重新构建安装

---

## 高级配置

### 修改应用图标
1. 准备108x108像素的PNG图片
2. 替换 `app/src/main/res/mipmap-*/ic_launcher.png`

### 修改应用名称
编辑 `app/src/main/res/values/strings.xml`：
```xml
<string name="app_name">你的应用名称</string>
```

### 修改包名
编辑 `app/build.gradle`：
```groovy
applicationId "com.yourcompany.yourapp"
```
同时修改 `app/src/main/AndroidManifest.xml` 中的包名。

---

## 技术支持

如果遇到问题：
1. 查看Android Studio的错误提示
2. 检查Gradle同步日志
3. 搜索错误信息
4. 在GitHub上提交Issue

---

**祝使用愉快！** 🚀
