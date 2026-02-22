package com.theriancircle.app.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theriancircle.app.R;
import com.theriancircle.app.auth.SessionPrefs;
import com.theriancircle.app.data.model.Message;
import com.theriancircle.app.data.repository.AppRepository;
import com.theriancircle.app.data.repository.RepositoryProvider;
import com.theriancircle.app.notifications.NotificationHelper;
import com.theriancircle.app.ui.chat.ChatMessageAdapter;

import java.util.List;

public class ChatFragment extends Fragment {
    private static final long POLL_INTERVAL_MS = 3000L;

    private ChatMessageAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar loadingView;
    private TextView emptyView;
    private View errorContainer;
    private EditText chatInput;
    private AppRepository repository;
    private SessionPrefs sessionPrefs;
    private Handler handler;
    private final Runnable pollingTask = new Runnable() {
        @Override
        public void run() {
            if (!isAdded()) {
                return;
            }
            try {
                List<Message> refreshed = repository.refreshMessagesFromRemote();
                notifyIncomingMessages(refreshed);
                loadData(false);
            } catch (Exception ignored) {
            } finally {
                handler.postDelayed(this, POLL_INTERVAL_MS);
            }
        }
    };

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        repository = RepositoryProvider.getRepository(requireContext());
        sessionPrefs = new SessionPrefs(requireContext());
        handler = new Handler(Looper.getMainLooper());

        recyclerView = view.findViewById(R.id.stateRecycler);
        loadingView = view.findViewById(R.id.stateLoading);
        emptyView = view.findViewById(R.id.stateEmpty);
        errorContainer = view.findViewById(R.id.errorContainer);
        chatInput = view.findViewById(R.id.chatInput);
        Button sendButton = view.findViewById(R.id.sendButton);
        Button retryButton = view.findViewById(R.id.retryButton);

        adapter = new ChatMessageAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        sendButton.setOnClickListener(v -> sendMessage());
        retryButton.setOnClickListener(v -> loadData(true));
        loadData(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (handler != null) {
            handler.postDelayed(pollingTask, POLL_INTERVAL_MS);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(pollingTask);
        }
    }

    private void notifyIncomingMessages(List<Message> refreshedMessages) {
        long lastNotified = sessionPrefs.getLastNotifiedMessageTimestamp();
        long newestNotified = lastNotified;
        String myUsername = sessionPrefs.getUsername();

        for (Message message : refreshedMessages) {
            if (message.getTimestamp() <= lastNotified) {
                continue;
            }
            if (message.getAuthorName().equalsIgnoreCase(myUsername)) {
                if (message.getTimestamp() > newestNotified) {
                    newestNotified = message.getTimestamp();
                }
                continue;
            }

            NotificationHelper.showIncomingChatMessage(
                    requireContext(),
                    message.getAuthorName(),
                    message.getText()
            );
            if (message.getTimestamp() > newestNotified) {
                newestNotified = message.getTimestamp();
            }
        }

        if (newestNotified > lastNotified) {
            sessionPrefs.setLastNotifiedMessageTimestamp(newestNotified);
        }
    }

    private void loadData(boolean showLoadingState) {
        if (showLoadingState) {
            showLoading();
        }
        try {
            List<Message> messages = repository.getMessages();

            if (messages.isEmpty()) {
                showEmpty(getString(R.string.state_empty_chat));
            } else {
                adapter.submitItems(messages);
                showContent();
                recyclerView.scrollToPosition(messages.size() - 1);
                if (sessionPrefs.getLastNotifiedMessageTimestamp() == 0L) {
                    sessionPrefs.setLastNotifiedMessageTimestamp(
                            messages.get(messages.size() - 1).getTimestamp()
                    );
                }
            }
        } catch (Exception e) {
            showError();
        }
    }

    private void sendMessage() {
        String text = chatInput.getText().toString().trim();
        if (text.isEmpty()) {
            chatInput.setError(getString(R.string.message_empty_error));
            return;
        }
        repository.sendMessage(sessionPrefs.getUsername(), text);
        chatInput.setText("");
        Toast.makeText(requireContext(), R.string.message_sent, Toast.LENGTH_SHORT).show();
        loadData(false);
    }

    private void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorContainer.setVisibility(View.GONE);
    }

    private void showContent() {
        loadingView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        errorContainer.setVisibility(View.GONE);
    }

    private void showEmpty(String message) {
        loadingView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        emptyView.setText(message);
        emptyView.setVisibility(View.VISIBLE);
        errorContainer.setVisibility(View.GONE);
    }

    private void showError() {
        loadingView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorContainer.setVisibility(View.VISIBLE);
    }
}
