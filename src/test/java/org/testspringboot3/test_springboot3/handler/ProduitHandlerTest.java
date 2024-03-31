package org.testspringboot3.test_springboot3.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.json.BasicJsonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
// import org.testspringboot3.test_springboot3.configuration.WebConfigurationTest;
import org.testspringboot3.test_springboot3.domain.Produit;
import org.testspringboot3.test_springboot3.service.ProduitService;

class ProduitHandlerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProduitHandlerTest.class);

    private ProduitService produitService;

    private ProduitHandler produitHandler;

    private final BasicJsonTester json = new BasicJsonTester(getClass());

    @BeforeEach
    void setUp() {
        produitService = mock(ProduitService.class);
        produitHandler = new ProduitHandler(produitService);
    }

    @Test
    void getProduit() throws Exception {
        // ARRANGE
        Produit produit = new Produit();
        produit.setId(45L);
        when(produitService.findById(45)).thenReturn(Optional.of(produit));
        // ServerRequest request2 = getServerRequest("/produit2/{produitId}",45);
        var tuple=getServerRequest("/produit2/{produitId}",45);

        var tmp3=tuple.request;
//        var mockServletContext = tuple.request;//new MockServletContext();
        //        var toto=new MappingJackson2HttpMessageConverter()
//        List<HttpMessageConverter<?>> listeMessageConverter =
//                List.of(new MappingJackson2HttpMessageConverter());
//        MockHttpServletRequestBuilder tmp2 =
//                MockMvcRequestBuilders.get("/produit2/{produitId}", 45);
//        var tmp3 = tmp2.buildRequest(mockServletContext);
//        Map<String, Object> map = Map.of("produitId", "45");
//        tmp3.setAttribute(RouterFunctions.URI_TEMPLATE_VARIABLES_ATTRIBUTE, map);
        ServerRequest request2 = tuple.serverRequest;//ServerRequest.create(tmp3, listeMessageConverter);

        // ACT
        var res = produitHandler.getProduit(request2);

        // ASSERT
        assertNotNull(res);
        assertTrue(res.statusCode().is2xxSuccessful());
        assertEquals(HttpStatus.OK, res.statusCode());
        MockHttpServletResponse mockHttpServletResponse = tuple.response;//new MockHttpServletResponse();
        var context = tuple.context;//mock(ServerResponse.Context.class);
//        when(context.messageConverters()).thenReturn(listeMessageConverter);
        res.writeTo(tmp3, mockHttpServletResponse, context);
        var s = mockHttpServletResponse.getContentAsString();
        LOGGER.info("res={}", s);
        assertEquals("{\"id\":45,\"nom\":null,\"description\":null}", s);
        assertThat(json.from(s))
                .hasJsonPath("$.id")
                .hasJsonPathValue("$.id")
                .extractingJsonPathValue("$.id")
                .isEqualTo(45);
    }

    @Test
    void getProduits() {}

    // méthodes privés

    private static TupleServer getServerRequest(String url, int idProduit) {
        var mockServletContext = new MockServletContext();
//        List<HttpMessageConverter<?>> listeMessageConverter = List.of();
        List<HttpMessageConverter<?>> listeMessageConverter =
                List.of(new MappingJackson2HttpMessageConverter());
        MockHttpServletRequestBuilder tmp2 = MockMvcRequestBuilders.get(url, idProduit);
        MockHttpServletRequest tmp3 = tmp2.buildRequest(mockServletContext);
        Map<String, Object> map = Map.of("produitId", ""+idProduit);
        tmp3.setAttribute(RouterFunctions.URI_TEMPLATE_VARIABLES_ATTRIBUTE, map);
        ServerRequest request2 = ServerRequest.create(tmp3, listeMessageConverter);
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        var context = mock(ServerResponse.Context.class);
        when(context.messageConverters()).thenReturn(listeMessageConverter);
        return new TupleServer(request2, tmp3, mockHttpServletResponse, context);
    }

    record TupleServer(
            ServerRequest serverRequest,
            MockHttpServletRequest request,
            MockHttpServletResponse response,
            ServerResponse.Context context) {}
}
