package spring.sample;

import org.apache.commons.dbcp.BasicDataSource;

public class JdbcAccountDao implements AccountDao {
    private BasicDataSource dataSource;

    public JdbcAccountDao() {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/spring-study-db" +
                "?autoReconnect=true");
        dataSource.setUsername("root");
        dataSource.setPassword("");
    }

}