# Spring DI 샘플


=====================================

### Step00. Gradle 프로젝트 설정, IDE로 import
Gradle은 1.10 버전을 사용하였습니다.
Java는 1.7 기준입니다.


* *nix 기준으로 진행
```
gradle wrapper
./gradlew check
./gradlew initProject
```

* eclipse 를 사용하는 경우
```
./gradlew eclipse
```
다음으로 IDE 에서 프로젝트를 import 함

다음 step 부터 진행 되는 내용은 모두 Spring in practice 서적의 소스 내용을 참조하였습니다.
Book [http://manning.com/wheeler/]
Blog [http://springinpractice.com/]
github [https://github.com/springinpractice]


### Step01. 간단한 DAO 작성하기1 - 예전 방식으로 구현

* 먼저 build.gradle 파일에 dependency 를 추가합니다.
```
compile(
    "commons-dbcp:commons-dbcp:${version.commonDbcp}"
)
```

* AccountDao.java 파일 작성
```
package spring.sample.dao;

public interface AccountDao {
}
```

* JdbcAccountDao.java
```
package spring.sample.dao.jdbc;

import org.apache.commons.dbcp.BasicDataSource;
import spring.sample.dao.AccountDao;

public class JdbcAccountDao implements AccountDao {
    private BasicDataSource dataSource;

    public JdbcAccountDao() {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/spring_study_db" +
                "?autoReconnect=true");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");
    }

}
```


### Step02. 간단한 DAO 작성하기2 - DI 적용하기, 비즈니스로직 추가하기

* JdbcAccountDao 를 수정합니다.
```
public class JdbcAccountDao implements AccountDao {

    private DataSource dataSource;

    public JdbcAccountDao() {}

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
```

* AccountService 를 추가합니다. (비즈니스로직)
```
package spring.sample.service;

import org.apache.commons.dbcp.BasicDataSource;
import spring.sample.dao.JdbcAccountDao;

import java.io.InputStream;
import java.util.Properties;

public class AccountService {
    private JdbcAccountDao accountDao;

    public AccountService() {
        try {
            Properties props = new Properties();
            InputStream inputStream = this.getClass().getClassLoader()
                    .getResourceAsStream("dataSource.properties");
            props.load(inputStream);

            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(
                    props.getProperty("driverClassName"));
            dataSource.setUrl(props.getProperty("url"));
            dataSource.setUsername(props.getProperty("username"));
            dataSource.setPassword(props.getProperty("password"));

            accountDao = new JdbcAccountDao();
            accountDao.setDataSource(dataSource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

* dataSource.properties 파일을 추가합니다.
```
driverClassName=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/spring_study_db?autoReconnect=true
username=root
password=1234
```


### Step03. DAO, 비즈니스로직 DI 적용하기

* AccountService 를 수정합니다.
```
public class AccountService {
    private AccountDao accountDao;

    public AccountService() {}

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

}
```

* applicationContext.xml 파일을 만듭니다.
```
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        ">

    <!-- dataSource -->
    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
          destroy-method="close">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
        <property name="url"
                  value="jdbc:mysql://localhost:3306/spring_study_db?autoReconnect=true"/>
        <property name="username" value="root"/>
        <property name="password" value="1234"/>
    </bean>

    <!-- dao -->
    <bean id="accountDao"
          class="spring.sample.dao.jdbc.JdbcAccountDao">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- service -->
    <bean id="accountService"
          class="spring.sample.service.AccountService">
        <property name="accountDao" ref="accountDao"/>
    </bean>
</beans>
```

### Step04. Domain class 작성하기

* Account domain class 를 만듭니다.
```
package spring.sample.model;

import java.math.BigDecimal;
import java.util.Date;

public class Account {

    private String accountNo;
    private BigDecimal balance;
    private Date lastPaidOn;

    public Account(String accountNo, BigDecimal balance, Date lastPaidOn) {
        this.accountNo = accountNo;
        this.balance = balance;
        this.lastPaidOn = lastPaidOn;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Date getLastPaidOn() {
        return lastPaidOn;
    }
}
```

* AccountDao 에 interface 를 추가합니다.
```
public interface AccountDao {

    List<Account> findAll() throws Exception;

}
```

* CsvAccountDao 를 작성합니다.
```
package spring.sample.dao.csv;

import org.springframework.core.io.Resource;
import spring.sample.dao.AccountDao;
import spring.sample.model.Account;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CsvAccountDao implements AccountDao {

    private Resource csvResource;

    public void setCsvResource(Resource csvFile) {
        this.csvResource = csvFile;
    }

    public List<Account> findAll() throws Exception {
        List<Account> results = new ArrayList<Account>();

        DateFormat fmt = new SimpleDateFormat("MMddyyyy");
        BufferedReader br = new BufferedReader(
                new FileReader(csvResource.getFile()));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");

            String accountNo = fields[0];
            BigDecimal balance = new BigDecimal(fields[1]);
            Date lastPaidOn = fmt.parse(fields[2]);
            Account account =
                    new Account(accountNo, balance, lastPaidOn);
            results.add(account);
        }
        br.close();
        return results;
    }
}
```

* JdbcAccountDao 에도 findAll() 메소드를 구현합니다.
```
    public List<Account> findAll() throws Exception {
        throw new UnsupportedOperationException("This method has not been implemented");
    }
```

* 다음의 메소드를 AccountService 에 추가합니다.
```
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
```


* main 메소드가 있는 실행 클래스를 만들고 실행해 봅니다.
```
package spring.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spring.sample.model.Account;
import spring.sample.service.AccountService;

import java.util.List;

public class ConsolApp {

    private static final Logger logger = LoggerFactory.getLogger(ConsolApp.class);

    public static void main(String[] args) throws Exception {

        ApplicationContext appCtx = new ClassPathXmlApplicationContext("applicationContext.xml");
        AccountService accountService = appCtx.getBean("accountService", AccountService.class);

        List<Account> delinquentAccounts = accountService.findDeliquentAccounts();

        for (Account a : delinquentAccounts) {
            logger.debug("delinquentAccount : " + a.getAccountNo());
        }
    }

}
```


### Step05. properties 설정 읽어오기

* properties 파일을 만듭니다.
```
dataSource.driverClassName=com.mysql.jdbc.Driver
dataSource.url=jdbc:mysql://localhost:3306/spring_study_db?autoReconnect=true
dataSource.username=root
dataSource.password=1234
mailSender.host=mail.sample.com
recaptcha.publicKey=get_one_from_recaptcha_website
recaptcha.privateKey=get_one_from_recaptcha_website
```

* applicationContext.xml 파일을 수정합니다. 밑의 소스를 넣어주세요.
```
    <bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="location" value="springSample.properties"/>
    </bean>

    <bean id="dataSource"
          class="org.apache.commons.dbcp.BasicDataSource"
          destroy-method="close">
        <property name="driverClassName"
                  value="${dataSource.driverClassName}"/>
        <property name="url" value="${dataSource.url}"/>
        <property name="username" value="${dataSource.username}"/>
        <property name="password" value="${dataSource.password}"/>
    </bean>
```






