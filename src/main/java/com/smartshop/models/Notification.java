package com.smartshop.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@NoArgsConstructor
@Setter
@Getter
public class Notification implements Serializable {

    private String message;

    private NotificationAction action;

    private Date created_at;

    public Notification(String name, NotificationAction action) {
        this.message = name;
        this.action = action;
        this.created_at = Date.from(Instant.now());
    }
}
