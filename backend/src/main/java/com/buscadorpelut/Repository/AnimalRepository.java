package com.buscadorpelut.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buscadorpelut.Model.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal,Long>{

    List<Animal> findByEspecie(String especie);
    List<Animal> findByEspecieNotIn(List<String>especies);
}
