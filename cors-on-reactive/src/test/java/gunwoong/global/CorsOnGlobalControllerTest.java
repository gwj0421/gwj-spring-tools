package gunwoong.global;

import gunwoong.CorsTestConfiguration;
import gunwoong.controller.global.CorsOnGlobalController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

@WebFluxTest(CorsOnGlobalController.class)
@Import({CorsTestConfiguration.class, CorsOnGlobalTestConfiguration.class})
@ContextConfiguration(classes = CorsOnGlobalFunctionalConfiguration.class)
public class CorsOnGlobalControllerTest {
    private static final String CORS_ON_GLOBAL_URL = "/cors-on-global";
    private static final String CORS_ON_FUNCTIONAL_URL = "/global-config-on-functional";
    private static final String CORS_REGULAR_PUT_ENDPOINT = CORS_ON_GLOBAL_URL + "/regular-put-endpoint";
    private static final String CORS_REGULAR_DELETE_ENDPOINT = CORS_ON_GLOBAL_URL + "/regular-delete-endpoint";
    private static final String CORS_ON_FUNCTIONAL_ENDPOINT = CORS_ON_FUNCTIONAL_URL + "/cors-disabled-functional-endpoint";
    private static final String CORS_ALLOWED_ORIGIN = "http://allowed-origin.com";

    @Autowired
    private WebTestClient client;

    @Test
    void whenRequestPutEndpoint_thenObtainResponseWithCorsHeaders() {
        ResponseSpec response = client.put()
                .uri(CORS_REGULAR_PUT_ENDPOINT)
                .header(HttpHeaders.ORIGIN, CORS_ALLOWED_ORIGIN)
                .exchange();

        response.expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, CORS_ALLOWED_ORIGIN);
    }

    @Test
    void whenRequestDeleteEndpoint_thenObtainForbiddenResponse() {
        ResponseSpec response = client.delete()
                .uri(CORS_REGULAR_DELETE_ENDPOINT)
                .header(HttpHeaders.ORIGIN, CORS_ALLOWED_ORIGIN)
                .exchange();

        response.expectStatus().isForbidden();
    }

    @Test
    void whenPreflightAllowedHeader_thenObtainResponseWithCorsHeaders() {
        String allowedHeader = "Custom-Allowed-Header";
        String notAllowedHeader = "Custom-Not-Allowed-Header";

        ResponseSpec response = client.options()
                .uri(CORS_REGULAR_PUT_ENDPOINT)
                .header(HttpHeaders.ORIGIN, CORS_ALLOWED_ORIGIN)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, notAllowedHeader + ", " + allowedHeader)
                .exchange();

        response.expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, allowedHeader);
    }

    @Test
    void whenPreflightExposedHeader_thenObtainResponseWithCorsHeaders() {
        String exposedHeader = "Custom-Exposed-Header";
        ResponseSpec response = client.options()
                .uri(CORS_REGULAR_PUT_ENDPOINT)
                .header(HttpHeaders.ORIGIN, CORS_ALLOWED_ORIGIN)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
                .exchange();
        response.expectStatus().isOk()
                .expectHeader().value(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, value -> value.contains(exposedHeader));
    }

    @Test
    void whenPreflightFunctionalEndpoint_thenObtainResponseWithCorsHeader() {
        ResponseSpec response = client.options()
                .uri(CORS_ON_FUNCTIONAL_ENDPOINT)
                .header(HttpHeaders.ORIGIN, CORS_ALLOWED_ORIGIN)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
                .exchange();

        response.expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, CORS_ALLOWED_ORIGIN);
    }

}
