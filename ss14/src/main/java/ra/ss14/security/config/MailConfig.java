package ra.ss14.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    @Bean
    public JavaMailSenderImpl mailSender(JavaMailSenderImpl sender) {
        sender.setDefaultEncoding("UTF-8");
        return sender;
    }
}
