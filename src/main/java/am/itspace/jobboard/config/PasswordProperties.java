package am.itspace.jobboard.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "password")
public class PasswordProperties {
    private String oAuth2UserPassword;
}
