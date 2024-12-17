package gunwoong.global;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@TestConfiguration
@EnableWebFlux
public class CorsOnGlobalTestConfiguration implements WebFluxConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://allowed-origin.com")
                    .allowedMethods("PUT")
                    .allowedHeaders("Custom-Allowed-Header", "Custom-Another-Allowed-Header")
                    .exposedHeaders("Custom-Exposed-Header", "Custom-Another-Exposed-Header")
                    .maxAge(3600);
        }
}
