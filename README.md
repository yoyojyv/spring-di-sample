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
package spring.sample;

public interface AccountDao {
}
```

* JdbcAccountDao.java
```
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
```





