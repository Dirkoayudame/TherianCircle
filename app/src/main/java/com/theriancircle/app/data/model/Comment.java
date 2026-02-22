package com.theriancircle.app.data.model;

public class Comment {
    private final String id;
    private final String postId;
    private final String authorName;
    private final String text;
    private final long timestamp;

    public Comment(String id, String postId, String authorName, String text, long timestamp) {
        this.id = id;
        this.postId = postId;
        this.authorName = authorName;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getPostId() {
        return postId;
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
