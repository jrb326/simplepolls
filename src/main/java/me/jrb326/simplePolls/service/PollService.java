package me.jrb326.simplePolls.service;

import me.jrb326.simplePolls.logging.InjectLogger;
import me.jrb326.simplePolls.model.Poll;
import me.jrb326.simplePolls.model.PollOption;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Singleton
public class PollService {

    @InjectLogger
    private Logger logger;

    @Inject
    public PollService() {
        // Constructor for DI
    }

    public void createPoll(String question, List<PollOption> options, UUID createdBy) {
        String pollId = UUID.randomUUID().toString();
        
        // Filter out null options and only use valid ones
        List<PollOption> validOptions = options.stream()
            .filter(opt -> opt != null && opt.getOptionText() != null && !opt.getOptionText().trim().isEmpty())
            .toList();
        
        Poll poll = Poll.builder()
            .id(pollId)
            .question(question)
            .createdBy(createdBy)
            .isOpen(true)
            .createdAt(Instant.now())
            .options(validOptions)
            .build();

        // Assign poll ID to options and set sort order
        for (int i = 0; i < validOptions.size(); i++) {
            PollOption option = validOptions.get(i);
            option.setId(UUID.randomUUID().toString());
            option.setPollId(pollId);
            option.setSortOrder(i + 1);
        }

        logger.info("Creating poll '{}' with {} options for player {}", 
                   question, validOptions.size(), createdBy);
        
        // TODO: Implement database persistence
        // For now, just log the creation
        logger.info("Poll created with ID: {}", pollId);
    }

    public boolean isValidPollData(String question, List<PollOption> options) {
        if (question == null || question.trim().isEmpty()) {
            return false;
        }
        
        if (options == null || options.size() < 2 || options.size() > 6) {
            return false;
        }
        
        // Check that all options have text
        return options.stream()
                .allMatch(option -> option.getOptionText() != null && 
                         !option.getOptionText().trim().isEmpty());
    }
}