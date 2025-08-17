package me.jrb326.simplePolls.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Poll {
    private String id;
    private String question;
    private String createdBy;  // Store as string for database compatibility
    private boolean isOpen;
    private Instant createdAt;
    private Instant endAt;
    private Instant closedAt;
    private String closedBy;  // Store as string for database compatibility
    private String closeReason;
    private List<PollOption> options;

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
