package com.buscadorpelut.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buscadorpelut.DTO.UsuarioDTO;
import com.buscadorpelut.Model.Animal;
import com.buscadorpelut.Model.Protectora;
import com.buscadorpelut.Service.AnimalService;
import com.buscadorpelut.Service.ProtectoraService;
import com.buscadorpelut.Service.UsuarioService;

/**
 * Controlador REST para el panel de administración de PETAqui.
 * 
 * <p>Este controlador proporciona puntos finales para administrar usuarios,
 *animales y protectores. Todos los puntos finales requieren autenticación
 *y permisos de rol ADMIN (administrados por Spring Security).
 * 
 * <p>Se puede acceder a todas las rutas desde {@code /api/admin} y soporte
 *Solicitudes CORS para integración con interfaces externas.
 * 
 * @author Luis Gil
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired

    private UsuarioService usuarioService;

    @Autowired

    private AnimalService animalService;

    @Autowired

    private ProtectoraService protectoraService;

    /**      USUARIS    */

    /**
     * Obtiene la lista completa de todos los usuarios del sistema.
     *
     *@return lista de {@link UsuarioDTO} con datos de usuario
     */
    @GetMapping("/usuaris")
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioService.findAll();
    }

    /**
     * Obtiene un usuario específico por su identificador.
     *
     *@param codiUs identificador único del usuario.
     *@return {@link ResponseEntity} con el usuario si lo encuentra (200 OK),
     *o 404 No encontrado si no existe.
     */
    @GetMapping("/usuaris/{codiUs}")
    public ResponseEntity<UsuarioDTO> getUsuarioByCodiUs(@PathVariable Long codiUs) {
        return usuarioService.findById(codiUs)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo uso al sistema.
     *
     *<p>El identificador de Usuario se ignora y se genera automáticamente.
     *
     *@param usuarioDTO datos del usuario para crear.
     *@return {@link ResponseEntity} con el usuario creado  (201 Creado).
     */
    @PostMapping("/usuaris")
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        usuarioDTO.setCodiUs(null);
        UsuarioDTO saved = usuarioService.save(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Actualiza los datos de un usuario existente.
     * 
     * <p>Se comprueba que el identificador coincida con el del usuario.
     * 
     * @param codiUs identificador del usuario a actualizar.
     * @param usuarioDTO datos de usuario actualizados.
     * @return {@link ResponseEntity} con el usuario actualizado (200 OK),
     *o 400 Solicitud incorrecta si losidentificadores no coinciden,
     *o 404 No encontrado si no existe.
     */
    @PutMapping("/usuaris/{codiUs}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long codiUs,@RequestBody UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getCodiUs() != null && !usuarioDTO.getCodiUs().equals(codiUs)) {
            return ResponseEntity.badRequest().build();
        }
        usuarioDTO.setCodiUs(codiUs);
        UsuarioDTO updated = usuarioService.save(usuarioDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Elimina un usuario del sistema a traves de su identificador.
     * 
     * @param codiUs identificador del usuario a eliminar.
     * @return {@link ResponseEntity} con codigo 204 (No Content).
     */
    @DeleteMapping("/usuaris/{codiUs}")
    public ResponseEntity<Void>deleteUsuari(@PathVariable Long codiUs){
        usuarioService.deleteBycodiUs(codiUs);
        return ResponseEntity.noContent().build();
    }

    /**      ANIMALS   */

    /**
     * Obtiene una lista completa de todos los animales e incluye la protectora a la que pertenece.
     * 
     * @return lista de {@link Animal}.
     */
    @GetMapping("/animals")
    public ResponseEntity<List<Animal>>getAllAnimals(){
        List<Animal> animals = animalService.getAllAnimals();
        return ResponseEntity.ok(animals);
    };

    /**
     * Obtiene un animal a traves de su identificador.
     * 
     * <p>Incluye los datos de la protectora a la que pertenece.
     * 
     * @param numId identificador del animal.
     * @return {@link ResponseEntity} con los datos del animal si existe(200 OK),
     *         o 404 Not Found si no existe.
     */
    @GetMapping("/animals/{numId}")
    public ResponseEntity<Animal>getAnimalsByNumId(@PathVariable Long numId){  
        return animalService.getAnimalByIdWithProtectora(numId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    };

    /**
     * Crea un nuevo animal en el sistema.
     * 
     * <p>El identificador se ignora i se genera automàticamente.
     * 
     * @param animal datos del animal a crear.
     * @return {@link ResponseEntity} con el animal creado (200 OK).
     */
    @PostMapping("/animals")
    public ResponseEntity<Animal>crearAnimal(@RequestBody Animal animal){
        animal.setNumId(null);
        Animal saved = animalService.save(animal);
        return ResponseEntity.ok(saved);
    };

    /**
     * Actualiza los datos de un animal existente.
     * 
     * @param numId identificador del animal a actualizar.
     * @param animal datos actualizados del animal.
     * @return {@link ResponseEntity} con el animal actualizado (200 OK),
     *         o 404 Not Found si no existe.
     */
    @PutMapping("/animals/{numId}")
    public ResponseEntity<Animal>updateAnimal(@PathVariable Long numId, @RequestBody Animal animal){
        if (!animalService.findById(numId).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        animal.setNumId(numId);
        return ResponseEntity.ok(animalService.save(animal));
    };

    /**
     * Elimina un animal del sistema por su identificador.
     * 
     * @param numId identificador del animal a eliminar.
     * @return {@link ResponseEntity} con codigo 204 (No Content),
     *         o 404 Not Found si no existe.
     */
    @DeleteMapping("/animals/{numId}")
    public ResponseEntity<Void>deleteAnimal(@PathVariable Long numId){
        if (!animalService.findById(numId).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        animalService.deleteById(numId);
        return ResponseEntity.noContent().build();
    };

    /**    PROTECTORAS   */

    /**
     * Obtiene una lista completa de todas las protectoras.
     * 
     * @return lista de {@link Protectora}.
     */
    @GetMapping("/protectores")
    public ResponseEntity<List<Protectora>> getAllProtectores(){
        List<Protectora>protectores = protectoraService.getAllProtectores();
        return ResponseEntity.ok(protectores);
    };

    /**
     * Obtiene una protectora a traves de su identificador.
     * 
     * @param codiProt identificador de la protectora.
     * @return {@link ResponseEntity} si la protectora existe (200 OK),
     *         o 404 Not Found si no existe.
     */
    @GetMapping("/protectores/{codiProt}")
    public ResponseEntity<Protectora> getProtectoresByCodiProt(@PathVariable Long codiProt) {
        return protectoraService.findById(codiProt)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    };

    /**
     * Crea una nueva protectora en el sistema.
     * 
     * <p>El identificador de la protectora se ignora y se genera automáticamente.
     * 
     * @param protectora datos de la protectora a crear.
     * @return {@link ResponseEntity} con la protectora creada (200 OK)
     */
    @PostMapping("/protectores")
    public ResponseEntity<Protectora>crearProtectora(@RequestBody Protectora protectora){
        protectora.setCodiProt(null);
        return ResponseEntity.ok(protectoraService.save(protectora));
    };

    /**
     * Actualiza los datos de una protectora existente.
     * 
     * @param codiProt identificador de la protectora a actualizar.
     * @param protectora datos actualizados de la protectora.
     * @return {@link ResponseEntity} con la protectora actualizada (200 OK),
     *         o 404 Not Found si no existe.
     */
    @PutMapping("/protectores/{codiProt}")
    public ResponseEntity<Protectora> updateProtectora(@PathVariable Long codiProt, @RequestBody Protectora protectora) {
        if (!protectoraService.findById(codiProt).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        protectora.setCodiProt(codiProt);
        return ResponseEntity.ok(protectoraService.save(protectora));
    };

    /**
     * Elimina una protectora del sistema por su identificador.
     * 
     * @param codiProt identificador de la protectora a eliminar.
     * @return {@link ResponseEntity} con codigo 204 (No Content),
     *         o 404 Not Found si no existe.
     * 
     */
    @DeleteMapping("/protectores/{codiProt}")
    public ResponseEntity<Void>deleteProtectora(@PathVariable Long codiProt){
        if (!protectoraService.findById(codiProt).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        protectoraService.deleteById(codiProt);
        return ResponseEntity.noContent().build();
    };

}
