package com.buscadorpelut.Controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.buscadorpelut.Model.Animal;
import com.buscadorpelut.Service.AnimalService;

@WebMvcTest(AnimalController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AnimalControllerTest {

     @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalService animalService;

    private Animal animal1;
    private Animal animal2;

    @BeforeEach
    void setUp() {
        // Creem dates de prova
        Date dataNeix1 = Date.valueOf("2021-03-15");

        Date dataNeix2 = Date.valueOf("2022-07-22");

        animal1 = new Animal();
        animal1.setNomAn("Dark");
        animal1.setEspecie("Gos");
        animal1.setSexe("M");
        animal1.setDataNeix(dataNeix1); // ✅ dataNeix en lloc d'edat
        animal1.setFotoPerfil("dark.jpg");
        animal1.setEsAdoptat(false);
        animal1.setDescripcio("Gos molt carinyos");
        animal1.setNumXip(null);

        animal2 = new Animal();
        animal2.setNomAn("Pelut");
        animal2.setEspecie("Gat");
        animal2.setSexe("F");
        animal2.setDataNeix(dataNeix2); // ✅ dataNeix en lloc d'edat
        animal2.setFotoPerfil("pelut.jpg");
        animal2.setEsAdoptat(false);
        animal2.setDescripcio("Gata juganera");
    }
    @Test
    void testGetAllAnimals()throws Exception {
        List<Animal> animals = Arrays.asList(animal1, animal2);
        when(animalService.getAllAnimals()).thenReturn(animals);

        mockMvc.perform(get("/api/animals")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nomAn").value("Dark"))
                .andExpect(jsonPath("$[1].especie").value("Gat"));

        verify(animalService, times(1)).getAllAnimals();
    }

    @Test
    void testGetAnimalsByEspecie_Exotic()throws Exception {
        animal2.setEspecie("Conill");
        List<Animal> Exotics = Arrays.asList(animal2);
        when(animalService.getAnimalsExcluirGosGat()).thenReturn(Exotics);

        mockMvc.perform(get("/api/animals")
                .param("especie", "Exòtic")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].especie").value("Conill"));

        verify(animalService, times(1)).getAnimalsExcluirGosGat();
    }

    @Test
    void testGetAnimalsByEspecie_NoExotic()throws Exception {
       
        List<Animal> Gossos = Arrays.asList(animal1);
        when(animalService.getAnimalsByEspecie("Gos")).thenReturn(Gossos);

        mockMvc.perform(get("/api/animals")
                .param("especie", "Gos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].especie").value("Gos"));

        verify(animalService, times(1)).getAnimalsByEspecie("Gos");
    }

    @Test
    void testGetAnimalsByEspecie_NoExistent()throws Exception {
        when(animalService.getAnimalsByEspecie("Peixos")).thenReturn(List.of());

        mockMvc.perform(get("/api/animals")
                .param("especie", "Peixos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        verify(animalService, times(1)).getAnimalsByEspecie("Peixos");
    }


}
