package com.buscadorpelut.Model;

import java.io.Serializable;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entitat JPA que representa una protectora a la base de dades.
 *
 * Mapeja la taula "protectora" i conté els atributs persistents de la protectora.
 * Aquesta classe forma part del model de domini i s’utilitza tant en 
 * la capa de persistència i servei com en la capa de vista.
 */
@Entity
@Table(name = "protectora")
public class Protectora implements Serializable{
    // Atributs i mètodes de la classe Protectora
    private static final long serialVersionUID = 1L;

    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codiProt")
    private long codiProt; /**Identificador únic autogenerat de la protectora.*/

    @NonNull
    @Column(name = "nomProt")   
    private String nomProt; /**Nom de la protectora. Camp obligatori.*/

    @Column(name = "adresa")
    private String adresa; /**Adreça de la protectora.*/

    @Column(name ="codiPostal")
    private String codiPostal; /**Codi postal de la protectora.*/

    @Column(name = "localitat")
    private String localitat; /**Ciutat on es troba la protectora.*/

    @Column(name = "provincia")
    private String provincia; /**Provincia on es troba la protectora.*/

    @Column(name = "url")
    private String url; /**Pàgina web de la protectora.*/

    @Column(name="longitud")
    private double longitud; /**Longitud de la protectora.*/

    @Column(name="latitud")
    private double latitud; /**Latitud de la protectora.*/

    @Column(name = "tlfProt")
    private String tlfProt; /**Telèfon de la protectora.*/

    @Column(name = "emailProt")
    private String emailProt; /**Email de la protectora.*/

    /**Constructors*/
    public Protectora() {
        // Constructor per defecte
    }

    /**Constructor amb paràmetres
     * @param nomProt Identificador de la protectora
     * @param adresa Adreça de la protectora
     * @param codiPostal Codi postal de la protectora
     * @param localitat Localitat de la protectora
     * @param provincia Provincia de la protectora
     * @param url Pàgina web de la protectora
     * @param longitud Longitud geogràfica de la protectora
     * @param latitud Latitud geogràfica de la protectora
     * @param tlfProt Telèfon de la protectora
     * @param emailProt Email de la protectora
    */
    public Protectora(String nomProt, String adresa, String codiPostal, String localitat, String provincia,
            String url, double longitud, double latitud, String tlfProt, String emailProt) {
        this.nomProt = nomProt;
        this.adresa = adresa;
        this.codiPostal = codiPostal;
        this.localitat = localitat;
        this.provincia = provincia;
        this.url = url;
        this.longitud = longitud;
        this.latitud = latitud;
        this.tlfProt = tlfProt;
        this.emailProt = emailProt;
    }

    /**Getters i Setters*/

    //Codi protectora
    public long getCodiProt() {
        return codiProt;
    }

    public void setCodiProt(long codiProt) {
        this.codiProt = codiProt;
    }

    //Nom protectora
    public String getNomProt() {
        return nomProt;
    }

    public void setNomProt(String nomProt) {
        this.nomProt = nomProt;
    }

    //Adreça
    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    //Codi postal
    public String getCodiPostal() {
        return codiPostal;
    }

    public void setCodiPostal(String codiPostal) {
        this.codiPostal = codiPostal;
    }

    //Localitat
    public String getLocalitat() {
        return localitat;
    }

    public void setLocalitat(String localitat) {
        this.localitat = localitat;
    }

    //Provincia
    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    //URL
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;    
    }

    //Longitud
    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    //Latitud
    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    //Telèfon
    public String getTlfProt() {
        return tlfProt;
    }

    public void setTlfProt(String tlfProt) {
        this.tlfProt = tlfProt;
    }

    //Email
    public String getEmailProt() {
        return emailProt;
    }
    public void setEmailProt(String emailProt) {
        this.emailProt = emailProt;
    }
}