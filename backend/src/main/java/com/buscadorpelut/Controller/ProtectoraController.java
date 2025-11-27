package com.buscadorpelut.Controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buscadorpelut.Model.Protectora;
import com.buscadorpelut.Service.ProtectoraService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/protectores")
@CrossOrigin(origins = "*")
public class ProtectoraController {

    @Autowired
    private ProtectoraService protectoraService;

    @GetMapping
    public ResponseEntity<List<Protectora>> getAllProtectores(){
        List<Protectora>protectores = protectoraService.getAllProtectores();
        return ResponseEntity.ok(protectores);
    } 
    
    @GetMapping("{codiProt}")
    public ResponseEntity<Protectora>getProtectoraByCodiProt(@PathVariable Long codiProt){
        return protectoraService.getProtectoraByCodiProt(codiProt)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(params = "nomProt")
    public ResponseEntity<Protectora>getProtectoraByNomProt(@RequestParam String nomProt){
        return protectoraService.getProtectoraByNomProt(nomProt)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(params = "adresa")
    public ResponseEntity<List<Protectora>>getProtectoresByCiutat(@RequestParam String adresa){
        return ResponseEntity.ok(protectoraService.getProtectoresByAdresa(adresa));
    }

    @GetMapping(params = "codiPostal")
    public ResponseEntity<List<Protectora>>getProtectoresByCodiPostal(@RequestParam String codiPostal){
        return ResponseEntity.ok(protectoraService.getProtectoresByCodiPostal(codiPostal));
    }

    @GetMapping(params = "localitat")
    public ResponseEntity<List<Protectora>>getProtectoresByLocalitat(@RequestParam String localitat){
        return ResponseEntity.ok(protectoraService.getProtectoresByLocalitat(localitat));
    }

    @GetMapping(params = "provincia")
    public ResponseEntity<List<Protectora>>getProtectoresByProvincia(@RequestParam String provincia){
        return ResponseEntity.ok(protectoraService.getProtectoresByProvincia(provincia));
    }

    @GetMapping(params = {"longitud","latitud"})
    public ResponseEntity<Protectora>getProtectoraByLongitudAndLatitud(@RequestParam double longitud, @RequestParam double latitud){
        return protectoraService.getProtectoraByLongitudAndLatitud(longitud, latitud)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping(params = "emailProt")
    public ResponseEntity<Protectora>getProtectoraByEmailProt(@RequestParam String emailProt){
        return protectoraService.getProtectoraByEmailProt(emailProt)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

}
