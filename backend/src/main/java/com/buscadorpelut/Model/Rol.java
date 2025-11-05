package com.buscadorpelut.Model;

/**
 * Enumeración que define los roles de usuario soportados en la aplicación.
 * 
 * Se utiliza en la entidad Usuario para controlar los permisos y accesos.
 * Los valores se almacenan como cadenas en la base de datos (gracias a @Enumerated(EnumType.STRING)).
 * 
 * Roles actuales:
 * - ADMIN: Tiene acceso completo al sistema (gestión de usuarios, configuración, etc.).
 * - ADOPTANT: Usuario estándar con permisos limitados (ej. ver mascotas, gestionar su perfil).
 * 
 * Recomendación: Si agregas nuevos roles, actualiza también las reglas de autorización
 * en la configuración de Spring Security o en los controladores (ej. con @PreAuthorize).
 */
public enum Rol {

    ADMIN, 
    ADOPTANT
}
