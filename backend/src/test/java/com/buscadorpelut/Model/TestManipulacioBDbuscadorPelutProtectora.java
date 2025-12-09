package com.buscadorpelut.Model;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

import com.buscadorpelut.Repository.ProtectoraRepository; 
import jakarta.transaction.Transactional;

/*
 * Classe de testeig de les operacions CRUD amb la base de dades BuscadorPelut
 * (Create, Read, Update, Delete) sobre l'entitat Protectora
 *
 * @SpringBootTest - carrega tot el context de spring Boot
 * @Transactional  - fa rollback automàtic després de cada test
 */
@SpringBootTest
@Transactional
public class TestManipulacioBDbuscadorPelutProtectora {

    @Autowired
    ProtectoraRepository protectoraRepository;

    @Test
    void testCreateAndReadProtectora() { //Test per comprovar l'operació de creació i de lectura de registre d'una protectora a la BD

        //1- Crear - creem un usuari i el guardem
        Protectora protectoraTest = new Protectora();
        protectoraTest.setNomProt("La granja de la tieta Maria");
        protectoraTest.setAdresa("Carrer de les Rates, 5");
        protectoraTest.setCodiPostal("04756");
        protectoraTest.setLocalitat("Inventat");
        protectoraTest.setProvincia("Província Imaginària");
        protectoraTest.setUrl("www.mariasfarm.com");
        protectoraTest.setLongitud(2.34567);
        protectoraTest.setLatitud(4.56789);
        protectoraTest.setTlfProt("987654321");
        protectoraTest.setEmailProt("maria@farm.com");

        protectoraRepository.save(protectoraTest); //guardem la protectora

        //2- Read - recuperem la protectora per comprovar que s'ha creat correctament
        Optional<Protectora> protectoraTestRecuperada = protectoraRepository.findProtectoraByNomProt("La granja de la tieta Maria");
        
        //3- Comprovem que la protectora creada està a la llista de protectores recuperats
        assertThat(protectoraTestRecuperada).isPresent();

        Protectora protectoraRecuperada = protectoraTestRecuperada.get();
        assertThat(protectoraRecuperada.getNomProt()).isEqualTo("La granja de la tieta Maria"); // Comprovem que la protectora "La granja de la tieta Maria" està a la llista
    }

    @Test
    void testUpdateProtectora(){// Test per provar el modificar una dada d'un registre

        //1- Creem una protectora dummy per fer les proves
        Protectora protectoraTest = new Protectora();
        protectoraTest.setNomProt("Chicken run");
        protectoraTest.setAdresa("Carrer de la font, 5");
        protectoraTest.setCodiPostal("00574");
        protectoraTest.setLocalitat("Mes enllà");
        protectoraTest.setProvincia("Província Imaginària");
        protectoraTest.setUrl("www.chickenrun.com");
        protectoraTest.setLongitud(2.34567);
        protectoraTest.setLatitud(4.56789);
        protectoraTest.setTlfProt("987654321");
        protectoraTest.setEmailProt("contact@chickenrun.com");

        protectoraRepository.save(protectoraTest);//guardem el registre

        //2- Recuperem la protectora creada de la llista de protectores
        Optional<Protectora> protectoraTestRecuperada = protectoraRepository.findProtectoraByNomProt("Chicken run"); //Guardem optional protectora amb el nom objectiu 
               

        //3- Comprovem que hem trobat la protectora
        assertThat(protectoraTestRecuperada).isPresent();

        //3.1 Comprovem quina id té la protectora que hem creat
        Protectora testProtectora = protectoraTestRecuperada.get();
        Long idProtectora = testProtectora.getCodiProt();

        //4- Modifiquem el nom de la protectora i el guardem
        testProtectora.setNomProt("Dorothy's animals");
        protectoraRepository.save(testProtectora);//guardem el canvi

        // 5- Recuperem la protectora de nou i comprovem que el nom ha canviat
        Optional<Protectora> protectoraUpdated = protectoraRepository.findProtectoraByNomProt("Dorothy's animals");
        assertThat(protectoraUpdated).isPresent(); //Coprovem que la protectora està present
                
        Protectora actualitzat2 = protectoraUpdated.get();

        assertThat(actualitzat2).isNotNull();
        assertThat(protectoraUpdated.get().getCodiProt()).isEqualTo(idProtectora); // Comprovem que és la mateixa protectora per l'id
        assertThat(actualitzat2.getNomProt().equals("Dorothy's animals")); // Comprovem que el nom ara és Dorothy's animals
    }

    @Test
    void testDeleteProtectora(){ // test per provar d'eliminar un registre

        //1- Creem un usuari dummy per fer les proves
        Protectora protectoraTest = new Protectora();
        protectoraTest.setNomProt("Zootropolis");
        protectoraTest.setAdresa("Carrer de les Rates, 5");
        protectoraTest.setCodiPostal("04756");
        protectoraTest.setLocalitat("Inventat");
        protectoraTest.setProvincia("Província Imaginària");
        protectoraTest.setUrl("www.protectoraZootropolis.com");
        protectoraTest.setLongitud(2.34567);
        protectoraTest.setLatitud(4.56789);
        protectoraTest.setTlfProt("987654321");
        protectoraTest.setEmailProt("contacte@protectoraZootropolis.com");

        protectoraRepository.save(protectoraTest);//guardem el registre
        
        Optional<Protectora> protectoraTestRecuperada = protectoraRepository.findProtectoraByNomProt("Zootropolis"); //obtenim la protectora amb el nom buscat
        Protectora aEliminar = protectoraTestRecuperada.get();
        
        //2- Eliminem la protectora creada
        protectoraRepository.delete(aEliminar);//Eliminem la protectora
        protectoraRepository.flush();

        //3-Tornem a comprovar si hi és
        Optional<Protectora> protectoraEsborrada = protectoraRepository.findProtectoraByNomProt("Zootropolis");
        assertThat(protectoraEsborrada).isNotPresent();//No l'ha de trobar perque l'hem esborrat
    }

}
