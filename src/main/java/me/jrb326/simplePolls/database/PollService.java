package me.jrb326.simplePolls.database;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import me.jrb326.simplePolls.logging.InjectLogger;
import me.jrb326.simplePolls.model.Poll;
import me.jrb326.simplePolls.model.PollOption;
import me.jrb326.simplePolls.model.Vote;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;

public class PollService {

    @InjectLogger
    private Logger logger;

    private final Jdbi jdbi;

    @Inject
    public PollService(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public List<Poll> getActivePolls() {
        return jdbi.withExtension(PollDao.class, PollDao::findActivePolls);
    }

    public Optional<Poll> getPoll(String pollId) {
        return jdbi.withExtension(PollDao.class, dao -> dao.findById(pollId));
    }

    public List<PollOption> getPollOptions(String pollId) {
        return jdbi.withExtension(PollOptionDao.class, dao -> dao.findByPollId(pollId));
    }

    public Optional<Vote> getPlayerVote(String pollId, String playerId) {
        return jdbi.withExtension(VoteDao.class, dao -> dao.findVote(pollId, playerId));
    }

    public boolean hasPlayerVoted(String pollId, String playerId) {
        return getPlayerVote(pollId, playerId).isPresent();
    }

    public boolean castVote(String pollId, String optionId, String playerId) {
        try {
            int rows = jdbi.withExtension(VoteDao.class, dao -> dao.insertVote(pollId, optionId, playerId));
            logger.info("Player {} voted for option {} in poll {}", playerId, optionId, pollId);
            return rows > 0;
        } catch (Exception e) {
            logger.warn("Failed to cast vote for player {} in poll {}: {}", playerId, pollId, e.getMessage());
            return false;
        }
    }

    public int getVoteCount(String optionId) {
        return jdbi.withExtension(VoteDao.class, dao -> dao.getVoteCount(optionId));
    }
}
