package com.example.myemail;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myemail.pojo.Result;
import com.google.gson.Gson;

import com.example.myemail.utils.NetworkUtils;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText nicknameEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private Button registerButton;

    String apiUrl = getResources().getString(R.string.api);

    String domain = getResources().getString(R.string.domain);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.et_register_username);
        nicknameEditText = findViewById(R.id.et_register_nickname);
        passwordEditText = findViewById(R.id.et_register_passwd);
        passwordConfirmEditText = findViewById(R.id.et_register_passwd_confirm);
        registerButton = findViewById(R.id.btn_register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String nickname = nicknameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String passwordConfirm = passwordConfirmEditText.getText().toString();

                //检查是否有空值
                if (username.isEmpty() || nickname.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                    //Snackbar.make(v, "请填写完整注册信息", Snackbar.LENGTH_LONG).show();
                    Toast.makeText(RegisterActivity.this, "请填写完整注册信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 检查用户名是否合法
                if (!username.matches("[a-zA-Z0-9]+")) {
                    //Snackbar.make(v, "用户名只能包含字母和数字", Snackbar.LENGTH_LONG).show();
                    Toast.makeText(RegisterActivity.this, "用户名只能包含字母和数字", Toast.LENGTH_SHORT).show();
                    return;
                }

                //检查用户名长度是否大于10
                if (username.length() > 10) {
                    //Snackbar.make(v, "用户名长度不能大于10", Snackbar.LENGTH_LONG).show();
                    Toast.makeText(RegisterActivity.this, "用户名长度不能大于10", Toast.LENGTH_SHORT).show();
                    return;
                }

                //检查昵称长度是否大于10
                if (nickname.length() > 10) {
                    //Snackbar.make(v, "昵称长度不能大于10", Snackbar.LENGTH_LONG).show();
                    Toast.makeText(RegisterActivity.this, "昵称长度不能大于10", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 检查密码是否一致
                if (!password.equals(passwordConfirm)) {
                    //Snackbar.make(v, "两次输入的密码不一致", Snackbar.LENGTH_LONG).show();
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    // Handle successful registration here
                    register(username + domain, nickname, password, v);
                } catch (Exception e){
                    //Snackbar.make(v, "注册失败", Snackbar.LENGTH_LONG).show();
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void register(String username, String nickname, String password, View v) {
        String url = apiUrl + "/user/register";
        String json = "{\"username\":\"" + username + "\",\"nickname\":\"" + nickname
                + "\",\"password\":\"" + password + "\"}";

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = NetworkUtils.post(url, json);
                    Gson gson = new Gson();
                    Result response = gson.fromJson(result, Result.class);
                    int code = response.getCode();
                    String message = response.getMessage();
                    Object data = response.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (code == 0) {
                                //Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                //Snackbar.make(v, "注册成功", Snackbar.LENGTH_LONG).show();
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                //暂停1秒，让用户看清注册成功提示
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                finish();  // 注册成功后结束RegisterActivity
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Snackbar.make(v, "网络连接失败", Snackbar.LENGTH_LONG).show();
                            Toast.makeText(RegisterActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

}