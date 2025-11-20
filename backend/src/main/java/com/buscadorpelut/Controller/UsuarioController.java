package com.buscadorpelut.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buscadorpelut.DTO.UsuarioDTO;

import com.buscadorpelut.Service.UsuarioService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Controlador REST para gestionar operaciones de lectura sobre los usuarios.
 * 
 * Proporciona endpoints públicos para:
 * - Obtener la lista completa de usuarios.
 * - Obtener un usuario específico por su ID.
 * 
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins="*")
public class UsuarioController {

    /**
     * Servicio inyectado para acceder a la lógica de negocio y persistencia de usuarios.
     */
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Obtiene la lista completa de todos los usuarios registrados en el sistema.
     * 
     * @return Lista de objetos representando a todos los usuarios.
     */
    @GetMapping
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioService.findAll();
    }

    /**
     * Obtiene un usuario específico por su identificador único (ID).
     * 
     * @param id El ID del usuario a buscar.
     * @return Los datos del usuario con esa ID si existe,y si no,
     *         devuelve un estado HTTP 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); 
    }
   
    /**
     * Actualiza el perfil de un usuario existente.
     * 
     * @param codiUs ID del usuario a actualizar.
     * @param usuarioDTO Datos actualizados del usuario.
     * @return El {@link UsuarioDTO} actualizado o un error si no se pudo actualizar.
     */
    @PutMapping("/{codiUs}")
    public ResponseEntity<?> updateUsuario(@PathVariable Long codiUs, @RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO usuarioGuardado = usuarioService.updatePerfilUsuario(codiUs, usuarioDTO);
            return ResponseEntity.ok(usuarioGuardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
 