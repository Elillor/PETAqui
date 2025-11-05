package com.buscadorpelut.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buscadorpelut.Model.Usuario;
import com.buscadorpelut.Service.UsuarioService;

/**
 * Controlador REST para gestionar la autenticación y registro de usuarios.
 * Proporciona endpoints para el registro y login de usuarios en la aplicación.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")// Permite solicitudes desde cualquier origen.
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param usuario Objeto Usuario con los datos del nuevo usuario (validado con @Validated).
     * @return ResponseEntity con mensaje de éxito o error según el resultado del registro.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register (@Validated @RequestBody Usuario usuario) {
        try {
            usuarioService.registroUsuario(usuario);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error al registrar el usuario");            
        }
    }

    /**
     * Autentica a un usuario existente mediante sus credenciales (email y contraseña).
     * 
     * @param loginRequest Objeto con email y contraseña proporcionados por el cliente.
     * @return ResponseEntity con mensaje de éxito si las credenciales son válidas,
     *         o 401 Unauthorized si son incorrectas.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = usuarioService.autenticarUsuario(loginRequest.getEmailUs(), loginRequest.getClauPas());
        if (isAuthenticated) {
            // En una mejora futura se puede implementar generación de token JWT en lugar de solo un mensaje de texto.
            return ResponseEntity.ok("Login exitoso");
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

}
