package com.theriancircle.app.ui;

import android.Manifest;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.theriancircle.app.R;
import com.theriancircle.app.auth.SessionPrefs;
import com.theriancircle.app.notifications.NotificationHelper;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_OPEN_TAB = "extra_open_tab";
    public static final String TAB_CHAT = "chat";
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        NotificationHelper.createChannels(this);
        requestNotificationPermissionIfNeeded();

        SessionPrefs sessionPrefs = new SessionPrefs(this);
        String username = getIntent().getStringExtra("username");
        String species = getIntent().getStringExtra("species");
        if (username == null || username.trim().isEmpty()) {
            username = sessionPrefs.getUsername();
        }
        if (species == null || species.trim().isEmpty()) {
            species = sessionPrefs.getSpecies();
        }
        sessionPrefs.saveProfile(username, species);
        final String profileUsername = username;
        final String profileSpecies = species;

        bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected;

            int itemId = item.getItemId();
            if (itemId == R.id.menu_feed) {
                selected = FeedFragment.newInstance();
            } else if (itemId == R.id.menu_groups) {
                selected = GroupsFragment.newInstance();
            } else if (itemId == R.id.menu_events) {
                selected = EventsFragment.newInstance();
            } else if (itemId == R.id.menu_chat) {
                selected = ChatFragment.newInstance();
            } else {
                selected = ProfileFragment.newInstance(profileUsername, profileSpecies);
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, selected)
                    .commit();
            return true;
        });

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(getInitialItemFromIntent(getIntent()));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(getInitialItemFromIntent(intent));
        }
    }

    private int getInitialItemFromIntent(Intent intent) {
        String openTab = intent.getStringExtra(EXTRA_OPEN_TAB);
        return TAB_CHAT.equals(openTab) ? R.id.menu_chat : R.id.menu_feed;
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                1022
        );
    }
}
