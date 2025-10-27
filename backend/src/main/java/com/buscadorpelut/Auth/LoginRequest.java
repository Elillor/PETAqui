package com.buscadorpelut.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) utilizado para recibir las credenciales de autenticación
 * en el endpoint de login. Mapea el cuerpo JSON de la solicitud HTTP a un objeto Java.
 * 
 * Los campos se renombran con @JsonProperty para coincidir con los nombres esperados
 * en el frontend (ej. "email" y "password"), aunque las variables internas usen nombres
 * distintos por convención del proyecto.
 * 
 * Con lombok utilizamos las anotaciones @Data, @AllArgsConstructor y @NoArgsConstructor
 * para generar automáticamente los métodos getters, setters, constructores.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    /**
     * Correo electrónico del usuario, recibido como "email" en el JSON.
     */
    @JsonProperty("email")
    private String emailUs;
    
    /**
     * Contraseña del usuario, recibida como "password" en el JSON.
     */
    @JsonProperty("password")
    private String clauPas;

}
