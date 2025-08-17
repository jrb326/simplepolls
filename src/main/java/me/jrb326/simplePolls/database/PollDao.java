package me.jrb326.simplePolls.database;

import java.util.List;
import java.util.Optional;
import me.jrb326.simplePolls.model.Poll;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

@RegisterBeanMapper(Poll.class)
public interface PollDao {

    @SqlQuery(
            "SELECT * FROM polls WHERE is_open = true AND (end_at IS NULL OR end_at > CURRENT_TIMESTAMP) ORDER BY created_at DESC")
    List<Poll> findActivePolls();

    @SqlQuery("SELECT * FROM polls WHERE id = ?")
    Optional<Poll> findById(String id);
}
