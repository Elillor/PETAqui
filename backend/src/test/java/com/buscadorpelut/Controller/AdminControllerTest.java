package com.buscadorpelut.Controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Date;
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

import com.buscadorpelut.DTO.UsuarioDTO;
import com.buscadorpelut.Model.Animal;
import com.buscadorpelut.Model.Protectora;
import com.buscadorpelut.Service.AnimalService;
import com.buscadorpelut.Service.ProtectoraService;
import com.buscadorpelut.Service.UsuarioService;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private AnimalService animalService;

    @MockBean
    private ProtectoraService protectoraService;

    private UsuarioDTO usuarioDTO;
    private Animal animal;
    private Protectora protectora;

    @BeforeEach
    void setUp(){
        Date dataNeix = Date.valueOf("2021-03-15");

        //Datos de prueba
        usuarioDTO = new UsuarioDTO(
        1L,
        "pedro",
        "garcia",
        "perez",
        "pedro@gmail.com",
        "22222222",
        "ADOPTANT"
        );

        animal = new Animal(
        1L,
        "Dark",
        "M",
        dataNeix,
        "Gos molt carinyos",
        null,
        "Gos",
        false,
        "data.jpg",
        null
        );

        protectora = new Protectora(
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
    }

    // <<<<<<<   TESTS PER USUARIS   >>>>>>>>>

    @Test
    void testGetReturnListUsuarios() throws Exception{
        List<UsuarioDTO>usuaris = Arrays.asList(usuarioDTO);
        when(usuarioService.findAll()).thenReturn(usuaris);

        mockMvc.perform(get("/api/admin/usuaris")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].nomUs").value("pedro"));

        verify(usuarioService, times(1)).findAll();
    }

    @Test
    void testGetReturnUsuarioByCodiUsExist() throws Exception{
        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuarioDTO));

        mockMvc.perform(get("/api/admin/usuaris/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUs").value("pedro"));

        verify(usuarioService, times(1)).findById(1L);
    }

    @Test
    void testGetReturnUsuarioByCodiUsNoExist() throws Exception{
        when(usuarioService.findById(1000L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admin/usuaris/1000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).findById(1000L);
    }

    @Test
    void testCrearUsuarioReturnCreated() throws Exception {
        when(usuarioService.save(any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        mockMvc.perform(post("/api/admin/usuaris")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nomUs\":\"pedro\",\"cognom1\":\"garcia\",\"emailUs\":\"pedro@example.com\",\"rolUs\":\"ADOPTANT\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomUs").value("pedro"));

        verify(usuarioService, times(1)).save(any(UsuarioDTO.class));
    }

    @Test
    void testReturnOkActualizarUsuario() throws Exception {
        when(usuarioService.save(any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        mockMvc.perform(put("/api/admin/usuaris/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"codiUs\":1,\"nomUs\":\"pedro\",\"cognom1\":\"garcia\",\"emailUs\":\"pedro@example.com\",\"rolUs\":\"ADOPTANT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUs").value("pedro"));

        verify(usuarioService, times(1)).save(any(UsuarioDTO.class));
    }

    @Test
    void testReturnBadRequestActualizarUsuario() throws Exception {
        when(usuarioService.save(any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        mockMvc.perform(put("/api/admin/usuaris/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"codiUs\":2,\"nomUs\":\"pedro\",\"cognom1\":\"garcia\",\"emailUs\":\"pedro@example.com\",\"rolUs\":\"ADOPTANT\"}"))
                .andExpect(status().isBadRequest());

        verify(usuarioService, never()).save(any());
    }

    @Test
    void testDeleteUsuariReturnNoContent() throws Exception{
        doNothing().when(usuarioService).deleteBycodiUs(1L);

        mockMvc.perform(delete("/api/admin/usuaris/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).deleteBycodiUs(1L);
    }

    //<<<<<<<<<<<<<  TESTS PER ANIMALS  >>>>>>>>>>>>>>

    @Test
    void testGetReturnListAllAnimals() throws Exception{
        List<Animal> animals = Arrays.asList(animal);
        when(animalService.getAllAnimals()).thenReturn(animals);

        mockMvc.perform(get("/api/admin/animals")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nomAn").value("Dark"));

        verify(animalService, times(1)).getAllAnimals();
    }

    @Test
    void testGetReturnAnimalsByNumIdExist() throws Exception {
        when(animalService.getAnimalByIdWithProtectora(1L)).thenReturn(Optional.of(animal));

        mockMvc.perform(get("/api/admin/animals/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomAn").value("Dark"));

        verify(animalService, times(1)).getAnimalByIdWithProtectora(1L);
    }

    @Test
    void testReturnOkCrearAnimal() throws Exception {
        when(animalService.save(any(Animal.class))).thenReturn(animal);
        
        mockMvc.perform(post("/api/admin/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nomAn\":\"Dark\",\"especie\":\"Gos\",\"sexe\":\"M\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomAn").value("Dark"));

        verify(animalService, times(1)).save(any(Animal.class));
    }

    @Test
    void testReturnOkUpdateAnimalWithIdExist() throws Exception{
        when(animalService.findById(1L)).thenReturn(Optional.of(animal));
        when(animalService.save(any(Animal.class))).thenReturn(animal);

        mockMvc.perform(put("/api/admin/animals/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"numId\":1,\"nomAn\":\"Dark\",\"especie\":\"Gos\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomAn").value("Dark"));

        verify(animalService, times(1)).findById(1L);
        verify(animalService, times(1)).save(any(Animal.class));
    }

    @Test
    void testDeleteAnimalWithIdExistReturnNoContent() throws Exception{
        when(animalService.findById(1L)).thenReturn(Optional.of(animal));
        doNothing().when(animalService).deleteById(1L);

        mockMvc.perform(delete("/api/admin/animals/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(animalService, times(1)).findById(1L);
        verify(animalService, times(1)).deleteById(1L);
    }

    //<<<<<<<<<<< TESTS PER PROTECTORES >>>>>>>>>

    @Test
    void testGetReturnListAllProtectores() throws Exception{
        List<Protectora> protectores = Arrays.asList(protectora);
        when(protectoraService.getAllProtectores()).thenReturn(protectores);

        mockMvc.perform(get("/api/admin/protectores")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nomProt").value("A.A.A. amics"));

        verify(protectoraService, times(1)).getAllProtectores();
    }

    @Test
    void testGetReturnProtectoresByCodiProtExist() throws Exception{
        when(protectoraService.findById(1L)).thenReturn(Optional.of(protectora));

        mockMvc.perform(get("/api/admin/protectores/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomProt").value("A.A.A. amics"));

        verify(protectoraService, times(1)).findById(1L);
    }

    @Test
    void testCrearProtectoraReturnOk() throws Exception{
        when(protectoraService.save(any(Protectora.class))).thenReturn(protectora);

        mockMvc.perform(post("/api/admin/protectores")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nomProt\":\"A.A.A. amics\",\"adreca\":\"carrer Fontanella\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomProt").value("A.A.A. amics"));

        verify(protectoraService, times(1)).save(any(Protectora.class));
    }

    @Test
    void testUpdateProtectora() throws Exception{
        when(protectoraService.findById(1L)).thenReturn(Optional.of(protectora));
        when(protectoraService.save(any(Protectora.class))).thenReturn(protectora);

        mockMvc.perform(put("/api/admin/protectores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"codiProt\":1,\"nomProt\":\"A.A.A. amics\",\"adreca\":\"carrer Fontanella\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomProt").value("A.A.A. amics"));

        verify(protectoraService, times(1)).findById(1L);
        verify(protectoraService, times(1)).save(any(Protectora.class));
    }

    @Test
    void testDeleteProtectoraWithIdExistReturnNoContent() throws Exception{
        when(protectoraService.findById(1L)).thenReturn(Optional.of(protectora));
        doNothing().when(protectoraService).deleteById(1L);

        mockMvc.perform(delete("/api/admin/protectores/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(protectoraService, times(1)).findById(1L);
        verify(protectoraService, times(1)).deleteById(1L);
    }

}
