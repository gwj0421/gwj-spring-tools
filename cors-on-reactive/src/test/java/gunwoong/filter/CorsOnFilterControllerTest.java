package gunwoong.filter;

import gunwoong.CorsTestConfiguration;
import gunwoong.controller.filter.CorsOnFilterController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

@WebFluxTest(CorsOnFilterController.class)
@Import({CorsTestConfiguration.class, CorsWebFilterConfiguration.class})
class CorsOnFilterControllerTest {
    private static final String CORS_ON_FILTER_URL = "cors-on-filter";
    private static final String CORS_ON_FILTER_REGULAR_PUT_ENDPOINT = CORS_ON_FILTER_URL + "/regular-put-endpoint";
    private static final String CORS_ON_FILTER_REGULAR_DELETE_ENDPOINT = CORS_ON_FILTER_URL + "/regular-delete-endpoint";
    private static final String CORS_ALLOWED_ORIGIN = "http://allowed-origin.com";

    @Autowired
    private WebTestClient client;

    @Test
    void whenPreflightRegularPutEndPoint_thenObtainResponseWithCorsHeaders() {
        ResponseSpec notAllowedCorsResponse = client.options()
                .uri(CORS_ON_FILTER_REGULAR_DELETE_ENDPOINT)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "DELETE")
                .header(HttpHeaders.ORIGIN, CORS_ALLOWED_ORIGIN)
                .exchange();
        ResponseSpec allowedCorsResponse = client.options()
                .uri(CORS_ON_FILTER_REGULAR_PUT_ENDPOINT)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
                .header(HttpHeaders.ORIGIN, CORS_ALLOWED_ORIGIN)
                .exchange();

        notAllowedCorsResponse.expectStatus().isForbidden();
        allowedCorsResponse.expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, CORS_ALLOWED_ORIGIN);
    }

}