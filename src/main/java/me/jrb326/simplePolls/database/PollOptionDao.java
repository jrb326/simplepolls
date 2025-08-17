package me.jrb326.simplePolls.database;

import java.util.List;
import me.jrb326.simplePolls.model.PollOption;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

@RegisterBeanMapper(PollOption.class)
public interface PollOptionDao {

    @SqlQuery("SELECT * FROM poll_options WHERE poll_id = ? ORDER BY sort_order")
    List<PollOption> findByPollId(String pollId);
}
