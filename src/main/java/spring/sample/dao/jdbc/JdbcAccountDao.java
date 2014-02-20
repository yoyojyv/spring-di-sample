package spring.sample.dao.jdbc;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import spring.sample.dao.AccountDao;
import spring.sample.model.Account;

import java.util.List;

public class JdbcAccountDao implements AccountDao {

    private static final String FIND_ALL_SQL = "SELECT * FROM Account";

    private NamedParameterJdbcOperations jdbcTemplate;

    private AccountRowMapper accountRowMapper;

    public void setJdbcTemplate(NamedParameterJdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setAccountRowMapper(AccountRowMapper accountRowMapper) {
        this.accountRowMapper = accountRowMapper;
    }

    public List<Account> findAll() throws Exception {
        return jdbcTemplate.query(FIND_ALL_SQL, accountRowMapper);
    }

}