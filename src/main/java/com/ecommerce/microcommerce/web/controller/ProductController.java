package com.ecommerce.microcommerce.web.controller;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.dao.ProductDao;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

// @RestController = @Controller + @ResponseBody 
// cette annotation indique que cette classe va pouvoir traiter les requêtes définies
// indique aussi que chaque méthode va renvoyer directement la réponse JSON à l'utilisateur

@RestController
public class ProductController {

    private final ProductDao productDao;

    public ProductController(ProductDao productDao) {
	this.productDao = productDao;
    }

    // Récupérer la liste des produits
    @GetMapping("/Produits")
    public MappingJacksonValue listeProduits() {
	List<Product> produits = productDao.findAll();
	SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
	FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
	MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);
	produitsFiltres.setFilters(listDeNosFiltres);
	return produitsFiltres;
    }

    @GetMapping(value = "/Produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) {
	return productDao.findById(id);
    }

    @GetMapping(value = "/test/produits/{prixLimit}")
    public List<Product> testeDeRequetes(@PathVariable int prixLimit) {
	return productDao.findByPrixGreaterThan(prixLimit);
    }
    

    @PostMapping(value = "/Produits")
    public ResponseEntity<Product> ajouterProduit(@RequestBody Product product) {
	Product productAdded = productDao.save(product);
	if (Objects.isNull(productAdded)) {
	    return ResponseEntity.noContent().build();
	}
	URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
		.buildAndExpand(productAdded.getId()).toUri();
	return ResponseEntity.created(location).build();
    }

    @DeleteMapping(value = "/Produits/{id}")
    public void supprimerProduit(@PathVariable int id) {
	productDao.deleteById(id);
    }
    
    @PutMapping (value = "/Produits")
    public void updateProduit(@RequestBody Product product)
    {
       productDao.save(product);
    }
    

}
