package spring.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.sample.config.PersonConfig;
import spring.sample.model.Account;
import spring.sample.model.Person;
import spring.sample.service.AccountService;

import java.util.List;

public class ConsoleJavaConfigApp {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleJavaConfigApp.class);

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(PersonConfig.class, AnnotationConfigApplicationContext.class);

        Person p = ctx.getBean("jaeyong", Person.class);
        logger.debug("jaeyong : " + p.getFirstName() + " " + p.getLastName() + "[" +  p + "]");


        p = ctx.getBean(Person.class);
        logger.debug("person : " + p.getFirstName() + " " + p.getLastName() + "[" +  p + "]");


//        AccountService accountService = ctx.getBean("accountService",  AccountService.class);
//        List<Account> delinquentAccounts = accountService.findDeliquentAccounts();
//
//        for (Account a : delinquentAccounts) {
//            logger.debug("delinquentAccount : " + a.getAccountNo());
//        }
    }
}
