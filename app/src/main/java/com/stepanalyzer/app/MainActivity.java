package com.stepanalyzer.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_FILE_PICK = 1001;
    private static final int REQUEST_PERMISSION = 1002;
    
    private MaterialToolbar toolbar;
    private MaterialCardView fileSelectCard;
    private TextView tvSelectFile;
    private TextView tvFileName;
    private MaterialCardView optionsCard;
    private Spinner spinnerDirection;
    private MaterialButton btnAnalyze;
    private ProgressBar progressBar;
    private MaterialCardView resultCard;
    private LinearLayout resultsContainer;
    private MaterialButton btnView3D;
    private MaterialButton btnExport;
    
    private Uri selectedFileUri;
    private String selectedFileName;
    private String[] drawDirections = {"Z轴正方向（默认）", "Z轴负方向", "X轴正方向", "X轴负方向", "Y轴正方向", "Y轴负方向"};
    private float[] drawDirectionValues = {0, 0, 1, 0, 0, -1, 1, 0, 0, -1, 0, 0, 0, 1, 0, 0, -1, 0};
    private int selectedDirectionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        setupListeners();
        setupSpinner();
        
        // 检查是否有传入的文件（通过Intent打开）
        handleIncomingIntent(getIntent());
    }
    
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        fileSelectCard = findViewById(R.id.fileSelectCard);
        tvSelectFile = findViewById(R.id.tvSelectFile);
        tvFileName = findViewById(R.id.tvFileName);
        optionsCard = findViewById(R.id.optionsCard);
        spinnerDirection = findViewById(R.id.spinnerDirection);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        progressBar = findViewById(R.id.progressBar);
        resultCard = findViewById(R.id.resultCard);
        resultsContainer = findViewById(R.id.resultsContainer);
        btnView3D = findViewById(R.id.btnView3D);
        btnExport = findViewById(R.id.btnExport);
    }
    
    private void setupListeners() {
        // 文件选择点击事件
        fileSelectCard.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                openFilePicker();
            } else {
                requestStoragePermission();
            }
        });
        
        // 分析按钮点击事件
        btnAnalyze.setOnClickListener(v -> startAnalysis());
        
        // 3D查看按钮点击事件
        btnView3D.setOnClickListener(v -> open3DViewer());
        
        // 导出按钮点击事件
        btnExport.setOnClickListener(v -> exportReport());
        
        // 工具栏菜单点击事件
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_history) {
                showHistory();
                return true;
            } else if (id == R.id.action_settings) {
                openSettings();
                return true;
            }
            return false;
        });
    }
    
    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this, 
            android.R.layout.simple_spinner_item, 
            drawDirections
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDirection.setAdapter(adapter);
        
        spinnerDirection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDirectionIndex = position;
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    
    private void handleIncomingIntent(Intent intent) {
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri fileUri = intent.getData();
            if (fileUri != null) {
                selectedFileUri = fileUri;
                selectedFileName = getFileName(fileUri);
                updateFileUI();
            }
        }
    }
    
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimeTypes = {
            "application/step",
            "application/stp",
            "application/x-step",
            "application/octet-stream"
        };
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        
        try {
            startActivityForResult(Intent.createChooser(intent, "选择STEP文件"), REQUEST_FILE_PICK);
        } catch (Exception e) {
            Toast.makeText(this, "无法打开文件选择器", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_FILE_PICK && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                selectedFileUri = data.getData();
                selectedFileName = getFileName(selectedFileUri);
                updateFileUI();
            }
        }
    }
    
    private void updateFileUI() {
        tvSelectFile.setText("点击更换文件");
        tvFileName.setText(selectedFileName);
        tvFileName.setVisibility(View.VISIBLE);
        optionsCard.setVisibility(View.VISIBLE);
        btnAnalyze.setEnabled(true);
    }
    
    private String getFileName(Uri uri) {
        String fileName = "未知文件";
        
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (cursor.moveToFirst() && nameIndex >= 0) {
                    fileName = cursor.getString(nameIndex);
                }
                cursor.close();
            }
        } else if (uri.getScheme().equals("file")) {
            fileName = new File(uri.getPath()).getName();
        }
        
        return fileName;
    }
    
    private void startAnalysis() {
        if (selectedFileUri == null) {
            Toast.makeText(this, R.string.no_file_selected, Toast.LENGTH_SHORT).show();
            return;
        }
        
        // 显示进度条
        progressBar.setVisibility(View.VISIBLE);
        btnAnalyze.setEnabled(false);
        
        // 模拟分析过程（实际应该调用后端API）
        new Thread(() -> {
            try {
                // 模拟分析延迟
                Thread.sleep(2000);
                
                // 在UI线程中更新界面
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnAnalyze.setEnabled(true);
                    showAnalysisResults();
                    Toast.makeText(this, R.string.analysis_complete, Toast.LENGTH_SHORT).show();
                });
            } catch (InterruptedException e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnAnalyze.setEnabled(true);
                    Toast.makeText(this, R.string.analysis_failed, Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    
    private void showAnalysisResults() {
        resultCard.setVisibility(View.VISIBLE);
        resultsContainer.removeAllViews();
        
        // 模拟分析结果（实际应该从API获取）
        String[][] results = {
            {"拔模角分析", "发现3个面拔模角不足", "fail"},
            {"壁厚分析", "平均壁厚2.5mm，分布均匀", "pass"},
            {"倒扣分析", "发现1个倒扣特征", "warning"},
            {"分型面分析", "找到2个候选分型面", "info"},
            {"模具报价", "预估成本¥15,000，周期20天", "info"},
            {"机台推荐", "推荐使用160T注塑机", "info"}
        };
        
        for (String[] result : results) {
            View itemView = getLayoutInflater().inflate(R.layout.item_result, resultsContainer, false);
            
            TextView tvStatusIcon = itemView.findViewById(R.id.tvStatusIcon);
            TextView tvAnalysisName = itemView.findViewById(R.id.tvAnalysisName);
            TextView tvAnalysisMessage = itemView.findViewById(R.id.tvAnalysisMessage);
            TextView tvStatus = itemView.findViewById(R.id.tvStatus);
            
            tvAnalysisName.setText(result[0]);
            tvAnalysisMessage.setText(result[1]);
            
            switch (result[2]) {
                case "pass":
                    tvStatusIcon.setText("✅");
                    tvStatus.setText("通过");
                    tvStatus.setTextColor(getColor(R.color.status_pass));
                    tvStatus.setBackgroundColor(getColor(R.color.status_pass) & 0x20FFFFFF);
                    break;
                case "warning":
                    tvStatusIcon.setText("⚠️");
                    tvStatus.setText("警告");
                    tvStatus.setTextColor(getColor(R.color.status_warning));
                    tvStatus.setBackgroundColor(getColor(R.color.status_warning) & 0x20FFFFFF);
                    break;
                case "fail":
                    tvStatusIcon.setText("❌");
                    tvStatus.setText("失败");
                    tvStatus.setTextColor(getColor(R.color.status_fail));
                    tvStatus.setBackgroundColor(getColor(R.color.status_fail) & 0x20FFFFFF);
                    break;
                default:
                    tvStatusIcon.setText("ℹ️");
                    tvStatus.setText("信息");
                    tvStatus.setTextColor(getColor(R.color.status_info));
                    tvStatus.setBackgroundColor(getColor(R.color.status_info) & 0x20FFFFFF);
                    break;
            }
            
            resultsContainer.addView(itemView);
        }
    }
    
    private void open3DViewer() {
        Intent intent = new Intent(this, ViewerActivity.class);
        intent.putExtra("file_name", selectedFileName);
        startActivity(intent);
    }
    
    private void exportReport() {
        Toast.makeText(this, "导出报告功能开发中...", Toast.LENGTH_SHORT).show();
    }
    
    private void showHistory() {
        Toast.makeText(this, "历史记录功能开发中...", Toast.LENGTH_SHORT).show();
    }
    
    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    
    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) 
                == PackageManager.PERMISSION_GRANTED;
    }
    
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, 
            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 
            REQUEST_PERMISSION);
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            } else {
                Toast.makeText(this, "需要存储权限才能选择文件", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
