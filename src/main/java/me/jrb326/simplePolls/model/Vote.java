package me.jrb326.simplePolls.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Vote {
    String pollId;
    String optionId;
    String voterId;
    LocalDateTime votedAt;
}
