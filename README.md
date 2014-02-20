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
    "commons-dbcp:commons-dbcp:${version.commonDbcpVersion}"
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
        dataSource.setUrl("jdbc:mysql://localhost:3306/spring-study-db" +
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
url=:mysql://localhost:3306/spring-study-db?autoReconnect=true
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
                  value="jdbc:mysql://localhost:3306/spring-study-db?autoReconnect=true"/>
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
