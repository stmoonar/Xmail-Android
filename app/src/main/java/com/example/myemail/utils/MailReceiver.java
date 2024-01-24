package com.example.myemail.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

public class MailReceiver {
    public Message[] receiveMail(Context context) throws Exception {
        String host = "pop.xianyan.software";
        String port = "110";

        //获取本地SharedPreferences存储的用户名和密码
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String username = sp.getString("username", "");
        String password = sp.getString("password", "");

        Properties properties = new Properties();
        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", port);
        properties.put("mail.pop3.starttls.enable", "true");
        Session emailSession = Session.getDefaultInstance(properties);

        Store store = emailSession.getStore("pop3s");
        store.connect(host, username, password);

        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);

        Message[] messages = emailFolder.getMessages();

        emailFolder.close(false);
        store.close();

        return messages;
    }
}