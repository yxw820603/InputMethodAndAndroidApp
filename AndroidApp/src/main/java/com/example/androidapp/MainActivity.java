package com.example.androidapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private EditText multiLineTextBox;
    private EditText multicastAddress;
    private EditText multicastPort;
    private Button toggleButton;
    private UdpSender udpSender;
    private Handler handler;
    private boolean isRunning = false;
    private Thread workerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        multiLineTextBox = findViewById(R.id.multiLineTextBox);
        multicastAddress = findViewById(R.id.multicastAddress);
        multicastPort = findViewById(R.id.multicastPort);
        toggleButton = findViewById(R.id.toggleButton);

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    stopSending();
                } else {
                    startSending();
                }
            }
        });

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // Handle UI updates if needed
            }
        };
    }

    private void startSending() {
        String address = multicastAddress.getText().toString();
        int port = Integer.parseInt(multicastPort.getText().toString());

        try {
            udpSender = new UdpSender(address, port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String lastText = multiLineTextBox.getText().toString();
                while (isRunning) {
                    try {
                        Thread.sleep(5000);
                        String currentText = multiLineTextBox.getText().toString();
                        if (!currentText.equals(lastText)) {
                            String changedText = currentText.substring(lastText.length());
                            udpSender.send(changedText);
                            lastText = currentText;
                        }
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        isRunning = true;
        toggleButton.setText("Stop");
        workerThread.start();
    }

    private void stopSending() {
        isRunning = false;
        if (udpSender != null) {
            udpSender.close();
            udpSender = null;
        }
        toggleButton.setText("Start");
    }
}
