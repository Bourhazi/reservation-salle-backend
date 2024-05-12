package org.sid.salle.web;

import org.sid.salle.dao.CategorieRepository;
import org.sid.salle.dao.SalleRepository;
import org.sid.salle.model.Categorie;
import org.sid.salle.model.Salle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SalleRestController {
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private CategorieRepository categorieRepository;

    @PostMapping("/categorie")
    public Categorie savecategorie(@RequestBody Categorie categorie){
        return categorieRepository.save(categorie);
    }

    @GetMapping("/categories")
    public List<Categorie> getAllCategories(){
        return categorieRepository.findAll();
    }

    @GetMapping("/categorie/{id}")
    public ResponseEntity<Categorie> getCategorieById(@PathVariable Long id){
        Categorie categorie = categorieRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("Categorie not Found"));
        return ResponseEntity.ok(categorie);
    }

    @PutMapping("/categorie/{id}")
    public ResponseEntity<Categorie> updateCategorie(@PathVariable Long id, @RequestBody Categorie cat){
        Categorie categorie = categorieRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("Categorie not Found"));
        categorie.setName(cat.getName());
        Categorie categorieUpdate = categorieRepository.save(categorie);
        return ResponseEntity.ok(categorieUpdate);
    }

    @DeleteMapping("/categorie/{id}")
    public ResponseEntity<Map<String,Boolean>> deleteCategorie(@PathVariable Long id){
        Categorie categorie = categorieRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("Categorie not Found"));
        categorieRepository.delete(categorie);
        Map<String,Boolean> responce = new HashMap<>();
        responce.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(responce);
    }

    @PostMapping("/salle")
    public ResponseEntity<Salle> saveSalle(@RequestBody Salle salle) {
        if (salle.getCategorie() != null && salle.getCategorie().getId() != 0) {
            Categorie categorie = categorieRepository.findById(salle.getCategorie().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + salle.getCategorie().getId()));
            salle.setCategorie(categorie);
        }
        Salle savedSalle = salleRepository.save(salle);
        return ResponseEntity.ok(savedSalle);
    }

    @GetMapping("/salle")
    public List<Salle> getAllSalles(){
        return salleRepository.findAll();
    }

    @GetMapping("/salle/{id}")
    public ResponseEntity<Salle> getSalleById(@PathVariable Long id){
        Salle salle = salleRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("Salle not Found"));
        return ResponseEntity.ok(salle);
    }

    @PutMapping("/salle/{id}")
    public ResponseEntity<Salle> updateSalle(@PathVariable Long id, @RequestBody Salle sal){
        Salle salle = salleRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("Salle not Found"));
        salle.setName(sal.getName());
        salle.setPrix(sal.getPrix());
        salle.setCategorie(sal.getCategorie());
        Salle salleUpdate = salleRepository.save(salle);
        return ResponseEntity.ok(salleUpdate);
    }

    @DeleteMapping("/salle/{id}")
    public ResponseEntity<Map<String,Boolean>> deleteSalle(@PathVariable Long id){
        Salle salle = salleRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("Salle not Found"));
        salleRepository.delete(salle);
        Map<String,Boolean> responce = new HashMap<>();
        responce.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(responce);
    }

}
