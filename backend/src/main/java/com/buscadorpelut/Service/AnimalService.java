package com.buscadorpelut.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buscadorpelut.Model.Animal;
import com.buscadorpelut.Repository.AnimalRepository;

@Service
public class AnimalService {

  @Autowired
  private AnimalRepository animalRepository;

  public List<Animal>getAllAnimals(){
    return animalRepository.findAll();
  }

  public Optional<Animal>getAnimalsById(Long id){
    if (id == null) {
        return Optional.empty();
    }
    return animalRepository.findById(id);
  }

  public List<Animal>getAnimalsByEspecie(String especie){
    return animalRepository.findByEspecie(especie);
  }

  public List<Animal>getAnimalsExcluirGosGat(){
    return animalRepository.findByEspecieNotIn(Arrays.asList("Gos","Gat"));
  }
}
