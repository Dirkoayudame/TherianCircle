package com.theriancircle.app.data.repository;

import com.theriancircle.app.data.model.Message;

import java.util.List;

public interface ChatRepository {
    List<Message> getMessages();
    List<Message> refreshMessagesFromRemote();
    Message sendMessage(String authorName, String text);
}
