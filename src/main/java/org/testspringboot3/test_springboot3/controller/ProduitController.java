package org.testspringboot3.test_springboot3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.testspringboot3.test_springboot3.domain.Produit;
import org.testspringboot3.test_springboot3.service.ProduitService;

import java.util.Optional;

@RestController
@RequestMapping("/produits")
public class ProduitController {

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping("/{produitId}")
    public Optional<Produit> getUser(@PathVariable Long produitId) {
        return this.produitService.findById(produitId);
    }
}
