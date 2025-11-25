package com.buscadorpelut.Model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.buscadorpelut.Repository.UsuarioRepository;

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
    
    /*@Autowired
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
       

        protectoraRepository.save(protectoraTest); //guardem la protectora

        //2- Read - recuperem la protectora per comprovar que s'ha creat correctament
        List<Protectora> protectores = protectoraRepository.findByNomProt("La granja de la tieta Maria");
        
        //3- Comprovem que la protectora creada està a la llista de protectores recuperats
        Protectora trobat = getProtectoraByNom(protectores, "La granja de la tieta Maria");
        assertThat(trobat).isNotNull(); // Comprovem que la protectora "La granja de la tieta Maria" està a la llista

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
        List<Protectora> protectores = protectoraRepository.findByNomProt("Chicken run"); //Guardem optional protectora amb el nom objectiu 
        
        Protectora actualitzat = getProtectoraByNom(protectores, "Chicken run");

        //3- Comprovem que hem trobat la protectora
        assertThat(actualitzat).isNotNull();

        //4- Modifiquem el nom de la protectora i el guardem
        actualitzat.setNomProt("Dorothy's animals");
        protectoraRepository.save(actualitzat);//guardem el canvi

        // 5- Recuperem la llista de protectores de nou i comprovem que el nom ha canviat
        List<Protectora> protectoresActualitzats = protectoraRepository.findByNomProt("Dorothy's animals");
        Protectora actualitzat2 = getProtectoraByNom(protectoresActualitzats, "Dorothy's animals");

        assertThat(actualitzat2).isNotNull(); // Comprovem que el nom ara és Dorothy's animals     
    }

    @SuppressWarnings("null")
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
        
        List<Protectora> protectores = protectoraRepository.findByNom("Zootropolis"); //obtenim la protectora amb el nom buscat
        Protectora aEliminar = getProtectoraByNom(protectores, "Zootropolis");
        
        //2- Eliminem la protectora creada
        protectoraRepository.delete(aEliminar);//Eliminem la protectora
        protectoraRepository.flush();

        //3-Tornem a comprovar si hi és
        List<Protectora> protectoresEsborrat = protectoraRepository.findByNom("Zootropolis");
        Protectora esborrat = getProtectoraByNom(protectoresEsborrat, "Zootropolis");
        assertThat(esborrat).isNull();//No l'ha de trobar perquè l'hem esborrat
    }


    /**
    * Retorna la protectora amb el nom especificat dins de la llista passada.
    * @param protectores llista de protectores (ja filtrada prèviament per espècie si cal)
    * @param nom Nom de la protectora que volem trobar
    * @return La protectora trobada, o null si no existeix
    */
    /*public Protectora getProtectoraByNom(List<Protectora> protectores, String nom) {
        for (Protectora protectora : protectores) {
            if (protectora.getNomProt().equals(nom)) {
                return protectora;
            }
        }
        return null; // Retorna null si no es troba cap animal amb el nom donat
    }*/


}
