package com.theriancircle.app.data.model;

public class CommunityGroup {
    private final String id;
    private final String name;
    private final int membersCount;
    private final boolean joinedByMe;

    public CommunityGroup(String id, String name, int membersCount) {
        this(id, name, membersCount, false);
    }

    public CommunityGroup(String id, String name, int membersCount, boolean joinedByMe) {
        this.id = id;
        this.name = name;
        this.membersCount = membersCount;
        this.joinedByMe = joinedByMe;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public boolean isJoinedByMe() {
        return joinedByMe;
    }
}
