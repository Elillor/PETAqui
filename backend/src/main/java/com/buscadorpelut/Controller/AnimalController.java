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

@RestController
@RequestMapping("/api/animals")
@CrossOrigin(origins="*")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @GetMapping
    public ResponseEntity<List<Animal>>getAllAnimals(){
        List<Animal> animals = animalService.getAllAnimals();
        return ResponseEntity.ok(animals);
    }

    @GetMapping("/{numId}")
    public ResponseEntity<Animal>getAnimalsByNumId(@PathVariable Long numId){
        return animalService.getAnimalsById(numId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

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
