package com.example.myemail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myemail.pojo.Result;
import com.example.myemail.utils.EmailValidator;
import com.example.myemail.utils.FriendAdapter;
import com.example.myemail.utils.NetworkUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends AppCompatActivity {

    private FriendAdapter adapter;
    private List<String> friends;
    private SharedPreferences sp;
    private String username;

    String apiUrl = getResources().getString(R.string.api);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        username = sp.getString("username", "");

        friends = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerView_friend);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendAdapter(friends);
        recyclerView.setAdapter(adapter);

        new Thread(new Runnable() {
            String url = apiUrl + "/user/listfriend?username=" + username;
            @Override
            public void run() {
                try {
                    String response = NetworkUtils.get(url);
                    Result result = new Gson().fromJson(response, Result.class);
                    if (result.getCode() == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FriendActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        //将result中的data转换成List<String>
                        friends.clear();
                        friends.addAll((List<String>) result.getData());
                        Log.d("FriendActivity", "friends: " + friends);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FriendActivity.this, "获取好友列表成功", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FriendActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        }
                    });
                    throw new RuntimeException(e);
                }
            }
        }).start();

        EditText etFriendname = findViewById(R.id.et_friendname);
        Button btnAddFriend = findViewById(R.id.btn_addfriend);

        //添加好友
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friendname = etFriendname.getText().toString();
                if (friendname.equals("") || friendname == null) {
                    Toast.makeText(FriendActivity.this, "请输入好友名", Toast.LENGTH_SHORT).show();
                } else if (!EmailValidator.isValid(friendname)) {
                    Toast.makeText(FriendActivity.this, "邮件格式不正确", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        String url = apiUrl + "/user/addfriend?username=" + username
                                + "&friendname=" + friendname;
                        @Override
                        public void run() {
                            try {
                                String response = NetworkUtils.get(url);
                                Result result = new Gson().fromJson(response, Result.class);
                                if (result.getCode() == 0) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(FriendActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(FriendActivity.this, "添加好友成功", Toast.LENGTH_SHORT).show();
                                            etFriendname.setText("");
                                            friends.add(friendname);
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            } catch (IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(FriendActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                throw new RuntimeException(e);
                            }
                        }
                    }).start();
                }
            }
        });

        //批量删除好友
        Button btnDeleteFriend = findViewById(R.id.btn_delete_friend);
        btnDeleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(FriendActivity.this, "暂不支持删除", Toast.LENGTH_SHORT).show();
                List<String> selectedFriends = adapter.getSelectedFriends();
                new Thread(new Runnable() {
                    String url = apiUrl + "/user/deletefriend?username=" + username;
                    @Override
                    public void run() {
                        try {
                            for (String friend : selectedFriends) {
                                String response = NetworkUtils.get(url + "&friendname=" + friend);
                                Result result = new Gson().fromJson(response, Result.class);
                                if (result.getCode() == 0) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(FriendActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            friends.remove(friend);
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(FriendActivity.this, "删除好友成功", Toast.LENGTH_SHORT).show();
                                    //清空选中的好友
                                    adapter.clearSelectedFriends();
                                }
                            });
                        } catch (IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(FriendActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                                }
                            });
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
        });

        //批量发送邮件
        Button btnSendMail = findViewById(R.id.btn_sendmail_friend);
        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到发送邮件界面，把收件人输入框的内容设置为选择的好友，用英文分号隔开
                Intent intent = new Intent(FriendActivity.this, SendMailActivity.class);
                String receiver = "";
                List<String> selectedFriends = adapter.getSelectedFriends();
                for (String friend : selectedFriends) {
                    receiver += friend + ";";
                }
                intent.putExtra("receiver", receiver);
                startActivity(intent);
            }
        });
    }
}