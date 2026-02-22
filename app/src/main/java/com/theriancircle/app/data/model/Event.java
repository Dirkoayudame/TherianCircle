package com.theriancircle.app.data.model;

public class Event {
    private final String id;
    private final String title;
    private final String location;
    private final String dateLabel;
    private final int attendeesCount;
    private final boolean attendingByMe;

    public Event(String id, String title, String location, String dateLabel) {
        this(id, title, location, dateLabel, 0, false);
    }

    public Event(
            String id,
            String title,
            String location,
            String dateLabel,
            int attendeesCount,
            boolean attendingByMe
    ) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.dateLabel = dateLabel;
        this.attendeesCount = attendeesCount;
        this.attendingByMe = attendingByMe;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDateLabel() {
        return dateLabel;
    }

    public int getAttendeesCount() {
        return attendeesCount;
    }

    public boolean isAttendingByMe() {
        return attendingByMe;
    }
}
