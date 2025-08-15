package me.jrb326.simplePolls.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import me.jrb326.simplePolls.database.PollDAO;
import me.jrb326.simplePolls.logging.InjectLogger;
import me.jrb326.simplePolls.model.Poll;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;

@Singleton
public class PollService {

    @InjectLogger
    private Logger logger;

    private final Jdbi jdbi;

    @Inject
    public PollService(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public Optional<Poll> getPoll(String pollId) {
        return jdbi.withExtension(PollDAO.class, dao -> dao.findById(pollId));
    }

    public boolean closePoll(String pollId, UUID closedBy, String reason) {
        int updated = jdbi.withExtension(
                PollDAO.class, dao -> dao.closePoll(pollId, Instant.now(), closedBy.toString(), reason));

        if (updated > 0) {
            logger.info("Poll {} closed by {} with reason: {}", pollId, closedBy, reason);
            return true;
        } else {
            logger.warn("Failed to close poll {} - poll not found or already closed", pollId);
            return false;
        }
    }

    public boolean removePoll(String pollId) {
        int deleted = jdbi.withExtension(PollDAO.class, dao -> dao.deletePoll(pollId));

        if (deleted > 0) {
            logger.info("Poll {} removed", pollId);
            return true;
        } else {
            logger.warn("Failed to remove poll {} - poll not found", pollId);
            return false;
        }
    }
}
