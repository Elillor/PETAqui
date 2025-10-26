package com.buscadorpelut.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buscadorpelut.Model.Usuario;



@Repository
public interface  UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailUs(String emailUs);
}
