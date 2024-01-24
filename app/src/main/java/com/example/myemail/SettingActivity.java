package com.example.myemail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myemail.pojo.Result;
import com.example.myemail.pojo.User;
import com.example.myemail.utils.NetworkUtils;
import com.google.gson.Gson;

import java.io.IOException;

public class SettingActivity extends AppCompatActivity {

    String apiUrl = getResources().getString(R.string.api);

    String domain = getResources().getString(R.string.domain);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        boolean isLogged = sp.getBoolean("isLogged", false); // 获取登录标志
        if (!isLogged) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        TextView tvUsername = findViewById(R.id.username_setting);
        TextView tvNickname = findViewById(R.id.nickname_setting);
        TextView tvSpace = findViewById(R.id.usespace_setting);

        String username = sp.getString("username", "");
        String nickname = sp.getString("nickname", "");
        String password = sp.getString("password", "");

        //获取用户信息
        String url = apiUrl + "/user/login"
                + "?username=" + username + "&password=" + password;

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
                                tvSpace.setText(user.getUsedSize() + "MB / " + user.getLimitSize() + "MB");
                            }
                            else {
                                Toast.makeText(SettingActivity.this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SettingActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

        tvUsername.setText(username);
        tvNickname.setText(nickname);

        EditText etOldPassword = findViewById(R.id.old_passwd);
        EditText etNewPassword = findViewById(R.id.new_passwd);
        EditText etConfirmPassword = findViewById(R.id.confirm_new_passwd);

        Button btnChangePassword = findViewById(R.id.btn_change_passwd);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = etOldPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SettingActivity.this, "输入不能有空", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(SettingActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    //=================TO DO修改密码=================
                    String url = apiUrl + "/user/updatePassword?username="
                            + username + "&oldpassword=" + oldPassword + "&newpassword=" + newPassword;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String response = NetworkUtils.get(url);
                                Result result = new Gson().fromJson(response, Result.class);
                                if (result.getCode() == 0) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SettingActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SettingActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                                            //修改成功后清除用户的登录信息
                                            SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.clear();
                                            editor.apply();
                                            // 跳转到登录页面
                                            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            } catch (IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SettingActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });


        if (username == "admin" + domain) {
            //=================TO DO显示管理员界面=================
        }
        else {
            //=================TO DO显示普通用户界面=================
        }

        Button btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清除用户的登录信息
                // 这里假设你使用SharedPreferences来保存用户的登录信息
                SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // 跳转到登录页面
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
