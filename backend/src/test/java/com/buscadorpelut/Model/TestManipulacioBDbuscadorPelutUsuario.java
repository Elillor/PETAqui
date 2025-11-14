package com.buscadorpelut.Model;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import com.buscadorpelut.Repository.UsuarioRepository;

import jakarta.transaction.Transactional;

/*
 * Classe de testeig de les operacions CRUD amb la base de dades BuscadorPelut
 * (Create, Read, Update, Delete) sobre l'entitat Usuario
 *
 * @SpringBootTest - carrega tot el context de spring Boot
 * @Transactional  - fa rollback automàtic després de cada test
 */

@SpringBootTest
@Transactional
public class TestManipulacioBDbuscadorPelutUsuario {

    @Autowired
    UsuarioRepository usuarioRepository;


    @Test
    void testCreateUsuari() { //Test per comprovar l'operació de creació ir egistre d'un usuari a la BD

        //1- Crear - creem un usuari i el guardem
        Usuario usuariTest = new Usuario();
        usuariTest.setnomUs("Gerard");
        usuariTest.setcognom1("Ortiz");
        usuariTest.setcognom2("Garcia");
        usuariTest.setclauPas("88888888");
        usuariTest.setemailUs("gerard@test.com");
        usuariTest.setrol(Rol.ADOPTANT);

        usuarioRepository.save(usuariTest);
        Optional<Usuario> test = usuarioRepository.findByEmailUs("gerard@test.com");

        assertThat(test).isPresent(); //Comprovem que estigui present l'usuari amb l'email cercat
        assertThat(test.get().getnomUs()).isEqualTo("Gerard"); //Comprove que sigui el nom que hem entrat
    }

    @Test
    void testReadUsuari () {//Test per provar la lectura d'un registre

        //1- Creem un usuari dummy per fer les proves
        Usuario usuariTest = new Usuario();
        usuariTest.setnomUs("Marc");
        usuariTest.setcognom1("Torres");
        usuariTest.setcognom2("Garcia");
        usuariTest.setclauPas("99999999");
        usuariTest.setemailUs("marc@test.com");
        usuariTest.setrol(Rol.ADOPTANT);

        usuarioRepository.save(usuariTest);//guardem el registre

        Optional<Usuario> test = usuarioRepository.findByEmailUs("marc@test.com"); //obtenim l'usuari amb l'email buscat

        assertThat(test).isPresent();//Comprovem que estigui present l'usuari
        assertThat(test.get().getnomUs()).isEqualTo("Marc");
    }

    @Test
    void testUpdateUsuari(){// Test per provar el modificar una dada d'un registre

        //1- Creem un usuari dummy per fer les proves
        Usuario usuariTest = new Usuario();
        usuariTest.setnomUs("Marta");
        usuariTest.setcognom1("Tartosa");
        usuariTest.setcognom2("Garcia");
        usuariTest.setclauPas("44444444");
        usuariTest.setemailUs("marta@test.com");
        usuariTest.setrol(Rol.ADOPTANT);

        usuarioRepository.save(usuariTest);//guardem el registre

        //2- Recuperem i modifiquem
        Optional<Usuario> actualitzat = usuarioRepository.findByEmailUs("marta@test.com"); //Guardem optional usuari amb l'email objectiu 
        
        Usuario actualitzatOk = actualitzat.get(); //Obtenim l'objecte usuari de l'optional
        actualitzatOk.setnomUs("Nuria");
        usuarioRepository.save(actualitzatOk);//guardem el canvi

        //3- Recuperem i comprovem el canvi
        Optional<Usuario> canviat = usuarioRepository.findByEmailUs("marta@test.com"); 
        assertThat(canviat).isPresent();
        assertThat(canviat.get().getnomUs()).isEqualTo("Nuria");//Comprovem que el nom ara es el nou: Nuria       
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteUsuari(){ // test per provar d'eliminar un registre

        //1- Creem un usuari dummy per fer les proves
        Usuario usuariTest = new Usuario();
        usuariTest.setnomUs("Carina");
        usuariTest.setcognom1("Emanuel");
        usuariTest.setcognom2("Garcia");
        usuariTest.setclauPas("11111111");
        usuariTest.setemailUs("carina@test.com");
        usuariTest.setrol(Rol.ADOPTANT);

        usuarioRepository.save(usuariTest);//guardem el registre
        
        Optional<Usuario> test = usuarioRepository.findByEmailUs("carina@test.com"); //obtenim l'usuari amb l'email buscat
        assertThat(test).isPresent();//Comprovem que estigui present l'usuari
        
        usuarioRepository.delete(test.get());//Eliminem l'usuari
        usuarioRepository.flush();

        //Tornem a comprovar si hi és
        Optional<Usuario> esborrat = usuarioRepository.findByEmailUs("carina@test.com");
        assertThat(esborrat).isEmpty();//No l'ha de trobar eprque l'hem esborrat
    }
}
