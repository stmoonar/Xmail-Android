package com.example.myemail.utils;

public class Function {
    private int iconResId;
    private String title;
    private Class<?> activityClass;

    public Function(int iconResId, String title, Class<?> activityClass) {
        this.iconResId = iconResId;
        this.title = title;
        this.activityClass = activityClass;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }

    public Class<?> getActivityClass() {
        return activityClass;
    }
}