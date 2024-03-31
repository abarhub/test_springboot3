package org.testspringboot3.test_springboot3.handler;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import org.testspringboot3.test_springboot3.service.ProduitService;

@Slf4j
public class ProduitHandler {

    private final ProduitService produitService;

    public ProduitHandler(ProduitService produitService) {
        this.produitService = produitService;
    }

    public ServerResponse getProduit(ServerRequest request) throws IOException {
        int id = 0;
        try {
            id = Integer.parseInt(request.pathVariable("produitId"));
        } catch (NumberFormatException e) {
            log.error("Erreur", e);
        }
        log.atInfo().log("get produit {}", id);
        if (id > 0) {
            var produitOpt = produitService.findById(id);
            if (produitOpt.isPresent()) {
                log.atInfo().log("get produit");
                return ServerResponse.ok().body(produitOpt.get());
            }
        }
        return ServerResponse.ok().build();
    }

    public ServerResponse getProduits(ServerRequest request) {
        log.atInfo().log("get all produit");
        return ServerResponse.ok().body(produitService.list());
    }
}
