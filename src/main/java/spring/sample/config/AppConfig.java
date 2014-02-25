package spring.sample.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource(value="classpath:springSample.properties")
@ComponentScan("spring.sample")
public class AppConfig {

    @Autowired
    Environment env;

//    @Bean
//    public DataSource dataSource() {
//        BasicDataSource dataSource = new org.apache.commons.dbcp.BasicDataSource();
//        dataSource.setUrl("jdbc:mysql://localhost:3306/spring_study_db?autoReconnect=true");
//        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSource.setUsername("root");
//        dataSource.setPassword("1234");
//
//        return dataSource;
//    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new org.apache.commons.dbcp.BasicDataSource();
        dataSource.setUrl(env.getProperty("dataSource.url"));
        dataSource.setDriverClassName(env.getProperty("dataSource.driverClassName"));
        dataSource.setUsername(env.getProperty("dataSource.username"));
        dataSource.setPassword(env.getProperty("dataSource.password"));

        return dataSource;
    }

    @Bean
    public NamedParameterJdbcTemplate jdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }


}
