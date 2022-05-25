package com.example.aihealthcare;

import android.graphics.drawable.Drawable;

public class ListViewItem2 {
    private Drawable drawableIcon;
    private String title;
    private String desc;

    public Drawable getDrawableIcon() {
        return drawableIcon;
    }

    public void setDrawableIcon(Drawable drawableIcon) {
        this.drawableIcon = drawableIcon;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDesc() { return desc; }

    public void setDesc(String desc) { this.desc = desc; }
}
