package com.example.myemail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myemail.utils.NetworkUtils;
import com.example.myemail.pojo.Result;
import com.example.myemail.pojo.User;
import com.google.gson.Gson;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    private Button registerButton;

    String apiUrl = getResources().getString(R.string.api);

    String domain = getResources().getString(R.string.domain);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.et_login_username);
        passwordEditText = findViewById(R.id.et_login_passwd);
        loginButton = findViewById(R.id.btn_login);
        registerButton = findViewById(R.id.btn_register);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                login(username, password, v);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到注册页面
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                // 启动目标Activity
                startActivity(intent);
            }
        });
    }

    private void login(String username, String password, View v) {
        String url = apiUrl + "/user/login"
                + "?username=" + username + domain + "&password=" + password;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = NetworkUtils.get(url);
                    Result result = new Gson().fromJson(response, Result.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(result.getCode() == 1) {
                                User user = new Gson().fromJson(result.getData().toString(), User.class);
                                if (user.getStatus() == 0) {
                                    Toast.makeText(LoginActivity.this, "您的账号已被禁用", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                saveUserInfo(user.getId(), user.getUsername(), password, user.getNickname());
                                //Snackbar.make(v, "用户"+username+"登录成功", Snackbar.LENGTH_LONG).show();
                                Toast.makeText(LoginActivity.this, "用户"+user.getUsername()+"登录成功", Toast.LENGTH_SHORT).show();
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                //Snackbar.make(v, result.getMessage(), Snackbar.LENGTH_LONG).show();
                                Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Snackbar.make(v, "网络连接失败", Snackbar.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void saveUserInfo(Integer id, String username, String password, String nickname) {
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("id", id);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("nickname", nickname);
        editor.putBoolean("isLogged", true);
        editor.apply();
    }

}
