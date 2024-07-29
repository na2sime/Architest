package fr.nassime.endmcwebapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "fr.nassime.endmcwebapi.repository")
public class EndMcWebApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EndMcWebApiApplication.class, args);
    }

}
