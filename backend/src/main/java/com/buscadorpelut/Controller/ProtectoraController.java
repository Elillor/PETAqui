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

/**
 * Controlador REST per gestionar les operacions sobre les protectores d'animals.
 * 
 * <p>Aquesta classe proporciona endpoints per:
 * <ul> 
 *  <li>Llistar totes les protectores</li>
 * </ul>
 * <p>Tots els endpoints accedeixen des de {@code /api/protectores} i admeten peticions CORS
 * per facilitar la integració amb frontends externs.
 * 
 * @author Luis Gil
 */

@RestController
@RequestMapping("/api/protectores")
@CrossOrigin(origins = "*")
public class ProtectoraController {

    @Autowired
    private ProtectoraService protectoraService;

    /**
     * Mètode per retornar totes les protectores registrades al sistema.
     * 
     * @return {@link ResponseEntity} amb una llista de protectores {@link Protectora} i codi HTTP 200 (OK).
     * @see ProtectoraService#getAllProtectores()
     */
    @GetMapping
    public ResponseEntity<List<Protectora>> getAllProtectores(){
        List<Protectora>protectores = protectoraService.getAllProtectores();
        return ResponseEntity.ok(protectores);
    } 
    
    /**
     * Mètode per retornar una protectora segons el seu codi únic.
     * 
     * @param  codiProt el codi únic de la protectora a cercar.
     * @ return {@link ResponseEntity} amb la protectora {@link Protectora} corresponent i codi HTTP 200 (OK).
     *         Si no es troba la protectora, retorna codi HTTP 404 (Not Found).
     * @see ProtectoraService#getProtectoraByCodiProt(Long)
     */
    @GetMapping("{codiProt}")
    public ResponseEntity<Protectora>getProtectoraByCodiProt(@PathVariable Long codiProt){
        return protectoraService.getProtectoraByCodiProt(codiProt)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Mètode per retornar una protectora segons el seu nom.
     * 
     * @param nomProt el nom de la protectora a cercar.
     * @return {@link ResponseEntity} amb la protectora {@link Protectora} corresponent i codi HTTP 200 (OK).
     *         Si no es troba la protectora, retorna codi HTTP 404 (Not Found).
     * @see ProtectoraService#getProtectoraByNomProt(String)
     */
    @GetMapping(params = "nomProt")
    public ResponseEntity<Protectora>getProtectoraByNomProt(@RequestParam String nomProt){
        return protectoraService.getProtectoraByNomProt(nomProt)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Mètode per retornar una llista de protectores segons la seva adreça (ciutat).
     * 
     * @param adresa la ciutat on es troben les protectores a cercar.
     * @return {@link ResponseEntity} amb una llista de protectores {@link Protectora} i codi HTTP 200 (OK).
     *         Pot retornar una llista buida si no hi ha protectores en aquesta ciutat.
     * @see ProtectoraService#getProtectoresByAdresa(String)
     */
    @GetMapping(params = "adresa")
    public ResponseEntity<List<Protectora>>getProtectoresByCiutat(@RequestParam String adresa){
        return ResponseEntity.ok(protectoraService.getProtectoresByAdresa(adresa));
    }

    /**
     * Mètode per retornar una llista de protectores segons el seu codi postal.
     * 
     * @param codiPostal el codi postal on es troben les protectores a cercar.
     * @return {@link ResponseEntity} amb una llista de protectores {@link Protectora} i codi HTTP 200 (OK).
     *         Pot retornar una llista buida si no hi ha protectores en aquest codi postal.
     * @see ProtectoraService#getProtectoresByCodiPostal(String)
     */
    @GetMapping(params = "codiPostal")
    public ResponseEntity<List<Protectora>>getProtectoresByCodiPostal(@RequestParam String codiPostal){
        return ResponseEntity.ok(protectoraService.getProtectoresByCodiPostal(codiPostal));
    }

    /**
     * Mètode per retornar una llista de protectores segons la seva localitat.
     * 
     * @param localitat la localitat on es troben les protectores a cercar.
     * @ return {@link ResponseEntity} amb una llista de protectores {@link Protectora} i codi HTTP 200 (OK).
     *         Pot retornar una llista buida si no hi ha protectores en aquesta localitat.
     * @see ProtectoraService#getProtectoresByLocalitat(String)
     */
    @GetMapping(params = "localitat")
    public ResponseEntity<List<Protectora>>getProtectoresByLocalitat(@RequestParam String localitat){
        return ResponseEntity.ok(protectoraService.getProtectoresByLocalitat(localitat));
    }

    /**
     * Mètode per retornar una llista de protectores segons la seva província.
     * 
     * @param provincia la província on es troben les protectores a cercar.
     * @return {@link ResponseEntity} amb una llista de protectores {@link Protectora} i codi HTTP 200 (OK).
     *        Pot retornar una llista buida si no hi ha protectores en aquesta província.
     * @see ProtectoraService#getProtectoresByProvincia(String)
     */
    @GetMapping(params = "provincia")
    public ResponseEntity<List<Protectora>>getProtectoresByProvincia(@RequestParam String provincia){
        return ResponseEntity.ok(protectoraService.getProtectoresByProvincia(provincia));
    }

    /**
     * Mètode per retornar una protectora segons la seva longitud i latitud.
     * 
     * @param longitud la longitud geogràfica de la protectora a cercar.
     * @param latitud la latitud geogràfica de la protectora a cercar.
     * @return {@link ResponseEntity} amb la protectora {@link Protectora} corresponent i codi HTTP 200 (OK).
     *         Si no es troba la protectora, retorna codi HTTP 404 (Not Found).
     * @see ProtectoraService#getProtectoraByLongitudAndLatitud(double, double)
     */
    @GetMapping(params = {"longitud","latitud"})
    public ResponseEntity<Protectora>getProtectoraByLongitudAndLatitud(@RequestParam double longitud, @RequestParam double latitud){
        return protectoraService.getProtectoraByLongitudAndLatitud(longitud, latitud)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Mètode per retornar una protectora segons el seu email.
     * 
     * @param emailProt l'email de la protectora a cercar.
     * @return {@link ResponseEntity} amb la protectora {@link Protectora} corresponent i codi HTTP 200 (OK).
     *         Si no es troba la protectora, retorna codi HTTP 404 (Not Found).
     * @see ProtectoraService#getProtectoraByEmailProt(String)
     */
    @GetMapping(params = "emailProt")
    public ResponseEntity<Protectora>getProtectoraByEmailProt(@RequestParam String emailProt){
        return protectoraService.getProtectoraByEmailProt(emailProt)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

}
