package com.buscadorpelut.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.buscadorpelut.DTO.UsuarioDTO;
import com.buscadorpelut.Model.Usuario;
import com.buscadorpelut.Repository.UsuarioRepository;

/**
 * Servicio que encapsula la lógica de negocio relacionada con los usuarios.
 * 
 * Responsabilidades principales:
 * - Registro seguro de nuevos usuarios (con encriptación de contraseñas).
 * - Autenticación basada en email y contraseña.
 * - Conversión de entidades a DTOs para exposición segura en la API.
 */
@Service
public class UsuarioService {

    /**
     * Repositorio para operaciones de persistencia con la entidad {@link Usuario}.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Codificador de contraseñas configurado como BCrypt en {@link com.buscadorpelut.Config.SecurityConfig}.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param usuario Objeto {@link Usuario} con los datos del nuevo usuario.
     * @return El usuario guardado en la base de datos (con contraseña encriptada).
     * 
     * La contraseña se encripta automáticamente antes de persistir.
     * Nunca se almacena en texto plano.
     */
    public Usuario registroUsuario(Usuario usuario) {
        // Cifrar la contraseña antes de guardar
        usuario.setclauPas(passwordEncoder.encode(usuario.getclauPas()));
        return usuarioRepository.save(usuario);
    }

    /**
     * Autentica a un usuario verificando su email y contraseña.
     * 
     * @param email Correo electrónico del usuario.
     * @param password Contraseña en texto claro proporcionada por el cliente.
     * @return Verdadero si las credenciales son válidas, falso en caso contrario.
     */
    public Boolean autenticarUsuario(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmailUs(email).orElse(null);
        if (usuario != null) {
            return passwordEncoder.matches(password, usuario.getclauPas());
        }
        return false;
    }

    /**
     * Obtiene la lista de todos los usuarios convertidos a DTO.
     * 
     * @return Lista de {@link UsuarioDTO} representando a todos los usuarios.
     */
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un usuario por su ID y lo convierte a DTO.
     * 
     * @param id Identificador del usuario.
     * @return {@link Optional} con el {@link UsuarioDTO} si existe, vacío en caso contrario.
     */
    public Optional<UsuarioDTO> findById(Long codiUs) {
        if (codiUs == null) {
        return Optional.empty();
        }
        return usuarioRepository.findById(codiUs)
                .map(this::convertirADTO);
    }
    /**
     * Actualiza el perfil de un usuario existente.
     * 
     * @param codiUs ID del usuario a actualizar.
     * @param usuarioDTO Datos actualizados del usuario.
     * @return El {@link UsuarioDTO} actualizado.
     * @throws RuntimeException si el usuario no existe o si el email ya está en uso.
     * 
     */
    public UsuarioDTO updatePerfilUsuario(Long codiUs, UsuarioDTO usuarioDTO) {

        Usuario usuario = usuarioRepository.findById(codiUs)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + codiUs));
        
        if(usuarioDTO.getEmailUs() != null && !usuarioDTO.getEmailUs().equalsIgnoreCase(usuario.getemailUs())) {
            if(usuarioRepository.findByEmailUs(usuarioDTO.getEmailUs()).isPresent()) {
                throw new RuntimeException("El email ya está en uso: " + usuarioDTO.getEmailUs());
            }
            usuario.setemailUs(usuarioDTO.getEmailUs());
        }
        usuario.setnomUs(usuarioDTO.getNomUs());
        usuario.setcognom1(usuarioDTO.getCognom1());
        usuario.setcognom2(usuarioDTO.getCognom2());

        if(usuarioDTO.getClauPas() != null && !usuarioDTO.getClauPas().isEmpty()) {
            usuario.setclauPas(passwordEncoder.encode(usuarioDTO.getClauPas()));
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioActualizado);
    }

    /**
     * Convierte una entidad {@link Usuario} a su representación DTO.
     *
     * @param usuario Entidad a convertir.
     * @return Objeto {@link UsuarioDTO} con todos los campos.
     */
    private UsuarioDTO convertirADTO(Usuario usuario){
        UsuarioDTO dto = new UsuarioDTO();    
        dto.setCodiUs(usuario.getCodiUs());
        dto.setNomUs(usuario.getnomUs());
        dto.setCognom1(usuario.getcognom1());
        dto.setCognom2(usuario.getcognom2());
        dto.setEmailUs(usuario.getemailUs());
        dto.setClauPas(usuario.getclauPas());
        dto.setRolUs(usuario.getrol().name());
        return dto;
    }

}
