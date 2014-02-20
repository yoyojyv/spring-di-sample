package spring.sample.dao;

import spring.sample.model.Account;

import java.util.List;

public interface AccountDao {

    List<Account> findAll() throws Exception;

}
