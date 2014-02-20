package spring.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.sample.model.Person;

@Configuration
public class PersonConfig {

    @Bean
    public Person jaeyong() {
        return new Person("Jaeyong", "Kim");
    }

}
