package com.example.androidapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextWatcher;
import android.text.Editable;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.text.SpanWatcher;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;

public class MainActivity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private EditText multiLineTextBox;
    private EditText multicastAddress;
    private EditText multicastPort;
    private Button toggleButton;
    private UdpSender udpSender;

    private String addr;
    private int port;

    private String oldNormal = "";

    void SendNormalString2(Editable s) {
        if (s instanceof Spannable) {
            Spannable spannable = (Spannable) s;
            UnderlineSpan[] underlineSpans = spannable.getSpans(0, spannable.length(), UnderlineSpan.class);

            Object[] os = spannable.getSpans(0, spannable.length(), Object.class);
            Object spanComposing = null;
            for (Object o : os) {
                int flags = spannable.getSpanFlags(o);
                if ((flags & Spannable.SPAN_COMPOSING) == Spannable.SPAN_COMPOSING) {
                    spanComposing = o;
                }
            }

            // TODO should check spanComposing == null here

            // 这里假设了只会出现一个有下划线的部分，而且位于内容的最后
            StringBuilder normalText = new StringBuilder();
            StringBuilder underlineText = new StringBuilder();

            Log.v("yxwdebug", "---------------------------------------------------");

            if (underlineSpans.length == 1) {
                UnderlineSpan span = underlineSpans[0];

                int spanStart = spannable.getSpanStart(spanComposing);
                int spanEnd = spannable.getSpanEnd(spanComposing);

                // Log.v("yxwdebug", "|start - end:" + spanStart + " - " + spanEnd);

                if (spanStart >= 0 && spanEnd > spanStart) {
                    normalText.append(spannable.subSequence(0, spanStart));
                    underlineText.append(spannable.subSequence(spanStart, spanEnd));
                }
            }

            // Log.v("yxwdebug", "|underlineSpans.length:" + underlineSpans.length);
            // Log.v("yxwdebug", "|normal:" + normalText.toString());
            // Log.v("yxwdebug", "|unline:" + underlineText.toString());

            // ??????
            String newNormal = normalText.toString(); // 临时保存完整字符串
            normalText.replace(0, oldNormal.length(), ""); // 把与旧字符串相同的部分替换，就剩下这次变化的了
            String mustsend = normalText.toString();

            oldNormal = newNormal;

            Log.v("yxwdebug", "|mustsend:" + mustsend);
            Log.v("yxwdebug", "|oldNormal:" + oldNormal);

            Log.v("yxwdebug", "---------------------------------------------------");

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.v("yxwdebug", "=====>in thread sending:" + mustsend);
                        udpSender.send(mustsend);
                    } catch (Exception e) {
                        Log.v("yxwdebug", "=====>in thread err:" + e.toString());
                        e.printStackTrace();
                    } finally {
                    }
                }
            });

        }

    }

    // not used
    void SendNormalString(Editable s) {
        // String after = "";
        // if (s instanceof Spannable) {
        // Spannable spannable = (Spannable) s;
        // UnderlineSpan[] underlineSpans = spannable.getSpans(0, spannable.length(),
        // UnderlineSpan.class);

        // StringBuilder normalText = new StringBuilder();

        // int lastEnd = 0;
        // for (UnderlineSpan span : underlineSpans) {
        // int spanStart = spannable.getSpanStart(span);
        // int spanEnd = spannable.getSpanEnd(span);

        // if (lastEnd < spanStart) {
        // normalText.append(spannable.subSequence(lastEnd, spanStart));
        // }

        // lastEnd = spanEnd;
        // }

        // // 添加最后一个下划线之后的文本
        // if (lastEnd < spannable.length()) {
        // normalText.append(spannable.subSequence(lastEnd, spannable.length()));
        // }

        // after = normalText.toString();
        // }

        // if (after.length() > beforeStr.length()) {
        // String temp = aftoldNormaling(beforeStr.length());

        // if (temp.length() > 0) {

        // executorService.submit(new Runnable() {
        // @Override
        // public void run() {
        // try {
        // udpSender.send(temp);
        // } catch (Exception e) {
        // String emsg = e.toString();
        // e.printStackTrace();
        // } finally {
        // }
        // }
        // });

        // }
        // }
        // oldNormal = after;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        multiLineTextBox = findViewById(R.id.multiLineTextBox);
        multicastAddress = findViewById(R.id.multicastAddress);
        multicastPort = findViewById(R.id.multicastPort);
        toggleButton = findViewById(R.id.toggleButton);

        Spannable sa = (Spannable) multiLineTextBox.getText();
        sa.setSpan(new SpanWatcher() {
            @Override
            public void onSpanAdded(Spannable spannable, Object what, int start, int end) {
            }

            @Override
            public void onSpanRemoved(Spannable spannable, Object what, int start, int end) {
                // Log.v("yxwdebug", "onSpanRemoved........");
                // Log.v("yxwdebug", "\t|" + what.toString());
                // Log.v("yxwdebug", "\t|" + spannable.toString());
                // Log.v("yxwdebug", "\t|start - end:" + start + " - " + end);
                // Log.v("yxwdebug", "\t|edittext:" + multiLineTextBox.getText().toString());

                // try {
                // int a = 1;
                // a--;
                // a = a / a;
                // } catch (Exception e) {
                // e.printStackTrace();
                // }

            }

            @Override
            public void onSpanChanged(Spannable spannable, Object what, int start, int end, int newStart, int newEnd) {
            }
        }, 0, sa.length(), sa.SPAN_INCLUSIVE_INCLUSIVE);

        multiLineTextBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // InputConnection ic = multiLineTextBox.onCreateInputConnection(new
                // EditorInfo());
                // BaseInputConnection bic = (BaseInputConnection)ic;

                // try {
                // Log.v("yxwdebug", "start before count:===>" + start + " " + before + " " +
                // count);

                // CharSequence normal = s.subSequence(0, start);
                // CharSequence changed = s.subSequence(start, start + count);

                // if (changed instanceof Spannable) {
                // Spannable sc = (Spannable) changed;

                // Object[] composingSpans = sc.getSpans(0, sc.length(), Object.class);
                // for (Object span : composingSpans) {
                // int flags = sc.getSpanFlags(span);
                // if ((flags & Spannable.SPAN_COMPOSING) == Spannable.SPAN_COMPOSING) {

                // // InputMethodManager imm = (InputMethodManager) getSystemService(
                // // Context.INPUT_METHOD_SERVICE);
                // // if (imm.isAcceptingText()) {
                // // Log.v("yxwdebug", "isAcceptingText == true");
                // // } else {
                // // Log.v("yxwdebug", "isAcceptingText == false");
                // // }
                // // Log.v("yxwdebug", "Text is composing,flag:" + flags + " objtype" +
                // // span.toString());
                // }

                // // if (isComposingSpan(span)) {
                // // Log.v("TextChange", "Text is composing");
                // // }
                // }
                // }

                // // multiLineTextBox.getSpan

                // if (s == null) {
                // Log.v("yxwdebug", "s null");
                // }

                // if (normal == null) {
                // Log.v("yxwdebug", "normal null");
                // }

                // if (changed == null) {
                // Log.v("yxwdebug", "changed null");
                // }
                // Log.v("yxwdebug", "====>full!!:" + s.toString());
                // Log.v("yxwdebug", "====>normal:" + normal + " \r\n---->change:" + changed);
                // Log.v("yxwdebug", "-----------------------");

                // } catch (Exception e) {
                // String ee = e.toString();
                // }

                // SendNormalString(multiLineTextBox.getText());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SendNormalString2(multiLineTextBox.getText());
            }
        });

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applySetting();
            }
        });

        applySetting();
    }

    private void applySetting() {
        addr = multicastAddress.getText().toString();
        port = Integer.parseInt(multicastPort.getText().toString());

        try {
            udpSender = new UdpSender(addr, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
