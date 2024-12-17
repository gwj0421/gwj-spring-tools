package gunwoong.controller.annotated;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cors-on-method")
public class CorsOnMethodController {
    @PutMapping("/regular-endpoint")
    public Mono<String> corsDisabledEndpoint() {
        return Mono.just("Regular endpoint");
    }

    @CrossOrigin
    @PutMapping("/cors-enabled-all-endpoint")
    public Mono<String> corsEnabledAllEndpoint() {
        return Mono.just("Cors enabled endpoint");
    }

    @CrossOrigin({"http://allowed-origin.com"})
    @PutMapping("/cors-enabled-origin-restrictive-endpoint")
    public Mono<String> corsEnabledOriginRestrictiveEndpoint() {
        return Mono.just("Cors enabled endpoint - Header Restrictive");
    }

    @CrossOrigin(allowedHeaders = {"Custom-Allowed-Header"})
    @PutMapping("/cors-enabled-header-restrictive-endpoint")
    public Mono<String> corsEnabledHeadersRestrictiveEndpoint() {
        return Mono.just("Cors enabled endpoint - Allowed Header");
    }

    @CrossOrigin(exposedHeaders = {"Custom-Exposed-Header"})
    @PutMapping("/cors-enabled-exposed-header-endpoint")
    public Mono<String> corsEnabledHeaderExposedEndpoint() {
        return Mono.just("Cors enabled endpoint - Exposed Header");
    }

}
