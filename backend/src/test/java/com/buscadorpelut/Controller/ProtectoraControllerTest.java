package com.buscadorpelut.Controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
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

import com.buscadorpelut.Model.Protectora;
import com.buscadorpelut.Service.ProtectoraService;

@WebMvcTest(ProtectoraController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProtectoraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private ProtectoraService protectoraService;

    private Protectora protectora1;
    private Protectora protectora2;

    @BeforeEach
    void setUp(){
        protectora1 = new Protectora(
            1L,
            "A.A.A. amics",
            "carrer Fontanella",
            "08031",
            "Barcelona",
            "Barcelona",
            "www.amics.com",
            42.000321,
            2.003234,
            "620234242",
            "amics@gmail.com"
        );

        protectora2 = new Protectora(
            2L,
            "A.P.A. Rodamón",
            "Pujada Morena s/n ",
            "17200",
            "Palafrugell",
            "Girona",
            "www.rodamon.com",
            3.14555,
            41.9215522,
            "629536778",
            "rodamon@gmail.com"
        );
    }

    @Test
    void testGetAllProtectores()throws Exception{
        List<Protectora>protectoras = Arrays.asList(protectora1,protectora2);
        when(protectoraService.getAllProtectores()).thenReturn(protectoras);

        mockMvc.perform(get("/api/protectores")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nomProt").value("A.A.A. amics"))
                .andExpect(jsonPath("$[1].provincia").value("Girona"));
        verify(protectoraService, times(1)).getAllProtectores();
    }

    @Test
    void testGetProtectoraByCodiProt()throws Exception {
        when(protectoraService.getProtectoraByCodiProt(1L)).thenReturn(Optional.of(protectora1));

        mockMvc.perform(get("/api/protectores/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomProt").value("A.A.A. amics"));

        verify(protectoraService, times(1)).getProtectoraByCodiProt(1L);
    }

    @Test
    void testGetProtectoraByCodiProtNoExist()throws Exception {
        when(protectoraService.getProtectoraByCodiProt(988L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/protectores/988")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
                

        verify(protectoraService, times(1)).getProtectoraByCodiProt(988L);
    }

    @Test
    void testGetProtectoraByEmailProt()throws Exception {
        when(protectoraService.getProtectoraByEmailProt("rodamon@gmail.com"))
                .thenReturn(Optional.of(protectora2));

        mockMvc.perform(get("/api/protectores")
                .param("emailProt", "rodamon@gmail.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomProt").value("A.P.A. Rodamón"));

        verify(protectoraService, times(1)).getProtectoraByEmailProt("rodamon@gmail.com");

    }

    @Test
    void testGetProtectoraByLongitudAndLatitud()throws Exception {

        when(protectoraService.getProtectoraByLongitudAndLatitud(42.000321, 2.003234))
                .thenReturn(Optional.of(protectora1));

        mockMvc.perform(get("/api/protectores")
                .param("longitud", "42.000321")
                .param("latitud", "2.003234")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomProt").value("A.A.A. amics"));

        verify(protectoraService, times(1))
                .getProtectoraByLongitudAndLatitud(42.000321, 2.003234);
    }

    @Test
    void testGetProtectoraByNomProt()throws Exception {

        when(protectoraService.getProtectoraByNomProt("A.P.A. Rodamón"))
                .thenReturn(Optional.of(protectora2));

        mockMvc.perform(get("/api/protectores")
                .param("nomProt", "A.P.A. Rodamón")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomProt").value("A.P.A. Rodamón"));

        verify(protectoraService, times(1)).getProtectoraByNomProt("A.P.A. Rodamón");
    }

    @Test
    void testGetProtectoresByCiutat()throws Exception {
        List<Protectora> protectores = Arrays.asList(protectora2);
        when(protectoraService.getProtectoresByAdresa("Pujada Morena s/n")).thenReturn(protectores);

        mockMvc.perform(get("/api/protectores")
                .param("adresa", "Pujada Morena s/n")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(protectoraService, times(1)).getProtectoresByAdresa("Pujada Morena s/n");
    }

    @Test
    void testGetProtectoresByCodiPostal()throws Exception{

        List<Protectora> protectores = Arrays.asList(protectora1);
        when(protectoraService.getProtectoresByCodiPostal("08031")).thenReturn(protectores);

        mockMvc.perform(get("/api/protectores")
                .param("codiPostal", "08031")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(protectoraService, times(1)).getProtectoresByCodiPostal("08031");
    }

    @Test
    void testGetProtectoresByLocalitat()throws Exception{

        List<Protectora> protectores = Arrays.asList(protectora2);
        when(protectoraService.getProtectoresByLocalitat("Palafrugell")).thenReturn(protectores);

        mockMvc.perform(get("/api/protectores")
                .param("localitat", "Palafrugell")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].localitat").value("Palafrugell"));

        verify(protectoraService, times(1)).getProtectoresByLocalitat("Palafrugell");
    }

    @Test
    void testGetProtectoresByProvincia()throws Exception {

        List<Protectora> protectores = Arrays.asList(protectora1);
        when(protectoraService.getProtectoresByProvincia("Barcelona")).thenReturn(protectores);

        mockMvc.perform(get("/api/protectores")
                .param("provincia", "Barcelona")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(protectoraService, times(1)).getProtectoresByProvincia("Barcelona");
    }
}
