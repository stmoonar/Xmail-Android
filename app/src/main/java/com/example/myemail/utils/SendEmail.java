package com.example.myemail.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.myemail.R;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
    public boolean sendEmail(Context context, List<String> toList,
                             String subject, String content) {

        String host = context.getResources().getString(R.string.smtp_host);

        //获取本地SharedPreferences存储的用户名和密码
        SharedPreferences sp = context.getSharedPreferences("userInfo",
                Context.MODE_PRIVATE);
        String username = sp.getString("username", "");
        String password = sp.getString("password", "");

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.auth", "true");

        // Get the Session object. Pass Authenticator object.
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));

            //设置收件人，多个
            InternetAddress[] toAddress = new InternetAddress[toList.size()];
            for (int i = 0; i < toList.size(); i++) {
                toAddress[i] = new InternetAddress(toList.get(i));
            }
            message.addRecipients(Message.RecipientType.TO, toAddress);

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(content);

            message.setSentDate(new Date());

            // Send message
            Transport.send(message);
            Log.d("SendEmail", "Sent message successfully....");
            return true;
        } catch (MessagingException mex) {
            Log.e("SendEmail", "Error sending email", mex);
            return false;
        }
    }

}