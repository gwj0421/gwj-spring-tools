package gunwoong.controller.annotated;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@CrossOrigin(value = {"http://allowed-origin.com"}, allowedHeaders = {"Custom-Allowed-Header"}, maxAge = 900)
@RestController
@RequestMapping("/cors-on-controller")
public class CorsOnClassController {

    @PutMapping("/regular-endpoint")
    public Mono<String> corsDisabledEndpoint() {
        return Mono.just("Regular endpoint");
    }

    @CrossOrigin()
    @PutMapping("/cors-enabled-all-endpoint")
    public Mono<String> corsEnabledEndpoint() {
        return Mono.just("Cors enabled endpoint");
    }

    @CrossOrigin({"http://another-allowed-origin.com"})
    @PutMapping("/cors-enabled-origin-restrictive-endpoint")
    public Mono<String> corsEnabledHeaderRestrictiveEndpoint() {
        return Mono.just("Cors enabled endpoint - Header Restrictive");
    }

    @CrossOrigin(exposedHeaders = {"Custom-Exposed-Header"})
    @PutMapping("/cors-enabled-exposed-header-endpoint")
    public Mono<String> corsEnabledExposedHeadersEndpoint() {
        return Mono.just("Cors enabled endpoint - Exposed Header");
    }

    @CrossOrigin(allowedHeaders = {"Custom-Allowed", "Custom-Other-Allowed"})
    @PutMapping("/cors-enabled-mixed-endpoint")
    public Mono<String> corsEnabledHeaderExposedEndpoint() {
        return Mono.just("Cors enabled endpoint - Mix Config");
    }
}
