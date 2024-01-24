package com.example.myemail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myemail.pojo.Result;
import com.example.myemail.pojo.User;
import com.example.myemail.utils.FriendAdapter;
import com.example.myemail.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserManageActivity extends AppCompatActivity {

    private FriendAdapter adapter1;
    private FriendAdapter adapter2;
    private List<User> users;

    private List<String> users1;
    private List<String> users2;

    String apiUrl = getResources().getString(R.string.api);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermanage);

        users1 = new ArrayList<>();

        RecyclerView recyclerView1 = findViewById(R.id.recyclerView_friend1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        adapter1 = new FriendAdapter(users1);
        recyclerView1.setAdapter(adapter1);

        users2 = new ArrayList<>();

        RecyclerView recyclerView2 = findViewById(R.id.recyclerView_friend2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new FriendAdapter(users2);
        recyclerView2.setAdapter(adapter2);

        new Thread(new Runnable() {
            String url = apiUrl + "/user";
            @Override
            public void run() {
                try {
                    String response = NetworkUtils.get(url);
                    Result result = new Gson().fromJson(response, Result.class);
                    if (result.getCode() == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UserManageActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        //将result中的data转换成List<String>
                        List<User> users = new Gson().fromJson(new Gson().toJson(result.getData()),
                                new TypeToken<List<User>>(){}.getType());
                        for (User user : users) {
                            if (user.getStatus() == 1) {
                                users1.add(user.getUsername());
                            } else {
                                users2.add(user.getUsername());
                            }
                        }
                        Log.d("FriendActivity", "users: " + users);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UserManageActivity.this, "获取用户列表成功", Toast.LENGTH_SHORT).show();
                                adapter1.notifyDataSetChanged();
                                adapter2.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserManageActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        }
                    });
                    throw new RuntimeException(e);
                }
            }
        }).start();

        Button btn_disable = findViewById(R.id.btn_disable_user);
        Button btn_sendmail = findViewById(R.id.btn_sendmail_admin);
        Button btn_enable = findViewById(R.id.btn_enable_user);

        btn_sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到发送邮件界面，把收件人输入框的内容设置为选择的好友，用英文分号隔开
                Intent intent = new Intent(UserManageActivity.this, SendMailActivity.class);
                String receiver = "";
                List<String> selectedFriends = adapter1.getSelectedFriends();
                for (String friend : selectedFriends) {
                    receiver += friend + ";";
                }
                intent.putExtra("receiver", receiver);
                startActivity(intent);
            }
        });

        btn_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selectedFriends = adapter1.getSelectedFriends();
                if (selectedFriends.size() == 0) {
                    Toast.makeText(UserManageActivity.this, "请先选择用户", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = apiUrl + "/user/disable?";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (String friend : selectedFriends) {
                                String response = NetworkUtils.get(url + "&username=" + friend);
                                Result result = new Gson().fromJson(response, Result.class);
                                if (result.getCode() == 0) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(UserManageActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            users1.remove(friend);
                                            adapter1.notifyDataSetChanged();
                                            users2.add(friend);
                                            adapter2.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UserManageActivity.this, "禁用用户成功", Toast.LENGTH_SHORT).show();
                                    //清空选中的好友
                                    adapter1.clearSelectedFriends();
                                }
                            });
                        } catch (IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UserManageActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                                }
                            });
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
        });

        btn_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selectedFriends = adapter2.getSelectedFriends();
                if (selectedFriends.size() == 0) {
                    Toast.makeText(UserManageActivity.this, "请先选择用户", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = apiUrl + "/user/enable?";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (String friend : selectedFriends) {
                                String response = NetworkUtils.get(url + "&username=" + friend);
                                Result result = new Gson().fromJson(response, Result.class);
                                if (result.getCode() == 0) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(UserManageActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            users2.remove(friend);
                                            adapter2.notifyDataSetChanged();
                                            users1.add(friend);
                                            adapter1.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UserManageActivity.this, "解禁用户成功", Toast.LENGTH_SHORT).show();
                                    //清空选中的好友
                                    adapter2.clearSelectedFriends();
                                }
                            });
                        } catch (IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UserManageActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                                }
                            });
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
        });
    }
}
