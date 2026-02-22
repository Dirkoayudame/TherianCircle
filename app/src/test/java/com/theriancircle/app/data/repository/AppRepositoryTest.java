package com.theriancircle.app.data.repository;

import com.theriancircle.app.data.model.CommunityGroup;
import com.theriancircle.app.data.model.Comment;
import com.theriancircle.app.data.model.Event;
import com.theriancircle.app.data.model.Message;
import com.theriancircle.app.data.model.Post;
import com.theriancircle.app.data.model.User;
import com.theriancircle.app.data.source.InMemoryCache;
import com.theriancircle.app.data.source.LocalDataSource;
import com.theriancircle.app.data.source.RemoteDataSource;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AppRepositoryTest {

    @Test
    public void getFeedPosts_usesCacheAfterFirstCall() {
        CountingRemoteDataSource remote = new CountingRemoteDataSource();
        FakeLocalDataSource local = new FakeLocalDataSource();
        AppRepository repository = new AppRepository(new InMemoryCache(), local, remote);

        repository.getFeedPosts();
        repository.getFeedPosts();

        assertEquals(1, remote.feedCalls);
    }

    @Test
    public void getGroups_returnsRemoteData() {
        CountingRemoteDataSource remote = new CountingRemoteDataSource();
        FakeLocalDataSource local = new FakeLocalDataSource();
        AppRepository repository = new AppRepository(new InMemoryCache(), local, remote);

        List<CommunityGroup> groups = repository.getGroups();

        assertEquals(2, groups.size());
        assertEquals("Manada Lobo", groups.get(0).getName());
    }

    @Test
    public void getEvents_returnsRemoteData() {
        CountingRemoteDataSource remote = new CountingRemoteDataSource();
        FakeLocalDataSource local = new FakeLocalDataSource();
        AppRepository repository = new AppRepository(new InMemoryCache(), local, remote);

        List<Event> events = repository.getEvents();

        assertEquals(1, events.size());
        assertEquals("Meetup Therian", events.get(0).getTitle());
    }

    @Test
    public void getMessages_returnsRemoteData() {
        CountingRemoteDataSource remote = new CountingRemoteDataSource();
        FakeLocalDataSource local = new FakeLocalDataSource();
        AppRepository repository = new AppRepository(new InMemoryCache(), local, remote);

        List<Message> messages = repository.getMessages();

        assertEquals(2, messages.size());
        assertEquals("Nyx", messages.get(1).getAuthorName());
    }

    @Test
    public void getFeedPosts_readsFromLocalBeforeRemote() {
        CountingRemoteDataSource remote = new CountingRemoteDataSource();
        FakeLocalDataSource local = new FakeLocalDataSource();
        User localUser = new User("local-1", "LocalUser", "Lince");
        local.posts.add(new Post("p_local", localUser, "Desde Room"));
        AppRepository repository = new AppRepository(new InMemoryCache(), local, remote);

        List<Post> posts = repository.getFeedPosts();

        assertEquals(1, posts.size());
        assertEquals("LocalUser", posts.get(0).getAuthor().getUsername());
        assertEquals(0, remote.feedCalls);
    }

    @Test
    public void getFeedPosts_savesRemoteResultInLocalWhenLocalEmpty() {
        CountingRemoteDataSource remote = new CountingRemoteDataSource();
        FakeLocalDataSource local = new FakeLocalDataSource();
        AppRepository repository = new AppRepository(new InMemoryCache(), local, remote);

        repository.getFeedPosts();

        assertEquals(1, local.posts.size());
        assertEquals("Nyx", local.posts.get(0).getAuthor().getUsername());
    }

    @Test
    public void createPost_addsPostAtTop() {
        CountingRemoteDataSource remote = new CountingRemoteDataSource();
        FakeLocalDataSource local = new FakeLocalDataSource();
        AppRepository repository = new AppRepository(new InMemoryCache(), local, remote);

        repository.getFeedPosts();
        Post created = repository.createPost("Neo", "Lobo", "Nuevo post");
        List<Post> posts = repository.getFeedPosts();

        assertEquals(created.getId(), posts.get(0).getId());
        assertEquals("Neo", posts.get(0).getAuthor().getUsername());
    }

    @Test
    public void toggleLike_updatesLikeState() {
        CountingRemoteDataSource remote = new CountingRemoteDataSource();
        FakeLocalDataSource local = new FakeLocalDataSource();
        AppRepository repository = new AppRepository(new InMemoryCache(), local, remote);
        Post post = repository.getFeedPosts().get(0);

        Post updated = repository.toggleLike(post.getId());

        assertEquals(true, updated.isLikedByMe());
        assertEquals(1, updated.getLikesCount());
    }

    @Test
    public void addComment_incrementsCommentCount() {
        CountingRemoteDataSource remote = new CountingRemoteDataSource();
        FakeLocalDataSource local = new FakeLocalDataSource();
        AppRepository repository = new AppRepository(new InMemoryCache(), local, remote);
        Post post = repository.getFeedPosts().get(0);

        repository.addComment(post.getId(), "Neo", "Buen post");
        Post updated = repository.getFeedPosts().get(0);

        assertEquals(1, updated.getCommentsCount());
    }

    @Test
    public void toggleGroupMembership_updatesGroupState() {
        CountingRemoteDataSource remote = new CountingRemoteDataSource();
        FakeLocalDataSource local = new FakeLocalDataSource();
        AppRepository repository = new AppRepository(new InMemoryCache(), local, remote);
        CommunityGroup group = repository.getGroups().get(0);

        CommunityGroup updated = repository.toggleGroupMembership(group.getId());

        assertEquals(true, updated.isJoinedByMe());
    }

    @Test
    public void toggleEventAttendance_updatesEventState() {
        CountingRemoteDataSource remote = new CountingRemoteDataSource();
        FakeLocalDataSource local = new FakeLocalDataSource();
        AppRepository repository = new AppRepository(new InMemoryCache(), local, remote);
        Event event = repository.getEvents().get(0);

        Event updated = repository.toggleEventAttendance(event.getId());

        assertEquals(true, updated.isAttendingByMe());
    }

    @Test
    public void sendMessage_addsMessageToCache() {
        CountingRemoteDataSource remote = new CountingRemoteDataSource();
        FakeLocalDataSource local = new FakeLocalDataSource();
        AppRepository repository = new AppRepository(new InMemoryCache(), local, remote);

        repository.sendMessage("Neo", "Hola chat");
        List<Message> messages = repository.getMessages();

        assertEquals("Neo", messages.get(messages.size() - 1).getAuthorName());
    }

    private static class CountingRemoteDataSource implements RemoteDataSource {
        int feedCalls;

        @Override
        public List<Post> fetchFeedPosts() {
            feedCalls++;
            User nyx = new User("1", "Nyx", "Lobo");
            List<Post> posts = new ArrayList<>();
            posts.add(new Post("p1", nyx, "Hola manada"));
            return posts;
        }

        @Override
        public List<CommunityGroup> fetchGroups() {
            List<CommunityGroup> groups = new ArrayList<>();
            groups.add(new CommunityGroup("g1", "Manada Lobo", 2300, false));
            groups.add(new CommunityGroup("g2", "Felinos Unidos", 1500));
            return groups;
        }

        @Override
        public Post createPost(String authorName, String authorSpecies, String content) {
            User author = new User("new", authorName, authorSpecies);
            return new Post("p_new", author, content);
        }

        @Override
        public Post toggleLike(String postId) {
            return null;
        }

        @Override
        public Comment addComment(String postId, String authorName, String text) {
            return null;
        }

        @Override
        public List<Event> fetchEvents() {
            List<Event> events = new ArrayList<>();
            events.add(new Event("e1", "Meetup Therian", "San Diego", "Sabado", 10, false));
            return events;
        }

        @Override
        public List<Message> fetchMessages() {
            List<Message> messages = new ArrayList<>();
            messages.add(new Message("m1", "Tu", "Hola", 1L));
            messages.add(new Message("m2", "Nyx", "Hey", 2L));
            return messages;
        }

        @Override
        public Message sendMessage(String authorName, String text) {
            return new Message("m_new", authorName, text, 3L);
        }
    }

    private static class FakeLocalDataSource implements LocalDataSource {
        final List<Post> posts = new ArrayList<>();
        final List<CommunityGroup> groups = new ArrayList<>();
        final List<Event> events = new ArrayList<>();
        final List<Message> messages = new ArrayList<>();

        @Override
        public List<Post> getFeedPosts() {
            return new ArrayList<>(posts);
        }

        @Override
        public void saveFeedPosts(List<Post> posts) {
            this.posts.clear();
            this.posts.addAll(posts);
        }

        @Override
        public Post createPost(String authorName, String authorSpecies, String content) {
            User author = new User("local", authorName, authorSpecies);
            Post post = new Post("p_local_new", author, content);
            posts.add(0, post);
            return post;
        }

        @Override
        public Post toggleLike(String postId) {
            for (int i = 0; i < posts.size(); i++) {
                Post current = posts.get(i);
                if (current.getId().equals(postId)) {
                    boolean liked = !current.isLikedByMe();
                    int likes = liked ? current.getLikesCount() + 1 : Math.max(0, current.getLikesCount() - 1);
                    Post updated = new Post(
                            current.getId(),
                            current.getAuthor(),
                            current.getContent(),
                            likes,
                            liked,
                            current.getCommentsCount()
                    );
                    posts.set(i, updated);
                    return updated;
                }
            }
            return null;
        }

        @Override
        public Comment addComment(String postId, String authorName, String text) {
            for (int i = 0; i < posts.size(); i++) {
                Post current = posts.get(i);
                if (current.getId().equals(postId)) {
                    Post updated = new Post(
                            current.getId(),
                            current.getAuthor(),
                            current.getContent(),
                            current.getLikesCount(),
                            current.isLikedByMe(),
                            current.getCommentsCount() + 1
                    );
                    posts.set(i, updated);
                    return new Comment("c_local", postId, authorName, text, 1L);
                }
            }
            return null;
        }

        @Override
        public List<CommunityGroup> getGroups() {
            return new ArrayList<>(groups);
        }

        @Override
        public void saveGroups(List<CommunityGroup> groups) {
            this.groups.clear();
            this.groups.addAll(groups);
        }

        @Override
        public CommunityGroup toggleGroupMembership(String groupId) {
            for (int i = 0; i < groups.size(); i++) {
                CommunityGroup current = groups.get(i);
                if (current.getId().equals(groupId)) {
                    boolean joined = !current.isJoinedByMe();
                    int members = joined ? current.getMembersCount() + 1 : Math.max(0, current.getMembersCount() - 1);
                    CommunityGroup updated = new CommunityGroup(
                            current.getId(),
                            current.getName(),
                            members,
                            joined
                    );
                    groups.set(i, updated);
                    return updated;
                }
            }
            return null;
        }

        @Override
        public List<Event> getEvents() {
            return new ArrayList<>(events);
        }

        @Override
        public void saveEvents(List<Event> events) {
            this.events.clear();
            this.events.addAll(events);
        }

        @Override
        public Event toggleEventAttendance(String eventId) {
            for (int i = 0; i < events.size(); i++) {
                Event current = events.get(i);
                if (current.getId().equals(eventId)) {
                    boolean attending = !current.isAttendingByMe();
                    int attendees = attending ? current.getAttendeesCount() + 1 : Math.max(0, current.getAttendeesCount() - 1);
                    Event updated = new Event(
                            current.getId(),
                            current.getTitle(),
                            current.getLocation(),
                            current.getDateLabel(),
                            attendees,
                            attending
                    );
                    events.set(i, updated);
                    return updated;
                }
            }
            return null;
        }

        @Override
        public List<Message> getMessages() {
            return new ArrayList<>(messages);
        }

        @Override
        public void saveMessages(List<Message> messages) {
            this.messages.clear();
            this.messages.addAll(messages);
        }

        @Override
        public Message addMessage(String authorName, String text) {
            Message message = new Message("m_local", authorName, text, 10L);
            messages.add(message);
            return message;
        }
    }
}
