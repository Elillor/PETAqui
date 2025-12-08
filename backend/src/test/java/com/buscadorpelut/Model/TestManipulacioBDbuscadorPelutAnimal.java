package com.buscadorpelut.Model;

import static org.assertj.core.api.Assertions.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.buscadorpelut.Repository.AnimalRepository;
import com.buscadorpelut.Repository.ProtectoraRepository;

import jakarta.transaction.Transactional;

/*
 * Classe de testeig de les operacions CRUD amb la base de dades BuscadorPelut
 * (Create, Read, Update, Delete) sobre l'entitat Animal
 *
 * @SpringBootTest - carrega tot el context de spring Boot
 * @Transactional  - fa rollback automàtic després de cada test
 */

@SpringBootTest
@Transactional

public class TestManipulacioBDbuscadorPelutAnimal {
    
    @Autowired
    AnimalRepository animalRepository;

    @Autowired
    ProtectoraRepository protectoraRepository;

    @Test
    void testCreateAndReadAnimal() { //Test per comprovar l'operació de creació i de lectura de registre d'un animal a la BD
        //0- Preparació dada protectora (clau forana)
        Protectora protectoraTest = new Protectora();
        protectoraTest.setNomProt("Protectora Test");
        protectoraTest.setLatitud(99999999);
        protectoraTest.setLongitud(99999999);

        protectoraRepository.save(protectoraTest); //guardem la protectora per evitar errors de clau forana
        
        //1- Crear - creem un usuari i el guardem
        Animal animalTest = new Animal();
        animalTest.setNomAn("Brownie");
        animalTest.setSexe("mascle");
        animalTest.setEspecie("cobaya");
        animalTest.setDescripcio("En brownie es una cobaya molt joveneta i poruga. Necessita una familia respectuosa i que en respecti els temps");
        animalTest.setDataNeix(Date.valueOf(LocalDate.of(2024, 1, 1)));
        animalTest.setNumXip("null");
        animalTest.setEsAdoptat(false);
        animalTest.setFotoPerfil("Brownie.jpg");
        animalTest.setProtectora(protectoraTest);

        animalRepository.save(animalTest); //guardem l'animal

        //2- Read - recuperem l'animal per comprovar que s'ha creat correctament
        List<Animal> cobaies = animalRepository.findByEspecie("cobaya");
        
        //3- Comprovem que l'animal creat està a la llista d'animals recuperats
        Animal trobat = getAnimalByNom(cobaies, "Brownie");
        assertThat(trobat).isNotNull(); // Comprovem que l'animal "Brownie" està a la llista

    }

    @Test
    void testUpdateAnimal(){// Test per provar el modificar una dada d'un registre
        //0- Preparació dada protectora (clau forana)
        Protectora protectoraTest = new Protectora();
        protectoraTest.setNomProt("Protectora Test");
        protectoraTest.setLatitud(99999999);
        protectoraTest.setLongitud(99999999);

        protectoraRepository.save(protectoraTest); //guardem la protectora per evitar errors de clau forana

        //1- Creem un animal dummy per fer les proves
        Animal animalTest = new Animal();
        animalTest.setNomAn("Morena");
        animalTest.setSexe("femella esterilitzada");
        animalTest.setEspecie("gos");
        animalTest.setDescripcio("La Morena és una gosseta molt activa i juganera. Li encanta córrer i jugar amb altres gossos.");
        animalTest.setDataNeix(Date.valueOf(LocalDate.of(2022,9, 14)));
        animalTest.setNumXip("null");
        animalTest.setEsAdoptat(false);
        animalTest.setFotoPerfil("Morena.jpg");
        animalTest.setProtectora(protectoraTest);

        animalRepository.save(animalTest);//guardem el registre

        //2- Recuperem l'animal creat de la llista de gossos
        List<Animal> gossos = animalRepository.findByEspecie("gos"); //Guardem optional animal amb el nom objectiu 
        
        Animal actualitzat = getAnimalByNom(gossos, "Morena");

        //3- comrpovem que hem trobat l'animal
        assertThat(actualitzat).isNotNull();

        //4- Modifiquem el nom de l'animal i el guardem
        actualitzat.setNomAn("Dorothy");
        animalRepository.save(actualitzat);//guardem el canvi

        // 5- Recuperem la llista de gossos de nou i comprovem que el nom ha canviat
        List<Animal> gossosActualitzats = animalRepository.findByEspecie("gos");
        Animal actualitzat2 = getAnimalByNom(gossosActualitzats, "Dorothy");

            assertThat(actualitzat2).isNotNull(); // Comprovem que el nom ara és Dorothy   
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteAnimal(){ // test per provar d'eliminar un registre
        //0- Preparació dada protectora (clau forana)
        Protectora protectoraTest = new Protectora();
        protectoraTest.setNomProt("Protectora Test");
        protectoraTest.setLatitud(99999999);
        protectoraTest.setLongitud(99999999);

        protectoraRepository.save(protectoraTest); //guardem la protectora per evitar errors de clau forana

        //1- Creem un usuari dummy per fer les proves
        Animal animalTest = new Animal();
        animalTest.setNomAn("Hilda");
        animalTest.setSexe("femella");
        animalTest.setEspecie("rata");
        animalTest.setDescripcio("La Hilda és una rata molt activa i juganera.");
        animalTest.setDataNeix(Date.valueOf(LocalDate.of(2023,7, 7)));
        animalTest.setNumXip("null");
        animalTest.setEsAdoptat(false);
        animalTest.setFotoPerfil("Hilda.jpg");
        animalTest.setProtectora(protectoraTest);

        animalRepository.save(animalTest);//guardem el registre
        
        List<Animal> rates = animalRepository.findByEspecie("rata"); //obtenim l'animal amb el nom buscat
        Animal aEliminar = getAnimalByNom(rates, "Hilda");
        
        //2- Eliminem l'animal creat
        animalRepository.delete(aEliminar);//Eliminem l'animal
        animalRepository.flush();

        //3-Tornem a comprovar si hi és
        List<Animal> ratesEsborrat = animalRepository.findByEspecie("rata");
        Animal esborrat = getAnimalByNom(ratesEsborrat, "Hilda");
        assertThat(esborrat).isNull();//No l'ha de trobar perquè l'hem esborrat
    }


    /**
    * Retorna l'animal amb el nom especificat dins de la llista passada.
    * @param animals Llista d'animals (ja filtrada prèviament per espècie si cal)
    * @param nom Nom de l'animal que volem trobar
    * @return L'animal trobat, o null si no existeix
    */
    public Animal getAnimalByNom(List<Animal> animals, String nom) {
        for (Animal animal : animals) {
            if (animal.getNomAn().equals(nom)) {
                return animal;
            }
        }
        return null; // Retorna null si no es troba cap animal amb el nom donat
    }
}
