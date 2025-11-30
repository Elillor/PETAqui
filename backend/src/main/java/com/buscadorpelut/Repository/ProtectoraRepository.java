package com.buscadorpelut.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buscadorpelut.Model.Protectora;

/**
 * Repositori JPA per a l'entitat {@link Protectora}.
 * 
 * <p>Aquesta interfície genera l'accés a dades per gestionar
 * els registres de protectores a la base de dades. Hereta mètodes estàndard de
 * {@link JpaRepository} (com {@code findAll()}, {@code findById()}, etc.)
 * i defineix mètodes personalitzats per a cerques específiques.
 * <p>Spring Data JPA genera automàticament la implementació d'aquests mètodes
 * en temps d'execució, basant-se en els noms dels mètodes i les anotacions.
 * 
 * @author Luis Gil
 */
@Repository
public interface ProtectoraRepository extends JpaRepository<Protectora,Long>{

    /**
     * Mètode per cercar una protectora pel seu codi únic.
     * 
     * @param codiProt el codi únic de la protectora.
     * @return un objecte {@link Optional} que conté la protectora si existeix,
     *         o buit si no s'ha trobat cap protectora amb aquest codi.
     * 
     * @see Protectora#setCodiProt(long)
     */
    Optional<Protectora> findByCodiProt(long codiProt);

    /**
     * Mètode per cercar una protectora pel seu nom.
     * 
     * @param nomProt el nom de la protectora.
     * @return un objecte {@link Optional} que conté la protectora si existeix,
     *         o buit si no s'ha trobat cap protectora amb aquest nom.
     * 
     * @see Protectora#setNomProt(String)
     */
    Optional<Protectora> findProtectoraByNomProt(String nomProt);

    /**
     * Mètode per cercar protectores per la seva adreça.
     * 
     * @param adresa la adreça de la protectora.
     * @return una llista de protectores que coincideixen amb l'adreça.
     *         La llista pot estar buida si no s'han trobat resultats.
     * @see Protectora#setAdresa(String)
     */
    List<Protectora> findByAdresa(String adresa);

    /**
     * Mètode per cercar protectores pel seu codi postal.
     * 
     * @param codiPostal el codi postal de la protectora.
     * @return una llista de protectores que coincideixen amb el codi postal.
     *         La llista pot estar buida si no s'han trobat resultats.
     * @see Protectora#setCodiPostal(String)
     */
    List<Protectora> findByCodiPostal(String codiPostal);

    /**
     * Mètode per cercar protectores per la seva localitat.
     * 
     * @param localitat la localitat de la protectora.
     * @return una llista de protectores que coincideixen amb la localitat.
     *         La llista pot estar buida si no s'han trobat resultats.
     * @see Protectora#setLocalitat(String)
     */
    List<Protectora> findByLocalitat(String localitat);

    /**
     * Mètode per cercar protectores per la seva província.
     * 
     * @param provincia la província de la protectora.
     * @return una llista de protectores que coincideixen amb la província.
     *         La llista pot estar buida si no s'han trobat resultats.
     * @see Protectora#setProvincia(String)
     */
    List<Protectora> findByProvincia(String provincia);

    /**
     * Mètode per cercar una protectora per la seva ubicació geogràfica (longitud i latitud).
     * 
     * @param longitud la longitud geogràfica de la protectora.
     * @param latitud la latitud geogràfica de la protectora.
     * @return un objecte {@link Optional} que conté la protectora si existeix,
     *         o buit si no s'ha trobat cap protectora amb aquesta ubicació.
     * @see Protectora#setLongitud(double)
     * @see Protectora#setLatitud(double)
     */
    Optional<Protectora> findByLongitudAndLatitud(double longitud, double latitud);

    /**
     * Mètode per cercar una protectora pel seu email.
     * 
     * @param emailProt l'email de la protectora.
     * @return un objecte {@link Optional} que conté la protectora si existeix,
     *         o buit si no s'ha trobat cap protectora amb aquest email.
     * @see Protectora#setEmailProt(String)
     */
    Optional<Protectora> findByEmailProt(String emailProt);



    

}
