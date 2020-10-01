package io.pivotal.pal.tracker;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var stmt = connection.prepareStatement(
                    "insert into time_entries (project_id, user_id, date, hours) values (?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS
            );

            stmt.setLong(1, timeEntry.getProjectId());
            stmt.setLong(2, timeEntry.getUserId());
            stmt.setDate(3, Date.valueOf(timeEntry.getDate()));
            stmt.setInt(4, timeEntry.getHours());

            return stmt;
        }, keyHolder);

        return find(keyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(Long id) {
        return jdbcTemplate.query(
            "select id, project_id, user_id, date, hours from time_entries where id = ?;",
                new Object[] {id},
                extractor
        );
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query(
                "select id, project_id, user_id, date, hours from time_entries;",
                mapper
        );
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        jdbcTemplate.update(connection -> {
            var stmt = connection.prepareStatement(
                    "update time_entries set project_id = ?, user_id = ?, date = ?, hours = ? where id = ?;"
            );

            stmt.setLong(1, timeEntry.getProjectId());
            stmt.setLong(2, timeEntry.getUserId());
            stmt.setDate(3, Date.valueOf(timeEntry.getDate()));
            stmt.setInt(4, timeEntry.getHours());
            stmt.setLong(5, id);

            return stmt;
        });

        return find(id);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(connection -> {
            var stmt = connection.prepareStatement(
                    "delete from time_entries where id = ?;"
            );

            stmt.setLong(1, id);

            return stmt;
        });
    }

    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;
}
