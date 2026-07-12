#!/bin/bash

# ===========================================
#    STEP模具分析 - 一键构建APK脚本
#    完全自动化，无需配置任何环境
# ===========================================

set -e

echo "🚀 开始构建 Android APK..."
echo ""

# 创建输出目录
OUTPUT_DIR="/vol1/1000/Qwen/apk-output"
mkdir -p "$OUTPUT_DIR"

# 项目目录
PROJECT_DIR="/vol1/1000/Qwen/step-analyzer-android"

echo "📦 步骤 1/3: 准备 Docker 环境..."
echo ""

# 构建 Docker 镜像（包含 Android SDK）
docker build -t step-analyzer-builder "$PROJECT_DIR"

echo ""
echo "🔨 步骤 2/3: 编译 APK..."
echo ""

# 运行 Docker 容器构建 APK
docker run --rm \
    -v "$OUTPUT_DIR:/output" \
    step-analyzer-builder

echo ""
echo "✅ 步骤 3/3: 完成！"
echo ""

# 检查 APK 是否生成
APK_FILE="$OUTPUT_DIR/app-debug.apk"
if [ -f "$APK_FILE" ]; then
    echo "=========================================="
    echo "🎉 APK 构建成功！"
    echo "=========================================="
    echo ""
    echo "📁 APK 文件位置："
    echo "   $APK_FILE"
    echo ""
    echo "📊 文件大小："
    ls -lh "$APK_FILE" | awk '{print "   " $5}'
    echo ""
    echo "📱 安装方法："
    echo "   1. 将 APK 文件传输到 Android 手机"
    echo "   2. 在手机上点击安装"
    echo ""
    echo "=========================================="
else
    echo "❌ 构建失败，未找到 APK 文件"
    exit 1
fi
