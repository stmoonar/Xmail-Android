package com.example.myemail.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class Mail implements Serializable {
    private String id;
    private Integer uid;
    private String from;
    private String fromEmail;
    private String to;
    private String toEmail;
    private String sendTime;
    private String subject;
    private Integer flag;
    private Integer deleted;
    private Integer draft;
    private Integer seen;
    private String attachments;
    private String filename;
    private Integer size;
    private String textContent;
    private String replyTo;
    private String encoding;

    public Mail(String id, Integer uid, String from, String fromEmail, String to, String toEmail, String sendTime, String subject, Integer flag, Integer deleted, Integer draft, Integer seen, String attachments, String filename, Integer size, String textContent, String replyTo) {
        this.id = id;
        this.uid = uid;
        this.from = from;
        this.fromEmail = fromEmail;
        this.to = to;
        this.toEmail = toEmail;
        this.sendTime = sendTime;
        this.subject = subject;
        this.flag = flag;
        this.deleted = deleted;
        this.draft = draft;
        this.seen = seen;
        this.attachments = attachments;
        this.filename = filename;
        this.size = size;
        this.textContent = textContent;
        this.replyTo = replyTo;
    }

    public Mail() {
    }

    @Override
    public String toString() {
        return "Mail{" +
                "id='" + id + '\'' +
                ", uid=" + uid +
                ", from='" + from + '\'' +
                ", fromEmail='" + fromEmail + '\'' +
                ", to='" + to + '\'' +
                ", toEmail='" + toEmail + '\'' +
                ", sendTime=" + sendTime +
                ", subject='" + subject + '\'' +
                ", flag=" + flag +
                ", deleted=" + deleted +
                ", draft=" + draft +
                ", seen=" + seen +
                ", attachments='" + attachments + '\'' +
                ", filename='" + filename + '\'' +
                ", size=" + size +
                ", textContent='" + textContent + '\'' +
                ", replyTo='" + replyTo + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public Object getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getDraft() {
        return draft;
    }

    public void setDraft(Integer draft) {
        this.draft = draft;
    }

    public Integer getSeen() {
        return seen;
    }

    public void setSeen(Integer seen) {
        this.seen = seen;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
