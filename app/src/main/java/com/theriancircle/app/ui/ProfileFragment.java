package com.theriancircle.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.theriancircle.app.R;
import com.theriancircle.app.auth.SessionPrefs;

public class ProfileFragment extends Fragment {
    private static final String ARG_USERNAME = "arg_username";
    private static final String ARG_SPECIES = "arg_species";

    public static ProfileFragment newInstance(String username, String species) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_SPECIES, species);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView profileName = view.findViewById(R.id.profileName);
        TextView profileSpecies = view.findViewById(R.id.profileSpecies);
        Button logoutButton = view.findViewById(R.id.logoutButton);

        Bundle args = getArguments();
        SessionPrefs sessionPrefs = new SessionPrefs(requireContext());
        String username = args != null
                ? args.getString(ARG_USERNAME, getString(R.string.default_username))
                : sessionPrefs.getUsername();
        String species = args != null
                ? args.getString(ARG_SPECIES, getString(R.string.default_species))
                : sessionPrefs.getSpecies();

        profileName.setText(username);
        profileSpecies.setText(getString(R.string.profile_theriotype, species));
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        return view;
    }
}
