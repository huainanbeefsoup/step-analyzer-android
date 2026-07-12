# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/Cellar/android-sdk/24.3.3/tools/proguard/proguard-android.txt

# Keep the application class
-keep class com.stepanalyzer.app.** { *; }

# Keep WebView JavaScript interface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep AndroidX classes
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# Keep Material Design components
-keep class com.google.android.material.** { *; }
