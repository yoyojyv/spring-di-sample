package spring.sample.dao.jdbc;

import org.apache.commons.dbcp.BasicDataSource;
import spring.sample.dao.AccountDao;

public class JdbcAccountDao implements AccountDao {
    private BasicDataSource dataSource;

    public JdbcAccountDao() {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/spring-study-db" +
                "?autoReconnect=true");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");
    }

}