package org.testspringboot3.test_springboot3.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import org.testspringboot3.test_springboot3.service.ProduitService;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class ServiceConfiguration {

    @Bean
    public ProduitService produitService() {
        return new ProduitService();
    }

}
