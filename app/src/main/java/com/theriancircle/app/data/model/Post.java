package com.theriancircle.app.data.model;

public class Post {
    private final String id;
    private final User author;
    private final String content;
    private final int likesCount;
    private final boolean likedByMe;
    private final int commentsCount;

    public Post(String id, User author, String content) {
        this(id, author, content, 0, false, 0);
    }

    public Post(
            String id,
            User author,
            String content,
            int likesCount,
            boolean likedByMe,
            int commentsCount
    ) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.likesCount = likesCount;
        this.likedByMe = likedByMe;
        this.commentsCount = commentsCount;
    }

    public String getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public boolean isLikedByMe() {
        return likedByMe;
    }

    public int getCommentsCount() {
        return commentsCount;
    }
}
