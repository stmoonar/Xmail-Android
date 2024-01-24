package com.example.myemail.pojo;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private Double limitSize;
    private Double usedSize;
    private int status;

    public User(Integer id, String username,
                String password, String nickname,
                String avatar, int status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.avatar = avatar;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Double getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(Double limitSize) {
        this.limitSize = limitSize;
    }

    public Double getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(Double usedSize) {
        this.usedSize = usedSize;
    }
}
