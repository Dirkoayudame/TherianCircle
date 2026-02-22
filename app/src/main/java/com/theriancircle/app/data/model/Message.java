package com.theriancircle.app.data.model;

public class Message {
    private final String id;
    private final String authorName;
    private final String text;
    private final long timestamp;

    public Message(String id, String authorName, String text, long timestamp) {
        this.id = id;
        this.authorName = authorName;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
