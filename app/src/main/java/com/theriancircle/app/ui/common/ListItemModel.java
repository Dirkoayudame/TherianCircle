package com.theriancircle.app.ui.common;

public class ListItemModel {
    private final String title;
    private final String subtitle;

    public ListItemModel(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
