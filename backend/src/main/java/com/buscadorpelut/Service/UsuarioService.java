package com.buscadorpelut.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.buscadorpelut.DTO.UsuarioDTO;
import com.buscadorpelut.Model.Rol;
import com.buscadorpelut.Model.Usuario;
import com.buscadorpelut.Repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

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
     * @param codiUs Identificador del usuario.
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

    /**
     * Convierte un objeto DTO de usuario a una entidad JPA.
     * 
     * <p>Este método se utiliza principalmente en operaciones de creación
     * o actualitzación para transformar los datos recibidos desde el frontend
     * (via API REST) a la entidad persistente.
     * 
     * <p>La contraseña se encripta automáticamente con BCrypt si se proporciona.
     * El rol se convierte de string a enum {@link Rol}.
     * 
     * @param dto objeto de transferencia de datos con los datos del usuario.
     * @return entidad {@link Usuario} preparada para persistencia.
     */
    private Usuario convertirAEntitat(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        if (dto.getCodiUs() != null && dto.getCodiUs() != 0) {
            usuario.setCodiUs(dto.getCodiUs());
        }
        usuario.setnomUs(dto.getNomUs());
        usuario.setcognom1(dto.getCognom1());
        usuario.setcognom2(dto.getCognom2());
        usuario.setemailUs(dto.getEmailUs());
        if (dto.getClauPas() != null && !dto.getClauPas().isEmpty()) {
            usuario.setclauPas(passwordEncoder.encode(dto.getClauPas()));
        }
        if (dto.getRolUs() != null) {
            usuario.setrol(Rol.valueOf(dto.getRolUs()));
        }
        return usuario;
    }

    /**
     * Guarda o actualiza un usuario al sistema.
     * 
     * <p>Si se especifica un {@code codiUs} válido, se actualiza el usuario existente.
     * Si no se especifica {@code codiUs} (o es null), se crea un usuario nuevo.
     * 
     * <p>La contraseña solo se actualiza si se proporciona un valor no vacio.
     * Esto permite cambiar otros campos sin tener que volver a enviar la contraseña.
     * 
     * @param dto datos del usuario a guardar (puede contener el identificador para actualizaciones).
     * @return {@link UsuarioDTO} con los datos guardados, añadiendo el identificador autogenerado si es nuevo.
     * @throws EntityNotFoundException si se intenta actualizar un usuario inexistente.
     */
    public UsuarioDTO save(UsuarioDTO dto) {
        Usuario entity;
        if (dto.getCodiUs() != null) {
            entity = usuarioRepository.findById(dto.getCodiUs())
                .orElseThrow(() -> new EntityNotFoundException("Usuari no trobat"));
        } else {
            entity = new Usuario();
        }
        // Actualitza només els camps que vols canviar
        entity.setnomUs(dto.getNomUs());
        entity.setcognom1(dto.getCognom1());
        entity.setcognom2(dto.getCognom2());
        entity.setemailUs(dto.getEmailUs());
        entity.setrol(Rol.valueOf(dto.getRolUs()));
        
        // Només actualitza la contrasenya si s'ha enviat
        if (dto.getClauPas() != null && !dto.getClauPas().isEmpty()) {
            entity.setclauPas(passwordEncoder.encode(dto.getClauPas()));
        }
        
        Usuario saved = usuarioRepository.save(entity);
        return convertirADTO(saved);
    }

    /**
     * Elimina un usuario del sistema por su identificador.
     * 
     * <p>No lanza ninguna excepción si el usuario no existe (comportamiento por defecto
     * de {@link JpaRepository#deleteById}).
     * 
     * @param codiUs identificador de el usuario a eliminar.
     */
    public void deleteBycodiUs(Long codiUs){
        usuarioRepository.deleteById(codiUs);
    }

}
