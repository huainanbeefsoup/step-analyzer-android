package com.stepanalyzer.app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ViewerActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private WebView webView;
    private LinearLayout loadingView;
    private MaterialButton btnReset;
    private MaterialButton btnWireframe;
    private MaterialButton btnScreenshot;
    
    private boolean wireframeMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        
        initViews();
        setupWebView();
        setupListeners();
        
        // 加载3D查看器HTML
        loadViewer();
    }
    
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        // 设置返回按钮
        toolbar.setNavigationOnClickListener(v -> finish());
        
        webView = findViewById(R.id.webView);
        loadingView = findViewById(R.id.loadingView);
        btnReset = findViewById(R.id.btnReset);
        btnWireframe = findViewById(R.id.btnWireframe);
        btnScreenshot = findViewById(R.id.btnScreenshot);
    }
    
    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingView.setVisibility(View.GONE);
            }
        });
        
        webView.setWebChromeClient(new WebChromeClient());
    }
    
    private void setupListeners() {
        btnReset.setOnClickListener(v -> {
            webView.evaluateJavascript("resetCamera()", null);
        });
        
        btnWireframe.setOnClickListener(v -> {
            wireframeMode = !wireframeMode;
            webView.evaluateJavascript("toggleWireframe()", null);
            btnWireframe.setText(wireframeMode ? "实体模式" : "线框模式");
        });
        
        btnScreenshot.setOnClickListener(v -> takeScreenshot());
    }
    
    private void loadViewer() {
        // 使用assets中的HTML文件
        webView.loadUrl("file:///android_asset/step_viewer.html");
        
        // 或者加载内嵌的HTML（如果assets中没有文件）
        // String html = generateViewerHTML();
        // webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }
    
    private void takeScreenshot() {
        webView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(webView.getDrawingCache());
        webView.setDrawingCacheEnabled(false);
        
        // 保存截图
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), 
                "step_analysis_" + System.currentTimeMillis() + ".png");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            
            Toast.makeText(this, "截图已保存: " + file.getName(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "保存截图失败", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
