package spring.sample.dao.jdbc;

import org.springframework.jdbc.core.RowMapper;
import spring.sample.model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRowMapper implements RowMapper<Account> {

    @Override
    public Account mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Account account = new Account();
        account.setAccountNo(resultSet.getString("accountNo"));
        account.setBalance(resultSet.getBigDecimal("balance"));
        account.setLastPaidOn(resultSet.getDate("lastPaidOn"));
        return account;
    }
}
