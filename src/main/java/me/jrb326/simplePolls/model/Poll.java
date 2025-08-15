package me.jrb326.simplePolls.model;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class Poll {
    private String id;
    private String question;
    private String createdBy;
    private boolean isOpen;
    private Instant createdAt;
    private Instant endAt;
    private Instant closedAt;
    private String closedBy;
    private String closeReason;

    public UUID getIdAsUUID() {
        return UUID.fromString(id);
    }

    public UUID getCreatedByAsUUID() {
        return UUID.fromString(createdBy);
    }

    public UUID getClosedByAsUUID() {
        return closedBy != null ? UUID.fromString(closedBy) : null;
    }
}
