package com.theriancircle.app.data.source;

import com.theriancircle.app.data.model.CommunityGroup;
import com.theriancircle.app.data.model.Event;
import com.theriancircle.app.data.model.Message;
import com.theriancircle.app.data.model.Post;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCache {
    private List<Post> posts = new ArrayList<>();
    private List<CommunityGroup> groups = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    public boolean hasPosts() {
        return !posts.isEmpty();
    }

    public boolean hasGroups() {
        return !groups.isEmpty();
    }

    public boolean hasEvents() {
        return !events.isEmpty();
    }

    public boolean hasMessages() {
        return !messages.isEmpty();
    }

    public List<Post> getPosts() {
        return new ArrayList<>(posts);
    }

    public List<CommunityGroup> getGroups() {
        return new ArrayList<>(groups);
    }

    public List<Event> getEvents() {
        return new ArrayList<>(events);
    }

    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }

    public void savePosts(List<Post> posts) {
        this.posts = new ArrayList<>(posts);
    }

    public void saveGroups(List<CommunityGroup> groups) {
        this.groups = new ArrayList<>(groups);
    }

    public void saveEvents(List<Event> events) {
        this.events = new ArrayList<>(events);
    }

    public void saveMessages(List<Message> messages) {
        this.messages = new ArrayList<>(messages);
    }
}
