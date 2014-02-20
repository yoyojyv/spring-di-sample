package spring.sample.service;

import org.apache.commons.dbcp.BasicDataSource;
import spring.sample.dao.AccountDao;
import spring.sample.dao.jdbc.JdbcAccountDao;

import java.io.InputStream;
import java.util.Properties;

public class AccountService {
    private AccountDao accountDao;

    public AccountService() {}

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

}