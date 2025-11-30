package com.buscadorpelut.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.buscadorpelut.Model.Animal;

/**
 * Repositori JPA per a l'entitat {@link Animal}.
 * 
 * <p>Aquesta interfície genera l'accés a dades per gestionar
 * els registres d'animals a la base de dades. Hereta mètodes estàndard de
 * {@link JpaRepository} (com {@code findAll()}, {@code findById()}, etc.)
 * i defineix mètodes personalitzats per a cerques específiques.
 * 
 * <p>Spring Data JPA genera automàticament la implementació d'aquests mètodes
 * en temps d'execució, basant-se en els noms dels mètodes i les anotacions.
 * 
 * @author Luis Gil
 */
@Repository
public interface AnimalRepository extends JpaRepository<Animal,Long>{

    /**
     * Mètode per cercar tots els animals que pertanyen a una espècie específica.
     * 
     * <p>La comparació és exacta, sensible a majúscules/minúscules i accents.
     * Per exemple, {@code "gos"} no coincidirà amb {@code "Gos"}.
     * 
     * @param especie el nom de l'espècie a cercar (ex: "Gos", "Gat", "Conill").
     * @return una llista d'objectes {@link Animal} que coincideixen amb l'espècie.
     *         La llista pot estar buida si no s'han trobat resultats.
     * 
     */
    List<Animal> findByEspecie(String especie);

    /**
     * Mètode per cercar tots els animals de les espècies que **no estan incloses**
     * en la llista.
     * 
     * <p>S'utilitza per excloure certes espècies
     * (per exemple, "Gos" i "Gat") i obtenir la resta (ex: animals exòtics).
     * 
     * @param especies una llista no buida de noms d'espècies a excloure.
     * @return una llista d'animals que no pertanyen a cap de les espècies indicades.
     *         Pot estar buida si tots els animals pertanyen a les espècies excloses.
     * 
     */
    List<Animal> findByEspecieNotIn(List<String>especies);

    /**
     * Mètode per mostrar tots els animals de les espècies Gat o Gos i que **no estan adoptats**.
     * 
     * @param especie el nom exacte de l'espècie a cercar (ex: "Gos", "Gat").
     * @return una llista d'animals no adoptats de l'espècie seleccionada.
     *         Pot estar buida si no n'hi ha cap disponible.
     * 
     */
    List<Animal> findByEspecieAndEsAdoptatFalse(String especie);

    /**
     * Mètode per mostrar tots els animals amb les espècies que **no estan incloses** en la llista donada
     * i que **no estan adoptats**.
     * 
     * <p>Aquest mètode s'utilitza principalment per obtenir animals "exòtics"
     * (és a dir, tot excepte "Gos" i "Gat") que estiguin disponibles per a adopció.
     * 
     * @param especies una llista de noms d'espècies a excloure (ex: Arrays.asList("Gos", "Gat")).
     * @return una llista d'animals no adoptats d'espècies diferents a les indicades.
     *         Pot estar buida si no hi ha animals disponibles fora d'aquestes espècies.
     */
    List<Animal> findByEspecieNotInAndEsAdoptatFalse(List<String>especies);

    /**
     * Mètode que retorna tots els animals del sistema que **no estan adoptats**.
     * 
     * <p>Aquest mètode s'utilitza per obtenir la llista completa d'animals disponibles
     * per a adopció, independentment de l'espècie, edat o sexe.
     * 
     * @return una llista d'animals no adoptats.
     *         Pot estar buida si tots els animals ja han estat adoptats.
     */
    List<Animal> findByEsAdoptatFalse();

    /**
     * Mètode que retorna tots els animals del sistema que **estan adoptats**.
     * 
     * <p>Aquest mètode s'utilitza per obtenir la llista completa d'animals que ja han estat adoptats.
     * 
     * @return una llista d'animals adoptats.
     *         Pot estar buida si cap animal ha estat adoptat encara.
     */
    List<Animal> findByEsAdoptatTrue();

    /**
     * Mètode per cercar tots els animals que no estan adoptats i que pertanyen a protectores
     * d'una determinada província.
     * @queryBy selecionar animals no adoptats segons la província de la protectora.
     * @param provincia la província de la protectora (ex: "Barcelona", "Madrid").
     * @return una llista d'animals no adoptats de protectores en la província especificada.
     *         La llista pot estar buida si no s'han trobat resultats.
     * 
     * @see com.buscadorpelut.Model.Protectora#setProvincia(String)
     */
    @Query("SELECT a FROM Animal a LEFT JOIN FETCH a.protectora p WHERE a.esAdoptat = false AND p.provincia = :provincia")
    List<Animal> findNonAdoptedAnimalsByProtectoraProvincia(@Param("provincia") String provincia);
    
    /**
     * Mètode per cercar tots els animals que no estan adoptats i que pertanyen a protectores
     * d'un determinat codi postal.
     * 
     * @queryBy selecionar animals no adoptats segons el codi postal de la protectora.
     * @param codiPostal el codi postal de la protectora (ex: "08001", "28013").
     * @return una llista d'animals no adoptats de protectores en el codi postal especificat.
     *         La llista pot estar buida si no s'han trobat resultats.
     * 
     * @see com.buscadorpelut.Model.Protectora#setCodiPostal(String)
     */
    @Query("SELECT a FROM Animal a LEFT JOIN FETCH a.protectora p WHERE a.esAdoptat = false AND p.codiPostal = :codiPostal")
    List<Animal> findNonAdoptedAnimalsByProtectoraCodiPostal(@Param("codiPostal") String codiPostal);

    /**
     * Mètode per cercar tots els animals que no estan adoptats i que pertanyen a protectores
     * d'una determinada localització (província o codi postal).
     * 
     * @queryBy selecionar animals no adoptats segons la localització de la protectora, que pot ser 
     * província o codi postal.
     * @param localitzacio la localització de la protectora (pot ser província o codi postal).
     * @return una llista d'animals no adoptats de protectores en la localització especificada.
     *        La llista pot estar buida si no s'han trobat resultats.
     *
     * @see com.buscadorpelut.Model.Protectora#setProvincia(String)
     */
    @Query("SELECT a FROM Animal a LEFT JOIN FETCH a.protectora p WHERE a.esAdoptat = false AND (p.provincia = :localitzacio OR p.codiPostal = :localitzacio)")
    List<Animal> findNonAdoptedAnimalsByProtectoraLocalitzacio(@Param("localitzacio") String localitzacio);

    /**
     * Mètode per cercar tots els animals que no estan adoptats, pertanyen a una espècie específica
     * i que estan en protectores d'una determinada localització (província o codi postal).
     * 
     * @queryBy selecionar animals no adoptats segons l'espècie i la localització de la protectora,
     * que pot ser província o codi postal.
     * @param especie el nom exacte de l'espècie a cercar (ex: "Gos", "Gat").
     * @param localitzacio la localització de la protectora (pot ser província o codi postal).
     * @return una llista d'animals no adoptats de l'espècie seleccionada en la localització especificada.
     *       La llista pot estar buida si no s'han trobat resultats.
     * 
     * @see com.buscadorpelut.Model.Protectora#setProvincia(String)
     * @see com.buscadorpelut.Model.Protectora#setCodiPostal(String)
     */
    @Query("SELECT a FROM Animal a LEFT JOIN FETCH a.protectora p WHERE a.esAdoptat = false AND a.especie = :especie  AND (p.provincia = :localitzacio OR p.codiPostal = :localitzacio)")
    List<Animal> findNonAdoptedAnimalsByEspecieAndLocalitzacio(@Param("especie") String especie, @Param("localitzacio") String localitzacio);

    /**
     * Mètode per cercar tots els animals d'espècies exòtiques (no Gos ni Gat) que no estan adoptats
     * i que estan en protectores d'una determinada localització (província o codi postal).
     * 
     * @queryBy selecionar animals exòtics no adoptats segons la localització de la protectora,
     * que pot ser província o codi postal.
     * @param localitzacio la localització de la protectora (pot ser província o codi postal).
     * @return una llista d'animals exòtics no adoptats en la localització especificada.
     *        La llista pot estar buida si no s'han trobat resultats.
     * 
     * @see com.buscadorpelut.Model.Protectora#setProvincia(String)
     * @see com.buscadorpelut.Model.Protectora#setCodiPostal(String)
     */
    @Query("SELECT a FROM Animal a LEFT JOIN FETCH a.protectora p WHERE a.esAdoptat = false AND a.especie NOT IN ('Gos','Gat')  AND (p.provincia = :localitzacio OR p.codiPostal = :localitzacio)")
    List<Animal> findNonAdoptedExoticAnimalsByLocalitzacio(@Param("localitzacio") String localitzacio);

    /**
     * Mètode per cercar un animal per la seva ID i carregar també la seva protectora associada.
     * 
     * @queryBy seleccionar un animal i la seva protectora segons la ID de l'animal.
     * @param numId la ID única de l'animal.
     * @ return un objecte {@link Optional} que conté l'animal amb la seva protectora si existeix,
     *         o buit si no s'ha trobat cap animal amb aquesta ID.
     * 
     * @see Animal#setProtectora(com.buscadorpelut.Model.Protectora)
     */
    @Query("SELECT a FROM Animal a LEFT JOIN FETCH a.protectora WHERE a.numId = :numId")
    Optional<Animal> findByIdWithProtectora(@Param("numId") Long numId);
}
