package com.example.myemail;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myemail.R;
import com.example.myemail.pojo.Mail;
import com.example.myemail.pojo.Result;
import com.example.myemail.utils.AttachmentAdapter;
import com.example.myemail.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FullEmailActivity extends AppCompatActivity {

    String apiUrl = getResources().getString(R.string.api);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullemail);

        Mail mail = (Mail) getIntent().getSerializableExtra("mail");

        TextView tvSubject = findViewById(R.id.tv_subject_fullemail);
        TextView tvFrom = findViewById(R.id.tv_from_fullemail);
        TextView tvContent = findViewById(R.id.tv_content_fullemail);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_fullemail);

        tvSubject.setText(mail.getSubject());
        tvFrom.setText(mail.getFromEmail());
        //tvContent.loadData(mail.getTextContent(), "text/html", "GB18030");
        Spanned spannedContent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spannedContent = Html.fromHtml(mail.getTextContent(), Html.FROM_HTML_MODE_COMPACT);
        } else {
            spannedContent = Html.fromHtml(mail.getTextContent());
        }
        tvContent.setText(spannedContent);

        String[] attachmentsList = new String[0];
        if (mail.getAttachments() != null) {
            attachmentsList = mail.getAttachments().substring(1, mail.getAttachments().length() - 1).
                    split(", ");
        }
        AttachmentAdapter adapter = new AttachmentAdapter(Arrays.asList(attachmentsList));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button btnDelete = findViewById(R.id.btn_delete_fullmail);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Mail mail = (Mail) getIntent().getSerializableExtra("mail");
                            String url = apiUrl + "/mail/delete?id=" + mail.getId();
                            String response = NetworkUtils.get(url);
                            Result result = new Gson().fromJson(response, Result.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.getCode() == 1) {
                                        Toast.makeText(FullEmailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(FullEmailActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(FullEmailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}