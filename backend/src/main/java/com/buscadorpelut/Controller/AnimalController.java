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
     * Mètode per retornar tots els animals registrats al sistema en situació d'adopció.
     * 
     * @return {@link ResponseEntity} amb una llista d'animals {@link Animal} no adoptats i codi HTTP 200 (OK).
     * 
     * @see AnimalService#getAllAnimals()
     */
    @GetMapping
    public ResponseEntity<List<Animal>>getAllAnimals(){
        List<Animal> animals = animalService.getAllAnimalsNoAdoptats();
        return ResponseEntity.ok(animals);
    }

    /**
     * Mètode per retornar tots els animals registrats al sistema que ja han estat adoptats.
     * 
     * @return {@link ResponseEntity} amb una llista d'animals {@link Animal} adoptats i codi HTTP 200 (OK).
     * 
     * @see AnimalService#getAllAnimalsAdoptats()
     */
    @GetMapping("/adoptats")
    public ResponseEntity<List<Animal>>getAllAnimalsAdoptats(){
        List<Animal> animals = animalService.getAllAnimalsAdoptats();
        return ResponseEntity.ok(animals);
    }
    /**
     * Retorna els detalls d'un animal identificat pel seu ID numèric.
     * 
     * @param numId l'identificador únic de l'animal el qual es mostraran els detalls.
     * @return {@link ResponseEntity} amb l'objecte {@link Animal} si es troba (codi 200 OK),
     *         o codi HTTP 404 (Not Found) si no existeix cap animal amb aquest ID.
     * 
     * @see AnimalService#getAnimalsById(Long)
     */
    @GetMapping("{numId}")
    public ResponseEntity<Animal>getAnimalsByNumId(@PathVariable Long numId){
        return animalService.getAnimalByIdWithProtectora(numId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Mètode per retornar una llista d'animals filtrats per espècie i que no estan adoptats.
     * 
     * <p>Si el paràmetre {@code especie} és "Exòtic", es retornen tots els animals
     * que no pertanyen a les espècies "Gos" ni "Gat" en situació d'adopció.
     * 
     * <p>Exemples d'ús:
     * <ul>
     *   <li>{@code GET /api/animals?especie=Gos} → només gossos</li>
     *   <li>{@code GET /api/animals?especie=Exòtic} → conills, aus, rèptils, etc.</li>
     * </ul>
     * 
     * @param especie el nom de l'espècie a filtrar (ex: "Gos", "Gat", "Exòtic").
     * @return {@link ResponseEntity} amb una llista d'animals {@link Animal} en situació d'adopció i codi HTTP 200 (OK).
     *         Pot retornar una llista buida si no hi ha animals d'aquella espècie.
     * 
     * @see AnimalService#getAnimalsByEspecie(String)
     * @see AnimalService#getAnimalsExcluirGosGat()
     */
    @GetMapping(params = "especie")
    public ResponseEntity<List<Animal>>getAnimalsByEspecie(@RequestParam String especie){
        List<Animal>animals;
        if("Exòtic".equals(especie)){
            animals=animalService.getAnimalsExcluirGosGatNoAdoptats();
        }else{
            animals=animalService.getAnimalsByEspecieNoAdoptats(especie);
        }
        return ResponseEntity.ok(animals);
    }

    /**
     * Mètode per retornar una llista d'animals filtrats per espècie i localització, excloent els ja adoptats.
     * 
     * @param especie el nom de l'espècie a filtrar (ex: "Gos", "Gat", "Exòtic").
     * @param localitzacio la localització geogràfica per filtrar els animals (ex: ciutat o codi postal).
     * @return {@link ResponseEntity} amb una llista d'animals {@link Animal} en situació d'adopció i codi HTTP 200 (OK).
     *         Pot retornar una llista buida si no hi ha animals que compleixin els criteris.
     * @see AnimalService#getAnimalsByEspecieAndLocalitzacio(String, String)
     */
    @GetMapping(params = {"especie", "localitzacio"})
    public ResponseEntity<List<Animal>>getAnimalsByEspecieAndLocalitzacio(
        @RequestParam String especie,
        @RequestParam String localitzacio){
        List<Animal>animals;
        if("Exòtic".equals(especie)){
            animals=animalService.getAnimalsExcluirGosGatNoAdoptatsByLocalitzacio(localitzacio);
        }else{
            animals=animalService.getAnimalsByEspecieNoAdoptatsByLocalitzacio(especie, localitzacio);
        }
        return ResponseEntity.ok(animals);
    }

    /**
     * Mètode per retornar una llista d'animals filtrats per localització i que no estan adoptats.
     * 
     * @param localitzacio la localització geogràfica per filtrar els animals (ex: ciutat o codi postal).
     * @return {@link ResponseEntity} amb una llista d'animals {@link Animal} en situació d'adopció i codi HTTP 200 (OK).
     *         Pot retornar una llista buida si no hi ha animals en aquella localització.
     * @see AnimalService#getAnimalsNoAdoptatsByLocalitzacio(String)
     */
    @GetMapping(params = "localitzacio")
    public ResponseEntity<List<Animal>>getAnimalsByLocalitzacio(
        @RequestParam String localitzacio){   
        return ResponseEntity.ok(animalService.getAnimalsNoAdoptatsByLocalitzacio(localitzacio));
    }


}
