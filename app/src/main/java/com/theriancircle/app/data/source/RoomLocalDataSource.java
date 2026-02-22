package com.theriancircle.app.data.source;

import android.content.Context;

import com.theriancircle.app.data.local.AppDatabase;
import com.theriancircle.app.data.local.entity.CommentEntity;
import com.theriancircle.app.data.local.entity.EventEntity;
import com.theriancircle.app.data.local.entity.GroupEntity;
import com.theriancircle.app.data.local.entity.MessageEntity;
import com.theriancircle.app.data.local.entity.PostEntity;
import com.theriancircle.app.data.model.CommunityGroup;
import com.theriancircle.app.data.model.Comment;
import com.theriancircle.app.data.model.Event;
import com.theriancircle.app.data.model.Message;
import com.theriancircle.app.data.model.Post;
import com.theriancircle.app.data.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoomLocalDataSource implements LocalDataSource {
    private final AppDatabase database;

    public RoomLocalDataSource(Context context) {
        this.database = AppDatabase.getInstance(context);
    }

    @Override
    public List<Post> getFeedPosts() {
        List<PostEntity> entities = database.postDao().getAll();
        List<Post> posts = new ArrayList<>();
        for (PostEntity entity : entities) {
            User author = new User(entity.id + "_author", entity.authorName, entity.authorSpecies);
            posts.add(new Post(
                    entity.id,
                    author,
                    entity.content,
                    entity.likesCount,
                    entity.likedByMe == 1,
                    entity.commentsCount
            ));
        }
        return posts;
    }

    @Override
    public void saveFeedPosts(List<Post> posts) {
        List<PostEntity> entities = new ArrayList<>();
        for (Post post : posts) {
            entities.add(new PostEntity(
                    post.getId(),
                    post.getAuthor().getUsername(),
                    post.getAuthor().getSpecies(),
                    post.getContent(),
                    post.getLikesCount(),
                    post.isLikedByMe() ? 1 : 0,
                    post.getCommentsCount()
            ));
        }
        database.postDao().clearAll();
        database.postDao().insertAll(entities);
    }

    @Override
    public Post createPost(String authorName, String authorSpecies, String content) {
        String id = "p_local_" + UUID.randomUUID();
        User author = new User("u_local_" + UUID.randomUUID(), authorName, authorSpecies);
        Post post = new Post(id, author, content, 0, false, 0);
        PostEntity entity = new PostEntity(
                id,
                authorName,
                authorSpecies,
                content,
                0,
                0,
                0
        );
        database.postDao().insert(entity);
        return post;
    }

    @Override
    public Post toggleLike(String postId) {
        PostEntity entity = database.postDao().findById(postId);
        if (entity == null) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }
        int nextLiked = entity.likedByMe == 1 ? 0 : 1;
        int nextLikes = nextLiked == 1 ? entity.likesCount + 1 : Math.max(0, entity.likesCount - 1);
        entity.likedByMe = nextLiked;
        entity.likesCount = nextLikes;
        database.postDao().insert(entity);
        User author = new User(entity.id + "_author", entity.authorName, entity.authorSpecies);
        return new Post(
                entity.id,
                author,
                entity.content,
                entity.likesCount,
                entity.likedByMe == 1,
                entity.commentsCount
        );
    }

    @Override
    public Comment addComment(String postId, String authorName, String text) {
        PostEntity postEntity = database.postDao().findById(postId);
        if (postEntity == null) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }

        CommentEntity commentEntity = new CommentEntity(
                "c_local_" + UUID.randomUUID(),
                postId,
                authorName,
                text,
                System.currentTimeMillis()
        );
        database.commentDao().insert(commentEntity);

        postEntity.commentsCount = postEntity.commentsCount + 1;
        database.postDao().insert(postEntity);

        return new Comment(
                commentEntity.id,
                commentEntity.postId,
                commentEntity.authorName,
                commentEntity.text,
                commentEntity.timestamp
        );
    }

    @Override
    public List<CommunityGroup> getGroups() {
        List<GroupEntity> entities = database.groupDao().getAll();
        List<CommunityGroup> groups = new ArrayList<>();
        for (GroupEntity entity : entities) {
            groups.add(new CommunityGroup(entity.id, entity.name, entity.membersCount, entity.joinedByMe == 1));
        }
        return groups;
    }

    @Override
    public void saveGroups(List<CommunityGroup> groups) {
        List<GroupEntity> entities = new ArrayList<>();
        for (CommunityGroup group : groups) {
            entities.add(new GroupEntity(
                    group.getId(),
                    group.getName(),
                    group.getMembersCount(),
                    group.isJoinedByMe() ? 1 : 0
            ));
        }
        database.groupDao().clearAll();
        database.groupDao().insertAll(entities);
    }

    @Override
    public CommunityGroup toggleGroupMembership(String groupId) {
        GroupEntity entity = database.groupDao().findById(groupId);
        if (entity == null) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }
        int joined = entity.joinedByMe == 1 ? 0 : 1;
        int members = joined == 1 ? entity.membersCount + 1 : Math.max(0, entity.membersCount - 1);
        entity.joinedByMe = joined;
        entity.membersCount = members;
        database.groupDao().insert(entity);
        return new CommunityGroup(entity.id, entity.name, entity.membersCount, entity.joinedByMe == 1);
    }

    @Override
    public List<Event> getEvents() {
        List<EventEntity> entities = database.eventDao().getAll();
        List<Event> events = new ArrayList<>();
        for (EventEntity entity : entities) {
            events.add(new Event(
                    entity.id,
                    entity.title,
                    entity.location,
                    entity.dateLabel,
                    entity.attendeesCount,
                    entity.attendingByMe == 1
            ));
        }
        return events;
    }

    @Override
    public void saveEvents(List<Event> events) {
        List<EventEntity> entities = new ArrayList<>();
        for (Event event : events) {
            entities.add(new EventEntity(
                    event.getId(),
                    event.getTitle(),
                    event.getLocation(),
                    event.getDateLabel(),
                    event.getAttendeesCount(),
                    event.isAttendingByMe() ? 1 : 0
            ));
        }
        database.eventDao().clearAll();
        database.eventDao().insertAll(entities);
    }

    @Override
    public Event toggleEventAttendance(String eventId) {
        EventEntity entity = database.eventDao().findById(eventId);
        if (entity == null) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }
        int attending = entity.attendingByMe == 1 ? 0 : 1;
        int attendees = attending == 1 ? entity.attendeesCount + 1 : Math.max(0, entity.attendeesCount - 1);
        entity.attendingByMe = attending;
        entity.attendeesCount = attendees;
        database.eventDao().insert(entity);
        return new Event(
                entity.id,
                entity.title,
                entity.location,
                entity.dateLabel,
                entity.attendeesCount,
                entity.attendingByMe == 1
        );
    }

    @Override
    public List<Message> getMessages() {
        List<MessageEntity> entities = database.messageDao().getAll();
        List<Message> messages = new ArrayList<>();
        for (MessageEntity entity : entities) {
            messages.add(new Message(entity.id, entity.authorName, entity.text, entity.timestamp));
        }
        return messages;
    }

    @Override
    public void saveMessages(List<Message> messages) {
        List<MessageEntity> entities = new ArrayList<>();
        for (Message message : messages) {
            entities.add(new MessageEntity(
                    message.getId(),
                    message.getAuthorName(),
                    message.getText(),
                    message.getTimestamp()
            ));
        }
        database.messageDao().clearAll();
        database.messageDao().insertAll(entities);
    }

    @Override
    public Message addMessage(String authorName, String text) {
        MessageEntity entity = new MessageEntity(
                "m_local_" + UUID.randomUUID(),
                authorName,
                text,
                System.currentTimeMillis()
        );
        database.messageDao().insert(entity);
        return new Message(entity.id, entity.authorName, entity.text, entity.timestamp);
    }
}
