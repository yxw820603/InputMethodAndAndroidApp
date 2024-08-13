package com.example.androidapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText multicastAddress = findViewById(R.id.multicastAddress);
        Button toggleButton = findViewById(R.id.toggleButton);

        toggleButton.setOnClickListener(v -> {
            // 处理按钮点击事件
        });
    }
}
