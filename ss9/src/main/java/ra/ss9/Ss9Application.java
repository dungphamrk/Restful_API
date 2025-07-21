package ra.ss9;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ss9Application implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Ss9Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Ss9Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Application started successfully");
        logger.warn("This is a warning message");
        logger.error("This is an error message");
    }
}