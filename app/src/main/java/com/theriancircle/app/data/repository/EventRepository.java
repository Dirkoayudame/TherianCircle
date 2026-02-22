package com.theriancircle.app.data.repository;

import com.theriancircle.app.data.model.Event;

import java.util.List;

public interface EventRepository {
    List<Event> getEvents();
    Event toggleEventAttendance(String eventId);
}
