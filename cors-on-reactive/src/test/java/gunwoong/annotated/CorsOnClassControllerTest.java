package gunwoong.annotated;

import gunwoong.CorsTestConfiguration;
import gunwoong.controller.annotated.CorsOnClassController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

@WebFluxTest(CorsOnClassController.class)
@Import(CorsTestConfiguration.class)
class CorsOnClassControllerTest {
    private static final String CORS_ON_CONTROLLER_URL = "/cors-on-controller";
    private static final String CORS_REGULAR_ENDPOINT = CORS_ON_CONTROLLER_URL + "/regular-endpoint";
    private static final String CORS_RESTRICTIVE_ENDPOINT = CORS_ON_CONTROLLER_URL + "/cors-enabled-origin-restrictive-endpoint";
    private static final String CORS_EXPOSED_ENDPOINT = CORS_ON_CONTROLLER_URL + "/cors-enabled-exposed-header-endpoint";
    private static final String ALLOWED_ORIGIN = "http://allowed-origin.com";
    private static final String NOT_ALLOWED_ORIGIN = "http://not-allowed-origin.com";

    @Autowired
    private WebTestClient client;

    @Test
    void whenPreflightControllerCorsRegularEndpoint_thenObtainResponseWithCorsHeaders() {
        ResponseSpec notAllowedResponse = client.options()
                .uri(CORS_REGULAR_ENDPOINT)
                .header(HttpHeaders.ORIGIN, NOT_ALLOWED_ORIGIN)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
                .exchange();
        ResponseSpec allowedResponse = client.options()
                .uri(CORS_REGULAR_ENDPOINT)
                .header(HttpHeaders.ORIGIN, ALLOWED_ORIGIN)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
                .exchange();

        notAllowedResponse
                .expectStatus().isForbidden()
                .expectHeader().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
        allowedResponse
                .expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ALLOWED_ORIGIN);
    }

    @Test
    void whenPreflightControllerCorsRestrictiveAnotherAllowedOrigin_thenObtainResponseWithCorsHeaders() {
        String anotherAllowedOrigin = "http://another-allowed-origin.com";
        ResponseSpec response = client.options()
                .uri(CORS_RESTRICTIVE_ENDPOINT)
                .header(HttpHeaders.ORIGIN, anotherAllowedOrigin)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
                .exchange();

        response.expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, anotherAllowedOrigin);
    }

    @Test
    void whePreflightControllerCorsExposingHeaders_thenObtainResponseWithCorsHeaders() {
        String exposedHeader = "Custom-Exposed-Header";
        ResponseSpec response = client.options()
                .uri(CORS_EXPOSED_ENDPOINT)
                .header(HttpHeaders.ORIGIN, ALLOWED_ORIGIN)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
                .exchange();

        response.expectStatus().isOk()
                .expectHeader().value(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, value->value.contains(exposedHeader));
    }
}