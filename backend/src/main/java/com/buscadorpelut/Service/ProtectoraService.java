package com.buscadorpelut.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.buscadorpelut.Model.Protectora;

import com.buscadorpelut.Repository.ProtectoraRepository;

/**
 * Servei per a l'entitat {@link Protectora}.
 * 
 * <p>Aquesta classe proporciona mètodes per gestionar
 * les operacions relacionades amb les protectores, utilitzant
 * el {@link ProtectoraRepository} per accedir a les dades.
 * 
 * @author Luis Gil
 */
@Service
public class ProtectoraService {

    
    @Autowired
    private ProtectoraRepository protectoraRepository;

    /**
     * Retorna totes les protectores registrades a la base de dades.
     * 
     * @return una llista de totes les protectores.
     *       Pot estar buida si no hi ha protectores registrades.
     * @see ProtectoraRepository#findAll()
     */
    public List<Protectora>getAllProtectores(){
        return protectoraRepository.findAll();
    }

    /**
     * Retorna una protectora por su identificador para la consola de administracion.
     * 
     * @param codiProt identificador de la protectora
     * @return un objeto {@link Optional} que contiene la protectora si existe,
     * o vacio si no existe.
     */
    public Optional<Protectora>findById(Long codiProt){
        return protectoraRepository.findById(codiProt);
    }

    /**
     * Retorna una protectora pel seu codi únic.
     * 
     * @param codiProt el codi únic de la protectora, si és null, retorna un Optional buit.
     * @return un objecte {@link Optional} que conté la protectora si existeix,
     *         o buit si no s'ha trobat cap protectora amb aquest codi.
     * @see ProtectoraRepository#findByCodiProt(long)
     */
    public Optional<Protectora>getProtectoraByCodiProt(Long codiProt){
        if(codiProt == null){
            return Optional.empty();
        }
        return protectoraRepository.findByCodiProt(codiProt);
    }

    /**
     * Retorna una protectora pel seu nom.
     * 
     * @param nomProt el nom de la protectora.
     * @return un objecte {@link Optional} que conté la protectora si existeix,
     *         o buit si no s'ha trobat cap protectora amb aquest nom.
     * @see ProtectoraRepository#findProtectoraByNomProt(String)
     */
    public Optional<Protectora>getProtectoraByNomProt(String nomProt ){
        return protectoraRepository.findProtectoraByNomProt(nomProt);
    }

    /**
     * Retorna una protectora per la seva adreça.
     * 
     * @param adresa la adreça de la protectora.
     * @return una llista de protectores que coincideixen amb l'adreça.
     *         pot estar buida si no s'han trobat resultats.
     * @see ProtectoraRepository#findByAdresa(String)
     */
    public List<Protectora>getProtectoresByAdresa(String adresa){
        return protectoraRepository.findByAdresa(adresa);
    }

    /**
     * Retorna una protectora per el seu codi postal.
     * 
     * @param codiPostal el codi postal de la protectora.
     * @return una llista de protectores que coincideixen amb el codi postal.
     *         pot estar buida si no s'han trobat resultats.
     * @see ProtectoraRepository#findByCodiPostal(String)
     */
    public List<Protectora>getProtectoresByCodiPostal(String codiPostal){
        return protectoraRepository.findByCodiPostal(codiPostal);
    }

    /**
     * Retorna una protectora per la seva localitat.
     * 
     * @param localitat la localitat de la protectora.
     * @returnuna llista de protectores que coincideixen amb la localitat.
     *         pot estar buida si no s'han trobat resultats.
     * @see ProtectoraRepository#findByLocalitat(String)
     */
    public List<Protectora>getProtectoresByLocalitat(String localitat){
        return protectoraRepository.findByLocalitat(localitat);
    }

    /**
     * Retorna una protectora per la seva provincia.
     * 
     * @param provincia la provincia de la protectora.
     * @return una llista de protectores que coincideixen amb la provincia.
     *         pot estar buida si no s'han trobat resultats.
     * @see ProtectoraRepository#findByProvincia(String)
     */
    public List<Protectora>getProtectoresByProvincia(String provincia){
        return protectoraRepository.findByProvincia(provincia);
    }

    /**
     * Retorna una protectora per la seva ubicació geogràfica (longitud, latitud).
     * 
     * @param longitud longitud geogràfica de la protectora.
     * @param latitud latitud geogràfica de la protectora.
     * @returnun objecte {@link Optional} que conté la protectora si existeix,
     *         o buit si no s'ha trobat cap protectora amb aquesta ubicació.
     * @see ProtectoraRepository#findByLongitudAndLatitud(double, double).
     */
    public Optional<Protectora>getProtectoraByLongitudAndLatitud(double longitud, double latitud){
        return protectoraRepository.findByLongitudAndLatitud(longitud, latitud);
    }

    /**
     * Retorna una protectora per el seu email.
     * 
     * @param emailProt el email de la protectora.
     * @return un objecte {@link Optional} que conté la protectora si existeix,
     *         o buit si no s'ha trobat cap protectora amb aquest email.
     * @see ProtectoraRepository#findByEmailProt(String)
     */
    public Optional<Protectora>getProtectoraByEmailProt(String emailProt){
        return protectoraRepository.findByEmailProt(emailProt);
    }

    /**
     * Guarda una protectora en el sistema..
     * 
     * <p>Si la protectora ya tiene un {@code codiProt} válido y existe en la base de datos,
    * se actualizan los datos. Si no tiene {@code codiProt} o es nuevo,
    * se crea una protectora nueva con su identificador autogenerado.
     * 
     * <p>Este mètode gestiona automáticamente todos los campos de la protectora,
     * incluida la localización geográfica (longitud i latitud).
     * 
     * @param protectora entidad {@link Protectora} con los datos a guardar.
     * @return {@link Protectora} guardada, incluido el identificador autogenerado si es nueva.
     */
    public Protectora save(Protectora protectora){
        return protectoraRepository.save(protectora);
    }

    /**
     * Elimina una protectora del sistema por su identificador.
     * 
     * <p>No lanza ninguna excepción si la protectora no existe (comportamiento per defecto
     * de {@link JpaRepository#deleteById}).
     * 
     * @param codiProt identificador de la protectora a eliminar.
     */
    public void deleteById(Long codiProt){
        protectoraRepository.deleteById(codiProt);
    }

}