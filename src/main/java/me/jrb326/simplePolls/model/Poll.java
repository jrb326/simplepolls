package me.jrb326.simplePolls.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Poll {
    String id;
    String question;
    String createdBy;
    boolean isOpen;
    LocalDateTime createdAt;
    LocalDateTime endAt;
    LocalDateTime closedAt;
    String closedBy;
    String closeReason;

    public boolean isActive() {
        return isOpen && (endAt == null || endAt.isAfter(LocalDateTime.now()));
    }
}
