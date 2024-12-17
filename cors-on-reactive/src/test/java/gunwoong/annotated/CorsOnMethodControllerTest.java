package gunwoong.annotated;

import gunwoong.CorsTestConfiguration;
import gunwoong.controller.annotated.CorsOnMethodController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(CorsOnMethodController.class)
@Import(CorsTestConfiguration.class)
class CorsOnMethodControllerTest {
    private static final String CORS_ON_METHOD_URL = "/cors-on-method";
    private static final String CORS_REGULAR_ENDPOINT = CORS_ON_METHOD_URL + "/regular-endpoint";
    private static final String CORS_ENABLED_ENDPOINT = CORS_ON_METHOD_URL + "/cors-enabled-all-endpoint";
    private static final String CORS_RESTRICTIVE_ORIGIN_ENDPOINT = CORS_ON_METHOD_URL + "/cors-enabled-origin-restrictive-endpoint";
    private static final String CORS_RESTRICTIVE_HEADER_ENDPOINT = CORS_ON_METHOD_URL + "/cors-enabled-header-restrictive-endpoint";
    private static final String CORS_EXPOSED_HEADER_ENDPOINT = CORS_ON_METHOD_URL + "/cors-enabled-exposed-header-endpoint";
    private static final String ALLOWED_ORIGIN = "http://allowed-origin.com";
    private static final String NOT_ALLOWED_ORIGIN = "http://not-allowed-origin.com";

    @Autowired
    WebTestClient client;

    @Test
    void whenPreflightMethodNotConfigureCors_thenObtainResponseWithForbidden() {
        WebTestClient.ResponseSpec response = client.options()
                .uri(CORS_REGULAR_ENDPOINT)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
                .exchange();

        response.expectStatus().isForbidden()
                .expectHeader().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
    }

    @Test
    void whenPreflightMethodConfigureCors_thenObtainResponseWithCorsHeaders() {
        String any_origin = "http://any-origin.com";
        WebTestClient.ResponseSpec response = client.options()
                .uri(CORS_ENABLED_ENDPOINT)
                .header(HttpHeaders.ORIGIN, any_origin)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
                .exchange();

        response.expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
    }

    @Test
    void whenPreflightMethodConfigureRestrictiveCors_thenObtainResponseWithCorsHeaders() {
        WebTestClient.ResponseSpec notAllowedOriginResponse = client.options()
                .uri(CORS_RESTRICTIVE_ORIGIN_ENDPOINT)
                .header(HttpHeaders.ORIGIN, NOT_ALLOWED_ORIGIN)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
                .exchange();
        WebTestClient.ResponseSpec allowedOriginResponse = client.options()
                .uri(CORS_RESTRICTIVE_ORIGIN_ENDPOINT)
                .header(HttpHeaders.ORIGIN, ALLOWED_ORIGIN)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
                .exchange();

        notAllowedOriginResponse.expectStatus().isForbidden()
                .expectHeader().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
        allowedOriginResponse.expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ALLOWED_ORIGIN);
    }

    @Test
    void whenPreflightMethodAllowedHeaders_thenObtainResponseCorsHeader() {
        String any_origin = "http://any-origin.com";
        String notAllowedHeaderKey = "Custom-Not-Allowed-Header";
        String allowedHeaderKey = "Custom-Allowed-Header";

        WebTestClient.ResponseSpec allowedHeaderResponse = client.options()
                .uri(CORS_RESTRICTIVE_HEADER_ENDPOINT)
                .header(HttpHeaders.ORIGIN, ALLOWED_ORIGIN)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, notAllowedHeaderKey + ", " + allowedHeaderKey)
                .exchange();

        allowedHeaderResponse.expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, allowedHeaderKey);
    }

    @Test
    void whePreflightMethodExposedHeader_thenObtainResponseCorsHeader() {
        String exposedHeader = "Custom-Exposed-Header";

        WebTestClient.ResponseSpec response = client.options()
                .uri(CORS_EXPOSED_HEADER_ENDPOINT)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
                .exchange();

        response.expectStatus().isOk()
                .expectHeader().value(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, exposed -> exposed.contains(exposedHeader));
    }
}