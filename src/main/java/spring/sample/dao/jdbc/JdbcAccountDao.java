package spring.sample.dao.jdbc;

import spring.sample.dao.AccountDao;
import spring.sample.model.Account;

import javax.sql.DataSource;
import java.util.List;

public class JdbcAccountDao implements AccountDao {

    private DataSource dataSource;

    public JdbcAccountDao() {}

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Account> findAll() throws Exception {
        throw new UnsupportedOperationException("This method has not been implemented");
    }

}