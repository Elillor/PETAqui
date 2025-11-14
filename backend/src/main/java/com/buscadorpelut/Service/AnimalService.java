package com.buscadorpelut.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buscadorpelut.Model.Animal;
import com.buscadorpelut.Repository.AnimalRepository;

/**
 * Servei per gestionar la lògica de negoci relacionada amb els animals.
 * 
 * <p>Aquesta classe actua com a intermediària entre el controlador REST i el repositori,
 * mostrant informació dels animals de diverses formes 
 * (total, per espècie, etc.).
 * 
 * <p>Totes les operacions deleguen en {@link AnimalRepository} per a l'accés a dades.
 * 
 * @author Luis Gil
 */
@Service
public class AnimalService {

  @Autowired
  private AnimalRepository animalRepository;

  /**
     * Recupera una llista completa de tots els animals registrats al sistema.
     * 
     * @return una llista (segons implementació del repositori) d'objectes {@link Animal}.
     *         Pot estar buida si no hi ha cap animal a la base de dades.
     * 
     * @see AnimalRepository#findAll()
     */
  public List<Animal>getAllAnimals(){
    return animalRepository.findAll();
  }

  public Optional<Animal>getAnimalsById(Long id){
    if (id == null) {
        return Optional.empty();
    }
    return animalRepository.findById(id);
  }

  /**
     * Mostra tots els animals que pertanyen a una espècie específica.
     * 
     * <p>La comparació és sensible a majúscules/minúscules i accents. 
     * Per exemple, "gos" ≠ "Gos" ≠ "Gòs".
     * 
     * @param especie el nom exacte de l'espècie a cercar (ex: "Gos", "Gat", "Conill").
     * @return una llista d'animals d'aquella espècie. Pot estar buida si no n'hi ha cap.
     * 
     * @see AnimalRepository#findByEspecie(String)
     */
  public List<Animal>getAnimalsByEspecie(String especie){
    return animalRepository.findByEspecie(especie);
  }

  /**
     * Retorna tots els animals que **no pertanyen** a les espècies "Gos" ni "Gat".
     * 
     * <p>Aquest mètode s'utilitza per implementar la categoria "Exòtic" al frontend,
     * incloent espècies com conills, aus, rèptils, cobaias, etc.
     * 
     * @return una llista d'animals d'espècies diferents a "Gos" i "Gat". 
     *         Pot estar buida si només hi ha gossos i gats al sistema.
     * 
     * @see AnimalRepository#findByEspecieNotIn(List)
     */
  public List<Animal>getAnimalsExcluirGosGat(){
    return animalRepository.findByEspecieNotIn(Arrays.asList("Gos","Gat"));
  }
}
