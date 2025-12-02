package com.buscadorpelut.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buscadorpelut.DTO.UsuarioDTO;
import com.buscadorpelut.Model.Animal;
import com.buscadorpelut.Model.Protectora;
import com.buscadorpelut.Service.AnimalService;
import com.buscadorpelut.Service.ProtectoraService;
import com.buscadorpelut.Service.UsuarioService;

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

    @GetMapping("/usuaris")
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioService.findAll();
    }

    /**      ANIMALS   */

     @GetMapping("/animals")
    public ResponseEntity<List<Animal>>getAllAnimals(){
        List<Animal> animals = animalService.getAllAnimals();
        return ResponseEntity.ok(animals);
    }

    /**    PROTECTORES   */

    @GetMapping("/protectores")
    public ResponseEntity<List<Protectora>> getAllProtectores(){
        List<Protectora>protectores = protectoraService.getAllProtectores();
        return ResponseEntity.ok(protectores);
    }
}
