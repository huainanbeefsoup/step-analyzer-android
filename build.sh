#!/bin/bash

# STEP模具分析 - Android构建脚本

echo "=========================================="
echo "    STEP模具分析 - Android APK构建"
echo "=========================================="
echo ""

# 检查是否安装了Java
if ! command -v java &> /dev/null; then
    echo "❌ 错误：未找到Java"
    echo "请安装JDK 8或更高版本"
    echo "下载地址：https://adoptium.net/"
    exit 1
fi

# 检查是否安装了Android SDK
if [ -z "$ANDROID_HOME" ] && [ -z "$ANDROID_SDK_ROOT" ]; then
    echo "⚠️  警告：未设置ANDROID_HOME环境变量"
    echo "如果你使用Android Studio，请确保SDK已正确配置"
fi

echo "🔨 开始构建..."
echo ""

# 清理之前的构建
if [ -d "app/build" ]; then
    echo "🧹 清理之前的构建文件..."
    ./gradlew clean
fi

# 构建Debug版本
echo "📦 构建Debug版本..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ 构建成功！"
    echo ""
    echo "📁 APK文件位置："
    echo "   app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "📱 安装方法："
    echo "   1. 连接Android设备（开启USB调试）"
    echo "   2. 运行命令：adb install app/build/outputs/apk/debug/app-debug.apk"
    echo "   或者直接将APK文件传输到设备上安装"
    echo ""
else
    echo ""
    echo "❌ 构建失败"
    echo "请检查错误信息并修复后重试"
    exit 1
fi
