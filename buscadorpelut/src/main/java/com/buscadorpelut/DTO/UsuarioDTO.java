package com.buscadorpelut.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) que representa la información pública o serializable de un usuario.
 * 
 * Se utiliza para exponer datos de usuario a través de la API REST, evitando exponer directamente
 * la entidad de dominio (por seguridad, rendimiento o desacoplamiento).
 * 
 * Utilizamos Lombok con @Data para generar automáticamente getters, setters, toString, equals y hashCode.
 * También usamos @AllArgsConstructor y @NoArgsConstructor para generar constructores con y sin argumentos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long codiUs;// Identificador único de la base de datos.
    private String nomUs;// Nombre del usuario.
    private String cognom1;// Primer apellido del usuario.
    private String cognom2;// Segundo apellido del usuario.
    private String emailUs;// Correo electrónico del usuario(unico y utilizado para el login).
    private String clauPas;// Contraseña del usuario (encryptada en la base de datos).
    private String rolUs;// Rol del usuario (ADMIN o ADOPTANT).
}
