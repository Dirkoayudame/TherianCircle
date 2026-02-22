package com.theriancircle.app.data.model;

public class User {
    private final String id;
    private final String username;
    private final String species;

    public User(String id, String username, String species) {
        this.id = id;
        this.username = username;
        this.species = species;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getSpecies() {
        return species;
    }
}
