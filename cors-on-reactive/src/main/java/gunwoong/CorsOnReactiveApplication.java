package gunwoong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.Collections;

@SpringBootApplication
public class CorsOnReactiveApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CorsOnReactiveApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
        app.run(args);
    }

    @Bean
    public SecurityWebFilterChain corsAnnotatedSpringSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(csrf->csrf.disable());
        return http.build();
    }
}
