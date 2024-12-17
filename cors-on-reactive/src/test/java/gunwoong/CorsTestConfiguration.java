package gunwoong;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.reactive.server.WebTestClientBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

@TestConfiguration
public class CorsTestConfiguration {
    private static final String DEFAULT_ORIGIN = "http://default-origin.com";

    @Bean
    public WebTestClientBuilderCustomizer webTestClientBuilderCustomizer() {
        return builder -> builder.baseUrl("http://localhost:8081")
                .defaultHeader(HttpHeaders.ORIGIN, DEFAULT_ORIGIN);
    }
}
