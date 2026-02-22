package com.theriancircle.app.data.repository;

import com.theriancircle.app.data.model.CommunityGroup;
import com.theriancircle.app.data.model.Comment;
import com.theriancircle.app.data.model.Event;
import com.theriancircle.app.data.model.Message;
import com.theriancircle.app.data.model.Post;
import com.theriancircle.app.data.source.InMemoryCache;
import com.theriancircle.app.data.source.LocalDataSource;
import com.theriancircle.app.data.source.RemoteDataSource;

import java.util.List;

public class AppRepository implements FeedRepository, GroupRepository, EventRepository, ChatRepository {
    private final InMemoryCache cache;
    private final LocalDataSource localDataSource;
    private final RemoteDataSource remoteDataSource;

    public AppRepository(
            InMemoryCache cache,
            LocalDataSource localDataSource,
            RemoteDataSource remoteDataSource
    ) {
        this.cache = cache;
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public List<Post> getFeedPosts() {
        if (cache.hasPosts()) {
            return cache.getPosts();
        }
        List<Post> localPosts = localDataSource.getFeedPosts();
        if (!localPosts.isEmpty()) {
            cache.savePosts(localPosts);
            return localPosts;
        }
        List<Post> posts = remoteDataSource.fetchFeedPosts();
        localDataSource.saveFeedPosts(posts);
        cache.savePosts(posts);
        return posts;
    }

    @Override
    public Post createPost(String authorName, String authorSpecies, String content) {
        Post post = remoteDataSource.createPost(authorName, authorSpecies, content);

        List<Post> currentPosts = cache.hasPosts() ? cache.getPosts() : localDataSource.getFeedPosts();
        currentPosts.add(0, post);
        localDataSource.saveFeedPosts(currentPosts);
        cache.savePosts(currentPosts);
        return post;
    }

    @Override
    public Post toggleLike(String postId) {
        Post updated = localDataSource.toggleLike(postId);
        updatePostInCache(updated);
        return updated;
    }

    @Override
    public Comment addComment(String postId, String authorName, String text) {
        Comment comment = localDataSource.addComment(postId, authorName, text);
        Post post = findPostInCache(postId);
        if (post != null) {
            Post updated = new Post(
                    post.getId(),
                    post.getAuthor(),
                    post.getContent(),
                    post.getLikesCount(),
                    post.isLikedByMe(),
                    post.getCommentsCount() + 1
            );
            updatePostInCache(updated);
        }
        return comment;
    }

    @Override
    public List<CommunityGroup> getGroups() {
        if (cache.hasGroups()) {
            return cache.getGroups();
        }
        List<CommunityGroup> localGroups = localDataSource.getGroups();
        if (!localGroups.isEmpty()) {
            cache.saveGroups(localGroups);
            return localGroups;
        }
        List<CommunityGroup> groups = remoteDataSource.fetchGroups();
        localDataSource.saveGroups(groups);
        cache.saveGroups(groups);
        return groups;
    }

    @Override
    public CommunityGroup toggleGroupMembership(String groupId) {
        CommunityGroup updated = localDataSource.toggleGroupMembership(groupId);
        updateGroupInCache(updated);
        return updated;
    }

    @Override
    public List<Event> getEvents() {
        if (cache.hasEvents()) {
            return cache.getEvents();
        }
        List<Event> localEvents = localDataSource.getEvents();
        if (!localEvents.isEmpty()) {
            cache.saveEvents(localEvents);
            return localEvents;
        }
        List<Event> events = remoteDataSource.fetchEvents();
        localDataSource.saveEvents(events);
        cache.saveEvents(events);
        return events;
    }

    @Override
    public Event toggleEventAttendance(String eventId) {
        Event updated = localDataSource.toggleEventAttendance(eventId);
        updateEventInCache(updated);
        return updated;
    }

    @Override
    public List<Message> getMessages() {
        if (cache.hasMessages()) {
            return cache.getMessages();
        }
        List<Message> localMessages = localDataSource.getMessages();
        if (!localMessages.isEmpty()) {
            cache.saveMessages(localMessages);
            return localMessages;
        }
        List<Message> messages = remoteDataSource.fetchMessages();
        localDataSource.saveMessages(messages);
        cache.saveMessages(messages);
        return messages;
    }

    @Override
    public List<Message> refreshMessagesFromRemote() {
        List<Message> messages = remoteDataSource.fetchMessages();
        localDataSource.saveMessages(messages);
        cache.saveMessages(messages);
        return messages;
    }

    @Override
    public Message sendMessage(String authorName, String text) {
        Message remote = remoteDataSource.sendMessage(authorName, text);
        localDataSource.addMessage(remote.getAuthorName(), remote.getText());

        List<Message> messages = cache.hasMessages() ? cache.getMessages() : localDataSource.getMessages();
        messages.add(remote);
        cache.saveMessages(messages);
        return remote;
    }

    private void updatePostInCache(Post updatedPost) {
        List<Post> currentPosts = cache.hasPosts() ? cache.getPosts() : localDataSource.getFeedPosts();
        boolean replaced = false;
        for (int i = 0; i < currentPosts.size(); i++) {
            if (currentPosts.get(i).getId().equals(updatedPost.getId())) {
                currentPosts.set(i, updatedPost);
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            currentPosts.add(0, updatedPost);
        }
        cache.savePosts(currentPosts);
    }

    private Post findPostInCache(String postId) {
        for (Post post : cache.getPosts()) {
            if (post.getId().equals(postId)) {
                return post;
            }
        }
        return null;
    }

    private void updateGroupInCache(CommunityGroup updatedGroup) {
        List<CommunityGroup> groups = cache.hasGroups() ? cache.getGroups() : localDataSource.getGroups();
        boolean replaced = false;
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).getId().equals(updatedGroup.getId())) {
                groups.set(i, updatedGroup);
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            groups.add(updatedGroup);
        }
        cache.saveGroups(groups);
    }

    private void updateEventInCache(Event updatedEvent) {
        List<Event> events = cache.hasEvents() ? cache.getEvents() : localDataSource.getEvents();
        boolean replaced = false;
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getId().equals(updatedEvent.getId())) {
                events.set(i, updatedEvent);
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            events.add(updatedEvent);
        }
        cache.saveEvents(events);
    }
}
