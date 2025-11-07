package com.buscadorpelut.Model;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.buscadorpelut.Repository.UsuarioRepository;

import jakarta.transaction.Transactional;

/*
 * Classe per provar la connexió a la BD real buscador pelut
 * Fem servir els tests d'SpringBoottest, pel qual ja ens inicialitza l'EntityManagerFactory, 
 * obre la connexió al començar el test
 * Crea o neteja la bd de test i tanca automàticament la connexió després de cada test. 
 * 
 * Aixó ens permet no haver de fer @BeforeAll ni @AfterAll per obrir i tancar connexió.
 * 
 * @SpringBootTest - carrega tot el context de spring Boot
 * @Transactional  - fa rollback automàtic després de cada test
 */

@SpringBootTest 
@Transactional 
public class TestConnectionBDbuscadorPelut {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testConnexioBaseDeDades(){

        //1- comprovem que el repositori s'ha injectat correctament
        assertThat(usuarioRepository).isNotNull();

        //2- Comprovem el número de registres a la bd abans del test i guardem a una variable
        long numInicial = usuarioRepository.count();
        System.out.println("Registres inicials: " + numInicial);

        //3- Provem a crear un usuari de prova a la bd
        Usuario usuariTest = new Usuario();
        usuariTest.setnomUs("Carla");
        usuariTest.setcognom1("Figueres");
        usuariTest.setcognom2("Garcia");
        usuariTest.setclauPas("44444444");
        usuariTest.setemailUs("carla@test.com");
        usuariTest.setrol(Rol.ADOPTANT);

        //4- Es guarda a la bd per pdoer-lo recuperar després
        usuarioRepository.save(usuariTest);

        //5- Comprovació de que s'ha guardat- comparem valor inicial amb el nou count
        long numDespres = usuarioRepository.count();
        System.out.println("Registres després del test: " + numDespres);

        assertTrue(numDespres > numInicial);//El número de després ha d'haver augmentat després del test

        //6- Comprovem que el podem recuperar de la bd
        Optional<Usuario> usuariC = usuarioRepository.findByEmailUs("carla@test.com");

        assertThat(usuariC).isPresent();
        
        assertThat(usuariC.get().getnomUs()).isEqualTo("Carla");
        assertThat(usuariC.get().getemailUs()).isEqualTo("carla@test.com");
    }


}
