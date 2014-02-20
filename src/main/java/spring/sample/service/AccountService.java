package spring.sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import spring.sample.dao.AccountDao;
import spring.sample.model.Account;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

public class AccountService {

    @Inject
    private AccountDao accountDao;

    public AccountService() {}

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public List<Account> findDeliquentAccounts() throws Exception {
        List<Account> delinquentAccounts = new ArrayList<Account>();
        List<Account> accounts = accountDao.findAll();

        Date thirtyDaysAgo = daysAgo(30);
        for (Account account : accounts) {
            boolean owesMoney = account.getBalance()
                    .compareTo(BigDecimal.ZERO) > 0;
            boolean thirtyDaysLate = account.getLastPaidOn()
                    .compareTo(thirtyDaysAgo) <= 0;

            if (owesMoney && thirtyDaysLate) {
                delinquentAccounts.add(account);
            }
        }
        return delinquentAccounts;
    }

    private static Date daysAgo(int days) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, -days);
        return gc.getTime();
    }

}