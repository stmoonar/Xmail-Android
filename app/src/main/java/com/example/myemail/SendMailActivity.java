package com.example.myemail;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myemail.utils.EmailValidator;
import com.example.myemail.utils.SendEmail;

import java.util.Arrays;

public class SendMailActivity extends AppCompatActivity {

    private EditText etTo;
    private EditText etSubject;
    private EditText etContent;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmail);

        etTo = findViewById(R.id.et_to);
        String receiver = getIntent().getStringExtra("receiver");
        if (receiver != null && !receiver.isEmpty()) {
            etTo.setText(receiver);
        }
        etSubject = findViewById(R.id.et_subject);
        etContent = findViewById(R.id.et_content);
        btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to = etTo.getText().toString();
                String subject = etSubject.getText().toString();
                String content = etContent.getText().toString();

                if (to.isEmpty() || subject.isEmpty() || content.isEmpty()) {
                    Toast.makeText(SendMailActivity.this, "收件人、主题和内容不能有空项", Toast.LENGTH_SHORT).show();
                    return;
                }

                //检查to是否由;分隔的多个收件人且每个收件人都是合法的
                String[] toList = to.split(";");
                for (String s : toList) {
                    if (!EmailValidator.isValid(s)) {
                        Toast.makeText(SendMailActivity.this, "收件人格式不正确", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                SendEmail sendEmail = new SendEmail();
                //====================TO DO====================
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //使按钮不可点击
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnSend.setEnabled(false);
                            }
                        });
                        if (sendEmail.sendEmail(SendMailActivity.this, Arrays.asList(toList), subject, content)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SendMailActivity.this, "已投递", Toast.LENGTH_SHORT).show();
                                    //暂停2秒后关闭当前Activity
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 1000);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SendMailActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                    //使按钮可点击
                                    btnSend.setEnabled(true);
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}