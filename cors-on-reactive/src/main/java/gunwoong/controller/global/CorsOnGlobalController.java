package gunwoong.controller.global;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cors-on-global")
public class CorsOnGlobalController {
    @PutMapping("/regular-put-endpoint")
    public Mono<String> regularCorsEndPoint() {
        return Mono.just("Regular put endpoint");
    }

    @DeleteMapping("/regular-delete-endpoint")
    public Mono<String> regularDeleteEndpoint() {
        return Mono.just("Regular delete endpoint");
    }
}
