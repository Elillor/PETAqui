package com.buscadorpelut.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buscadorpelut.Model.Usuario;

/**
 * Repositorio JPA para la entidad Usuario.
 * 
 * Proporciona operaciones CRUD automáticas gracias a JpaRepository,
 * además de un método personalizado para buscar un usuario por su correo electrónico.
 * 
 * Spring Data JPA genera automáticamente la implementación en tiempo de ejecución,
 * por lo que no es necesario escribir código SQL ni implementaciones manuales.
 */
@Repository
public interface  UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario en la base de datos por su dirección de correo electrónico.
     * 
     * @param emailUs El correo electrónico del usuario.
     * @return Un Optional que contiene el usuario si existe el correo electrónico,
     *         o un Optional vacio si no existe ningún usuario con ese correo electronico.
     */
    Optional<Usuario> findByEmailUs(String emailUs);
}
