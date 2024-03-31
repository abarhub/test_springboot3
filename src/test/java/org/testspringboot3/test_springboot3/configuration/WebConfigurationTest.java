package org.testspringboot3.test_springboot3.configuration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import org.testspringboot3.test_springboot3.handler.ProduitHandler;

class WebConfigurationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebConfigurationTest.class);

    private WebConfiguration webConfiguration = new WebConfiguration();

    @BeforeEach
    void setUp() {}

    @Test
    void routerFunction() throws Exception {
        // ARRANGE
        ProduitHandler produitHandler = mock(ProduitHandler.class);
        ServerResponse response = ServerResponse.status(123).body("abc");
        when(produitHandler.getProduits(any(ServerRequest.class))).thenReturn(response);
        var res = webConfiguration.routerFunction(produitHandler);
        ServerRequest request2 = getServerRequest("/produit2");

        // ACT
        var res2 = res.route(request2);

        // ASSERT
        assertNotNull(res2);
        LOGGER.info("res2={}", res2);
        assertTrue(res2.isPresent());
        LOGGER.info("res2.get()={}", res2.get());
        var res3 = res2.get().handle(request2);
        LOGGER.info("res3={}", res3);
        LOGGER.info("res3.statusCode={}", res3.statusCode());
        assertEquals(123, res3.statusCode().value());
    }

    @Test
    void routerFunction2() throws Exception {
        // ARRANGE
        ProduitHandler produitHandler = mock(ProduitHandler.class);
        ServerResponse response = ServerResponse.status(125).body("abc");
        when(produitHandler.getProduit(any(ServerRequest.class))).thenReturn(response);
        var res = webConfiguration.routerFunction(produitHandler);
        ServerRequest request2 = getServerRequest("/produit2/12");

        // ACT
        var res2 = res.route(request2);

        // ASSERT
        assertNotNull(res2);
        LOGGER.info("res2={}", res2);
        assertTrue(res2.isPresent());
        LOGGER.info("res2.get()={}", res2.get());
        var res3 = res2.get().handle(request2);
        LOGGER.info("res3={}", res3);
        LOGGER.info("res3.statusCode={}", res3.statusCode());
        assertEquals(125, res3.statusCode().value());
    }

    // méthodes privés

    private static ServerRequest getServerRequest(String str) {
        var mockServletContext = new MockServletContext();
        List<HttpMessageConverter<?>> listeMessageConverter = List.of();
        MockHttpServletRequestBuilder tmp2 = MockMvcRequestBuilders.get(URI.create(str));
        ServerRequest request2 =
                ServerRequest.create(tmp2.buildRequest(mockServletContext), listeMessageConverter);
        return request2;
    }
}
