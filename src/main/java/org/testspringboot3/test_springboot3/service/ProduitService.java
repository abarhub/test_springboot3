package org.testspringboot3.test_springboot3.service;

import org.testspringboot3.test_springboot3.domain.Produit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProduitService {

    private List<Produit> liste;

    public ProduitService() {
        List<Produit> liste=new ArrayList<>();
        Produit produit;
        for(int i=0;i<10;i++) {
            long no=i+1;
            produit = new Produit();
            produit.setId(no);
            produit.setNom("Produit "+no);
            produit.setDescription("Description "+no);
            liste.add(produit);
        }
        this.liste=List.copyOf(liste);
    }

    public List<Produit> list(){
        return List.copyOf(liste);
    }

    public Optional<Produit> findById(long id){
        return liste.stream().filter(x-> Objects.equals(x.getId(),id)).findAny();
    }

}
