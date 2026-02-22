package com.theriancircle.app.data.source;

import com.theriancircle.app.data.model.CommunityGroup;
import com.theriancircle.app.data.model.Comment;
import com.theriancircle.app.data.model.Event;
import com.theriancircle.app.data.model.Message;
import com.theriancircle.app.data.model.Post;
import com.theriancircle.app.data.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MockRemoteDataSource implements RemoteDataSource {
    private final List<Post> posts = new ArrayList<>();
    private final Map<String, List<Comment>> commentsByPost = new HashMap<>();
    private final List<Message> messages = new ArrayList<>();
    private int pendingAutoReplies;

    public MockRemoteDataSource() {
        seedIfNeeded();
    }

    private void seedIfNeeded() {
        if (!posts.isEmpty() || !messages.isEmpty()) {
            return;
        }
        User nyx = new User("u_1", "Nyx", "Lobo");
        User kairo = new User("u_2", "Kairo", "Zorro");
        posts.add(new Post(
                "p_1",
                nyx,
                "Hoy hice grounding en el bosque y me sentí super conectada.",
                4,
                false,
                1
        ));
        posts.add(new Post("p_2", kairo, "Abrí grupo para therians nocturnos.", 2, false, 0));

        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("c_1", "p_1", "Luna", "Qué bonito, me pasó algo similar.", 1L));
        commentsByPost.put("p_1", comments);

        messages.add(new Message("m_1", "Tu", "Alguien para meetup este finde?", 1L));
        messages.add(new Message("m_2", "Nyx", "Yo me apunto, llevo snacks.", 2L));
    }

    @Override
    public List<Post> fetchFeedPosts() {
        return new ArrayList<>(posts);
    }

    @Override
    public Post createPost(String authorName, String authorSpecies, String content) {
        String postId = "p_" + UUID.randomUUID();
        User author = new User("u_" + UUID.randomUUID(), authorName, authorSpecies);
        Post post = new Post(postId, author, content, 0, false, 0);
        posts.add(0, post);
        return post;
    }

    @Override
    public Post toggleLike(String postId) {
        for (int i = 0; i < posts.size(); i++) {
            Post current = posts.get(i);
            if (current.getId().equals(postId)) {
                boolean nextLiked = !current.isLikedByMe();
                int nextLikes = nextLiked ? current.getLikesCount() + 1 : Math.max(0, current.getLikesCount() - 1);
                Post updated = new Post(
                        current.getId(),
                        current.getAuthor(),
                        current.getContent(),
                        nextLikes,
                        nextLiked,
                        current.getCommentsCount()
                );
                posts.set(i, updated);
                return updated;
            }
        }
        throw new IllegalArgumentException("Post not found: " + postId);
    }

    @Override
    public Comment addComment(String postId, String authorName, String text) {
        String commentId = "c_" + UUID.randomUUID();
        Comment comment = new Comment(commentId, postId, authorName, text, System.currentTimeMillis());
        List<Comment> comments = commentsByPost.containsKey(postId)
                ? commentsByPost.get(postId)
                : new ArrayList<>();
        comments.add(comment);
        commentsByPost.put(postId, comments);

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
                break;
            }
        }
        return comment;
    }

    @Override
    public List<CommunityGroup> fetchGroups() {
        List<CommunityGroup> groups = new ArrayList<>();
        groups.add(new CommunityGroup("g_1", "Manada Lobo", 2300, false));
        groups.add(new CommunityGroup("g_2", "Felinos Unidos", 1500, true));
        return groups;
    }

    @Override
    public List<Event> fetchEvents() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("e_1", "Meetup Therian", "San Diego", "Sabado 28 Febrero, 5:00 PM", 134, false));
        return events;
    }

    @Override
    public List<Message> fetchMessages() {
        if (pendingAutoReplies > 0) {
            Message reply = new Message(
                    "m_" + UUID.randomUUID(),
                    "Nyx",
                    "Te leí, caigo en un rato.",
                    System.currentTimeMillis()
            );
            messages.add(reply);
            pendingAutoReplies--;
        }
        return new ArrayList<>(messages);
    }

    @Override
    public Message sendMessage(String authorName, String text) {
        Message message = new Message(
                "m_" + UUID.randomUUID(),
                authorName,
                text,
                System.currentTimeMillis()
        );
        messages.add(message);
        pendingAutoReplies++;
        return message;
    }
}
