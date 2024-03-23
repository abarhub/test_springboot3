package org.testspringboot3.test_springboot3.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import org.testspringboot3.test_springboot3.handler.ProduitHandler;
import org.testspringboot3.test_springboot3.service.ProduitService;

import static org.springframework.web.servlet.function.RequestPredicates.accept;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class WebConfiguration {

    private static final RequestPredicate ACCEPT_JSON = accept(MediaType.APPLICATION_JSON);


    @Bean
    public RouterFunction<ServerResponse> routerFunction(ProduitHandler produitHandler) {
        return route()
                .GET("/produit2/{produitId}", ACCEPT_JSON, produitHandler::getProduit)
                .GET("/produit2", ACCEPT_JSON, produitHandler::getProduits)
//                .DELETE("/{user}", ACCEPT_JSON, userHandler::deleteUser)
                .build();
    }

    @Bean
    public ProduitHandler produitHandler(ProduitService produitService){
        return new ProduitHandler(produitService);
    }

}
