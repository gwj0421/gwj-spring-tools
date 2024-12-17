package gunwoong.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@TestConfiguration
public class CorsOnGlobalFunctionalConfiguration {
    @Bean
    public CorsGlobalFunctionalHandler functionalHandler() {
        return new CorsGlobalFunctionalHandler();
    }

    @Bean
    public RouterFunction<ServerResponse> corsGlobalRouter(@Autowired CorsGlobalFunctionalHandler handler) {
        return RouterFunctions.route()
                .PUT("/global-config-on-functional/cors-disabled-functional-endpoint", handler::useHandler)
                .build();
    }


    public static class CorsGlobalFunctionalHandler {
        public Mono<ServerResponse> useHandler(final ServerRequest request) {
            String responseMessage = "CORS GLOBAL CONFIG IS EFFECTIVE ON FUNCTIONAL ENDPOINTS!!!";
            return ServerResponse.ok()
                    .body(Mono.just(responseMessage), String.class);
        }
    }
}
