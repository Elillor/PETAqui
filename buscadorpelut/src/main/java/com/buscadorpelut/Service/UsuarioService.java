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



@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registroUsuario(Usuario usuario) {
        // Cifrar la contraseña antes de guardar
        usuario.setclauPas(passwordEncoder.encode(usuario.getclauPas()));
        return usuarioRepository.save(usuario);
    }

    public Boolean autenticarUsuario(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmailUs(email).orElse(null);
        if (usuario != null) {
            return passwordEncoder.matches(password, usuario.getclauPas());
        }
        return false;
    }

    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioDTO> findById(Long id) {
        return usuarioRepository.findById(id)
                .map(this::convertirADTO);
    }

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
