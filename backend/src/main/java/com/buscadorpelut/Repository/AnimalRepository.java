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
}
