package com.buscadorpelut.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.buscadorpelut.Model.Animal;
import com.buscadorpelut.Model.Protectora;
import com.buscadorpelut.Repository.AnimalRepository;

/**
 * Servei per gestionar la lògica de negoci relacionada amb els animals.
 * 
 * <p>Aquesta classe actua com a intermediària entre el controlador REST i el repositori,
 * mostrant informació dels animals de diverses formes 
 * (total, per espècie, etc.).
 * 
 * <p>Totes les operacions deleguen en {@link AnimalRepository} per a l'accés a dades.
 * 
 * @author Luis Gil
 */
@Service
public class AnimalService {

  @Autowired
  private AnimalRepository animalRepository;

  /**
     * Recupera una llista completa de tots els animals registrats al sistema.
     * 
     * @return una llista (segons implementació del repositori) d'objectes {@link Animal}.
     *         Pot estar buida si no hi ha cap animal a la base de dades.
     * 
     * @see AnimalRepository#findAll()
     */
  public List<Animal>getAllAnimals(){
    return animalRepository.findAll();
  }

  /**
    * Recupera una llista completa de tots els animals registrats al sistema que no estan adoptats.
    *  
    * @return una llista d'objectes {@link Animal} que estan en situació d'adopció.
    *         Pot estar buida si no hi ha cap animal a la base de dades.
    *
    * @see AnimalRepository#findByEsAdoptatFalse()     
   */
  public List<Animal>getAllAnimalsNoAdoptats(){
    return animalRepository.findByEsAdoptatFalse();
  }
  /**
   * Recupera una llista completa de tots els animals registrats al sistema que estan adoptats.
   * 
   * @return una llista d'objectes {@link Animal} que ja han estat adoptats.
   *        Pot estar buida si no hi ha cap animal adoptat a la base de dades.
   * 
   * @see AnimalRepository#findByEsAdoptatTrue()
   */
  public List<Animal>getAllAnimalsAdoptats(){
    return animalRepository.findByEsAdoptatTrue();
  }

  /**
     * Mostra els detalls d'un animal pel seu identificador únic numèric.
     * 
     * <p>Si l'ID proporcionat és {@code null}, es retorna un {@link Optional#empty()} 
     * per evitar excepcions.
     * 
     * @param id l'identificador únic de l'animal a cercar.
     * @return un {@link Optional} que conté l'animal si es troba, o buit si no existeix 
     *         o si l'ID és {@code null}.
     * 
     * @see AnimalRepository#findById(Object)
     */
  public Optional<Animal>findById(Long numId){
      return animalRepository.findById(numId);
  }

  /**
     * Mostra tots els animals que pertanyen a una espècie específica.
     * 
     * <p>La comparació és sensible a majúscules/minúscules i accents. 
     * Per exemple, "gos" ≠ "Gos" ≠ "Gòs".
     * 
     * @param especie el nom exacte de l'espècie a cercar (ex: "Gos", "Gat", "Conill").
     * @return una llista d'animals d'aquella espècie. Pot estar buida si no n'hi ha cap.
     * 
     * @see AnimalRepository#findByEspecie(String)
     */
  public List<Animal>getAnimalsByEspecie(String especie){
    return animalRepository.findByEspecie(especie);
  }

  /**
     * Retorna tots els animals que **no pertanyen** a les espècies "Gos" ni "Gat".
     * 
     * <p>Aquest mètode s'utilitza per implementar la categoria "Exòtic" al frontend,
     * incloent espècies com conills, aus, rèptils, cobaias, etc.
     * 
     * @return una llista d'animals d'espècies diferents a "Gos" i "Gat". 
     *         Pot estar buida si només hi ha gossos i gats al sistema.
     * 
     * @see AnimalRepository#findByEspecieNotIn(List)
     */
  public List<Animal>getAnimalsExcluirGosGat(){
    return animalRepository.findByEspecieNotIn(Arrays.asList("Gos","Gat"));
  }

  /**
 * Retorna tots els animals d'una espècie específica que **encara no han estat adoptats**.
 * 
 * <p>Aquest mètode s'utilitza quan es vol filtrar la llista d'animals disponibles
 * per adopció segons la seva espècie (per exemple, només "Gos" o només "Gat").
 * 
 * @param especie el nom de l'espècie a cercar (ha de coincidir exactament amb el valor
 *                emmagatzemat a la base de dades, p. ex. "Gos", "Gat").
 * @return una llista d'animals {@link Animal} que pertanyen a l'espècie indicada
 *         i que tenen {@code esAdoptat = false}.
 *         La llista pot estar buida si no hi ha animals d'aquella espècie disponibles.
 * 
 * @see AnimalRepository#findByEspecieAndEsAdoptatFalse(String)
 */
  public List<Animal>getAnimalsByEspecieNoAdoptats(String especie){
    return animalRepository.findByEspecieAndEsAdoptatFalse(especie);
  }

  /**
 * Retorna tots els animals que no pertanyen a les espècies "Gos" ni "Gat" i que estan en situació d'adopció.
 * 
 * <p>Aquest mètode implementa la categoria "Exòtic",
 * incloent espècies com conills, aus, cobaias, rèptils, etc., sempre que estiguin
 * disponibles per a adopció ({@code esAdoptat = false}).
 * 
 * @return una llista d'animals d'espècies diferents a "Gos" i "Gat" i que estan en situació d'adopció.
 *         La llista pot estar buida si no hi ha animals "exòtics" disponibles.
 * 
 * @see AnimalRepository#findByEspecieNotInAndEsAdoptatFalse(List)
 */
  public List<Animal>getAnimalsExcluirGosGatNoAdoptats(){
    return animalRepository.findByEspecieNotInAndEsAdoptatFalse(Arrays.asList("Gos","Gat"));
  }

  /**
   * Retorna tots els animals que no estan adoptats i que pertanyen a protectores
   * ubicades en una província específica.
   * 
   * @param provincia la província on es troben les protectores.
   * @return una llista d'animals no adoptats de protectores en la província indicada.
   *         Pot estar buida si no hi ha animals disponibles en aquesta ubicació.
   * @see AnimalRepository#findNonAdoptedAnimalsByProtectoraProvincia(String)
   */
  public List<Animal>getAnimalsNoAdoptatsByProvincia(String provincia){
    return animalRepository.findNonAdoptedAnimalsByProtectoraProvincia(provincia);
  }

  /**
   * Retorna tots els animals que no estan adoptats i que pertanyen a protectores
   * ubicades en un codi postal específic.
   * 
   * @param codiPostal el codi postal on es troben les protectores.
   * @return una llista d'animals no adoptats de protectores en el codi postal indicat.
   *         Pot estar buida si no hi ha animals disponibles en aquesta ubicació.
   * @see AnimalRepository#findNonAdoptedAnimalsByProtectoraCodiPostal(String)
   */
  public List<Animal>getAnimalsNoAdoptatsByCodiPostal(String codiPostal){
    return animalRepository.findNonAdoptedAnimalsByProtectoraCodiPostal(codiPostal);
  }

  /**
   * Retorna tots els animals que no estan adoptats i que pertanyen a protectores
   * ubicades en una localització específica (adresa, ciutat, etc.).
   * 
   * @param localitzacio la localització on es troben les protectores.
   * @return una llista d'animals no adoptats de protectores en la localització indicada.
   *         Pot estar buida si no hi ha animals disponibles en aquesta ubicació.
   * @see AnimalRepository#findNonAdoptedAnimalsByProtectoraLocalitzacio(String)
   */
  public List<Animal>getAnimalsNoAdoptatsByLocalitzacio(String localitzacio){
    return animalRepository.findNonAdoptedAnimalsByProtectoraLocalitzacio(localitzacio);
  }

  /**
   * Retorna tots els animals d'una espècie específica que no estan adoptats i que pertanyen a protectores
   * ubicades en una localització específica (adresa, ciutat, etc.).
   * 
   * @param especie el nom de l'espècie a cercar (ha de coincidir exactament amb el valor
   *                emmagatzemat a la base de dades, p. ex. "Gos", "Gat").
   * @param localitzacio la localització on es troben les protectores.
   * @return una llista d'animals no adoptats de l'espècie seleccionada en la localització indicada.
   *         Pot estar buida si no hi ha animals disponibles que compleixin aquests criteris.
   * @see AnimalRepository#findNonAdoptedAnimalsByEspecieAndLocalitzacio(String, String)
   */
  public List<Animal>getAnimalsByEspecieNoAdoptatsByLocalitzacio(String especie, String localitzacio){
    return animalRepository.findNonAdoptedAnimalsByEspecieAndLocalitzacio(especie, localitzacio);
  }

  /**
   * Retorna tots els animals que no pertanyen a les espècies "Gos" ni "Gat" i que no estan adoptats,
   * i que pertanyen a protectores ubicades en una localització específica (adresa, ciutat, etc.).
   * 
   * @param localitzacio la localització on es troben les protectores.
   * @return una llista d'animals d'espècies diferents a "Gos" i "Gat" i que estan en situació d'adopció
   *         en la localització indicada. Pot estar buida si no hi ha animals disponibles que compleixin aquests criteris.
   * @see AnimalRepository#findNonAdoptedExoticAnimalsByLocalitzacio(String)
   */
  public List<Animal>getAnimalsExcluirGosGatNoAdoptatsByLocalitzacio(String localitzacio){
    return animalRepository.findNonAdoptedExoticAnimalsByLocalitzacio(localitzacio);
  }

  /**
   * Mostra els detalls d'un animal pel seu identificador únic numèric,
   * incloent la informació de la protectora associada.
   * 
   * <p>Si l'ID proporcionat és {@code null}, es retorna un {@link Optional#empty()} 
   * per evitar excepcions.
   * 
   * @param numId l'identificador únic de l'animal a cercar.
   * @return un {@link Optional} que conté l'animal amb la seva protectora si es troba,
   *         o buit si no existeix o si l'ID és {@code null}.
   * 
   * @see AnimalRepository#findByIdWithProtectora(Long)
   */
  public Optional<Animal>getAnimalByIdWithProtectora(Long numId){
    if (numId == null) {
        return Optional.empty();
    }
    return animalRepository.findByIdWithProtectora(numId);
  }

  /**
   * Guarda un animal en el sistema.
   * 
   * <p>Si el animal ya tiene un {@code numId} válido y existe en la base de datos,
   * se actualizan los datos. Si no tiene {@code numId} o es nuevo,
   * se crea un animal nuevo con su identificador autogenerado.
   * 
   * <p>Este métode gestiona automáticamente la relación con la protectora
   * si se incluye el objeto {@link Protectora} completo dentro del animal.
   * 
   * @param animal entidad {@link Animal} con los datos a guardar.
   * @return {@link Animal} guardado, añadiendo el identificador autogenerado si es nuevo.
   */
  public Animal save(Animal animal){
      return animalRepository.save(animal);
  }

  /**
   * Elimina un animal del sistema por su identificador.
   * 
   * <p>No lanza ninguna excepción si el animal no existe (comportamiento por defecto
   * de {@link JpaRepository#deleteById}).
   * 
   * @param numId identificador del animal a eliminar.
   */
  public void deleteById(Long numId){
      animalRepository.deleteById(numId);
  }
}
