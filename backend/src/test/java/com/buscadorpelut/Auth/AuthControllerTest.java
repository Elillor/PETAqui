package com.buscadorpelut.Auth;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.buscadorpelut.Model.Rol;
import com.buscadorpelut.Model.Usuario;
import com.buscadorpelut.Service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Clase de prueba para AuthController.
 * Utiliza {@link WebMvcTest} para probar los endpoints de autenticación.
 * Utiliza MockMvc para simular solicitudes HTTP y Mockito para simular el servicio de usuario.
 * Prueba los endpoints de registro de usuario con casos válidos e inválidos.
 * 
 */
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)

/**
 * clase de prueba para el controlador de autenticación.
 */
public class AuthControllerTest {

    /**
     * MockMvc para simular solicitudes HTTP en las pruebas.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * ObjectMapper para convertir objetos Java a JSON y viceversa.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * MockBean para simular el servicio de usuario.
     */
    @MockBean
    private UsuarioService usuarioService;

    /**
     * Prueba el registro de un usuario válido.
     * Simula una solicitud POST al endpoint /api/register con datos de usuario válidos.
     * Verifica que la respuesta sea exitosa y contenga el mensaje esperado.
     * @throws Exception si ocurre un error durante la prueba.
     */
    @Test
    void registrarUsuarioValido()throws Exception {
        /**
         * Given - Datos de usuario válidos y comportamiento simulado del servicio.
         * creación de un nuevo usuario con datos válidos.
         * Simulación del comportamiento del servicio para devolver el usuario guardado.
         */
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setnomUs("Luis");
        nuevoUsuario.setcognom1("Garcia");
        nuevoUsuario.setcognom2("Lopez");
        nuevoUsuario.setclauPas("password123");
        nuevoUsuario.setemailUs("luis@gmail.com");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setnomUs("Luis");
        usuarioGuardado.setcognom1("Garcia");
        usuarioGuardado.setcognom2("Lopez");
        usuarioGuardado.setclauPas("password123");
        usuarioGuardado.setemailUs("luis@gmail.com");
        usuarioGuardado.setrol(Rol.ADOPTANT);

        when(usuarioService.registroUsuario(any(Usuario.class)))
        .thenReturn(usuarioGuardado);

        /**
         * When - Simulación de la solicitud POST al endpoint /api/register.
         * Then - Verificación de la respuesta.
         * Verificación de que la respuesta sea exitosa y contenga el mensaje esperado.
         */
        mockMvc.perform(post("/api/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario registrado exitosamente"));
    }

    /**
     * Prueba el registro de un usuario inválido.
     * Simula una solicitud POST al endpoint /api/register con datos de usuario inválidos.
     * <p>
     * En este caso, el email es obligatorio y se omite en los datos del usuario.
     * 
     * Verifica que la respuesta sea un error de solicitud incorrecta.
     * @throws Exception si ocurre un error durante la prueba.
     */
    @Test
    void registrarUsuarioInvalido() throws Exception {
        /**
         * Given - Datos de usuario inválidos y comportamiento simulado del servicio.
         * creación de un nuevo usuario sin email.
         * Simulación del comportamiento del servicio para lanzar una excepción.
         */
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setnomUs("Luis");
        nuevoUsuario.setcognom1("Garcia");
        nuevoUsuario.setcognom2("Lopez");
        nuevoUsuario.setclauPas("password123");

        when(usuarioService.registroUsuario(any(Usuario.class)))
                .thenThrow(new IllegalArgumentException("Email es obligatorio"));

        /**
         * When - Simulación de la solicitud POST al endpoint /api/register.
         * Then - Verificación de la respuesta.
         * Verificación de que la respuesta sea un error de solicitud incorrecta.
         */
        mockMvc.perform(post("/api/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andExpect(status().isBadRequest());
        
    }

    /**
     * Prueba el login con credenciales válidas.
     * Simula una solicitud POST al endpoint /api/login con email y contraseña válidos.
     * Verifica que la respuesta sea exitosa y contenga el mensaje de login exitoso
     * @throws Exception
     */
    @Test
    void testLoginValidar_Devolver_200() throws Exception {
        /**
         * Given - Credenciales de login válidas y comportamiento simulado del servicio.
         * creación de un objeto LoginRequest con email y contraseña válidos.
         * Simulación del comportamiento del servicio para devolver true en la autenticación.
         */
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailUs("carlos@gmail.com");
        loginRequest.setClauPas("22222222");

        when(usuarioService.autenticarUsuario("carlos@gmail.com", "22222222")).thenReturn(true);

        /**
         * When - Simulación de la solicitud POST al endpoint /api/login.
         * Then - Verificación de la respuesta.
         * Verificación de que la respuesta sea exitosa y contenga el mensaje de login exitos
         */
        mockMvc.perform(post("/api/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login exitoso"));
    }

    /**
     * Prueba el login con credenciales inválidas.
     * Simula una solicitud POST al endpoint /api/login con email y contraseña inválidos.
     * Verifica que la respuesta sea Unauthorized con el mensaje de credenciales inválidas.
     * @throws Exception
     */
    @Test
    void testLoginInvalidar_Devolver_Unauthorized() throws Exception {
        /**
         * Given - Credenciales de login inválidas y comportamiento simulado del servicio.
         * creación de un objeto LoginRequest con email y contraseña inválidos.
         * Simulación del comportamiento del servicio para devolver false en la autenticación.
         */
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailUs("josep@gmail.com");
        loginRequest.setClauPas("patata");

        when(usuarioService.autenticarUsuario("josep@gmail.com", "patata")).thenReturn(false);

        /**
         * When - Simulación de la solicitud POST al endpoint /api/login.
         * Then - Verificación de la respuesta.
         * Verificación de que la respuesta sea Unauthorized con el mensaje de credenciales inválidas.
         */
        mockMvc.perform(post("/api/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciales inválidas"));
    }
}
