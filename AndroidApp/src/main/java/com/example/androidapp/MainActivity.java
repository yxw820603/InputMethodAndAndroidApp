package com.example.androidapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;

import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private StatusManager statusManager = new StatusManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toggleButton = findViewById(R.id.toggle_button);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusManager.toggleStatus();
                if (statusManager.isEnabled()) {
                    toggleButton.setText("Stop");
                } else {
                    toggleButton.setText("Start");
                }

                EditText multicastAddressField = findViewById(R.id.multicast_address);
                String multicastAddress = multicastAddressField.getText().toString();
                String message = "Input method state changed";
                UdpSender udpSender = new UdpSender();
                udpSender.sendMessage(message, multicastAddress, 4446);
            }
        });

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                EditText multicastAddressField = findViewById(R.id.multicast_address);
                String multicastAddress = multicastAddressField.getText().toString();

                EditText multilineTextField = findViewById(R.id.multiline_text);
                String message = multilineTextField.getText().toString();

                if (statusManager.isEnabled()) {
                    UdpSender udpSender = new UdpSender();
                    udpSender.sendMessage(message, multicastAddress, 4446);
                }

                handler.postDelayed(this, 5000); // Check every 5 seconds
            }
        };
        handler.post(runnable);
    }
}
