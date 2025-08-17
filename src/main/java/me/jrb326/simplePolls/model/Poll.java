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
    private UUID createdBy;
    private boolean isOpen;
    private Instant createdAt;
    private Instant endAt;
    private Instant closedAt;
    private UUID closedBy;
    private String closeReason;
    private List<PollOption> options;
}