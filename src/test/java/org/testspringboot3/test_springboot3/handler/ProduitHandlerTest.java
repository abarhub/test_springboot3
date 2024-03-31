package org.testspringboot3.test_springboot3.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
        long id = 45;
        Produit produit = new Produit();
        produit.setId(id);
        when(produitService.findById(id)).thenReturn(Optional.of(produit));
        var tuple = getServerRequest("/produit2/{produitId}", id);

        var mockHttpServletRequest = tuple.request;
        ServerRequest request2 = tuple.serverRequest;

        // ACT
        var res = produitHandler.getProduit(request2);

        // ASSERT
        assertNotNull(res);
        assertTrue(res.statusCode().is2xxSuccessful());
        assertEquals(HttpStatus.OK, res.statusCode());
        MockHttpServletResponse mockHttpServletResponse = tuple.response;
        var context = tuple.context;
        res.writeTo(mockHttpServletRequest, mockHttpServletResponse, context);
        var s = mockHttpServletResponse.getContentAsString();
        LOGGER.info("res={}", s);
        assertEquals("{\"id\":45,\"nom\":null,\"description\":null}", s);
        assertThat(json.from(s))
                .hasJsonPath("$.id")
                .hasJsonPathValue("$.id")
                .extractingJsonPathValue("$.id")
                .isEqualTo((int) id);
    }

    @Test
    void getProduits() throws Exception {

        // ARRANGE
        long id = 45;
        Produit produit = new Produit();
        produit.setId(id);
        when(produitService.list()).thenReturn(getListProduit(10));
        var tuple = getServerRequest("/produit2", -1);

        var mockHttpServletRequest = tuple.request;
        ServerRequest request2 = tuple.serverRequest;

        // ACT
        var res = produitHandler.getProduits(request2);

        // ASSERT
        assertNotNull(res);
        assertTrue(res.statusCode().is2xxSuccessful());
        assertEquals(HttpStatus.OK, res.statusCode());
        MockHttpServletResponse mockHttpServletResponse = tuple.response;
        var context = tuple.context;
        res.writeTo(mockHttpServletRequest, mockHttpServletResponse, context);
        var s = mockHttpServletResponse.getContentAsString();
        LOGGER.info("res={}", s);
        var jsonObject = json.from(s);
        assertThat(jsonObject).extractingJsonPathValue("$[0].id").isEqualTo((int) 1);
        assertThat(jsonObject).extractingJsonPathValue("$[1].id").isEqualTo((int) 2);
    }

    // méthodes privés

    private static TupleServer getServerRequest(String url, long idProduit) {
        var mockServletContext = new MockServletContext();
        List<HttpMessageConverter<?>> listeMessageConverter =
                List.of(new MappingJackson2HttpMessageConverter());
        MockHttpServletRequestBuilder tmp2 = MockMvcRequestBuilders.get(url, idProduit);
        MockHttpServletRequest mockHttpServletRequest = tmp2.buildRequest(mockServletContext);
        Map<String, Object> map = Map.of("produitId", "" + idProduit);
        mockHttpServletRequest.setAttribute(RouterFunctions.URI_TEMPLATE_VARIABLES_ATTRIBUTE, map);
        ServerRequest request2 =
                ServerRequest.create(mockHttpServletRequest, listeMessageConverter);
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        var context = mock(ServerResponse.Context.class);
        when(context.messageConverters()).thenReturn(listeMessageConverter);
        return new TupleServer(request2, mockHttpServletRequest, mockHttpServletResponse, context);
    }

    record TupleServer(
            ServerRequest serverRequest,
            MockHttpServletRequest request,
            MockHttpServletResponse response,
            ServerResponse.Context context) {}

    private List<Produit> getListProduit(int nb) {
        List<Produit> liste = new ArrayList<>();
        Produit produit;
        for (int i = 0; i < nb; i++) {
            long no = i + 1;
            produit = new Produit();
            produit.setId(no);
            produit.setNom("Produit " + no);
            produit.setDescription("Description " + no);
            liste.add(produit);
        }
        return List.copyOf(liste);
    }
}
