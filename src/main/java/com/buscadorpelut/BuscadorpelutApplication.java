package com.buscadorpelut;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.buscadorpelut.Model.Usuari;
//import com.buscadorpelut.Repository.UsuarioRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

@SpringBootApplication
public class BuscadorpelutApplication {
public static void main(String[] args) {
        SpringApplication.run(BuscadorpelutApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(EntityManagerFactory emf) {
        return args -> {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();

            Usuari usuari = em.find(Usuari.class, 1L); // id = 1
            if (usuari != null) {
                System.out.println("Usuari trobat: " + usuari.getNomUs() + " " + usuari.getCognom1());
            } else {
                System.out.println("Usuari amb id=1 no trobat");
            }

            em.getTransaction().commit();
            em.close();
        };
    }

}
