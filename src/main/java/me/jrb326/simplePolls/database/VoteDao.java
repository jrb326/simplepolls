package me.jrb326.simplePolls.database;

import java.util.Optional;
import me.jrb326.simplePolls.model.Vote;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterBeanMapper(Vote.class)
public interface VoteDao {

    @SqlQuery("SELECT * FROM votes WHERE poll_id = ? AND voter_id = ?")
    Optional<Vote> findVote(String pollId, String voterId);

    @SqlUpdate("INSERT INTO votes (poll_id, option_id, voter_id) VALUES (?, ?, ?)")
    int insertVote(String pollId, String optionId, String voterId);

    @SqlQuery("SELECT COUNT(*) FROM votes WHERE option_id = ?")
    int getVoteCount(String optionId);
}
