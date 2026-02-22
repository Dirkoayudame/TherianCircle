package com.theriancircle.app.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
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
import com.theriancircle.app.data.model.Post;
import com.theriancircle.app.data.repository.AppRepository;
import com.theriancircle.app.data.repository.RepositoryProvider;
import com.theriancircle.app.ui.feed.FeedPostAdapter;
import com.theriancircle.app.auth.SessionPrefs;

import java.util.List;

public class FeedFragment extends Fragment {
    private FeedPostAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar loadingView;
    private TextView emptyView;
    private View errorContainer;
    private EditText createPostInput;
    private AppRepository repository;
    private SessionPrefs sessionPrefs;

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        repository = RepositoryProvider.getRepository(requireContext());
        sessionPrefs = new SessionPrefs(requireContext());

        createPostInput = view.findViewById(R.id.createPostInput);
        Button publishButton = view.findViewById(R.id.publishPostButton);
        recyclerView = view.findViewById(R.id.stateRecycler);
        loadingView = view.findViewById(R.id.stateLoading);
        emptyView = view.findViewById(R.id.stateEmpty);
        errorContainer = view.findViewById(R.id.errorContainer);
        Button retryButton = view.findViewById(R.id.retryButton);

        adapter = new FeedPostAdapter(new FeedPostAdapter.FeedPostActions() {
            @Override
            public void onLikeClicked(Post post) {
                repository.toggleLike(post.getId());
                loadData();
            }

            @Override
            public void onCommentClicked(Post post) {
                openCommentDialog(post);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        publishButton.setOnClickListener(v -> publishPost());
        retryButton.setOnClickListener(v -> loadData());
        loadData();
        return view;
    }

    private void loadData() {
        showLoading();
        try {
            List<Post> posts = repository.getFeedPosts();

            if (posts.isEmpty()) {
                showEmpty(getString(R.string.state_empty_feed));
            } else {
                adapter.submitPosts(posts);
                showContent();
            }
        } catch (Exception e) {
            showError();
        }
    }

    private void publishPost() {
        String content = createPostInput.getText().toString().trim();
        if (content.isEmpty()) {
            createPostInput.setError(getString(R.string.post_empty_error));
            return;
        }
        String username = sessionPrefs.getUsername();
        String species = sessionPrefs.getSpecies();
        repository.createPost(username, species, content);
        createPostInput.setText("");
        Toast.makeText(requireContext(), R.string.post_published, Toast.LENGTH_SHORT).show();
        loadData();
    }

    private void openCommentDialog(Post post) {
        final EditText input = new EditText(requireContext());
        input.setHint(R.string.comment_hint);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.comment_prompt_title)
                .setView(input)
                .setNegativeButton(R.string.action_cancel, null)
                .setPositiveButton(R.string.btn_comment, (dialog, which) -> {
                    String commentText = input.getText().toString().trim();
                    if (!commentText.isEmpty()) {
                        repository.addComment(post.getId(), sessionPrefs.getUsername(), commentText);
                        Toast.makeText(requireContext(), R.string.comment_added, Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                })
                .show();
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
