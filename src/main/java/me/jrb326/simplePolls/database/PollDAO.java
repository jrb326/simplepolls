package me.jrb326.simplePolls.database;

import java.time.Instant;
import java.util.Optional;
import me.jrb326.simplePolls.model.Poll;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterBeanMapper(Poll.class)
public interface PollDAO {

    @SqlQuery("SELECT * FROM polls WHERE id = :id")
    Optional<Poll> findById(@Bind("id") String id);

    @SqlUpdate(
            "UPDATE polls SET is_open = false, closed_at = :closedAt, closed_by = :closedBy, close_reason = :closeReason WHERE id = :id AND is_open = true")
    int closePoll(
            @Bind("id") String id,
            @Bind("closedAt") Instant closedAt,
            @Bind("closedBy") String closedBy,
            @Bind("closeReason") String closeReason);

    @SqlUpdate("DELETE FROM polls WHERE id = :id")
    int deletePoll(@Bind("id") String id);
}
