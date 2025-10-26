package com.buscadorpelut.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) utilizado para recibir los datos de un nuevo usuario
 * durante el proceso de registro desde el frontend.
 * 
 * Este objeto se mapea automáticamente desde el cuerpo JSON de la solicitud HTTP
 * en el endpoint /api/register.
 * 
 * Se utiliza Lombok para generar automáticamente:
 * - Getters y setters (@Data)
 * - Constructor vacío y con todos los argumentos (@NoArgsConstructor, @AllArgsConstructor)
 * - Patrón Builder para facilitar la creación de instancias en pruebas o servicios (@Builder)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    /**
     * Nombre del usuario. Requerido en el registro.
     */
    private String nomUs;
    /**
     * Primer apellido del usuario. Requerido en el registro.
     */
    private String cognom1;
    /**
     * Segundo apellido del usuario. Opcional en el registro.
     */
    private String cognom2;
    /**
     * Correo electrónico del usuario. Requerido en el registro.
     */
    private String emailUs;
    /**
     * Contraseña del usuario. Requerido en el registro.
     */
    private String clauPas;
}
