package com.buscadorpelut.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buscadorpelut.Model.Usuari;

public interface UsuarioRepository extends JpaRepository <Usuari, Long>{

}
