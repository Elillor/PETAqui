package com.buscadorpelut.Model;

import java.io.Serializable;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entitat JPA que representa a un animal a la base de dades.
 *
 * Mapeja la taula "animal" i conté els atributs persistents de l’animal.
 * Aquesta classe forma part del model de domini i s’utilitza tant en 
 * la capa de persistència i servei com en la capa de vista.
 */

@Entity
@Table(name = "animal")
public class Animal implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador únic autogenerat de l'animal.
     * Correspon a la columna "numId" a la base de dades.
     */
    @Id
    @Nonnull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numId")
    private long numId;

    
    @Nonnull
    @Column(name = "nomAn")
    private String nomAn; //Nom de l'animal. Camp obligatori.

    @Column(name = "edat")
    private int edat; //Edat de l'animal. 

    @Nonnull
    @Column(name = "sexe")
    private String sexe; //Sexe de l'animal. Camp obligatori. 

    @Nonnull
    @Column(name = "especie")
    private String especie; //Especie de l'animal. Camp obligatori.

    @Column(name = "numXip", unique = true)
    private int numXip; //Nº d'identificació oficial de l'animal.

    @Nonnull
    @Column(name = "adoptat")
    private boolean esAdoptat = false; //Booleà per indicar si l'animal està adoptat o no. Per defecte té valor false (no)

    @Nonnull
    @Column(name = "descripcio")
    private String descripcio; //Detalls i história de l'animal. Camp obligatori. 

    @Nonnull
    @Column(name= "fotoPerfil", unique = true)
    private String fotoPerfil;// URL d'on treiem la imatge de l'animal. Camp obligatori.

    /**
     * Clau foranea: codi protectora
     * Relació amb la protectora. 
     * Creem una entitat Protectora per poder consultar-ne dades (nom, web, etc.) més endavant.
     * De moment ho comento perquè no farem la taula de les protectores, ho deixo llest.
     */

    /*@ManyToOne
    @JoinColumn(name = "codiProt", referencedColumnName = "codiProt")
    private Protectora protectora;
    *
    //Constructors

    /**
     * Constructor per defecte requerit per JPA.
     */
    public Animal() {
    }

    /**
     * Constructor complet per crear una instancia d'Animal amb tots els atributs.
     * 
     * @param numId    ID de l'animal
     * @param nomAn     Nom de l'animal.
     * @param especie   Especie de l'animal
     * @param sexe      Sexe de l'animal
     * @param numXip    Número de xip (pot ser null)
     * @param edat      Edat de l'animal (pot ser null)
     * @param esAdoptat Si està adoptat (valor inicial = false)
     * @param descripcio Detalls de l'història de l'animal
     * @param fotoPerfil URL on es troba l'imatge de perfil
     * @param protectora Entitat d'una protectora on es troba l'animal. 
     */
    public Animal(Long numId, String nomAn, String sexe, int edat, String descripcio,int numXip, String especie, boolean esAdoptat, String fotoPerfil /*, Protectora protectora*/) {
        this.numId = numId;
        this.nomAn = nomAn;
        this.sexe = sexe;
        this.edat = edat;
        this.descripcio = descripcio;
        this.numXip = numXip;
        this.especie = especie;
        this.esAdoptat = esAdoptat;
        this.fotoPerfil = fotoPerfil;
        //this.protectora = protectora;
    }

//Getters i setters    
    /*De numId només fem get perquè és un valor autoincremental i no es modificarà
    mai des de el codi*/
    public Long getNumId() {
        return this.numId;
    }

    //Nom animal
    public String getNomAn() {
        return this.nomAn;
    }

    public void setNomAn(String nom) {
        this.nomAn = nom;
    }

    //Sexe de l'animal
    public String getSexe() {
        return this.sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    //Especie de l'animal
    public String getEspecie() {
        return this.especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    //Descripció de l'animal
    public String getDescripcio() {
        return this.descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    //Edat de l'animal
    public int getEdat() {
        return this.edat;
    }

    public void setEdat(int edat) {
        this.edat = edat;
    }

    //Número de xip
    public int getNumXip() {
        return this.numXip;
    }

    public void seNumXip(int numXip) {
        this.numXip = numXip;
    }

    /*És adoptat - si o no
    * Valor per defecte = false
    */
    public boolean getEsAdoptat(){
        return this.esAdoptat;
    }

    public void setEsAdoptat(Boolean esAdoptat){
        this.esAdoptat = esAdoptat;
    }

    //URL on es troba la imatge de l'animal
    public String getFotoPerfil () {
        return this.fotoPerfil;
    }

    public void setFotoPerfil (String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    /*
     * Protectora on està l'animal allotjat
     * Clau foranea
     * 
     * public Protectora getProtectora (){
     * return this.protectora;
     * }
     * 
     * public void setProtectora (Protectora protectora){
     * this.protectora = protectora;
     * }
     */
}
