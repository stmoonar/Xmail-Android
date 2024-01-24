package com.example.myemail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myemail.pojo.Mail;
import com.example.myemail.pojo.Result;
import com.example.myemail.utils.EmailAdapter;
import com.example.myemail.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

public class TrashActivity extends AppCompatActivity {

    String apiUrl = getResources().getString(R.string.api);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        String username = sp.getString("username", "");

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = apiUrl + "/mail/listDeleted?"
                        + "username=" + username;
                try {
                    String response = NetworkUtils.get(url);
                    Result result = new Gson().fromJson(response, Result.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.getCode() == 1) {
                                Toast.makeText(TrashActivity.this, "获取回收站成功", Toast.LENGTH_SHORT).show();
                                //显示邮件列表
                                showEmails(result.getData());
                            } else {
                                Toast.makeText(TrashActivity.this, "获取回收站失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TrashActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    protected void showEmails(Object data) {
        List<Mail> mailList = new Gson().fromJson(new Gson().toJson(data),
                new TypeToken<List<Mail>>(){}.getType());
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EmailAdapter adapter = new EmailAdapter(mailList, new EmailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 在这里处理邮件点击事件，例如打开一个新的活动来显示邮件的详细内容
                Toast.makeText(TrashActivity.this, "正在打开该邮件", Toast.LENGTH_SHORT).show();
                Log.d("InboxActivity", "onItemClick: " + mailList.get(position).getId());
                final String url = apiUrl + "/mail/getById?"
                        + "id=" + mailList.get(position).getId();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("InboxActivity", "Sending request to: " + url);
                            String response = NetworkUtils.get(url);
                            Log.d("InboxActivity", "Received response: " + response);
                            Log.d("InboxActivity", "run:" + response);
                            Result result = new Gson().fromJson(response, Result.class);
                            Mail mail = new Gson().fromJson(new Gson().toJson(result.getData()), Mail.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //打开FullEmailActivity
                                    Intent intent = new Intent(TrashActivity.this, FullEmailActivity.class);
                                    intent.putExtra("mail", mail);
                                    startActivity(intent);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TrashActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
        recyclerView.setAdapter(adapter);
    }

}
