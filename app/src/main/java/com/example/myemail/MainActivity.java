package com.example.myemail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.myemail.utils.Function;
import com.example.myemail.utils.FunctionAdapter;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myemail.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    String domain = getResources().getString(R.string.domain);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);*/

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        boolean isLogged = sp.getBoolean("isLogged", false); // 获取登录标志
        String username = sp.getString("username", ""); // 获取用户名
        if (!isLogged) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        RecyclerView rvFunctions = findViewById(R.id.rv_functions);
        rvFunctions.setLayoutManager(new LinearLayoutManager(this));

        List<Function> functionList = new ArrayList<>();
        functionList.add(new Function(R.drawable.icon, "好友", FriendActivity.class));
        functionList.add(new Function(R.drawable.icon, "收件箱", InboxActivity.class));
        functionList.add(new Function(R.drawable.icon, "回收站", TrashActivity.class));

        if (username.equals("admin" + domain)) {
            functionList.add(new Function(R.drawable.icon, "用户管理", UserManageActivity.class));
        }

        FunctionAdapter adapter = new FunctionAdapter(functionList);
        rvFunctions.setAdapter(adapter);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogged) {
                    Intent intent = new Intent(MainActivity.this, SendMailActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        boolean isLogged = sp.getBoolean("isLogged", false); // 获取登录标志

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (isLogged) {
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if (id == R.id.action_login) {
            if (!isLogged) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(MainActivity.this, "您已登录，请先退出登录", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if (id == R.id.action_settings) {
            if (isLogged) {
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
}