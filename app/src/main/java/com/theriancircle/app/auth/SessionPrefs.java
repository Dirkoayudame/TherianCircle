package com.theriancircle.app.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.theriancircle.app.R;

public class SessionPrefs {
    private static final String PREF_NAME = "therian_session";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_SPECIES = "species";
    private static final String KEY_LAST_NOTIFIED_MESSAGE_TS = "last_notified_message_ts";

    private final SharedPreferences prefs;
    private final Context context;

    public SessionPrefs(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveProfile(String username, String species) {
        prefs.edit()
                .putString(KEY_USERNAME, username)
                .putString(KEY_SPECIES, species)
                .apply();
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, context.getString(R.string.default_username));
    }

    public String getSpecies() {
        return prefs.getString(KEY_SPECIES, context.getString(R.string.default_species));
    }

    public long getLastNotifiedMessageTimestamp() {
        return prefs.getLong(KEY_LAST_NOTIFIED_MESSAGE_TS, 0L);
    }

    public void setLastNotifiedMessageTimestamp(long timestamp) {
        prefs.edit().putLong(KEY_LAST_NOTIFIED_MESSAGE_TS, timestamp).apply();
    }
}
