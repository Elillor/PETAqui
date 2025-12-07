package com.buscadorpelut.Controller;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.sql.Date;

import java.util.List;
import java.util.Optional;

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

    @SuppressWarnings("removal")
    @MockBean
    private AnimalService animalService;

    private Animal animal1;
    private Animal animal2;

    @BeforeEach
    void setUp() {
        // Creem dates de prova
        Date dataNeix1 = Date.valueOf("2021-03-15");

        Date dataNeix2 = Date.valueOf("2022-07-22");

        animal1 = new Animal(
        1L,
        "Dark",
        "M",
        dataNeix1,
        "Gos molt carinyos",
        null,
        "Gos",
        false,
        "data.jpg",
        null
        );

        animal2 = new Animal(
            2L,
            "Pelut",
            "F",
            dataNeix2,
            "Gata juganera",
            null,
            "Gat",
            false, 
            "pelut.jpg",
            null
        
        );
        
    }
    @Test
    void testGetAllAnimals()throws Exception {
        List<Animal> animals = Arrays.asList(animal1, animal2);
        when(animalService.getAllAnimalsNoAdoptats()).thenReturn(animals);

        mockMvc.perform(get("/api/animals")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nomAn").value("Dark"))
                .andExpect(jsonPath("$[1].especie").value("Gat"));

        verify(animalService, times(1)).getAllAnimalsNoAdoptats();
    }

    @Test
    void testGetAnimalsByEspecie_Exotic()throws Exception {
        animal2.setEspecie("Conill");
        List<Animal> Exotics = Arrays.asList(animal2);
        when(animalService.getAnimalsExcluirGosGatNoAdoptats()).thenReturn(Exotics);

        mockMvc.perform(get("/api/animals")
                .param("especie", "Exòtic")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].especie").value("Conill"));

        verify(animalService, times(1)).getAnimalsExcluirGosGatNoAdoptats();
    }

    @Test
    void testGetAnimalsByEspecie_NoExotic()throws Exception {
       
        List<Animal> Gossos = Arrays.asList(animal1);
        when(animalService.getAnimalsByEspecieNoAdoptats("Gos")).thenReturn(Gossos);

        mockMvc.perform(get("/api/animals")
                .param("especie", "Gos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].especie").value("Gos"));

        verify(animalService, times(1)).getAnimalsByEspecieNoAdoptats("Gos");
    }

    @Test
    void testGetAnimalsByEspecie_NoExistent()throws Exception {
        when(animalService.getAnimalsByEspecieNoAdoptats("Peixos")).thenReturn(List.of());

        mockMvc.perform(get("/api/animals")
                .param("especie", "Peixos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        verify(animalService, times(1)).getAnimalsByEspecieNoAdoptats("Peixos");
    }

    @Test
    void getAnimalsByNumId()throws Exception{
        when(animalService.findById(1L)).thenReturn(Optional.of(animal1));

        mockMvc.perform(get("/api/animals/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomAn").value("Dark"))
                .andExpect(jsonPath("$.especie").value("Gos"));

        verify(animalService, times(1)).findById(1L);
    }

    @Test
    void getAnimalsById_NoExistent()throws Exception{
        when(animalService.findById(70L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/animals/70")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(animalService, times(1)).findById(70L);
    }


}
