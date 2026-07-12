FROM eclipse-temurin:17-jdk

# 安装必要的工具
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    git \
    && rm -rf /var/lib/apt/lists/*

# 设置 Android SDK 环境变量
ENV ANDROID_HOME=/opt/android-sdk
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools

# 下载并安装 Android SDK 命令行工具
RUN mkdir -p $ANDROID_HOME/cmdline-tools && \
    cd $ANDROID_HOME/cmdline-tools && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-10406996_latest.zip -O cmdline-tools.zip && \
    unzip -q cmdline-tools.zip && \
    mv cmdline-tools latest && \
    rm cmdline-tools.zip

# 接受所有 SDK 许可证
RUN yes | sdkmanager --licenses

# 安装必要的 SDK 组件
RUN sdkmanager \
    "platforms;android-34" \
    "build-tools;34.0.0" \
    "platform-tools"

# 设置工作目录
WORKDIR /app

# 复制项目文件
COPY . .

# 给 gradlew 执行权限
RUN chmod +x gradlew

# 构建 APK
RUN ./gradlew assembleDebug

# 默认命令：复制 APK 到输出目录
CMD ["sh", "-c", "cp app/build/outputs/apk/debug/app-debug.apk /output/"]
