package com.buscadorpelut.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
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
     * @see Animal#setEspecie(String)
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
     * @see Animal#setEspecie(String)
     */
    List<Animal> findByEspecieNotIn(List<String>especies);

    /**
     * Mètode per mostrar tots els animals de les espècies Gat o Gos i que **no estan adoptats**.
     * 
     * @param especie el nom exacte de l'espècie a cercar (ex: "Gos", "Gat").
     * @return una llista d'animals no adoptats de l'espècie seleccionada.
     *         Pot estar buida si no n'hi ha cap disponible.
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
}
