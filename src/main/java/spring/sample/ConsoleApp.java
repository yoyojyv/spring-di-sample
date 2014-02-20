package spring.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spring.sample.model.Account;
import spring.sample.model.Person;
import spring.sample.service.AccountService;

import java.util.List;

public class ConsoleApp {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleApp.class);

    public static void main(String[] args) throws Exception {

        ApplicationContext appCtx = new ClassPathXmlApplicationContext("applicationContext.xml");
        AccountService accountService = appCtx.getBean("accountService", AccountService.class);

        List<Account> delinquentAccounts = accountService.findDeliquentAccounts();

        for (Account a : delinquentAccounts) {
            logger.debug("delinquentAccount : " + a.getAccountNo());
        }

        logger.debug("--------------------------------");
        Person p = appCtx.getBean("jaeyong", Person.class);
        logger.debug("jaeyong : " + p.getFirstName() + " " + p.getLastName() + "[" +  p + "]");

        p = appCtx.getBean("yongsub", Person.class);
        logger.debug("yongsub : " + p.getFirstName() + " " + p.getLastName() + "[" +  p + "]");

        p = appCtx.getBean("minchan", Person.class);
        logger.debug("minchan : " + p.getFirstName() + " " + p.getLastName() + "[" +  p + "]");

        p = appCtx.getBean("yuri", Person.class);
        logger.debug("yuri : " + p.getFirstName() + " " + p.getLastName() + "[" +  p + "]");

        p = appCtx.getBean("jaeyong", Person.class);

        Person jaeyong = appCtx.getBean("jaeyong", Person.class);
        logger.debug("jaeyong : " + jaeyong.getFirstName() + " " + jaeyong.getLastName() + "[" +  jaeyong + "]");

        logger.debug(" p == jaeyong -> " + ( p == jaeyong) );

    }

}
