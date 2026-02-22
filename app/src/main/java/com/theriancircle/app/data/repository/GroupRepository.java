package com.theriancircle.app.data.repository;

import com.theriancircle.app.data.model.CommunityGroup;

import java.util.List;

public interface GroupRepository {
    List<CommunityGroup> getGroups();
    CommunityGroup toggleGroupMembership(String groupId);
}
