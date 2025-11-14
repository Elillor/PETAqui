package com.buscadorpelut.Controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buscadorpelut.Model.Animal;
import com.buscadorpelut.Service.AnimalService;

/**
 *  Controlador REST per gestionar les operacions sobre els animals disponibles per a adopció.
 * 
 * <p>Aquesta classe proporciona endpoints per:
 * <ul>
 *   <li>Llistar tots els animals</li>
 *   <li>Filtrar animals per espècie (incloent l'opció "Exòtic" per a espècies no convencionals)</li>
 * </ul>
 * 
 * <p>Tots els endpoints accedeixen des de {@code /api/animals} i admeten peticions CORS
 * per facilitar la integració amb frontends externs.
 * 
 * @author Luis Gil
 */
@RestController
@RequestMapping("/api/animals")
@CrossOrigin(origins="*")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    /**
     * Mètode per retornar tots els animals registrats al sistema.
     * 
     * @return {@link ResponseEntity} amb una llista d'objectes {@link Animal} i codi HTTP 200 (OK).
     * 
     * @see AnimalService#getAllAnimals()
     */
    @GetMapping
    public ResponseEntity<List<Animal>>getAllAnimals(){
        List<Animal> animals = animalService.getAllAnimals();
        return ResponseEntity.ok(animals);
    }

    @GetMapping("{numId}")
    public ResponseEntity<Animal>getAnimalsByNumId(@PathVariable Long numId){
        return animalService.getAnimalsById(numId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Mètode per retornar una llista d'animals filtrats per espècie.
     * 
     * <p>Si el paràmetre {@code especie} és "Exòtic", es retornen tots els animals
     * que no pertanyen a les espècies "Gos" ni "Gat".
     * 
     * <p>Exemples d'ús:
     * <ul>
     *   <li>{@code GET /api/animals?especie=Gos} → només gossos</li>
     *   <li>{@code GET /api/animals?especie=Exòtic} → conills, aus, rèptils, etc.</li>
     * </ul>
     * 
     * @param especie el nom de l'espècie a filtrar (ex: "Gos", "Gat", "Exòtic").
     * @return {@link ResponseEntity} amb una llista d'objectes {@link Animal} i codi HTTP 200 (OK).
     *         Pot retornar una llista buida si no hi ha animals d'aquella espècie.
     * 
     * @see AnimalService#getAnimalsByEspecie(String)
     * @see AnimalService#getAnimalsExcluirGosGat()
     */
    @GetMapping(params = "especie")
    public ResponseEntity<List<Animal>>getAnimalsByEspecie(@RequestParam String especie){
        List<Animal>animals;
        if("Exòtic".equals(especie)){
            animals=animalService.getAnimalsExcluirGosGat();
        }else{
            animals=animalService.getAnimalsByEspecie(especie);
        }
        return ResponseEntity.ok(animals);
    }
}
