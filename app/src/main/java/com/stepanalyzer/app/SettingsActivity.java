package com.stepanalyzer.app;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class SettingsActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private Spinner spinnerLanguage;
    private Switch switchAutoSave;
    private Switch switchHighQuality;
    private Spinner spinnerTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        initViews();
        setupSpinners();
        loadSettings();
    }
    
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        
        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        switchAutoSave = findViewById(R.id.switchAutoSave);
        switchHighQuality = findViewById(R.id.switchHighQuality);
        spinnerTheme = findViewById(R.id.spinnerTheme);
    }
    
    private void setupSpinners() {
        // 语言选择
        String[] languages = {"中文", "English", "日本語"};
        ArrayAdapter<String> langAdapter = new ArrayAdapter<>(
            this, 
            android.R.layout.simple_spinner_item, 
            languages
        );
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(langAdapter);
        
        // 主题选择
        String[] themes = {"浅色主题", "深色主题", "跟随系统"};
        ArrayAdapter<String> themeAdapter = new ArrayAdapter<>(
            this, 
            android.R.layout.simple_spinner_item, 
            themes
        );
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTheme.setAdapter(themeAdapter);
    }
    
    private void loadSettings() {
        // 从SharedPreferences加载设置
        // 这里先使用默认值
        switchAutoSave.setChecked(true);
        switchHighQuality.setChecked(true);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        saveSettings();
    }
    
    private void saveSettings() {
        // 保存设置到SharedPreferences
        // 这里先留空，实际实现时再添加
    }
}
