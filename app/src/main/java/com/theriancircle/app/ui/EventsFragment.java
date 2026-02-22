package com.theriancircle.app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theriancircle.app.R;
import com.theriancircle.app.data.model.Event;
import com.theriancircle.app.data.repository.AppRepository;
import com.theriancircle.app.data.repository.RepositoryProvider;
import com.theriancircle.app.ui.events.EventAdapter;
import java.util.List;

public class EventsFragment extends Fragment {
    private EventAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar loadingView;
    private TextView emptyView;
    private View errorContainer;
    private AppRepository repository;

    public static EventsFragment newInstance() {
        return new EventsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        repository = RepositoryProvider.getRepository(requireContext());
        recyclerView = view.findViewById(R.id.stateRecycler);
        loadingView = view.findViewById(R.id.stateLoading);
        emptyView = view.findViewById(R.id.stateEmpty);
        errorContainer = view.findViewById(R.id.errorContainer);
        Button retryButton = view.findViewById(R.id.retryButton);

        adapter = new EventAdapter(event -> {
            Event updated = repository.toggleEventAttendance(event.getId());
            int message = updated.isAttendingByMe() ? R.string.event_rsvp_on : R.string.event_rsvp_off;
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            loadData();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        retryButton.setOnClickListener(v -> loadData());
        loadData();
        return view;
    }

    private void loadData() {
        showLoading();
        try {
            List<Event> events = repository.getEvents();

            if (events.isEmpty()) {
                showEmpty(getString(R.string.state_empty_events));
            } else {
                adapter.submitItems(events);
                showContent();
            }
        } catch (Exception e) {
            showError();
        }
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
